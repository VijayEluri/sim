// CatfoOD 2010-5-11 ����12:39:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jym.sim.util.Tools;

public class SelectTag extends TagSupport {

	private static final long serialVersionUID = 1535321924476706984L;
	
	private Map<String, String> enumMap;
	
	private String errorMsg = null;
	private String sname;
	private String cname;
	private String fname;
	/** ���ֵ�Ǽ��ϵ�����,ָ�������е��ĸ�Ԫ����Ĭ��ѡ�� */
	private String value;
	private JspWriter out;
	private boolean empty = false;
	private String def;
	
	
	@SuppressWarnings("unchecked")
	protected void init() {
		out = super.pageContext.getOut();
		
		try {
			Class<?> clazz = Class.forName(cname);
			Field field = clazz.getField(fname);
			Object value = field.get(null);
			
			if (value!=null) {
				if ( value instanceof String[] ) {
					array2map((String[]) value);
				}
				else if ( value instanceof Map<?,?> ) {
					enumMap = (Map<String, String>) value;
				}
				else {
					error(getEnumObjName() 
							+ "�������Ͳ�����Ч��ö������-(String[], Map)");
				}
			} else {
				error(getEnumObjName() + "����ֵΪnull");
			}
			
		} catch (ClassNotFoundException e) {
			error(cname + "������Ч��������");
			
		} catch (SecurityException e) {
			error(getEnumObjName() + "��ȡ����ʱ����" + e.getMessage());
			
		} catch (NoSuchFieldException e) {
			error(getEnumObjName() + "������Ч��������");
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			error(getEnumObjName() + "���ɷ���");
			
		} catch (NullPointerException  e) {
			error(getEnumObjName() + "���Ǿ�̬�ֶ�");
		}
	}
	
	private void array2map(String[] arr) {
		enumMap = new LinkedHashMap<String, String>();
		for (int i=0; i<arr.length; ++i) {
			enumMap.put(Integer.toString(i), arr[i]);
		}
	}
	
	public int doStartTag() throws JspException {
		init();
		
		try {
			out.print("<select name='" + sname + "' ");
			if (def!=null) {
				out.print(def);
			}
			out.print(">");
			
			if (errorMsg==null) {
				printSelect();
			} else {
				printError();
			}
			out.print("</select>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	private void printSelect() throws IOException {
		if (!empty) {
			out.print("<option selected disabled>��ѡ��</option>");
		}
		
		Iterator<String> it = enumMap.keySet().iterator();
		
		while (it.hasNext()) {
			String k = it.next();
			String v = enumMap.get(k);
			
			out.print("<option ");
			if (k.equals(value)) {
				out.print("selected ");
			}
			out.print("value=");
			out.print("'");
			out.print(k);
			out.print("'>");
			out.print(v);
			out.print("</option>");
		}
	}
	
	private void printError() throws IOException {
		out.print("<option value='NaN'>");
		out.print(errorMsg);
		out.print("</option>");
	}
	
	public void setName(String name) {
		sname = name;
	}
	
	public void setClazz(String name) {
		cname = name;
	}
	
	public void setField(String name) {
		fname = name;
	}
	
	/** ö�ٶ�������������� */
	public String getEnumObjName() {
		return cname + '.' + fname;
	}
	
	public void setValue(String v) {
		value = v;
	}
	
	private void error(String s) {
		Tools.pl("SelectTag: " + s);
		errorMsg = s;
	}

	/**
	 * ����ö�ٶ���,û�з���null
	 */
	public Map<String, String> getEnumMap() {
		return enumMap;
	}
	
	protected void print(Object o) throws IOException {
		out.print(o);
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public void setDef(String def) {
		this.def = def;
	}
}
