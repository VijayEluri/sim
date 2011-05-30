// CatfoOD 2010-4-21 ����09:20:02 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * ��֤ע���ʵ�ֽӿ�
 */
public interface IVerify {
	
	/**
	 * ��֤�ɹ�
	 */
	public final String SUCCESS = null;
	
	/**
	 * ��֤�����г��ִ���
	 */
	public final String ERROR = null;
	
	/**
	 * ��֤��������֤
	 */
	public final String SKIP = null;
	
	/**
	 * ������֤��Ҫ��֤��'ע��'��class
	 */
	public Class<? extends Annotation> getAnnoClass();
	
	/**
	 * ����ָ���Ĺ�����֤field��ֵvalue,<br>
	 * ������Ϲ���(��֤�ɹ�)����null,<br>
	 * ���򷵻ش����ַ���<br>
	 * <b>���valueֵ�޷���֤(valueΪnull, �����Ͳ���)Ҳ����null</b>
	 * 
	 * @param field - Ҫ��֤���ֶ�
	 * @param value - �ֶ����������ֵ
	 */
	public String getMessage(Field field, Object value);
}
