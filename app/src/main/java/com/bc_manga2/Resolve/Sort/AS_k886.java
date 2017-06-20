package com.bc_manga2.Resolve.Sort;

import java.util.ArrayList;

import com.bc_manga2.Application.GivenHttp;

public class AS_k886 {

	private String HomePK;
	private String[] g = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
			, "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"
			, "21", "22", "23" };
	private String[] f = {"萌系","搞笑","格鬥","科幻","劇情","偵探","競技","魔法","神鬼","校園"
			,"驚悚","廚藝","偽娘","圖片","冒險","小說","港漫","耽美","經典","歐美","日文","親情","修真"};
	public AS_k886(String HomePK) {
		this.HomePK = HomePK;
	}

	public ArrayList<ItemSort> SortItem() {
		ArrayList<ItemSort> itemSorts = new ArrayList<>();
		for (int i = 0; i < g.length; i++) {
			String url = GivenHttp.k886_SortFine+g[i];
			itemSorts.add(new ItemSort(HomePK, f[i], url, ""));
		}
		return itemSorts;
	}

}
