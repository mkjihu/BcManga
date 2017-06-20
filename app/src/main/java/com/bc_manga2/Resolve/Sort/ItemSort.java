package com.bc_manga2.Resolve.Sort;

public class ItemSort {

	private String HomePK="";//-主站識別
	private String ItemName="";//名稱--預設為空
	private String ItemUrl ="";//網址--預設為空
	private String ImageUrl="";//Image--預設為空
	
	
	public ItemSort() {}
	public ItemSort(String HomePK,String ItemName,String ItemUrl,String ImageUrl) {
		this.HomePK=HomePK;
		this.ItemName=ItemName;
		this.ItemUrl=ItemUrl;
		this.ImageUrl=ImageUrl;
	}
	
	public String getHomePK() {
		return HomePK;
	}
	public void setHomePK(String homePK) {
		HomePK = homePK;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getItemUrl() {
		return ItemUrl;
	}
	public void setItemUrl(String itemUrl) {
		ItemUrl = itemUrl;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	
}
