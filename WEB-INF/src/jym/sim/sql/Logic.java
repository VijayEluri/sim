// CatfoOD 2010-4-16 ����11:07:11 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.orm.ISqlLogic;
import jym.sim.orm.IUpdateLogic;
import jym.sim.sql.logic.DateRange;
import jym.sim.sql.logic.DefinitionLogic;
import jym.sim.sql.logic.FixedLogic;
import jym.sim.sql.logic.OperatorIN;
import jym.sim.sql.logic.OperatorOR;

/**
 * ���ݿ��ѯwhere�־����߼��жϲ���,IWhere.w()��������null,������������
 */
public class Logic implements ISqlLogic {
	
////////////////////// -----------�����ж�-------------------------------- ////
	
	/** �����ж�, ������ֶ�����ΪNULL */
		public static final IUpdateLogic ALLOW_NULL = new IUpdateLogic() {
			public Object up(Object columnValue) {
				
				if (columnValue==null) {
					return NULL;
					
				} else if (columnValue instanceof String) {
					if (((String) columnValue).trim().length() < 1) {
						return NULL;
					}
				}
				
				return columnValue;
			}
		};
	
////////////////////// -----------��ѯ�ж�-------------------------------- ////
	
	/** ��ѯ�ж�, ���� */
		public static final IWhere EQ = new Easy("=");
		
	/** ��ѯ�ж�, ������ */
		public static final IWhere NE = new Easy("!=");
		
	/** ��ѯ�ж�, С�� */
		public static final IWhere LT = new Easy("<");
		
	/** ��ѯ�ж�, С�ڵ��� */
		public static final IWhere LE = new Easy("<=");
		
	/** ��ѯ�ж�, ���� */
		public static final IWhere GT = new Easy(">");
		
	/** ��ѯ�ж�, ���ڵ��� */
		public static final IWhere GE = new Easy(">=");
		
	/** �Զ����like��ѯ, ���Ե�ֵ��Ҫ����ģ������ */
		public static final IWhere LIKE = new Easy("like");
		
	/** ������ѯ; �������к����Ӵ���Ϊtrue */
		public static final IWhere INCLUDE = new Format("%1$s like '%%%2$s%%'");
		
	/** �ų���ѯ; �������в������Ӵ���Ϊtrue */
		public static final IWhere EXCLUDE = new Format("%1$s not like '%%%2$s%%'");
		
	/** ���ڲ�ѯ,��ȷ���� */
		public static final IWhere DATE = new Format("to_char(%1$s, 'yyyy-mm-dd') = '%2$s'");
		
	/** ��ѯ�ж�, ���Բ���Ϊ��ѯ��������� */
		public static final IWhere NONE = new IWhere() {
			public String w(String columnName, Object value, Object model) {
				return null;
			}
		};
		
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
	 * ��ѯ�ж�, ��װ���IWhere����,ʵ�ֿ�ѡ����߼�,���w[n]�Ľ��Ϊnull,��ʹ��w[n+1]�Ľ��
	 * */
		public static final IWhere OR(IWhere ...w) {
			return new OperatorOR(w);
		};
		
	/**
	 * ��ѯ�ж�, �̶���where�Ӿ�,���Ƿ��ز������趨��sql,һ����������߼����ʹ��
	 * */
		public static final IWhere FIXED(final String sql) {
			return new FixedLogic(sql);
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
	 * <br><b>${}֮�䲻���пո�</b>
	 * 
	 * @param defstr - ����where�Ӿ��ģʽ�ַ���
	 * */
		public static final IWhere DEF(String defstr) {
			return new DefinitionLogic(defstr);
		};
		
		
////////////////////// ------------------------------------------------- ////
	
	/**
	 * ʹ���ַ����滻��ƴװ��ѯ���
	 * %1$s - ����
	 * %2$s - ���Ե�ֵ 
	 */
	public static class Format implements IWhere {
		
		private final String format;
		
		public Format(String fmt) {
			format = fmt;
		}
		
		public String w(String columnName, Object value, Object model) {
			return String.format(format, columnName, value);
		}
	}
	
	
	/**
	 * ���߼����ʽ, "columnName OP 'value'"
	 */
	public static class Easy implements IWhere {
		
		private String op;
		
		/**
		 * <code>columnName OP 'value'</code>
		 */
		public Easy(String op) {
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
