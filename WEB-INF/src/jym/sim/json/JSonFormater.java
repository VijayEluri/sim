// CatfoOD 2010-7-29 ����09:19:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

/**
 * ��ʽ��Java�ַ���, ת�岻�ɼ��ַ�Ϊ�ı�<br>
 * ��ʵ�ֱ��� <b>String.replaceAll</b> ��4��
 */
public class JSonFormater {
	
	private static final String[] table;
	private static final int INDEX = 35;
	
	static {
		table = new String[INDEX];
		
		table['\n'] = "\\n";
		table['\r'] = "\\r";
		table['\"'] = "\\\"";
	}

	/**
	 * ��ʽ���ַ���
	 */
	public static String frm(String src) {
		if (src==null) return null;
		
		char[] cs = src.toCharArray();
		StringBuilder buff = new StringBuilder((int)(cs.length * 1.7));
		
		for (int i=0, size = cs.length; i<size; ++i) {
			char c = cs[i];
			
			if (c<INDEX && table[c]!=null) {
				buff.append(table[c]);
				continue;
			}
			buff.append(c);
		}
		
		return buff.toString();
	}
	
}
