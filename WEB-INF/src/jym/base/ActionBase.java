// CatfoOD 2009-12-30 ����12:44:05

package jym.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ��Struts1.3 Action ��ķ�װ
 * ʵ��������ʹ��execute�������ӵĲ���
 */
public abstract class ActionBase extends Action {
	
	/**
	 * ���಻Ҫ�����������
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionData ad = new ActionData(mapping, form, request, response);
		String path = execute(ad);
		
		ActionForward forward = null;
		if (path!=null) {
			forward = mapping.findForward(path);
		}
		
		return forward;
	}
	
	/**
	 * ��Action��execute������װ���Լ�ԭexecute�Ĳ�������
	 * 
	 * @param data - Action�����ķ�װ
	 * @return ��Ч��mapping·��
	 * @throws Exception
	 */
	abstract public String execute(IActionData data) throws Exception ;
	
	/**
	 * ���ݸ�execute
	 */
	private class ActionData implements IActionData {
		
		private ActionMapping map;
		private ActionForm fm;
		private HttpServletRequest req;
		private HttpServletResponse resp;
		private HttpSession ses;
		private PrintWriter out;
		
		private ActionData(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) 
		throws IOException
		{
			map = mapping;
			fm  = form;
			req = request;
			resp= response;
			ses = req.getSession();
			out =resp.getWriter();
		}
		
		public ActionForm getActionForm() {
			return fm;
		}

		public ActionMapping getActionMapping() {
			return map;
		}

		public Object getAttribute(String name) {
			return req.getAttribute(name);
		}

		public HttpServletRequest getHttpServletRequest() {
			return req;
		}

		public HttpServletResponse getHttpServletResponse() {
			return resp;
		}

		public String getParameter(String name) {
			return req.getParameter(name);
		}

		public Object getSessionAttribute(String name) {
			return ses.getAttribute(name);
		}

		public void print(Object data) {
			out.print(data);
		}

		public void setAttribute(String name, Object obj) {
			req.setAttribute(name, obj);
		}

		public void setSessionAttribute(String name, Object obj) {
			ses.setAttribute(name, obj);
		}
	}
}
