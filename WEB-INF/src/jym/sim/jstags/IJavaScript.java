// CatfoOD 2009-11-10 ����01:46:53

package jym.sim.jstags;

import java.net.URL;
import java.util.Map;

import jym.sim.tags.ITag;

/**
 * js�ű���ǩ
 */
public interface IJavaScript extends ITag {
	
	/**
	 * ��jsӦ�õ�Ŀ���ǩ�У�����js��ǩ��������
	 * 
	 * @param tag - Ŀ���ǩ
	 */
	void setTarget(ITag tag);
	
	/**
	 * ����fromfile�ƶ���js�ļ��������<br/>
	 * fromfileΪ��׼�ⲿjs�ļ�
	 * 
	 * @param fromfile - js�ļ�·������Class.getResource()�м����ļ�
	 */
	void readJavaScript(String fromfile);
	
	/**
	 * �����ⲿjs����ǩ��
	 * 
	 * @param fromurl - js�ļ���url
	 */
	void readJavaScript(URL fromurl);
	
	/**
	 * ���ش��ļ����صĽű�URL
	 * Object �ǽű��ı�ʶ�������ʶ�ظ�����ű�ֻ�����һ��
	 */
	Map<Object, URL> getInnerJs();
}
