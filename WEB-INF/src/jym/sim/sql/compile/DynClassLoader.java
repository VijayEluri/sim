// CatfoOD 2010-9-10 ����01:43:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;


/**
 *  <b>�����ظ�����ͬһ���࣬���봴����������ʵ��</b><br>
 *  <br>
 *  �ɲ�ͬ�������ʵ���ظ�ǿ�Ƽ���(���ж�������defineClass����)<br>
 *  ͬһ���Ͳ�������java.lang.LinkageError����<br>
 *  ���Ǽ��ؽ����Ӧ��Class����ʵ���ǲ�ͬ�ģ���ʵ�����ǲ�ͬ������(��Ȼ����+������ͬ).<br> 
 *  ���ǿ��ת��ʹ�ã�������ClassCastException.<br>
 *  <br>
 *  <b>��ͬ</b>�����������<b>ͬ��</b>���ͣ�ʵ�ʵõ��Ľ����ʵ�ǲ�ͬ���ͣ�<br> 
 *  ��JVM��һ��������ȫ����һ��������ClassLoader��ʵ����ΪΨһ��ʶ��<br>
 *  <b>��ͬ����������ص��ཫ�����ڲ�ͬ�������ռ�.</b><br>
 *  <br>
 */
public class DynClassLoader extends ClassLoader {
	
	private static final String CLASS = ".class";
	private static final Map<String, Cache> cache = new HashMap<String, Cache>();

	
	public Class<?> reLoadClass(String classname) throws ClassNotFoundException {
		
		URL url = openUrl(classname);
		
		File file = new File(url.getFile());
		long lastModify = file.lastModified();
		
		Cache cc = getCache(classname, lastModify);
		
		if (cc==null) {
			byte[] bin = readFromFile(url, file.length());
			cc = createCache(classname, bin, lastModify);
		}
		
		return cc.clazz;
	}
	
	private Cache createCache(String classname, byte[] classbyte, long lastModify) {
		Cache cc = new Cache();
		cc.clazz = super.defineClass(classname, classbyte, 0, classbyte.length);
		cc.modify = lastModify;
		cache.put(classname, cc);
		
		return cc;
	}
	
	/**
	 * ���������class���޸�ʱ�����ֽ����ļ����޸�ʱ�䲻ͬ������Ϊ
	 * ��class�Ķ����Ѿ����޸ģ���ʱ����null
	 */
	private Cache getCache(String classname, long lastModify) {
		Cache cc = cache.get(classname);
		if (cc!=null) {
			if (cc.modify!=lastModify) {
				cache.remove(classname);
				cc = null;
			}
		}
		return cc;
	}
	
	private byte[] readFromFile(URL url, long fileLen) throws ClassNotFoundException {
		byte[] bin = new byte[(int)fileLen];
		try {
			url.openStream().read(bin);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
		return bin;
	}
	
	private URL openUrl(String classname) throws ClassNotFoundException {
		char[] chs = classname.toCharArray();
		Tools.replaceAll(chs, '.', '/');
		
		String name = "/" + new String(chs) + CLASS;
		URL u = super.getClass().getResource(name);
		
		if (u==null) {
			throw new ClassNotFoundException(name);
		}
		
		return u;
	}

	
	private static class Cache {
		private Class<?> clazz;
		private long modify;
	}
}
