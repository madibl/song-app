// == CS400 Spring 2024 File Header Information ==
// Name: Madison Lin
// Email: mblin@wisc.edu
// Lecturer: Garyy Dahl
// Notes to Grader:

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Class that implements the methods listed in the interface. Determines the
 * main functions of the entire program.
 */
public class Backend extends ISCPlaceholder<SongInterface> implements BackendInterface {
	IterableSortedCollection<SongInterface> tree;
	protected boolean maxYearSet;
	protected int maxYear;
	protected int lastLow;
	protected int lastHigh;
	protected String filename;
	boolean getRangeCalled = false;
	List<String> getRangeList;
	List<String> filterList;
	List<String> danceableList;

	public Backend(IterableSortedCollection<SongInterface> tree) {
		this.tree = tree;
		maxYearSet = false;
		getRangeList = new ArrayList<String>();
		filterList = new ArrayList<String>();
		danceableList = new ArrayList<String>();
	}

	/**
	 * Loads data from the .csv file referenced by filename.
	 * 
	 * @param filename is the name of the csv file to load data from
	 * @throws IOException when there is trouble finding/reading file
	 */
	@Override
	public void readData(String filename) throws IOException {
		if (filename == null)
			throw new IOException("Trouble finding/reading file.");
//		File csv = new File(filename);
		BufferedReader csvReader = new BufferedReader(new FileReader(filename));

		// account for the header line - skip one line
		csvReader.readLine();
		String line;
		while ((line = csvReader.readLine()) != null) {
			Song song = new Song(line);
			tree.insert(song);
		}
		csvReader.close();

	}

	/**
	 * Retrieves a list of song titles for songs that have a Speed (BPM) within the
	 * specified range (sorted by BPM in ascending order). If a maxYear filter has
	 * been set using filterOldSongs(), then only songs on Billboard during or
	 * before that maxYear should be included in the list that is returned by this
	 * method.
	 *
	 * Note that either this bpm range, or the resulting unfiltered list of songs
	 * should be saved for later use by the other methods defined in this class.
	 *
	 * @param low   is the minimum Speed (BPM) of songs in the returned list
	 * @param hight is the maximum Speed (BPM) of songs in the returned list
	 * @return List of titles for all songs in specified range
	 */
	@Override
	public List<String> getRange(int low, int high) {
		// set iterator
		Iterator<SongInterface> treeIt = tree.iterator();
		List<String> toReturn = new ArrayList<String>();
		List<SongInterface> working = new ArrayList<SongInterface>();
		lastLow = low;
		lastHigh = high;

		// if maxYear filter is applies
		if (maxYearSet) {
			// traverse through filtered list?
			while (treeIt.hasNext()) {
				SongInterface current = treeIt.next();
				if (current.getBPM() >= low && current.getBPM() <= high && current.getYear() <= maxYear) {
					working.add(current);
				}
			}

		} else {
			// traverse through tree, finding song within the bpm range
			while (treeIt.hasNext()) {
				SongInterface current = treeIt.next();
				if (current.getBPM() >= low && current.getBPM() <= high) {
					working.add(current);
				}
			}
		}

		insertionSortBPM(working);
		for (SongInterface s : working) {
			toReturn.add(s.getTitle());
		}
		getRangeCalled = true;
		return toReturn;

	}

	/**
	 * Filters the list of songs returned by future calls of getRange() and
	 * fiveMostDanceable() to only include older songs. If getRange() was previously
	 * called, then this method will return a list of song titles (sorted in
	 * ascending order by Speed BPM) that only includes songs on Billboard on or
	 * before the specified maxYear. If getRange() was not previously called, then
	 * this method should return an empty list.
	 *
	 * Note that this maxYear threshold should be saved for later use by the other
	 * methods defined in this class.
	 *
	 * @param maxYear is the maximum year that a returned song was on Billboard
	 * @return List of song titles, empty if getRange was not previously called
	 */
	@Override
	public List<String> filterOldSongs(int maxYear) {
		Iterator<SongInterface> treeIt = tree.iterator();
		List<String> toReturn = new ArrayList<String>();
		List<SongInterface> working = new ArrayList<SongInterface>();
		this.maxYear = maxYear;
		// if getRange is not called
		if (getRangeCalled == false) {
			return toReturn;
		} else {
			while (treeIt.hasNext()) {
				SongInterface current = treeIt.next();
				if (current.getBPM() >= lastLow && current.getBPM() <= lastHigh && current.getYear() <= maxYear) {
					working.add(current);
				}
			}
		}

		maxYearSet = true;
		insertionSortBPM(working);
		// after finished the final list, make it into a List<String>
		for (SongInterface s : working) {
			toReturn.add(s.getTitle());
		}
		return toReturn;
	}

	/**
	 * This method makes use of the attribute range specified by the most recent
	 * call to getRange(). If a maxYear threshold has been set by filterOldSongs()
	 * then that will also be utilized by this method. Of those songs that match
	 * these criteria, the five most danceable will be returned by this method as a
	 * List of Strings in increasing order of danceability. Each string contains the
	 * danceability followed by a colon, a space, and then the song's title. If
	 * fewer than five such songs exist, return all of them.
	 *
	 * @return List of five most danceable song titles and their danceabilities
	 * @throws IllegalStateException when getRange() was not previously called.
	 */
	@Override
	public List<String> fiveMostDanceable() {

		Iterator<SongInterface> treeIt = tree.iterator();
		List<String> toReturn = new ArrayList<String>();
		List<SongInterface> working = new ArrayList<SongInterface>();

		if (getRangeCalled == false) {
			throw new IllegalStateException("getRange() was not previously called.");
		}

		// if getRange defined and max year defined
		while (treeIt.hasNext()) {
		    SongInterface current = treeIt.next();
		    if (getRangeCalled && maxYearSet) {
		    	if (current.getBPM() >= lastLow && current.getBPM() <= lastHigh && current.getYear() <= maxYear) {
			        if (working.size() < 5) {
			            working.add(current);
			        } else {
			            if (current.getDanceability() > working.get(0).getDanceability()) {
			                working.remove(0);
			                working.add(current);
			            }
			        }
		    	}
		    } 
		    else {
		    	if (current.getBPM() >= lastLow && current.getBPM() <= lastHigh) {
			        if (working.size() < 5) {
			            working.add(current);
			        } else {
			            if (current.getDanceability() > working.get(0).getDanceability()) {
			                working.remove(0);
			                working.add(current);
			            }
			        }
		    	}
		    }
			// Sort the working list based on danceability
			List<SongInterface> sortedWorking = new ArrayList<>(working);
			insertionSortDanceability(sortedWorking);
		}


		

		// after finished the final list, make it into a List<String>
		for (SongInterface s : working) {
			toReturn.add(s.getTitle() + ": " + s.getDanceability());
		}

		return toReturn;
	}

	/**
	 * Helper method that sorts the list by ascending danceabilities
	 * @param list - list to sort
	 */
	private void insertionSortDanceability(List<SongInterface> list) {
		int n = list.size();
		// iterate through list starting with second element
		for (int i = 1; i < n; ++i) {
			// store current element
			SongInterface song = list.get(i);
			int j = i - 1;
			// move elements of list that are greater than key
			while (j >= 0 && list.get(j).getDanceability() > song.getDanceability()) {
				list.set(j + 1, list.get(j)); // shift to right
				j = j - 1; // move to previous element
			}
			list.set(j + 1, song);
		}
	}

	/**
	 * Helper method that sorts the list by ascending bpm
	 * @param list - list to sort
	 */
	private void insertionSortBPM(List<SongInterface> list) {
		int n = list.size();
		// iterate through list starting with second element
		for (int i = 1; i < n; ++i) {
			// store current element
			SongInterface song = list.get(i);
			int j = i - 1;
			// move elements of list that are greater than key
			while (j >= 0 && list.get(j).getBPM() > song.getBPM()) {
				list.set(j + 1, list.get(j)); // shift to right
				j = j - 1; // move to previous element
			}
			list.set(j + 1, song);
		}
	}

}
