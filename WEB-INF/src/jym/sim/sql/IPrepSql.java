// CatfoOD 2011-4-25 ����08:49:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.PreparedStatement;

/**
 * sql����ִ����
 */
public interface IPrepSql {
	
	/**
	 * JdbcTemplate�ص�����, �����е�sql��������ر�
	 * 
	 * @param pstm - JdbcTemplate������PreparedStatement 
	 * @throws Throwable - �����в���Ҫ��׽�κ��쳣
	 */
	public void exe(PreparedStatement pstm) throws Throwable;
	
	/**
	 * ����sql���, ������ʼ��PreparedStatement
	 * @see java.sql.PreparedStatement
	 */
	public String getSql();
	
}
