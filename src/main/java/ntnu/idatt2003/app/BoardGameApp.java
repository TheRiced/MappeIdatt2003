package ntnu.idatt2003.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.file.BoardLoader;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.SnakeAndLadderGame;

/**
 * Application to run the Snakes and Ladders board game.
 * Handles user input for setting up the game (players, dice) and starts the game.
 */
public class BoardGameApp {
    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);

        try {
            Board board = BoardLoader.loadBoardFromJson("src/main/resources/snakes_and_ladders_90.json");

            System.out.println("Welcome to Snakes and Ladders!");
            System.out.println("Enter the number of players: ");
            int numberOfPlayers = Integer.parseInt(ss.nextLine());

            if (numberOfPlayers < 2) {
                System.out.println("You need at least 2 players to play the game.");
                return;
            }

            System.out.println("Enter number of dice (1 or 2): ");
            int numberOfDice = Integer.parseInt(ss.nextLine());

            List<Player> players = new ArrayList<>();
            for (int i = 0; i < numberOfPlayers; i++) {
                System.out.println("Enter the name of player " + (i + 1) + ": ");
                String name = ss.nextLine();
                System.out.println("Enter the age of player " + (i + 1) + ": ");
                int age = Integer.parseInt(ss.nextLine());

                System.out.println("Available icons: ");
                for (PlayerIcon icon : PlayerIcon.values()) {
                    System.out.println("- " + icon.name());
                }

                PlayerIcon selectedIcon = null;
                while (selectedIcon == null) {
                    System.out.println("Choose an icon from the list: ");
                    String iconChoice = ss.nextLine().trim().toUpperCase();
                    try {
                        selectedIcon = PlayerIcon.valueOf(iconChoice);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid icon. Please choose again.");
                    }
                }

                Player player = new Player(name, age, selectedIcon, board.getTile(1));
                players.add(player);
            }

            Collections.sort(players);

            SnakeAndLadderGame game = new SnakeAndLadderGame(board, players, numberOfDice);
            game.start();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        ss.close();
    }
}
