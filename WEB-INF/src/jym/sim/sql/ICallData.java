// CatfoOD 2010-6-13 ����02:33:03 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.CallableStatement;

public interface ICallData {
	
	/**
	 * ���ش洢���̵�����
	 */
	public String getProcedureName();
	
	/**
	 * ���ش洢���̲����ĸ���
	 */
	public int getParameterCount();
	
	/**
	 * �Ƿ�ʹ�ô洢���̵ķ���ֵ,true�������´���:
	 * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
	 */
	public boolean hasReturnValue();
	
	/**
	 * ��CallableStatementѹ�����,ִ�й��̵���,���ز���<br/>
	 * CallableStatement�׳����쳣��������Ŀ��Բ���catch
	 */
	public void exe(CallableStatement cs) throws Exception;
}
