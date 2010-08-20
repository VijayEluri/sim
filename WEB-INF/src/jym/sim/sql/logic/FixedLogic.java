// CatfoOD 2010-8-20 ����10:48:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISkipValueCheck;
import jym.sim.sql.IWhere;

/**
 * �̶���sql where�Ӿ�,���Ƿ����Ѿ������sql
 */
public class FixedLogic implements IWhere, ISkipValueCheck {
	
	private final String sql;
	
	public FixedLogic(String _sql) {
		sql = _sql;
	}

	public String w(String columnName, Object value, Object model) {
		return sql;
	}

}
