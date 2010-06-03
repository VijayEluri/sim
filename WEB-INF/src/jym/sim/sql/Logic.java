// CatfoOD 2010-4-16 ����11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * ���ݿ��ѯwhere�־����߼��жϲ���
 */
public class Logic implements IWhere {
	
	/** ���� */
		public static final Logic EQ = new Logic("%1$s='%2$s'");
		
	/** ������ */
		public static final Logic NE = new Logic("%1$s!='%2$s'");
		
	/** С�� */
		public static final Logic LT = new Logic("%1$s<'%2$s'");
		
	/** С�ڵ��� */
		public static final Logic LE = new Logic("%1$s<='%2$s'");
		
	/** ���� */
		public static final Logic GT = new Logic("%1$s>'%2$s'");
		
	/** ���ڵ��� */
		public static final Logic GE = new Logic("%1$s>='%2$s'");
		
	/** like��ѯ */
		public static final Logic LIKE = new Logic("%1$s like '%2$s'");
		
	/** ������ѯ; �������к����Ӵ���Ϊtrue */
		public static final Logic INCLUDE = new Logic("%1$s like '%%%2$s%%'");
		
	/** �ų���ѯ; �������в������Ӵ���Ϊtrue */
		public static final Logic EXCLUDE = new Logic("%1$s not like '%%%2$s%%'");
		
	/** ���ڲ�ѯ,��ȷ���� */
		public static final Logic DATE = new Logic("to_char(%1$s, 'yyyy-mm-dd') = '%2$s'");
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value) {
		return String.format(format, columnName, value);
	}
	
}
