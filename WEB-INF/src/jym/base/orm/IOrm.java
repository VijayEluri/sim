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
	 * �������ݿ����
	 */
	public String getTableName();
	
	/**
	 * ������ʵ���ӳ�����
	 * ��plot�з���ʵ�����������ݿ������Ķ�Ӧ��ϵ
	 */
	public void mapping(IPlot plot);
	
	/**
	 * ���ر��������е�������
	 */
	public String getKey();
	
}
