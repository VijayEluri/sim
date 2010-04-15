package jym.base.sql;

import java.util.HashMap;
import java.util.Map;

public class Where implements ISqlsub {
	
	private Map<String, Object> vars;
	private StringBuilder where;
	
	/**
	 * where�Ӿ�,������ȫ��,Ҳ�����ǲ���
	 */
	public Where(String wh) {
		vars  = new HashMap<String, Object>();
		where = new StringBuilder();
		add(wh);
	}
	
	/**
	 * ����������where�Ӿ�,prere�п�����$��ʼ�ı���������$��ʼ�ո����<br>
	 * ��ʽ: and {prere} 
	 * 
	 * @param prere - where�Ӿ�,�����б���
	 * @return Where����,������д
	 */
	public Where and(String prere) {
		where.append(" and ");
		add(prere);
		return this;
	}
	
	/**
	 * ����������where�Ӿ�,prere�п�����$��ʼ�ı���������$��ʼ�ո����<br>
	 * ��ʽ: and {prere} 
	 * 
	 * @param prere - where�Ӿ�,�����б���
	 * @return Where����,������д
	 */
	public Where or(String prere) {
		where.append(" or ");
		add(prere);
		return this;
	}
	
	private void add(String p) {
		where.append('(');
		where.append(p);
		where.append(')');
	}
	
	/**
	 * ��value�滻where�Ӿ��еı���ռλ��
	 * @param variableName -- ������
	 * @param value -- �滻��ֵ
	 */
	public Where set(String variableName, Object value) {
		vars.put(variableName, value);
		return this;
	}

	@Override
	public String get() {
		return null;
	}
}
