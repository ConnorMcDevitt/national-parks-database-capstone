package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllAvailableParks() {
		List<Park> listOfParks = new ArrayList<>();
		
		String sqlAvailableParks = "SELECT * FROM park "
				+ "ORDER BY name";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableParks);
		
		while(results.next()) {
			Park newPark = mapRowToPark(results);
			listOfParks.add(newPark);
		}	
		
		return listOfParks;
	}
	
	@Override
	public Park getParkInformation(String parkName) {
		Park selectedPark = new Park();
		
		String sqlSelectedPark = "SELECT * FROM park "
				+ "WHERE name = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectedPark, parkName);
		
		while(results.next()) {
			selectedPark = mapRowToPark(results);
		}
		
		return selectedPark;
		
	}
	
	private Park mapRowToPark(SqlRowSet results) {
		Park park;
		park = new Park();
		park.setParkId(results.getLong("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishedDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getLong("area"));
		park.setVisitors(results.getLong("visitors"));
		park.setDescription(results.getString("description"));
		
		return park;
		
	}

}
