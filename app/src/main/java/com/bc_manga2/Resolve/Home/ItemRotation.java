package com.bc_manga2.Resolve.Home;

/**
 * Item基本構成
 */
public class ItemRotation {

	
	/***
	 * 該連結解析方式是否需要特殊手段
	 * 
	 * false為預設--不用
	 * true為要二度解析
	 */
	private boolean SpecialType=false;
	
	private String Image;//圖
	private String Name;//名
	private String Url;//连
	
	private String UpDate;//更新時間
	private String NewAion;//最新話
	private String HomePK;//-主站識別
	
	public ItemRotation() {}

	/**預設宣告方式 --不用特殊手段*/
	public ItemRotation(String Image,String Name,String Url) 
	{
		setImage(Image);
		setName(Name);
		setUrl(Url);
		setSpecialType(false);
	}
	/**指定解析方式 SpecialType == 1為要二度解析*/
	public ItemRotation(String Image,String Name,String Url,boolean SpecialType) 
	{
		setImage(Image);
		setName(Name);
		setUrl(Url);
		setSpecialType(SpecialType);
	}
	/**指定解析方式 SpecialType == 1為要二度解析*/
	public ItemRotation(String HomePK,String Image,String Name,String Url,boolean SpecialType) 
	{
		setHomePK(HomePK);
		setImage(Image);
		setName(Name);
		setUrl(Url);
		setSpecialType(SpecialType);
	}

	public boolean isSpecialType() {
		return SpecialType;
	}

	public void setSpecialType(boolean specialType) {
		SpecialType = specialType;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getUpDate() {
		return UpDate;
	}

	public void setUpDate(String upDate) {
		UpDate = upDate;
	}

	public String getNewAion() {
		return NewAion;
	}

	public void setNewAion(String newAion) {
		NewAion = newAion;
	}

	public String getHomePK() {
		return HomePK;
	}

	public void setHomePK(String homePK) {
		HomePK = homePK;
	}
	
	
	
}
