package ntnu.idatt2003.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ntnu.idatt2003.core.PlayerIcon;


public class PlayerSetupPage extends VBox {

  private final Spinner<Integer> playerCountSpinner = new Spinner<>(2,6,2);
  private final ToggleGroup diceGroup = new ToggleGroup();
  private List<TextField> names;
  private List<TextField> ages;
  private List<ComboBox<PlayerIcon>> icons;
  private final Button generate = new Button("Generate Fields");
  private final Button start = new Button("Start Game");
  private final VBox fieldsBox = new VBox(10);

  public PlayerSetupPage() {
    setPadding(new Insets(20));
    setSpacing(10);
    getChildren().addAll(new Label("Players:"), playerCountSpinner);
    RadioButton one = new RadioButton("1 Die");
    RadioButton two = new RadioButton("2 Dice");
    one.setToggleGroup(diceGroup);
    two.setToggleGroup(diceGroup);
    one.setSelected(true);
    HBox diceBox = new HBox(10, new Label("Dice:"), one, two);
    getChildren().addAll(diceBox, generate, fieldsBox);

  }

  public Button getGenerateButton() { return generate; }
  public Button getStartButton() { return start; }
  public int getPlayerCount() {
    return playerCountSpinner.getValue();
  }
  public int getDiceCount() {
    RadioButton sel = (RadioButton) diceGroup.getSelectedToggle();
    return sel.getText().contains("1") ? 1 : 2;
  }

  public void createFields() {
    fieldsBox.getChildren().clear();
    names = new ArrayList<>();
    ages = new ArrayList<>();
    icons = new ArrayList<>();

    for (int i = 0; i < getPlayerCount(); i++) {
      TextField name = new TextField();
      name.setPromptText("Name");
      TextField age = new TextField();
      age.setPromptText("Age");
      ComboBox<PlayerIcon> icon = new ComboBox<>();
      icon.getItems().addAll(PlayerIcon.values());
      names.add(name);
      ages.add(age);
      icons.add(icon);

      HBox line = new HBox(10, new Label("Player " + (i+1) + ":"), name, age, icon);
      fieldsBox.getChildren().add(line);
    }
    fieldsBox.getChildren().add(start);
  }

  public List<PlayerFormData> collectPlayers() {
    List<PlayerFormData> data = new ArrayList<>();
    for (int i = 0; i < names.size(); i++) {
      data.add(new PlayerFormData(names.get(i).getText(), Integer.parseInt(ages.get(i).getText()),
          icons.get(i).getValue()));
    }
    return data;
  }




}
