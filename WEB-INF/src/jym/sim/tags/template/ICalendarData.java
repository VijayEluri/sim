// CatfoOD 2009-12-20 ����10:41:46

package jym.sim.tags.template;

import java.util.Calendar;

import jym.sim.tags.ITag;

public interface ICalendarData {
	/**
	 * ����Ҫ���������(��/��)
	 */
	Calendar getCalendar();
	
	/**
	 * ȡ��data�����ڵ��¼���������ʾ�ڸ�ʽ��
	 * 
	 * @param data - �����е�����
	 * @param disp - ���data�������к����¼���<br>
	 * 				����disp.append()��Ҫ��ʾ��������ӵ�������
	 */
	void event(Calendar data, ITag disp);
}
