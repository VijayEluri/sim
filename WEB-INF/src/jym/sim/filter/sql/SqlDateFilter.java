// CatfoOD 2010-8-19 ����11:03:41 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

import jym.sim.filter.SimFilterException;

/**
 * ����ת��Ϊsql�ַ���
 */
public class SqlDateFilter implements ISqlInputParamFilter<Date> {
	
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String ORACLE_DEFAULT_FMT = "dd-M�� -yy";

	private SimpleDateFormat sqlDateFormat;
	
	
	/**
	 * ʹ��Ĭ�ϸ�ʽƴװsql����
	 */
	public SqlDateFilter() {
		this(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * ʹ��ָ���ĸ�ʽƴװsql����
	 * @param format - ���ڸ�ʽ
	 */
	public SqlDateFilter(String format) {
		this(new SimpleDateFormat(format));
	}
	
	public SqlDateFilter(SimpleDateFormat df) {
		sqlDateFormat = df;
	}
	
	public Date see(final Date date) throws SimFilterException {
		return new Date(date.getTime()) {
			private static final long serialVersionUID = 8902062727482387855L;

			public String toString() {
				return sqlDateFormat.format(date);
			}
		};
	}
	
}
