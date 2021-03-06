// CatfoOD 2010-4-16 上午08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.SqlFormat;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;

public class JdbcTemplate implements IQuery, ICall {
	
	private final static ThreadLocal<JdbcSession> 
				db_connect = new ThreadLocal<JdbcSession>();

	private final ThreadLocal<IExceptionHandle> 
				handle = new ThreadLocal<IExceptionHandle>();
	
	private final static int ADD_BASE 	= 5;
	private static int maxSessCount 	= 20;
	private static int connectCount 	= 1;
	
	private boolean needformat 	= false;
	private boolean showsql 	= false;
	private boolean lazyMode 	= false;
	private boolean debug 		= false;
	private boolean convertSql	= false;
	private DataSource src;
	
	
	/**
	 * 用数据源初始化模板
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
	}
	
	/**
	 * 初始化session,默认的事务是基于线程的
	 */
	private JdbcSession initSession() throws SQLException {
		JdbcSession js = db_connect.get();
		if (js==null) {
			/* 已经在构造函数中操作db_connect */
			js = new JdbcSession();
		}
		return js;
	}
	
	public IJdbcSession getSession() {
		try {
			return initSession();
		} catch(SQLException e) {
			handleException(e);
		}

		return null;
	}
	
	/**
	 * 是否回显sql语句,默认不显示
	 * @param show - true显示
	 */
	public void showSql(boolean show) {
		showsql = show;
	}
	
	/**
	 * 如果设置为true,则所有的sql语句都会被转换为PreparedStatement
	 * 的方式调用,此时需要把sql语句进行转换,并且设置给语句中的参数绑定变量<br>
	 * 默认为false
	 * 
	 * @param use - true则使用PreparedStatement方式调用sql
	 */
	public void convertPreparedSql(boolean use) {
		convertSql = use;
	}
	
	/**
	 * 格式化sql的输出,默认不格式化
	 * @param need - true格式化
	 */
	public void needFormat(boolean need) {
		needformat = need;
	}
	
	public boolean isShowSql() {
		return showsql;
	}
	
	/**
	 * 设置关闭数据库连接的模式<br>
	 * 
	 * false - (默认的) 在执行完请求后立即关闭数据库连接<br>
	 * 
	 * true - 请求结束后会释放对数据库的引用,但是直到虚拟
	 * 机的垃圾回收进程启动才会关闭数据库连接,<b>该方法会占用
	 * 较多的数据库连接</b><br>
	 * 
	 * 注意如果当前的连接处于手动递交状态,则数据库绝不会关闭连接<br>
	 * 修改该设置,需要测试与数据库的兼容性
	 */
	public void setConnectMode(boolean lazy) {
		lazyMode = lazy;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * 用上下文中的数据源初始化模板
	 * 
	 * @param name - 要查询的数据源名称,如:<br>
	 * 				<code> "java:/comp/env/jdbc/ora_rmcsh" <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "JdbcTemplate:上下文未定义");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "数据源无效");
		src = ds;
	}

	public void query(final ISql sql) {
		query(new IResultSql() {
			public Object exe(Statement stm) throws Throwable {
				sql.exe(stm);
				return null;
			}
		});
	}
		
	public Object query(IResultSql sql) {
		if (convertSql) {
			try {
				return convertQuary(sql);
			} catch (SQLException e) {
				/* 是否应该再次用普通模式调用sql呢,如果抛出的异常属于业务上的错误
				 * 则不应该调用,但是暂时没有方法能有效的判断这种情况 */
				// Tools.pl("<<转换为预编译模式失败,转用普通模式调用SQL>>");
				return null;
			}
		}
		
		ProxyStatement proxy  = null;
		Statement st = null;
		JdbcSession js = null;
		Object result = null;
		
		try {
			UsedTime.start("执行sql");
			js = initSession();
			Connection conn = js.getConnection();
			st = conn.createStatement();
			proxy = getProxy(st);
			
			result = sql.exe(proxy);
			
			showSql(proxy);
		
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
				} catch (SQLException e) 
				{}
			}
			if (js!=null) {
				js.close();
			}
		}
		
		return result;
	}
	
	public void query(IPrepSql sql) {
		PreparedStatement st = null;
		JdbcSession js = null;
		
		try {
			UsedTime.start("执行sql");
			js = initSession();
			Connection conn = js.getConnection();
			st = conn.prepareStatement(sql.getSql());
			
			sql.exe(st);
		
		} catch (SQLException e) {
			String msg = e.getMessage();
			if (msg==null) {
				msg = "未知的sql异常";
			}
			Tools.p(msg.trim() + ": ");
			
			handleException(e);
			
		} catch (Throwable t) {
			t.printStackTrace();
			handleException(t);
			
		} finally {
			if (st!=null) {
				try {
					st.close();
				} catch (SQLException e) 
				{}
			}
			if (js!=null) {
				js.close();
			}
		}
	}
	
	public void call(ICallData cd) {
		StringBuilder buff = new StringBuilder();
		JdbcSession js = null;
		CallableStatement cs = null;
		
		try {
			js = initSession();
			Connection conn = js.getConnection();
			
			buff.append('{');
			if (cd.hasReturnValue()) {
				buff.append("?=");
			}
			buff.append(" call ");
			buff.append(cd.getProcedureName());
			
			int parmCount = cd.getParameterCount();
			if (parmCount>0) {
				buff.append(" (");
				for (int i=0; i<parmCount; ++i) {
					buff.append("?");
					if (i<parmCount-1) {
						buff.append(",");
					}
				}
				buff.append(" )");
			}
			buff.append('}');
			
			cs = conn.prepareCall(buff.toString());
			cs = getProxy(cs);
			
			cd.exe(cs);
			
		} catch (Throwable t) {
			Tools.pl("存储过程错误:" + buff.toString());
			handleException(t);
			t.printStackTrace();
			
		} finally {
			if (cs!=null) {
				try {
					cs.close();
				} catch (SQLException e) {}
			}
			if (js!=null) {
				js.close();
			}
		}
	}
	
	private Object convertQuary(IResultSql sql) throws SQLException {
		
		PreparedStatementHandler handler = new PreparedStatementHandler();
		
		ProxyStatement proxy = (ProxyStatement) Proxy.newProxyInstance(
								this.getClass().getClassLoader(), 
								new Class[]{ ProxyStatement.class },
								handler );
		
		Object result = null;
		boolean showTrandSql = showsql;
		
		try {
			UsedTime.start("执行sql");
			result = sql.exe(proxy);
			showSql(handler);
			
		} catch (SQLException e) {
			String msg = e.getMessage();
			if (msg==null) {
				msg = "未知的sql异常";
			}
			
			Tools.p(msg.trim() + ": ");
			Tools.plsql(handler.getSql());
			showTrandSql = true;
			
			handleException(e);
			throw e;
			
		} catch (Throwable t) {
			t.printStackTrace();
			handleException(t);
			
		} finally {
			if (showTrandSql) {
				Tools.p("[转换后的SQL]: ");
				handler.tp.debug();
			}
			try {
				handler.close();
			} catch (SQLException e) {
			}
		}
		
		return result;
	}
	
	private void showSql(IShowSql proxy) {
		if (showsql) {
			String sqls = proxy.getSql();
			if (needformat) {
				sqls = SqlFormat.format(sqls);
			}
			if (convertSql) {
				Tools.p("[--[转换为预编译模式]--]::");
			}
			Tools.plsql(sqls);
			UsedTime.endAndPrint();
		}
	}

	public void regExceptionHandle(IExceptionHandle eh) {
		handle.set(eh);
	}
	
	/**
	 * 数据库抛出的异常注入这个方法中，然后由注册过的异常处理器处理
	 */
	protected void handleException(Throwable t) {
		IExceptionHandle ie = handle.get();
		if (ie!=null) {
			ie.exception(t, t.getMessage());
		}
	}
	
	/**
	 * 取得一个Connection对象,返回的Connection必须自行管理
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return src.getConnection();
	}
	
	private ProxyStatement getProxy(Statement st) {
		return (ProxyStatement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
					new Class[]{ ProxyStatement.class },
						new StatementHandler(st) );
	}
	
	private CallableStatement getProxy(CallableStatement cst) {
		return (CallableStatement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
					new Class[]{ CallableStatement.class },
						new CallStatementHandler(cst) );
	}
	
	
	/**
	 * 实际是一个Statement接口<br>
	 * 包装PreparedStatement以便能传给query<br>
	 */
	public class PreparedStatementHandler implements InvocationHandler, IShowSql {
		private PreparedStatement ps;
		private JdbcSession js;
		private TransformPrep tp = new TransformPrep();
		private String sql;

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String mname = method.getName();

			if (mname.equals("executeQuery")) {
				doSql( (String) args[0] );
				return ps.getResultSet();
			}
			else if (mname.equals("executeUpdate")) {
				doSql( (String) args[0] );
				return ps.getUpdateCount();
			}
			else if (mname.equals("execute")) {
				doSql( (String) args[0] );
				return ps.getUpdateCount()<=0;
			}
			else if (mname.equals("getSql")) {
				return sql;
			}
			else if (mname.equals("close")) {
				/* 不执行关闭操作,必须等到外层调用结束才能关闭 */
				return null;
			}
			
			try {
				if (ps == null)
				throw new UnsupportedOperationException(
						"JdbcTemplate 使用了自动转换语法,不支持 " + method);
				
				return method.invoke(ps, args);
				
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
		
		/* 关闭PreparedStatement */
		public void close() throws SQLException {
			if (ps!=null) {
				ps.close();
			}
			if (js!=null) {
				js.close();
			}
		}
		
		private void doSql(String _sql) throws SQLException, Throwable {
			sql = _sql;
			tp.clear();
			tp.setSql(_sql);

			js = initSession();
			Connection conn = js.getConnection();
			ps = conn.prepareStatement(tp.getSql());
			tp.exe(ps);
		}
		
		/** 返回原始sql */
		public String getSql() {
			return sql;
		}
	}
	
	/**
	 * CallableStatement代理, 用于获取ResultSet并在CallableStatement关闭时关闭
	 */
	public class CallStatementHandler implements InvocationHandler {
		CallableStatement original;
		List<ResultSet> rslist;

		public CallStatementHandler(CallableStatement cs) {
			original = cs;
			rslist = new ArrayList<ResultSet>();
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) 
				throws Throwable 
		{
			String name = method.getName();
			
			if ( "close".equals(name) ) {
				closeResultSet();
			}
			
			Object result = null;
			try {
				result = method.invoke(original, args);
			} catch(InvocationTargetException e) {
				throw e.getCause();
			}
			
			if ( "getObject".equals(name) ) {
				if (result instanceof ResultSet) {
					rslist.add((ResultSet) result);
				}
			}
			
			return result;
		}
		
		private void closeResultSet() {
			Iterator<ResultSet> itr = rslist.iterator();
			while (itr.hasNext()) {
				try {
					itr.next().close();
				} catch (SQLException e) {
					Tools.pl("JdbcTemplate close resultSet: " + e);
				}
			}
			rslist.clear();
		}
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
				return sql;
			}
			
			try {
				return m.invoke(statement, ps);
				
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
	}
	
/*-------------------------------------------- 用来显示sql的接口 ----*/
	private interface IShowSql {
		String getSql();
	}
	private interface ProxyStatement extends Statement, IShowSql {
	}
/*-----------------------------------------------------------------*/


	/**
	 * 默认自动递交的事务
	 */
	public class JdbcSession implements IJdbcSession {
		private Connection conn;
		
		private JdbcSession() throws SQLException {
			getConnection();
			
			if (connectCount%maxSessCount==0 && connectCount>0) {
				Tools.pl(new Date() + " Jdbc使用的数据库连接: " + connectCount);
				maxSessCount += ADD_BASE;
			}
		}
		
		public Connection getConnection() throws SQLException {
			if (conn==null || conn.isClosed()) {
				conn = src.getConnection();
				++connectCount;
				
				if (debug) Tools.pl("create conn@" + conn.hashCode() 
						+ " sess@" + this.hashCode() + " C^" + connectCount);
			}
			return conn;
		}

		public boolean commit() {
			try {
				if (debug) Tools.pl("commit conn@" + conn.hashCode());
				conn.commit();
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public boolean isAutoCommit() {
			try {
				return conn.getAutoCommit();
			} catch (SQLException e) {
				handleException(e);
			}
			return true;
		}

		public boolean releaseSavepoint(Savepoint savepoint) {
			try {
				conn.releaseSavepoint(savepoint);
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public boolean rollback() {
			return rollback(null);
		}

		public boolean rollback(Savepoint savepoint) {
			try {
				if (savepoint==null) conn.rollback();
				else conn.rollback(savepoint);
				
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public void setCommit(boolean isAuto) {
			try {
				if (debug) Tools.pl("setCommit("+isAuto+") conn@" + conn.hashCode());
				
				db_connect.set(isAuto ? null : this);
				conn.setAutoCommit(isAuto);
			} catch (SQLException e) {
				handleException(e);
			}
		}

		public Savepoint setSavepoint() {
			try {
				return conn.setSavepoint();
			} catch (SQLException e) {
				handleException(e);
			}
			return null;
		}

		public Savepoint setSavepoint(String name) {
			try {
				return conn.setSavepoint(name);
			} catch (SQLException e) {
				handleException(e);
			}
			return null;
		}
		
		public void close() {
			if (conn!=null && isAutoCommit()) {
				/* 把JdbcSession从线程变量中移除,
				   直到没有其他引用时再释放连接 */
				db_connect.set(null);
				
				if (!lazyMode) {
					try {
						finalize();
					} catch (Throwable e) {
					}
				}
			}
		}
		
		protected void finalize() throws Throwable {			
			if (conn!=null) {
				if (debug) Tools.pl("close conn@" 
						+ conn.hashCode() + " sess@" + this.hashCode());
				
				conn.close();
				conn = null;
				--connectCount;
			}
		}
	}

}
