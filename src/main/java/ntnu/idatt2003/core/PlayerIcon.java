package ntnu.idatt2003.core;

/**
 * Enum representing the available player icon in the board game.
 */
public enum PlayerIcon {
  TOP_HAT("🎩"),
  CAT("🐱"),
  DOG("🐶"),
  CAR("🚗"),
  BOAT("⛵️");

  private final String symbol;

  PlayerIcon(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }
}
