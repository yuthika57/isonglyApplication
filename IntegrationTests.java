import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * Refactored Integration Tests for Frontend and Backend integration without TextUITester.
 */
public class IntegrationTests {
    private static Backend backend;
    private static Frontend frontend;

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
     * Integration Test 1 - Tests the `year` command with an invalid range.
     * Ensures the correct error is thrown for an invalid year range.
     */
    @Test
    public void invalidYearRangeIntegrationTest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            frontend.executeSingleCommand("year 2025 to 2015");
        });

        assertTrue(exception.getMessage().contains("ERROR: <MAX> is less than <MIN>"),
                   "Expected error message for invalid year range.");
    }

    /**
     * Integration Test 2 - Tests `show` command with a high limit.
     * Ensures the displayed songs match the available data.
     */
    @Test
    public void showMaxSongsIntegrationTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        frontend.executeSingleCommand("year 2010 to 2020");
        frontend.executeSingleCommand("show 5");

        System.setOut(System.out);

        // Extract printed output
        String output = outputStream.toString().trim();

        // Capture only the printed song list, ignoring debug statements
        String[] lines = output.split("\n");
        String songListLine = "";
        for (String line : lines) {
            if (line.startsWith("[") && line.endsWith("]")) { // Detects list format
                songListLine = line;
                break;
            }
        }

        // Extract song titles from list
        String[] displayedSongs = songListLine.substring(1, songListLine.length() - 1).split(", ");

        assertEquals(5, displayedSongs.length, "Expected exactly 5 songs in the displayed list.");
    }


    /**
     * Integration Test 3 - Tests the `load` command with an invalid file path.
     * Ensures the frontend throws an error when attempting to load invalid data.
     */
    @Test
    public void loadInvalidFileIntegrationTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            frontend.executeSingleCommand("load invalid_file.csv");
        });

        assertTrue(exception.getMessage().contains("ERROR: Backend error (Error: File not found"),
                   "Expected error message for invalid file path.");
    }

    /**
     * Integration Test 4 - Tests combined commands for complex filtering.
     * Ensures multiple filters work together as expected.
     */
    @Test
    public void combinedFilterIntegrationTest() {
        frontend.executeSingleCommand("year 2015 to 2019");  // Shorter range for precise filtering
        frontend.executeSingleCommand("loudness -5");
        frontend.executeSingleCommand("show most danceable");

        List<String> result = backend.fiveMost();

        assertFalse(result.isEmpty(), "Expected non-empty result for top danceable songs.");
        assertTrue(result.contains("Dangerous"), "Expected song 'Dangerous' should appear.");
        assertFalse(result.contains("Loud Song Outside Threshold"),
                   "Unexpected song exceeding loudness threshold should be excluded.");
    }

}
