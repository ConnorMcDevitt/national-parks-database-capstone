package com.techelevator.campground.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import com.techelevator.campground.model.Park;

public class Menu {
	
	private PrintWriter out;
	private Scanner in;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}
	
	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}
	
	public Park getParkFromInput(Park[] parkArray) {
		Park choice = null;
		while(choice == null) {
			displayParkOptions(parkArray);
			choice = getParkSelectionFromUser(parkArray);
		}
		return choice;
	}
	
	public Park getParkSelectionFromUser(Park[] parkArray) {
		Park parkChoice = null;
		String userInput = in.nextLine();
		try {
			int selectedNumber = Integer.valueOf(userInput);
			if(selectedNumber <= parkArray.length) {
				parkChoice = parkArray[selectedNumber - 1];
			} else if(selectedNumber == parkArray.length + 1) {
				System.exit(0);
			}
		} catch(NumberFormatException e) {
			
		}
		if(parkChoice == null) {
			out.println("\n*** " +userInput+" is not a valid park ***\n");
		}
		return parkChoice;
	}
	
	public Integer getInputNumberFromUser(){
		String userInput = in.nextLine();
		return Integer.valueOf(userInput);
	}
	
	public String getReservationName(){
		String userInput = in.nextLine();
		return userInput;	
	}
	
	public LocalDate getDateFromUser(){
		String userInput = in.nextLine();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		LocalDate date =  LocalDate.parse(userInput, formatter);
		return date;
	}
	
	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
	
	private void displayParkOptions(Park[] parkArray) {
		out.println();
		int number = 0;
		for(int i = 0; i < parkArray.length; i++) {
			number = i+1;
			out.println(number + ") " + parkArray[i].getName());
		}
		number++;
		out.println(number + ") Quit");
		out.flush();
	}
	
	public void displayParkInfo(Park selectedPark) {
		String location = selectedPark.getLocation();
		String established = selectedPark.getEstablishedDate().format(dateFormatter).toString();
		String area = NumberFormat.getNumberInstance(Locale.US).format(selectedPark.getArea()).toString();
		String visitors = NumberFormat.getNumberInstance(Locale.US).format(selectedPark.getVisitors()).toString();
		
		StringBuilder info = new StringBuilder();
		info.append(String.format("%-18s %s", "Location: ", location  + "\n"));
		info.append(String.format("%-18s %s", "Established: ", established + "\n"));
		info.append(String.format("%-18s %s", "Area: ", area  + " sq km\n"));
		info.append(String.format("%-18s %s", "Annual Visitors: ", visitors  + "\n"));

		System.out.println();
		System.out.println(info);


		StringBuilder description = new StringBuilder(selectedPark.getDescription());
		int i = 0;
		while ((i = description.indexOf(" ", i + 60)) != -1) {
			description.replace(i, i + 1, "\n");
		}
		System.out.println(description.toString());
	}

}
