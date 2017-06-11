package com.techelevator;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {

	private static final String PARKS_INFO_INTERFACE_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARKS_INFO_INTERFACE_SEARCH_RESERVATION = "Search For Reservation";
	private static final String PARKS_INFO_INTERFACE_RETURN = "Return to Previous Screen";
	private static final String[] PARKS_INFO_INTERFACE = new String[] { PARKS_INFO_INTERFACE_VIEW_CAMPGROUNDS, PARKS_INFO_INTERFACE_SEARCH_RESERVATION, PARKS_INFO_INTERFACE_RETURN };

	private static final String PARK_CAMPGROUNDS_SEARCH_RESERVATIONS = "Search For Available Reservation";
	private static final String PARK_CAMPGROUNDS_RETURN = "Return to Previous Screen";
	private static final String[] PARK_CAMPGROUNDS = new String[] { PARK_CAMPGROUNDS_SEARCH_RESERVATIONS, PARK_CAMPGROUNDS_RETURN };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;


	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
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

		while(true) {

			printHeading("Select a park for further details");
			Park[] databaseParksArray = parkOptionArrayCreation();
			Park parkChoice = (Park)menu.getParkFromInput(databaseParksArray);
			handleParkInformationScreen(parkChoice);
		}
	}

	private void handleParkInformationScreen(Park park) {

		printHeading(park.getName() + " National Park");
		menu.displayParkInfo(park);

		Campground[] databaseCampgroundArray = campgroundOptionArrayCreation(park);

		while(true) {

			printHeading("Select A Command");
			String choice = (String)menu.getChoiceFromOptions(PARKS_INFO_INTERFACE);
			if(choice.equals(PARKS_INFO_INTERFACE_VIEW_CAMPGROUNDS)) {
				handleViewCampgrounds(park, databaseCampgroundArray);
			} else if (choice.equals(PARKS_INFO_INTERFACE_SEARCH_RESERVATION)) {
				System.out.println();
				System.out.println("This function is not yet implemented.");
			} else if (choice.equals(PARKS_INFO_INTERFACE_RETURN)) {
				break;
			}
		}
	}

	private void handleViewCampgrounds(Park park, Campground[] databaseCampgroundArray) {
		menu.displayCampgrounds(park, databaseCampgroundArray);

		while(true) {

			String choice = (String)menu.getChoiceFromOptions(PARK_CAMPGROUNDS);
			if(choice.equals(PARK_CAMPGROUNDS_SEARCH_RESERVATIONS)) {
				handleSearchForCampgroundReservation(park, databaseCampgroundArray);
			} else if(choice.equals(PARK_CAMPGROUNDS_RETURN)) {
				break; 
			}
		}
	}

	private void handleSearchForCampgroundReservation(Park park, Campground[] databaseCampgroundArray) {

		menu.displayCampgrounds(park, databaseCampgroundArray);
		Campground campgroundChoice = new Campground();
		LocalDate arrivalDate = null;
		LocalDate departureDate = null;

		while(true) {

			System.out.println("Which campground (enter 0 to cancel)? ");
			campgroundChoice = menu.getCampgroundSelectionFromUser(databaseCampgroundArray);
			if(campgroundChoice == null) {
				break;
			}
			while(arrivalDate == null) {
				System.out.println("What is the arrival date (MM/DD/YYYY)? ");
				arrivalDate = (LocalDate)menu.getDateFromUser();
			}
			while(departureDate == null) {
				System.out.println("What is the departure date(MM/DD/YYYY)? ");
				departureDate = (LocalDate)menu.getDateFromUser();
				if(departureDate.isBefore(arrivalDate)) {
					System.out.println(departureDate.toString() + " is before " + arrivalDate + ". Please enter a different date.\n");
					departureDate = null;
				}
			}
			handleMakingReservation(campgroundChoice, arrivalDate, departureDate);
		}

	}

	private void handleMakingReservation(Campground campgroundChoice, LocalDate arrivalDate, LocalDate departureDate ) {

		boolean inMenu = true;
		Site[] sitesInCampground = siteOptionArrayCreation(campgroundChoice, arrivalDate, departureDate);
		menu.displaySites(sitesInCampground, campgroundChoice, arrivalDate, departureDate);
		Site siteChoice = new Site();
		String reservationName = null;
		
		if(sitesInCampground.length == 0) {
			inMenu = false;
			System.out.println("No sites are available between those dates.");
		}
		
		while(inMenu) {
			System.out.println("Which site should be reserved (enter 0 to cancel)? ");
			siteChoice = menu.getSiteSelectionFromUser(sitesInCampground);
			if(campgroundChoice == null) {
				break;
			}
			while(reservationName == null) {
				System.out.println("What name should the reservation be made under? ");
				reservationName = menu.getReservationName();
				if(reservationName != null) {
					reservationDAO.bookReservation(siteChoice, arrivalDate, departureDate, reservationName);
					Reservation reservation = reservationDAO.getBookedReservation(siteChoice, arrivalDate, departureDate, reservationName);
					String reservationId = reservation.getReservationId().toString();
					System.out.println("The reservation has been made and the confirmation id is " + reservationId + ".");
					run();
				} else {
					System.out.println("Please enter a name!\n");
				}
			}
		}

	}

	private Park[] parkOptionArrayCreation() {
		Park[] parkArray = new Park[parkDAO.getAllAvailableParks().size()];
		for(int i = 0; i < parkDAO.getAllAvailableParks().size(); i++) {
			parkArray[i] = parkDAO.getAllAvailableParks().get(i);
		}
		return parkArray;
	}

	private Campground[] campgroundOptionArrayCreation(Park park) {
		Campground[] campgroundArray = new Campground[campgroundDAO.getAvailableCampgrounds(park).size()];
		for(int i = 0; i < campgroundDAO.getAvailableCampgrounds(park).size(); i++) {
			campgroundArray[i] = campgroundDAO.getAvailableCampgrounds(park).get(i);
		}
		return campgroundArray;
	}

	private Site[] siteOptionArrayCreation(Campground campground, LocalDate arrivalDate, LocalDate departureDate) {
		Site[] siteArray = new Site[siteDAO.getAvailableSites(campground, arrivalDate, departureDate).size()];
		for(int i = 0; i < siteDAO.getAvailableSites(campground, arrivalDate, departureDate).size(); i++) {
			siteArray[i] = siteDAO.getAvailableSites(campground, arrivalDate, departureDate).get(i);
		}
		return siteArray;
	}

	private void displayBanner() {
		System.out.println(" __  _   ____  _____  _  ____  __  _   ____   _       _____  ____  _____  __  __   ____ ");
		System.out.println("|  \\| | / () \\|_   _|| |/ () \\|  \\| | / () \\ | |__    | ()_)/ () \\ | () )|  |/  / (_ (_`");
		System.out.println("|_|\\__|/__/\\__\\ |_|  |_|\\____/|_|\\__|/__/\\__\\|____|   |_|  /__/\\__\\|_|\\_\\|__|\\__\\.__)__)");
		System.out.println();
	}

	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
