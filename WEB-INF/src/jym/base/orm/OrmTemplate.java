package jym.base.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jym.base.sql.ISql;
import jym.base.sql.JdbcTemplate;
import jym.base.sql.Logic;
import jym.base.util.BeanUtil;

/**
 * ���ݿ�ʵ��ӳ��ģ��
 */
public class OrmTemplate<T> implements ISelecter<T> {

	private JdbcTemplate jdbc;
	private Class<T> clazz;
	private IOrm<T> orm;
	private Plot<T> plot;
	
	
	/**
	 * jdbcģ�幹�캯��, ȫ��ʹ�ñ����ӳ��ʵ������
	 * 
	 * @param ds - ����Դ
	 * @param modelclass - ����ģ�͵�class��
	 * @param simSql - �����ʽ�Ĳ�ѯ���,��{@link jym.base.orm.IOrm}
	 */
	public OrmTemplate(DataSource ds, final Class<T> modelclass, final String simSql) {
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public String getSimSql() {
				return simSql;
			}

			public void mapping(IPlot plot) {
			}
		});
	}
	
	/**
	 * jdbcģ�幹�캯��,Ĭ��ÿ�����Ӳ����Զ��ر�����
	 * 
	 * @param orm - ���ݿ���������beanʵ������ӳ�����
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public OrmTemplate(DataSource ds, IOrm<T> orm) {
		jdbc = new JdbcTemplate(ds);
		this.orm = orm;
		
		init();
	}
	
	private void init() {
		clazz = orm.getModelClass();
		plot = new Plot<T>(orm);
		
		checkSql();
	}
	
	private void checkSql() {
		String simsql = orm.getSimSql();
		if ( simsql.indexOf("$where") < 0 ) {
			warnning("sql����в�����$where����");
		}
		if ( simsql.indexOf(" where ")>=0 ) {
			throw new IllegalArgumentException("sql����в��ܺ���where�Ӿ�");
		}
	}
	
	public List<T> select(T model, String join) {
		
		Method[] ms = model.getClass().getMethods();
		final StringBuilder where = new StringBuilder();
		
		boolean first = true;
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Object value = ms[i].invoke(model, new Object[0]);
					
					if ( BeanUtil.isValid(value) ) {
						if (first) {
							where.append(" where ");
							first = false;
						} else {
							where.append(join);
						}
						Logic logic = plot.getColumnLogic(colname);
						where.append( " (" ).append(colname).append(' ')
								.append(logic.in(value)).append( ") " );
					}
					
				} catch (Exception e) {
					warnning("invoke����: "+ e);
				}
			}
		}
		
		return select(where.toString());
	}
	
	public List<T> select(final String where) {
		final List<T> brs = new ArrayList<T>();
		
		jdbc.query(new ISql() {
			public void exception(Throwable tr, String msg) {
			}

			public void exe(Statement stm) throws Throwable {
				String sql = swapwhere(orm.getSimSql(), where);
				select( stm.executeQuery(sql), brs );
			}
		});
		
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
					plot.mapping(rsmd.getColumnLabel(i), i, rs, model);
				}
				brs.add(model);
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
	
	/**
	 * �滻sim��ʽsql�е�$whereΪָ����where�Ӿ�<br>
	 * ���where�ǿ��ַ���(trim().length()==0), �����where�Ӿ�<br>
	 * �������where�ַ���δ��'where'��ʼ���Զ����'where'�ַ���
	 */
	private String swapwhere(String simsql, String where) {
		where = where.trim();
		StringBuilder buff = new StringBuilder(where);
		
		if ( where.length()>0 && !where.startsWith("where") ) {
			buff.insert(0, "where ");
		}
		buff.insert(0, ' ');
		buff.append(' ');
		
		return simsql.replaceFirst("\\$where", buff.toString());
	}
	
	private void warnning(String msg) {
		System.out.println("����:(OrmTemplate): " + msg);
	}
	
}
