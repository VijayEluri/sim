// CatfoOD 2010-8-13 ����02:09:12 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import jym.sim.util.Tools;


/**
 * ��װ���ݿ��������
 */
public class Transaction {
	
	private IJsGeter getter;
	
	/**
	 * ΪIQuery�ӿ��ṩ����֧��
	 */
	public Transaction(final IQuery iq) {		
		this(new IJsGeter() {
			public IJdbcSession get() throws Exception {
				if (iq==null) {
					throw new NullPointerException("Transaction ��ʼ��ʱIQuery����Ϊnull");
				}
				return iq.getSession();
			}
		});
	}
	
	/**
	 * ͨ��IJsGeter�ӿ��ṩ����֧��
	 */
	public Transaction(IJsGeter g) {
		Tools.check(g, "Transaction ��ʼ��ʧ�ܣ���������Ϊnull");
		getter = g;
	}
	
	/**
	 * ��ʼһ������
	 * 
	 * @param handle - ��������Ķ���
	 * @return �������ɹ����ݽ�����null, ���򷵻�����ʧ�ܵ�ԭ��
	 */
	public Exception start(ITransactionHanle handle) {
		IJdbcSession js = null;
		Exception ex = null;
		
		try {
			js = getter.get();
			js.setCommit(false);
			
			handle.start();
			
			if (!js.commit()) {
				throw new Exception("Transaction �ݽ�ʧ�ܣ�δ֪�Ĵ���.");
			}
			
		} catch(Exception e) {
			if (js!=null) {
				js.rollback();
			}
			ex = e;
			
		} finally {
			if (js!=null) {
				js.setCommit(true);
			}
		}
		
		return ex;
	}
	
	/**
	 * �ṩIJdbcSession����
	 */
	public interface IJsGeter {
		IJdbcSession get() throws Exception;
	}
}
