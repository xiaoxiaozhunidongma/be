package com.BJ.javabean;

import java.util.List;

public class MapAddParty {
	List<ImageText> remarkArray;
	Party party;
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public List<ImageText> getRemarkArray() {
		return remarkArray;
	}
	public void setRemarkArray(List<ImageText> remarkArray) {
		this.remarkArray = remarkArray;
	}
	public MapAddParty(List<ImageText> graphicDetailsList, Party party) {
		super();
		this.remarkArray = graphicDetailsList;
		this.party = party;
	}
	
	
}
