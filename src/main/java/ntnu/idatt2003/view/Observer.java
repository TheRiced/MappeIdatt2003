package ntnu.idatt2003.view;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;

public interface Observer<P> {

  void onPlayerMoved(P player, int fromTileId, int toTileId);
  void onNextPlayer(P next);
  void onGameOver(P winner);

  void placeAllPlayers();
}

