package jym.base.orm;

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
	 */
	public void fieldPlot(String fieldName, String colname);
	
	/**
	 * ����Ҫӳ���ʵ��������fieldName�����ݿ�����colname����ƥ��<br>
	 * δӳ���ʵ������ʹ�ñ����ƥ��<br>
	 * <br>
	 * ����ҵ�ƥ��,����sql��䴴��fieldName���͵Ķ���,<b>����colname����ֵ�ŵ�sql���ĵ�һ��������</b>
	 * 
	 * @param fieldName -- ������,��������Ӧ��setter����,�����ִ�Сд
	 * @param colname -- ����,�����ִ�Сд
	 * @param sql -- ����fieldName���Ͷ�����Ҫ��sql���,���Ϊnull��͵������������ķ���Ч����ͬ
	 */
	public void fieldPlot(String fieldName, String colname, String sql);
}
