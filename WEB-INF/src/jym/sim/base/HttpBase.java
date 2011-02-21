// CatfoOD 2009-10-20 ����03:10:47

package jym.sim.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.BeanUtil;
import jym.sim.util.ForwardProcess;
import jym.sim.util.ICallBack;
import jym.sim.util.ServletData;
import jym.sim.util.Tools;

/**
 * Ĭ��ʹ�����ı���<br>
 * ��ǰ֧��get/post<br>
 * 
 * �����URL��ʽ: <servlet>?[do=<method>]<br>
 * 		�����do��������ִ����Եķ���
 */
public abstract class HttpBase<BEAN> extends HttpServlet {
	
	private static final long serialVersionUID = 4056930472082034056L;
	private static final String PARM_CLASSNAME = "bean-class";
	private static final String PARM_CHARSET = "charset";
	private static final String PARM_METHOD = "do";
	private static final String DEFAULT_METHOD = "execute";
	
	private static String charset = null;
	private BeanUtil<BEAN> bean = null;
	
	
	/**
	 * ��InitParameter�ж�ȡbeanclass��ֵ
	 * ��post�������ݳ�ʼ�����bean
	 */
	@Override
	public final void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		Class<BEAN> bc = getBeanClass();
		
		if (bc!=null) {
			bean = new BeanUtil<BEAN>(bc);
		} else {
			String bclass = config.getInitParameter(PARM_CLASSNAME);
			
			if (bclass!=null) {
				bean = new BeanUtil<BEAN>(bclass);
			} else {
				Tools.pl(PARM_CLASSNAME + " init-param not set, or getBeanClass() return NULL");
			}
		}
		
		if (charset==null) {
			charset = config.getServletContext().getInitParameter(PARM_CHARSET);
			try {
				Charset _cs = Charset.forName(charset);
				charset = _cs.name();
			} catch(Exception e) {
				Tools.pl(PARM_CHARSET + " context-param not set." + e);
				charset = Charset.defaultCharset().name();
			}
		}
		
		init2(config);
	}
	
	/**
	 * ������д��init����֮�󱻵���
	 * @param config
	 * @throws ServletException
	 */
	protected void init2(ServletConfig config) throws ServletException {
	}
	
	/**
	 * ����ʵ���������, Ĭ�Ϸ���null, ��ʱ��Ҫ��web.xml������ʵ������<br>
	 * �Ƽ�ʹ�ø÷�����������, ���Ҹ÷��������ȼ�����
	 */
	protected Class<BEAN> getBeanClass() {
		return null;
	}
	
	/**
	 * ��doGet/doPost..��װ,����data���������formbean
	 * �Ѿ���������HttpServletRequest,��Сд��������������������,<br>
	 * �������uriû��ָ������,��÷���������,�÷���Ĭ�ϲ�ִ�в���
	 * 
	 * @param data - HttpServlet���ݶ���
	 * @return	�������String���ͣ���StringΪ��Ч��mapping·��<br>
	 * 			�������IPrinter���ͣ����ӡ����������null·��<br>
	 * 			��������������ͣ���ֱ�Ӱ�toString�Ľ��������ͻ��ˣ�
	 * 			������null·��<br>
	 * @throws Exception
	 */
	public Object execute(IHttpData<BEAN> data) throws Exception {
		Tools.pl(getClass() + " servlet do nothing, " +
				"must rewrite execute method.");
		return null;
	}
	
	/**
	 * �ڷ���������ǰ�������,����׳��쳣�򷵻�false����ִ�к�̷���<br>
	 * Ĭ�����Ƿ���true
	 * @param methodName - ����ķ���,����Ϊnull
	 */
	public boolean before(IHttpData<BEAN> data, String methodName) throws Exception {
		return true;
	}
	
	
	/** ��Ҫֱ�Ӹ���������� */
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/** ��Ҫֱ�Ӹ���������� */
	protected final void doPost(final HttpServletRequest req, 
			final HttpServletResponse resp)
			throws ServletException, IOException {

		charCoding(req, resp);
		BEAN formbean = packBean(req, resp);
		HttpData data = new HttpData(req, resp, formbean);
		
		String methodName = data.getParameter(PARM_METHOD);
		if (methodName==null) methodName = DEFAULT_METHOD;
		
		try {
			if ( !before(data, methodName) ) return;
			
			final Object obj = callFunction(data);
			
			if (obj!=null) {
				ForwardProcess.exec(data, obj, new ICallBack() {
					public void back() throws Exception {
						forward(req, resp, (String)obj);
					}
				});
			}
			
		} catch (ServletException se) {
			throw se;
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			throw new ServletException("servlet execute error", e);
		}
	}
	
	protected Object callFunction(IHttpData<BEAN> data) throws Exception {
		
		String methodName = data.getParameter(PARM_METHOD);
		Object result = null;
		
		if (methodName!=null) {
			Method method = getClass().getMethod(methodName, IHttpData.class);
			result = method.invoke(this, data);
		} else {
			result = execute(data);
		}
		return result;
	}
	
	private BEAN packBean(HttpServletRequest req, HttpServletResponse resp) {
		BEAN formbean = null;
		if (bean!=null) {
			formbean = bean.creatBean(req);
			req.setAttribute(bean.getBeanName(), formbean);
		}
		return formbean;
	}
	
	private void charCoding(HttpServletRequest req, HttpServletResponse resp){
		resp.setContentType("text/html; charset=" + charset);
		resp.setCharacterEncoding(charset);
		try {
			req.setCharacterEncoding(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void forward(HttpServletRequest req, HttpServletResponse resp, String path) 
			throws ServletException, IOException {
		
		if (path!=null) {
			RequestDispatcher rd = req.getRequestDispatcher(path);
			if (rd!=null) {
				rd.forward(req, resp);
			}
		}
	}
	
	private class HttpData extends ServletData implements IHttpData<BEAN> {
		private BEAN fb;

		public HttpData(HttpServletRequest request, HttpServletResponse response,
				BEAN formbean) throws IOException {
			
			super(request, response);
			fb = (BEAN) formbean;
		}

		public BEAN getFormObj() {
			return fb;
		}
	}
}
