// CatfoOD 2010-8-19 ����09:44:30 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

import jym.sim.util.Tools;

/**
 * �������Ƕ���γɹ�������
 */
public class FilterChain<T> implements IFilter<T> {
	
	private IFilter<T> f1;
	private IFilter<T> f2;
	
	/**
	 * ������one�ȹ���,Ȼ����two
	 */
	public FilterChain(IFilter<T> one, IFilter<T> two) {
		Tools.check(one, "one����������Ϊnull");
		Tools.check(two, "two����������Ϊnull");
		
		f1 = one;
		f2 = two;
	}

	public T see(T src) throws SimFilterException {
		return f2.see(f1.see(src));
	}

}
