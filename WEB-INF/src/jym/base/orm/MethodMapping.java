package jym.base.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import jym.base.sql.Logic;
import jym.base.util.BeanUtil;

class MethodMapping {

	@SuppressWarnings("unchecked")
	private ISelecter objcrt;
	private Method m;
	private ITransition it;
	private Logic logic;
	private String pkmethod;
	
	/**
	 * �׳��쳣,˵������������Ҫ��<br>
	 * if log==null log=Logic.EQ
	 */
	MethodMapping(Method md, ISelecter<?> is, String pk, Logic log) 
		throws SecurityException, IllegalArgumentException, NoSuchMethodException, 
		InstantiationException, IllegalAccessException, InvocationTargetException 
	{
		objcrt 		= is;
		m 			= md; 
		logic 		= (log==null) ? Logic.EQ : log;
		pkmethod 	= (pk!=null) ? BeanUtil.getSetterName(pk) : null;
		
		Class<?>[] pt0 = md.getParameterTypes();
		if (pt0.length==1) {
			it = getTransitionType(pt0[0]);
		} else {
			warnning(md.getName() + " ʵ�巽������������ƥ��");
		}
	}
	
	/** 
	 * ת���������ݿ��е����ݣ�setter��ʵ����
	 * model.setXxx(sqldata);
	 */
	public void invoke(ResultSet rs, int col, Object model)
		throws IllegalArgumentException, IllegalAccessException, 
		InvocationTargetException, SQLException
	{
		Object data = it.trans(rs, col);
		m.invoke(model, data);
	}
	
	public String getName() {
		return m.getName();
	}
	
	private ITransition getTransitionType(final Class<?> type) 
	throws SecurityException, NoSuchMethodException, IllegalArgumentException, 
		InstantiationException,	IllegalAccessException, InvocationTargetException 
	{
		ITransition it;
		
		if (String.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getString(col);
				}
			};
		}
		else if (Integer.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getInt(col);
				}
			};
		}
		else if (Double.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getDouble(col);
				}
			};
		}
		else if (Float.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getFloat(col);
				}
			};
		}
		else if (Long.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getLong(col);
				}
			};
		}
		else if (Short.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getShort(col);
				}
			};
		}
		else if (Date.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getDate(col);
				}
			};
		}
		else if (BigDecimal.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return new BigDecimal(rs.getString(col));
				}
			};
		}
		else if (Collection.class.isAssignableFrom(type)) {
			it = getCollection(type);
		}
		else {
			it = getObjTransition(type);
		}
		
		return it;
	}
	
	private ITransition getCollection(final Class<?> type) {
		return new ITransition() {

			@SuppressWarnings("unchecked")
			public Object trans(ResultSet rs, int col) throws SQLException {
				if (objcrt != null) {
					try {
						Object pkobj = BeanUtil.creatBean(objcrt.getModelClass());
						Object param = rs.getObject(col);
						BeanUtil.invoke(pkobj, pkmethod, param);
						
						return objcrt.select(pkobj, "and");
						
					} catch (Exception e) {
						warnning("���ӳ�����:" + e.getMessage());
					}
				}
				else {
					warnning("���ʵ��δӳ��.");
				}
				return null;
			}
			
		};
	}
	
	private ITransition getObjTransition(final Class<?> type) {
		return new ITransition() {
			public Object trans(ResultSet rs, int col) throws SQLException {
				try {
					return BeanUtil.creatBean(type, rs.getObject(col));
					
				} catch (Exception e) {
					warnning(type + "����û��(String)���캯��." + e.getMessage());
				}
				return null;
			}
		};
	}
	
	protected Logic getColumnLogic() {
		return logic;
	}
	
	private void warnning(String msg) {
		System.out.println("����: (MethodMapping): " + msg);
	}
}
