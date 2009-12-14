// CatfoOD 2009-10-20 ����03:10:47

package jym.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.base.util.BeanUtil;

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
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String bclass = config.getInitParameter("beanclass");
		if (bclass!=null) {
			bean = new BeanUtil(bclass);
		}
	}

	/** ��Ҫֱ�Ӹ���������� */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		String path = get(req, resp, formbean);
		forward(req, resp, path);
	}

	/** ��Ҫֱ�Ӹ���������� */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Object formbean = common(req, resp);
		String path = post(req, resp, formbean);
		forward(req, resp, path);
	}
	
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
	
	/**
	 * ��doGet��װ,����formbean�Ѿ���������HttpServletRequest,��Сд��������������������
	 * 
	 * @param req - ����
	 * @param resp - ��Ӧ
	 * @param formbean - web.xml������beanclass���ԵĶ���ʹ��post/get������ʼ��
	 * @return String - ת����·��������Ϊnull
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String get(HttpServletRequest req, HttpServletResponse resp, Object formbean)
		throws ServletException, IOException {
		return null;
	}
	
	/**
	 * ��doPost��װ,����formbean�Ѿ���������HttpServletRequest,��Сд��������������������
	 * 
	 * @param req - ����
	 * @param resp - ��Ӧ
	 * @param formbean - web.xml������beanclass���ԵĶ���ʹ��post/get������ʼ��
	 * @return String - ת����·��������Ϊnull
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String post(HttpServletRequest req, HttpServletResponse resp, Object formbean)
		throws ServletException, IOException {
		return null;
	}
}
