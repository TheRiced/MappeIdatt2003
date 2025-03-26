package ntnu.idatt2003.model;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BoardGame {

    
    private Board board;
    private List<Player> players;
    private Die die;
    private int currentPlayerIndex;


    public BoardGame(Board board, Die die, List<Player> players) {
        this.board = board;
        this.die = die;
        this.players = players;
        createBoard();
    }

    public void addPlayer(String name, int age) {
        players.add(new Player(name, age));
        Collections.sort(players);
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
}