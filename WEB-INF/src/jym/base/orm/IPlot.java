package jym.base.orm;

import jym.base.sql.Logic;

/**
 * ʵ�����������ݿ���ӳ�����
 */
public interface IPlot {
	
	/**
	 * ����Ҫӳ���ʵ��������fieldName�����ݿ�����colname����ƥ��<br>
	 * δӳ���ʵ������ʹ�ñ����ƥ��<br>
	 * <br>
	 * <b>���fieldName���Ǽ򵥱�������:</b>������������������fieldPlot����
	 * 
	 * @param fieldName -- ������,��������Ӧ��setter����,�����ִ�Сд
	 * @param colname -- ����,�����ִ�Сд
	 * @param log -- ��ʹ��ʵ��������ִ��selectʱ, colname�е��߼�����<br>
	 * 				���fieldName=='value', log==Logic.EQ, ��where���Ϊ<br>
	 * 				<code>where colname = 'value'</code>
	 */
	public void fieldPlot(String fieldName, String colname, Logic log);
	
	/**
	 * ����Ҫӳ���ʵ��������fieldName�����ݿ�����colname����ƥ��<br>
	 * δӳ���ʵ������ʹ�ñ����ƥ��<br>
	 * ��ʹ��ʵ��������ִ��selectʱ, colname�е��߼�����Ĭ��ʹ�� Login.EQ
	 * <br>
	 * <b>���fieldName���Ǽ򵥱�������:</b>������������������fieldPlot����
	 * 
	 * @param fieldName -- ������,��������Ӧ��setter����,�����ִ�Сд
	 * @param colname -- ����,�����ִ�Сд
	 */
	public void fieldPlot(String fieldName, String colname);
	
	/**
	 * ����Ҫӳ���ʵ��������fieldName�����ݿ�����colname����ƥ��<br>
	 * δӳ���ʵ������ʹ�ñ����ƥ��<br>
	 * <br>
	 * ����ҵ�ƥ��,����getter����fieldName���͵Ķ���,
	 * <b>����colname����ֵ�ŵ�getter.select(...)�����ĵ�һ��������</b>
	 * 
	 * @param fieldName -- ������,��������Ӧ��setter����,�����ִ�Сд
	 * @param colname -- ����,�����ִ�Сд
	 * @param getter -- ����fieldName���Ͷ�����Ҫ��ISelecter����,
	 * 					���Ϊnull��͵������������ķ���Ч����ͬ
	 */
	public void fieldPlot(String fieldName, String colname, ISelecter<?> getter);
}
