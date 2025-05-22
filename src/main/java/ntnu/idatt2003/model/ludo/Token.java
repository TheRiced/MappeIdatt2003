package ntnu.idatt2003.model.ludo;

import java.util.Objects;

/**
 * Represents a single token (pawn) in a Ludo game.
 *
 * <p>Each token belongs to a player, has a unique color (matching the player),
 * and always occupies exactly one {@link LudoTile} (either in home, on the main path, in a finish
 * lane, or at the finish).
 * </p>
 */
public class Token {

  private final int id;
  private final LudoPlayer owner;
  private final TokenColor color;
  private final LudoTile homeTile;
  private LudoTile position;

  /**
   * Constructs a Token for Ludo.
   *
   * @param id       unique identifier (0-3) per player
   * @param owner    the LudoPlayer who owns this token
   * @param color    this token's color (must match owner)
   * @param homeTile the HOME tile where this token starts
   */
  public Token(int id, LudoPlayer owner, TokenColor color, LudoTile homeTile) {
    if (owner == null) {
      throw new IllegalArgumentException("Owner can not be null");
    }
    if (color == null) {
      throw new IllegalArgumentException("Color can not be null");
    }
    if (homeTile == null) {
      throw new IllegalArgumentException("Home tile can not be null");
    }
    this.id = id;
    this.owner = owner;
    this.color = color;
    this.homeTile = homeTile;
    this.position = homeTile;
    homeTile.enter(this);
  }

  public int getId() {
    return id;
  }

  public LudoPlayer getOwner() {
    return owner;
  }

  public TokenColor getColor() {
    return color;
  }

  public LudoTile getPosition() {
    return position;
  }

  /**
   * Moves this token onto the given tile, handling removal from the previous tile and placement to
   * the new tile.
   *
   * @param newTile the destination tile (must not be null)
   */
  public void moveTo(LudoTile newTile) {
    if (newTile == null) {
      throw new IllegalArgumentException("New tile can not be null");
    }
    position.leave(this);
    newTile.enter(this);
    this.position = newTile;
  }

  /**
   * Sends the token back to its HOME tile.
   */
  public void sendHome() {
    moveTo(homeTile);
  }

  /**
   * Returns true if this token is still on its HOME tile.
   *
   * @return token is now on its Home tile.
   */
  public boolean isAtHome() {
    return position.getType() == LudoTileType.HOME;
  }

  /**
   * Returns true if this token has reached its FINISH tile.
   *
   * @return token is now on its FINISH tile.
   */
  public boolean isFinished() {
    return position.getType() == LudoTileType.FINISH;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Token)) {
      return false;
    }
    Token that = (Token) o;
    return id == that.id && owner.equals(that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, owner);
  }

}
