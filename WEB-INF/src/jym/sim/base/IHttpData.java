// CatfoOD 2010-1-4 ����09:56:18

package jym.sim.base;

import jym.sim.util.IServletData;

public interface IHttpData<TYPE> extends IServletData {
	/**
	 * ���أ�
	 * web.xml������beanclass���ԵĶ���ʹ��post/get������ʼ��
	 */
	TYPE getFormObj();
}
