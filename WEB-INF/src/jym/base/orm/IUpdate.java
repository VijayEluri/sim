// CatfoOD 2010-4-16 ����04:44:50 yanming-sohu@sohu.com/@qq.com

package jym.base.orm;

/**
 * ��ִ��insert delete update����<br>
 * ���ֲ�����������������Ч��,����������Ч����x>=0
 */
public interface IUpdate<T> {
	/**
	 * �����ݿ��в���һ������,�ɹ�����true
	 */
	public boolean add(T model);
	
	/**
	 * ɾ�����ݿ�������
	 */
	public int delete(T model);
	
	/**
	 * �������ݿ��е�����
	 */
	public int update(T model);
}
