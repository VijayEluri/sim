package jym.base.orm;

import java.util.List;

/**
 * ����ִ��sql-select����
 */
public interface ISelecter<T> {
	
	/**
	 * ִ��select��ѯ, ����sql����е�$where�滻Ϊwhere�е�����
	 * 
	 * @param where - where��ѯ����, ���Բ���where�ؼ���
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(String where);
	
	/**
	 * ʹ��model�е���Ч���Բ�ѯ�����
	 * 
	 * @param model - bean����
	 * @param join - where�Ӿ�ÿ���߼������ӷ�ʽ and/or/not
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
}
