package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {
	
	private static final String PARK_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_RESERVATION = "Search For Reservation";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_VIEW_CAMPGROUNDS, PARK_MENU_OPTION_SEARCH_RESERVATION, PARK_MENU_OPTION_RETURN };
	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
	}
	
	public void run() {
		
		displayBanner();
		//Main Menu
		while(true) {
			printHeading("Select a park for further details");
			
			String[] PARK_DISPLAY_OPTIONS = parkDisplayOptionsInterface();
			String choice = (String)menu.getChoiceFromOptions(PARK_DISPLAY_OPTIONS);
			int numberChoice = 0;
			for(int i = 0; i < PARK_DISPLAY_OPTIONS.length; i++) {
				if(PARK_DISPLAY_OPTIONS[i].equals(choice)) {
					numberChoice = i;
				}
			}
			
			if(numberChoice == (PARK_DISPLAY_OPTIONS.length - 1)) {
				System.exit(0);
			} else {
				printHeading(PARK_DISPLAY_OPTIONS[numberChoice]);
				
				displayParkInfo(numberChoice);
				
				handleParkSubMenu(parkDAO.getAllAvailableParks().get(numberChoice).getParkId());
			}
		}
		
	}
	
private void handleParkSubMenu(Long parkId) {
		printHeading("Select A Command");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if(choice.equals(PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
				handleViewCampground(parkId);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH_RESERVATION)) {
				handleReservationSearch();
		} else if (choice.equals(PARK_MENU_OPTION_RETURN)) {

		}
	}
	
	private void handleViewCampground(Long parkId) {
		Park currentPark = new Park();
		for(int i = 0; i < parkDAO.getAllAvailableParks().size(); i++) {
			if(parkDAO.getAllAvailableParks().get(i).getParkId().equals(parkId)) {
				currentPark = parkDAO.getAllAvailableParks().get(i);
			}
		}
		System.out.println(currentPark.getName() + " National Park Campgrounds");
		System.out.println();
		
		StringBuilder campgrounds = new StringBuilder();
		campgrounds.append(String.format("%-5s %-25s %-13s %-13s %s", "", "Name", "Open", "Close", "Daily Fee" + "\n"));
		for(int i = 0; i < campgroundDAO.getAvailableCampgrounds(parkId).size(); i++) {
			Campground camp = new Campground();
			camp = campgroundDAO.getAvailableCampgrounds(parkId).get(i);
			campgrounds.append(String.format("%-5s %-25s %-13s %-13s %s", "#" + (i+1), camp.getName(), camp.getOpeningDate(), camp.getClosingDate(), "$" + camp.getDailyFee() + "\n"));
		}		
		System.out.println(campgrounds);
		
	}

	private void handleReservationSearch() {
	// TODO Auto-generated method stub
	
}

	private void displayParkInfo(int userChoice) {
		
		Park selectedPark = parkDAO.getAllAvailableParks().get(userChoice);
		
		StringBuilder info = new StringBuilder();
		info.append(String.format("%-18s %s", "Location: ", selectedPark.getLocation()  + "\n"));
		info.append(String.format("%-18s %s", "Established: ", selectedPark.getEstablishedDate().toString()  + "\n"));
		info.append(String.format("%-18s %s", "Area: ", selectedPark.getArea().toString()  + " sq km\n"));
		info.append(String.format("%-18s %s", "Annual Visitors: ", selectedPark.getVisitors().toString()  + "\n"));
		
		System.out.println(info);
		System.out.println();
		
	    StringBuilder description = new StringBuilder(selectedPark.getDescription());
	    int i = 0;
	    while ((i = description.indexOf(" ", i + 60)) != -1) {
	        description.replace(i, i + 1, "\n");
	    }
		System.out.println(description.toString());
	}
	
	private String[] parkDisplayOptionsInterface() {
		String[] parkDisplay = new String[parkDAO.getAllAvailableParks().size() + 1];
		for(int i = 0; i < parkDAO.getAllAvailableParks().size(); i++) {
			parkDisplay[i] = parkDAO.getAllAvailableParks().get(i).getName();
		}
		parkDisplay[parkDAO.getAllAvailableParks().size()] = "Quit";
		return parkDisplay;
	}
	
	private Campground[] campgroundDisplayOptionsInterface(Park userChoicePark) {
		Campground[] campgroundDisplay = new Campground[campgroundDAO.getAvailableCampgrounds(userChoicePark.getParkId()).size() + 1];
		for(int i = 0; i < campgroundDAO.getAvailableCampgrounds(userChoicePark.getParkId()).size(); i++) {
			campgroundDisplay[i] = campgroundDAO.getAvailableCampgrounds(userChoicePark.getParkId()).get(i);
		}
		return campgroundDisplay;
	}
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private void displayBanner() {

		System.out.println(" __  _   ____  _____  _  ____  __  _   ____   _       _____  ____  _____  __  __   ____ ");
		System.out.println("|  \\| | / () \\|_   _|| |/ () \\|  \\| | / () \\ | |__    | ()_)/ () \\ | () )|  |/  / (_ (_`");
		System.out.println("|_|\\__|/__/\\__\\ |_|  |_|\\____/|_|\\__|/__/\\__\\|____|   |_|  /__/\\__\\|_|\\_\\|__|\\__\\.__)__)");
		System.out.println();

	}
}
