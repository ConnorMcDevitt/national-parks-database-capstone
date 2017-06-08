package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Campground {
	
	private Long campgroundId;
	private Long parkId;
	private String name;
	private String openingDate;
	private String closingDate;
	private BigDecimal dailyFee;
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public Long getParkId() {
		return parkId;
	}
	
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOpeningDate() {
		return openingDate;
	}
	
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	
	public String getClosingDate() {
		return closingDate;
	}
	
	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}
	
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	public String toString() {
		return this.name;
	}

}
