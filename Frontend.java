import java.util.List;
import java.util.Scanner;

/**
 * Frontend class to interface with the user
 */
public class Frontend implements FrontendInterface {

  private Scanner in;
  private BackendInterface backend;
  private Integer yearMax;
  private Integer yearMin;

  //public Frontend(Scanner in, BackendInterface backend)
  // Your constructor must have the signature above. This class must rely
  // rely only on the provided Scanner to read input from the user, and must
  // use the provided BackendInterface reference to compute the results
  // of an command requested by the user.
  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }

  /**
   * Displays instructions for the syntax of user commands.  And then repeatedly gives the user an
   * opportunity to issue new commands until they enter "quit".  Uses the evaluateSingleCommand
   * method below to parse and run each command entered by the user.  If the backend ever throws any
   * exceptions, they should be caught here and reported to the user.  The user should then continue
   * to be able to issue subsequent commands until they enter "quit".  This method must use the
   * scanner passed into the constructor to read commands input by the user.
   */
  public void runCommandLoop() {
    // show details of how to issue commands
    displayCommandInstructions();
    String command;

    // always allow user to issue commands unless they quit
    while (true) {
      // process command
      System.out.print("Command: ");
      if (in.hasNextLine())
        command = in.nextLine();
      else
        continue;

      // quit command (strip trailing removes any white space that follows the string)
      if (command.isBlank()) continue;
      else if (command.stripTrailing().equals("quit")) break;

      // attempt to execute the command and display error if needed
      try {
        executeSingleCommand(command);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  /**
   * Displays instructions for the user to understand the syntax of commands that they are able to
   * enter.  This should be displayed once from the command loop, before the first user command is
   * read in, and then later in response to the user entering the command: help.
   * <p>
   * The lowercase words in the following examples are keywords that the user must match exactly in
   * their commands, while the upper case words are placeholders for arguments that the user can
   * specify.  The following are examples of valid command syntax that your frontend should be able
   * to handle correctly.
   * <p>
   * load FILEPATH
   * year MAX
   * year MIN to MAX
   * loudness MAX
   * show MAX_COUNT
   * show most danceable
   * help
   * quit
   */
  public void displayCommandInstructions() {
    System.out.print("""
        
        VALID COMMANDS:
        
        load <FILEPATH> (load data from specified filepath)
        year <MAX> (update the most recent song year threshold)
        year <MIN> to <MAX> (update the range of song years threshold)
        loudness <MAX> (update the max loudness threshold)
        show <MAX_COUNT> (display specified number of songs w/ current thresholds)
        show most danceable (display most danceable songs w/ current thresholds)
        help (display valid commands)
        quit (end program)
       
        """);
  }

  /**
   * This method takes a command entered by the user as input. It parses that command to determine
   * what kind of command it is, and then makes use of the backend (which was passed to the
   * constructor) to update the state of that backend.  When a show or help command are issued, this
   * method prints the appropriate results to standard out.  When a command does not follow the
   * syntax rules described above, this method should print out an error message that describes at
   * least one defect in the syntax of the provided command argument.
   * <p>
   * Some notes on the expected behavior of the different commands:
   *  load: results in backend loading data from specified path
   *  year: updates backend's range of songs to return, should not result in any songs being
   *        displayed
   *  loudness: updates backend's filter threshold, should not result in any songs being displayed
   *  show: displays list of songs with currently set thresholds
   *        MAX_COUNT: argument limits the number of song titles displayed to the first MAX_COUNT
   *        in the list returned from backend
   *  most danceable: argument displays results returned from the backend's fiveMost method
   *  help: displays command instructions
   *  quit: ends this program (handled by runCommandLoop method above) (do NOT use System.exit(),
   *        as this will interfere with tests)
   *
   * @param command command to be executed
   * @throws NumberFormatException if cannot parse the integer value
   * @throws IllegalArgumentException if argument doesn't exist
   * @throws RuntimeException if backend error or command is generally invalid
   */
  public void executeSingleCommand(String command) {

    // help command
    if (command.stripTrailing().equals("help")) displayCommandInstructions();

    // show most danceable command
    else if (command.stripTrailing().equals("show most danceable")) System.out.println(backend.fiveMost());

    else if (command.startsWith("show ")) {
      try {
        if (command.stripTrailing().length() < 6)
          throw new IllegalArgumentException("ERROR: No <MAX_COUNT> specified");
        // parse argument
        int maxCount = Integer.parseInt(command.substring(5).stripTrailing());

        // display maxCount number of songs
        List<String> songList = backend.getRange(yearMin, yearMax);
        if (songList.size() < maxCount) System.out.println(songList);
        else System.out.println(songList.subList(0, maxCount));
      } catch (NumberFormatException e) {
        throw new NumberFormatException("ERROR: <MAX_COUNT> argument is not an integer");
      } catch (IllegalArgumentException e ) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("ERROR: Backend error (" + e.getMessage() + ")");
      }
    }




    // loudness <MAX> command with some error handling
    else if (command.startsWith("loudness ")) {
      try {
        if (command.stripTrailing().length() < 10)
          throw new IllegalArgumentException("ERROR: No <MAX> specified");
        // parse argument
        Integer maxLoudness = Integer.parseInt(command.substring(9).stripTrailing());

        // update backend status
        backend.filterSongs(maxLoudness);
      } catch (NumberFormatException e) {
        throw new NumberFormatException("ERROR: <MAX> argument is not an integer");
      } catch (IllegalArgumentException e ) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("ERROR: Backend error (" + e.getMessage() + ")");
      }
    }

    // year <MIN> to <MAX> command with some error handling
    else if (command.startsWith("year ") && command.contains(" to ")) {
      try {
        // local variable so command.indexOf only has to be called once
        int indexOfTo = command.indexOf(" to ");

        if (indexOfTo < 6) throw new IllegalArgumentException("ERROR: No <MIN> specified");
        // parse arguments
        yearMin = Integer.parseInt(command.substring(5, indexOfTo));
        yearMax = Integer.parseInt(command.substring(indexOfTo + 4).stripTrailing());

        // make sure max is more than min
        if (yearMax < yearMin) throw new IllegalArgumentException("ERROR: <MAX> is less than <MIN>");

        // update backend status
        backend.getRange(yearMin, yearMax);
      } catch (NumberFormatException e) {
        throw new NumberFormatException("ERROR: <MIN> or <MAX> argument is not an integer");
      } catch (IllegalArgumentException e ) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("ERROR: Backend error (" + e.getMessage() + ")");
      }
    }

    // year <MAX> command with some error handling
    else if (command.startsWith("year ")) {
      try {
        if (command.stripTrailing().length() < 6)
          throw new IllegalArgumentException("ERROR: No <MAX> specified");
        // parse argument
        yearMax = Integer.parseInt(command.substring(5).stripTrailing());

        // update backend status
        backend.getRange(yearMin, yearMax);
      } catch (NumberFormatException e) {
        throw new NumberFormatException("ERROR: <MAX> argument is not an integer");
      } catch (IllegalArgumentException e ) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("ERROR: Backend error (" + e.getMessage() + ")");
      }
    }

    // load <FILEPATH> command with some error handling
    else if (command.startsWith("load ")) {
      try {
        if (command.stripTrailing().length() < 6)
          throw new IllegalArgumentException("ERROR: No <FILEPATH> specified");
        // parse argument
        String fileName = command.substring(5).strip();

        // load data in backend
        backend.readData(fileName);
      } catch (IllegalArgumentException e ) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("ERROR: Backend error (" + e.getMessage() + ")");
      }
    }

    // generally invalid command
    else {
      throw new IllegalArgumentException("ERROR: Command is not valid");
    }

  }

}
