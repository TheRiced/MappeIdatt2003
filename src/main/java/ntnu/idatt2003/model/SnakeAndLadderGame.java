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

  private void playTurn(Scanner scanner) {
    Player currentPlayer = players.get(currentPlayerIndex);
    boolean repeat;

    do {
      repeat = false;

      System.out.println(currentPlayer.getName() + "'s turn. Press enter to roll the dice.");
      scanner.nextLine();

      List<Integer> individualRolls = dice.rollEach();
      int roll = individualRolls.stream().mapToInt(Integer::intValue).sum();

      if (dice.numberOfDice() == 1) {
        System.out.println(currentPlayer.getName() + " rolled a " + roll);
      } else {
        System.out.println(currentPlayer.getName() + " rolled " + individualRolls +
            " (Total: " + roll + ")");
      }

      String result = board.movePlayer(currentPlayer, roll);
      System.out.print(result);

      if (currentPlayer.hasPendingMove()) {
        currentPlayer.setPendingMoveTile(board);
        System.out.println(currentPlayer.getName() + " moved to tile " +
            currentPlayer.getCurrentTile().getTileId());
      }

      if (currentPlayer.getCurrentTile().getTileId() == board.size()) {
        winner = currentPlayer;
        System.out.println("And the winner is: " + winner.getName() + "!");
        HandleCSVPlayer.savePlayersToCSV(players, "src/main/resources/players.csv");
        return;
      }

      if (dice.numberOfDice() == 1 && individualRolls.get(0) == 1) {
        System.out.println("You rolled a 1! Roll again!");
        repeat = true;
      } else if (dice.numberOfDice() == 2 && individualRolls.get(0) == 6 &&
          individualRolls.get(1) == 6) {
        System.out.println("You rolled double sixes! Roll again!");
        repeat = true;
      }

      if (currentPlayer.hasExtraTurn()) {
        currentPlayer.clearExtraTurn();
        System.out.println("Bonus! " + currentPlayer.getName() + " gets to roll again!");
        repeat = true;
      }
    } while (repeat);

    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  /**
   * Starts the game loop.
   */
  @Override
  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("\n--- Game started! ---\n");

    while (!gameDone()) {
      playTurn(scanner);
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
