// CatfoOD 2009-10-26 ����09:15:11

package jym.sim.util;

import java.io.PrintStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools {
	
	public static final String start = 
		">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	public static final String end = 
		"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";

	private static volatile int sqlid = 0;
	private static int id = 0;
	private static PrintStream out = System.out;
	
	
	/**
	 * ���¶�λ�����,Ĭ��System.out;
	 */
	public static void setOut(PrintStream newout) {
		out = newout;
	}
	
	public static void nocatch(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-cache");
	}
	
	public static void setAjaxXmlHeader(HttpServletResponse resp) {
		nocatch(resp);
		resp.setContentType("text/xml");
	}
	
	public static String creatTagID(String tagname) {
		int _id;
		synchronized (Tools.class) {
			_id = id++;
		}
		return tagname + _id;
	}
	
	public static boolean isNull(String s) {
		return s==null || s.trim().length()==0;
	}

	/**
	 * ����name�ĵ�һ���ַ�
	 */
	public static String getFirstName(String name) {
		if (name.length()>0) {
			return name.substring(0, 1);
		}
		return name;
	}
	
	public static void pl(Object o) {
		out.println(o);
	}
	
	public static void p(Object o) {
		out.print(o);
	}
	
	public static void plsql(String sql) {
		out.println( String.format("sql(%1$#06x): %2$s", sqlid++, sql) );
	}
	
	public static void pl(Object ...o) {
		for (int i=0; i<o.length; ++i) {
			out.print(o[i]+" ");
		}
		out.println();
	}
	
	public static void plerr(Throwable t) {
		StringBuilder ps = new StringBuilder();
		StackTraceElement[] st = t.getStackTrace();
		
		ps.append(t.getClass());
		ps.append(':');
		ps.append(t.getMessage());
		ps.append("\n");
		
		for (int i=0; i<st.length; ++i) {
			ps.append("\t~: ");
			ps.append(st[i].getClassName());
			ps.append(" - ");
			ps.append(st[i].getMethodName());
			ps.append("() [ ");
			ps.append(st[i].getFileName());
			ps.append(" | ");
			ps.append(st[i].getLineNumber());
			ps.append(" ]\n");
		}
		
		pl(ps);
	}
	
	/**
	 * ���o==null, ���׳��쳣,�쳣��Ϣ��msg�ж���
	 * 
	 * @throws RuntimeException
	 */
	public static void check(Object o, String msg) {
		if (o==null) {
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * ����HttpServletRequest�е���������������б�
	 * �Ա�jsp��EL����
	 */
	public static void copyParam2Attrib(HttpServletRequest req) {
		Enumeration<?> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			Object value= req.getParameter(name);
			req.setAttribute(name, value);
		}
	}
	
	public static void start() {
		pl(start);
	}
	
	public static void end() {
		pl(end);
	}
}
