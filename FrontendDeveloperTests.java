import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Scanner;

public class FrontendDeveloperTests {
	@Test
	public void testDisplayDefaultMainMenu() {
		TextUITester tester = new TextUITester("Q\n");
		Scanner scanner = new Scanner(System.in);
		BackendInterface backend = new Backend(null);

		FrontendInterface testHolder = new Frontend(scanner, backend);
		testHolder.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("~~~ Command Menu ~~~") && output.contains("[R]")
				&& output.contains("[G]") && output.contains("[F]") && output.contains("[D]")
				&& output.contains("[Q]"));
	}

	@Test
	public void testReadFile() {
		TextUITester tester = new TextUITester("R\ncrash.csv\nsongs.csv\nQ\n");
		Scanner scanner = new Scanner(System.in);
		BackendInterface backend = new Backend(null);
		FrontendInterface testHolder = new Frontend(scanner, backend);
		testHolder.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("Enter path to csv file to load: "));
		Assertions.assertTrue(output.contains("That is not a valid file path. Please try again."));
		Assertions.assertTrue(output.contains("Done reading file."));
	}


	@Test
	public void testGetValues() {
		TextUITester tester = new TextUITester("G\n0 20\n-100 10\n10 20\nQ\n");
		Scanner scanner = new Scanner(System.in);
		BackendInterface backend = new Backend(null);
		FrontendInterface testHolder = new Frontend(scanner, backend);
		testHolder.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("Enter range of values (MIN - MAX):"));
		Assertions.assertTrue(output.contains(
				"Low must be greater than 0, and MAX must be greater than or equal to MIN."));
		Assertions.assertTrue(output.contains("5 songs found between 10 - 20:"));
	}


	@Test
	public void testSetFilter() {
		TextUITester tester = new TextUITester("G\n10 20\nF\n-2000\n1999\nQ\n");
		Scanner scanner = new Scanner(System.in);
		BackendInterface backend = new Backend(null);
		FrontendInterface testHolder = new Frontend(scanner, backend);
		testHolder.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("Enter maximum year: "));
		Assertions.assertTrue(output.contains("Please enter a valid maximum year."));
		Assertions.assertTrue(output.contains("2 songs found between 10 - 20 from 1999:"));
	}

	@Test
	public void testTopFive() {
		TextUITester tester = new TextUITester("D\nG\n10 20\nF\n1999\nD\nQ\n");
		Scanner scanner = new Scanner(System.in);
		BackendInterface backend = new Backend(null);
		FrontendInterface testHolder = new Frontend(scanner, backend);
		testHolder.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("You must call [G]et songs by BPM first."));
		Assertions.assertTrue(output.contains("Top Five songs found between 10 - 20 from 1999"));
	}

}
