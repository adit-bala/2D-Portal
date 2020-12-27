import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public final int SIZE = 20;
    private final int margin = 1;
    private int speedX;
    private int speedY;
    private int posX;
    private int posY;
    private final int gravity = -1;
    private Timer timer;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void move(Block[][] map) {
        /*Block playerBlock = getCurrBlock(map);*/
        if(speedX > 0) {
            Point currPoint = new Point(posX + speedX + SIZE, posY + SIZE);
            if(!getCurrBlock(map, currPoint).isSolid()) {
                /*System.out.println("Pos X:" +  posX + ", " + "Get X: " + (getCurrBlock(map, currPoint).getPixelCoords().getX()+Block.SIZE/2));*/
                posX += speedX;

            }
        } else if (speedX < 0) {
            Point currPoint = new Point(posX + speedX + margin, posY + SIZE);
            if(!getCurrBlock(map, currPoint).isSolid()) {
                posX += speedX;
            }
        }
        if(speedY > 0) {
            Point currPoint = new Point(posX + SIZE, posY + + speedY + SIZE);
            /*System.out.println("Pos X:" +  posX + ", " + "Get X: " + (getCurrBlock(map, currPoint).getPixelCoords().getX()+Block.SIZE/2));*/
            if(!getCurrBlock(map, currPoint).isSolid()) {
                posY += speedY;

            }
        } else if (speedY < 0) {
            Point currPoint = new Point(posX + SIZE, posY + + speedY + margin);
            if(!getCurrBlock(map, currPoint).isSolid()) {
                posY += speedY;
            }
        }
    }

    private Block getCurrBlock(Block[][] map, Point coord) {
        for(int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++) {
                Block currBlock = map[i][j];
               /* System.out.println("X Position: " + currBlock.getPixelCoords().getX() + " Y Position: " + currBlock.getPixelCoords().getY());*/
                if(coord.getX() >= currBlock.getPixelCoords().getX() && coord.getX() <=  currBlock.getPixelCoords().getX()+Block.SIZE && coord.getY() >= currBlock.getPixelCoords().getY() && coord.getY() <= currBlock.getPixelCoords().getY()+Block.SIZE) {
                    return currBlock;
                }
            }
        }
        return null;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            speedX = -2;
        }
        if (key == KeyEvent.VK_D) {
            speedX = 2;
        }
        if (key == KeyEvent.VK_W) {
            speedY = -2;
        }
        if (key == KeyEvent.VK_S) {
            speedY = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            speedX = 0;
        }
        if (key == KeyEvent.VK_D) {
            speedX = 0;
        }
        if (key == KeyEvent.VK_W) {
            speedY = 0;
        }
        if (key == KeyEvent.VK_S) {
            speedY = 0;
        }
    }

}
