package ntnu.idatt2003.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ntnu.idatt2003.core.PlayerIcon;

/**
 * PlayerSetupPage allows users to configure the number of players, dice count, and enter player
 * information (name, age, and icon) before starting a game.
 *
 * <p>Fields are dynamically generated and validated. The start button
 * is enabled only when all entries are valid.
 * </p>
 */
public class PlayerSetupPage extends BorderPane {

  private final Spinner<Integer> playerCountSpinner = new Spinner<>(2, 5, 2);
  private final ToggleGroup diceGroup = new ToggleGroup();
  private List<TextField> names;
  private List<TextField> ages;
  private List<ComboBox<PlayerIcon>> icons;
  private final Button generate = new Button("Generate Fields");
  private final Button start = new Button("Start Game");
  private final VBox fieldsBox = new VBox(15);
  private List<Label> nameErrors;
  private List<Label> ageErrors;
  private List<Label> iconErrors;

  /**
   * Constructs the player setup page with controls for selecting player count, dice count, and
   * entering player information.
   */
  public PlayerSetupPage() {
    // Title
    Text title = new Text("Player Setup");
    title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));

    // Top controls: player count + dice selection
    HBox topControls = new HBox(20,
        new Label("Players:"), playerCountSpinner,
        new Label("Dice:"), createDiceRadio("1 Die", true),
        createDiceRadio("2 Dice", false)
    );
    topControls.setAlignment(Pos.CENTER);

    // Style buttons
    generate.setFont(Font.font(14));
    generate.setStyle(
        "-fx-background-color: #59753c;"
            + "-fx-text-fill: white;"
            + "-fx-background-radius: 10;"
    );
    start.setFont(Font.font(14));
    start.setStyle(
        "-fx-background-color: #89b391;"
            + "-fx-text-fill: white;"
            + "-fx-background-radius: 10;"
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
        "-fx-background-color: #faf0e3;"
            + "-fx-border-color: #aeca7e;"
            + "-fx-border-radius: 15;"
    );
    setCenter(centerBox);
  }

  /**
   * Dynamically generates name/age/icon fields for each player.
   */
  public void createFields() {
    fieldsBox.getChildren().clear();
    names = new ArrayList<>();
    ages = new ArrayList<>();
    icons = new ArrayList<>();
    nameErrors = new ArrayList<>();
    ageErrors = new ArrayList<>();
    iconErrors = new ArrayList<>();

    for (int i = 0; i < getPlayerCount(); i++) {
      TextField name = new TextField();
      name.setPromptText("Name");
      TextField age = new TextField();
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

      Label nameErr = new Label();
      nameErr.setTextFill(Color.RED);
      Label ageErr = new Label();
      ageErr.setTextFill(Color.RED);
      Label iconErr = new Label();
      iconErr.setTextFill(Color.RED);

      names.add(name);
      ages.add(age);
      icons.add(iconCb);
      nameErrors.add(nameErr);
      ageErrors.add(ageErr);
      iconErrors.add(iconErr);

      HBox fieldsLine = new HBox(10, new Label("Player " + (i + 1) + ":"), name, age, iconCb);
      fieldsLine.setAlignment(Pos.CENTER_LEFT);
      HBox errorsLine = new HBox(10, new Label("                "), nameErr, ageErr, iconErr);
      errorsLine.setAlignment(Pos.CENTER_LEFT);

      VBox playerBox = new VBox(2, fieldsLine, errorsLine);
      fieldsBox.getChildren().add(playerBox);

      name.textProperty().addListener((obs, o, n) -> validateForm());
      age.textProperty().addListener((obs, o, n) -> validateForm());
      iconCb.valueProperty().addListener((obs, o, n) -> validateForm());

    }

    // Add and enable the Start button once fields exist
    HBox startBox = new HBox(start);
    startBox.setAlignment(Pos.CENTER);
    fieldsBox.getChildren().add(startBox);
    validateForm();
  }

  /**
   * Validates all player entry fields (name, age, icon). Shows error messages
   * next to invalid fields, and enables the start button only when all are valid.
   */
  private void validateForm() {
    boolean allValid = true;

    for (int i = 0; i < names.size(); i++) {
      nameErrors.get(i).setText("");
      ageErrors.get(i).setText("");
      iconErrors.get(i).setText("");

      String nm = names.get(i).getText().trim();
      if (nm.isEmpty()) {
        nameErrors.get(i).setText("Name required");
        allValid = false;
      } else if (nm.matches(".*\\d.*")) {
        nameErrors.get(i).setText("No digits in name");
        allValid = false;
      }

      String age = ages.get(i).getText().trim();
      try {
        int a = Integer.parseInt(age);
        if (a < 1) {
          ageErrors.get(i).setText("Age must be greater than 0");
          allValid = false;
        }
      } catch (NumberFormatException e) {
        ageErrors.get(i).setText("Enter a valid integer");
        allValid = false;
      }

      if (icons.get(i).getValue() == null) {
        iconErrors.get(i).setText("Select an icon");
        allValid = false;
      }
    }
    start.setDisable(!allValid);
  }

  /**
   * Creates a styled radio button for dice selection.
   *
   * @param text the label for the radio button
   * @param selected whether this radio button is initially selected
   * @return the created RadioButton
   */
  private RadioButton createDiceRadio(String text, boolean selected) {
    RadioButton rb = new RadioButton(text);
    rb.setToggleGroup(diceGroup);
    rb.setSelected(selected);
    return rb;
  }

  /**
   * Returns the button for generating player fields.
   *
   * @return the generate fields Button
   */
  public Button getGenerateButton() {
    return generate;
  }

  /**
   * Returns the button for starting the game.
   *
   * @return the start game Button
   */
  public Button getStartButton() {
    return start;
  }

  /**
   * Returns the selected number of players.
   *
   * @return the player count (minimum 2, maximum 5)
   */
  public int getPlayerCount() {
    return playerCountSpinner.getValue();
  }

  /**
   * Returns the selected number of dice (1 or 2).
   *
   * @return the dice count
   */
  public int getDiceCount() {
    RadioButton sel = (RadioButton) diceGroup.getSelectedToggle();
    return sel.getText().contains("1") ? 1 : 2;
  }

  /**
   * Collects and returns form data for all players, to be used by the controller.
   * The returned list contains PlayerFormData objects with name, age, and icon for each player.
   *
   * @return a list of PlayerFormData for all players
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
