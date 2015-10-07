package com.BJ.javabean;

public class MapAddParty {
	String remarkArray;
	Party party;
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public String getRemarkArray() {
		return remarkArray;
	}
	public void setRemarkArray(String remarkArray) {
		this.remarkArray = remarkArray;
	}
	public MapAddParty(String remarkArray, Party party) {
		super();
		this.remarkArray = remarkArray;
		this.party = party;
	}
	
	
}
