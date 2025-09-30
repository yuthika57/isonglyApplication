import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Backend - Implements the BackendInterface for managing a collection of songs.
 */

public class Backend implements BackendInterface {
    private IterableSortedCollection<Song> songTree;
    private Integer yearLow = null, yearHigh = null;
    private Integer loudnessThreshold = null;

    public Backend(IterableSortedCollection<Song> tree) {
        this.songTree = tree;
    }

    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to this backend' constructor.  Don't forget to
     * create a Comparator to pass to the constructor for each Song object that
     * you create.  This will be used to store these songs in order within your
     * tree, and to retrieve them by year range in the getRange method.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */

    @Override
    public void readData(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IOException("Error: File not found at: " + file.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("Error: CSV file is empty or missing headers.");
            }

            String[] headers = parseCSVLine(headerLine);
            int titleIndex = -1, artistIndex = -1, genreIndex = -1, yearIndex = -1;
            int bpmIndex = -1, energyIndex = -1, danceabilityIndex = -1, loudnessIndex = -1, livenessIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].trim().toLowerCase()) {
                    case "title": titleIndex = i; break;
                    case "artist": artistIndex = i; break;
                    case "top genre": genreIndex = i; break;
                    case "year": yearIndex = i; break;
                    case "bpm": bpmIndex = i; break;
                    case "nrgy": energyIndex = i; break;
                    case "dnce": danceabilityIndex = i; break;
                    case "db": loudnessIndex = i; break;
                    case "live": livenessIndex = i; break;
                }
            }

            if (titleIndex == -1 || artistIndex == -1 || genreIndex == -1 || yearIndex == -1 ||
                bpmIndex == -1 || energyIndex == -1 || danceabilityIndex == -1 ||
                loudnessIndex == -1 || livenessIndex == -1) {
                throw new IOException("Error: Missing required column(s).");
            }

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] values = parseCSVLine(line);
                    if (values.length < headers.length) {
                        continue;
                    }

                    String title = values[titleIndex].trim();
                    String artist = values[artistIndex].trim();
                    String genre = values[genreIndex].trim();
                    int year = Integer.parseInt(values[yearIndex].trim());
                    int bpm = Integer.parseInt(values[bpmIndex].trim());
                    int energy = Integer.parseInt(values[energyIndex].trim());
                    int danceability = Integer.parseInt(values[danceabilityIndex].trim());
                    int loudness = Integer.parseInt(values[loudnessIndex].trim());
                    int liveness = Integer.parseInt(values[livenessIndex].trim());

                    Comparator<Song> songComparator = Comparator.comparingInt(Song::getYear);
                    Song song = new Song(title, artist, genre, year, bpm, energy, danceability, loudness, liveness, songComparator);
                    songTree.insert(song);

                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                }
            }
        }
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        return result.toArray(new String[0]);
    }

    /**
     * Retrieves a list of song titles from the tree passed to the contructor.
     * The songs should be ordered by the songs' year, and fall within
     * the specified range of year values.  This year range will
     * also be used by future calls to filterSongs and getFiveMost.
     *
     * If a loudness filter has been set using the filterSongs method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.
     *
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a
     * argument for the high parameter means that there is no maximum
     * year to include in the returned list.
     *
     * @param low is the minimum year of songs in the returned list
     * @param high is the maximum year of songs in the returned list
     * @return List of titles for all songs from low to high that pass any
     *     set filter, or an empty list when no such songs can be found
     */

    @Override
    public List<String> getRange(Integer low, Integer high) {
        if (low != null) this.yearLow = low;
        else this.yearLow = null;

        if (high != null) this.yearHigh = high;
        else this.yearHigh = null;

        List<Song> filteredSongs = new ArrayList<>();

        for (Song song : songTree) {
            int year = song.getYear();
            boolean withinYearRange = (yearLow == null || year >= yearLow) &&
                                      (yearHigh == null || year <= yearHigh);
            boolean passesLoudnessFilter = (this.loudnessThreshold == null || song.getLoudness() < this.loudnessThreshold);

            if (withinYearRange && passesLoudnessFilter) {
                filteredSongs.add(song);
            }
        }

        filteredSongs.sort(Comparator.comparingInt(Song::getYear).thenComparing(Song::getTitle));

        List<String> result = new ArrayList<>();
        for (Song song : filteredSongs) {
            result.add(song.getTitle());
        }
        return result;
    }

    /**
     * Retrieves a list of song titles that have a loudness that is
     * smaller than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * year, and should only include songs that fall within the specified
     * range of year values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high year bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and fiveMost methods.
     *
     * When null is passed as the threshold to this method, then no
     * loudness threshold should be used.  This clears the filter.
     *
     * @param threshold filters returned song titles to only include songs that
     *     have a loudness that is smaller than this threshold.
     * @return List of titles for songs that meet this filter requirement and
     *     are within any previously set year range, or an empty list
     *     when no such songs can be found
     */

    @Override
    public List<String> filterSongs(Integer threshold) {
        this.loudnessThreshold = threshold;
        List<String> filteredTitles = new ArrayList<>();
        for (Song song : songTree) {
            boolean meetsLoudnessCriteria = (this.loudnessThreshold == null || song.getLoudness() >= this.loudnessThreshold);
            if (meetsLoudnessCriteria) {
                filteredTitles.add(song.getTitle());
            }
        }
        filteredTitles.sort(Comparator.comparingInt(title -> {
            for (Song song : songTree) {
                if (song.getTitle().equals(title)) {
                    return song.getYear();
                }
            }
            return Integer.MAX_VALUE;
        }));
        return filteredTitles;
    }

    /**
     * This method returns a list of song titles representing the five
     * most danceable songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to filteredSongs.  The order of the song titles
     * in this returned list is up to you.
     *
     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most danceable song titles
     */

    @Override
    public List<String> fiveMost() {
        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : songTree) {
            boolean meetsYearCriteria = (yearLow == null || song.getYear() >= yearLow) &&
                                         (yearHigh == null || song.getYear() <= yearHigh);
            boolean meetsLoudnessCriteria = (this.loudnessThreshold == null || song.getLoudness() >= this.loudnessThreshold);
            if (meetsYearCriteria && meetsLoudnessCriteria) {
                filteredSongs.add(song);
            }
        }

        filteredSongs.sort(Comparator.comparingInt(Song::getDanceability).reversed());

        List<String> topFive = new ArrayList<>();
        for (int i = 0; i < Math.min(5, filteredSongs.size()); i++) {
            topFive.add(filteredSongs.get(i).getTitle());
        }
        return topFive;
    }
}
