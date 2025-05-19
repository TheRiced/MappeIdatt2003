package ntnu.idatt2003.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.ludo.TokenColor;
import ntnu.idatt2003.model.ludo.LudoTile;

/**
 *
 */

public class LudoSetupPage extends VBox {
  private final GridPane playerGrid = new GridPane();

  private final List<TextField>            nameFields  = new ArrayList<>();
  private final List<TextField>            ageFields   = new ArrayList<>();
  private final List<ChoiceBox<PlayerIcon>> iconFields  = new ArrayList<>();
  private final List<ChoiceBox<TokenColor>> colorFields = new ArrayList<>();


  /**
   * Constructs a new {@code LudoSetupPage} with fields for configuring Ludo players.
   *
   * @param board the {@link LudoBoard} used to retrieve player home tiles based on color
   * @param onStart a callback that will be executed when all input is valid, providing a
   * list of fully initialized {@link LudoPlayer} objects
   */
  public LudoSetupPage(LudoBoard board, Consumer<List<LudoPlayer>> onStart) {
    setSpacing(25);
    setPadding(new Insets(40));
    setAlignment(Pos.TOP_CENTER);
    setStyle("-fx-background-color: #fff8f0;");

    Text title = new Text("Ludo Adventure: Player Setup");
    title.setFont(Font.font("Comic Sans MS", 26));

    ComboBox<Integer> numPlayersCb = new ComboBox<>();
    HBox numBox = new HBox(10, new Label("👥 How many players?"), numPlayersCb);
    numBox.setAlignment(Pos.CENTER);
    numPlayersCb.setItems(FXCollections.observableArrayList(2, 3, 4));
    numPlayersCb.setValue(2);

    playerGrid.setHgap(15);
    playerGrid.setVgap(12);
    playerGrid.setAlignment(Pos.CENTER);
    rebuildPlayerRows();


    Button startButton = new Button("🎲 Let's Play!");
    startButton.setStyle("-fx-font-size: 16px; -fx-background-radius: 10px; -fx-padding: 10 20;");
    startButton.setOnAction(e -> {
      List<LudoPlayer> players = new ArrayList<>();
      for (int i = 0; i < nameFields.size(); i++) {
        String nameText = nameFields.get(i).getText().trim();
        String ageText  = ageFields.get(i).getText().trim();
        PlayerIcon icon  = iconFields.get(i).getValue();
        TokenColor color = colorFields.get(i).getValue();

        if (nameText.isEmpty()) {
          showAlert("📝 Player " + (i+1) + " needs a name!"); return;
        }
        int age;
        try {
          age = Integer.parseInt(ageText);
          if (age < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
          showAlert("🎂 Please enter a valid non-negative age for player " + (i+1)); return;
        }
        if (icon == null) {
          showAlert("🎨 Player " + (i+1) + " needs a fun icon!"); return;
        }
        if (color == null) {
          showAlert("🌈 Player " + (i+1) + " must pick a color!"); return;
        }

        List<LudoTile> homeTiles = board.getHome(color);
        players.add(new LudoPlayer(nameText, age, icon, color, homeTiles));
      }
      onStart.accept(players);
    });

    getChildren().addAll(title, numBox, playerGrid, startButton);
  }

  private void rebuildPlayerRows() {
    playerGrid.getChildren().clear();
    nameFields.clear();
    ageFields.clear();
    iconFields.clear();
    colorFields.clear();

    playerGrid.add(new Label("🎮"), 0, 0);
    playerGrid.add(new Label("Name"),     1, 0);
    playerGrid.add(new Label("Age"),      2, 0);
    playerGrid.add(new Label("Icon"),     3, 0);
    playerGrid.add(new Label("Color"),    4, 0);

    for (int i = 0; i < 2; i++) {
      int row = i + 1;
      playerGrid.add(new Label("P" + (i+1) + ":"), 0, row);

      TextField nameTf = new TextField("Player" + (i+1));
      nameFields.add(nameTf);
      playerGrid.add(nameTf, 1, row);

      TextField ageTf = new TextField("10");
      ageFields.add(ageTf);
      playerGrid.add(ageTf, 2, row);

      ChoiceBox<PlayerIcon> iconCb = new ChoiceBox<>(
          FXCollections.observableArrayList(PlayerIcon.values()));
      iconCb.getSelectionModel().select(i % PlayerIcon.values().length);
      iconFields.add(iconCb);
      playerGrid.add(iconCb, 3, row);

      ChoiceBox<TokenColor> colorCb = new ChoiceBox<>(
          FXCollections.observableArrayList(TokenColor.values()));
      colorCb.getSelectionModel().select(i);
      colorFields.add(colorCb);
      playerGrid.add(colorCb, 4, row);
    }
  }

  private void showAlert(String msg) {
    Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
    a.setHeaderText("Oops!");
    a.showAndWait();
  }
}
