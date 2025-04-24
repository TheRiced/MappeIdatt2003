package ntnu.idatt2003.view;

import ntnu.idatt2003.model.Player;

public interface Observer {

  void movePlayer(Player player);
  void endGame(Player winner);

  }

