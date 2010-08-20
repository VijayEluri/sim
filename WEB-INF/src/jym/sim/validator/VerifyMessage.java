// CatfoOD 2010-4-21 ����09:21:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jym.sim.util.Tools;

/**
 * ��֤�����
 */
public class VerifyMessage {
	
	private List<Msg> msgs;
	private String prefix = "";
	private String suffix = "";
	
	/**
	 * ��֤��Ϣ
	 */
	public VerifyMessage() {
		msgs = new ArrayList<Msg>();
	}
	
	/**
	 * ѹ����Ϣ
	 */
	protected void putMsg(Field field, String msg) {
		msgs.add(new Msg(field, msg));
	}
	
	/**
	 * ȡ�ô�����Ϣ�ĵ�����,�������е�Ԫ�ؿ���ȡ�ô�����ϸ��Ϣ
	 */
	public Iterator<Msg> getMessages() {
		return msgs.iterator();
	}
	
	/**
	 * ���û�д�����Ϣ����true,������getMessages()����ȡ��������Ϣ
	 */
	public boolean isSuccess() {
		return msgs.size()==0;
	}
	
	
	public class Msg {
		private Field field;
		private String msg;
		
		public Msg(Field f, String m) {
			field = f;
			msg = m;
		}
		/**
		 * ���ط���������,���Ҽ���ǰ׺��׺
		 */
		public String getFieldName() {
			return prefix + field.getName() + suffix;
		}
		public String getMessage() {
			return msg;
		}
		public Field getField() {
			return field;
		}
	}

	/**
	 * ����һ��VerifyMessage�е�������Ϣ���뵱ǰ��VerifyMessage��
	 */
	public void add(VerifyMessage vm) {
		this.msgs.addAll(vm.msgs);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setPrefix(String prefix) {
		Tools.check(prefix, "ǰ׺����Ϊnull");
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		Tools.check(suffix, "��׺����Ϊnull");
		this.suffix = suffix;
	}
	
}
