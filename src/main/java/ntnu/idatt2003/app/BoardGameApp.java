package ntnu.idatt2003.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.factory.BoardGameFactory;
import ntnu.idatt2003.file.BoardFileReader;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.SnakeAndLadderGame;
import ntnu.idatt2003.model.Tile;

/**
 * Application to run the Snakes and Ladders board game.
 * Handles user input for setting up the game (players, dice) and starts the game.
 */
public class BoardGameApp {
    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);

        try {
            System.out.println("--- Welcome to Snakes and Ladders! ---");

            BoardFileReader reader = new BoardFileReaderGson();
            Board board = reader.readBoard(Path.of("snakes_and_ladders_90.json"));

            int numberOfPlayers = 0;
            while (numberOfPlayers < 2) {
                System.out.println("Enter the number of players (minimum 2): ");
                try {
                    numberOfPlayers = Integer.parseInt(ss.nextLine());
                    if (numberOfPlayers < 2) {
                        System.out.println("You need at least 2 players to play the game.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }


            int numberOfDice = 0;
            while (numberOfDice != 1 && numberOfDice != 2) {
                System.out.println("Enter number of dice (1 or 2): ");
                try {
                    numberOfDice = Integer.parseInt(ss.nextLine());
                    if (numberOfDice != 1 && numberOfDice != 2) {
                        System.out.println("You can only choose 1 or 2 dice!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number (1 or 2).");
                }
            }

            List<Player> players = new ArrayList<>();

            for (int i = 0; i < numberOfPlayers; i++) {
                System.out.println("\nEnter details for player " + (i + 1) + ":");

                System.out.print("Name: ");
                String name = ss.nextLine();

                int age = 0;
                while (age < 5) {
                    System.out.print("Age: ");
                    try {
                        age = Integer.parseInt(ss.nextLine());
                        if (age < 5) {
                            System.out.println("Age must be more than or equal to 5!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }

                PlayerIcon chosenIcon = null;
                while (chosenIcon == null) {
                    System.out.println("Available icons: ");
                    for (PlayerIcon icon : PlayerIcon.values()) {
                        System.out.println("- " + icon.name());
                    }
                    System.out.println("Choose an icon from the list: ");
                    String iconInput = ss.nextLine().trim().toUpperCase();
                    try {
                        chosenIcon = PlayerIcon.valueOf(iconInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid icon. Please choose again.");
                    }
                }

                Tile startingTile = board.getTile(1);
                Player player = new Player(name, age, chosenIcon, startingTile);
                players.add(player);
            }

            Collections.sort(players);

            BoardGame game = BoardGameFactory.createSnakeAndLadderGame(board, players, numberOfDice);

            game.start();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }
}