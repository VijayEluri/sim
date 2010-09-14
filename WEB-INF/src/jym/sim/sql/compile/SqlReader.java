// CatfoOD 2010-9-9 ����03:40:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jym.sim.sql.IQuery;
import jym.sim.sql.ISql;
import jym.sim.util.Tools;


/**
 * ����Ķ���<b>Ӧ�ñ�����</b>,�Ա����Ч��
 */
public class SqlReader {
	
	private Exception lastErr;
	private Info inf;
	private Class<?> sqlclz;
	private Object instance;
	
	
	/**
	 * ��ʼ��sql��ȡ��<br>
	 * <br>
	 * ���ļ���������������������淶,�ļ��е�${..}����������<br>
	 * �������ı���ͨ������set()������ֵ,�������������Java�����������淶<br>
	 * sql�ļ�����ʹ��<b>UTF-8</b>����
	 * 
	 * @param filename - ��classpath�ж�ȡsql�ļ�������·��,<br>
	 * ��<code>/jym/sim/sql/complie/x.sql</code>
	 */
	public SqlReader(String filename) throws IOException {
		inf = new Info(filename);
		URL sqlfile = getClass().getResource(filename);

		if (loadClass()) {
			if (sqlfile!=null) {
				inf.setUrl(sqlfile);
				checkLastModify();
			}
		} else {
			if (sqlfile==null) {
				throw new IOException("�Ҳ����ļ�: " + filename);
			}
			inf.setUrl(sqlfile);
			
			compileSql();

			if (!loadClass()) {
				throw new IOException("��ʼ�����ļ�[" + inf.getFullClassName() + "]ʧ��" + lastErr);
			}
		}
	}
	
	private void checkLastModify() throws IOException {
		try {
			Field f = sqlclz.getField(Compiler.MODIFY_FIELD);
			long lm = (Long) f.get(instance);
			
			if (inf.lastModified()!=lm) {
				compileSql();
				if (!loadClass()) {
					Tools.pl(inf.getSqlFileName() + " �Ѿ��޸Ĳ����±���,�����¼���ʱʧ��: " + lastErr);
				}
			} else {
				inf.clear();
			}
		} catch (Exception e) {
			throw new IOException("��ȡ����޸�����ʧ��: " + e);
		}
	}
	
	private void compileSql() throws IOException {
		Compiler c = new Compiler(inf);
		
		if (c.start()) {
			inf.clear();
		} else {
			throw new IOException(inf.getSqlFileName() + "ת��Ϊjava�ļ������ʧ��" + lastErr);
		}
	}
	
	/**
	 * ����sql�ļ��е�${..}������ֵ,�������������Java���������淶
	 * 
	 * @param name - ���滻�ı�����
	 * @param value - �滻���ֵ
	 * @throws NoSuchFieldException - �Ҳ���nameָ���ı���
	 */
	public void set(String name, Object value) throws 
			NoSuchFieldException {
		
		try {
			Field f = sqlclz.getField(FileParse.VAR_PREFIX + name);
			f.set(instance, value);
			
		} catch(SecurityException se) {
			lastErr = se;
		} catch(IllegalAccessException ae) {
			lastErr = ae;
		} catch(IllegalArgumentException ae) {
			lastErr = ae;
		}
	}
	
	/**
	 * ִ��ָ���ļ��е�sql���,���ļ��еı���Ӧ���Ѿ���set()�滻
	 * ���ص�Statement��Ҫ���йر�
	 * 
	 * @param conn - �����ݿ������,����֮�������йر�
	 * @return java.sql.Statement
	 * @throws SQLException
	 */
	public Statement execute(Connection conn) throws SQLException {
		Statement s = conn.createStatement();
		s.execute(instance.toString());
		return s;
	}
	
	/**
	 * ��IQuery��ִ��sql�ļ��е�sql���,ÿ��sql�Ľ�������ListԪ����<br>
	 * ����Ǹ������,��Ԫ����һ��Integer����,�����Ԫ������һ��List,<br>
	 * ��List��ÿ��Ԫ����һ������,����ĳ��ȵ��ڽ�������г�,��0��Ԫ��<br>
	 * ��ű�ͷ������,֮���Ԫ���ǽ������������<br>
	 * <br>
	 * ͨ��,���ô˺���ǰ�Ѿ����sql������,��������ȷ��ת��<br>
	 * 
	 * @param iq - һ�㴫��JdbcTemplate
	 * @return List<Integer or List<Object[]>> ���ز�ѯ�Ľ����
	 */
	public List<Object> execute(IQuery iq) {
		final List<Object> rel = new ArrayList<Object>();
		
		iq.query(new ISql() {
			public void exe(Statement stm) throws Throwable {
				do {
					boolean isSet = stm.execute(instance.toString());
					int uc = stm.getUpdateCount();
					
					if (isSet) {
						ResultSet rs = stm.getResultSet();
						ResultSetMetaData smd = rs.getMetaData();
						int cc = smd.getColumnCount();
						
						List<Object[]> resultSet = new ArrayList<Object[]>();
						Object[] row = new Object[cc];
						
						for (int i=0; i<cc; ++i) {
							row[i] = smd.getColumnLabel(i+1);
						}
						resultSet.add(row);
						
						while (rs.next()) {
							row = new Object[cc];
							for (int i=0; i<cc; ++i) {
								row[i] = rs.getObject(i+1);
							}
							resultSet.add(row);
						}
						rel.add(resultSet);
					}
					else if (uc>0) {
						rel.add(uc);
					}
				} while(stm.getMoreResults());
			}
		});
		
		return rel;
	}
	
	/**
	 * ����ƴװ�õ�sql,���еı���ʹ��set()��ǰ���ú�
	 */
	public String getResultSql() {
		return instance.toString();
	}
	
	/**
	 * ��sql��ӡ������̨
	 */
	public void showSql() {
		Tools.pl(instance);
	}

	private boolean loadClass() {
		try {
			DynClassLoader loader = new DynClassLoader();
			sqlclz = loader.reLoadClass(inf.getFullClassName());
			instance = sqlclz.newInstance();
			return true;
		} catch (Exception e) {
			lastErr = e;
			return false;
		}
	}
}
