// CatfoOD 2010-9-1 ����10:11:26 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import jym.sim.filter.FilterPocket;
import jym.sim.filter.IFilter;
import jym.sim.filter.SimFilterException;

/**
 * ����ֵ��Ч�Լ��
 */
public class CheckVaildValue extends FilterPocket {
	
	/**
	 * <strike>���value==null����false<br>
	 * ���value!=null����û����صĹ�����,�򷵻�true ����,</strike>
	 * 
	 * ���û����ع�������ֵΪnull�򷵻�false, ���򷵻�true(��Ĭ�ϲ���Ϊnull)<br>
	 * ����й�������,����������null�򷵻�false,��null����true
	 */
	@SuppressWarnings({ "unchecked" })
	public boolean isValid(Object value, Class<?> valueType) {
		IFilter f = super.getFilter(valueType, false);
		if (f!=null) {
			try {
				return f.see(value) != null;
			} catch (SimFilterException e) {
				e.printStackTrace();
			}
		} else {
			return value!=null;
		}
		return false;
	}
	
}
