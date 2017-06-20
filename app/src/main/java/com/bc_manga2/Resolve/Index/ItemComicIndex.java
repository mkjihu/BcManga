package com.bc_manga2.Resolve.Index;

public class ItemComicIndex {
	
	private String HomePK="";//HomePK--主站識別
	private String ItemUrl="";//該話數開頭網址--預設為空
	private String ItemName="";//該話數名稱--預設為空
	private boolean isDownload= false;//該話數是否下載過
	
	public ItemComicIndex(String HomePK,String ItemUrl,String ItemName,boolean isDownload) {
		this.HomePK = HomePK;
		this.ItemUrl= ItemUrl;
		this.ItemName=ItemName; 
		this.isDownload = isDownload;	
	}
	public ItemComicIndex(String HomePK,String ItemUrl,String ItemName) {
		this.HomePK = HomePK;
		this.ItemUrl= ItemUrl;
		this.ItemName=ItemName; 
	}
	
	public String getHomePK() {
		return HomePK;
	}
	public void setHomePK(String homePK) {
		HomePK = homePK;
	}
	public String getItemUrl() {
		return ItemUrl;
	}
	public void setItemUrl(String itemUrl) {
		ItemUrl = itemUrl;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public boolean isDownload() {
		return isDownload;
	}
	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	
	
}
