// CatfoOD 2010-1-4 ����10:17:32

package jym.base.util;

import java.io.PrintWriter;

import jym.base.tags.IPrinter;

public final class ForwardProcess {
	
	/**
	 * ��exec�������͵�ͨ�ô�����
	 * 
	 * @param data - ���ݶ���
	 * @param obj - �����String���ͣ���StringΪ��Ч��mapping·��<br>
	 * 			�����IPrinter���ͣ����ӡ����������null·��<br>
	 * 			������������ͣ���ֱ�Ӱ�toString�Ľ��������ͻ���
	 * 
	 * @param back - ���objΪString���ͣ������bak.back()
	 * @throws Exception
	 */
	public static void exec(IServletData data, Object obj, ICallBack back)
	 throws Exception {
		
		PrintWriter out = data.getHttpServletResponse().getWriter();
		
		if (obj instanceof String) {
			back.back();
		}
		else if (obj instanceof IPrinter) {
			IPrinter prt = (IPrinter) obj;
			prt.printout(out);
		}
		else {
			out.print(obj);
		}
	}
	
}
