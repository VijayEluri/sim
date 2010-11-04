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
import jym.sim.sql.ResultSetList;
import jym.sim.util.Tools;

/**
 * ���ݿ�ʵ�����ģ��
 */
public class SelectTemplate<T> extends JdbcTemplate 
implements ISelecter<T>, IQuery, ResultSetList.IGetBean<T> {

	private final static NotPagination NOPAGE_PLOT = new NotPagination();
	
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
	 * Ĭ��nullֵ������Ϊ����Ч��(��������������͵Ĺ�����)
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
	protected final boolean isValid(Object value, Class<?> valueType) {
		return vaildChecker.isValid(value, valueType);
	}
	
	protected void loopMethod2Colume(T model, IColumnValue cv) {
		Method[] ms = plot.getAllMethod();
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Tools.check(model, "bean��������Ϊnull.");
					Object value = ms[i].invoke(model, new Object[0]);
					cv.set(colname, value, ms[i].getReturnType());
					
				} catch (Exception e) {
					warnning("invoke����: "+ e);
					Tools.plerr(e, "jym.*");
				}
			}
		}
	}
	
	public List<T> select(T model, String join) {
		
		String where = getWhereSub(model, join);
		String sql = NOPAGE_PLOT.select(orm.getTableName(), 
				where, plot.order().toString(), null);
		
		try {
			if (super.isShowSql()) Tools.plsql(sql);
			return new ResultSetList<T>(sql, this, this);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<T> select(T model, String join, final PageBean pagedata) {
		
		pagedata.getCurrent();
		
		String where = getWhereSub(model, join);
		final String sql = pagePlot.select(orm.getTableName(), 
					where, plot.order().toString(), pagedata);
		
		final List<T> brs = new ArrayList<T>();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				assembleBeanList( stm.executeQuery(sql), brs, pagedata );
			}
		});
		
		return brs;
	}
	
	private String getWhereSub(final T model, final String join) {
		
		final StringBuilder where = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;

			public void set(String column, Object value, Class<?> valueType) {
				IWhere logic = plot.getColumnLogic(column);
				
				if (logic instanceof ISkipValueCheck || isValid(value, valueType) ) {
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
		
		return where.toString();
	}
	
	/**
	 * �˷������rs�����е�����ѹ��brs�в�����,�������кܶ�ʱ,�ڴ����<br>
	 * ���÷����ȶ�̬ȡ���ݵķ�����
	 */
	private void assembleBeanList(ResultSet rs, List<T> brs, PageBean pagedata) throws Exception {
		if (rs==null) return;
		
		try {
			String[] cols = getColumnNames(rs.getMetaData());
			
			if (rs.next()) {
				int total = 1;
			try { 
				// û��TOTAL_COLUMN_NAMEָ�����в����Ǵ���
				total = rs.getInt(IPage.TOTAL_COLUMN_NAME);
				}
			catch(Exception e) {}
				
				pagedata.setTotalRow(total);
				
				do {
					T model = fromRowData(cols, rs, 0);
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
	
	public T fromRowData(String[] columnNames, ResultSet rs, int rowNum) 
	throws Exception {
		
	if (rowNum<0) 
		throw new IndexOutOfBoundsException("�����и�ֵ����: " + rowNum);
		
		T model = clazz.newInstance();
		for (int i=1; i<=columnNames.length; ++i) {
			plot.mapping(columnNames[i-1], i, rs, model);
		}
		return model;
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
	
	/**
	 * ���ؽ���������У�����������
	 * @throws SQLException 
	 */
	public static final String[] getColumnNames(ResultSetMetaData rsmd) 
	throws SQLException {
		int columnCount = rsmd.getColumnCount();
		
		String[] columnNames = new String[columnCount];
		for (int i=0; i<columnCount; ++i) {
			columnNames[i] = rsmd.getColumnLabel(i+1);
		}
		return columnNames;
	}
}
