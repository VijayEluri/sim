package jym.sim.sql;

import java.sql.SQLException;

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
	
	/**
	 * ȡ�õ�ǰ��JDBC�Ự(����),�˻Ự���̰߳�ȫ��
	 */
	public IJdbcSession getSession() throws SQLException;
}
