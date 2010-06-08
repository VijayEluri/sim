// CatfoOD 2010-6-8 ����03:21:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Savepoint;

/**
 * һ�����ݿ�Ự
 */
public interface IJdbcSession {
	/**
	 * �Ƿ�������ִ�к��Զ��ݽ�
	 */
	public boolean isAutoCommit();
	/**
	 * �����Ƿ��Զ��ݽ�,<b>�������������Ҫ��״̬�Ļ�</b>
	 */
	public void setCommit(boolean isAuto);
	/**
	 * �ڵ�ǰ�����д���һ��δ�����ı���� (savepoint)�������ر�ʾ������ Savepoint ���� 
	 * ����ڻ����Χ֮����� setSavepoint�������´����ı��������������
	 * 
	 * @return - ������������ʧ��,�ڹرյ������ϵ��ô˷���,���ߵ�ǰ�����Զ��ύģʽ�� ����null
	 */
	public Savepoint setSavepoint(); 
	/**
	 * �ڵ�ǰ�����д���һ�����и������Ƶı���㣬�����ر�ʾ������ Savepoint ���� 
	 * ����ڻ����Χ֮����� setSavepoint�������´����ı�������������� 
	 * 
	 * @return - ������������ʧ��,�ڹرյ������ϵ��ô˷���,���ߵ�ǰ�����Զ��ύģʽ�� ����null
	 */
	public Savepoint setSavepoint(String name);
	/**
	 * �ӵ�ǰ�������Ƴ�ָ���� Savepoint �ͺ��� Savepoint ����
	 * �����Ƴ������֮�󣬶Ըñ������κ����ö���ʧ��
	 * 
	 * @param savepoint - ���Ƴ��� Savepoint ����
	 * @return - �ɹ�����true
	 */
	public boolean releaseSavepoint(Savepoint savepoint);
	/**
	 * ȡ���ڵ�ǰ�����н��е����и��ģ����ͷŴ� Connection ����ǰ���е��������ݿ�����
	 * �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * 
	 * @return - �ع��ɹ�����true.
	 * 		����������ݿ���ʴ����ڲ���ֲ�ʽ�����ͬʱ���ô˷�����
	 * 		�ڹرյ������ϵ��ô˷��������ߴ� Connection �������Զ��ύģʽ�򷵻�false
	 */
	public boolean rollback();
	/**
	 * ȡ���������ø��� Savepoint ����֮����еĸ��ġ� 
	 * �˷���ֻӦ�����ѽ����Զ��ύʱʹ�á�.
	 * 
	 * @param savepoint - Ҫ�ع����� Savepoint ����
	 * @return - �ع��ɹ�����true.
	 * 		����������ݿ���ʴ����ڲ���ֲ�ʽ�����ͬʱ���ô˷�����
	 * 		�ڹرյ������ϵ��ô˷��������ߴ� Connection �������Զ��ύģʽ�򷵻�false
	 */
	public boolean rollback(Savepoint savepoint);
	/**
	 * ʹ������һ���ύ/�ع�����еĸ��ĳ�Ϊ�־ø��ģ�
	 * ���ͷŴ� Connection ����ǰ���е��������ݿ�����
	 * �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * 
	 * @return �ݽ��ɹ�����true
	 */
	public boolean commit();
}
