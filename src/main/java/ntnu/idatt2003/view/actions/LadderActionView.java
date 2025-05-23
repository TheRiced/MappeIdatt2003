package ntnu.idatt2003.view.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Visualizes a ladder action on the game board between two tiles.
 *
 * <p>Draws two parallel rails and several rungs between them,
 * using customizable visual parameters.
 * </p>
 */
public class LadderActionView implements ActionView {

  private final int rungCount;
  private final double railOffset;
  private final double railWidth;
  private final double rungWidth;

  /**
   * Constructs a LadderActionView with visual parameters.
   *
   * @param rungCount   the number of rungs (steps) to draw
   * @param railOffset  the perpendicular distance from the center to each rail
   * @param railWidth   the stroke width for the rails
   * @param rungWidth   the stroke width for the rungs
   */
  public LadderActionView(int rungCount, double railOffset, double railWidth, double rungWidth) {
    this.rungCount = rungCount;
    this.railOffset = railOffset;
    this.railWidth = railWidth;
    this.rungWidth = rungWidth;
  }

  /**
   * Builds a JavaFX Path representing a simple line (deprecated or for reference only).
   *
   * @param from the starting point of the ladder
   * @param to   the ending point of the ladder
   * @return a Path object representing the ladder center line
   */
  public Path buildLadderPath(Point2D from, Point2D to) {
    Path p = new Path();

    p.getElements().add(new MoveTo(from.getX(), from.getY()));

    p.getElements().add(new LineTo(to.getX(), to.getY()));
    return p;
  }

  @Override
  public Group build(Point2D p1, Point2D p2) {

    double dx = p2.getX() - p1.getX();
    double dy = p2.getY() - p1.getY();
    double len = Math.hypot(dx, dy);
    double ux = dx / len;
    double uy = dy / len;
    double px = -uy;
    double py = ux;

    // rails
    Line rail1 = new Line(p1.getX() + px * railOffset, p1.getY() + py * railOffset,
        p2.getX() + px * railOffset, p2.getY() + py * railOffset);
    Line rail2 = new Line(p1.getX() - px * railOffset, p1.getY() - py * railOffset,
        p2.getX() - px * railOffset, p2.getY() - py * railOffset);
    rail1.setStrokeWidth(railWidth);
    rail2.setStrokeWidth(railWidth);
    rail1.setStroke(Color.SADDLEBROWN);
    rail2.setStroke(Color.SADDLEBROWN);

    Group g = new Group();
    g.getChildren().addAll(rail1, rail2);

    // rungs
    for (int i = 1; i <= rungCount; i++) {
      double t = (double) i / (rungCount + 1);
      double mx = p1.getX() + dx * t, my = p1.getY() + dy * t;
      Line rung = new Line(mx + px * railOffset, my + py * railOffset,
          mx - px * railOffset, my - py * railOffset);
      rung.setStrokeWidth(rungWidth);
      rung.setStroke(Color.ROSYBROWN);
      g.getChildren().add(rung);
    }
    return g;
  }
}
