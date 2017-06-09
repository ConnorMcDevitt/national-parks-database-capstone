package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
	
	public List<Reservation> getReservations(Site site);
	public void bookReservation(Site siteChoice, LocalDate arrivalDate, LocalDate departureDate, String nameInput);
	public Reservation getBookedReservation(Site siteChoice, LocalDate arrivalDate, LocalDate departureDate, String nameInput) ;


}
