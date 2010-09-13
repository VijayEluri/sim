package jym.sim.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jym.sim.filter.FilterPocket;
import jym.sim.filter.SimFilterException;
import jym.sim.orm.page.IPage;
import jym.sim.orm.page.NotPagination;
import jym.sim.orm.page.PageBean;
import jym.sim.sql.IQuery;
import jym.sim.sql.ISql;
import jym.sim.sql.IWhere;
import jym.sim.sql.JdbcTemplate;
import jym.sim.util.Tools;

/**
 * ���ݿ�ʵ�����ģ��
 */
public class SelectTemplate<T> extends JdbcTemplate implements ISelecter<T>, IQuery {

	private Class<T> clazz;
	private IOrm<T> orm;
	private Plot<T> plot;
	private IPage pagePlot;
	private FilterPocket infilter;
	private FilterPocket outfilter;
	private CheckVaildValue vaildChecker;
	
	
	/**
	 * jdbcģ�幹�캯��, ȫ��ʹ�ñ����ӳ��ʵ������
	 * 
	 * @param ds - ����Դ
	 * @param modelclass - ����ģ�͵�class��
	 * @param tablename - ���ݿ����
	 * @param priKey - ������
	 */
	public SelectTemplate(DataSource ds, final Class<T> modelclass, 
			final String tablename, final String priKey) {
		
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public void mapping(IPlot plot) {
			}

			public String getTableName() {
				return tablename;
			}

			public String getKey() {
				return priKey;
			}
		});
	}
	
	/**
	 * jdbcģ�幹�캯��,Ĭ��ÿ�����Ӳ����Զ��ر�����
	 * 
	 * @param orm - ���ݿ���������beanʵ������ӳ�����
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public SelectTemplate(DataSource ds, IOrm<T> orm) {
		super(ds);
		this.orm = orm;
		pagePlot = new NotPagination();
		
		check();
		init();
	}
	
	private void init() {
		vaildChecker = new CheckVaildValue();
		infilter = new FilterPocket();
		outfilter = new FilterPocket();
		clazz = orm.getModelClass();
		plot = new Plot<T>(orm, outfilter);
	}
	
	private void check() {
	//	Tools.check(orm.getKey(), 			"getKey()���ܷ���null"			);
		Tools.check(orm.getModelClass(),	"getModelClass()���ܷ���null"	);
		Tools.check(orm.getTableName(),		"getTableName()���ܷ���null"		);
	}
	
	/**
	 * ȡ���������ݹ�������������ͨ��FilterBase��ʵ�����Բ����µĹ�����<br>
	 * ����������������ʵ������ƴװΪsql֮ǰ��ת�����Ե�ֵ
	 */
	public FilterPocket getInputParamPocket() {
		return infilter;
	}
	
	/**
	 * ȡ�����ݿⷵ�����ݹ�������������ͨ����FilterBase��ʵ�����Բ����µĹ�����<br>
	 * �������������ǰѴ����ݿⷵ�ص����ݣ��ڴ���ʵ������ǰ���й���
	 */
	public FilterPocket getOutputParamPocket() {
		return outfilter;
	}
	
	/**
	 * Ϊƴװsqlʱ��ʵ��������Ч������ò���,<br>
	 * ������ֻҪ���ط�null,����Ϊֵ��Ч<br>
	 * ��Ч������ֵ��������ƴװsql(��ɾ�Ĳ�),��Ч��ֵ��ᱻ����<br>
	 * Ĭ��nullֵ������Ϊ����Ч��
	 */
	public FilterPocket getCheckVaildValue() {
		return vaildChecker;
	}
	
	/**
	 * ����ͨ����ת��Ϊsql����ַ���
	 */
	protected final Object transformValue(Object o) {
		try {
			return infilter.filter(o);
		} catch (SimFilterException e) {
			warnning("�������������ת��ʧ��:" + e);
			handleException(e);
		}
		return o;
	}
	
	/**
	 * ��ֵ�Ƿ���Ч,��Ч�Բ�����getCheckVaildValue���صĶ���������<br>
	 * nullֵ������Ϊ����Ч��
	 */
	protected final boolean isValid(Object value) {
		return vaildChecker.isValid(value);
	}
	
	protected void loopMethod2Colume(T model, IColumnValue cv) {
		Method[] ms = plot.getAllMethod();
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Tools.check(model, "bean��������Ϊnull.");
					Object value = ms[i].invoke(model, new Object[0]);
					
					cv.set(colname, value);
					
				} catch (Exception e) {
					warnning("invoke����: "+ e);
					Tools.plerr(e, "jym.*");
				}
			}
		}
	}
	
	public List<T> select(T model, String join) {
		PageBean p = new PageBean();
		p.setCurrent(1);
		p.setOnesize(Integer.MAX_VALUE);
		p.setTotal(1);
		return select(model, join, p);
	}
	
	public List<T> select(final T model, final String join, final PageBean pagedata) {
		Tools.check(pagedata, "��ҳ���ݲ���Ϊnull");
		final StringBuilder where = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;

			public void set(String column, Object value) {
				IWhere logic = plot.getColumnLogic(column);
				
				if (logic instanceof ISkipValueCheck || isValid(value) ) {
					value = logic.w(column, transformValue( value ), model);

					if (value!=null) {
						if (first) {
							where.append("where");
							first = false;
						} else {
							where.append(join);
						}
						where.append(" (" ).append( value ).append( ") " );
					}
				}
			}
		});
		
		return select(
				pagePlot.select(orm.getTableName(), where.toString(), plot.getOrder(), pagedata),
				pagedata
				);
	}
	
	private List<T> select(final String sql, final PageBean pagedata) {
		final List<T> brs = new ArrayList<T>();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				select( stm.executeQuery(sql), brs, pagedata );
			}
		});
		
		return brs;
	}
	
	private void select(ResultSet rs, List<T> brs, PageBean pagedata) throws Exception {
		if (rs==null) return;
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int col = rsmd.getColumnCount();
			
			if (rs.next()) {
				int total = 1;
			try { 
				// û��TOTAL_COLUMN_NAMEָ�����в����Ǵ���
				total = rs.getInt(IPage.TOTAL_COLUMN_NAME);
				}
			catch(Exception e) {}
				
				pagedata.setTotalRow(total);
				
				do {
					T model = clazz.newInstance();
					
					for (int i=1; i<=col; ++i) {
						// ormmap.setʱ�Ѿ���ΪСд
						plot.mapping(rsmd.getColumnLabel(i), i, rs, model);
					}
					brs.add(model);
				} while ( rs.next() );
			}
			
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			plot.stopColnameMapping();
		}
	}
	
	private void warnning(String msg) {
		System.out.println("����:(SelectTemplate): " + msg);
	}
	
	public void setPaginationPlot(IPage plot) {
		if (plot!=null) {
			pagePlot = plot;
		}
	}

	public Class<T> getModelClass() {
		return orm.getModelClass();
	}
	
	protected IOrm<T> getOrm() {
		return orm;
	}
	
	protected Plot<T> getPlot() {
		return plot;
	}
	
}
