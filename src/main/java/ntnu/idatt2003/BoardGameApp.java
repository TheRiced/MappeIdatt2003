package ntnu.idatt2003;

import java.util.ArrayList;
import java.util.Scanner;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.core.Die;

public class BoardGameApp {
    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);
        Board board = new Board();
        BoardGame game = new BoardGame(board, new ArrayList<>(), 2);

        System.out.println("Welcome to the board game!");
        System.out.println("Enter the number of players: ");
        int numberOfPlayers = ss.nextInt();
        ss.nextLine(); // ← consume newline

        if (numberOfPlayers < 2) {
            System.out.println("You need at least 2 players to play the game.");
            return;
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter the name of player " + (i + 1) + ": ");
            String name = ss.nextLine();
            System.out.println("Enter the age of player " + (i + 1) + ": ");
            int age = Integer.parseInt(ss.nextLine());
            game.addPlayer(name, age, game);
        }

        System.out.println("The game is ready to start!");
        System.out.println("\nPlayers in game:");
        for (Player player : game.getPlayers()) {
            System.out.println(player.getName() + " (" + player.getAge() + ")");
        }

        int round = 1;
        Die die = new Die();
        while (!game.gameDone()) {
            System.out.println("Round " + round);
            for (Player player : game.getPlayers()) {
                System.out.println(player.getName() + "'s turn");
                System.out.println("Press enter to roll the dice");
                ss.nextLine();
                int roll = die.roll();
                System.out.println(player.getName() + " rolled " + roll);
                player.move(roll);
            }

            // Save players after each round
            HandleCSVPlayer.savePlayersToCSV(game.getPlayers(), "src/main/resources/players.csv");

            round++;
        }

        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("And the winner is: " + winner.getName() + "!");
        } else {
            System.out.println("No winner found.");
        }

        ss.close();
    }
}
