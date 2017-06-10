package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
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

	private static final String CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION = "Search For Available Reservation";
	private static final String CAMPGROUND_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION, CAMPGROUND_MENU_OPTION_RETURN };

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
			printHeading(parkChoice.getName() + " National Park");
			menu.displayParkInfo(parkChoice);
			handleParkInformationScreen(parkChoice);
			Campground[] databaseCampgroundArray = campgroundOptionArrayCreation(parkChoice);

		}
	}

	private void handleParkInformationScreen(Park park) {
		while(true) {
			printHeading("Select A Command");
			String choice = (String)menu.getChoiceFromOptions(PARKS_INFO_INTERFACE);
			if(choice.equals(PARKS_INFO_INTERFACE_VIEW_CAMPGROUNDS)) {
				handleViewCampground(park);
				campgroundmenu(park);
			} else if (choice.equals(PARKS_INFO_INTERFACE_SEARCH_RESERVATION)) {
				System.out.println();
				System.out.println("This function is not yet implemented.");
			} else if (choice.equals(PARKS_INFO_INTERFACE_RETURN)) {
				break;
			}
		}
	}

	private void campgroundmenu(Park park) {

		String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if(choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION)){
			while(true){
				handleViewCampground(park);
				System.out.println("Which campground (enter 0 to cancel)? ");
				Integer input = menu.getInputNumberFromUser();
				Campground[] campgroundchoices = campgroundDisplayOptionsInterface(park);
				Campground campgroundchoice = new Campground();
				if(input == 0){
					break;
				} else if (input <= campgroundchoices.length) {
					campgroundchoice = campgroundchoices[input];
				}
				System.out.println("What is the arrival date?");
				LocalDate arrivalDate = (LocalDate)menu.getDateFromUser();
				System.out.println("What is the departure date?");
				LocalDate departureDate = (LocalDate)menu.getDateFromUser();

				handleViewSites(campgroundchoice, arrivalDate, departureDate);

				System.out.println();
				System.out.println("Which site should be reserved (enter 0 to cancel)?");
				Site[] siteChoices = siteDisplayOptionsInterface(campgroundchoice, arrivalDate, departureDate);
				Site siteChoice = new Site();
				input = menu.getInputNumberFromUser();
				if(input == 0){
					break;
				} else { 
					for(Site site : siteChoices) {
						if(site.getSiteId() == (long)input) {
							siteChoice = site;
						}
					}
				}

				System.out.println("What name should the reservation be made under?");
				String nameInput = menu.getReservationName();
				reservationDAO.bookReservation(siteChoice, arrivalDate, departureDate, nameInput);
				Long reservationId = reservationDAO.getBookedReservation(siteChoice, arrivalDate, departureDate, nameInput).getReservationId();
				System.out.println();
				System.out.println("The reservation has been made and the confirmation id is " + reservationId);



			}

		}
		else if( choice.equals(CAMPGROUND_MENU_OPTION_RETURN)){

		}
	}

	private void handleViewSites(Campground campground, LocalDate arrivalDate, LocalDate departureDate){
		StringBuilder siteString = new StringBuilder();
		//			Long campgroundId = campground.getCampgroundId();
		Long daysBetween = ChronoUnit.DAYS.between(arrivalDate, departureDate);
		BigDecimal daysBetweens = new BigDecimal(daysBetween);
		siteString.append(String.format("%-9s %-13s %-13s %-13s %-13s %s", "Site No.", "Max Occup.", "Accesible?", "Max RV Length", "Utility" ,"Cost" +"\n"));
		for(int i = 0; i < siteDAO.getAvailableSites(campground ,arrivalDate, departureDate).size(); i++) {
			Site site = new Site();
			site = siteDAO.getAvailableSites(campground , arrivalDate, departureDate).get(i);
			siteString.append(String.format("%-9s %-13s %-13s %-13s %-13s %s", site.getSiteId(), site.getMaxOccupancy(),
					site.isAccessibleToString(site.isAccessible()), site.maxRVToString(site.getMaxRvLength()),
					site.isUtilitiesToString(site.isUtilities()), "$"+(campground.getDailyFee().multiply(daysBetweens))+  "\n"));
		}		
		System.out.println(siteString);

	}
	private void handleViewCampground(Park park) {
//		Park currentPark = new Park();
//		for(int i = 0; i < parkDAO.getAllAvailableParks().size(); i++) {
//			if(parkDAO.getAllAvailableParks().get(i).getParkId().equals(park.getParkId())) {
//				currentPark = parkDAO.getAllAvailableParks().get(i);
//			}
//		}
		System.out.println(park.getName() + " National Park Campgrounds");
		System.out.println();

		StringBuilder campgrounds = new StringBuilder();
		campgrounds.append(String.format("%-5s %-25s %-13s %-13s %s", "", "Name", "Open", "Close", "Daily Fee" + "\n"));
		for(int i = 0; i < campgroundDAO.getAvailableCampgrounds(park).size(); i++) {
			Campground camp = new Campground();
			camp = campgroundDAO.getAvailableCampgrounds(park).get(i);
			campgrounds.append(String.format("%-5s %-25s %-13s %-13s %s", "#" + (i+1), camp.getName(),
					camp.getOpeningDate(), camp.getClosingDate(), "$" + camp.getDailyFee() + "\n"));
		}		
		System.out.println(campgrounds);


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

	private Campground[] campgroundDisplayOptionsInterface(Park park) {
		Campground[] campgroundDisplay = new Campground[campgroundDAO.getAvailableCampgrounds(park).size()];
		for(int i = 0; i < campgroundDAO.getAvailableCampgrounds(park).size(); i++) {
			campgroundDisplay[i] = campgroundDAO.getAvailableCampgrounds(park).get(i);
		}
		return campgroundDisplay;
	}

	private Site[] siteDisplayOptionsInterface(Campground campground, LocalDate arrivalDate, LocalDate departureDate) {
		Site[] siteDisplay = new Site[siteDAO.getAvailableSites(campground, arrivalDate, departureDate).size()];
		for(int i = 0; i < siteDAO.getAvailableSites(campground, arrivalDate, departureDate).size(); i++) {
			siteDisplay[i] = siteDAO.getAvailableSites(campground, arrivalDate, departureDate).get(i);
		}
		return siteDisplay;
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
