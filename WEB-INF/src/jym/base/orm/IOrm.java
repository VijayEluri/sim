package jym.base.orm;

/**
 * ormӳ��ʱʹ�õ�ʵ��ӳ�����,
 * 
 * @param <T> - ����ʵ��
 */
public interface IOrm<T> {
	
	/**
	 * ����T���͵�class
	 * 
	 * @return T���͵�class
	 */
	public Class<T> getModelClass();
	
	/**
	 * ���������sql����
	 * 
	 * @return 
	 * 		�����ʽ��sql����,���е�where�Ӿ�ʹ��$where����,
	 * 		�Ա㶯̬�滻���ض�������,����<br>
	 * 		ԭ��: <code>select * from table_name where col1 > 1;</code><br>
	 * 		�滻: <code>select * from table_name $where; </code>
	 */
	public String getSimSql();
	
	/**
	 * ������ʵ���ӳ�����
	 * ��plot�з���ʵ�����������ݿ������Ķ�Ӧ��ϵ
	 */
	public void mapping(IPlot plot);
	
}
