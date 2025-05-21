package ntnu.idatt2003.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Shows X amount of dice by swapping one of six face images.
 */
public class DieDiceView extends HBox {

  private final List<ImageView> dice = new ArrayList<>();
  private final Image[] faces = new Image[7]; // ignore index 0

  public DieDiceView(int diceCount) {
    setSpacing(8);
    setAlignment(Pos.CENTER);

    // preload the six face images
    for (int i = 1; i <= 6; i++) {
      faces[i] = new Image(getClass().getResourceAsStream(
          "/images/dice-" + i + ".png"
      ));
    }

    // create as many ImageViews as we need
    for (int i = 0; i < diceCount; i++) {
      ImageView iv = new ImageView(faces[1]);  // start showing “1”
      iv.setFitWidth(40);
      iv.setFitHeight(40);
      dice.add(iv);
      getChildren().add(iv);
    }
  }

  /**
   * Update each die with the proper face image.
   */
  public void updateDice(List<Integer> rolls) {
    // if we rolled more dice than we have views, add extras
    while (rolls.size() > dice.size()) {
      ImageView iv = new ImageView(faces[1]);
      iv.setFitWidth(40);
      iv.setFitHeight(40);
      dice.add(iv);
      getChildren().add(iv);
    }
    // if fewer, remove the extras
    while (rolls.size() < dice.size()) {
      ImageView iv = dice.remove(dice.size() - 1);
      getChildren().remove(iv);
    }

    // set each view’s image
    for (int i = 0; i < rolls.size(); i++) {
      int face = rolls.get(i);
      if (face >= 1 && face <= 6) {
        dice.get(i).setImage(faces[face]);
      }
    }
  }
}
