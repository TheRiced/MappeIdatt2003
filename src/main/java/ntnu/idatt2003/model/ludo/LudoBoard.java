package ntnu.idatt2003.model.ludo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the board for a Ludo game, including the main path, home yards, and finish lanes for
 * all token colors.
 *
 * <p>The board is composed of:
 * <ul>
 *   <li>Main path: 52 tiles, some of which are safe tiles.</li>
 *   <li>Home yards: Four tiles per color, where each player's tokens start.</li>
 *   <li>Finish lanes: Six tiles per color, leading to the finish for each token.</li>
 * </ul>
 * The logic for calculating moves, including entering the finish lane, is included.
 */
public class LudoBoard {

  private static final int MAIN_PATH_SIZE = 52;
  private final List<LudoTile> mainPath;
  private final Map<TokenColor, List<LudoTile>> home;
  private final Map<TokenColor, List<LudoTile>> finishLanes;

  /**
   * Constructs a standard Ludo board with all tiles initialized.
   */
  public LudoBoard() {
    this.mainPath = buildMainPath();
    this.home = buildHome();
    this.finishLanes = buildFinishLanes();

    finishLanes.forEach((color, tiles) -> color.setFinishLaneTiles(tiles));
  }

  /**
   * Builds the main path of the board, marking safe tiles.
   *
   * @return an unmodifiable list of LudoTile representing the main path.
   */
  private List<LudoTile> buildMainPath() {
    List<Integer> safeIndices = Arrays.asList(8, 21, 34, 47);
    List<LudoTile> path = new ArrayList<>(MAIN_PATH_SIZE);
    for (int i = 0; i < MAIN_PATH_SIZE; i++) {
      LudoTileType type = safeIndices.contains(i) ? LudoTileType.SAFE : LudoTileType.NORMAL;
      path.add(new LudoTile(i, type));
    }
    return Collections.unmodifiableList(path);
  }

  /**
   * Builds the home yard tiles for each color.
   *
   * @return a map from each TokenColor to its list of home tiles.
   */
  private Map<TokenColor, List<LudoTile>> buildHome() {
    Map<TokenColor, List<LudoTile>> map = new EnumMap<>(TokenColor.class);
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> homeYard = new ArrayList<>(4);
      for (int i = 0; i < 4; i++) {
        homeYard.add(new LudoTile(i, LudoTileType.HOME));
      }
      map.put(color, Collections.unmodifiableList(homeYard));
    }
    return Collections.unmodifiableMap(map);
  }

  /**
   * Builds the finish lanes for each color.
   *
   * @return a map from each TokenColor to its list of finish lane tiles.
   */
  private Map<TokenColor, List<LudoTile>> buildFinishLanes() {
    Map<TokenColor, List<LudoTile>> map = new EnumMap<>(TokenColor.class);
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> lane = new ArrayList<>(6);
      for (int i = 0; i < 6; i++) {
        LudoTileType type = (i == 5) ? LudoTileType.FINISH : LudoTileType.NORMAL;
        lane.add(new LudoTile(i, type));
      }
      map.put(color, Collections.unmodifiableList(lane));
    }
    return Collections.unmodifiableMap(map);
  }

  /**
   * Gets the list of tiles in the main path (size: 52).
   *
   * @return unmodifiable list of main path tiles.
   */
  public List<LudoTile> getMainPath() {
    return mainPath;
  }

  /**
   * Gets the list of home yard tiles for the given color.
   *
   * @param color the token color.
   * @return unmodifiable list of home tiles for that color.
   */
  public List<LudoTile> getHome(TokenColor color) {
    return home.get(color);
  }

  /**
   * Gets the list of finish lane tiles for the given color.
   *
   * @param color the token color.
   * @return unmodifiable list of finish lane tiles.
   */
  public List<LudoTile> getFinishLanes(TokenColor color) {
    return finishLanes.get(color);
  }

  /**
   * Calculates the next tile for a token, given its current position and the steps to move. Handles
   * entering the board from home, traversing the main path, and moving into the finish lane.
   *
   * @param token the token to move.
   * @param steps number of steps to move.
   * @return the target tile after the move, or the current tile if the move is invalid.
   */
  public LudoTile getNextTile(Token token, int steps) {
    LudoTile current = token.getPosition();
    TokenColor color = token.getColor();

    // If token is at home and rolls a 6, move to start tile
    if (current.getType() == LudoTileType.HOME && steps == 6) {
      return mainPath.get(color.getStartIndex());
    }

    // If token is on main path (normal/safe), calculate position
    if (current.getType() == LudoTileType.NORMAL || current.getType() == LudoTileType.SAFE) {
      int idx = current.getIndex();
      int finishEntry = color.getFinishEntryIndex();
      int distToEntry = (finishEntry - idx + MAIN_PATH_SIZE) % MAIN_PATH_SIZE;

      // If the token can enter finish lane
      if (steps > distToEntry) {
        int intoLane = steps - distToEntry - 1;
        List<LudoTile> lane = finishLanes.get(color);
        return (intoLane < lane.size()) ? lane.get(intoLane) : current;
      }

      int target = (idx + steps) % MAIN_PATH_SIZE;
      return mainPath.get(target);
    }

    // If token is in finish lane
    List<LudoTile> lane = finishLanes.get(color);
    int posInLane = lane.indexOf(current);
    int nextPos = posInLane + steps;
    return (nextPos < lane.size()) ? lane.get(nextPos) : current;
  }
}
