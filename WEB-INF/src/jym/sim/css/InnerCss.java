// CatfoOD 2009-12-21 ����08:34:38

package jym.sim.css;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jym.sim.tags.HtmlTagBase;
import jym.sim.tags.ITag;
import jym.sim.util.ResourceLoader;

public final class InnerCss extends HtmlTagBase {
	private List<Css> cs;

	public InnerCss() {
		super("style");
		cs = new ArrayList<Css>();
	}
	
	/**
	 * ���ⲿ�ļ���������ʽ��
	 * @param fromClassPath - ��ʽ�����ڵ�·��,·���� '/' ��ʼ
	 */
	public InnerCss(String fromClassPath) {
		this();
		
		URL url = ResourceLoader.getUrl(fromClassPath);
		if (url!=null) {
			PrintWriter out = this.creatText();
			ResourceLoader.urlWriteOut(url, out);
		} else {
			System.out.println("InnerCss: �Ҳ���url ("+fromClassPath+")");
		}
	}

	@Override
	public void printout(PrintWriter out) {
		printCss(out);
		super.printout(out);
	}
	
	private void printCss(PrintWriter out) {
		Iterator<Css> it = cs.iterator();
		while (it.hasNext()) {
			it.next().printout(out);
		}
	}

	/**
	 * ֱ������ʽ��������ı���ע���ʽ
	 */
	@Override
	public boolean append(String text) {
		return super.append(text);
	}
	
	/**
	 * ����ʽ���������ʽ
	 */
	public boolean append(Css css) {
		return cs.add(css);
	}
	
	/**
	 * ֱ������ʽ��������ı���ע���ʽ
	 */
	@Override
	public PrintWriter creatText() {
		return super.creatText();
	}

	// ------------------------------------- ��֧�ֵķ���
	
	private void unsupport() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean append(ITag tag) {
		unsupport();
		return false;
	}

	@Override
	public ITag creat(String newtagname) {
		unsupport();
		return null;
	}
	
	@Override
	public void addAttribute(String name, String value) {
		unsupport();
		super.addAttribute(name, value);
	}
}
