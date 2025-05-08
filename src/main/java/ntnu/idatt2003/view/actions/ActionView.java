package ntnu.idatt2003.view.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;

/**
 * A little interface for anything that can draw itself
 * between two tile‐centers.
 */
public interface ActionView {
  /**
   * Build and return a JavaFX node (Group) that renders
   * this action from `from` → `to`.
   */
  Group build(Point2D from, Point2D to);
}
