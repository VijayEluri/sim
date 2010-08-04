// CatfoOD 2010-8-4 ����11:05:53 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;


/**
 * ת���ַ���,��ֹsql���ע��
 */
public class SafeSql {
	
	private final static char TAG = '\'';
	
	public static final String transformValue(Object o) {
		String s = o.toString();
		// String.indexOf����ʹ���ڲ�����Ч�ʸ�
		if (s.indexOf(TAG)>=0) {
			s = to(s);
		}
		return s;
	}
	
	private static final String to(String a) {
		char[] ch = a.toCharArray();
		char[] nch = new char[ch.length * 2];
		int ni = 0;
		
		for (int i=0; i<ch.length; ++i) {
			if (ch[i]==TAG) {
				nch[ni++] = TAG;
			}
			nch[ni++] = ch[i];
		}
		
		return new String(nch, 0, ni);
	}
}
