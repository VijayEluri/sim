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

	/**
	 * ���ط�ҳ�������е���ʼ����, ��һ�����ݵ�������Ϊ<b>1</b><br>
	 * <br>
	 * <b>��ǰҳ(current)��ֵӦ���Ѿ�������, ��ҳ���ݳ���(onesize)Ҳ������Ч</b>
	 */
	public int getStartPosition() {
		return (getCurrent() - 1) * getOnesize() + 1;
	}
	
	/**
	 * �����������������������<br>
	 * �˽����ת��Ϊ��ҳ��<br>
	 * <br>
	 * <b>��ҳ���ݳ���(onesize)Ҳ������Ч<b/>
	 */
	public void setTotalRow(int rownum) {
		int tp = rownum / getOnesize();
		int tc = rownum % getOnesize();
		if (tc>0) tp++;
		setTotal(tp);
	}
	
	/**
	 * ���ط�ҳ�������еĽ�������, ��һ�����ݵ�������Ϊ<b>1</b><br>
	 * <br>
	 * <b>��ǰҳ(current)��ֵӦ���Ѿ�������, ��ҳ���ݳ���(onesize)Ҳ������Ч</b>
	 */
	public int getEndPosition() {
		return getStartPosition() + getOnesize() - 1;
	}
}
