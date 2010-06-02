// CatfoOD 2009-12-14 ����09:33:05

package jym.sim.tags;

import java.io.PrintWriter;

public class PaginationTag extends HtmlTagBase {
	/** tagԪ��class���Ե�ǰ׺ */
	public final static String CSS_CLASS_NAME = "sim_pagination";
	public final static String CSS_CLASS_FIRSTPAGE = "sim_pagination_firstpage";
	public final static String CSS_CLASS_LASTPAGE = "sim_pagination_lastpage";
	public final static String CSS_CLASS_JUMPPAGE = "sim_pagination_page_jump";
	public final static String CLASS_ATTR = "class";
	public final static String HREF_ATTR = "href";
	public final static String TAG_A = "a";
	
	private int current;
	private int total;
	private int dispsize;
	private String url;
	
	/**
	 * ���ɷ�ҳhtml
	 */
	public PaginationTag() {
		super("div");
		super.addAttribute(CLASS_ATTR, CSS_CLASS_NAME);
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
	public void setUrlPattern(String _url) {
		url = _url; //up.replaceAll("%page", "%d");
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
		
	if (start+1>end) return;
		
		ITag stag = new HtmlTagBase(TAG_A);
		stag.addAttribute(HREF_ATTR, getUrl(1));
		stag.addAttribute(CLASS_ATTR, CSS_CLASS_FIRSTPAGE);
		stag.append("��ҳ");
		append(stag);
		
		for (int i=start; i<=end; ++i) {
			appendSpace();
			ITag atag = new HtmlTagBase(TAG_A);
			atag.addAttribute(CLASS_ATTR, CSS_CLASS_JUMPPAGE);
			if (i!=current) {
				atag.addAttribute(HREF_ATTR, getUrl(i));
				atag.append(String.valueOf(i));
			} else {
				atag.append("["+ (i) +"]");
			}
			append(atag);
			appendSpace();
		}
		
		ITag etag = new HtmlTagBase(TAG_A);
		etag.addAttribute(HREF_ATTR, getUrl(total));
		etag.addAttribute(CLASS_ATTR, CSS_CLASS_LASTPAGE);
		etag.append("ĩҳ");
		append(etag);
	}
	
	private void appendSpace() {
		append("&nbsp;");
	}
	
	private String getUrl(int page) {
		return url.replaceAll("%page", Integer.toString(page));
	}
}
