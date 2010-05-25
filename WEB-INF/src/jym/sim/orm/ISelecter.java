package jym.sim.orm;

import java.util.List;

import jym.sim.orm.page.IPage;
import jym.sim.orm.page.PageBean;

/**
 * ����ִ��sql-select����
 */
public interface ISelecter<T> {
	
	/**
	 * ʹ��model�е���Ч���Բ�ѯ�����,����ҳ
	 * 
	 * @param model - bean����,������е����Զ���Ч����������������������
	 * @param join - where�Ӿ�ÿ���߼������ӷ�ʽ and/or
	 * @return T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(T model, String join);
	
	/**
	 * ʹ��model�е���Ч���Բ�ѯ�����,��ҳ<br/>
	 * �˷������غ�,page.total���Խ�������Ϊ��ҳ��
	 * 
	 * @param model -  bean����,������е����Զ���Ч����������������������
	 * @param join - where�Ӿ�ÿ���߼������ӷ�ʽ and/or
	 * @param page - ��ҳ����
	 * @return - T�����б� -- ���û�ж��󷵻�,�򷵻ؿյ�List(List.size()==0)
	 */
	public List<T> select(T model, String join, PageBean page);
	
	/**
	 * �������ݿ�ķ�ҳ����,�˷���������jym.sim.orm.page.*��<br/>
	 * Ĭ�ϲ���ҳ
	 * @param plot - ���ݿ��ҳ������
	 */
	public void setPaginationPlot(IPage plot);
	
	/**
	 * ����bean�����class
	 */
	public Class<T> getModelClass();
	
}
