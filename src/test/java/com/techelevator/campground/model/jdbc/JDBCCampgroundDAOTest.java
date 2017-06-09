package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Park;

public class JDBCCampgroundDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO dao;
	private JDBCParkDAO parkDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
		dataSource.setUsername("postgres");

		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		dao = new JDBCCampgroundDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAvailableCampgrounds() {
		Park testPark = new Park();
		testPark = parkDAO.getAllAvailableParks().get(0);
		
		assertEquals("Blackwoods", dao.getAvailableCampgrounds(testPark).get(0).getName());
		assertEquals("Seawall", dao.getAvailableCampgrounds(testPark).get(1).getName());
		assertEquals("Schoodic Woods", dao.getAvailableCampgrounds(testPark).get(2).getName());
		assertEquals("01", dao.getAvailableCampgrounds(testPark).get(0).getOpeningDate());
		assertEquals("05", dao.getAvailableCampgrounds(testPark).get(1).getOpeningDate());
		assertEquals("05", dao.getAvailableCampgrounds(testPark).get(2).getOpeningDate());
		assertEquals("12", dao.getAvailableCampgrounds(testPark).get(0).getClosingDate());
		assertEquals("09", dao.getAvailableCampgrounds(testPark).get(1).getClosingDate());
		assertEquals("10", dao.getAvailableCampgrounds(testPark).get(2).getClosingDate());
		assertEquals(new BigDecimal("35.0"), dao.getAvailableCampgrounds(testPark).get(0).getDailyFee());
		assertEquals(new BigDecimal("30.0"), dao.getAvailableCampgrounds(testPark).get(1).getDailyFee());
		assertEquals(new BigDecimal("30.0"), dao.getAvailableCampgrounds(testPark).get(2).getDailyFee());
		
	}

}
