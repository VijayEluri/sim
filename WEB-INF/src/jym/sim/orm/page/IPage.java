// CatfoOD 2010-5-24 ����09:28:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

/**
 * ISelecter�ķ�ҳ����,ʵ����Ӧ��ѭ����ģʽ
 */
public interface IPage {
	
	/** һ���ո� */
	public final char BLANK = ' ';
	
	/** ��ҳ��ѯ��䷵�صı�����һ�б�ʶ������Ĵ�С,����еı��� */
	public final String TOTAL_COLUMN_NAME = "sim__total__row";
	
	
	/**
	 * ����select���,����ķ�ҳ������ʵ���������<br>
	 * ���ɵ���������ʹ�ñ���,Ӧ����'sim__'Ϊǰ׺
	 * 
	 * @param tableName - ����
	 * @param whereSub - ��ѯ�����Ӿ�,�־��а���where�ؼ���
	 * @param orderSub - �����Ӿ�, ��������ʱΪ���ַ���
	 * @param page - ��ҳ���ݶ���
	 * 
	 * @return ����ƴװ�õ�select���
	 */
	public String select(String tableName, String whereSub, String orderSub, PageBean page);
}
