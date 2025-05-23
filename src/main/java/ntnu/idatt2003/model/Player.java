package ntnu.idatt2003.model;

import ntnu.idatt2003.core.PlayerIcon;

/**
 * Abstract base class representing a player in a board game.
 *
 * <p>Stores basic information such as name, age, and icon.
 * Implements {@link Comparable} so players can be sorted by age (youngest first).
 * </p>
 */
public abstract class Player implements Comparable<Player> {

  private final String name;
  private final int age;
  private final PlayerIcon icon;

  /**
   * Constructs a new Player.
   *
   * @param name  the player's name (must not be null or blank)
   * @param age   the player's age (must be non-negative)
   * @param icon  the icon representing the player
   * @throws IllegalArgumentException if name is null/blank or age is negative
   */
  protected Player(String name, int age, PlayerIcon icon) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name required");
    }
    if (age < 0) {
      throw new IllegalArgumentException("Age cannot be negative");
    }
    this.name = name;
    this.age = age;
    this.icon = icon;
  }

  /**
   * Returns the player's name.
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the player's age.
   *
   * @return the age of the player
   */
  public int getAge() {
    return age;
  }

  /**
   * Returns the player's icon.
   *
   * @return the icon representing the player
   */
  public PlayerIcon getIcon() {
    return icon;
  }

  /**
   * Compares players based on their age for determining turn order. Younger players go first.
   *
   * @param o the object to be compared.
   * @return Negative if this player is younger, positive if older, zero if same age.
   */
  @Override
  public int compareTo(Player o) {
    return Integer.compare(this.age, o.age);
  }
}
