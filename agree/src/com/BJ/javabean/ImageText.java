package com.BJ.javabean;

import java.io.Serializable;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "graphic_details")
public class ImageText extends Model implements Serializable {
	public ImageText() {
	}

	// 1.文本 2.图片
	@Column
	private Integer pk_party_remark;
	@Column
	private String fk_party;
	@Column
	private Integer type;
	@Column
	private String text;
	@Column
	private String image_path;
	@Column
	private Integer style;
	@Column
	private Integer font_size;
	@Column
	private String font_color;
	@Column
	private Integer image_height;
	@Column
	private Integer image_width;
	// @Column
	// private Integer _order;
	@Column
	private Integer status;

	public Integer getPk_party_remark() {
		return pk_party_remark;
	}

	public void setPk_party_remark(Integer pk_party_remark) {
		this.pk_party_remark = pk_party_remark;
	}

	public String getFk_party() {
		return fk_party;
	}

	public void setFk_party(String fk_party) {
		this.fk_party = fk_party;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public Integer getFont_size() {
		return font_size;
	}

	public void setFont_size(Integer font_size) {
		this.font_size = font_size;
	}

	public String getFont_color() {
		return font_color;
	}

	public void setFont_color(String font_color) {
		this.font_color = font_color;
	}

	public Integer getImage_height() {
		return image_height;
	}

	public void setImage_height(Integer image_height) {
		this.image_height = image_height;
	}

	public Integer getImage_width() {
		return image_width;
	}

	public void setImage_width(Integer image_width) {
		this.image_width = image_width;
	}

	// public Integer get_order() {
	// return _order;
	// }
	//
	// public void set_order(Integer _order) {
	// this._order = _order;
	// }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		// return "{pk_party_remark=" + pk_party_remark + ", fk_party="+
		// fk_party +
		// ", type=" + type + ", text=" + text
		// + ", image_path=" + image_path + ", style=" + style
		// + ", font_size=" + font_size + ", font_color=" + font_color
		// + ", image_height=" + image_height + ", image_width="
		// + image_width + ", status=" + status + "}";

//		return "{" + "\"" +"pk_party_remark" + "\"" + ":" + pk_party_remark + ","+"\""+"fk_party"+"\""+":"
//				+ fk_party + ","+"\""+"type"+"\""+":" + type + ","+"\""+"text"+"\""+":" + text
//				+ ",\"image_path\":" + image_path + ",\"style\":" + style
//				+ ","+"\""+"font_size"+"\""+":" + font_size + ","+"\""+"font_color"+"\""+":"
//				+ font_color + ","+"\""+"image_height"+"\""+":" + image_height
//				+ ","+"\""+"image_width"+"\""+":" + image_width + ","+"\""+"status"+"\""+":" + status
//				+ "}";
		
		StringBuffer sb = new StringBuffer(); 
//		sb.append('{').append("\"pk_party_remark\":").append(pk_party_remark).append(",\"fk_party\":").append(fk_party)
//		.append(",\"type\":").append(type).append(",\"text\":").append(text).append(",\"image_path\":").append(image_path)
//		.append(",\"style\":").append(style).append(",\"font_size\":").append(font_size).append(",\"font_color\":").append(font_color).append(",\"image_height\":")
//		.append(image_height).append(",\"image_width\":").append(image_width).append(",\"status\":").append(status).append('}');
		sb.append('{').append('"').append("pk_party_remark").append('"').append(':').append(pk_party_remark)
		.append(',').append('"').append("fk_party").append('"').append(':').append(fk_party)
		.append(',').append('"').append("type").append('"').append(':').append(type)
		.append(',').append('"').append("text").append('"').append(':').append(text)
		.append(',').append('"').append("image_path").append('"').append(':').append(image_path)
		.append(',').append('"').append("style").append('"').append(':').append(style)
		.append(',').append('"').append("font_size").append('"').append(':').append(font_size)
		.append(',').append('"').append("font_color").append('"').append(':').append(font_color)
		.append(',').append('"').append("image_height").append('"').append(':').append(image_height)
		.append(',').append('"').append("image_width").append('"').append(':').append(image_width)
		.append(',').append('"').append("status").append('"').append(':').append(status).append('}');
		
		Log.e("ImageText", "获取的sb======="+sb.toString());
		
		
		
		
		
		return sb.toString();

	}

	public ImageText(Integer pk_party_remark, String fk_party, Integer type,
			String text, String image_path, Integer style, Integer font_size,
			String font_color, Integer image_height, Integer image_width,
			Integer status) {
		super();
		this.pk_party_remark = pk_party_remark;
		this.fk_party = fk_party;
		this.type = type;
		this.text = text;
		this.image_path = image_path;
		this.style = style;
		this.font_size = font_size;
		this.font_color = font_color;
		this.image_height = image_height;
		this.image_width = image_width;
		this.status = status;
	}

}
