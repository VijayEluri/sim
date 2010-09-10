// CatfoOD 2010-9-10 ����01:43:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
	

	public Class<?> reLoadClass(String classname) throws ClassNotFoundException {
		
		char[] chs = classname.toCharArray();
		Tools.replaceAll(chs, '.', '/');
		
		String name = "/" + new String(chs) + CLASS;
		URL u = super.getClass().getResource(name);
		
		if (u==null) {
			throw new ClassNotFoundException(name);
		}
		
		File file = new File(u.getFile());
		byte[] bin = new byte[(int)file.length()];
		
		try {
			u.openStream().read(bin);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
		
		return super.defineClass(classname, bin, 0, bin.length);
	}

}
