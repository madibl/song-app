// == CS400 Spring 2024 File Header Information ==
// Name: Madison Lin
// Email: mblin@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:

import java.util.List;

/**
 * Class for that implements a song object. Implements the Song Interface.
 */
public class Song implements SongInterface {

	private String title;
	private String artist;
	private String genre;
	private int year;
	private int bpm;
	private int energy;
	private int danceability;
	private int loudness;
	private int liveness;

	/**
	 * Constructor for a song object which takes a string of the raw csv line,
	 * parses it, and stores it in its respective variables.
	 * 
	 * @param csvLine - string that csv returns, given by the readData method in the
	 *                backend class
	 */
	public Song(String csvLine) {
		// parse data
		String elements[] = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		this.title = elements[0];
		this.artist = elements[1].trim();
        this.genre = elements[2].trim();
        this.year = Integer.parseInt(elements[3].trim());
        this.bpm = Integer.parseInt(elements[4].trim());
        this.energy = Integer.parseInt(elements[5].trim());
        this.danceability = Integer.parseInt(elements[6].trim());
        this.loudness = Integer.parseInt(elements[7].trim());
        this.liveness = Integer.parseInt(elements[8].trim());
	}

	/**
	 * Compares this song object to another song object
	 * @param o - another object
	 * @return an integer. If 0, they are equal. If -1, song is less than o. If 1, song i
	 */
	@Override
	public int compareTo(SongInterface o) {
//		if (this.danceability > o.getDanceability())
//			return 1;
//		else if (this.danceability < o.getDanceability())
//			return -1;
//		else 
//			return 0;
		return this.title.compareTo(o.getTitle());
		
	}

	/**
	 * Getter method for the song's title
	 * @return the title
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Getter method for the song's artist
	 * @return the artist
	 */
	@Override
	public String getArtist() {
		return artist;
	}

	/**
	 * Getter method for the song's genres
	 * @return the genres
	 */
	@Override
	public String getGenres() {
		return genre;
	}

	/**
	 * Getter method for the song's year
	 * @return the year
	 */
	@Override
	public int getYear() {
		return year;
	}

	/**
	 * Getter method for the song's bpm
	 * @return the bpm
	 */
	@Override
	public int getBPM() {
		return bpm;
	}

	/**
	 * Getter method for the song's energy
	 * @return the energy
	 */
	@Override
	public int getEnergy() {
		return energy;
	}

	/**
	 * Getter method for the song's danceability
	 * @return the danceability
	 */
	@Override
	public int getDanceability() {
		return danceability;
	}

	/**
	 * Getter method for the song's loudness
	 * @return the loudness
	 */
	@Override
	public int getLoudness() {
		return loudness;
	}

	/**
	 * Getter method for the song's liveness
	 * @return the liveness
	 */
	@Override
	public int getLiveness() {
		return liveness;
	}

}
