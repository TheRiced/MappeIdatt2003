package ntnu.idatt2003.model;

import java.util.List;
import java.util.Scanner;
import ntnu.idatt2003.core.Dice;
import ntnu.idatt2003.file.HandleCSVPlayer;

/**
 * Implements the game logic for Snakes and Ladders.
 */
public class SnakeAndLadderGame implements BoardGame {
  private final Board board;
  private final List<Player> players;
  private final Dice dice;
  private int currentPlayerIndex;
  private Player winner;

  /**
   * Constructs a SnakeAndLadderGame.
   *
   * @param board the game board.
   * @param players the list of players.
   * @param numberOfDice the number of dice to use.
   */
  public SnakeAndLadderGame(Board board, List<Player> players, int numberOfDice) {
    this.board = board;
    this.players = players;
    this.dice = new Dice(numberOfDice);
    this.currentPlayerIndex = 0;
    this.winner = null;
  }

  /**
   * Starts the game loop.
   */
  @Override
  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("\n--- Game started! ---\n");

    while (!gameDone()) {
      Player currentPlayer = players.get(currentPlayerIndex);
      System.out.println(currentPlayer.getName() + "'s turn. Press enter to roll the dice.");
      scanner.nextLine();

      int roll = dice.rollAll();
      System.out.println(currentPlayer.getName() + " rolled a " + roll);

      board.movePlayer(currentPlayer, roll);
      System.out.println(currentPlayer.getName() + " is now on tile " +
          currentPlayer.getCurrentTile().getTileId());

      if (currentPlayer.hasPendingMove()) {
        currentPlayer.setPendingMoveTile(board);
        System.out.println(currentPlayer.getName() + " move to tile " +
            currentPlayer.getCurrentTile().getTileId());
      }

      if (currentPlayer.getCurrentTile().getTileId() == board.size()) {
        winner = currentPlayer;
        System.out.println("And the winner is: " + winner.getName() + "!");

        HandleCSVPlayer.savePlayersToCSV(players, "src/main/resources/players.csv");
        break;
      }

      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    scanner.close();
  }

  @Override
  public boolean gameDone() {
    return winner != null;
  }

  @Override
  public Player getWinner() {
    return winner;
  }

  public Board getBoard() {
    return board;
  }

  public List<Player> getPlayers() {
    return players;
  }


}
