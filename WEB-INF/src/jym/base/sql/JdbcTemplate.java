// CatfoOD 2010-4-16 ����08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.base.util.Tools;

public class JdbcTemplate implements IQuery {
	
	private DataSource src;
	private ThreadLocal<IExceptionHandle> handle;
	
	/**
	 * ������Դ��ʼ��ģ��
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
		handle = new ThreadLocal<IExceptionHandle>();
	}
	
	/**
	 * ���������е�����Դ��ʼ��ģ��
	 * 
	 * @param name - Ҫ��ѯ������Դ����,��:<br>
	 * 				<code> "java:/comp/env/jdbc/ora_rmcsh" <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "������δ����");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "����Դ��Ч");
		src = ds;
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
