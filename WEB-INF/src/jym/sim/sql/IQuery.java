package jym.sim.sql;


/**
 * sql��ѯ�ӿ�
 */
public interface IQuery {
	/**
	 * ִ�в�ѯ
	 * 
	 * @param sql - sql����ִ�ж���
	 */
	public void query(ISql sql);
	
	/**
	 * ִ�в�ѯ���,������IResultSql.exe()�Ľ��
	 * 
	 * @param rsql - sql����ִ�ж���
	 * @return IResultSql.exe()
	 */
	public Object query(IResultSql rsql);
	
	/**
	 * ע���������,eh�ǻ����̰߳�ȫ��,����ÿ���̶߳���Ҫ�ֱ�ע��<br>
	 * ÿ���߳�ֻ��ע��Ψһһ��������,����ǰһ���������ᱻ����
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * ȡ�õ�ǰ��JDBC�Ự(����),�˻ỰĬ�����̰߳�ȫ��,<br>
	 * ���Ҫʵ�ֲ�ͬ�����񷽷�,����д�˺���<br>
	 * ��������Ѿ��ر�,�򷵻�null
	 */
	public IJdbcSession getSession();
}
