// CatfoOD 2010-4-16 上午08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.Tools;

public class JdbcTemplate implements IQuery {
	
	protected final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private SimpleDateFormat sqlDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	
	private boolean showsql = false;
	private DataSource src;
	private ThreadLocal<IExceptionHandle> handle;
	
	/**
	 * 用数据源初始化模板
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
		handle = new ThreadLocal<IExceptionHandle>();
	}
	
	/**
	 * 是否回显sql语句,默认不显示
	 * @param show - true显示
	 */
	public void showSql(boolean show) {
		showsql = show;
	}
	
	/**
	 * 用上下文中的数据源初始化模板
	 * 
	 * @param name - 要查询的数据源名称,如:<br>
	 * 				<code> "java:/comp/env/jdbc/ora_rmcsh" <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "上下文未定义");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "数据源无效");
		src = ds;
	}
	
	/**
	 * 设置数据库日期格式默认 "yyyy-MM-dd"
	 * 
	 */
	public void setDateFormat(String format) {
		sqlDateFormat = new SimpleDateFormat(format);
	}
	
	/**
	 * 从普通对象转换为sql语句字符串
	 */
	protected Object transformValue(Object o) {
		if (o instanceof Date) {
			Date d = (Date)o;
			o = sqlDateFormat.format(d);
		} 
		return o;
	}

	public void query(ISql sql) {
		
		ProxyStatement proxy  = null;
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = src.getConnection();
			st = conn.createStatement();
			proxy = getProxy(st);
			sql.exe(proxy);
			
			if (showsql) {
				Tools.plsql(proxy.getSql());
			}
		
		} catch (SQLException e) {
			String msg = e.getMessage();
			if (msg==null) {
				msg = "未知的sql异常";
			}
			Tools.p(msg.trim() + ": ");
			
			if (proxy!=null) {
				Tools.plsql(proxy.getSql());
			} else {
				Tools.pl("unknow sql.");
			}
			handleException(e);
			
		} catch (Throwable t) {
			t.printStackTrace();
			handleException(t);
			
		} finally {
			if (st!=null) {
				try {
					st.close();
				} catch (SQLException e) {}
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (Exception e) {};
			}
		}
	}

	public void regExceptionHandle(IExceptionHandle eh) {
		handle.set(eh);
	}
	
	private void handleException(Throwable t) {
		IExceptionHandle ie = handle.get();
		if (ie!=null) {
			ie.exception(t, t.getMessage());
		}
	}
	
	private ProxyStatement getProxy(Statement st) {
		return (ProxyStatement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
					new Class[]{ ProxyStatement.class },
						new StatementHandler(st) );
	}
	
	/**
	 * Statement代理,用于截取Sql
	 */
	public class StatementHandler implements InvocationHandler {
		private Statement statement;
		private String sql;
		
		public StatementHandler(Statement st) {
			statement = st;
		}

		public Object invoke(Object st, Method m, Object[] ps) 
		throws Throwable, SQLException
		{
			String mname = m.getName();

			if (mname.equals("executeQuery")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("executeUpdate")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("execute")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("getSql")) {
				return getSql();
			}
			
			try {
				return m.invoke(statement, ps);
				
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
		
		private String getSql() {
			return sql;
		}
	}
	
	private interface ProxyStatement extends Statement {
		public String getSql();
	}
	
}
