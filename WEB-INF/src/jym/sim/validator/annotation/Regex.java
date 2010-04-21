// CatfoOD 2010-4-21 ����02:20:45 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ������ʽ��֤
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Regex {
	/**
	 * ��֤����ʱ����Ϣ
	 */
	String msg();
	/**
	 * ������ʽ
	 */
	String exp();
}
