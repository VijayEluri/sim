// CatfoOD 2009-12-15 ����08:10:04

package jym.sim.util;

/**
 * servlet session���ݶ���
 */
public interface ISessionData {
	/**
	 * HttpSession�ı�ݷ���
	 */
	void setSessionAttribute(String name, Object obj);
	
	/**
	 * HttpSession�ı�ݷ���
	 */
	Object getSessionAttribute(String name);
	
}
