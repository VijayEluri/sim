// CatfoOD 2010-6-3 ����10:18:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * sql��where�Ӿ�������ֵ���жϷ���
 */
public interface IWhere {
	
	/**
	 * ���Ҫ���Դ�where�Ӿ�����w()�з���SKIP_WHERE_SUB
	 */
	public static final String SKIP_WHERE_SUB = null;
	
	/**
	 * ����where��һ���ж��Ӿ�
	 * 
	 * @param columnName - ���ݿ��е�����
	 * @param value - ��Ӧ���ݿ�����������ʵ�����Ե�ֵ
	 * @param model - ִ�в�ѯ�����ݶ���,������������
	 * 
	 * @return ����ж�columnName��sql,�������null����Դ��Ӿ���ж�
	 */
	public String w(String columnName, Object value, Object model);
}
