// CatfoOD 2010-10-16 ����08:14:02

package jym.sim.json;

import java.io.IOException;

public interface IGo {
	/**
	 * ������json�ַ��������Appendable(StringBuilder, PrintWriter)��,
	 * ֻ�����ӱ���,�޸�����
	 * 
	 * @param json - Ҫ�����StringBuilder����
	 */
	public void go(Appendable json) throws IOException;
}
