// CatfoOD 2010-4-16 ����11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;

public enum Logic {
	
	/** ���� */
		EQ("='%1$s'"),
		
	/** ������ */
		NE("!='%1$s'"),
		
	/** С�� */
		LT("<'%1$s'"),
		
	/** С�ڵ��� */
		LE("<='%1$s'"),
		
	/** ���� */
		GT(">'%1$s'"),
		
	/** ���ڵ��� */
		GE(">='%1$s'"),
		
	/** like��ѯ */
		LIKE("like '%1$s'"),
		
	/** ������ѯ, �������к����Ӵ���Ϊtrue */
		INCLUDE("like '%%%1$s%%'"),
		
	/** �ų���ѯ, �������в������Ӵ���Ϊtrue */
		EXCLUDE("not like '%%%1$s%%'"),
	;
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};
	
	/**
	 * ��parm��Ϊ�������߼���ʽ��ϳ��ַ���
	 */
	public String in(Object parm) {
		return String.format(format, parm);
	}
	
}
