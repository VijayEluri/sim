// CatfoOD 2010-4-16 ����10:07:01 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * ����ת���ӿ�
 */
interface ITransition {
	
	/**
	 * ��rs��col�е�����ȡ��ת����ָ��������,����
	 */
	Object trans(ResultSet rs, int col) throws SQLException;
	
}
