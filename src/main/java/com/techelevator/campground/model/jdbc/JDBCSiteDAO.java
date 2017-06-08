package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Site> getAvailableSites(String campgroundId) {
		List<Site> listOfSites = new ArrayList<>();
		
		String sqlAvailableSites = "SELECT * FROM site "
				+ "WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableSites, campgroundId);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			listOfSites.add(site);
		}	
		
		return listOfSites;
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
		site.setUtilities(results.getBoolean("utilites"));
		return site;
	}

}
