package com.BJ.javabean;

public class Photo_Review {
	
	private Integer pr_id;
	
	private String fk_photo;
	
	private String content;
	
	private String create_date;
	
	private Integer fk_user;
	
	private Integer review_type;
	
	private Integer status;

	public Integer getPr_id() {
		return pr_id;
	}

	public void setPr_id(Integer pr_id) {
		this.pr_id = pr_id;
	}

	public String getFk_photo() {
		return fk_photo;
	}

	public void setFk_photo(String fk_photo) {
		this.fk_photo = fk_photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Integer getFk_user() {
		return fk_user;
	}

	public void setFk_user(Integer fk_user) {
		this.fk_user = fk_user;
	}

	public Integer getReview_type() {
		return review_type;
	}

	public void setReview_type(Integer review_type) {
		this.review_type = review_type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
