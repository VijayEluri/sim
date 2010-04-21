// CatfoOD 2009-12-14 ����08:25:03

package jym.sim.tags.template;

import java.util.List;

/**
 * ��������Ļص�����
 */
public interface IResultCallback<E> {
	/**
	 * ���ر������
	 */
	int getColumn();
	
	/**
	 * ��ҳ�����ҳ��
	 */
	int getTotalPage();
	
	/**
	 * ��ҳ��ĵ�ǰҳ
	 */
	int getCurrentPage();
	
	/**
	 * ����ҳ�볬����urlģʽ�ַ���%pageת��Ϊҳ��
	 */
	String getUrlPattern();
	
	/**
	 * ������������Ϊ��ͷ
	 * @param column - �е�����
	 * @return ��ֵ
	 */
	String getColumnName(int column);
	
	/**
	 * ӳ��������
	 * ��source����ȡ�����ԣ����������List��
	 * ResultTableTemplate����ÿ�����ݣ�����ÿ���е����������
	 * 
	 * @param target - �еĽ��ֵ
	 * @param source - ������Դ����
	 */
	void mappingRowValue(List<String>target, E source);
}
