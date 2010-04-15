package jym.base.orm;

import java.util.List;

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
	
	/**
	 * ʹ��model�е���Ч���Բ�ѯ�����
	 * 
	 * @param model - bean����
	 * @param join - where�Ӿ�����ӷ�ʽ and/or/not
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
}
