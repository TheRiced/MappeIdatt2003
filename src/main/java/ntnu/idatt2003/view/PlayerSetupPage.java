package ntnu.idatt2003.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.view.PlayerFormData;

/**
 * PlayerSetupPage lets users pick number of players, dice count, and enter
 * player names, ages, and icons (via PNG images).
 */
public class PlayerSetupPage extends BorderPane {

  private final Spinner<Integer> playerCountSpinner = new Spinner<>(2, 5, 2);
  private final ToggleGroup diceGroup = new ToggleGroup();
  private List<TextField> names;
  private List<TextField> ages;
  private List<ComboBox<PlayerIcon>> icons;
  private final Button generate = new Button("Generate Fields");
  private final Button start    = new Button("Start Game");
  private final VBox fieldsBox  = new VBox(15);

  public PlayerSetupPage() {
    // Title
    Text title = new Text("Player Setup");
    title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));

    // Top controls: player count + dice selection
    HBox topControls = new HBox(20,
        new Label("Players:"), playerCountSpinner,
        new Label("Dice:"), createDiceRadio("1 Die", true), createDiceRadio("2 Dice", false)
    );
    topControls.setAlignment(Pos.CENTER);

    // Style buttons
    generate.setFont(Font.font(14));
    generate.setStyle(
        "-fx-background-color: #59753c;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;"
    );
    start.setFont(Font.font(14));
    start.setStyle(
        "-fx-background-color: #89b391;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;"
    );
    start.setDisable(true);  // only enabled after fields are generated

    // Wire the Generate button to build fields
    generate.setOnAction(e -> createFields());

    // Only Generate button shown initially
    FlowPane actionPane = new FlowPane(15, 15, generate);
    actionPane.setAlignment(Pos.CENTER);

    // Center layout
    VBox centerBox = new VBox(25, title, topControls, fieldsBox, actionPane);
    centerBox.setAlignment(Pos.CENTER);
    centerBox.setPadding(new Insets(30));
    centerBox.setStyle(
        "-fx-background-color: #faf0e3;" +
            "-fx-border-color: #aeca7e;" +
            "-fx-border-radius: 15;"
    );

    setCenter(centerBox);
  }

  private RadioButton createDiceRadio(String text, boolean selected) {
    RadioButton rb = new RadioButton(text);
    rb.setToggleGroup(diceGroup);
    rb.setSelected(selected);
    return rb;
  }

  public Button getGenerateButton() { return generate; }
  public Button getStartButton()    { return start;    }
  public int getPlayerCount()       { return playerCountSpinner.getValue(); }
  public int getDiceCount() {
    RadioButton sel = (RadioButton) diceGroup.getSelectedToggle();
    return sel.getText().contains("1") ? 1 : 2;
  }

  /**
   * Dynamically generates name/age/icon fields for each player.
   */
  public void createFields() {
    fieldsBox.getChildren().clear();
    names = new ArrayList<>();
    ages  = new ArrayList<>();
    icons = new ArrayList<>();

    for (int i = 0; i < getPlayerCount(); i++) {
      TextField name = new TextField();
      name.setPromptText("Name");
      TextField age  = new TextField();
      age.setPromptText("Age");

      ComboBox<PlayerIcon> iconCb = new ComboBox<>();
      iconCb.getItems().addAll(PlayerIcon.values());
      iconCb.setPromptText("Select Icon");

      // Render each PNG in the dropdown
      iconCb.setCellFactory(cb -> new ListCell<>() {
        private final ImageView iv = new ImageView();
        @Override
        protected void updateItem(PlayerIcon item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setGraphic(null);
            setText(null);
          } else {
            iv.setImage(item.getImage());
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            setGraphic(iv);
            setText(item.toString());
          }
        }
      });
      // Show selected icon in the combo button
      iconCb.setButtonCell(iconCb.getCellFactory().call(null));

      names.add(name);
      ages.add(age);
      icons.add(iconCb);

      Label playerLabel = new Label("Player " + (i + 1) + ":");
      playerLabel.setFont(Font.font(16));
      playerLabel.setContentDisplay(ContentDisplay.LEFT);

      HBox line = new HBox(10, playerLabel, name, age, iconCb);
      line.setAlignment(Pos.CENTER_LEFT);
      fieldsBox.getChildren().add(line);
    }

    // Add and enable the Start button once fields exist
    fieldsBox.getChildren().add(start);
    start.setDisable(false);
  }

  /**
   * Collects form data into a list of PlayerFormData for controller use.
   */
  public List<PlayerFormData> collectPlayers() {
    List<PlayerFormData> data = new ArrayList<>();
    for (int i = 0; i < names.size(); i++) {
      data.add(new PlayerFormData(
          names.get(i).getText(),
          Integer.parseInt(ages.get(i).getText()),
          icons.get(i).getValue()
      ));
    }
    return data;
  }
}
