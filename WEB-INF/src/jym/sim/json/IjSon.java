// CatfoOD 2010-7-28 ����02:20:32 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.io.IOException;


/**
 * json����
 */
public interface IjSon {

	/**
	 * ������json�ַ��������Appendable(StringBuilder, PrintWriter)��,ֻ�����ӱ���,�޸�����
	 * @param json - Ҫ�����StringBuilder����
	 */
	public void go(Appendable json) throws IOException;
	/**
	 * ����һ������������-ֵ��
	 * @param name - ����������
	 * @param value - ������ֵ,��������һ��IjSon��Ϊ����
	 */
	public void set(Object name, Object value);
	/**
	 * ����һ��name���ֵ��������,����������Ѿ������򷵻���
	 * �������ָ���Ķ�����IjSon����null
	 * @return - json����
	 */
	public IjSon createSub(Object name);
	
}
