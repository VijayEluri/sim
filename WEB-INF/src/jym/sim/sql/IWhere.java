// CatfoOD 2010-6-3 ����10:18:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * sql��where�Ӿ�������ֵ���жϷ���
 */
public interface IWhere {
	/**
	 * ����where��һ���ж��Ӿ�
	 * @param columnName - �е�����
	 * @param value - �е�ֵ
	 * @return ����ж�columnName��sql
	 */
	public String w(String columnName, Object value);
}
