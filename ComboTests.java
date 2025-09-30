import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * Tests both the backend and the frontend.
 */
public class ComboTests {
    private static Backend backend;
    private static Frontend frontend;
    
    /**
     * Integration Test Setup: Initializes the backend and frontend instances
     * before running integration tests. This method loads the `songs.csv` data file
     * and ensures any potential I/O errors are properly handled.
     *
     * If the `songs.csv` file cannot be read, the test setup will fail with an
     * appropriate error message.
     */
    @BeforeAll
    public static void setup() {
        try {
            IterableSortedCollection<Song> songTree = new IterableRedBlackTree<>();
            backend = new Backend(songTree);
            frontend = new Frontend(new Scanner(System.in), backend);
            backend.readData("songs.csv");
        } catch (IOException e) {
            fail("IOException occurred while reading the file: " + e.getMessage());
        }
    }

    /**
     * This test verifies that the backend correctly loads and retrieves songs 
     * within the specified year range. It ensures that:
     * - The `getRange` method does not return null.
     * - Songs are printed for verification purposes.
     */
    @Test
    public void backendTest1() {
        Backend backend = new Backend(new Tree_Placeholder());
     // Check that the backend loads songs within the given year range
        var songs = backend.getRange(2000, 2025);
        assertNotNull(songs, "Songs should have been loaded into the backend.");
        System.out.println("Songs loaded within the range 2000-2025:");
        for (String song : songs) {
            System.out.println(song);
        }
    }

    /**
     * Tests the getRange() method to verify that songs within a specified 
     * year range are correctly retrieved from the backend.
     * Steps:
     * - Initializes a new backend instance with a placeholder tree.
     * - Reads song data from the CSV file.
     * - Calls getRange method and retrieves the result.
     * - Checks that the returned list matches the expected songs.
     */
    @Test
    public void backendTest2() throws IOException {
        Backend backend = new Backend(new Tree_Placeholder());
        backend.readData("songs.csv");

        List<String> result = backend.getRange(2015, 2017);


        List<String> expected = Arrays.asList("BO$$", "Cake By The Ocean", "A L I E N S");
        assertEquals(expected, result, "getRange() did not return the expected list.");
    }


    /**
     * Tests the filterSongs() method to verify that songs are correctly filtered 
     * based on a given loudness threshold.
     * Steps:
     * - Defines a loudness threshold of -5.
     * - Calls filterSongs() with the threshold to get the filtered song list.
     * - Ensures that the returned list is not empty after applying the filter.
     * - Verifies that expected songs are present in the actual filtered list.
     *
     * Expected Behavior:
     * - The returned list should not be empty.
     * - The expected songs should be included in the list after filtering.
     */
    @Test
    public void backendTest3() {
        Backend backend = new Backend(new Tree_Placeholder());
        int threshold = -5;
        List<String> result = backend.filterSongs(threshold);
        
        // Ensure that the result is not empty after applying the filter
        assertFalse(result.isEmpty(), "Filtered song list should not be empty.");
        
        // Checking that the expected songs are in the result
        assertTrue(result.contains("BO$$") && result.contains("Cake By The Ocean") && result.contains("A L I E N S"));
    }

    /**
     * Tests the fiveMost() method to check that the top 5 most danceable songs 
     * are correctly returned and contain expected songs.
     *
     * Steps:
     * - Calls fiveMost() to get the list of the top 5 danceable songs.
     * - Ensures the list contains no more than 5 songs.
     * - Verifies that the expected songs are present in the final list.
     * - Also checks that the songs are sorted in descending order of danceability.
     */
    @Test
    public void backendTest4() {
        Backend backend = new Backend(new Tree_Placeholder());
        
        // Call the method to get the top 5 danceable songs
        List<String> result = backend.fiveMost();
       
        // Ensure that the result list does not contain more than 5 songs
        assertTrue(result.size() <= 5, "The list should not contain more than 5 songs.");
        
        // Ensure that the expected songs are present in the result
        assertTrue(result.contains("BO$$") && result.contains("Cake By The Ocean") && result.contains("A L I E N S"));
        
        //Ensure the songs are ordered by danceability if needed
        assertTrue(result.indexOf("BO$$") < result.indexOf("Cake By The Ocean") ||
                   result.indexOf("Cake By The Ocean") < result.indexOf("A L I E N S"));
    }

    /**
     * Integration Test: Tests the frontend's `year` command for an invalid year range.
     * Ensures an appropriate error message is displayed when the maximum year is less than the minimum year.
     */
    @Test
    public void invalidYearRangeIntegrationTest() {
        // Verify that an IllegalArgumentException is thrown when the year range is invalid.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            frontend.executeSingleCommand("year 2025 to 2015"); // Invalid year range command
        });

        // Ensure the error message is as expected.
        assertTrue(exception.getMessage().contains("ERROR: <MAX> is less than <MIN>"));
    }

    /**
     * Integration Test: Tests the frontend's `show` command to ensure it displays the correct
     * number of songs when given a maximum limit.
     * Ensures that the output correctly matches the provided limit value and filters are applied properly.
     */
    @Test
    public void showMaxSongsIntegrationTest() {
        // Redirect console output to capture the result.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Apply year filter and display maximum number of songs.
        frontend.executeSingleCommand("year 2010 to 2020");
        frontend.executeSingleCommand("show 5");

        // Restore system output.
        System.setOut(System.out);

        // Extract the captured output.
        String output = outputStream.toString().trim();
        String[] lines = output.split("\n");
        String songListLine = "";
        for (String line : lines) {
            if (line.startsWith("[") && line.endsWith("]")) {
                songListLine = line;
                break;
            }
        }

        // Verify the correct number of songs are displayed.
        String[] displayedSongs = songListLine.substring(1, songListLine.length() - 1).split(", ");
        assertEquals(5, displayedSongs.length);
    }

    /**
     * Integration Test: Tests the frontend's `load` command with an invalid file path to verify
     * that the correct error message is displayed when a non-existent file is specified.
     */
    @Test
    public void loadInvalidFileIntegrationTest() {
        // Verify that a RuntimeException is thrown for an invalid file path.
        Exception exception = assertThrows(RuntimeException.class, () -> {
            frontend.executeSingleCommand("load invalid_file.csv"); // Invalid file path
        });

        // Ensure the error message matches expected content.
        assertTrue(exception.getMessage().contains("ERROR: Backend error (Error: File not found"));
    }

    /**
     * Integration Test: Tests combined frontend commands that apply year range filtering,
     * loudness filtering, and danceability sorting. Ensures correct results
     * are returned when combining multiple conditions.
     * Verifies that relevant songs are correctly included and irrelevant songs are excluded.
     */
    @Test
    public void combinedFilterIntegrationTest() {
        // Apply multiple filters and sort by danceability.
        frontend.executeSingleCommand("year 2015 to 2019");
        frontend.executeSingleCommand("loudness -5");
        frontend.executeSingleCommand("show most danceable");

        // Retrieve filtered results from the backend.
        List<String> result = backend.fiveMost();

        // Ensure correct song inclusion and exclusion.
        assertFalse(result.isEmpty());          // Ensure result is not empty.
        assertTrue(result.contains("Dangerous")); // Expected song should be present.
        assertFalse(result.contains("Loud Song Outside Threshold")); // Excluded song should not appear.
    }

}
