// CatfoOD 2010-4-16 ����04:49:18 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.Statement;

import javax.sql.DataSource;

import jym.sim.sql.ISql;
import jym.sim.util.Tools;

public class UpdateTemplate<T> extends SelectTemplate<T> implements IUpdate<T> {
	
	private final static String INSERT = "INSERT INTO ";
	private final static String DELETE = "DELETE FROM ";
	private final static String UPDATE = "UPDATE ";
	private final String pk;

	public UpdateTemplate(DataSource ds, Class<T> modelclass, String tablename, String key) {
		super(ds, modelclass, tablename, key);
		pk = key;
	}

	public UpdateTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
		pk = orm.getKey();
	}

	public boolean add(T model) {
		final StringBuilder sql = new StringBuilder(INSERT);
		sql.append(getOrm().getTableName());

		final StringBuilder columns = new StringBuilder();
		final StringBuilder values  = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			public void set(String column, Object value, Class<?> valueType) {
				if ( isValid(value, valueType) ) {
					if (first) first = false;
					else {
						columns.append(',');
						values.append(',');
					}
					columns.append(column);
					values.append('\'').append(transformValue(value)).append('\'');
				}
			}			
		});
		
		sql.append('(').append(columns).append(')');
		sql.append("VALUES");
		sql.append('(').append(values).append(')');
		
		final Refer result = new Refer();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.b = stm.executeUpdate(sql.toString()) > 0;
			}
		});
		
		return result.b;
	}

	public int delete(T model) {
		final StringBuilder sql = new StringBuilder(DELETE);
		sql.append(getOrm().getTableName());
		sql.append(" WHERE ");
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			
			public void set(String column, Object value, Class<?> valueType) {
				if ( isValid(value, valueType) ) {
					if (first) {
						first = false;
					} else {
						sql.append(" AND ");
					}
					sql.append(column).append('=');
					sql.append('\'').append(transformValue(value)).append('\'');
				}
			}			
		});
		
		final Refer result = new Refer();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}

	public int update(final T model) {
		Tools.check(pk, "IOrm.getKey()����null, update����ִ��.");
		
		final Refer result = new Refer();
		
		final StringBuilder sql = new StringBuilder(UPDATE);
		sql.append(getOrm().getTableName()).append(" SET ");
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			
			public void set(String column, Object value, Class<?> valueType) {
				if ( isValid(value, valueType) ) {
					if ( !column.equalsIgnoreCase(pk) ) {
						if (first) {
							first = false;
						} else {
							sql.append(" , ");
						}
						sql.append(column).append('=');
						sql.append('\'').append(transformValue(value)).append('\'');
						
					} else {
						result.value = value;
					}
				}
			}
		});
		
		Tools.check(result.value, "����IOrm.getKey()�����Ƿ񷵻���ȷ������:" + pk
				+ " �����������Ƿ񷵻�nullֵ:" + model.getClass() );
		
		sql.append(" WHERE ").append( pk )
				.append("= '").append( result.value ).append("'");
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}
	
//	private void warnning(String msg) {
//		System.out.println("����:(UpdataTemplate): " + msg);
//	}

	private class Refer {
		public static final int ERROR = -1;
		
		private boolean b = false;
		private int i = ERROR;
		private Object value = null;
	}
}
