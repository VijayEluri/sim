// CatfoOD 2010-4-16 ����01:42:14 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import jym.sim.orm.IOrm;
import jym.sim.orm.IPlot;
import jym.sim.orm.ISelecter;
import jym.sim.orm.IUpdate;
import jym.sim.orm.OrmTemplate;
import jym.sim.orm.page.PageBean;
import jym.sim.sql.Logic;
import jym.sim.util.Tools;
import jym.sim.util.UsedTime;

@SuppressWarnings("unused")
public class OrmDemo {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DataSource ds = TestDBPool.mySqlSource();
		
		OrmTemplate<UserBean> orm = new OrmTemplate<UserBean>(ds, new IOrm<UserBean>() {

			public Class<UserBean> getModelClass() {
				return UserBean.class;
			}

			public void mapping(IPlot plot) {
				plot.fieldPlot("brongthname", "brongthname", Logic.INCLUDE);
				plot.fieldPlot("brongthid", "brongthid");
				plot.fieldPlot("brongthsn", "brongthsn");
			}

			public String getKey() {
				return "brongthsn";
			}

			public String getTableName() {
				return "ba_brongth";
			}
			
		});
		
//		checkDelete(orm);
//		checkUpdate(orm);
//		checkInsert(orm);
		
		checkSelect(orm);
		
		Tools.pl("over.");
	}
	
	private static void checkDelete(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			
			orm.delete(user);
		}
	}
	
	private static void checkUpdate(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=100; i<120; ++i) {
			user.setBrongthname("rename2 " + i);
			user.setBrongthsn(""+i);
			user.setBrongthid("i" + i);
			
			orm.update(user);
		}
	}
	
	
	private static void checkInsert(IUpdate<UserBean> orm) {
		UserBean user = new UserBean();
		for (int i=1; i<12000; ++i) {
			user.setBrongthname("name " + i);
			user.setBrongthsn(String.format("%05d", i));
			user.setBrongthid("id " + i);
			orm.add(user);
		}
	}

	/**
	 * odbc����:<br>
	 * 10000������,3���ַ����ֶ�,<b>������</b>�����,��̬�������Լ100��;<br>
	 * <b>����</b>�����,��̬��������Լ5��;���ܲ�����Ҫԭ������ʹ�ÿɹ����Ľ����:<br>
	 * <code>
	 * statement = conn.createStatement(
	 *			ResultSet.TYPE_SCROLL_INSENSITIVE, 
	 *			ResultSet.CONCUR_READ_ONLY);
	 *</code>
	 */
	private static void checkSelect(ISelecter<UserBean> orm) {
		boolean iteratorResult = true;
		
		UserBean user = new UserBean();
		PageBean page = new PageBean();
		int loop = 0;
		
		for (int i=0; i<3; ++i)
		{
			UsedTime.start("��̬��ѯ");
			List<?> list = orm.select(user, "or");
			
		
			if (iteratorResult) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					it.next();
					loop++;
				}
			}
			Tools.pl("size: " + list.size() + " loop: " + loop); loop=0;
			UsedTime.printAll();
		}
		for (int i=0; i<3; ++i)
		{
			UsedTime.start("һ���Բ�ѯ");
			List<?> list = orm.select(user, "or", page);
		
			if (iteratorResult) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					it.next();
					loop++;
				}
			}
			Tools.pl("size: " + list.size() + " loop: " + loop); loop=0;
			UsedTime.printAll();
		}
	}
}
