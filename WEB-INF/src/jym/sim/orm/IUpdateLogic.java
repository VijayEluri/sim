// CatfoOD 2010-11-10 ����10:14:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

/**
 * ��ƴװupdate���ʱ,�������ת��Ϊ�ַ���,���δָ����ʹ��ȫ������
 * ������������˸ýӿڵ�ʵ��,��ȫ���趨��Ч
 */
public interface IUpdateLogic extends ISqlLogic {
	
	/**
	 * ���Ҫ����Ϊnullֵ,��up�������뷵��������Զ���"null"�ַ���
	 */
	String NULL = "null";
	
	/**
	 * ƴװΪupdate sql�Ĳ���
	 * 
	 * @param columnValue - ʵ�������е�ֵ
	 * @return ֻ�з�nullֵ���вŻᱻƴװΪsql,
	 * 	���Ҫ����Ϊnull��Ҫ���ظýӿڵľ�̬����NULL
	 */
	Object up(Object columnValue);
	
}
