package ntnu.idatt2003.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ntnu.idatt2003.model.GameLevel;

public class LevelSelectionPage extends BorderPane {
  private final ToggleGroup levelGroup = new ToggleGroup();
  private final RadioButton easyRb = new RadioButton("Easy");
  private final RadioButton advRb = new RadioButton("Advanced");
  private final RadioButton customRb = new RadioButton("Custom");
  private final Button confirmBtn = new Button("Confirm");

  public LevelSelectionPage() {
    Label title = new Label("Select Game Level");
    title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));

    Font rbFont = Font.font("Comic Sans MS", FontWeight.NORMAL, 20);

    easyRb.setToggleGroup(levelGroup);
    advRb.setToggleGroup(levelGroup);
    advRb.setSelected(true);
    customRb.setToggleGroup(levelGroup);

    easyRb.setFont(rbFont);
    advRb.setFont(rbFont);
    customRb.setFont(rbFont);

    easyRb.setTextFill(Color.LIMEGREEN);
    advRb.setTextFill(Color.GOLDENROD);
    customRb.setTextFill(Color.DARKCYAN);

    easyRb.setPadding(new Insets(5, 0, 5, 0));
    advRb.setPadding(new Insets(5, 0, 5, 0));
    customRb.setPadding(new Insets(5, 0, 5, 0));

    VBox vBox = new VBox(10, title, easyRb, advRb, customRb, confirmBtn);
    vBox.setAlignment(Pos.CENTER);
    vBox.setPrefWidth(300);
    vBox.setPrefHeight(200);
    vBox.setPadding(new Insets(20));
    this.setCenter(vBox);
  }

  public GameLevel getSelectedLevel() {
    if (easyRb.isSelected()) {
      return GameLevel.EASY;
    } else if (advRb.isSelected()) {
      return GameLevel.ADVANCED;
    } else {
      return GameLevel.CUSTOM;
    }
  }

  public Button getConfirmBtn() {
    return confirmBtn;
  }

}
