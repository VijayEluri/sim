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
	 * ִ�в�ѯ���,������IResultSql.exe()�Ľ��
	 * @param rsql - sql����ִ�ж���
	 * @return IResultSql.exe()
	 */
	public Object query(IResultSql rsql);
	
	/**
	 * ע���������
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * ȡ�õ�ǰ��JDBC�Ự(����),�˻Ự���̰߳�ȫ��
	 */
	public IJdbcSession getSession() throws SQLException;
}
