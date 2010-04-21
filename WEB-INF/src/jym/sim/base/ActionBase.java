// CatfoOD 2009-12-30 ����12:44:05

package jym.sim.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.ForwardProcess;
import jym.sim.util.ICallBack;
import jym.sim.util.ServletData;

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
	 * ���಻Ҫ�����������,ActionBase��Struts1��execute���������˷�װ<br>
	 * ִ�з�ʽ����execute(IActionData)������ע��ʵ��
	 */
	@Override
	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionData ad = new ActionData(mapping, form, request, response);
		prepositive(ad);
		Object obj = execute(ad);
		
		ForwardCallBack fcb = new ForwardCallBack(mapping, obj);
		ForwardProcess.exec(ad, obj, fcb);
		
		return fcb.getForward();
	}
	
	/**
	 * ��execute����ִ��ǰ���ṩ��������Ļ��ᣬĬ��ʲô������
	 * 
	 * @param data - Action�����ķ�װ
	 */
	protected void prepositive(IActionData data) throws Exception {
	}
	
	/**
	 * ��Action��execute������װ���Լ�ԭexecute�Ĳ�������
	 * 
	 * @param data - Action�����ķ�װ
	 * @return	�������String���ͣ���StringΪ��Ч��mapping·��<br>
	 * 			�������IPrinter���ͣ����ӡ����������null·��<br>
	 * 			��������������ͣ���ֱ�Ӱ�toString�Ľ��������ͻ��ˣ�
	 * 			������null·��<br>
	 * 
	 * @throws Exception
	 */
	abstract public Object execute(IActionData data) throws Exception ;
	
	/**
	 * ���ݸ�execute
	 */
	private class ActionData extends ServletData implements IActionData {
		
		private ActionMapping map;
		private ActionForm fm;
		
		private ActionData(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) 
		throws IOException
		{
			super(request, response);
			map = mapping;
			fm  = form;
		}
		
		public ActionForm getActionForm() {
			return fm;
		}

		public ActionMapping getActionMapping() {
			return map;
		}		
	}
	
	private class ForwardCallBack implements ICallBack {
		private Object obj;
		private ActionForward forward;
		private ActionMapping mapping;
		
		private ForwardCallBack(ActionMapping map, Object ob) {
			obj = ob;
			mapping = map;
			forward = null;
		}
		
		public void back() throws Exception {
			forward = mapping.findForward( (String)obj );
		}
		
		public ActionForward getForward() {
			return forward;
		}
	}
}
