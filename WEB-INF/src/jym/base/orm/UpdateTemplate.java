// CatfoOD 2010-4-16 下午04:49:18 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

import java.sql.Statement;

import javax.sql.DataSource;

import jym.base.sql.ISql;
import jym.base.util.Tools;

public class UpdateTemplate<T> extends SelectTemplate<T> implements IUpdate<T> {
	
	private final static String INSERT = "INSERT INTO ";
	private final static String DELETE = "DELETE FROM ";
	private final static String UPDATE = "UPDATE ";
	

	public UpdateTemplate(DataSource ds, Class<T> modelclass, String tablename, String key) {
		super(ds, modelclass, tablename, key);
	}

	public UpdateTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
	}

	public boolean add(T model) {
		final StringBuilder sql = new StringBuilder(INSERT);
		sql.append(getOrm().getTableName());

		final StringBuilder columns = new StringBuilder();
		final StringBuilder values  = new StringBuilder();
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			public void set(String column, Object value) {
				if (first) first = false;
				else {
					columns.append(',');
					values.append(',');
				}
				columns.append(column);
				values.append('\'').append(value).append('\'');
			}			
		});
		
		sql.append('(').append(columns).append(')');
		sql.append("VALUES");
		sql.append('(').append(values).append(')');
		
		final Refer result = new Refer();
		
		super.query(new ISql() {
			public void exception(Throwable tr, String msg) {
				warnning("插入对象错误: " + msg);
			}

			public void exe(Statement stm) throws Throwable {
				result.b = stm.execute(sql.toString());
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
			
			public void set(String column, Object value) {
				if (first) {
					first = false;
				} else {
					sql.append(" AND ");
				}
				sql.append(column).append('=');
				sql.append('\'').append(value).append('\'');
			}			
		});
		
		final Refer result = new Refer();
		
		super.query(new ISql() {
			public void exception(Throwable tr, String msg) {
				warnning("删除对象错误: " + msg);
			}

			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}

	public int update(T model) {
		final String pkey = getOrm().getKey();
		final Refer result = new Refer();
		
		final StringBuilder sql = new StringBuilder(UPDATE);
		sql.append(getOrm().getTableName()).append(" SET ");
		
		loopMethod2Colume(model, new IColumnValue() {
			boolean first = true;
			
			public void set(String column, Object value) {
				if ( !column.equalsIgnoreCase(pkey) ) {
					if (first) {
						first = false;
					} else {
						sql.append(" , ");
					}
					sql.append(column).append('=');
					sql.append('\'').append(value).append('\'');
					
				} else {
					result.value = value;
				}
			}			
		});
		
		Tools.check(result.value, "请检查IOrm.getKey()方法是否返回正确的列名");
		
		sql.append(" WHERE ").append( pkey )
				.append("= '").append( result.value ).append("'");
		
		super.query(new ISql() {
			public void exception(Throwable tr, String msg) {
				warnning("删除对象错误: " + msg);
			}

			public void exe(Statement stm) throws Throwable {
				result.i = stm.executeUpdate(sql.toString());
			}
		});
		
		return result.i;
	}
	
	private void warnning(String msg) {
		System.out.println("警告:(UpdataTemplate): " + msg);
	}

	private class Refer {
		private boolean b = false;
		private int i = 0;
		private Object value = null;
	}
}
