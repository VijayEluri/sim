// CatfoOD 2010-4-16 ����09:38:08 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import jym.base.sql.Logic;
import jym.base.util.BeanUtil;


/**
 * ʵ�����������ݿ���ӳ�����ʵ��
 *
 * @param <T> - ʵ������
 */
class Plot<T> implements IPlot {	
	
	/** ʹ��Сд�Ƚ�String <������, ������װ>*/
	private Map<String, MethodMapping> ormmap;
	/** ʹ��Сд�Ƚ�String <����Сд��, ����>*/
	private Map<String, Method> classMethodmap;
	/** ��Сд����, <����, ����> */
	private Map<Method, String> reverse;
	
	private IOrm<T> orm;
	private boolean usecolnamemap = true;
	
	
	public Plot(IOrm<T> _orm) {
		orm = _orm;
		initMethods();
		initOrm();
	}
	
	private void initMethods() {
		Method[] ms = orm.getModelClass().getMethods();
		classMethodmap = new HashMap<String, Method>();
		for (int i=0; i<ms.length; ++i) {
			// ʹ��Сд�Ƚ�
			classMethodmap.put(ms[i].getName().toLowerCase(), ms[i]);
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		reverse = new HashMap<Method, String>();
		orm.mapping(this);
	}
	
	public void fieldPlot(String fn, String cn) {
		setMappingPlot(fn, cn, null, null);
	}

	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter) {
		setMappingPlot(fieldName, colname, getter, null);
	}
	
	public void fieldPlot(String fieldName, String colname, Logic log) {
		setMappingPlot(fieldName, colname, null, log);
	}
	
	protected void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		MethodMapping md = null;

		// �Զ�ʹ�ñ����������ӳ��
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null, null);
			
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
	protected MethodMapping setMappingPlot(
			String fieldname, String colname, ISelecter<?> is, Logic log) {

		Method setm = getMethod( BeanUtil.getSetterName(fieldname) );
		Method getm = getMethod( BeanUtil.getGetterName(fieldname) );
		MethodMapping mm = null;
		
		try {
			mm = new MethodMapping(setm, is, log);
			// ormmap.set �Ĳ�����ΪСд
			ormmap.put(colname.toLowerCase(), mm);
			reverse.put(getm, colname);
			
		} catch (Exception e) {
			warnning("����(" + setm.getName() + ")��Ч: " + e.getMessage());
		}
		
		return mm;
	}
	
	private Method getMethod(String methodname) {
		return classMethodmap.get(methodname.toLowerCase());
	}
	
	/**
	 * ȡ��ָ���еıȽϷ�ʽ, ��Сд������
	 */
	public Logic getColumnLogic(String colname) {
		Logic log = null;
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
	 * ȡ��ʵ��get�����Ա�����ӳ��
	 */
	protected String getColname(Method m) {
		return reverse.get(m);
	}
	
	private void warnning(String msg) {
		System.out.println("����:(Plot): " + msg);
	}
	
}
