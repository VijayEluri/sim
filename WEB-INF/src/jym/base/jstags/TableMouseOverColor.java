// CatfoOD 2009-11-10 ����01:39:13

package jym.base.jstags;

import jym.base.tags.ITag;

/**
 * ������ǩ������ɫЧ��
 */
public class TableMouseOverColor extends InnerScript {
	private String fc;
	private String sc;
	private String mc;
	
	/**
	 * ʹ��Ĭ����ɫ��ʼ��
	 */
	public TableMouseOverColor() {
		this("#ddd", "#f0f0f0", "#faa");
	}
	
	/**
	 * �����ɫ��ʼ��,��ɫ�ַ�������Ч��css��ɫ
	 * 
	 * @param fcolor - �����е���ɫ
	 * @param scolor - ż���е���ɫ
	 * @param mcolor - �����ͣ����ɫ
	 */
	public TableMouseOverColor(String fcolor, String scolor, String mcolor) {
		super("/jym/javascript/tablecolor.js");
		fc = fcolor;
		sc = scolor;
		mc = mcolor;
	}

	@Override
	public void setTarget(ITag tag) {
		callMethodString("changeTableColor", tag.getID(), fc, sc, mc);
		tag.append(this);
	}
}
