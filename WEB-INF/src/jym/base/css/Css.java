// CatfoOD 2009-12-21 ����08:45:13

package jym.base.css;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.base.tags.IPrinter;

/**
 * ��ʽ���е�һ����ʽ
 */
public class Css implements IPrinter {
	
	private final static String ST = "{";
	private final static String ED = "}";
	private final static String AT = ";";
	private final static String TO = ":";
	
	private String sename;
	private Map<String, String> smap;
	
	/**
	 * �½�һ��css��ʽ
	 * @param selecterName - cssѡ����
	 */
	public Css(String selecterName) {
		sename = selecterName;
		smap = new HashMap<String, String>();
	}
	
	/**
	 * ����ʽ�������һ����ʽ����
	 * 
	 * @param name - ��������
	 * @param value - ����ֵ������;������
	 * @return
	 */
	public Css addStyle(String name, String value) {
		smap.put(name, value);
		return this;
	}

	public void printout(PrintWriter out) {
		out.print(sename);
		out.print(ST);
		
		Iterator<String> names = smap.keySet().iterator();
		while (names.hasNext()) {
			String name = names.next();
			out.print(name);
			out.print(TO);
			out.print(smap.get(name));
			out.print(AT);
		}
		out.println(ED);
	}
}
