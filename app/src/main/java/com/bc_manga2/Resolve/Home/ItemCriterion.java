package com.bc_manga2.Resolve.Home;

import java.util.ArrayList;




/**
 * 項目標準
 * @author kevinh
 *
 */
public class ItemCriterion {
	/**項目名稱*/
	private String CritName;
	
	/**該項目子項列表*/
	private ArrayList<ItemRotation> itemRotations;

	public String getCritName() {
		return CritName;
	}

	public void setCritName(String critName) {
		CritName = critName;
	}

	public ArrayList<ItemRotation> getItemRotations() {
		return itemRotations;
	}

	public void setItemRotations(ArrayList<ItemRotation> itemRotations) {
		this.itemRotations = itemRotations;
	}
	
	
	
}
