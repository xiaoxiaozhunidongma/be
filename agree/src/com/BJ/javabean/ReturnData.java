package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;

public class ReturnData {

	private List<Party> party = new ArrayList<Party>();
	private List<Relation> relation = new ArrayList<Relation>();

	/**
	 * 
	 * @return The party
	 */
	public List<Party> getParty() {
		return party;
	}

	/**
	 * 
	 * @param party
	 *            The party
	 */
	public void setParty(List<Party> party) {
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
