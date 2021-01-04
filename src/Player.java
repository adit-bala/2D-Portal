import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public final int SIZE = 20;
    // Pixel coordinates (upper left corner of cube)
    private int posX;
    private int posY;
    private double velX = 0;
    private double velY = 0;
    private final int margin = 1;
    private final int GRAVITY = 2;
    private Block[][] map;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setMap(Block[][] map) {
        this.map = map;
    }

    public void move() {
        // Checking for block going right
        if (velX > 0) {
            // Checks if block is going out of map or if the bottom right corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + SIZE + velX + margin), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + SIZE), (int) posY + SIZE)).isSolid()) {
                posX += velX;
            }
        } else if (velX < 0) {
            // Checks if block is going out of map or if the bottom left corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + velX + margin), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + margin),posY + SIZE)).isSolid()) {
                posX += velX;
            }
        }

        // Executes Physics if block is in the air
        if(inAir()) {
            // Check if block is hitting a block above
            if(velY < 0) {
                // margin could cause problems in the future : (
                Point currPoint = new Point((posX), posY - 5);
                if (getCurrBlock(map, currPoint).isSolid()) {
                    velY = 0;
                }
            }
            // Incrementally reduce velocity
            velY += GRAVITY;
            // Remove the small gap when a block is coming down
            if(!getCurrBlock(map, new Point(posX, (int) (posY + SIZE + velY + margin))).getColor().equals(Color.white) || !getCurrBlock(map, new Point(posX + SIZE, (int) (posY + SIZE + velY + margin))).getColor().equals(Color.white)){
                    Point gap = new Point(posX,  (posY + SIZE + margin));
                    Point bottom = getCurrBlock(map, new Point(posX, (int) (posY + SIZE + velY + margin))).getPixelCoords();
                    posY += bottom.getY() - gap.getY() + margin;
                    velY = 0;

            }
        }
        // Incrementally reduce block's height
        posY += velY;




    }

    // Checks below cube to see if the color is not white (or in space)
    private boolean inAir() {
        if(!getCurrBlock(map, new Point(posX, posY + SIZE + margin)).getColor().equals(Color.white) ||
           !getCurrBlock(map, new Point(posX + SIZE, posY + SIZE + margin)).getColor().equals(Color.white)) {
            return false;
        } else {
            return true;
        }
    }

    // Get's the current block with the passed in Pixel Coordinates
    private Block getCurrBlock(Block[][] map, Point coord) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Block currBlock = map[i][j];
                //System.out.println("X Position: " + currBlock.getPixelCoords().getX() + " Y Position: " + currBlock.getPixelCoords().getY() + "X: " + coord.getX() + " Y: " + coord.getY());
                if (coord.getX() >= currBlock.getPixelCoords().getX() && coord.getX() <= currBlock.getPixelCoords().getX() + Block.SIZE && coord.getY() >= currBlock.getPixelCoords().getY() && coord.getY() <= currBlock.getPixelCoords().getY() + Block.SIZE) {
                    return currBlock;
                }
            }
        }
        return null;
    }
    // Getter methods
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            velX = -2;
        }
        if (key == KeyEvent.VK_D) {
            velX = 2;
        }
        if (key == KeyEvent.VK_W) {
            // Player only jumps when the block is not in the air
            if (!inAir()) {
                velY = -22;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            velX = 0;
        }
        if (key == KeyEvent.VK_D) {
            velX = 0;
        }
    }

}
