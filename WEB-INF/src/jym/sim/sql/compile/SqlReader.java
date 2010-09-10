// CatfoOD 2010-9-9 ����03:40:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
	 */
	public void set(String name, Object value) {
		try {
			Field f = sqlclz.getField(FileParse.VAR_PREFIX + name);
			f.set(instance, value);
		} catch (Exception e) {
			lastErr = e;
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
