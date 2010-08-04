// CatfoOD 2010-8-4 ����09:02:28 yanming-sohu@sohu.com/@qq.com

package jym.sim.pool;

import jym.sim.util.Tools;

/**
 * ���ݿ��������
 */
public class PoolConf {

	private String user;
	private String pass;
	private String url;
	private String driver;
	
	private String jndiName;
	private String poolClassName;
	
	private int maxWait;
	private int maxIdle;
	private int maxActive;
	
	private String validation;
	private boolean hasValidation;
	
	
	/** ��¼���ݿ���û��� */
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	public String getUrl() {
		return url;
	}
	/** ���ݿ������� */
	public String getDriver() {
		return driver;
	}
	public int getMaxWait() {
		return maxWait;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public int getMaxActive() {
		return maxActive;
	}
	/** ��֤���ݿ�������Ч�Ե�sql��� */
	public String getValidation() {
		return validation;
	}
	/** ���getValidation()���طǿ��ַ���, �򷵻�true */
	public boolean isHasValidation() {
		return hasValidation;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public void setValidation(String validation) {
		this.validation = validation;
		hasValidation = !Tools.isNull(validation);
	}
	public String getJndiName() {
		return jndiName;
	}
	public String getPoolClassName() {
		return poolClassName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public void setPoolClassName(String poolClassName) {
		this.poolClassName = poolClassName;
	}
}
