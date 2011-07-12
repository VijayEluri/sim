// CatfoOD 2010-5-24 上午09:28:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * ISelecter的分页策略,实现类应遵循单类模式
 */
public interface IPage {
	
	/** 一个空格 */
	public final char BLANK = ' ';
	
	/** 分页查询语句返回的表中有一列标识整个表的大小,这个列的别名 */
	public final String TOTAL_COLUMN_NAME = "sim__total__row";
	
	
	/**
	 * 生成select语句,具体的分页方法由实现类来完成<br>
	 * 生成的语句中如果使用别名,应该以'sim__'为前缀
	 * 
	 * @param parm - 参数
	 * @return 返回拼装好的select语句
	 * @see jym.sim.orm.page.PaginationParam
	 */
	public String select(PaginationParam parm);
}
