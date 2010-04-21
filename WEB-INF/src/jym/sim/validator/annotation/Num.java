// CatfoOD 2010-4-21 ����01:27:04 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ���������͵Ĳ���, ����֤��ֵx����� max()<=x<=min()
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Num {
	/**
	 * ��������Ϣ
	 */
	String msg();
	/**
	 * ���ֵ
	 */
	int max();
	/**
	 * ��Сֵ
	 */
	int min();
}
