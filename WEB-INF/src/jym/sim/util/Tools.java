// CatfoOD 2009-10-26 ����09:15:11

package jym.sim.util;

import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools {
	
	public static final String start 
		= ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	public static final String end 
		= "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";

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
	
	/**
	 * ��ӡ����,���������
	 * @param t - Ҫ��ӡ���쳣
	 */
	public static void plerr(Throwable t) {
		plerr(t, null);
	}
	
	/**
	 * ��ӡ���󲢹������
	 * @param t - Ҫ��ӡ�Ĵ���
	 * @param filterExp - ������ʽ,�ñ��ʽ��������,
	 * 			�������ӡ,������� ������ʽ������ƥ��ȫ��
	 */
	public static void plerr(Throwable t, String filterExp) {
		StringBuilder ps = new StringBuilder();
		StackTraceElement[] st = t.getStackTrace();
		
		Pattern p = null; 
		try {
			if (filterExp!=null) {
			p = Pattern.compile(filterExp);
			}
		} catch(Exception e) {}
		
		ps.append(t.getClass());
		ps.append(':');
		ps.append(t.getMessage());
		ps.append("\n");
		
		boolean isSkip = false;
		for (int i=0; i<st.length; ++i) {
			if ( p==null ||  p.matcher(st[i].getClassName()).find() ) {
				ps.append("\t~: ");
				ps.append(st[i].getClassName());
				ps.append(" - ");
				ps.append(st[i].getMethodName());
				ps.append("() [ ");
				ps.append(st[i].getFileName());
				ps.append(" | ");
				ps.append(st[i].getLineNumber());
				ps.append(" ]\n");
				isSkip = false;
			} else {
				if (!isSkip) {
					ps.append("\t~: ... [ overlook ]\n");
				}
				isSkip = true;
			}
		}
		
		pl(ps);
	}
	
	/**
	 * ��ӡ��ǰ���̵ĵ��ö�ջ
	 */
	public static void plStackTrace() {
		Tools.plerr(new Throwable("���ö�ջ"));
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
	
	/**
	 * ����һ����Ƶ����
	 */
	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	/**
	 * ����ָ�������飬��ȡ���� 0 ��䣨���б�Ҫ������ʹ��������ָ���ĳ��ȡ�
	 * ������ԭ����͸����ж���Ч���������������������齫������ͬ��ֵ��
	 * �����ڸ�������Ч����ԭ������Ч���������������������� (byte)0��
	 * ���ҽ���ָ�����ȴ���ԭ����ĳ���ʱ����Щ�������ڡ�<br>
	 * <br>
	 * �˷���Ϊjava1.5�ṩ��ݵ����鸴��
	 */
	public static byte[] copyOf(byte[] original, int newLength) {
		byte[] newarr = new byte[newLength];
		System.arraycopy(original, 0, newarr, 0, original.length);
		
		return newarr;
	}
	
	/**
	 * �滻ch���������е�s�ַ�Ϊr�ַ�
	 */
	public static void replaceAll(char[] ch, char s, char r) {
		for (int i=0; i<ch.length; ++i) {
			if (ch[i]==s) {
				ch[i] = r;
			}
		}
	}
	
}
