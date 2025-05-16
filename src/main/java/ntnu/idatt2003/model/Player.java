package ntnu.idatt2003.model;

import ntnu.idatt2003.core.PlayerIcon;

public abstract class Player implements Comparable<Player> {
  private final String name;
  private final int age;
  private final PlayerIcon icon;

  protected Player(String name, int age, PlayerIcon icon) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
    if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    this.name = name;
    this.age = age;
    this.icon = icon;
  }

  public String getName() { return name; }
  public int getAge() { return age; }
  public PlayerIcon getIcon() { return icon; }

  /**
   * Compares players based on their age for determining turn order.
   * Younger players go first.
   * @param o the object to be compared.
   * @return Negative if this player is younger, positive if older, zero if same age.
   */
  @Override
  public int compareTo(Player o) {
    return Integer.compare(this.age, o.age);
  }
}
