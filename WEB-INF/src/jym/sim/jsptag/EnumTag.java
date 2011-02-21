// CatfoOD 2010-5-11 ����02:28:03 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import jym.sim.util.Tools;

public class EnumTag extends SelectTag {

	private static final long serialVersionUID = 4858481581469242308L;
	private static final String UNKONW = "δֵ֪";
	private String key;

	public int doStartTag() throws JspException {
		super.init();
		try {
			do_enum();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private void do_enum() throws IOException {
		Map<String, String> obj = super.getEnumMap();
		
		if (obj==null) {
			print("����������");
			return;
		}
	
		String v = obj.get(key);
		if (v!=null) {
			print(v);
		} else {
			print(UNKONW+key);
			Tools.pl("EnumTag: key����ֵ��Ч:" + key + " ��:" + getEnumObjName());
		}
	}
	
	public void setName(String name) {
		super.setName(name);
	}
	
	public void setClazz(String name) {
		super.setClazz(name);
	}
	
	public void setField(String name) {
		super.setField(name);
	}
	
	public void setKey(String k) {
		key = k;
	}
}
