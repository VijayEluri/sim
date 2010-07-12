// CatfoOD 2010-4-16 ����11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.sql.logic.DateRange;
import jym.sim.sql.logic.DefinitionLogic;
import jym.sim.sql.logic.OperatorIN;
import jym.sim.sql.logic.OperatorOR;

/**
 * ���ݿ��ѯwhere�־����߼��жϲ���,IWhere.w()��������null,������������
 */
public class Logic implements IWhere {
	
	/** ���� */
		public static final IWhere EQ = new Easy("=");
		
	/** ������ */
		public static final IWhere NE = new Easy("!=");
		
	/** С�� */
		public static final IWhere LT = new Easy("<");
		
	/** С�ڵ��� */
		public static final IWhere LE = new Easy("<=");
		
	/** ���� */
		public static final IWhere GT = new Easy(">");
		
	/** ���ڵ��� */
		public static final IWhere GE = new Easy(">=");
		
	/** like��ѯ */
		public static final IWhere LIKE = new Easy("like");
		
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
	 * 
	 * */
		public static final IWhere DATE_RANGE(String beginFieldName, String endFieldName) {
			return new DateRange(beginFieldName, endFieldName);
		};
		
	/**
	 * �б��ѯ,����arrayFieldName��ֵ����IN�Ĳ�ѯ������
	 * 
	 * @param arrayFieldName - ʵ�����������,���Ե�������һ������
	 * */
		public static final IWhere IN(String arrayFieldName) {
			return new OperatorIN(arrayFieldName);
		};
	
	/**
	 * ��װ���IWhere����,ʵ�ֿ�ѡ����߼�,���w[n]�Ľ��Ϊnull,��ʹ��w[n+1]�Ľ��
	 * */
		public static final IWhere OR(IWhere ...w) {
			return new OperatorOR(w);
		};
		
	/**
	 * where�Ӿ���ָ���ĸ�ʽ�ַ�������
	 * <br>��ʽ�ַ�����<b>�̶�</b>�Ĳ���,��<b>ƴװ</b>�Ĳ������,
	 * <br>�̶��Ĳ���ԭ�����,ƴװ�Ĳ�����ָ��ʹ��bean���Ǹ����Ե�ֵ
	 * <br><br>���������ַ���:
	 * <br>
	 * <code>"${field1} < ${field2}"</code>
	 * <br>���bean��field1=10,field2=40�����յĽ����
	 * <br>
	 * <code>"10 < 40"</code>
	 * <br><br>���field�����Ͳ���String,��ʹ�øö����toString()����,
	 * <br>�����ʽ�ַ�����ָ�������ֵ��һ��Ϊnull,����������Ӿ������
	 * <br><b>${}֮�䲻���пո�</b
	 * 
	 * @param defstr - ����where�Ӿ��ģʽ�ַ���
	 * */
		public static final IWhere DEF(String defstr) {
			return new DefinitionLogic(defstr);
		};
		
		
	////////////////////// ------------------------------------------------- ////
		
	
	private final String format;
	
	private Logic(String fmt) {
		format = fmt;
	};

	public String w(String columnName, Object value, Object model) {
		return String.format(format, columnName, value);
	}
	
	/**
	 * ���߼����ʽ, "v1 op v2"
	 */
	private static class Easy implements IWhere {
		
		private String op;
		
		/**
		 * <code>columnName OP 'value'</code>
		 */
		private Easy(String op) {
			this.op = op;
		}
		
		public String w(String columnName, Object value, Object model) {
			StringBuilder buff = new StringBuilder();
			buff.append(columnName);
			buff.append(' ');
			buff.append(op);
			buff.append(' ');
			buff.append('\'');
			buff.append(value);
			buff.append('\'');
			return buff.toString();
		}
	}
}
