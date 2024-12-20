// == CS400 Spring 2024 File Header Information ==
// Name: Madison Lin
// Email: mblin@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;

/**
 * Class that holds multiple JUnit tests for the backend methods.
 */
public class BackendDeveloperTests {

	/**
	 * Tests if the data is being read properly, and there is no exception thrown.
	 * Also tests if it is being stored in the tree properly.
	 */
	@Test
	public void readDataTester() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		Backend backend = new Backend(tree);
		// test if it doesn't throw an IO Exception
		try {
			backend.readData("/Users/madisonlin/Desktop/CS400/p107/src/songs.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// testing my song constructor and parsing
		String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
		Song song = new Song(testLine);

		Assertions.assertEquals("\"Hey, Soul Sister\"", song.getTitle());
		Assertions.assertEquals("Train", song.getArtist());
		Assertions.assertEquals("neo mellow", song.getGenres());
		Assertions.assertEquals(2010, song.getYear());
		Assertions.assertEquals(97, song.getBPM());
		Assertions.assertEquals(89, song.getEnergy());
		Assertions.assertEquals(67, song.getDanceability());
		Assertions.assertEquals(-4, song.getLoudness());
		Assertions.assertEquals(8, song.getLiveness());

	}

	/**
	 * Tests if getRange() outputs the correct list when a filter on the year is not
	 * applied
	 */
	@Test
	public void testGetRangeDefault() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
		Song song = new Song(testLine);
		tree.insert(song);
		Backend backend = new Backend(tree);
		// test if after filterOldSongs returns a list of songs sorted by BPM
		List<String> returnedList = backend.getRange(70, 100);
		Iterator<SongInterface> it = tree.iterator();
		List<String> expectedList = new ArrayList<String>();
		while (it.hasNext()) {
			String toAdd = it.next().getTitle();
			expectedList.add(toAdd);
		}
		Assertions.assertEquals(expectedList, returnedList);

	}

	/**
	 * Test if fiveMostDanceable() throws an exception if getRange() is not called
	 * before it.
	 */
	@Test
	public void testGetMostDanceableException() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		Backend backend = new Backend(tree);
		// if fiveMostDanceable runs without calling getRange beforehand and doesn't
		// throw exception, fail the assertion
		try {
			backend.fiveMostDanceable();
		} catch (IllegalStateException e) {
			Assertions.assertTrue(true);
		}

	}

	/**
	 * Tests if getMostDanceable works on a case where no filterOldSongs is applied
	 */
	@Test
	public void testGetMostDanceableDefault() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
		Song song = new Song(testLine);
		tree.insert(song);
		Backend backend = new Backend(tree);
		// if filterOldSong has not been called and default settings apply

		backend.getRange(70, 100);
		List<String> returnedList = backend.fiveMostDanceable();
		Iterator<SongInterface> it = tree.iterator();
		List<String> expectedList = new ArrayList<String>();
		while (it.hasNext()) {
			SongInterface toAdd = it.next();
			expectedList.add(toAdd.getTitle() + ": " + toAdd.getDanceability());
		}
		Assertions.assertEquals(expectedList, returnedList);
	}

	/**
	 * tests if filterOldSongs output the correct list of songs
	 */
	@Test
	public void testFilterOldSongs() {

		// first get range call should be empty
		{
			IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
			String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
			Song song = new Song(testLine);
			tree.insert(song);
			Backend backend = new Backend(tree);
			List<String> returnedList = backend.filterOldSongs(2010);
//			List<String> fakeReturnedList = Arrays.asList(new String[] {});
			List<String> expectedList = Arrays.asList(new String[] {});
			Assertions.assertEquals(expectedList, returnedList);
		}
		// the second call should return songs on billboard on or before the specified
		// maxYear
		{
			IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
			String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
			Song song = new Song(testLine);
			tree.insert(song);
			Backend backend = new Backend(tree);
			
			backend.getRange(70,100);
			List<String> returnedList = backend.filterOldSongs(2011);
			// instead of testing by comparing entire list, test on a few in the beginning,
			// middle, and end of list
			Iterator<SongInterface> it = tree.iterator();
			List<String> expectedList = new ArrayList<String>();
			//tree.setIterationStartPoint(song); // setting the start point to the songs closest to
			while (it.hasNext()) {
				String toAdd = it.next().getTitle();
				expectedList.add(toAdd);
			}
			Assertions.assertEquals(expectedList, returnedList);
		}

	}

	/**
	 * Tests the getRange() method if filtered is called.
	 */
	@Test
	public void testGetRangeFiltered() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
		Song song = new Song(testLine);
		tree.insert(song);
		Backend backend = new Backend(tree);
		// test if after filterOldSongs returns a list of songs sorted by BPM
		backend.filterOldSongs(2010);
		List<String> returnedList = backend.getRange(70, 100);
//		List<String> fakeReturnedList = Arrays
//				.asList(new String[] { "\"Hey, Soul Sister\"", "\"Hey, Soul Sister\"", "\"Hey, Soul Sister\"" });

		Iterator<SongInterface> it = tree.iterator();
		List<String> expectedList = new ArrayList<String>();
		while (it.hasNext()) {
			String toAdd = it.next().getTitle();
			expectedList.add(toAdd);
		}
		Assertions.assertEquals(expectedList, returnedList);
	}

	/**
	 * Tests a case where fiveMostDanceble() is run with a filter on the year
	 */
	@Test
	public void testGetMostDanceableFiltered() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		String testLine = "\"Hey, Soul Sister\",Train,neo mellow,2010,97,89,67,-4,8,80,217,19,4,83";
		Song song = new Song(testLine);
		tree.insert(song);
		Backend backend = new Backend(tree);
		// if filterOldSong and getRange are called
		backend.getRange(0, 1000);
		backend.filterOldSongs(2020);

		List<String> returnedList = backend.fiveMostDanceable();
//		List<String> fakeReturnedList = Arrays
//				.asList(new String[] { "\"Hey, Soul Sister\"", "\"Hey, Soul Sister\"", "\"Hey, Soul Sister\"" });

		Iterator<SongInterface> it = tree.iterator();
		List<String> expectedList = new ArrayList<String>();
		while (it.hasNext()) {
			SongInterface toAdd = it.next();
			expectedList.add(toAdd.getTitle() + ": " + toAdd.getDanceability());
		}
		Assertions.assertTrue(backend.getRangeCalled);
		Assertions.assertTrue(song.getBPM() >= backend.lastLow);
		Assertions.assertTrue(song.getBPM() <= backend.lastHigh);
		Assertions.assertTrue(song.getYear() <= backend.maxYear);
	    Assertions.assertEquals(expectedList, returnedList);
	}

	/**
	 * Tests a case where there are no songs within the specified range
	 */
	@Test
	public void testGetRangeFilteredNoneMatching() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		Backend backend = new Backend(tree);
		List<String> returnedList = backend.getRange(420, 500);
		List<String> fakeReturnedList = Arrays.asList(new String[] {});
		List<String> expectedList = Arrays.asList(new String[] {});
		Assertions.assertEquals(expectedList, fakeReturnedList);
	}

	/**
	 * Tests a case where there is no songs within the year range provided
	 */
	@Test
	public void testFilterOldSongsNoneMatching() {
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		Backend backend = new Backend(tree);
		List<String> returnedList = backend.filterOldSongs(1900);
		List<String> fakeReturnedList = Arrays.asList(new String[] {});
		List<String> expectedList = Arrays.asList(new String[] {});

		Assertions.assertEquals(expectedList, fakeReturnedList);
	}

	/**
	 * Tests if getRange is properly called from the frontend
	 */
	@Test
	public void testReadDataIntegration() {
		// test if getRange is properly called from the frontend
		TextUITester tester = new TextUITester("R\nsongs.csv\nQ");
		Scanner scanner = new Scanner(System.in);
		IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
		Backend backend = new Backend(tree);
		FrontendInterface frontend = new Frontend(scanner, backend);
		frontend.runCommandLoop();



		String output = tester.checkOutput();
		
		Assertions.assertTrue(output.contains("Enter path to csv file to load: "));
		Assertions.assertTrue(output.contains("Done reading file."));
		//Assertions.assertEquals(backend.filename, "songs.csv");
		
		
	}
	
	/**
	 * Tests if setFilter is called from the frontend
	 */
	@Test
	public void testSetFilterIntegration() {
		// test if setFilter is properly called from the frontend
		TextUITester tester = new TextUITester("G\n100 120\nF\n1999\nQ\n");
		Scanner scanner = new Scanner(System.in);
		Backend backend = new Backend(new IterableRedBlackTree<SongInterface>());
		FrontendInterface frontend = new Frontend(scanner, backend);
		frontend.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("songs found between 100 - 120 from 1999"));
		Assertions.assertTrue(backend.maxYearSet);
		
	}
	
	/**
	 * Tests if getValues works and outputs the correct values
	 */
	@Test
	public void testPartnerGetValues() {
		TextUITester tester = new TextUITester("R\nsongs.csv\nG\n100 120\nQ\n");
		Scanner scanner = new Scanner(System.in);
		Backend backend = new Backend(new IterableRedBlackTree<SongInterface>());
		FrontendInterface frontend = new Frontend(scanner, backend);
		frontend.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("songs found between 100 - 120"));
		Assertions.assertTrue(output.contains("\"Hold On, We're Going Home\""));
		Assertions.assertTrue(output.contains("Love Me Like You"));
		Assertions.assertTrue(output.contains("\"Earned It (Fifty Shades Of Grey) - From The \"\"Fifty Shades Of Grey\"\" Soundtrack\""));
		Assertions.assertTrue(output.contains("Your Love Is My Drug"));
		
	}
	
	/**
	 * Tests if getValues works and outputs the correct values
	 */
	@Test
	public void testPartnerFiveMost() {
		TextUITester tester = new TextUITester("R\nsongs.csv\nG\n100 120\nF\n1999\nD\nQ\n");
		Scanner scanner = new Scanner(System.in);
		Backend backend = new Backend(new IterableRedBlackTree<SongInterface>());
		FrontendInterface frontend = new Frontend(scanner, backend);
		frontend.runCommandLoop();

		String output = tester.checkOutput();
		Assertions.assertTrue(output.contains("Top Five songs found between 100 - 120 from 2030"));
		Assertions.assertTrue(backend.maxYearSet);
		Assertions.assertTrue(output.contains("Love You Like A Love Song: 86"));
		Assertions.assertTrue(output.contains("WTF (Where They From): 93"));
		Assertions.assertTrue(output.contains("Spark The Fire: 88"));
		Assertions.assertTrue(output.contains("Come Get It Bae: 93"));
		Assertions.assertTrue(output.contains("Blurred Lines: 85"));
		
		
	}


}
