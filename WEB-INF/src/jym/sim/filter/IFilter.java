// CatfoOD 2010-8-19 ����08:32:40 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

/**
 * ���й������ĸ��ӿڸ�,����ָʾ�ù��������˵���������
 */
public interface IFilter<T> {
	/**
	 * ����T������
	 * 
	 * @param src - Դ����
	 * @throws SimFilterException - ����׳����쳣�����ʵ�ֲ�ͬ,����ʽ��ͬ
	 * @return ���˺������
	 */
	public T see(T src) throws SimFilterException;
}
