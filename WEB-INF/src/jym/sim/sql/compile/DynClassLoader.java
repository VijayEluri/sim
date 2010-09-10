// CatfoOD 2010-9-10 ����01:43:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;


/**
 *  �ɲ�ͬ�������ʵ���ظ�ǿ�Ƽ���(���ж�������defineClass����)<br>
 *  ͬһ���Ͳ�������java.lang.LinkageError����<br>
 *  ���Ǽ��ؽ����Ӧ��Class����ʵ���ǲ�ͬ�ģ���ʵ�����ǲ�ͬ������(��Ȼ����+������ͬ).<br> 
 *  ���ǿ��ת��ʹ�ã�������ClassCastException.<br>
 *  <br>
 *  <b>��ͬ</b>�����������<b>ͬ��</b>����ʵ�ʵõ��Ľ����ʵ�ǲ�ͬ���ͣ�<br> 
 *  ��JVM��һ��������ȫ����һ��������ClassLoader��ʵ����ΪΨһ��ʶ��<br>
 *  ��ͬ����������ص��ཫ�����ڲ�ͬ�������ռ�.
 */
public class DynClassLoader extends ClassLoader {
	
	private static final String CLASS = ".class";
	private static final Map<String, CacheClass> 	cache = new HashMap<String, CacheClass>();

	
	public Class<?> reLoadClass(String classname) throws ClassNotFoundException {
		
		char[] chs = classname.toCharArray();
		Tools.replaceAll(chs, '.', '/');
		
		String name = "/" + new String(chs) + CLASS;
		URL u = super.getClass().getResource(name);
		
		if (u==null) {
			throw new ClassNotFoundException(name);
		}
		
		File file = new File(u.getFile());
		CacheClass cc = cache.get(classname);
		if (cc!=null) {
			if (cc.modify==file.lastModified()) {
				return cc.clazz;
			}
		}
		
		byte[] bin = new byte[(int)file.length()];
		try {
			u.openStream().read(bin);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
		
		cc = new CacheClass();
		cc.clazz = super.defineClass(classname, bin, 0, bin.length);
		cc.modify = file.lastModified();
		cache.put(classname, cc);
		
		return cc.clazz;
	}

	
	private static class CacheClass {
		private Class<?> clazz;
		private long modify;
	}
}
