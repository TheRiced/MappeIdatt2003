package ntnu.idatt2003.view;

import java.util.List;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;

public interface Observer<P> {

  void onPlayerMoved(P player, int fromTileId, int toTileId);

  void onDiceRolled(List<Integer> values);

  void onNextPlayer(P next);
  void onGameOver(P winner);

  void placeAllPlayers();
}

