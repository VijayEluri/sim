package jym.base.sql;

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
	 * ע���������
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
}
