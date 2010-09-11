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
	 * ���value==null����false<br>
	 * ���value!=null����û����صĹ�����,�򷵻�true<br>
	 * ����,�������������null�򷵻�false,��null����true
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isValid(Object value) {
		if (value!=null) {
			IFilter f = super.getFilter(value.getClass(), false);
			if (f!=null) {
				try {
					return f.see(value) != null;
				} catch (SimFilterException e) {
					e.printStackTrace();
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
}
