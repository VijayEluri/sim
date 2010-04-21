// CatfoOD 2009-10-20 ����03:10:47

package jym.sim.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

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

/**
 * Ĭ��ʹ�����ı���<br>
 * ��ǰ֧��get/post
 */
public abstract class HttpBase extends HttpServlet {
	
	private static final long serialVersionUID = 4056930472082034056L;
	private BeanUtil bean = null;
	
	/**
	 * ��InitParameter�ж�ȡbeanclass��ֵ
	 * ��post�������ݳ�ʼ�����bean
	 */
	@Override
	public final void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String bclass = config.getInitParameter("beanclass");
		if (bclass!=null) {
			bean = new BeanUtil(bclass);
		}
	}
	
	/**
	 * ��doGet/doPost..��װ,����data���������formbean
	 * �Ѿ���������HttpServletRequest,
	 * ��Сд��������������������
	 * 
	 * @param data - HttpServlet���ݶ���
	 * @return	�������String���ͣ���StringΪ��Ч��mapping·��<br>
	 * 			�������IPrinter���ͣ����ӡ����������null·��<br>
	 * 			��������������ͣ���ֱ�Ӱ�toString�Ľ��������ͻ��ˣ�
	 * 			������null·��<br>
	 * @throws Exception
	 */
	public abstract Object execute(IHttpData data) throws Exception;
	
	
	/** ��Ҫֱ�Ӹ���������� */
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/** ��Ҫֱ�Ӹ���������� */
	protected final void doPost(final HttpServletRequest req, 
			final HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		HttpData data = new HttpData(req, resp, formbean);
		
		try {
			final Object obj = execute(data);
			ForwardProcess.exec(data, obj, new ICallBack() {

				public void back() throws Exception {
					forward(req, resp, (String)obj);
				}
			});
			
		} catch (ServletException se) {
			throw se;
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ���ñ��뼯������formbean
	private Object common(HttpServletRequest req, HttpServletResponse resp) {
		charencoding(req,resp);
		Object formbean = null;
		if (bean!=null) {
			formbean = bean.creatBean(req);
			req.setAttribute(bean.getBeanName(), formbean);
		}
		return formbean;
	}
	
	private void charencoding(HttpServletRequest req, HttpServletResponse resp){
		if (req.getLocale().equals(Locale.CHINA)) {
			resp.setContentType("text/html; charset=gbk");
			resp.setCharacterEncoding("gbk");
			try {
				req.setCharacterEncoding("gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
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
	
	private class HttpData extends ServletData implements IHttpData {
		private Object fb;

		public HttpData(HttpServletRequest request, HttpServletResponse response,
				Object formbean) throws IOException {
			
			super(request, response);
			fb = formbean;
		}

		public Object getFormObj() {
			return fb;
		}
	}
}
