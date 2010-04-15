package jym.base.orm;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jym.base.util.BeanUtil;

/**
 * ���ݿ�ʵ��ӳ��ģ��
 */
public class JdbcTemplate<T> implements ISelecter<T> {
	
	private DataSource dsrc;
	private Connection conn;
	private PreparedStatement ps;
	private IOrm<T> orm;
	private Class<T> clazz;
	private boolean usecolnamemap = true;
	private boolean autocloseconn = false;
	private Plot plot;
	
	/** ʹ��Сд�Ƚ�String <������, ������װ>*/
	private Map<String, MethodMapping> ormmap;
	/** ʹ��Сд�Ƚ�String <����Сд��, ����>*/
	private Map<String, Method> classMethodmap;
	/** ��Сд����, <����, ����> */
	private Map<Method, String> reverse;
	
	
	/**
	 * jdbcģ�幹�캯��, autoClose()��������Ч, ���Ӳ��ᱻ�ر�
	 * 
	 * @param conn - ���ݿ�����,autoClose()������Ч
	 * @param orm - ���ݿ���������beanʵ������ӳ�����
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public JdbcTemplate(Connection conn, IOrm<T> orm) throws SQLException {
		this.conn = conn;
		this.orm = orm;
		dsrc = null;
		
		createConn();
		init();
	}
	
	/**
	 * jdbcģ�幹�캯��,Ĭ��ÿ�����Ӳ����Զ��ر�����
	 * 
	 * @param ds - ����Դ
	 * @param orm - ���ݿ���������beanʵ������ӳ�����
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public JdbcTemplate(DataSource ds, IOrm<T> orm) {
		dsrc = ds;
		this.orm = orm;
		
		init();
	}
	
	private void init() {
		clazz = orm.getModelClass();
		plot = new Plot();
		
		initMethods();
		initOrm();
	}
	
	/**
	 * jdbcģ�幹�캯��, ȫ��ʹ�ñ����ӳ��ʵ������
	 * 
	 * @param conn - ���ݿ�����
	 * @param modelclass - ����ģ�͵�class��
	 * @param preSql - ��ѯ���
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public JdbcTemplate(Connection conn, final Class<T> modelclass, final String preSql) 
	throws SQLException {
		
		this(conn, new IOrm<T>() {
			public Class<T> getModelClass() {
				return modelclass;
			}
			public String getPrepareSql() {
				return preSql;
			}
			public void mapping(IPlot plot) {
			}
		});
	}
	
	/**
	 * jdbcģ�幹�캯��, ȫ��ʹ�ñ����ӳ��ʵ������
	 * 
	 * @param ds - ����Դ
	 * @param modelclass - ����ģ�͵�class��
	 * @param preSql - ��ѯ���
	 */
	public JdbcTemplate(DataSource ds, final Class<T> modelclass, final String preSql) {
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public String getPrepareSql() {
				return preSql;
			}

			public void mapping(IPlot plot) {
			}
		});
	}
	
	private void initMethods() {
		Method[] ms = clazz.getMethods();
		classMethodmap = new HashMap<String, Method>();
		for (int i=0; i<ms.length; ++i) {
			// ʹ��Сд�Ƚ�
			classMethodmap.put(ms[i].getName().toLowerCase(), ms[i]);
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		reverse = new HashMap<Method, String>();
		orm.mapping(plot);
	}

	private void createConn() throws SQLException {
		if (conn==null || conn.isClosed()) { //  || !conn.isValid(6) java5 ��֧��
			if (dsrc!=null) {
				conn = dsrc.getConnection();
				ps = conn.prepareStatement(orm.getPrepareSql());
			} else {
				throw new SQLException("�����ݿ�������Ѿ���ʧ");
			}
		}
	}
	
	public List<T> select(T model, String join) {
		List<T> brs = new ArrayList<T>();
		
		Method[] ms = plot.getClass().getMethods();
		StringBuilder where = new StringBuilder(" where ");
		Statement st = null;
		
		try {
			st = conn.createStatement();
			boolean first = true;
			
			for (int i=0; i<ms.length; ++i) {
				String colname = reverse.get(ms[i]);
				
				if (colname!=null) {
					Object value = ms[i].invoke(model, new Object[0]);
					
					if ( BeanUtil.isValid(value) ) {
						if (first) {
							where.append(join);
						} else {
							first = false;
						}
						
						where.append(" (" + colname + "= '" + value + "') ");
					}
				}
			}
			
			String sql = nowhere(orm.getPrepareSql()) + where;
			ResultSet rs = st.executeQuery(sql);
			select(rs, brs);
			
		} catch (Exception e) {
			warnning("select����: " + e);
		} finally {
			if (st!=null)
				try {
					st.close();
				} catch (SQLException e) {
				}
		}
		
		return brs;
	}
	
	public List<T> select(Object ...params) {
		List<T> brs = new ArrayList<T>();
		try {
			createConn();
			for (int i=1; i<=params.length; ++i) {
				ps.setObject(i, params[i-1]);
			}
			
			select(ps.executeQuery(), brs);
			
		} catch (Exception e) {
			warnning("select����: " + e.getMessage());
		}
		
		return brs;
	}
	
	private void select(ResultSet rs, List<T> brs) throws Exception {
		if (rs==null) return;
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int col = rsmd.getColumnCount();
			
			while ( rs.next() ) {
				T model = clazz.newInstance();
				
				for (int i=1; i<=col; ++i) {
					// ormmap.setʱ�Ѿ���ΪСд
					mapping(rsmd.getColumnLabel(i), i, rs, model);
				}
				brs.add(model);
			}
			
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			// ���dsrc!=null���Դ�����Դ���»�ȡ����,������ͷ�����
			if (dsrc!=null && autocloseconn) {
				close();
			}
			usecolnamemap = false;
		}
	}
	
	private void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		MethodMapping md = null;

		// �Զ�ʹ�ñ����������ӳ��
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null);
		} else {
			md = ormmap.get(colname);
		}
		
		if (md!=null) {
			try {
				md.invoke(rs, colc, model);
				
			} catch(Exception e) {
				warnning("ִ�з��� (" + md.getName() + ") ʱ����: " + e.getMessage());
			}
		} else {
			warnning(colname+" ָ����������û��ӳ��");
		}
	}
	
	
	/**
	 * ���filedname�����Ͳ��Ǽ�����,��ʹ��sql����<br>
	 * sql����Ϊnull
	 */
	private MethodMapping setMappingPlot(String filedname, String colname, ISelecter<?> is) {
		String methodName = BeanUtil.getSetterName(filedname);
		Method m = getSetterMethod(methodName);
		MethodMapping mm = null;
		
		try {
			mm = new MethodMapping(m, is);
			// ormmap.set �Ĳ�����ΪСд
			ormmap.put(colname.toLowerCase(), mm);
			reverse.put(m, colname);
			
		} catch (Exception e) {
			warnning("����(" + m.getName() + ")��Ч: " + e.getMessage());
		}
		
		return mm;
	}
	
	private Method getSetterMethod(String methodname) {
		return classMethodmap.get(methodname.toLowerCase());
	}
	
	/**
	 * ���ز�����where�Ӿ��sql���
	 */
	private String nowhere(String s) {
		int i = s.toLowerCase().indexOf("where");
		if (i>0) {
			return s.substring(0, i);
		}
		return s;
	}

	/**
	 * �ر�Connection����,ͬʱPreparedStatementҲ���ر�,<br>
	 * ����Ƶ��ִ�����ݿ�����Ĳ�����Ӧ���ر�����<br>
	 * <b>���δʹ������Դ���캯��,�رպ󱾶����ܼ���ʹ��</b>
	 */
	public void close() {
		try {
			if (conn!=null)
				conn.close();
		} catch (SQLException e) {
		}
		conn = null;
	}
	
	/**
	 * ÿ�β�ѯִ����Ϻ��Ƿ��Զ��ر�����<br>
	 * �������Ϊtrue��PreparedStatement�Ż���Ч<br>
	 * Ĭ��ÿ�����Ӳ����Զ��ر�����<br>
	 * <br>
	 * <b>����Ƶ��ִ�����ݿ�����Ĳ�����Ӧ���ر�����</b>
	 */
	public void autoClose(boolean ac) {
		autocloseconn = ac;
	}
	
	private class Plot implements IPlot {
		public void fieldPlot(String fn, String cn) {
			setMappingPlot(fn, cn, null);
		}

		@Override
		public void fieldPlot(String fieldName, String colname, ISelecter<?> getter) {
			//throw new UnsupportedOperationException("�ݲ�֧���������");
			setMappingPlot(fieldName, colname, getter);
		}
	}
	
	interface ITransition {
		Object trans(ResultSet rs, int col) throws SQLException;
	}
	
	private void warnning(String msg) {
		System.out.println("����:(JdbcTemplate): " + msg);
	}
	
}
