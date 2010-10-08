// CatfoOD 2010-10-1 ����03:32:04

package jym.sim.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;

import jym.sim.orm.SelectTemplate;

/**
 * ��װ��ѯ�����ΪList����
 * 
 * @param <BEAN> - ��Ų�ѯ�����ݵ�ʵ��������
 */
public class ResultSetList<BEAN> extends AbstractList<BEAN> {
	
	private Connection connection;
	private ResultSet resultSet;
	private Statement statement;
	private final String[] columnNames;
	private IGetBean<BEAN> getter;
	private int currentRow = 0;
	private int maxRow = -1;
	
	
	/**
	 * Connection������jdbc����
	 * 
	 * @param sql �����ǲ�ѯ���sql���,��ֻ���ǵ�����ѯ���
	 * @throws SQLException
	 */
	public ResultSetList(String sql, JdbcTemplate jdbc, IGetBean<BEAN> gb) 
	throws SQLException {
		this(sql, jdbc.getConnection(), gb);
		// ���connection==null,�򲻻�ر���
		connection = null;
	}
	
	/**
	 * ��װ��ѯ����,
	 *
	 * @param sql - �����ǲ�ѯ���sql���,��ֻ���ǵ�����ѯ���
	 * @param conn - �����Ӳ������ⲿ�رգ�����������쳣
	 * @throws SQLException 
	 */
	public ResultSetList(String sql, Connection conn, IGetBean<BEAN> gb) 
	throws SQLException {
		connection = conn;
		getter = gb;
		
		//XXX ���ǵ���resultSet��ѯ�ٶ�����ԭ��
		statement = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		
		resultSet = statement.executeQuery(sql);
		
		ResultSetMetaData rsmd = resultSet.getMetaData();
		columnNames = SelectTemplate.getColumnNames(rsmd);
	}
	
	/**
	 * ȡ��ָ��������������ӳ���ʵ����,Ӧ��ʹ������������(0,1,2,N)
	 * �ſ���ʵ�ֺܺõļ�����,��������׳��쳣(������֧�ֵ����)<br>
	 * <b>��ֵ</b>�������ڲ�ͬʵ���л��в�ͬ��������ӽ������ȡ
	 */
	@Override
	public BEAN get(int index) {
		if (index>=size()) {
			throw new IndexOutOfBoundsException("max: "+size()+" not: "+index);
		}
		
		try {
			if (index>=0) {
				index++;
				if (index-1 == currentRow) {
					resultSet.next();
				} else {
					resultSet.absolute(index);
				}
				currentRow = index;
			}
			
			return getter.fromRowData(columnNames, resultSet, index);
		
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		if (maxRow<0) {
			try {
				if (resultSet.last()) {
					maxRow = resultSet.getRow();
					currentRow = maxRow;
				} else {
					maxRow = 0;
				}
			} catch(Exception e) {
				maxRow = 0;
			}
		}
		return maxRow;
	}

	/**
	 * �ͷ������ݿ����ӵĶ���,�൱��close()
	 */
	@Override
	public void clear() {
		try {
			resultSet.close();
		} catch (SQLException e) {
		}
		try {
			statement.close();
		} catch (SQLException e) {
		}
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		clear();
	}
	
	/**
	 * ������ת��Ϊʵ������̵�ʵ��
	 * @param <BEAN> �洢���ݵĶ�������
	 */
	public interface IGetBean<BEAN> {
		/**
		 * ��rs��ǰ���е�����,����BEAN����,
		 * <b>���붨��</b>��ֵrowNum����Ϊ,һ�����׳��쳣
		 * 
		 * @param columnNames - ������
		 * @param rs - �����
		 * @param rowNum - ��ǰ�����У�������ֵ��Χ�ڲ�ͬ��ʵ�����в�ͬ�Ķ���,
		 *  		ͨ�������쳣;rowNum�����ֵ�ǲ�ѯ��������������
		 * @return �������ǰ�ж�Ӧ��ʵ�����
		 */
		public BEAN fromRowData(String[] columnNames, 
				ResultSet rs, int rowNum) throws Exception;
	}
	
}
