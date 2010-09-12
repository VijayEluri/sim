// CatfoOD 2010-9-10 ����08:35:22 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.compile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * �ļ�����������ȡ�ļ��е�${varname}���ʽ
 * varname�������Java���������淶
 */
public class FileParse {
	
	private static final Pattern exp = Pattern.compile("^[_A-Za-z][\\$_A-Za-z0-9]*");
	public static final String VAR_PREFIX = "__";
	
	private Set<String> variables;
	private List<String> texts;
	private List<String> items;
	private int line = 1;
	private String filename;
	
	
	public FileParse(Info inf) throws IOException {
		this(inf.getSqlFileName(), inf.openSqlInputStream());
	}
	
	/**
	 * ��ʼ����sql�ļ�����������ص���Ϣ
	 * 
	 * @param _filename - ���������ļ������������������Ϣ������Ϊnull
	 * @param reader - ��reader�ж�ȡĿ���ļ�
	 * @throws IOException - �������ʧ�ܣ��׳��쳣
	 */
	public FileParse(String _filename, Reader reader) throws IOException {
		Reader in = new BufferedReader(reader);
		filename = _filename;
		init();
		
	try {
		StringBuilder buff = new StringBuilder();
		int ch = ' ';
		boolean isVar = false;
		
		while (true) {
			ch = in.read();

			if (ch=='\r') continue;
			
			if (ch=='\n') {	
				line++;	
				buff.append('\\').append('n');
				continue;
			}
			
			if (ch=='"') {
				invalid("������˫����");
				continue;
			}
			
			if (isVar) {
				if (ch=='}') {
					addItem(variables, buff);
					isVar = false;
					continue;
				}
			}
			else if (ch=='$') {
				in.mark(3);
				ch = (char) in.read();
				
				if (ch=='{') {
					addItem(texts, buff);
					isVar = true;
					continue;
				} else {
					in.reset();
				}
			}

			if (ch>=0) {
				buff.append((char)ch);
			} else {
				break;
			}
		}
		addItem(texts, buff);
		
	}
	finally {
		try {if (in!=null) in.close();} catch(Exception e) {} 
		}
	}
	
	private void addItem(Collection<String> point, StringBuilder str) throws IOException {
		String s = str.toString();
		
		if (s.trim().length()>0) {
			if (point==texts) {
				str.insert(0, '"').append('"');
				s = str.toString();
			} else {
				checkVarName(s);
				s = VAR_PREFIX + s;
			}
			
			items.add(s);
			point.add(s);
			str.setLength(0);
		}
	}
	
	private void checkVarName(String name) throws IOException {
		if (!exp.matcher(name).matches()) {
			invalid("��Ч�ı�����:[" + name + "]");
		}
	}
	
	private void invalid(String msg) throws IOException {
		throw new IOException(
				msg + ", ���ļ� " + filename + 
				" ��" + line + "��" );
	}
	
	private void init() {
		variables	= new HashSet<String>();
		texts		= new ArrayList<String>();
		items		= new ArrayList<String>();
	}
	
	/**
	 * ���ر������ĵ�����
	 */
	public Iterator<String> getVariableNames() {
		return variables.iterator();
	}
	
	/**
	 * ���ؽ������sql�ļ���Ԫ�صĵ�����
	 * ���У��ı��Ѿ���˫���Ű�Χ����������ֱ�ӷ���
	 */
	public Iterator<String> getItems() {
		return items.iterator();
	}
	
	/**
	 * �����ļ��е��ı�Ԫ�صĵ�������Ԫ�ر��ֿ���ԭ��
	 * �������м�ı���
	 */
	public Iterator<String> getTexts() {
		return texts.iterator();
	}
}
