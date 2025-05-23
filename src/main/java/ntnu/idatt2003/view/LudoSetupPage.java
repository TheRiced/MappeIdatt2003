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
import ntnu.idatt2003.model.ludo.LudoTile;
import ntnu.idatt2003.model.ludo.TokenColor;

/**
 * A JavaFX page for setting up a new Ludo game.
 *
 * <p>Allows the user to select the number of players, enter each player's
 * name and age, choose a player icon and token color, and then start the game.
 * </p>
 */
public class LudoSetupPage extends VBox {

  private final GridPane playerGrid = new GridPane();

  private final List<TextField> nameFields = new ArrayList<>();
  private final List<TextField> ageFields = new ArrayList<>();
  private final List<ChoiceBox<PlayerIcon>> iconFields = new ArrayList<>();
  private final List<ChoiceBox<TokenColor>> colorFields = new ArrayList<>();
  private final ComboBox<Integer> numPlayersCb = new ComboBox<>();
  private final Button startButton = new Button("🎲 Let's Play!");

  /**
   * Constructs the setup page.
   *
   * @param board   the LudoBoard model used to obtain home tiles for each player
   * @param onStart callback to invoke with the list of created {@link LudoPlayer} when the user has
   *                completed setup and clicks the start button
   */
  public LudoSetupPage(LudoBoard board, Consumer<List<LudoPlayer>> onStart) {
    setSpacing(25);
    setPadding(new Insets(40));
    setAlignment(Pos.TOP_CENTER);
    setStyle("-fx-background-color: #fff8f0;");

    Text title = new Text("Ludo: Player Setup");
    title.setFont(Font.font("Comic Sans MS", 26));

    HBox numBox = new HBox(10, new Label("👥 How many players?"), numPlayersCb);
    numBox.setAlignment(Pos.CENTER);
    numPlayersCb.setItems(FXCollections.observableArrayList(2, 3, 4));
    numPlayersCb.setValue(2);

    numPlayersCb.setOnAction(e -> rebuildPlayerRows());

    playerGrid.setHgap(15);
    playerGrid.setVgap(12);
    playerGrid.setAlignment(Pos.CENTER);
    rebuildPlayerRows();

    startButton.setStyle("-fx-font-size: 16px; -fx-background-radius: 10px; -fx-padding: 10 20;");
    startButton.setOnAction(e -> {
      List<LudoPlayer> players = new ArrayList<>();
      for (int i = 0; i < nameFields.size(); i++) {
        String nameText = nameFields.get(i).getText().trim();
        String ageText = ageFields.get(i).getText().trim();
        PlayerIcon icon = iconFields.get(i).getValue();
        TokenColor color = colorFields.get(i).getValue();

        if (nameText.isEmpty()) {
          showAlert(" Player " + (i + 1) + " needs a name!");
          return;
        }

        int age;
        try {
          age = Integer.parseInt(ageText);
          if (age < 0) {
            throw new NumberFormatException();
          }
        } catch (NumberFormatException ex) {
          showAlert(" Please enter a valid non-negative age for player " + (i + 1));
          return;
        }

        if (icon == null) {
          showAlert(" Player " + (i + 1) + " needs a fun icon!");
          return;
        }

        if (color == null) {
          showAlert(" Player " + (i + 1) + " must pick a color!");
          return;
        }

        List<LudoTile> homeTiles = board.getHome(color);
        players.add(new LudoPlayer(nameText, age, icon, color, homeTiles));
      }

      onStart.accept(players);
    });

    getChildren().addAll(title, numBox, playerGrid, startButton);
  }

  /**
   * Rebuilds the player-input rows whenever the selected number of players changes.
   *
   * <p>Clears existing rows and fields, then adds one row per player containing:
   * <ul>
   *   <li>Label ("P1:", "P2:", ...)</li>
   *   <li>TextField for name (default "PlayerX")</li>
   *   <li>TextField for age (default "22")</li>
   *   <li>ChoiceBox for {@link PlayerIcon} values</li>
   *   <li>ChoiceBox for {@link TokenColor} values</li>
   * </ul>
   * </p>
   */
  private void rebuildPlayerRows() {
    playerGrid.getChildren().clear();
    nameFields.clear();
    ageFields.clear();
    iconFields.clear();
    colorFields.clear();

    playerGrid.add(new Label("🎮"), 0, 0);
    playerGrid.add(new Label("Name"), 1, 0);
    playerGrid.add(new Label("Age"), 2, 0);
    playerGrid.add(new Label("Icon"), 3, 0);
    playerGrid.add(new Label("Color"), 4, 0);

    int count = numPlayersCb.getValue();
    for (int i = 0; i < count; i++) {
      int row = i + 1;
      playerGrid.add(new Label("P" + (i + 1) + ":"), 0, row);

      TextField nameTf = new TextField("Player" + (i + 1));
      nameFields.add(nameTf);
      playerGrid.add(nameTf, 1, row);

      TextField ageTf = new TextField("22");
      ageFields.add(ageTf);
      playerGrid.add(ageTf, 2, row);

      ChoiceBox<PlayerIcon> iconCb =
          new ChoiceBox<>(FXCollections.observableArrayList(PlayerIcon.values()));
      iconCb.getSelectionModel().select(i % PlayerIcon.values().length);
      iconFields.add(iconCb);
      playerGrid.add(iconCb, 3, row);

      ChoiceBox<TokenColor> colorCb =
          new ChoiceBox<>(FXCollections.observableArrayList(TokenColor.values()));
      colorCb.getSelectionModel().select(i);
      colorFields.add(colorCb);
      playerGrid.add(colorCb, 4, row);
    }
  }

  /**
   * Displays a warning alert with the specified message.
   *
   * @param msg the message to display in the alert
   */
  private void showAlert(String msg) {
    Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
    a.setHeaderText("Oops!");
    a.showAndWait();
  }

  /**
   * Returns the button that triggers the start of the game.
   *
   * @return the {@link Button} labeled "Let's Play!"
   */
  public Button getStartButton() {
    return startButton;
  }

  /**
   * Gets the currently selected number of players.
   *
   * @return the number of players chosen in the combo box
   */
  public int getPlayerCount() {
    return numPlayersCb.getValue();
  }

  /**
   * Gets the list of name input fields.
   *
   * @return a list of {@link TextField} for entering player names
   */
  public List<TextField> getNameFields() {
    return nameFields;
  }

  /**
   * Gets the list of age input fields.
   *
   * @return a list of {@link TextField} for entering player ages
   */
  public List<TextField> getAgeFields() {
    return ageFields;
  }

  /**
   * Gets the list of icon selection boxes.
   *
   * @return a list of {@link ChoiceBox} for selecting {@link PlayerIcon}s
   */
  public List<ChoiceBox<PlayerIcon>> getIconFields() {
    return iconFields;
  }

  /**
   * Gets the list of color selection boxes.
   *
   * @return a list of {@link ChoiceBox} for selecting {@link TokenColor}s
   */
  public List<ChoiceBox<TokenColor>> getColorFields() {
    return colorFields;
  }
}
