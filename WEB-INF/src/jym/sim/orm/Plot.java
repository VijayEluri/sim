// CatfoOD 2010-4-16 ����09:38:08 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import jym.sim.exception.OrmException;
import jym.sim.filter.FilterPocket;
import jym.sim.sql.IOrder;
import jym.sim.sql.IWhere;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;


/**
 * ʵ�����������ݿ���ӳ�����ʵ��
 *
 * @param <T> - ʵ������
 */
class Plot<T> implements IPlot {
	
	private final static String PASS_COLM_NAME = "sim__";
	
	/** ʹ��Сд�Ƚ�String <������, ������װ>*/
	private Map<String, MethodMapping> ormmap;
	/** ʹ��Сд�Ƚ�String <����Сд��, ����>*/
	private Map<String, Method> classMethodmap;
	/** ��Сд����, <����, ����> */
	private Map<Method, String> reverse;
	
	private Method[] ms;
	private IOrm<T> orm;
	private boolean usecolnamemap = true;
	private Order order_sub = new Order();
	private FilterPocket outfilter;
	
	public Plot(IOrm<T> _orm, FilterPocket _outfilter) {
		orm = _orm;
		outfilter = _outfilter;
		initMethods();
		initOrm();
	}
	
	private void initMethods() {
		ms = orm.getModelClass().getMethods();
		classMethodmap = new HashMap<String, Method>();
		
		for (int i=0; i<ms.length; ++i) {
			// ʹ��Сд�Ƚ�
			String name = ms[i].getName().toLowerCase();
			
			Method m = classMethodmap.put(name, ms[i]);
			// ���m�ǿ�,������к�������
			if (m!=null) {
				Field f = BeanUtil.getMethodTargetField(m);
				if (f!=null) {
					Class<?>[] pts = m.getParameterTypes();
					if (pts.length==1) {
						if ( f.getType().equals(pts[0]) ) {
							classMethodmap.put(name, m);
						} 
						// else ���Ե������뷽�����Ͳ�ͬ
					} 
					// else �����Ĳ������Ϸ�
				}
			}
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		reverse = new HashMap<Method, String>();
		orm.mapping(this);
	}
	
	public void fieldPlot(String fn, String cn) {
		setMappingPlot(fn, cn, null, null, null);
	}

	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter, String pk) {
		setMappingPlot(fieldName, colname, getter, pk, null);
	}
	
	public void fieldPlot(String fieldName, String colname, IWhere log) {
		setMappingPlot(fieldName, colname, null, null, log);
	}
	
	protected void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		if (colname.startsWith(PASS_COLM_NAME)) {
			return;
		}
		
		MethodMapping md = null;

		// �Զ�ʹ�ñ����������ӳ��
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null, null, null);
			
		} else {
			md = ormmap.get(colname);
		}
		
		if (md!=null) {
			try {
				md.invoke(rs, colc, model);
				
			} catch(Exception e) {
				warnning(model.getClass(), "ִ�з��� (" 
						+ md.getName() + ") ʱ����: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			warnning(model.getClass(), colname+" ָ������������û��ӳ��");
		}
	}
	
	/**
	 * ���filedname�����Ͳ��Ǽ�����,��ʹ��sql����<br>
	 * sql����Ϊnull
	 */
	protected MethodMapping setMappingPlot(
			String fieldname, String colname, ISelecter<?> is, String pk, IWhere log) {

		Method setm = getMethod( BeanUtil.getSetterName(fieldname) );
		Method getm = getMethod( BeanUtil.getGetterName(fieldname) );
		MethodMapping mm = null;
		
		try {
			if (setm==null) {
				throw new OrmException("û��setter����");
			}
			mm = new MethodMapping(setm, is, pk, log, outfilter);
			// ormmap.set �Ĳ�����ΪСд
			ormmap.put(colname.toLowerCase(), mm);
			reverse.put(getm, colname);
			
		} catch (OrmException e) {
			warnning(orm.getModelClass(),
					"ӳ������(" + fieldname + ")ʱ����: " + e.getMessage());
		}
		
		return mm;
	}
	
	private Method getMethod(String methodname) {
		return classMethodmap.get(methodname.toLowerCase());
	}
	
	protected Method[] getAllMethod() {
		return ms;
	}
	
	/**
	 * ȡ��ָ���еıȽϷ�ʽ, ��Сд������
	 */
	public IWhere getColumnLogic(String colname) {
		IWhere log = null;
		MethodMapping mm = ormmap.get(colname.toLowerCase());
		if (mm!=null) {
			log = mm.getColumnLogic();
		}
		return log;
	}
	
	protected void stopColnameMapping() {
		usecolnamemap = false;
	}
	
	/**
	 * ȡ��ʵ��get�����Ա�����ӳ��, ���ִ�Сд
	 */
	protected String getColname(Method m) {
		return reverse.get(m);
	}
	
	private void warnning(Class<?> beanClass, String msg) {
		Tools.pl("����:(Plot): (" + beanClass +") " + msg);
	}

	public IOrder order() {
		return order_sub;
	}
	
	
	private class Order implements IOrder {
		private StringBuilder out;
		private boolean isFirst = false;
		
		private Order() {
			this(new StringBuilder());
			isFirst = true;
		}
		
		private Order(StringBuilder _out) {
			out = _out;
		}

		public IOrder asc(String columnName) {
			return set(columnName, "asc");
		}

		public IOrder desc(String columnName) {
			return set(columnName, "desc");
		}

		private IOrder set(String cn, String o) {
			Tools.check(cn, "�������������Ϊnull");
		if (isFirst) {
				out.append(" ORDER BY ");
				isFirst = false;
			} else {
				out.append(',');
			}
			out.append(cn);
			out.append(' ');
			out.append(o);
			return this;
		}
		
		public String toString() {
			return out.toString();
		}
	}
}
