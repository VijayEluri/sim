// CatfoOD 2009-12-15 ����08:10:04

package jym.base;

import jym.base.util.IServletData;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * ��װStruts.Action��execute�����Ĳ���
 */
public interface IActionData extends IServletData {
	ActionMapping getActionMapping();
	ActionForm getActionForm();
}
