package ntnu.idatt2003.view;

import ntnu.idatt2003.model.Player;

public interface Observer {

  void onPlayerMoved(Player player, int fromTileId, int toTileId);
  void onNextPlayer(Player next);
  void onGameOver(Player winner);

  void placeAllPlayers();
}

