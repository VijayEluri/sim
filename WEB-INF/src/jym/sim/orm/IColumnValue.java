// CatfoOD 2010-4-19 上午08:28:23 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

/**
 * 表格列的值
 */
public interface IColumnValue {
	
	/**
	 * 模板对象通过这个方法把表格列与列值传给实现这个接口的对象
	 * 
	 * @param column - 列名,不会是null
	 * @param value - 值
	 * @param valueType - 值的类型
	 */
	void set(String column, Object value, Class<?> valueType);
	
}
