// CatfoOD 2010-4-20 ����08:24:37 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;


public interface IExceptionHandle {

	/**
	 * ���exe()�׳��쳣���������ִ��
	 *  
	 * @param tr - �׳����쳣
	 * @param msg - �쳣��Ϣ
	 */
	public void exception(Throwable tr, String msg);
	
}
