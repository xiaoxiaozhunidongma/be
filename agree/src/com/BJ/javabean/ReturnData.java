package com.BJ.javabean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnData {

	private List<Party3> party = new ArrayList<Party3>();
	private List<Relation> relation = new ArrayList<Relation>();

	/**
	 * 
	 * @return The party
	 */
	public List<Party3> getParty() {
		return party;
	}

	/**
	 * 
	 * @param party
	 *            The party
	 */
	public void setParty(List<Party3> party) {
		this.party = party;
	}

	/**
	 * 
	 * @return The relation
	 */
	public List<Relation> getRelation() {
		return relation;
	}

	/**
	 * 
	 * @param relation
	 *            The relation
	 */
	public void setRelation(List<Relation> relation) {
		this.relation = relation;
	}

	@Override
	public String toString() {
		return "ReturnData [party=" + party + ", relation=" + relation + "]";
	}

}
