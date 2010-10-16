// CatfoOD 2010-7-28 ����02:20:32 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.util.Collection;
import java.util.Map;



/**
 * json����
 */
public interface IjSon extends IGo {

	/**
	 * ����һ������������-ֵ��,���value==null�����ֵ��"null"��������
	 * 
	 * @param name - ����������
	 * @param value - ������ֵ,��������һ��IjSon��Ϊ����
	 */
	public void set(Object name, Object value);
	
	/**
	 * ����һ��name���ֵ��������,����������Ѿ������򷵻���
	 * �������ָ���Ķ�����IjSon����null
	 * 
	 * @return - json����
	 */
	public IjSon createSub(Object name);
	
	/**
	 * ����һ������������-ֵ��,ԭ������ֱ�Ӷ�Ӧjs������
	 */
	public void set(Object name, boolean b);
	
	/**
	 * ����һ������������-ֵ��,ԭ������ֱ�Ӷ�Ӧjs������
	 */
	public void set(Object name, long i);
	
	/**
	 * ����һ������������-ֵ��,ԭ������ֱ�Ӷ�Ӧjs������
	 */
	public void set(Object name, double d);
	
	/**
	 * ����nameָ����ֵ������һ������<br>
	 * name : {<br>
	 * 	1 : c[1],<br>
	 * 	n : c[n]<br>
	 * }<br>
	 */
	public void set(Object name, Collection<?> c);
	
	/**
	 * ����name�ƶ���ֵ������һ��Map<br>
	 * name : {<br>
	 * 	key : value,<br>
	 * 	key2: value2<br>
	 * }
	 */
	public void set(Object name, Map<?,?> map);
	
	/**
	 * ����һ��������������name������ֵ������bean�е�����
	 * ֻ����get���������Բ��������json������,
	 * bean�м̳е�����<b>����</b>�����json��
	 */
	public void setBean(Object name, Object bean); 
	
}
