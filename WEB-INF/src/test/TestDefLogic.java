// CatfoOD 2010-7-12 ����10:06:36 yanming-sohu@sohu.com/@qq.com

package test;

import jym.sim.sql.logic.DefinitionLogic;
import jym.sim.util.Tools;

public class TestDefLogic {
	
	public static void main(String[] ss) {
		UserBean user = new UserBean();
		user.setBrongthname("������");
		user.setBrongthid("x3000");
		
		String s = "fdsaf=fdsaf";//"((bname = '${brongthname}') and (id = '${brongthid}'))";
		
		DefinitionLogic dl = new DefinitionLogic(s);
		Tools.pl( dl.w(null, null, user) );
	}
	
}
