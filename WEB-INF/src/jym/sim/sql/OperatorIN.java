// CatfoOD 2010-7-5 ����04:04:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.util.Collection;
import java.util.Iterator;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

/**
 * �б������(�жϱ��ʽ�Ƿ�Ϊ�б��е�ָ����)��IN (��1,��2����)
 */
public class OperatorIN implements IWhere, ISkipValueCheck {
	
	private String fname;
	
	public OperatorIN(String arrayFieldName) {
		fname = arrayFieldName;
	}

	@SuppressWarnings("unchecked")
	public String w(String columnName, Object value, Object model) {
		Object arr = getValue(model, fname);
		
		while (arr instanceof Collection<?>) {
			
			Collection<Object> c = (Collection<Object>) arr;
			Iterator<Object> it = c.iterator();
			
				if (!it.hasNext()) break;

			StringBuilder out = new StringBuilder();
			out.append(columnName).append(" IN (");
			
				while (it.hasNext()) {
					out.append("'").append(it.next()).append("'");
					if (it.hasNext()) {
						out.append(",");
					}
				}
			
			out.append(")");
			
			return out.toString();
		}
		return null;
	}

	private Object getValue(Object model, String fieldname) {
		String methodName = BeanUtil.getGetterName(fieldname);
		Object value = null;
		
		try {
			value = BeanUtil.invoke(model, methodName, new Object[0]);
			
		} catch (NoSuchMethodException e) {
			error("�Ҳ��� "+fieldname+" ��get����: " + e.getMessage());
			
		} catch (Exception e) {
			error("����: " + e.getMessage());
		}
		
		return value;
	}
	
	private void error(String msg) {
		Tools.pl("OperatorIN error: " + msg);
	}
}
