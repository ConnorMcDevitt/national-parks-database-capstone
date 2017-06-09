package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Site> getAvailableSites(Campground campground, LocalDate arrivalDate, LocalDate departureDate){
		List<Site> availableSites = new ArrayList<>();
		

		String sqlAvailableSites = "SELECT * FROM site WHERE campground_id = ? AND site_id NOT IN (SELECT site.site_id  "
				+ "FROM reservation "
				+ "JOIN site ON site.site_id = reservation.site_id "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE site.campground_id = ? "
				+ "AND ((?  BETWEEN reservation.from_date AND reservation.to_date)  "
						+ " OR (?  BETWEEN reservation.from_date AND reservation.to_date) "
						+ "OR (reservation.from_date BETWEEN ? AND ?) "
						+ "OR (reservation.to_date BETWEEN ? AND ?))) "
						+ " LIMIT 5; "; 
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableSites, campground.getCampgroundId(), campground.getCampgroundId(), 
				arrivalDate, departureDate, arrivalDate, departureDate,arrivalDate, departureDate) ;
		while(results.next()) {
			Site site = mapRowToSite(results);
			availableSites.add(site);
		}	
		
		return availableSites;
	}
	
	private Site mapRowToSite(SqlRowSet results) {
		Site site;
		site = new Site();
		site.setSiteId(results.getLong("site_id"));
		site.setCampgroundId(results.getLong("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		return site;
	}

}
