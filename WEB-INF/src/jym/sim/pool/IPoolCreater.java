// CatfoOD 2010-8-4 ����09:24:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;

import javax.sql.DataSource;

/**
 * ��ͬ���ݳصĴ�����,ʵ������ӿ�<br>
 * �������޲ι��캯��
 */
public interface IPoolCreater {
	
	/**
	 * �������д������ӳض���, <b>���ܷ���null</b>
	 * 
	 * @param conf - ���ݿ���������
	 * @return ���ݳ�
	 * @exception ��Ч������,�����ݿ����Ӵ���
	 */
	DataSource create(PoolConf conf) throws IOException;
}
