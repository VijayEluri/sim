// CatfoOD 2009-12-14 下午09:52:45

package jym.sim.test.sql;

import jym.sim.tags.PaginationTag;

public class TestPaginationTag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PaginationTag t = new PaginationTag();
		t.setCurrentPage(2);
		t.setTotalPage(20);
		t.setUrlPattern("a/b/page?page=%page");
		
		System.out.println(t);
	}

}
