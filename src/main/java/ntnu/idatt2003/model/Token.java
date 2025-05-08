package ntnu.idatt2003.model;

import java.util.Objects;
import ntnu.idatt2003.core.LudoTileType;
import ntnu.idatt2003.core.PlayerColor;

/**
 * Represents a single token in a Ludo game.
 * A token always resides on exactly one LudoTile.
 */
public class Token {
  private final int id;
  private final LudoPlayer owner;
  private final LudoTile homeTile;
  private LudoTile position;


  public Token(int id, LudoPlayer owner, LudoTile homeTile) {
    if (owner == null) throw new IllegalArgumentException("Owner can not be null");
    if (homeTile == null) throw new IllegalArgumentException("Home tile can not be null");
    this.id = id;
    this.owner = owner;
    this.homeTile = homeTile;
    this.position = homeTile;
    homeTile.enter(this);
  }

  public int getId() { return id; }
  public LudoPlayer getOwner() { return owner; }
  public PlayerColor getColor() { return owner.getColor(); }
  public LudoTile getPosition() { return position; }

  /**
   * Moves this token onto the given tile, handling removal from the previous tile and addition to
   * the new one.
   * @param newTile the next tile on the board.
   */
  public void moveTo(LudoTile newTile) {
    position.leave(this);
    newTile.enter(this);
    this.position = newTile;
  }

  /**
   * Sends the token back to its home area.
   */
  public void sendHome() {
    moveTo(homeTile);
  }

  /**
   * Returns true if this token is still on its HOME tile.
   * @return token is now on its Home tile.
   */
  public boolean isAtHome() {
    return position.getType() == LudoTileType.HOME;
  }

  /**
   * Returns true if this token has reached its FINISH tile.
   * @return token is now on its FINISH tile.
   */
  public boolean isFinished() {
    return position.getType() == LudoTileType.FINISH;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Token)) return false;
    Token that = (Token) o;
    return id == that.id && owner.equals(that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, owner);
  }

}
