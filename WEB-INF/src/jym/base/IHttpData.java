// CatfoOD 2010-1-4 ����09:56:18

package jym.base;

import jym.base.util.IServletData;

public interface IHttpData extends IServletData {
	/**
	 * ���أ�
	 * web.xml������beanclass���ԵĶ���ʹ��post/get������ʼ��
	 */
	Object getFormObj();
}
