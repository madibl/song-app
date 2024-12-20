import java.io.IOException;
import java.util.Scanner;
import java.util.List;

/**
 * Frontend - CS400 Project 1: iSongify This class is an implementation of the FrontendInterface for
 * the iSongify project.
 */
public class Frontend implements FrontendInterface {

	private String min = "min";
	private String max = "max";
	private String year = "none";
	private Scanner in;
	private BackendInterface backend;

	public Frontend(Scanner in, BackendInterface backend) {
		this.in = in;
		this.backend = backend;
	}

	/**
	 * Repeated gives the user an opportunity to issue new commands until they select Q to quit.
	 */
	public void runCommandLoop() {
		String userChoice = "";
		while (!userChoice.equals("Q")) {
			displayMainMenu();
			userChoice = in.nextLine();
			switch (userChoice) {
				case "R":
					readFile();
					break;
				case "G":
					getValues();
					break;
				case "F":
					setFilter();
					break;
				case "D":
					topFive();
					break;
				case "Q":
					break;
				default:
					System.out.println("That's not a valid option. Try again.");
					break;
			}
		}
	}

	/**
	 * Displays the menu of command options to the user.
	 */
	public void displayMainMenu() {
		String menu = """
				~~~ Command Menu ~~~
				    [R]ead Data
				    [G]et Songs by Speed BPM [min - max]
				    [F]ilter Old Songs (by Max Year: none)
				    [D]isplay Five Most Danceable
				    [Q]uit
				Choose command:""";
		menu = menu.replace("min", min).replace("max", max).replace("none", year);
		System.out.print(menu + " ");
	}

	/**
	 * Provides text-based user interface and error handling for the [R]ead Data command.
	 */
	public void readFile() {
		String filePath = "";

		while (true) {
			try {
				System.out.print("Enter path to csv file to load: ");
				filePath = in.nextLine();
				backend.readData(filePath);
				System.out.println("Done reading file.");
				break;
			} catch (IOException e) {
				System.err.println("That is not a valid file path. Please try again.");
			} catch (Exception e) {
				System.err.println("Something went wrong. Please try again.");
			}
		}

	}

	/**
	 * Provides text-based user interface and error handling for the [G]et Songs by Speed BPM
	 * command.
	 */
	public void getValues() {
		System.out.print("Enter range of values (MIN - MAX): ");

		int low = in.nextInt();
		int high = in.nextInt();

		while (low > high || low <= 0) {
			System.out.println(
					"Low must be greater than 0, and MAX must be greater than or equal to MIN.");
			System.out.print("Enter range of values (MIN - MAX): ");
			low = in.nextInt();
			high = in.nextInt();
		}
		List<String> songs = backend.getRange(low, high);
		String outputString = songs.size() + " songs found between " + low + " - " + high + ":\n";
		for (String song : backend.getRange(low, high)) {
			outputString += "\t" + song + "\n";
		}
		this.min = String.valueOf(low);
		this.max = String.valueOf(high);
		System.out.println(outputString);
		in.nextLine();
	}


	/**
	 * Provides text-based user interface and error handling for the [F]ilter Old Songs (by Max
	 * Year) command.
	 */
	public void setFilter() {
		System.out.print("Enter maximum year: ");
		int maxYear = in.nextInt();
		while (maxYear <= 0) {
			System.err.println("Please enter a valid maximum year.");
			System.out.print("Enter maximum year: ");
			maxYear = in.nextInt();
		}
		this.year = String.valueOf(maxYear);
		List<String> songs = backend.filterOldSongs(maxYear);
		String outputString = songs.size() + " songs found between " + this.min + " - " + this.max
				+ " from " + maxYear + ":\n";

		for (String song : songs) {
			outputString += "\t" + song + "\n";
		}
		System.out.println(outputString);
		in.nextLine();
	}

	/**
	 * Provides text-based user interface and error handling for the [D]isplay Five Most Danceable
	 * command.
	 */
	public void topFive() {
		try {
			List<String> songs = backend.fiveMostDanceable();
			String output = "Top Five songs found between " + this.min + " - " + this.max + " from "
					+ this.year + "\n";
			for (String song : songs) {
				output += "\t" + song + "\n";
			}
			System.out.println(output);
		} catch (IllegalStateException e) {
			System.err.println("You must call [G]et songs by BPM first.");
		} catch (Exception e) {
			System.out.println("Something went wrong. Please try again later.");
		}
	}

}
