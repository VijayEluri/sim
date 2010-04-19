package jym.base.orm;

import java.util.List;

/**
 * ����ִ��sql-select����
 */
public interface ISelecter<T> {
	
	/**
	 * ʹ��model�е���Ч���Բ�ѯ�����
	 * 
	 * @param model - bean����
	 * @param join - where�Ӿ�ÿ���߼������ӷ�ʽ and/or
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
	public Class<T> getModelClass();
	
}
