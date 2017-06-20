package com.bc_manga2.Resolve.SortFine;

import java.util.ArrayList;

abstract class SortFineRexolve {
	protected String Html;//分類細項第一頁HTML
	protected String HomePK;//資料庫PK
	
	/**
	 * @param Html 分類細項第一頁HTML
	 * @param HomePK 資料庫PK
	 */
	public SortFineRexolve(String Html,String HomePK) {
		this.Html = Html;
		this.HomePK = HomePK;
	}
	
	/**從HTML解析分類細項格狀列表 */
	protected abstract ArrayList<SortFineItem> HtmlSortFineItem(String Html);
	
	/**從HTML解析下一頁需要的URL */
	protected abstract String GetNextUrl(String Html);
	
	
	/**傳出SortArrary*/
	public ArrayList<SortFineItem> GetSortFineItemArray() {
		return HtmlSortFineItem(Html);
	}
	/**傳出下一頁需要的URL 有可能有有可能沒有*/
	public String NextUrl() {
		return GetNextUrl(Html);
	}
	
}
