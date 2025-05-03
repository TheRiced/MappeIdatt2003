package ntnu.idatt2003.view;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;

public interface Observer {

  void onPlayerMoved(SnakeLadderPlayer player, int fromTileId, int toTileId);
  void onNextPlayer(SnakeLadderPlayer next);
  void onGameOver(SnakeLadderPlayer winner);

  }

