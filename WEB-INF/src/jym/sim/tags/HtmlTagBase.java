// CatfoOD 2009-11-10 ����07:53:09

package jym.sim.tags;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.sim.jstags.IJavaScript;
import jym.sim.util.ResourceLoader;

/**
 * html���
 */
public class HtmlTagBase extends TagBase {
	
	private ArrayList<IJavaScript> innerjs;
	private Map<Object, URL> amassjs;
	private PrintWriter insertText;
	
	
	public HtmlTagBase(String tagname) {
		super(tagname);
		innerjs = new ArrayList<IJavaScript>();
		insertText = super.createText();
	}

	/**
	 * ���һ��IJavaScript����Ϊ��xml��ǲ�ͬ<br/>
	 * ���tag��һ��IJavaScript�ű��ᱻ����ڱ�ǽ�β�ĺ���
	 */
	@Override
	public boolean append(ITag tag) {
		boolean r;
		if (tag instanceof IJavaScript) {
			r = innerjs.add((IJavaScript) tag);
			send2Root((IJavaScript) tag);
		} else {
			r = super.append(tag);
		}
		return r;
	}
	
	public ITag create(String newtagname) {
		HtmlTagBase tag = null;
		if (!super.isEndSelf()) {
			tag = new HtmlTagBase(newtagname);
			if (!super.append(tag)) {
				tag = null;
			}
		}
		return tag;
	}

	@Override
	public void printout(PrintWriter out) {
		if (amassjs!=null)  {
			printAmassJs();
			clearAmassJs();
		}
		
		super.printout(out);
		
		for (int i=0; i<innerjs.size(); ++i) {
			IJavaScript ijs = innerjs.get(i);
			ijs.printout(out);
		}

	}

	/**
	 * ���ⲿ�ű����͵���Ԫ��
	 * @param ijs
	 */
	private void send2Root(IJavaScript ijs) {
		ITag root = super.getRoot();

		if (root!=null) {
			if (root instanceof HtmlTagBase) {
				HtmlTagBase rh = (HtmlTagBase) root;
				rh.amass(ijs);
			} else {
				System.out.println("WARN: ["
						+ root.getTagName() + "] is not HtmlTag");
			}
		}
	}
	
	/**
	 * ��ӡ�ű�
	 * @param target - ���Ŀ��
	 * @param jsmap - �ű�����
	 */
	private void printAmassJs() {		
		insertText.append("<script language='JavaScript'>");
		
		Iterator<URL> it = amassjs.values().iterator();
		while (it.hasNext()) {
			ResourceLoader.urlWriteOut(it.next(), insertText);
		}
		
		insertText.append("</script>");
	}
	
	/**
	 * ����ռ����ýű�����Ϊ�ű��Ѿ���д��
	 */
	private void clearAmassJs() {
		amassjs = null;
	}
	
	/** 
	 * �ռ�ijs�е����нű� <br/>
	 * ͬʱ������������ù���˵����ǰԪ���Ǹ�Ԫ��
	 */
	private void amass(IJavaScript ijs) {
		if (amassjs==null) {
			amassjs = new HashMap<Object, URL>();
		}
		amassjs.putAll(ijs.getInnerJs());
	}
}
