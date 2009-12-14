/**
 * �޸�tableidָ���ı����ɫ������ɫΪ������ʾ
 * 
 * @param tableid - ����id
 * @param fcolor - �����е���ɫ
 * @param scolor - ż���е���ɫ
 * @param mousecolor - �����ͣ����ɫ
 * @return ����tableid�Ķ���
 */
function changeTableColor(tableid, fcolor, scolor, mousecolor) {
	var table = getByid(tableid);
	if (table==null) return;
	
	var color = true;
	
	if (fcolor==null) {
		fcolor = '#ddd';
	}
	if (scolor==null) {
		scolor = '#f0f0f0';
	}
	if (mousecolor==null) {
		mousecolor = '#faa';
	}
	
	var rows = table.rows;
	for (var i=0; i<rows.length; ++i) {
		var row = rows[i];
		
		if (color) {
			changeColor(row, fcolor);
		} else {
			changeColor(row, scolor);
		}
		color = !color;

		onMouseOverChangeColor(row, mousecolor);
	}
	
	return table;
}

/**
 * �������ͣ��obj�����ʱ����ɫ��Ϊcolor
 * 
 * @param obj - html��Ƕ���
 * @param color - ��Ч��css��ɫֵ
 * @return null
 */
function onMouseOverChangeColor(obj, color) {
	var oldcolor = obj.style.backgroundColor;
	var r = obj;
	var ncolor = color;
	
	r.onmouseover = function() {
		changeColor(r, ncolor);
	}
	r.onmouseout = function() {
		changeColor(r, oldcolor);
	}
}

/**
 * �޸�obj����ɫΪcolor
 * 
 * @param obj - html��Ƕ���
 * @param color - ��Ч��css��ɫֵ
 * @return null
 */
function changeColor(obj, color) {
	obj.style.backgroundColor = color;
}

function getByid(id) {
	return document.getElementById(id);
}