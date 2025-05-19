package ntnu.idatt2003.view;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.ludo.TokenColor;

public class LudoSetupPage extends BorderPane {
  private final Spinner<Integer> playerCountSpinner;
  private final GridPane playersGrid;
  private final Button startButton;

  public LudoSetupPage() {
    Text title = new Text("Setup Ludo Players");
    title.getStyleClass().add("page-title");

    Label lblCount = new Label("Number of players:");
    playerCountSpinner = new Spinner<>(2, 4, 2);
    HBox countBox = new HBox(10, lblCount, playerCountSpinner);
    countBox.setAlignment(Pos.CENTER);

    playersGrid = new GridPane();
    playersGrid.getColumnConstraints().addAll(
        new ColumnConstraints(100),
        new ColumnConstraints(150),
        new ColumnConstraints(80),
        new ColumnConstraints(120),
        new ColumnConstraints(120)
    );
    playersGrid.setVgap(10);
    playersGrid.setHgap(20);
    playersGrid.setAlignment(Pos.CENTER);

    startButton = new Button("Start Game");
    startButton.getStyleClass().add("btn-primary");

    VBox centerBox = new VBox(20, title, countBox, playersGrid, startButton);
    centerBox.setAlignment(Pos.CENTER);
    centerBox.getStyleClass().add("page-container");

    this.setCenter(centerBox);

    playerCountSpinner.valueProperty().addListener((obs, oldV, newV) -> rebuildPlayerRows(newV));
    rebuildPlayerRows(playerCountSpinner.getValue());
  }

  private void rebuildPlayerRows(Integer count) {
    playersGrid.getChildren().clear();
    for (int i = 0; i < count; i++) {
      TextField nameField = new TextField();
      nameField.setPromptText("Name");

      TextField ageField = new TextField();
      ageField.setPromptText("Age");

      ComboBox<PlayerIcon> iconChoice = new ComboBox<>(
          FXCollections.observableArrayList(PlayerIcon.values())
      );
      iconChoice.setPromptText("Select icon");

      ComboBox<TokenColor> colorChoice = new ComboBox<>(
          FXCollections.observableArrayList(TokenColor.values())
      );
      colorChoice.setPromptText("Select color");

      playersGrid.addRow(i,
          new Label("Player " + (i + 1) + ":"),
          nameField, ageField, iconChoice, colorChoice);
    }
  }

  public int getPlayerCount() {
    return playerCountSpinner.getValue();
  }

  public GridPane getPlayersGrid() {
    return playersGrid;
  }

  public Button getStartButton() {
    return startButton;
  }
}
