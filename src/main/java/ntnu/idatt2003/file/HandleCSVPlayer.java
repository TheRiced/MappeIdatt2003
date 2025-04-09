package ntnu.idatt2003.file;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ntnu.idatt2003.model.Player;

/**
 * Handle saving player data to a CSV file.
 */
public class HandleCSVPlayer {

  /**
   * Saves players to a CSV file
   * @param players the list of players to save
   * @param filename the name of the CSV file to write to
   */

  public static void savePlayersToCSV(List<Player> players, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write("Name,Age,Icon\n");
      for (Player player : players) {
        writer.write(player.getName() + "," + player.getAge() + player.getIcon() + "\n");
      }
    } catch (IOException e) {
      System.err.println("Failed to save players: " + e.getMessage());
    }
  }
}
