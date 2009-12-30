// CatfoOD 2009-12-15 ����08:10:04

package jym.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.base.util.ISessionData;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * ��װStruts.Action��execute�����Ĳ���
 */
public interface IActionData extends ISessionData {
	
	ActionMapping getActionMapping();
	ActionForm getActionForm();
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
	 * HttpSession�ı�ݷ���
	 */
	void setSessionAttribute(String name, Object obj);
	
	/**
	 * HttpSession�ı�ݷ���
	 */
	Object getSessionAttribute(String name);
	
	/**
	 * response.getWriter()�ı�ݷ���
	 */
	void print(Object data);
}
