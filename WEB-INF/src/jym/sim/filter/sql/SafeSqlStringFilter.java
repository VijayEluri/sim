// CatfoOD 2010-8-19 ����11:18:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import jym.sim.filter.SimFilterException;

/**
 * ���ַ���ת��Ϊ������sql�в�������������ַ���
 */
public class SafeSqlStringFilter implements ISqlInputParamFilter<String> {
	
	private final static char TAG = '\'';
	
	public String see(String s) throws SimFilterException {
		if (s!=null) {
			// String.indexOf����ʹ���ڲ�����Ч�ʸ�
			// ��toCharArray������Ҫ����
			if (s.indexOf(TAG)>=0) {
				s = to(s);
			}
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
