package com.BJ.javabean;


public class CreateOrder {
	private Integer pk_financial;
	private String order_id;
	private String order_sn;
	private Integer order_type;
	private String create_time;
	private String arrival_time;
	private Integer from_user;
	private Integer to_user;
	private String fk_party;
	private Integer arrival_type;
	private float amount;
	private Integer status;
	public Integer getPk_financial() {
		return pk_financial;
	}
	public void setPk_financial(Integer pk_financial) {
		this.pk_financial = pk_financial;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getOrder_type() {
		return order_type;
	}
	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}
	public Integer getFrom_user() {
		return from_user;
	}
	public void setFrom_user(Integer from_user) {
		this.from_user = from_user;
	}
	public Integer getTo_user() {
		return to_user;
	}
	public void setTo_user(Integer to_user) {
		this.to_user = to_user;
	}
	public String getFk_party() {
		return fk_party;
	}
	public void setFk_party(String fk_party) {
		this.fk_party = fk_party;
	}
	public Integer getArrival_type() {
		return arrival_type;
	}
	public void setArrival_type(Integer arrival_type) {
		this.arrival_type = arrival_type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
