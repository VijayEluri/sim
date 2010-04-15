package jym.base.orm;

import java.util.List;

import jym.base.sql.ISqlQuery;

/**
 * ����ִ��sql-select����
 */
public interface ISelecter<T> {
	
	/**
	 * ִ��select��ѯ
	 * 
	 * @param params -- setPrepareSql()ʱsql�����?�Ĳ���
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(Object ...params);
	
}
