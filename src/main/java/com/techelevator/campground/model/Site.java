package com.techelevator.campground.model;

public class Site {
	private Long siteId;
	private Long campgroundId;
	private Integer siteNumber;
	private Integer maxOccupancy;
	private boolean accessible;
	private Integer maxRvLength;
	private boolean utilities;
	
	public Long getSiteId() {
		return siteId;
	}
	
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public Integer getSiteNumber() {
		return siteNumber;
	}
	
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	public Integer getMaxOccupancy() {
		return maxOccupancy;
	}
	
	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	
	public boolean isAccessible() {
		
		return accessible;
	}
	public String isAccessibleToString(boolean accesible){
		if(accesible){
			return "Yes";
		}else{
			return "No";
		}
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	public Integer getMaxRvLength() {
		return maxRvLength;
	}
	public String maxRVToString(Integer maxRvLength){
		if(maxRvLength == 0){
			return "N/A";
		}else{
			return maxRvLength.toString();
		}
	}
	
	public void setMaxRvLength(Integer maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	
	public boolean isUtilities() {
		return utilities;
	}
	public String isUtilitiesToString(boolean utilities){
		if(utilities){
			return "Yes";
		}else{
			return "N/A";
		}
	}
	
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
}