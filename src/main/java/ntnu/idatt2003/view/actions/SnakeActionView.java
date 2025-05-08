package ntnu.idatt2003.view.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 * Renders a wiggling snake-like polyline between two points, with a round head,
 * eyes, and an additional highlight line along the body.
 */
public class SnakeActionView implements ActionView {
  private final int wiggles;
  private final double thickness;

  // Example constant; replace or import from your tile/map config
  private static final double TILE_SIZE = 40.0;

  public SnakeActionView(int wiggles, double thickness) {
    this.wiggles   = wiggles;
    this.thickness = thickness;
  }

  /**
   * Builds a Path from 'from' to 'to' that follows the same sine-wavy
   * curve you used in drawSnake().
   */
  public Path buildSnakePath(Point2D from, Point2D to, double iconWidth, double iconHeight) {
    // how much to shift so that Path coords == node.translate coords
    double dxShift = -iconWidth  / 2.0;
    double dyShift = -iconHeight / 2.0;

    // compute your usual snake geometry…
    double dx = to.getX() - from.getX();
    double dy = to.getY() - from.getY();
    double length = Math.hypot(dx, dy);
    double ux = dx / length, uy = dy / length;
    double px = -uy, py = ux;

    int wiggles = 2;
    int segments = wiggles * 20;
    double thickness = TILE_SIZE * 0.25;  // or whatever you used

    // first point (with shift)
    double x0 = from.getX() + dxShift;
    double y0 = from.getY() + dyShift;
    Path path = new Path(new MoveTo(x0, y0));

    // intermediate wiggly points
    for (int i = 1; i <= segments; i++) {
      double t = (double)i / segments;
      double sine   = Math.sin(2 * Math.PI * wiggles * t);
      double offset = sine * thickness;
      double bx = from.getX() + dx * t + px * offset + dxShift;
      double by = from.getY() + dy * t + py * offset + dyShift;
      path.getElements().add(new LineTo(bx, by));
    }

    // finally snap to the destination (with shift)
    double xEnd = to.getX() + dxShift;
    double yEnd = to.getY() + dyShift;
    path.getElements().add(new LineTo(xEnd, yEnd));

    return path;
  }



  @Override
  public Group build(Point2D p1, Point2D p2) {
    Group g = new Group();

    double dx = p2.getX() - p1.getX(), dy = p2.getY() - p1.getY();
    double len = Math.hypot(dx, dy), ux = dx / len, uy = dy / len;
    double px = -uy, py = ux;

    // Draw main body as a wiggling curve
    int segments = wiggles * 20;
    Polyline body = new Polyline();
    body.setStroke(Color.FORESTGREEN);
    body.setStrokeWidth(thickness);
    body.setStrokeLineCap(StrokeLineCap.ROUND);
    body.setStrokeLineJoin(StrokeLineJoin.ROUND);


    Polyline highlight = new Polyline();
    highlight.setStroke(Color.YELLOWGREEN);
    highlight.setStrokeWidth(thickness * 0.5);
    highlight.setStrokeLineCap(StrokeLineCap.ROUND);
    highlight.setStrokeLineJoin(StrokeLineJoin.ROUND);

    for (int i = 0; i <= segments; i++) {
      double t = (double) i / segments;
      double bx = p1.getX() + dx * t;
      double by = p1.getY() + dy * t;
      double off = Math.sin(2 * Math.PI * wiggles * t) * thickness * 1.25;
      double x = bx + px * off;
      double y = by + py * off;

      body.getPoints().addAll(x, y);
      // highlight closer to center (offset fractionally less)
      double off2 = off * 0.5;
      double hx = bx + px * off2;
      double hy = by + py * off2;
      highlight.getPoints().addAll(hx, hy);
    }
    g.getChildren().addAll(body, highlight);

    // Draw round head
    double headRadius = TILE_SIZE * 0.35;
    Circle head = new Circle(p1.getX(), p1.getY(), headRadius);
    head.setFill(Color.FORESTGREEN);
    head.setStroke(Color.FORESTGREEN);
    g.getChildren().add(head);

    // Draw eyes
    double eyeDistance = headRadius * 0.25;
    double eyeOffset   = headRadius * 0.45;
    double eyeRadius   = TILE_SIZE * 0.04;
    double raise       = headRadius * 0.6;

    // Left eye
    double lx = p1.getX() + ux * eyeDistance + px * eyeOffset;
    double ly = p1.getY() + uy * eyeDistance + py * eyeOffset - raise;
    Circle leftEye = new Circle(lx, ly, eyeRadius);
    leftEye.setFill(Color.BLACK);

    // Right eye
    double rx = p1.getX() + ux * eyeDistance - px * eyeOffset;
    double ry = p1.getY() + uy * eyeDistance - py * eyeOffset - raise;
    Circle rightEye = new Circle(rx, ry, eyeRadius);
    rightEye.setFill(Color.BLACK);

    g.getChildren().addAll(leftEye, rightEye);

    return g;
  }
}
