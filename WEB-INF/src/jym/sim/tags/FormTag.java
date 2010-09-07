// CatfoOD 2010-9-7 ����03:44:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.tags;

public class FormTag extends HtmlTagBase {

	public FormTag() {
		super("form");
	}

	/**
	 * ���ݽ���uri
	 */
	public void setAction(String uri) {
		super.addAttribute("action", uri);
	}
	
	/**
	 * ���һ������
	 * 
	 * @param type - ��������
	 * @param name - ��������
	 * @return �������������
	 */
	public ITag addInput(String type, String name) {
		ITag div = super.create("div");
		
		ITag lable = div.create("span");
		lable.append(name);
		
		ITag input = div.create("input");
		input.addAttribute("type", type);
		input.addAttribute("name", name);
		
		return input;
	}
	
	/**
	 * ���һ�������ı���
	 * 
	 * @param name - ��������
	 * @return �������������
	 */
	public ITag addInput(String name) {
		return addInput("text", name);
	}
	
	/**
	 * �����ݽ���ť
	 * @param label - �ݽ���ť������ֵ
	 * @return ���������ť
	 */
	public ITag addSubmit(String label) {
		ITag submit = addInput("submit", "");
		submit.addAttribute("value", label);
		return submit;
	}
}
