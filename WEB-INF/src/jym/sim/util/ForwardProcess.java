// CatfoOD 2010-1-4 ����10:17:32

package jym.sim.util;

import java.io.PrintWriter;

import jym.sim.json.IjSon;
import jym.sim.tags.IPrinter;

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
		else if (obj instanceof IjSon) {
			IjSon json = (IjSon) obj;
			json.go(out);
		}
		else {
			out.print(obj);
		}
	}
	
}
