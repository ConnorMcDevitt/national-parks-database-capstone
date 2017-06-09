package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
	public List<Site> getAvailableSites (Long campgroundId, LocalDate arrivalDate, LocalDate departureDate);

}
