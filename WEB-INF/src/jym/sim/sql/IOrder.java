// CatfoOD 2010-6-3 ����10:39:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * ��ѯ�������ӿ�,����������ķ���
 */
public interface IOrder {
	/**
	 * ��������,�Ӵ�С
	 * @param columnName - �е�����
	 */
	public void desc(String columnName);
	/**
	 * ��������,��С����
	 * @param columnName - �е�����
	 */
	public void asc(String columnName);
}
