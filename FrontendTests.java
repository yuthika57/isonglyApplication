import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Scanner;


/**
 * Class to test Frontend.java's methods
 */
public class FrontendTests {

  /**
   * Tests the load, help, and quit commands
   */
  @Test
  public void frontendTest1() {
    // creat UI tester with input
    TextUITester tester = new TextUITester("load\nload  \nload     x     \nload x\nhelp  x" +
        "\nhelp\nquit x\nquit", true);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

    // run method to be tested
    frontend.runCommandLoop();

    // actual result output
    String actual = tester.checkOutput();

    // test results contain some of the expected phrases
    assertTrue(actual.contains("""
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        """), "ERROR: Output does not contain command instructions");

    assertTrue(actual.contains("Command: "), "ERROR: Output does not contain command prompt");
    assertTrue(actual.contains("ERROR: Command is not valid"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: No <FILEPATH> specified"),
        "ERROR: Output does not contain correct error message");

    /*
    String expected = """
        
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        
        Command: ERROR: Command is not valid
        Command: ERROR: No <FILEPATH> specified
        Command: Command: Command: ERROR: Command is not valid
        Command:\s
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        
        Command: ERROR: Command is not valid
        Command:\s""";

    assertEquals(expected, tester.checkOutput(), "ERROR: Strings are not equal");
    */

    in.close();
  }

  /**
   * Tests the year command
   */
  @Test
  public void frontendTest2() {
    // create UI tester with input
    TextUITester tester = new TextUITester("year   10\nyear n\nyear  \nyear 10\nyear 10  \n" +
                                           "year x to x\nyear  to 10\nyear  10 to  15  \n" +
                                           "year 1 to 10\nyear 1 to 10  \nquit",true);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

    // run method to be tested
    frontend.runCommandLoop();

    // actual result output
    String actual = tester.checkOutput();

    // test results contain some of the expected phrases
    assertTrue(actual.contains("""
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        """), "ERROR: Output does not contain command instructions");

    assertTrue(actual.contains("Command: "), "ERROR: Output does not contain command prompt");
    assertTrue(actual.contains("ERROR: <MAX> argument is not an integer"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: No <MAX> specified"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: <MIN> or <MAX> argument is not an integer"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: No <MIN> specified"),
        "ERROR: Output does not contain correct error message");

    /*
    String expected = """
        
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        
        Command: ERROR: <MAX> argument is not an integer
        Command: ERROR: <MAX> argument is not an integer
        Command: ERROR: No <MAX> specified
        Command: Command: Command: ERROR: <MIN> or <MAX> argument is not an integer
        Command: ERROR: No <MIN> specified
        Command: ERROR: <MIN> or <MAX> argument is not an integer
        Command: Command: Command:\s""";

    assertEquals(expected, tester.checkOutput(), "ERROR: Strings are not equal");
    */

    in.close();
  }

  /**
   * Tests the loudness command
   */
  @Test
  public void frontendTest3() {
    // create UI tester with input
    TextUITester tester = new TextUITester("loudness   10\nloudness  \nloudness x\n" +
                                           "loudness 10    \nloudness 5\nquit", true);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

    // run method to be tested
    frontend.runCommandLoop();

    // actual result output
    String actual = tester.checkOutput();

    // test results contain some of the expected phrases
    assertTrue(actual.contains("""
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        """), "ERROR: Output does not contain command instructions");

    assertTrue(actual.contains("Command: "), "ERROR: Output does not contain command prompt");
    assertTrue(actual.contains("ERROR: <MAX> argument is not an integer"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: No <MAX> specified"),
        "ERROR: Output does not contain correct error message");

    /*
    String expected = """
        
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        
        Command: ERROR: <MAX> argument is not an integer
        Command: ERROR: No <MAX> specified
        Command: ERROR: <MAX> argument is not an integer
        Command: Command: Command:\s""";

    assertEquals(expected, tester.checkOutput(), "ERROR: Strings are not equal");
    */

    in.close();
  }

  /**
   * Tests the show command
   */
  @Test
  public void frontendTest4() {
    // create UI tester with input
    TextUITester tester = new TextUITester("shw most  dnceable\nshow most danceable\n" +
                                           "show x\nshow  \nload x\nyear -1 to 2\nloudness 10\n" +
                                           "show 1  \nshow 5\nquit",true);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

    // run method to be tested
    frontend.runCommandLoop();

    // actual result output
    String actual = tester.checkOutput();

    // test results contain some of the expected phrases
    assertTrue(actual.contains("""
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        """), "ERROR: Output does not contain command instructions");

    assertTrue(actual.contains("Command: "), "ERROR: Output does not contain command prompt");
    assertTrue(actual.contains("ERROR: Command is not valid"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: No <MAX_COUNT> specified"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("ERROR: <MAX_COUNT> argument is not an integer"),
        "ERROR: Output does not contain correct error message");
    assertTrue(actual.contains("[A L I E N S, BO$$, Cake By The Ocean]"),
        "ERROR: Output does not contain correct song list");
    assertTrue(actual.contains("[A L I E N S, BO$$]"),
        "ERROR: Output does not contain correct song list");

    /*
    String expected = """
        
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
        
        Command: ERROR: Command is not valid
        Command: [A L I E N S, BO$$, Cake By The Ocean]
        Command: ERROR: <MAX_COUNT> argument is not an integer
        Command: ERROR: No <MAX_COUNT> specified
        Command: Command: Command: Command: [A L I E N S]
        Command: [A L I E N S, BO$$]
        Command:\s""";

    assertEquals(expected, tester.checkOutput(), "ERROR: Strings are not equal");
    */

    in.close();
  }

}
