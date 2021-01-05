import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Portal {
    public final int SIZE = 10;
    private final int DELAY = 15;
    private Player player;
    private Block[][] map;
    private int posX;
    private int posY;

    public Portal() {

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMap(Block[][] map) {
        this.map = map;
    }

    private void shootOrangePortal(int posX, int posY) {
        if(validPortal(posX, posY)) {

        }
    }


    private void shootBluePortal(int posX, int posY) {

    }

    private boolean validPortal(int posX, int posY) {
        Block currBlock;
        do{
            currBlock = Player.getCurrBlock(map, new Point(posX, posY));
        } while (!currBlock.isSolid());
        return currBlock.isPortal();
    }

    public void mouseClicked(MouseEvent e) {
        int key = e.getButton();
        if(key == MouseEvent.BUTTON1) {
            shootBluePortal(e.getX(), e.getY());
            System.out.println("Left Click");
        }
        if(key == MouseEvent.BUTTON3) {
            shootOrangePortal(e.getX(), e.getY());
            System.out.println("Right Click");
        }

    }

}
