// CatfoOD 2010-4-16 ����08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.Tools;

public class JdbcTemplate implements IQuery, ICall {
	
	private static ThreadLocal<JdbcSession> db_connect = new ThreadLocal<JdbcSession>();
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	private SimpleDateFormat sqlDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	private boolean showsql = false;
	private DataSource src;
	private ThreadLocal<IExceptionHandle> handle;
	
	/**
	 * ������Դ��ʼ��ģ��
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
		handle = new ThreadLocal<IExceptionHandle>();
	}
	
	private JdbcSession initSession() throws SQLException {
		JdbcSession js = db_connect.get();
		if (js==null) {
			js = new JdbcSession();
			db_connect.set(js);
		}
		return js;
	}
	
	public IJdbcSession getSession() throws SQLException {
		return initSession();
	}
	
	/**
	 * �Ƿ����sql���,Ĭ�ϲ���ʾ
	 * @param show - true��ʾ
	 */
	public void showSql(boolean show) {
		showsql = show;
	}
	
	/**
	 * ���������е�����Դ��ʼ��ģ��
	 * 
	 * @param name - Ҫ��ѯ������Դ����,��:<br>
	 * 				<code> "java:/comp/env/jdbc/ora_rmcsh" <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "JdbcTemplate:������δ����");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "����Դ��Ч");
		src = ds;
	}
	
	/**
	 * �������ݿ����ڸ�ʽĬ�� "yyyy-MM-dd"
	 * 
	 */
	public void setDateFormat(String format) {
		sqlDateFormat = new SimpleDateFormat(format);
	}
	
	/**
	 * ����ͨ����ת��Ϊsql����ַ���
	 */
	protected Object transformValue(Object o) {
		if (o instanceof Date) {
			Date d = (Date)o;
			o = sqlDateFormat.format(d);
		} else {
			o = SafeSql.transformValue(o);
		}
		return o;
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
		
		ProxyStatement proxy  = null;
		Statement st = null;
		JdbcSession js = null;
		Object result = null;
		
		try {
			js = initSession();
			Connection conn = js.getConnection();
			st = conn.createStatement();
			proxy = getProxy(st);
			
			result = sql.exe(proxy);
			
			if (showsql) {
				Tools.plsql(proxy.getSql());
			}
		
		} catch (SQLException e) {
			String msg = e.getMessage();
			if (msg==null) {
				msg = "δ֪��sql�쳣";
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
			if (js!=null) {
				js.close();
			}
		}
		
		return result;
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
			
			cd.exe(cs);
			
		} catch (Throwable t) {
			Tools.pl("�洢���̴���:" + buff.toString());
			t.printStackTrace();
			handleException(t);
			
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

	public void regExceptionHandle(IExceptionHandle eh) {
		handle.set(eh);
	}
	
	private void handleException(Throwable t) {
		IExceptionHandle ie = handle.get();
		if (ie!=null) {
			ie.exception(t, t.getMessage());
			// t.printStackTrace();
		}
	}
	
	private ProxyStatement getProxy(Statement st) {
		return (ProxyStatement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
					new Class[]{ ProxyStatement.class },
						new StatementHandler(st) );
	}
	
	/**
	 * Statement����,���ڽ�ȡSql
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
	
	/**
	 * Ĭ���Զ��ݽ�������
	 */
	private class JdbcSession implements IJdbcSession {
		private Connection conn;
		
		private JdbcSession() throws SQLException {
			getConnection();
		}
		
		public Connection getConnection() throws SQLException {
			if (conn==null || conn.isClosed()) {
				conn = src.getConnection();
			}
			return conn;
		}

		public boolean commit() {
			try {
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
			// �رպ�,JdbcSession��ִ��sqlǰ�ᱻ����,��ʱconn���ر�,�ᵼ���쳣
//			if (isAutoCommit()) {
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					handleException(e);
//				}
//			}
		}
	}

}
