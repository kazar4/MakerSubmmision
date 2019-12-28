import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class Engine extends GameWindow {

    public void EngineRun() {

        //Player 1 start position
        rect.setX(scene.getHeight() / 4);
        rect.setY(scene.getWidth() / 4);

        //Player 1 color
        rect.setFill(Color.GREEN);

    }

    //Collision of player and enemy logic
    public boolean CheckCollision(ArrayList < Rectangle > enemies) {
        for (Rectangle enemy: enemies) {
            if (rect.intersects(enemy.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    //Animation for when a player hits an enemy
    public void playerDied() {
        FadeTransition ft = new FadeTransition();
        ft.setNode(rect);
        ft.setFromValue(0.1);
        ft.setByValue(0.3);
        ft.setToValue(1.0);
        ft.setAutoReverse(false);
        ft.setCycleCount(1);
        ft.setDuration(Duration.millis(1000));
        ft.play();
        ft.setOnFinished(e - > canDie = true);
    }
}
