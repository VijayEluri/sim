// CatfoOD 2010-7-29 ����08:47:54 yanming-sohu@sohu.com/@qq.com

package jym.sim.util;

import java.util.Stack;

/**
 * ����һ�ι������ĵ�ʱ��, �̰߳�ȫ����, ����ʵ��
 */
public final class UsedTime {
	
	private static ThreadLocal<Stack<Time>>	times = new ThreadLocal<Stack<Time>>();
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
		Stack<Time> q = times.get();
		
		if (q == null) {
			q = new Stack<Time>();
			times.set(q);
		}

		Time t = new Time();
		t.setName(desc);
		t.reset();
		
		q.push(t);
	}
	
	/**
	 * ������ʱ,������start��end����֮��,������ʱ��<br>
	 * ���ô˷�����Ӱ����,���������Ҫ���ȡ�ý��,Ӧʹ��getUsedTime
	 */
	public static long end() {
		Stack<Time> q = times.get();
		Time t = q.pop();
		Tools.check(t, "����: ��δ����start()");
		t.end();
		
		return t.usedTime();
	}
	
	/**
	 * �����ϴ�start()��end()����֮��,������ʱ��
	 * �÷���������end(),endAndPrint()֮ǰ������Ч
	 */
	public static long getUsedTime() {
		Stack<Time> q = times.get();
		Time t = q.peek();
		Tools.check(t, "����: ��δ����start()");
		return t.usedTime();
	}
	
	/**
	 * ֹͣ��ʱ,�����ڿ���̨���ʹ�õ�ʱ��
	 */
	public static void endAndPrint() {
		Stack<Time> q = times.get();
		Time t = q.pop();
		Tools.check(t, "����: ��δ����start()");
		t.end();
		
		Tools.pl(t.getName() + "ʹ����: " + t.usedTime() + " ms");
	}
	
	public static void printAll() {
		Stack<Time> q = times.get();
		Time t = q.pop();
		while (t!=null) {
			t.end();
			Tools.pl(t.getName() + "ʹ����: " + t.usedTime() + " ms");
			t = q.pop();
		}
	}
	
	
	private static class Time {
		private long startT;
		private long endT;
		private String name;
		
		private Time() {
			name = NULL_STR;
			reset();
		}
		public void reset() {
			startT = System.currentTimeMillis();
			endT = startT;
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
