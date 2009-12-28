// CatfoOD 2009-12-17 ����09:28:22

package jym.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ��ȫ��IDӳ�䣬��ֹ���ݿ�IDֱ�Ӵ����ͻ���
 */
public class SafeidMapping {
	
	private Map<Integer, Integer> idmap;
	private Random ran;
	
	private static final int MAX = 0xF00000;
	private static final String NAME = SafeidMapping.class.getName();
	
	public SafeidMapping() {
		idmap = new HashMap<Integer, Integer>();
		ran = new Random();
	}
	
	/**
	 * ��ݵķ���<br>
	 * ��session��ȡ��SafeidMapping���󲢵õ���ȫid</br>
	 * ���SafeidMapping���������ʼ����
	 * 
	 * @param id - ԭʼid
	 * @param data - Action���ݶ���
	 * @return ��ȫ��ID
	 */
	public static int getSafeID(int id, ISessionData data) {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			resetSafeID(data);
		}
		return smp.getSafe(id);
	}
	
	/**
	 * ��ݵķ���<br>
	 * ���session��SafeidMapping���������idӳ��<br>
	 * ���SafeidMapping�������򴴽���<br>
	 * <b>�˷���һ���ڹ�������֮ǰ����</b>
	 * 
	 * @param data - Action���ݶ���
	 * @return ������session�󶨵�SafeidMapping
	 */
	public static SafeidMapping resetSafeID(ISessionData data) {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			smp = new SafeidMapping();
			data.setSessionAttribute(NAME, smp);
		}
		smp.reset();
		return smp;
	}
	
	/**
	 * ��ݵķ���<br>
	 * ��session��ȡ��SafeidMapping���󲢵õ�ԭʼid
	 * 
	 * @param sid - ��ȫid
	 * @param data - Action���ݶ���
	 * @return ԭʼ��ID
	 * @throws SafeException - ���SafeidMapping���ܴ��ڻ���û��sid��ӳ��
	 */
	public static int getRealID(int sid, ISessionData data) throws SafeException {
		SafeidMapping smp = (SafeidMapping) data.getSessionAttribute(NAME);
		if (smp==null) {
			throw new SafeException();
		}
		return smp.getReal(sid);
	}
	
	
	/**
	 * �����ݿ���ȡ�ð�ȫӳ��<br>
	 * ͬʱIDֵ�������Ա㷴��ӳ��
	 */
	public int getSafe(int id) {
		int sid = safe();
		idmap.put(sid, id);
		return sid;
	}
	
	private int safe() {
		int i = 0;
		do {
			i = ran.nextInt(MAX);
		} while (idmap.containsKey(i));
		
		return i;
	}
	
	/**
	 * ��ӳ����ȡ����ʵID
	 * @throws SafeException - ���sid������˵��sid�ǲ���ȫ�ģ����׳�����쳣
	 */
	public int getReal(int sid) throws SafeException {
		if (!idmap.containsKey(sid)) {
			throw new SafeException();
		}
		return idmap.get(sid);
	}
	
	/**
	 * ������е�ӳ��ID
	 */
	public void reset() {
		idmap.clear();
	}
}
