package ntnu.idatt2003.view.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class LadderActionView implements ActionView {
  private final int   rungCount;
  private final double railOffset;
  private final double railWidth;
  private final double rungWidth;

  public LadderActionView(int rungCount, double railOffset, double railWidth, double rungWidth) {
    this.rungCount  = rungCount;
    this.railOffset = railOffset;
    this.railWidth  = railWidth;
    this.rungWidth  = rungWidth;
  }


  public Path buildLadderPath(Point2D from, Point2D to) {
    Path p = new Path();

    p.getElements().add(new MoveTo(from.getX(), from.getY()));

    p.getElements().add(new LineTo(to.getX(), to.getY()));
    return p;
  }

  @Override
  public Group build(Point2D p1, Point2D p2) {
    Group g = new Group();

    double dx = p2.getX() - p1.getX(), dy = p2.getY() - p1.getY();
    double len = Math.hypot(dx, dy), ux = dx/len, uy = dy/len;
    double px = -uy, py = ux;

    // rails
    Line rail1 = new Line(p1.getX()+px*railOffset, p1.getY()+py*railOffset,
        p2.getX()+px*railOffset, p2.getY()+py*railOffset);
    Line rail2 = new Line(p1.getX()-px*railOffset, p1.getY()-py*railOffset,
        p2.getX()-px*railOffset, p2.getY()-py*railOffset);
    rail1.setStrokeWidth(railWidth);
    rail2.setStrokeWidth(railWidth);
    rail1.setStroke(Color.SADDLEBROWN);
    rail2.setStroke(Color.SADDLEBROWN);

    g.getChildren().addAll(rail1, rail2);

    // rungs
    for (int i = 1; i <= rungCount; i++) {
      double t = (double)i/(rungCount+1);
      double mx = p1.getX()+dx*t, my = p1.getY()+dy*t;
      Line rung = new Line(mx+px*railOffset, my+py*railOffset,
          mx-px*railOffset, my-py*railOffset);
      rung.setStrokeWidth(rungWidth);
      rung.setStroke(Color.ROSYBROWN);
      g.getChildren().add(rung);
    }

    return g;
  }
}
