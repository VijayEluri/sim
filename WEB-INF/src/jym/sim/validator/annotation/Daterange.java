// CatfoOD 2010-4-21 ����01:48:18 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ����У����,����min��max�ĸ�ʽ: '��-��-��'<br>
 * ���У������ʽ����,����ΪУ��ͨ��
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Daterange {
	/**
	 * У���������Ϣ
	 */
	String msg();
	/**
	 * ��С������ֵ
	 */
	String min();
	/**
	 * ��������ֵ
	 */
	String max();
}
