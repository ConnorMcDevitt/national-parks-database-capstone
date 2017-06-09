package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Park;

public class JDBCParkDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;

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
		dao = new JDBCParkDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllAvailableParks() {
		
		assertEquals("Acadia", dao.getAllAvailableParks().get(0).getName());
		assertEquals("Maine", dao.getAllAvailableParks().get(0).getLocation());
		assertEquals(LocalDate.of(1919, 02, 26), dao.getAllAvailableParks().get(0).getEstablishedDate());
		assertEquals(47389, dao.getAllAvailableParks().get(0).getArea().longValue());
		
	}
	
	@Test
	public void testGetParkInformation() {
		
		assertEquals("Acadia", dao.getParkInformation("Acadia").getName());
		assertEquals("Maine", dao.getParkInformation("Acadia").getLocation());
		assertEquals(LocalDate.of(1919, 02, 26), dao.getParkInformation("Acadia").getEstablishedDate());
		assertEquals(47389, dao.getParkInformation("Acadia").getArea().longValue());
		assertEquals("Acadia", dao.getAllAvailableParks().get(0).getName());
		assertEquals("Maine", dao.getAllAvailableParks().get(0).getLocation());
		assertEquals(LocalDate.of(1919, 02, 26), dao.getAllAvailableParks().get(0).getEstablishedDate());
		assertEquals(47389, dao.getAllAvailableParks().get(0).getArea().longValue());
		
	}

}
