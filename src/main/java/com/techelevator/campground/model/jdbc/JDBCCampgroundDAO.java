package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> getAvailableCampgrounds(Long parkId) {
		List<Campground> listOfCampgrounds = new ArrayList<>();
		
		String sqlAvailableCampgrounds = "SELECT * FROM campground "
				+ "WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableCampgrounds, parkId);
		
		while(results.next()) {
			Campground campground = mapRowToCampground(results);
			listOfCampgrounds.add(campground);
		}	
		
		return listOfCampgrounds;
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground;
		campground = new Campground();
		campground.setCampgroundId(results.getLong("campground_id"));
		campground.setParkId(results.getLong("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpeningDate(results.getDate("open_from_mm").toLocalDate());
		campground.setClosingDate(results.getDate("open_to_mm").toLocalDate());
		campground.setDailyFee(results.getBigDecimal("daily_fee"));
		return campground;
	}

}
