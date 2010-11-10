// CatfoOD 2010-11-10 ����10:48:52 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import jym.sim.sql.IWhere;
import jym.sim.sql.Logic;


public class LogicPackage {
	
	public static final LogicPackage DEFAULT = new LogicPackage();
	
	private IWhere whereLogic = Logic.EQ;
	private IUpdateLogic updateLogic;
	
	
	protected LogicPackage(ISqlLogic[] log) {
		if (log!=null) {
			for (int i=0; i<log.length; ++i) {
				if (log[i] instanceof IWhere) {
					whereLogic = (IWhere) log[i];
				}
				if (log[i] instanceof IUpdateLogic) {
					updateLogic = (IUpdateLogic) log[i];
				}
			}
		}
	}
	
	private LogicPackage() {}
	
	/**
	 * Ĭ�����û��ָ��where����,�򷵻���ȱȽϲ���
	 */
	protected IWhere getWhereLogic() {
		return whereLogic;
	}
	
	/**
	 * ���û��ָ������,�򷵻�null
	 */
	protected IUpdateLogic getUpdateLogic() {
		return updateLogic;
	}
}