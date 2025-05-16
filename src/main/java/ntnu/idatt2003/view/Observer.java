package ntnu.idatt2003.view;

import java.util.List;
import ntnu.idatt2003.model.Player;

public interface Observer {

  void onPlayerMoved(Player player, int fromTileId, int toTileId);

  void onDiceRolled(List<Integer> values);

  void onNextPlayer(Player next);
  void onGameOver(Player winner);

  void placeAllPlayers();
}

