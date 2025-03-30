package ntnu.idatt2003.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import ntnu.idatt2003.model.Dice;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.Board;
import java.util.Scanner;

public class BoardGame {

    
    private Board board;
    private List<Player> players;
    private Die die;
    private Dice dice;
    private int currentPlayerIndex;
    private Player winner;


    public BoardGame(Board board, List<Player> players, int numberOfDice) {
        this.board = board;
        this.dice = new Dice(numberOfDice);
        this.currentPlayerIndex = 0;
        this.players = new ArrayList<>();
        this.winner = null;
        createBoard();
    }

    public void addPlayer(String name, int age, BoardGame game) {
        Player player = new Player(name, age, game);
        player.placeOnTile(board.getTile(0));
        players.add(player);
        Collections.sort(players);
    }

    public List<Player> getPlayers() {
        return players;
    }


    public void createBoard() {
        for (int i = 0; i < 90; i++) {
            Tile tile = new Tile(i);
            board.addTile(tile);
        }
    }

    public int getBoardSize() {
        return board.size();
    }

    public boolean gameDone(){
        return winner != null;
    }

    public Player getWinner() {
        return winner;
    }
}