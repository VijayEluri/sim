// CatfoOD 2009-12-14 ����09:33:05

package jym.sim.tags;

import java.io.PrintWriter;

public class PaginationTag extends HtmlTagBase {
	
	public final static String CSS_CLASS_NAME = "sim_pagination";
	
	private int current;
	private int total;
	private int dispsize;
	private String url;
	
	/**
	 * ���ɷ�ҳhtml
	 */
	public PaginationTag() {
		super("div");
		super.addAttribute("class", CSS_CLASS_NAME);
		dispsize = 10;
	}

	/**
	 * ��ǰͣ����ҳ��,ҳ����Чֵ1~*
	 */
	public void setCurrentPage(int p) {
		current = p;
	}
	
	/**
	 * ��ҳ��
	 */
	public void setTotalPage(int t) {
		total = t;
	}
	
	/**
	 * ��ҳ��ʾ������
	 */
	public void setDisplaySize(int s) {
		dispsize = s/2;
	}
	
	/**
	 * ���ó������ַ������ַ����е�%pageת��Ϊҳ��ֵ
	 */
	public void setUrlPattern(String up) {
		url = up.replaceAll("%page", "%d");
	}

	@Override
	public void printout(PrintWriter out) {
		createPagination();
		super.printout(out);
	}
	
	private void createPagination() {
		int start = current - dispsize;
		if (start<1) start = 1;
		int end = current + dispsize;
		if (end>total) end = total;
		
	if (start+2>end) return;
		
		ITag stag = new HtmlTagBase("a");
		stag.addAttribute("href", String.format(url, 1));
		stag.append("<<<");
		append(stag);
		
		for (int i=start; i<=end; ++i) {
			appendSpace();
			ITag atag = new HtmlTagBase("a");
			if (i!=current) {
				atag.addAttribute("href", String.format(url, i));
				atag.append(String.valueOf(i));
			} else {
				atag.append("["+ (i) +"]");
			}
			append(atag);
			appendSpace();
		}
		
		ITag etag = new HtmlTagBase("a");
		etag.addAttribute("href", String.format(url, total));
		etag.append(">>>");
		append(etag);
	}
	
	private void appendSpace() {
		append("&nbsp;");
	}
}
