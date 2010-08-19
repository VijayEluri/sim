// CatfoOD 2010-8-19 ����08:47:45 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;

/**
 * Ϊ�������ṩͳһ�����뼯��
 */
public class FilterPocket {
	
	/** �ù�������������ֵ */
	public static final IFilter<Object> NULL_FILTER;
	
	private Map<Class<Object>, IFilter<Object>> filters;
	
	
	public FilterPocket() {
		filters = new HashMap<Class<Object>, IFilter<Object>>();
	}
	
	/**
	 * ע�������
	 * 
	 * @param <T> - �����˵���������
	 * @param type - �������������͵�class
	 * @param ft - �����������T���͵Ĺ������Ѿ����ˣ��ù������ᱻ���
	 * 
	 * @return ����T����ԭ���Ĺ�������û�з���null
	 */
	@SuppressWarnings("unchecked")
	public <T> IFilter<T> reg(Class<T> type, IFilter<T> ft) {
		Tools.check(type, "����type����Ϊnull");
		Tools.check(ft, "����ft����Ϊnull");
		
		return (IFilter<T>) filters.put((Class<Object>)type, (IFilter<Object>)ft);
	}
	
	/**
	 * �ѹ�����������һ���������ĺ���
	 * 
	 * @param <T> - �����˵���������
	 * @param type - �������������͵�class
	 * @param ft - ������
	 */
	@SuppressWarnings("unchecked")
	public <T> void add(Class<T> type, IFilter<T> ft) {
		IFilter<T> f = (IFilter<T>) filters.get(type);
		if (f!=null) {
			f = new FilterChain<T>(f,ft);
		} else {
			f = ft;
		}
		
		reg(type, f);
	}
	
	/**
	 * ����src���ݣ�ʹ�õĹ�����ͨ�����src�������Զ����������src==null
	 * �򷵻�null,����Ҳ��������͵Ĺ������򷵻�src
	 * 
	 * @param src - Ҫ���˵�ֵ
	 * @return ���˺��ֵ
	 * @throws SimFilterException - �������ڹ���ʱ�׳����쳣
	 */
	public Object filter(Object src) throws SimFilterException {
		if (src!=null) {
			IFilter<Object> f = filters.get(src.getClass());
			
			if (f!=null) {
				return f.see(src);
			}
		}
		
		return src;
	}
	
	/**
	 * ȡ��ָ�����͵Ĺ�����
	 * 
	 * @param <T> - ��������
	 * @param type - ���͵�class
	 * @param getNullFilter - true��������������Ͳ���
	 * ���򷵻�һ���������ݹ��˵Ĺ����������򷵻�null
	 * 
	 * @return ��T���͵Ĺ�����
	 */
	@SuppressWarnings("unchecked")
	public <T> IFilter<T> getFilter(Class<T> type, boolean getNullFilter) {
		IFilter<?> f = filters.get(type);
		if (f==null && getNullFilter) {
			f = NULL_FILTER;
		}
		return (IFilter<T>)f;
	}
	
	static {
		NULL_FILTER = new IFilter<Object>() {
			public Object see(Object src) throws SimFilterException {
				return src;
			}
		};
	}
}
