// CatfoOD 2010-4-16 ����08:22:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Statement;

/**
 * sql����ִ����
 */
public interface ISql {
	
	/**
	 * JdbcTemplate�ص�����, �����е�sql��������ر�
	 * 
	 * @param stm - JdbcTemplate������Statement 
	 * @throws Throwable - �����в���Ҫ��׽�κ��쳣
	 */
	public void exe(Statement stm) throws Throwable;
	
}
