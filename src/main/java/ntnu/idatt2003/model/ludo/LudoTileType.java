package ntnu.idatt2003.model.ludo;

/**
 * Defines the different types of tiles on a Ludo board.
 * <ul>
 *   <li>{@code HOME}: Where each player's tokens start the game and return when sent home.</li>
 *   <li>{@code START}: The entry tile for each color (tokens leave home and enter here, usually
 *   by rolling a 6).</li>
 *   <li>{@code NORMAL}: Standard tile on the main loop; most movement occurs here.</li>
 *   <li>{@code SAFE}: Special tile on the main path; multiple tokens can share this tile safely
 *   (cannot be captured).</li>
 *   <li>{@code FINISH_ENTRY}: The main-path tile where tokens turn off the loop and enter their
 *   color's finish lane.</li>
 *   <li>{@code FINISH}: The last tile in a color's finish lane; the goal for each token.</li>
 * </ul>
 */
public enum LudoTileType {
  HOME,
  START,
  NORMAL,
  SAFE,
  FINISH_ENTRY,
  FINISH;
}
