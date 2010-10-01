// CatfoOD 2010-4-16 ����01:42:14 yanming-sohu@sohu.com/@qq.com

package test;

import java.util.Iterator;
import java.util.List;

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
		
		OrmTemplate<UserBean> orm = new OrmTemplate<UserBean>(TestDBPool.getDataSource(), new IOrm<UserBean>() {

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
		
		// list do something...
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
		for (int i=100; i<12000; ++i) {
			user.setBrongthname("test " + i);
			user.setBrongthsn(""+i);
			user.setBrongthid("i" + i);
			orm.add(user);
		}
	}

	/**
	 * odbc����:<br>
	 * 10000������,3���ַ����ֶ�,�����������,��̬�������Լ100��<br>
	 * ���������,��̬��������Լ3��
	 */
	private static void checkSelect(ISelecter<UserBean> orm) {
		boolean iteratorResult = true;
		
		UserBean user = new UserBean();
		PageBean page = new PageBean();
		
		for (int i=0; i<3; ++i)
		{
			UsedTime.start("��̬��ѯ");
			List<?> list = orm.select(user, "or", null);
			
			Tools.pl("size: " + list.size());
		
			if (iteratorResult) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					it.next();
				}
			}
			UsedTime.printAll();
		}
		for (int i=0; i<3; ++i)
		{
			UsedTime.start("һ���Բ�ѯ");
			List<?> list = orm.select(user, "or", page);
			
			Tools.pl("size: " + list.size());
		
			if (iteratorResult) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					it.next();
				}
			}
			UsedTime.printAll();
		}
	}
}
