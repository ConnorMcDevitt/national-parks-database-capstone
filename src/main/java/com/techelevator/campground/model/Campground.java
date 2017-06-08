package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Campground {
	
	private Long campgroundId;
	private Long parkId;
	private String name;
	private LocalDate openingDate;
	private LocalDate closingDate;
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
	
	public LocalDate getOpeningDate() {
		return openingDate;
	}
	
	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}
	
	public LocalDate getClosingDate() {
		return closingDate;
	}
	
	public void setClosingDate(LocalDate closingDate) {
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
