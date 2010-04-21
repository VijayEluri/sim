// CatfoOD 2010-4-19 ����10:59:36 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import java.sql.SQLException;

import javax.sql.DataSource;

import jym.sim.sql.IQuery;

/**
 * ʵ�����ӳ��
 */
public class OrmTemplate<T> 
	extends UpdateTemplate<T> 
	implements IQuery, ISelecter<T>, IUpdate<T> {
	

	/**
	 * ormģ�幹�캯��, ȫ��ʹ�ñ����ӳ��ʵ������
	 * 
	 * @param ds - ����Դ
	 * @param modelclass - ����ģ�͵�class��
	 * @param tablename - ���ݿ����
	 * @param priKey - ������
	 */
	public OrmTemplate(DataSource ds, Class<T> modelclass, String tablename,
			String priKey) {
		super(ds, modelclass, tablename, priKey);
	}

	/**
	 * jdbcģ�幹�캯��,Ĭ��ÿ�����Ӳ����Զ��ر�����
	 * 
	 * @param orm - ���ݿ���������beanʵ������ӳ�����
	 * @throws SQLException - ���ݿ�����׳��쳣
	 */
	public OrmTemplate(DataSource ds, IOrm<T> orm) {
		super(ds, orm);
	}
	
}
