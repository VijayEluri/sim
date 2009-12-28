// CatfoOD 2009-10-20 ����08:44:10

package jym.base.tags;

import java.io.PrintWriter;

/**
 * Xml��ǣ����Դ����ӱ�����ı��ڵ�
 */
public interface ITag extends IPrinter {
	final String SP = "&nbsp;";
	
	/**
	 * ����һ��xmlԪ�ؽڵ�
	 * @param newtagname - Ԫ�ص�����
	 * @return xmlԪ�ؽڵ�
	 */
	ITag creat(String newtagname);
	
	/**
	 * ����һ���ı��ڵ�
	 * @return - �ı��ڵ�������
	 */
	PrintWriter creatText();
	
	/**
	 * ��ӱ�ǩ������
	 * @param name - ���Ե�����
	 * @param value - ���Ե�ֵ
	 */
	void addAttribute(String name, String value);
	
	/**
	 * ֱ������ı�����ǰλ�ã�Ƶ��ʹ��Ӱ��Ч��<br/>
	 * �ɹ�����true
	 */
	boolean append(String text);
	
	/**
	 * ��ӱ�ǩ����ǰ��ǩ���ݵĽ�β<br/>
	 * �ɹ�����true
	 */
	boolean append(ITag tag);
	
	/**
	 * ������Թرձ�Ƿ���true<br>
	 * �Թرձ�ǲ�������ӱ�ǵȷ���
	 */
	boolean isEndSelf();
	
	/**
	 * ȡ�ñ�ǩ��ID
	 */
	String getID();
	
	/**
	 * ���ر�ǩ��
	 */
	String getTagName();
}
