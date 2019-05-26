package com.leave.project.MODELS;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class PublicHollyday {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int hollydayId;
	
	@NotNull
	private String hollydayName;
	@NotNull
	private Date startDate;
	private String description;
	
	
	///getters and setters
	
	
	public int getHollydayId() {
		return hollydayId;
	}
	public void setHollydayId(int hollydayId) {
		this.hollydayId = hollydayId;
	}
	public String getHollydayName() {
		return hollydayName;
	}
	public void setHollydayName(String hollydayName) {
		this.hollydayName = hollydayName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PublicHollyday(int hollydayId, @NotNull String hollydayName, @NotNull Date startDate, String description) {
		super();
		this.hollydayId = hollydayId;
		this.hollydayName = hollydayName;
		this.startDate = startDate;
		this.description = description;
	}
	public PublicHollyday() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PublicHollyday [hollydayId=" + hollydayId + ", hollydayName=" + hollydayName + ", startDate="
				+ startDate + ", description=" + description + "]";
	}
	
}
