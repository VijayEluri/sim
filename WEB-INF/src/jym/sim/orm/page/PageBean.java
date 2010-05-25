// CatfoOD 2010-5-24 ����09:44:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * �洢��ҳ���ݵ���
 */
public class PageBean {
	
	public final static int DEFAULT_CURRENT_PAGE = 1;
	public final static int DEFAULT_TOTAL_PAGE = 1;
	public final static int DEFAULT_ONE_SIZE = 20;
	
	// ��ǰҳ,��1��ʼ
	private int current;
	// ҳ������
	private int total;
	// һҳ��ʾ������
	private int onesize;
	
	/**
	 * Ĭ��ֵ: current = 1, total = 1, onesize = 20
	 */
	public PageBean() {
		current = DEFAULT_CURRENT_PAGE;
		total = DEFAULT_TOTAL_PAGE;
		onesize = DEFAULT_ONE_SIZE;
	}

	/**
	 * ��ǰҳ����Чֵ1~*
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * ��ҳ������Чֵ 1~*
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * ��ǰҳ����Чֵ1~*
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	/**
	 * ��ҳ������Чֵ 1~*<br>
	 * ���total<=0����1
	 */
	public void setTotal(int total) {
		if (total<=0) total = 1;
		this.total = total;
	}

	/**
	 * Ĭ��ֵ20
	 */
	public int getOnesize() {
		return onesize;
	}

	public void setOnesize(int onesize) {
		this.onesize = onesize;
	}

}
