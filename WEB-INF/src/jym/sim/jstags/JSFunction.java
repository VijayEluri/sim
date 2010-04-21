// CatfoOD 2009-11-10 ����02:10:56

package jym.sim.jstags;

import java.util.ArrayList;
import java.util.List;

/**
 * ��װjavaScript����
 */
public class JSFunction {
	private String fname;
	private List<String> args;
	
	/**
	 * ��װһ��js��������
	 * 
	 * @param funcname - js������
	 */
	public JSFunction(String funcname) {
		fname = funcname;
		args = new ArrayList<String>();
	}
	
	/**
	 * ��arg��Ϊ������ӵ�js���������б��ĩβ<br/>
	 * ���arg��Ϊnull,arg��ȡֵΪObject.toString(),����Ϊnull<br/>
	 * ������Ϊ���������ݣ��������Ű�Χ��
	 * 
	 * @param arg - ����
	 */
	public void add(Object arg) {
		String s = "null";
		if (arg!=null) {
			s = arg.toString();
		}
		args.add(s);
	}
	
	/**
	 * ��arg��Ϊ������ӵ�js���������б��ĩβ<br/>
	 * ���arg��Ϊnull,arg��ȡֵΪObject.toString(),����Ϊ"null"<br/>
	 * ������Ϊ�ַ��������ݣ������Ű�Χ��
	 * 
	 * @param arg - ����
	 */
	public void addString(Object arg) {
		add("\"" + arg + "\"");
	}
	
	public String toString() {
		return getCallString();
	}
	
	/**
	 * ���ص���js�����ı��ʽ�ַ���
	 */
	public String getCallString() {
		StringBuilder buff = new StringBuilder();
		buff.append(fname);
		buff.append('(');
		int size = args.size();
		for (int i=0; i<size; ++i) {
			buff.append(args.get(i));
			if (i<size-1) {
				buff.append(',');
			}
		}
		buff.append(')');
		buff.append(';');
		return buff.toString();
	}
	
	public static void main(String[] s) {
		JSFunction js = new JSFunction("func");
		js.add("ssss");
		System.out.println(js);
	}
}
