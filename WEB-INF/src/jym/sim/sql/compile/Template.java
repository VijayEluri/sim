// CatfoOD 2010-9-9 ����03:40:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;


/**
 * sql����ΪJava�ļ������ʽģ�壬��ʵ������
 */
class Template {
	
	public long _lastModify = 0l;
	public Object v1;
	public Object v2;
	
	
	public String toString() {
		return "select * from " + v1 + " where " + v2;
	}
	
}
