package ntnu.idatt2003.core;

import javafx.scene.image.Image;

/**
 * Enum representing the available player icons as PNGs.
 */
public enum PlayerIcon {
  TOP_HAT("top_hat.png"),
  CAT("cat.png"),
  DOG("dog.png"),
  CAR("car.png"),
  BOAT("boat.png");

  private final Image image;

  PlayerIcon(String filename) {
    // load from /resources/images/
    this.image = new Image(
        getClass().getResourceAsStream("/images/" + filename),
        32,
        32,
        true,
        true
    );
  }

  /** The JavaFX Image you can show in an ImageView. */
  public Image getImage() {
    return image;
  }

  /** So ComboBox’s toString() isn’t the filename. */
  @Override
  public String toString() {
    return name().replace('_',' ');
  }
}
