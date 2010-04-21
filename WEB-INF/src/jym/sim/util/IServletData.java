// CatfoOD 2010-1-4 ����09:36:50

package jym.sim.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IServletData extends ISessionData {

	HttpServletRequest getHttpServletRequest();
	HttpServletResponse getHttpServletResponse();
	
	/**
	 * HttpServletRequest�ı�ݷ���
	 */
	String getParameter(String name);
	
	/**
	 * HttpServletRequest�ı�ݷ���
	 */
	void setAttribute(String name, Object obj);
	
	/**
	 * HttpServletRequest�ı�ݷ���
	 */
	Object getAttribute(String name);
	
	/**
	 * response.getWriter()�ı�ݷ���
	 */
	void print(Object data);
}
