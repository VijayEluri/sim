package jym.base.orm;


public interface IOrm<T> {
	
	/**
	 * ����T���͵�class
	 * 
	 * @return T���͵�class
	 */
	public Class<T> getModelClass();
	
	/**
	 * ����sql����
	 * 
	 * @param sql -- PreparedStatement��ʽ��sql
	 */
	public String getPrepareSql();
	
	/**
	 * ������ʵ���ӳ�����
	 * ��plot�з���ʵ�����������ݿ������Ķ�Ӧ��ϵ
	 */
	public void mapping(IPlot plot);
	
}
