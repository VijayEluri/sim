// CatfoOD 2010-4-21 ����02:35:57 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * �����ʼ���ַЧ����
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Email {
	/**
	 * Ч�����ʱ����Ϣ
	 */
	String msg();
}
