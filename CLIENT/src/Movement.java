import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Movement extends GameWindow {

    int mouseXObj = mouseX;
    int mouseYObj = mouseY;

    //Mouse event is passed here, and setting the new X & Y positions is handled here
    public void move(MouseEvent e) {
        mouseX = (int) e.getX();
        mouseY = (int) e.getY();
        if (rect.getX() != mouseX || rect.getY() != mouseY) {
            rect.setY(mouseY - (rect.getHeight() / 2));
            rect.setX(mouseX - (rect.getWidth() / 2));
        }
        super.setMouseX(mouseX);
        super.setMouseY(mouseY);

    }

    public void otherPlayerMovement(int x, int y) {
        rect2.setX(x);
        rect2.setY(y);
    }

    public int getXobj() {
        return mouseXObj;
    }
    public int getYobj() {
        return mouseYObj;
    }
    public boolean isObjectInteger(Object o) {
        return o instanceof Integer;
    }
}
