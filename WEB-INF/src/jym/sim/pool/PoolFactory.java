// CatfoOD 2010-8-4 ����08:58:28 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.ResourceLoader;
import jym.sim.util.Tools;

/**
 * ���ݳع���,Ӧ��һ��ֻ��Ҫ���д����һ������
 */
public class PoolFactory {
	
	private DataSource _ds;
	
	
	/**
	 * �������ӳع���,���ô��ļ�classpath/[fromfile]�ж�ȡ
	 * 
	 * @param fromfile �����ļ���·��,�����ļ��ĸ�ʽ�ο� test/datasource.conf
	 * @throws IOException - �����ļ���ȡ����,�������ݿ����
	 */
	public PoolFactory(String fromfile) throws IOException {
		Properties prop = new Properties();
		readFromFile(prop, fromfile);
		PoolConf pc = createConfig(prop);
		
		
		if (!Tools.isNull(pc.getJndiName())) {
			try {
				_ds = createFromJndi(pc);
				success("ʹ�÷�������jndi���ṩ������Դ");
			} catch (Exception e) {
				Tools.pl(e);
			}
		}
		
		if (_ds==null) {
			_ds = createFromLocal(pc);
			success("�����˶���������Դ");
		}
	}
	
	private PoolConf createConfig(Properties prop) {
		PoolConf conf = new PoolConf();
		
		conf.setUser(				prop.getProperty("username")		);
		conf.setPass(				prop.getProperty("password")		);
		conf.setUrl(				prop.getProperty("url")				);
		conf.setDriver(				prop.getProperty("driverClassName")	);
		
		conf.setMaxWait( 	toInt(	prop.getProperty("maxWait")		)	);
		conf.setMaxIdle(	toInt(	prop.getProperty("maxIdle")		)	);
		conf.setMaxActive(	toInt(	prop.getProperty("maxActive")	)	);
		
		conf.setValidation(			prop.getProperty("validation")		);
		conf.setJndiName(			prop.getProperty("poolJndiName")	);
		conf.setPoolClassName(		prop.getProperty("poolClassName")	);
		
		return conf;
	}
	
	private DataSource createFromLocal(PoolConf conf) throws IOException {
		String cname = conf.getPoolClassName();
		
		try {
			Class<?> clazz = Class.forName(cname);
			Object o = clazz.newInstance();
			if (!(o instanceof IPoolCreater)) {
				error("ָ������û��ʵ�� IPoolCreater", conf);
			}
			IPoolCreater creater = (IPoolCreater) o;
			return creater.create(conf);
			
		} catch (ClassNotFoundException e) {
			error("ָ��������Ч", conf);
			
		} catch (InstantiationException e) {
			error("ָ�������ʼ��ʧ��", conf);
			
		} catch (IllegalAccessException e) {
			error("ָ������Ĺ��췽�����ɷ���", conf);
		}
		
		return null;
	}
	
	private void error(String msg, PoolConf conf) throws IOException {
		throw new IOException("�������ӳ�ʧ��(" + conf.getPoolClassName() + ")");
	}
	
	private DataSource createFromJndi(PoolConf conf) throws IOException, NamingException {
		String name = conf.getJndiName();
        
		InitialContext cxt = new InitialContext();
		if (cxt == null) {
			throw new NamingException("����InitialContextʧ��");
		}

		DataSource ds = (DataSource) cxt.lookup(name);
		if (ds == null) {
			throw new NamingException("DataSourceΪ��,poolnameָ����name��Ч:" + name);
		}

		return ds;
	}
	
	private void readFromFile(Properties conf, String fromfile) throws IOException {
		InputStream in = ResourceLoader.getInputStream(fromfile);
		try {
			conf.load(in);
		} finally {
			if (in!=null) in.close();
		}
	}

	public DataSource getDataSource() {
		return _ds;
	}
	
	private int toInt(String i) {
		return Integer.parseInt(i);
	}
	
	private void success(String msg) {
		Tools.pl("PoolFactory : ����Դ���ӳش������," + msg);
		
		try {
			_ds.setLogWriter(new LogStream().getOut());
		} catch (SQLException e) {
			Tools.pl("����Դ������־��ʼ��ʧ��");
			Tools.plerr(e);
		}
	}
	
	
	private class LogStream extends OutputStream {
		private String NAME = ".:DataPoolMsg: ";
		private PrintWriter out;
		private byte[] buff = new byte[512];
		private int point = 0;
		
		private LogStream() {
			out = new PrintWriter(LogStream.this);
		}

		@Override
		public void write(int b) throws IOException {
			if (b=='\n') {
				flush();
			}
			else {
				buff[point++] = (byte)b;
				if (point >= buff.length) expand();
			}
		}
		
		private void expand() {
			buff = Tools.copyOf(buff, buff.length * 2);
		}
		
		public PrintWriter getOut() {
			return out;
		}
		
		@Override
		public void close() throws IOException {
			flush();
		}

		@Override
		public void flush() throws IOException {
			Tools.pl(NAME + new String(buff, 0, point));
			point = 0;
		}
	}
}
