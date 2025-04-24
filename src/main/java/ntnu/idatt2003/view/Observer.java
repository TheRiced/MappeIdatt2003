package ntnu.idatt2003.view;

import javafx.beans.Observable;
import ntnu.idatt2003.actions.TileAction;
import ntnu.idatt2003.model.Player;

public interface Observer {

  void movePlayer(Player player);
  void endGame(Player winner);

  }

