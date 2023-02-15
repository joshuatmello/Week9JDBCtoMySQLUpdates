package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	//This is my I/O layer. 
	
	/**
	 * A list of operations will be printed on the console so 
	 * that the user will be reminded which selection to make.
	 */
	
	// @formatter:off
			private List<String> operations = List.of(
				"1) Add a project"
			);
			// @formatter:on
			
	private Scanner scanner = new Scanner(System.in);
			
	//instantiates an object from the service layer class
	private ProjectService projectService = new ProjectService();
						

	/**
	 * main menu
	 */	
	public static void main(String[] args) {
		
		//Instantiates a new object and calls the method to process the menu.
		
		new ProjectsApp().processUserSelections();
		
				
	} //closes the main method

	/**
	 * This method displays the menu selections, gets user input, processes input
	 */
	private void processUserSelections() {
		boolean done = false;
		
		//While done is true, do the try-catch block
		while (!done) {
			try {
				int selection = getUserSelection();
				
				switch (selection) {
				case -1: 
					done = exitMenu();
					break;
					//this works by making done true, so the while(!done)
					//becomes false and ends the loop. 
				
				case 1:
					createProject();					
					break;
					
				default:
					System.out.println("\n" + selection + 
							" is not a valid selection. Try again.");
				}
			}
			catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
				//the above implicitly calls the toString() method in 
				//Exception class on object e
			}
		}
	}

	/**
	 * Gather the project details from user, put them into a new project. 
	 */
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
				
		//calls the service layer class method to add the project. 
		//The value returned from projectService.addProject() is different 
		//from the Project object passed to the method. 
		//It contains the project ID that was added by MySQL.

		Project dbProject = projectService.addProject(project); 
		System.out.println("You have successfully created project: " +
				dbProject);
	}

	/**
	 * Takes user input and converts it into a BigDecimal. 
	 * @param prompt
	 * @return null, user input as a BigDecimal, or throws exception
	 */
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		//uses isNull() method of Objects class, passing in input
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2); //2 decimal places
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a decimal number. Try again.");
		}		
	}

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	/**
	 * Prints the operations and accepts user input as an Integer. 
	 * @return if null enters -1 and exits menu, otherwise returns the input
	 */
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		//calls the isNull method in Object class to see if input is null, 
		//if it is returns -1; otherwise returns input value
		return Objects.isNull(input) ? -1 : input;	
	}

	/**
	 * Takes user input and converts it to an Integer, which may be null. 
	 * @param prompt is input from the user
	 * @return null or the converted value of user input, if valid
	 * otherwise throws unchecked exception
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
//		Objects.isNull(input) ? null : input;
		
		//using the isNull method of Class Objects to see if input is null
		if(Objects.isNull(input)) {
			return null;
		}
		
		//if input is not null, it needs to be converted from String to Integer
		//calls the valueOf method on the Integer class to make this conversion.
		//If the conversion is not possible (entry is not a number), catch the error
		//which is thrown as NumberFormatException. 
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number. Try again.");
		}		
	
	}

	/**
	 * Prints prompt and gets input from user. 
	 * @param prompt is what is entered by user
	 * @return either null or the user input, trimmed of spaces
	 */
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
		
		
//		return Objects.isNull(input) ? null : input.trim();
		
//		if(Objects.isNull(input)) {
//			return null;
//		}
//		try {
//			input.trim();
//		}
//		catch(Exception e) {
//			throw new DbException("Error.");
//		}
		
	}

	/**
	 * Prints each available selection on a separate line. 
	 */
	
	private void printOperations() {
		System.out.println("\nThese are the available selections. "
				+ "Press the Enter key to quit:");
		
		//Print each line in the operations variable:
		//operations, of type List, calls a method 
		//on the List class called forEach(Consumer) which runs a loop for all items. 
		//The parameter Consumer is an Interface that has a single abstract method 
		//which is void and name accept(parameter). The Lambda -> expression is a 
		//single parameter and the printLn returns void. 
		
		operations.forEach(line -> System.out.println("   " + line));
		
	}

}
