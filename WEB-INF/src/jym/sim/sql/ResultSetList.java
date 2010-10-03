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
	 * ��װ��ѯ����
	 *
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
	 * �ſ���ʵ�ֺܺõļ�����,��������׳��쳣(������֧�ֵ����)
	 */
	@Override
	public BEAN get(int index) {
		if (index<0 || index>=size()) {
			throw new IndexOutOfBoundsException("max: "+size()+" not: "+index);
		}
		
		try {
			index++;
			if (index-1 == currentRow) {
				resultSet.next();
			} else {
				resultSet.absolute(index);
			}
			currentRow = index;
			
			return getter.fromRowData(columnNames, resultSet);
		
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
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
	
	/**
	 * ������ת��Ϊʵ�����ʵ��
	 * @param <BEAN>
	 */
	public interface IGetBean<BEAN> {
		/**
		 * ��rs��ǰ���е�����,����BEAN����
		 * 
		 * @param columnNames - ������
		 * @param rs - �����
		 * @return �������ǰ�ж�Ӧ��ʵ�����
		 */
		public BEAN fromRowData(String[] columnNames, ResultSet rs) throws Exception;
	}
	
}
