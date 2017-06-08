package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getReservations(Long siteId) {
		List<Reservation> listOfReservations = new ArrayList<>();
		
		String sqlAvailableReservations = "SELECT * FROM reservation "
				+ "WHERE site_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableReservations, siteId);
		
		while(results.next()) {
			Reservation reservation = mapRowToReservation(results);
			listOfReservations.add(reservation);
		}	
		
		return listOfReservations;
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
