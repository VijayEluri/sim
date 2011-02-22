// CatfoOD 2009-10-28 ����09:16:22

package jym.sim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BeanUtil<TYPE> {
	
	
	public static String firstUp(String s) {
		char[] cs = s.toCharArray();
		cs[0] = Character.toUpperCase(cs[0]);
		return new String(cs);
	}
	
	/**
	 * ���fieldnameΪnull, �׳��쳣
	 */
	public static String getSetterName(String fieldname) {
		return getXetName(fieldname, "set");
	}
	
	/**
	 * ���fieldnameΪnull, �׳��쳣
	 */
	public static String getGetterName(String fieldname) {
		return getXetName(fieldname, "get");
	}
	
	private static String getXetName(String fieldname, String xxx) {
		char[] fns = fieldname.toCharArray();
		StringBuilder buff = new StringBuilder();
		buff.append(xxx);
		buff.append( Character.toTitleCase(fns[0]) );
		buff.append(fns, 1, fns.length-1);
		return buff.toString();
	}
	
	public static Method[] getGetterMethods(Class<?> clazz) {
		ArrayList<Method> ms = new ArrayList<Method>();
		Method[] all = clazz.getMethods();
		for (int i=0; i<all.length; ++i) {
			String mname = all[i].getName();
			if (mname.startsWith("get")) {
				ms.add(all[i]);
			}
		}
		return ms.toArray(new Method[0]);
	}
	
	/**
	 * ����clazz��Ķ���,params�ǹ��캯���Ĳ���
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public static Object creatBean(Class<?> clazz, Object...params) 
			throws SecurityException, NoSuchMethodException, 
			IllegalArgumentException, InstantiationException, 
			IllegalAccessException, InvocationTargetException  {
		
		Class<?>[] cls = new Class[params.length];
		for (int i=0; i<cls.length; ++i) {
			cls[i] = params[i].getClass();
		}
		Constructor<?> cons = clazz.getConstructor(cls);
		return cons.newInstance(params);
	}
	
	/**
	 * ִ��targer�����method����, ������params
	 */
	public static Object invoke(Object target, String method, Object...params) 
	throws Exception, NoSuchMethodException {
		
		Method m = getMethod(target, method, params);
		return m.invoke(target, params);
	}
	
	/**
	 * ȡ��targer�����method����, ������params
	 */
	public static Method getMethod(Object target, String method, Object...params) 
	throws SecurityException, NoSuchMethodException {
		
		Class<?> clazz = target.getClass();
		Class<?>[] cls = null;
		
		if (params!=null) {
			cls = new Class[params.length];
			for (int i=0; i<cls.length; ++i) {
				cls[i] = params[i].getClass();
			}
		}
		
		return clazz.getMethod(method, cls);
	}
	
	/**
	 * ����obj�Ƿ���Ч
	 * 
	 * @param obj 
	 * @return ���obj��Ϊnull, �������obj��Number������>=0, 
	 * 			����obj��String���ͳ���>0 �򷵻�true, ���򷵻�false;
	 */
	public static boolean isValid(Object obj) {
		boolean r = false;
		if (obj!=null) {
			r = true;
			if (obj instanceof Number) {
				Number num = (Number) obj;
				r = num.doubleValue()>=0;
			}
			if (obj instanceof String) {
				String str = obj.toString();
				r = str.trim().length()>0;
			}
		}
		
		return r;
	}
	
	/**
	 * ȡ��get/set������Ӧ������,���Ե����ͺʹ����������Ĳ������Ϳ��ܲ�ͬ
	 * 
	 * @param m - ����
	 * @return ����,û�з���null
	 */
	public static Field getMethodTargetField(Method m) {
		String name = m.getName();
		Field f = null;
		
		if (name.length()>3 && (name.startsWith("set") || name.startsWith("get")) ) {
			char[] ch = name.toCharArray();
			ch[3] = Character.toLowerCase(ch[3]);
			name = new String(ch, 3, ch.length-3);
			try {
				f = m.getDeclaringClass().getField(name);
			} catch (Exception e) {
			}
		}
		return f;
	}
	
	/**
	 * ͨ��bean����fieldname���Զ�Ӧ��getter����ȡ�ø����Ե�ֵ
	 * 
	 * @param bean - ���ݶ���, ���Ϊnull, ���׳��쳣
	 * @param fieldname - ������, ���Ϊnull, �򷵻�null
	 * @return ���Ե�ֵ
	 * 
	 * @throws Exception - �����������Ĵ���
	 * @throws NoSuchMethodException - �Ҳ������Զ�Ӧ��getter����
	 */
	public static Object getFieldValue(Object bean, String fieldname) 
	throws Exception, NoSuchMethodException {
		
		Object value = null;
		
		if (fieldname!=null) {
			String fieldGetMethod = getGetterName(fieldname);
			if (fieldGetMethod!=null) {
				value = invoke(bean, fieldGetMethod, new Object[0]);	
			}
		}
		return value;
	}
}
