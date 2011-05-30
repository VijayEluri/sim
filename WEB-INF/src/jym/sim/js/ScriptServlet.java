// CatfoOD 2010-9-9 ����09:23:15 yanming-sohu@sohu.com/@qq.com

package jym.sim.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.ResourceLoader;
import jym.sim.util.Tools;


/**
 * �ű�ת��
 */
public class ScriptServlet extends HttpServlet {

	private static final long serialVersionUID	= -1969856037757165065L;
	private static final long CURRENT_TIME		= System.currentTimeMillis();
	private static final String CONF_DEBUG		= "debug";
	private static final String CONF_NAME 		= "mappingConfigs";
	private static final String SYS_PATH			= "WEB-INF";
	private static final String FROM_CLASSPATH	= "classpath:";
	private static final String DEFAULT_CONF		= "/jym/javascript/js_mapping.conf";
	
	private Properties urimapping = new Properties();
	private boolean debug = false;
	
	
	@Override
	public void init() throws ServletException {
		loadConfFrom(DEFAULT_CONF);
		
		String conf = getInitParameter(CONF_NAME);
		if (conf!=null) {
			String[] files = conf.split(",");
		
			for (int i=0; i<files.length; ++i) {
				loadConfFrom(files[i]);
			}
		}
		
		debug = "true".equalsIgnoreCase(getInitParameter(CONF_DEBUG));
	}
	
	private void loadConfFrom(String file) {
		InputStream in = getClass().getResourceAsStream(file);
		
		if (in!=null) {
			try {
				urimapping.load(in);
				in.close();
			} catch (IOException e) {
				Tools.pl("��ȡ�ű�ӳ�������ļ�ʱ����? [" + file + "] " + e);
			}
		} else {
			Tools.pl("�ű�ӳ�������ļ�������? " + file);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String path = req.getServletPath();
		
		if (path.indexOf(SYS_PATH)>=0) {
			Tools.pl("��ֹ����ϵͳ��Դ: " + req.getRequestURL() 
					+ " - IP: " + req.getLocalAddr());
			
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	
		InputStream in = getResourceAsStream(path);
		if (in==null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		resp.setContentType("application/javascript;charset=utf-8");
		ServletOutputStream out = resp.getOutputStream();
		
		try {
			//XXX JSMin�ᵼ�²��ִ�����Ч?..������?
			boolean compress = !debug;
			if (compress) {
				compress = JSMin.compress(in, out);
			}
			if (!compress) {
				ResourceLoader.writeOut(in, out);
			}
		} finally {
			in.close();
		}
	}
	
	private InputStream getResourceAsStream(String path) {
		String map = urimapping.getProperty(path);
		
		if (map!=null) {
			if (map.startsWith(FROM_CLASSPATH)) {
				path = map.substring(FROM_CLASSPATH.length());
				return getClass().getResourceAsStream(path);
			} else {
				path = map;
			}
		}
		
		return getServletContext().getResourceAsStream(path);
	}

	@Override
	protected long getLastModified(HttpServletRequest req) {
		String path = req.getServletPath();
		String map  = urimapping.getProperty(path);
		
		if (map!=null) {
			if (map.startsWith(FROM_CLASSPATH)) {
				return this.hashCode();
			} else {
				path = map;
			}
		}
		
		File file = new File( getServletContext().getRealPath(path) );
		if (file.exists()) {
			return file.lastModified();
		}
		
		return CURRENT_TIME;
	}

}
