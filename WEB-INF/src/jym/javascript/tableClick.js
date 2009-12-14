/**
 * �����������¼�
 * 
 * @param tableid - Ҫ�����ı��id
 * @param func - ��������,��һ������Ϊ��ǰ�����ͣ��tr����,
 * 						�ڶ��������ǵ�ǰ�е�������0��ʼ
 * @return ����tableid�Ķ���
 */
function tableRowMouseOverListener(tableid, func) {
	var table = document.getElementById(tableid);

	if (table==null) return;
	var rows = table.rows;
	for (var i=0; i<rows.length; ++i) {
		(function () {
			var funcstr = func;
			var row = rows[i];
			var rowindex = i;
			
			row.onclick = function () {
				funcstr(row, rowindex);
			}
		})();
	}
	
	return table;
}