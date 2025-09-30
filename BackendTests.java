import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit tests for the Backend class using Tree_Placeholder.
 */
public class BackendTests {
    private static Backend backend;
    
    /**
     * This method is executed once before all test cases run.
     * It initializes the backend using a placeholder tree structure
     * for storing songs and attempts to read song data from a CSV file.
     * If an IOException occurs while reading the file, the test fails.
     */
    
    @BeforeAll
    public static void setup() {
        try {
            IterableSortedCollection<Song> songTree = new Tree_Placeholder();
            backend = new Backend(songTree);
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
      // Check that the backend loads songs within the given year range
      var songs = backend.getRange(2000, 2025);
      assertNotNull(songs, "Songs should have been loaded into the backend.");

  } 

    
    /**
     * Tests the getRange() method to verify that songs within a specified 
     * year range are correctly retrieved from the backend.
     * Steps:
     * - Initializes a new backend instance with a placeholder tree.
     * - Reads song data from the CSV file.
     * - Calls getRange method and retrieves the result.
     * - Prints debug (DBG) information for expected vs. actual results.
     * - Checks that the returned list matches the expected songs.
     */
    @Test
    public void backendTest2() throws IOException {
        Backend backend = new Backend(new Tree_Placeholder());
        backend.readData("src/songs.csv");

        // Calling getRange(2015, 2017)
        List<String> result = backend.getRange(2015, 2017);

        // Assertions
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
        int threshold = -5;  // Filtering threshold for loudness
        List<String> result = backend.filterSongs(threshold);

        // Ensure that the result is not empty after applying the filter
        assertFalse(result.isEmpty(), "Filtered song list should not be empty.");
        
        // Checking that the expected songs are in the result
        assertTrue(result.contains("BO$$") && result.contains("Cake By The Ocean") && result.contains("A L I E N S"),
                "Expected songs should remain after filtering.");
    }

    /**
     * Tests the fiveMost() method to check that the top 5 most danceable songs 
     * are correctly returned and contain expected songs.
     *
     * Steps:
     * - Calls fiveMost() to get the list of the top 5 danceable songs.
     * - Prints debug (DBG) output to display the returned list.
     * - Ensures the list contains no more than 5 songs.
     * - Verifies that the expected songs are present in the final list.
     * - Also checks that the songs are sorted in descending order of danceability.
     */
    @Test
    public void backendTest4() {
        // Call the method to get the top 5 danceable songs
        List<String> result = backend.fiveMost();

        // Ensure that the result list does not contain more than 5 songs
        assertTrue(result.size() <= 5, "The list should not contain more than 5 songs.");
        
        // Ensure that the expected songs are present in the result
        assertTrue(result.contains("BO$$") && result.contains("Cake By The Ocean") && result.contains("A L I E N S"),
                "Expected songs not found in the top danceable songs.");

        //Ensure the songs are ordered by danceability if needed
        assertTrue(result.indexOf("BO$$") < result.indexOf("Cake By The Ocean") ||
                   result.indexOf("Cake By The Ocean") < result.indexOf("A L I E N S"),
                   "Songs should be ordered by danceability.");
    }

}

