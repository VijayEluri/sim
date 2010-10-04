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
 * 数据库实体检索模板
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
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param tablename - 数据库表名
	 * @param priKey - 主键名
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
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
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
	//	Tools.check(orm.getKey(), 			"getKey()不能返回null"			);
		Tools.check(orm.getModelClass(),	"getModelClass()不能返回null"	);
		Tools.check(orm.getTableName(),		"getTableName()不能返回null"		);
	}
	
	/**
	 * 取得输入数据过滤器设置器，通过FilterBase的实例可以插入新的过滤器<br>
	 * 过滤器的作用是在实体属性拼装为sql之前先转换属性的值
	 */
	public FilterPocket getInputParamPocket() {
		return infilter;
	}
	
	/**
	 * 取得数据库返回数据过滤器设置器，通过在FilterBase的实例可以插入新的过滤器<br>
	 * 过滤器的作用是把从数据库返回的数据，在传给实体属性前进行过滤
	 */
	public FilterPocket getOutputParamPocket() {
		return outfilter;
	}
	
	/**
	 * 为拼装sql时的实体属性有效检查配置策略,<br>
	 * 过滤器只要返回非null,则认为值有效<br>
	 * 有效的属性值将被用来拼装sql(增删改查),无效的值则会被忽略<br>
	 * 默认null值总是认为是无效的
	 */
	public FilterPocket getCheckVaildValue() {
		return vaildChecker;
	}
	
	/**
	 * 从普通对象转换为sql语句字符串
	 */
	protected final Object transformValue(Object o) {
		try {
			return infilter.filter(o);
		} catch (SimFilterException e) {
			warnning("输入参数过滤器转换失败:" + e);
			handleException(e);
		}
		return o;
	}
	
	/**
	 * 该值是否有效,有效性测试在getCheckVaildValue返回的对象中设置<br>
	 * null值总是认为是无效的
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
					Tools.check(model, "bean参数不能为null.");
					Object value = ms[i].invoke(model, new Object[0]);
					
					cv.set(colname, value);
					
				} catch (Exception e) {
					warnning("invoke错误: "+ e);
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
		
		return where.toString();
	}
	
	/**
	 * 此方法会把rs中所有的数据压入brs中并返回,在数据行很多时,内存溢出<br>
	 * 但该方法比动态取数据的方法快
	 */
	private void assembleBeanList(ResultSet rs, List<T> brs, PageBean pagedata) throws Exception {
		if (rs==null) return;
		
		try {
			String[] cols = getColumnNames(rs.getMetaData());
			
			if (rs.next()) {
				int total = 1;
			try { 
				// 没有TOTAL_COLUMN_NAME指定的列并不是错误
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
		throw new IndexOutOfBoundsException("不能有负值索引: " + rowNum);
		
		T model = clazz.newInstance();
		for (int i=1; i<=columnNames.length; ++i) {
			plot.mapping(columnNames[i-1], i, rs, model);
		}
		return model;
	}
	
	private void warnning(String msg) {
		System.out.println("警告:(SelectTemplate): " + msg);
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
	 * 返回结果集数据中，列名的数组
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
