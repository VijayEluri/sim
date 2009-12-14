// CatfoOD 2009-11-10 ����09:31:24

package jym.base.jstags;

import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.base.tags.HtmlTagBase;
import jym.base.tags.ITag;

/**
 * Ƕ��html�Ľű�
 */
public class InnerScript extends HtmlTagBase implements IJavaScript {
	private Map<Object, URL> jss;
	
	/**
	 * ����һ���յ��ڲ��ű�
	 */
	public InnerScript() {
		super("script");
		this.addAttribute("language", "JavaScript");
		jss = new HashMap<Object, URL>();
	}
	
	/**
	 * ����һ���ڲ��ű���������fromfile�ƶ���js�ļ��������
	 * 
	 * @param fromfile - js�ļ�·������Class.getResource()�м����ļ�
	 */
	public InnerScript(String fromfile) {
		this();
		readJavaScript(fromfile);
	}
	
	public void readJavaScript(String fromfile) {
		URL url = null;
		Class<?> c = this.getClass();
		
		while (url==null) {
			url = c.getResource(fromfile);
			ClassLoader loader = c.getClassLoader();
			
			if (loader!=null) {
				c = loader.getClass();
			} else {
				break;
			}
		}
		
		readJavaScript(url);
	}
	
	public void readJavaScript(URL fromurl) {
		if (fromurl!=null) {
			jss.put(fromurl, fromurl);
		} else {
			throw new IllegalArgumentException("cannot find file: " + fromurl);
		}
	}
	
	public Map<Object, URL> getInnerJs() {
		return jss;
	}
	
	/**
	 * ����js����,ȫ�����������ַ������Զ������Ű�Χ<br/>
	 * ��ʵ�ʰѸ�ʽ���õĺ������ӵ��������
	 * 
	 * @param method - js����������������
	 * @param args - �����б��Զ������Ű�Χ
	 */
	public void callMethodString(String method, Object ...args) {
		JSFunction jsf = new JSFunction(method);
		for (int i=0; i<args.length; ++i) {
			jsf.addString(args[i]);
		}
		callMethod(jsf);
	}
	
	/**
	 * ����js����
	 * 
	 * @param func
	 */
	public void callMethod(JSFunction func) {
		append(func.getCallString());
	}
	
	/**
	 * ����js����,ע���ַ�������Ӧ�������Ű�Χ<br/>
	 * ��ʵ�ʰѸ�ʽ���õĺ������ӵ��������
	 * 
	 * @param method - js����������������
	 * @param args - �����б�
	 */
	public void callMethod(String method, Object ...args) {
		JSFunction jsf = new JSFunction(method);
		for (int i=0; i<args.length; ++i) {
			jsf.add(args[i]);
		}
		callMethod(jsf);
	}

	/**
	 * ��ӽű��ַ���
	 */
	@Override
	public boolean append(String text) {
		return super.append(text);
	}
	
	/**
	 * ��ű���д����
	 */
	@Override
	public PrintWriter creatText() {
		return super.creatText();
	}

	/**
	 * ��֧�ֵķ���
	 */
	@Override
	public ITag creat(String newtagname) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * ��֧�ֵķ���
	 */
	@Override
	public boolean append(ITag tag) {
		throw new UnsupportedOperationException();
	}

	public void setTarget(ITag tag) {
	}
}
