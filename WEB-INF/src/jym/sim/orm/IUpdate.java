// CatfoOD 2010-4-16 ����04:44:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

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
	 * ɾ�����ݿ�������,��������ݿ���󷵻�-1
	 */
	public int delete(T model);
	
	/**
	 * �������ݿ��е�����,��������ݿ���󷵻�-1<br>
	 * ������ֵ����ı�,���Ҹ�������ֻ������
	 */
	public int update(T model);
}
