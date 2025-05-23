package ntnu.idatt2003.model.ludo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import javafx.geometry.Point2D;

public class LudoBoard implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final int MAIN_PATH_SIZE = 44;

  private final List<LudoTile> mainPath;
  private final Map<TokenColor, List<LudoTile>> home;
  private final Map<TokenColor, List<LudoTile>> finishLanes;

  /** Measured pixel‐coordinates (from your Figma) for every tile index. */
  private static final Map<Integer, Point2D> tileCoordinates;
  static {
    Map<Integer, Point2D> m = new HashMap<>();

    m.put(0,  new Point2D(460, 305));
    m.put(1,  new Point2D(420, 305));
    m.put(2,  new Point2D(380, 305));
    m.put(3,  new Point2D(350, 305));
    m.put(4,  new Point2D(310, 338));
    m.put(5,  new Point2D(310, 380));
// star
    m.put(6,  new Point2D(310, 420));
    m.put(7,  new Point2D(310, 460));
    m.put(8,  new Point2D(310, 500));
    m.put(9,  new Point2D(260, 500));
    m.put(10, new Point2D(225, 500));
    m.put(11, new Point2D(225, 460));
    m.put(12, new Point2D(225, 420));
    m.put(13, new Point2D(225, 380));
    m.put(14, new Point2D(225, 340));
    m.put(15, new Point2D(180, 305));
    m.put(16, new Point2D(140, 305));
    m.put(17, new Point2D(100, 305));
// star
    m.put(18, new Point2D(60,  305));
    m.put(19, new Point2D(20,  305));
    m.put(20, new Point2D(20,  265));
    m.put(21, new Point2D(20,  225));
    m.put(22, new Point2D(60,  225));
    m.put(23, new Point2D(100, 225));
    m.put(24, new Point2D(140, 225));
    m.put(25, new Point2D(180, 225));
    m.put(26, new Point2D(225, 190));
    m.put(27, new Point2D(225, 150));
    m.put(28, new Point2D(225, 110));
    m.put(29, new Point2D(225, 70));
    m.put(30, new Point2D(225,  30));
    m.put(31, new Point2D(260,  30));
    m.put(32, new Point2D(300,  30));
    m.put(33, new Point2D(300,  65));
    m.put(34, new Point2D(300,  105));
    m.put(35, new Point2D(300, 145));
    m.put(36, new Point2D(300, 185));
    m.put(37, new Point2D(340, 225));
    m.put(38, new Point2D(380, 225));
    m.put(39, new Point2D(420, 225));
    m.put(40, new Point2D(460, 225));
    m.put(41, new Point2D(500, 225));
    m.put(42, new Point2D(500, 265));
    m.put(43, new Point2D(500, 305));
// yellow tiles to finish
    m.put(44, new Point2D(460, 265));
    m.put(45, new Point2D(420, 265));
    m.put(46, new Point2D(380, 265));
    m.put(47, new Point2D(340, 265));
// yellow finish
    m.put(48, new Point2D(300, 265));
// blue tiles to finish
    m.put(49, new Point2D(265, 460));
    m.put(50, new Point2D(265, 420));
    m.put(51, new Point2D(265, 380));
    m.put(52, new Point2D(265, 340));
// blue finish
    m.put(53, new Point2D(265, 300));
// red tiles to finish (finish lane)
    m.put(54, new Point2D(60,  265));
    m.put(55, new Point2D(100, 265));
    m.put(56, new Point2D(140, 265));
    m.put(57, new Point2D(180, 265));
// red finish
    m.put(58, new Point2D(220, 265));
// green tiles
    m.put(59, new Point2D(265,  25));
    m.put(60, new Point2D(265, 65));
    m.put(61, new Point2D(265, 105));
    m.put(62, new Point2D(265, 145));
    m.put(63, new Point2D(265, 185));
// starting home tiles
// yellow home
    m.put(64, new Point2D(365, 365));
    m.put(65, new Point2D(475, 365));
    m.put(66, new Point2D(365, 475));
    m.put(67, new Point2D(475, 475));
// blue home
    m.put(68, new Point2D(50, 365));
    m.put(69, new Point2D(50,  475));
    m.put(70, new Point2D(160,  365));
    m.put(71, new Point2D(160, 475));
// red home
    m.put(72, new Point2D(50,  50));
    m.put(73, new Point2D(160, 50));
    m.put(74, new Point2D(160, 160));
    m.put(75, new Point2D(50,  160));
// green home
    m.put(76, new Point2D(365, 50));
    m.put(77, new Point2D(365, 160));
    m.put(78, new Point2D(475, 50));
    m.put(79, new Point2D(475, 160));


    tileCoordinates = Collections.unmodifiableMap(m);
  }

  public LudoBoard() {
    this.mainPath    = buildMainPath();
    this.home        = buildHome();
    this.finishLanes = buildFinishLanes();
    // Let each TokenColor know its finish‐lane tiles
    finishLanes.forEach((c, tiles) -> c.setFinishLaneTiles(tiles));
  }

  private static final Set<Integer> SAFE_INDICES = Set.of(6, 18, 29, 40);

  /** Build the 0–43 looping main path, marking safe stars. */
  private List<LudoTile> buildMainPath() {
    var p = new ArrayList<LudoTile>(MAIN_PATH_SIZE);
    for (int i = 0; i < MAIN_PATH_SIZE; i++) {
      var type = SAFE_INDICES.contains(i)
          ? LudoTileType.SAFE
          : LudoTileType.NORMAL;
      p.add(new LudoTile(i, type));
    }
    return Collections.unmodifiableList(p);
  }

  /** Build each colour’s 4 home‐yard tiles (indices 64–79). */
  private Map<TokenColor, List<LudoTile>> buildHome() {
    var m = new EnumMap<TokenColor, List<LudoTile>>(TokenColor.class);
    m.put(TokenColor.YELLOW, createHomeTiles(64));
    m.put(TokenColor.BLUE,   createHomeTiles(68));
    m.put(TokenColor.RED,    createHomeTiles(72));
    m.put(TokenColor.GREEN,  createHomeTiles(76));
    return Collections.unmodifiableMap(m);
  }


  private List<LudoTile> createHomeTiles(int startIndex) {
    var list = new ArrayList<LudoTile>(4);
    for (int i = 0; i < 4; i++) {
      list.add(new LudoTile(startIndex + i, LudoTileType.HOME));
    }
    return Collections.unmodifiableList(list);
  }

  private Map<TokenColor, List<LudoTile>> buildFinishLanes() {
    var map = new EnumMap<TokenColor, List<LudoTile>>(TokenColor.class);

    for (var color : TokenColor.values()) {
      int base = color.getFinishStartIndex();
      var lane = new ArrayList<LudoTile>(5);


      for (int i = 0; i < 5; i++) {
        LudoTileType type = (i == 4)
            ? LudoTileType.FINISH
            : LudoTileType.NORMAL;
        lane.add(new LudoTile(base + i, type));
      }

      map.put(color, Collections.unmodifiableList(lane));
    }
    return Collections.unmodifiableMap(map);
  }

  /** @return the circular main path. */
  public List<LudoTile> getMainPath() {
    return mainPath;
  }

  public List<LudoTile> getHome(TokenColor c) {
    return home.get(c);
  }


  public List<LudoTile> getFinishLanes(TokenColor c) {
    return finishLanes.get(c);
  }

  public static Map<Integer, Point2D> getTileCoordinates() {
    return tileCoordinates;
  }


  public List<LudoTile> getFullPath(TokenColor c) {
    List<LudoTile> loop = mainPath;
    var path = new ArrayList<LudoTile>(loop.size() + finishLanes.get(c).size());
    path.addAll(loop.subList(c.getStartIndex(), loop.size()));
    path.addAll(loop.subList(0, c.getStartIndex()));
    path.addAll(finishLanes.get(c));
    return Collections.unmodifiableList(path);
  }


  public LudoTile getNextTile(Token token, int steps) {
    LudoTile cur = token.getPosition();
    TokenColor c = token.getColor();
    List<LudoTile> full = getFullPath(c);
    LudoTile dest;

    if (cur.getType() == LudoTileType.HOME) {
      // only a 6 can leave HOME
      dest = (steps == 6) ? full.get(0) : cur;
    } else {
      int idx = full.indexOf(cur);
      if (idx < 0) idx = 0;                       // safety fallback
      int next = Math.min(idx + steps, full.size() - 1);
      dest = full.get(next);
    }

    // block landing on occupied SAFE star
    if (dest.getType() == LudoTileType.SAFE && dest.hasOpponent(token)) {
      return cur;
    }
    return dest;
  }

  /** @return colour→finish-lane map. */
  public Map<TokenColor, List<LudoTile>> getFinishLanesMap() {
    return finishLanes;
  }
}