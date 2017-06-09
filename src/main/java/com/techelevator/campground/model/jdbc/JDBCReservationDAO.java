package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getReservations(Site site) {
		List<Reservation> listOfReservations = new ArrayList<>();
		Long siteId = site.getSiteId();
		String sqlAvailableReservations = "SELECT * FROM reservation "
				+ "WHERE site_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableReservations, siteId);
		
		while(results.next()) {
			Reservation reservation = mapRowToReservation(results);
			listOfReservations.add(reservation);
		}	
		
		return listOfReservations;
	}
	
	@Override
	public void bookReservation(Site siteChoice, LocalDate arrivalDate, LocalDate departureDate, String nameInput){
		String sqlBookReservation = "Insert into reservation (site_id, name, from_date, to_date, create_date) "
				+ "Values (?,?,?,?,NOW())";
		jdbcTemplate.update(sqlBookReservation, siteChoice.getSiteId(), nameInput, arrivalDate, departureDate);
	
	}
	public Reservation getBookedReservation(Site siteChoice, LocalDate arrivalDate, LocalDate departureDate, String nameInput) {
		String sqlGetReservationId = "Select * FROM reservation WHERE site_id = ? AND name=? AND from_date = ? AND to_date =? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationId, siteChoice.getSiteId(), nameInput, arrivalDate, departureDate);
		if(results.next()){
			return mapRowToReservation(results);
			
		}else{
			return null;
		}		
	}

	
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		
		Reservation reservation;
		reservation = new Reservation();
		
		reservation.setReservationId(results.getLong("reservation_id"));
		reservation.setSiteId(results.getLong("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		
		return reservation;
	}
	

}
