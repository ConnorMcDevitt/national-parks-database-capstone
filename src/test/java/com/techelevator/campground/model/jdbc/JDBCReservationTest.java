package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.Site;

public class JDBCReservationTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO dao;
	private JDBCCampgroundDAO campDAO;
	private JDBCParkDAO parkDAO;
	private JDBCReservationDAO reservationDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campgroundtest");
		dataSource.setUsername("postgres");

		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();

	}

	@Before
	public void setUp() throws Exception {
		dao = new JDBCSiteDAO(dataSource);
		campDAO = new JDBCCampgroundDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();

	}

	@Test
	public void testGetReservations() {
		 String nameInput;
		 Site siteChoice = new Site();
		 Long id = new Long(1);
		 siteChoice.setSiteId(id);
		 Reservation test;
		 test = reservationDAO.getReservations(siteChoice).get(0);
		 
			assertEquals(1, reservationDAO.getReservations(siteChoice).get(0).getSiteId().longValue());
			assertEquals(1, reservationDAO.getReservations(siteChoice).get(0).getReservationId().longValue());
			assertEquals("Smith Family Reservation", reservationDAO.getReservations(siteChoice).get(0).getName());	 
	}

	@Test
	public void testBookReservation() {
		 String nameInput = "Steve";
		 LocalDate arrival = LocalDate.of(2017, 1, 1);
		 LocalDate departure = LocalDate.of(2017, 1, 2);
		 Site siteChoice = new Site();
		 Long id = new Long(1);
		 siteChoice.setSiteId(id);
		 reservationDAO.bookReservation(siteChoice, arrival, departure, nameInput);	
		 assertEquals(1, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getSiteId().longValue());
		 assertEquals(arrival, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getFromDate());
		 assertEquals(departure, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getToDate());
		 assertEquals("Steve", reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).toString());
		 
	}

	@Test
	public void testGetBookedReservation() {
		 String nameInput = "Steve";
		 LocalDate arrival = LocalDate.of(2017, 1, 1);
		 LocalDate departure = LocalDate.of(2017, 1, 2);
		 Site siteChoice = new Site();
		 Long id = new Long(1);
		 siteChoice.setSiteId(id);
		 reservationDAO.bookReservation(siteChoice, arrival, departure, nameInput);	
		 assertEquals(1, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getSiteId().longValue());
		 assertEquals(arrival, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getFromDate());
		 assertEquals(departure, reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).getToDate());
		 assertEquals("Steve", reservationDAO.getBookedReservation(siteChoice, arrival, departure, nameInput).toString());
	}

}
