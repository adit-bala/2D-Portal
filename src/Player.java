import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private int dx;
    private int dy;
    private int x;
    private int y;
    private int w;
    private int h;
    private final int gravity = -10;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        w = 20;
        h = 20;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getWidth() {

        return w;
    }

    public int getHeight() {

        return h;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            dx = -2;
        }

        if (key == KeyEvent.VK_D) {
            dx = 2;
        }

        if (key == KeyEvent.VK_W) {
            dy = -2;
        }

        if (key == KeyEvent.VK_S) {
            dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }
    }

}
