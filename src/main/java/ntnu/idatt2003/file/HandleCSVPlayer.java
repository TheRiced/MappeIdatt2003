package ntnu.idatt2003.file;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ntnu.idatt2003.model.Player;

public class HandleCSVPlayer {

  public static void savePlayersToCSV(List<Player> players, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      for (Player player : players) {
        writer.write(player.getName() + "," + player.getAge() + "\n");
      }
      System.out.println("Players saved to " + filename);
    } catch (IOException e) {
      System.err.println("Failed to save players: " + e.getMessage());
    }
  }
}
