// CatfoOD 2010-7-29 ����08:47:54 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;

/**
 * ����һ�ι������ĵ�ʱ��, �̰߳�ȫ����, ����ʵ��
 */
public final class UsedTime {
	
	private static ThreadLocal<Time> times = new ThreadLocal<Time>();
	private static final String NULL_STR = "";
	
	
	/**
	 * ��ʼ��ʱ
	 */
	public static void start() {
		start(NULL_STR);
	}
	
	/**
	 * ��ʼ��ʱ,������һ������
	 * @param desc - �Դ˴μ�ʱ������
	 */
	public static void start(String desc) {
		Time t = times.get();
		
		if (t == null) {
			t = new Time();
			times.set(t);
		} else {
			t.reset();
		}
		t.setName(desc);
	}
	
	/**
	 * ������ʱ,������start��end����֮��,������ʱ��<br>
	 * ���ô˷�����Ӱ����,���������Ҫ���ȡ�ý��,Ӧʹ��getUsedTime
	 */
	public static long end() {
		Time t = times.get();
		Tools.check(t, "����: ��δ����start()");
		t.end();
		
		return t.usedTime();
	}
	
	/**
	 * �����ϴ�start()��end()����֮��,������ʱ��
	 */
	public static long getUsedTime() {
		Time t = times.get();
		Tools.check(t, "����: ��δ����start()");
		return t.usedTime();
	}
	
	/**
	 * ֹͣ��ʱ,�����ڿ���̨���ʹ�õ�ʱ��
	 */
	public static void endAndPrint() {
		Time t = times.get();
		Tools.check(t, "����: ��δ����start()");
		t.end();
		
		Tools.pl(t.getName() + "ʹ����: " + t.usedTime() + " ms");
	}
	
	
	private static class Time {
		private long startT;
		private long endT;
		private String name;
		
		private Time() {
			reset();
		}
		public void reset() {
			startT = System.currentTimeMillis();
			endT = startT;
			name = NULL_STR;
		}
		public void end() {
			endT = System.currentTimeMillis();
		}
		public long usedTime() {
			return endT - startT;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
