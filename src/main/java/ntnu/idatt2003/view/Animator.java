package ntnu.idatt2003.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.List;

public class Animator {
  private static final double DRIFT_X = 5;
  private static final double DRIFT_Y = 3;

  /** Makes the node drift back and forth in X and Y indefinitely. */
  public void drift(Node node) {
    TranslateTransition driftX = new TranslateTransition(Duration.seconds(3), node);
    driftX.setByX(DRIFT_X);
    driftX.setCycleCount(Animation.INDEFINITE);
    driftX.setAutoReverse(true);

    TranslateTransition driftY = new TranslateTransition(Duration.seconds(4), node);
    driftY.setByY(DRIFT_Y);
    driftY.setCycleCount(Animation.INDEFINITE);
    driftY.setAutoReverse(true);

    // start both in parallel
    driftX.play();
    driftY.play();
  }

  /**
   * Moves the node sequentially through the given list of points,
   * spending {@code stepDuration} on each leg.
   * When finished, runs optional onFinished callback.
   */
  public void moveAlongPath(Node node, Path path, Duration duration, Runnable onFinished) {
    PathTransition pt = new PathTransition(duration, path, node);
    pt.setInterpolator(Interpolator.EASE_BOTH);
    if (onFinished != null) {
      pt.setOnFinished(e -> onFinished.run());
    }
    pt.play();
  }

  // overload without callback
  public void moveAlongPath(Node node, Path path, Duration duration) {
    moveAlongPath(node, path, duration, null);
  }

  public void moveAlong(Node node, List<Point2D> path, javafx.util.Duration stepDuration, Runnable onFinished) {
    if (path.isEmpty()) return;
    SequentialTransition seq = new SequentialTransition();
    for (Point2D p : path) {
      TranslateTransition tt = new TranslateTransition(stepDuration, node);
      tt.setToX(p.getX());
      tt.setToY(p.getY());
      seq.getChildren().add(tt);
    }
    if (onFinished != null) {
      seq.setOnFinished(evt -> onFinished.run());
    }
    seq.play();
  }
}
