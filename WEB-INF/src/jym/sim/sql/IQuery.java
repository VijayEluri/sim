package jym.sim.sql;

import java.sql.Connection;
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
	 * 
	 * @param rsql - sql����ִ�ж���
	 * @return IResultSql.exe()
	 */
	public Object query(IResultSql rsql);
	
	/**
	 * ����һ�������ݿ������,������ֱ�Ӵ�����Դ��ȡ��
	 * ����ʹ�ý�����Ҫ�ֶ��ر�
	 * 
	 * @throws SQLException
	 */
	public Connection createConnection() throws SQLException;
	
	/**
	 * ע���������
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * ȡ�õ�ǰ��JDBC�Ự(����),�˻Ự���̰߳�ȫ��
	 */
	public IJdbcSession getSession() throws SQLException;
}
