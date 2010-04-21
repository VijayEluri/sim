// CatfoOD 2009-11-10 ����02:03:27

package jym.sim.jstags;

import java.util.ArrayList;
import java.util.List;

import jym.sim.tags.ITag;

public class TableRowClick extends InnerScript {
	private List<String> funcs;

	public TableRowClick() {
		super("/jym/javascript/tableClick.js");
		funcs = new ArrayList<String>();
	}
	
	/**
	 * ���һ����������������У����������ĸ�ʽ��<br>
	 * 		function clickListener(row, rowindex) {}
	 * 
	 * <li>rowΪ��ǰ�����¼��ı���ж���</li>
	 * <li>rowindexΪ������ڵ��е���������0��ʼ���������ͷ</li>
	 * 
	 * <br/>������������jsurlָ������Դ
	 * 
	 * @param jsurl - �ű��ļ����ڵ���Դurl
	 * @param funname - ����������
	 */
	public void appendCall(String jsurl, String funname) {
		readJavaScript(jsurl);
		funcs.add(funname);
	}
	
	/**
	 * 	
	 * ע��˳��������������ڱ��������Ѿ����ؽ�����ʱ�����
	 *
	 */
	@Override
	public void setTarget(ITag tag) {
		for (int i=0; i<funcs.size(); ++i) {
			JSFunction func = new JSFunction("tableRowMouseOverListener");
			func.addString(tag.getID());
			func.add(funcs.get(i));
			callMethod(func);
			tag.append(this);
		}
	}
}
