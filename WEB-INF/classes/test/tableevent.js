
function editTableData(row, rowindex) {
	if (rowindex==0) return;
	
	alert(rowindex);
}

/**
 * �ƶ�divָ���Ĳ㵽��Ļ������
 * 
 * @param div - div����
 */
function moveCenter(div) {
	var w = div.clientWidth;
	var h = div.clientWidth;
	var x = (document.body.clientWidth - w)/2
	var y = (document.body.clientHeight - h)/2
	div.style.top = y;
	div.style.left = x;
	
	return div;
}