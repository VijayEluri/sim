// CatfoOD 2010-7-12 ����01:06:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Statement;

/**
 * ���ؽ����sql���ִ����
 */
public interface IResultSql {

	/**
	 * JdbcTemplate�ص�����, �����е�sql��������ر�
	 * 
	 * @param stm - JdbcTemplate������Statement 
	 * @throws Throwable - �����в���Ҫ��׽�κ��쳣
	 * @return ����һ������
	 */
	public Object exe(Statement stm) throws Throwable;
	
}
