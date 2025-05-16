package ntnu.idatt2003.model.ludo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LudoBoard {

  private static final int MAIN_PATH_SIZE = 52;
  private final List<LudoTile> mainPath;
  private final Map<TokenColor, List<LudoTile>> home;
  private final Map<TokenColor, List<LudoTile>> finishLanes;

  public LudoBoard() {
    this.mainPath = buildMainPath();
    this.home = buildHome();
    this.finishLanes = buildFinishLanes();

    finishLanes.forEach((color, tiles) -> color.setFinishLaneTiles(tiles));
  }

  private List<LudoTile> buildMainPath() {
    List<Integer> safeIndices = Arrays.asList(8, 21, 34, 47);
    List<LudoTile> path = new ArrayList<>(MAIN_PATH_SIZE);
    for (int i = 0; i < MAIN_PATH_SIZE; i++) {
      LudoTileType type = safeIndices.contains(i) ? LudoTileType.SAFE : LudoTileType.NORMAL;
      path.add(new LudoTile(i, type));
    }
    return Collections.unmodifiableList(path);
  }

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

  public List<LudoTile> getMainPath() { return mainPath; }
  public List<LudoTile> getHome(TokenColor color) { return home.get(color); }
  public List<LudoTile> getFinishLanes(TokenColor color) { return finishLanes.get(color); }

  public LudoTile getNextTile(Token token, int steps) {
    LudoTile current = token.getPosition();
    TokenColor color = token.getColor();

    if (current.getType() == LudoTileType.HOME && steps == 6) {
      return mainPath.get(color.getStartIndex());
    }

    if (current.getType() == LudoTileType.NORMAL || current.getType() == LudoTileType.SAFE) {
      int idx = current.getIndex();
      int finishEntry = color.getFinishEntryIndex();
      int distToEntry = (finishEntry - idx + MAIN_PATH_SIZE) % MAIN_PATH_SIZE;

      if (steps > distToEntry) {
        int intoLane = steps - distToEntry - 1;
        List<LudoTile> lane = finishLanes.get(color);
        return (intoLane < lane.size()) ? lane.get(intoLane) : current;
      }

      int target = (idx + steps) % MAIN_PATH_SIZE;
      return mainPath.get(target);
    }

    List<LudoTile> lane = finishLanes.get(color);
    int posInLane = lane.indexOf(current);
    int nextPos = posInLane + steps;
    return (nextPos < lane.size()) ? lane.get(nextPos) : current;
  }
}
