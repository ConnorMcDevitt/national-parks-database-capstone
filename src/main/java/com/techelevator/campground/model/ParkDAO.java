package com.techelevator.campground.model;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> getAllAvailableParks();
	public Park getParkInformation(String parkName);

}
