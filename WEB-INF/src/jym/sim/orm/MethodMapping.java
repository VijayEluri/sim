package jym.sim.orm;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import jym.sim.filter.FilterPocket;
import jym.sim.sql.IWhere;
import jym.sim.sql.Logic;
import jym.sim.util.BeanUtil;

class MethodMapping {

	@SuppressWarnings("unchecked")
	private ISelecter 		objcrt;
	private Method 		method;
	private ITransition 	trans;
	private IWhere 		logic;
	private String 		pkmethod;
	private FilterPocket	outfilter;
	
	
	/**
	 * �׳��쳣,˵������������Ҫ��<br>
	 * if log==null log=Logic.EQ
	 */
	MethodMapping(Method md, ISelecter<?> is, String pk, IWhere log, FilterPocket _outfilter)	{

		logic 		= (log==null) ? Logic.EQ : log;
		objcrt 		= is;
		method 		= md; 
		pkmethod 	= (pk!=null) ? BeanUtil.getSetterName(pk) : null;
		outfilter	= _outfilter;
		
		Class<?>[] pt0 = md.getParameterTypes();
		if (pt0.length==1) {
			trans = getTransitionType(pt0[0]);
		} else {
			warnning(md.getName() + " ʵ�巽������������ƥ��");
		}
	}
	
	/** 
	 * ת���������ݿ��е����ݣ�setter��ʵ����
	 * model.setXxx(sqldata);
	 */
	public void invoke(ResultSet rs, int col, Object model) throws Exception {
		if (rs.getObject(col)!=null) {
			
			Object data = null;
			try {
				data =  trans.trans(rs, col);
			
			} catch (Exception e) {	
				warnning(model.getClass() + "ӳ����������������ݿ����Ͳ�ƥ��,����" 
						+ method + " ��ֱ��ʹ�����ݿ����Ͷ���");
				
				data = rs.getObject(col);
			}
			
			method.invoke(model, outfilter.filter(data) );
		}
	}
	
	public String getName() {
		return method.getName();
	}
	
	private ITransition getTransitionType(final Class<?> type) {
		
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
					warnning("���÷���" + method.getName() + "ʱ,��Ϊ��������" 
							+ type + "����,����û��(String)���캯��.(�Ƿ�ʹ���˷�������?)");
				}
				return null;
			}
		};
	}
	
	protected IWhere getColumnLogic() {
		return logic;
	}
	
	private void warnning(String msg) {
		System.out.println("����: (MethodMapping): " + msg);
	}
}
