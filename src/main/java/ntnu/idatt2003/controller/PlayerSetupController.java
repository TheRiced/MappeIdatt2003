package ntnu.idatt2003.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.Tile;
import ntnu.idatt2003.view.BoardView;
import ntnu.idatt2003.view.HomePage;
import ntnu.idatt2003.view.PlayerFormData;
import ntnu.idatt2003.view.PlayerSetupPage;

public class PlayerSetupController {
  private final Stage stage;
  private final PlayerSetupPage view;

  public PlayerSetupController(Stage stage, HomePage view) {
    this.stage = stage;
    this.view = new PlayerSetupPage();

    this.view.getGenerateButton().setOnAction(e -> this.view.createFields());
    this.view.getStartButton().setOnAction(e -> onStartGame());
  }

  public void showSetup() {
    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Setup Players");
    stage.show();
  }

  private void onStartGame() {
    int diceCount = view.getDiceCount();
    List<PlayerFormData> forms = view.collectPlayers();

    try {

      Board board = new BoardFileReaderGson().readBoard(Path.of("snakes_and_ladders_90.json"));
      Tile startTile = board.getTile(1);
      List<Player> players = forms.stream().map(f -> new Player(f.name(), f.age(), f.icon(),
          startTile)).toList();

      BoardView boardView = new BoardView(board, players);

      GameController gameCtrl = new GameController(stage, boardView, players, diceCount);

      gameCtrl.start();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
