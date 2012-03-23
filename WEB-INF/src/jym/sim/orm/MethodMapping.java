package jym.sim.orm;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import jym.sim.filter.FilterPocket;
import jym.sim.util.BeanUtil;

/**
 * 保存实体属性与数据库之间映射的关系，和列的转换相关策略
 */
class MethodMapping {

	@SuppressWarnings({ "unchecked" })
	private ISelecter		objcrt;
	private Method			method;
	private ITransition	trans;
	private String 		pkmethod;
	private FilterPocket	outfilter;
	private LogicPackage	logics;
	
	
	/**
	 * 抛出异常,说明方法不符合要求<br>
	 * if (log==null) log=Logic.EQ
	 */
	MethodMapping(Method md, ISelecter<?> is, String pk, ISqlLogic[] log, FilterPocket _outfilter)	{
		
		logics		= new LogicPackage(log);
		objcrt 		= is;
		method 		= md; 
		pkmethod 	= (pk!=null) ? BeanUtil.getSetterName(pk) : null;
		outfilter	= _outfilter;
		
		Class<?>[] pt0 = md.getParameterTypes();
		
		if (pt0.length==1) {
			trans = getTransitionType(pt0[0]);
		} else {
			warnning(md.getName() + " 实体方法参数数量不匹配");
		}
	}
	
	/** 
	 * 转化来自数据库中的数据，setter到实体中
	 * model.setXxx(sqldata);
	 */
	public void invoke(ResultSet rs, int col, Object model) throws Exception {
		Object data = null;
		try {
			data = trans.trans(rs, col);
		
		} catch (Exception e) {
			e.printStackTrace();
			warnning(method + "与数据库类型不匹配(原因:"
							+ e.getMessage()
							+ "),已经设置为null");
		}
		
		/*XXX outfilter 按策略转换数据库中的值到实体属性中 */
		method.invoke(model, outfilter.filter(data) );
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
		else if (Timestamp.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return rs.getTimestamp(col);
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
					return rs.getBigDecimal(col);
				}
			};
		}
		else if (UUID.class.isAssignableFrom(type)) {
			it = new ITransition() {
				public Object trans(ResultSet rs, int col) throws SQLException {
					return UUID.fromString(rs.getString(col));
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
						warnning("外键映射错误:" + e.getMessage());
					}
				}
				else {
					warnning("外键实体未映射.");
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
					warnning("调用方法" + method.getName() + "时,作为参数创建" 
							+ type + "类型,该类没有(String)构造函数.(是否使用了方法重载?)");
				}
				return null;
			}
		};
	}
	
	protected LogicPackage getLogicPackage() {
		return logics;
	}
	
	private void warnning(String msg) {
		System.out.println("警告: (MethodMapping): " + msg);
	}
	
}
