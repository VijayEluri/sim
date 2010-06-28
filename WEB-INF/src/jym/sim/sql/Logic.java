// CatfoOD 2010-4-16 ����11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * ���ݿ��ѯwhere�־����߼��жϲ���
 */
public class Logic implements IWhere {
	
	/** ���� */
		public static final IWhere EQ = new Logic("%1$s='%2$s'");
		
	/** ������ */
		public static final IWhere NE = new Logic("%1$s!='%2$s'");
		
	/** С�� */
		public static final IWhere LT = new Logic("%1$s<'%2$s'");
		
	/** С�ڵ��� */
		public static final IWhere LE = new Logic("%1$s<='%2$s'");
		
	/** ���� */
		public static final IWhere GT = new Logic("%1$s>'%2$s'");
		
	/** ���ڵ��� */
		public static final IWhere GE = new Logic("%1$s>='%2$s'");
		
	/** like��ѯ */
		public static final IWhere LIKE = new Logic("%1$s like '%2$s'");
		
	/** ������ѯ; �������к����Ӵ���Ϊtrue */
		public static final IWhere INCLUDE = new Logic("%1$s like '%%%2$s%%'");
		
	/** �ų���ѯ; �������в������Ӵ���Ϊtrue */
		public static final IWhere EXCLUDE = new Logic("%1$s not like '%%%2$s%%'");
		
	/** ���ڲ�ѯ,��ȷ���� */
		public static final IWhere DATE = new Logic("to_char(%1$s, 'yyyy-mm-dd') = '%2$s'");
		
	/**
	 * ���ڷ�Χ��ѯ,��ѯ�Ľ��������������֮��(�������������),����ʱ�䲿��<br/>
	 * <code>beginFieldName <= result <= endFieldName</code><br/>
	 * �����һ��������Ϊnull���������ʹ�õ�ǰ����,���������Ϊnull,����Դ��еıȽ�<br/>
	 * <br/>
	 * <b>�����Ƚ��õ�������,���Ϳ�����String,�����ֵΪ''���߸�ʽ��Ч��ȽϽ�����ܲ���ȷ</b><br/>
	 * 
	 * @param beginFieldName - ʵ�����������,��ѯ������ڵ��ڴ������е�ֵ
	 * @param endFieldName - ʵ�����������,��ѯ���С�ڵ��ڴ������е�ֵ
	 * * * */
		public static final IWhere DATE_RANGE(String beginFieldName, String endFieldName) {
			return new DateRange(beginFieldName, endFieldName);
		};
	
	////////////////////// ----------------------------------
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value, Object model) {
		return String.format(format, columnName, value);
	}
	
}
