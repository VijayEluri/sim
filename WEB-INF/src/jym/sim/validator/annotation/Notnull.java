// CatfoOD 2010-4-21 ����08:06:09 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ��null����,��������ԵĶ�����String,������Ƿ�0�����ַ���
 * ��������Ǽ���,����Լ����Ƿ���Ԫ��,������Ϊnull
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Notnull {
	/**
	 * ������Ϣ
	 */
	String msg();
}
