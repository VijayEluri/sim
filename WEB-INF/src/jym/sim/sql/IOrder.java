// CatfoOD 2010-6-3 ����10:39:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * ��ѯ�������ӿ�,����������ķ���
 */
public interface IOrder {
	
	/**
	 * ��������,�Ӵ�С
	 * 
	 * @param columnName - �е�����
	 * @return ���ص�IOrder����������һ���������
	 */
	public IOrder desc(String columnName);
	
	/**
	 * ��������,��С����
	 * 
	 * @param columnName - �е�����
	 * @return ���ص�IOrder����������һ���������
	 */
	public IOrder asc(String columnName);
	
}
