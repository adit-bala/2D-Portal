import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public final int SIZE = 30;
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

    // FIXME: Entering Portal from Left and Right is not working
    public void move() {
        // Checking for block going right
        if (velX > 0) {
            // Checks if block is going out of map or if the bottom right corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + SIZE + velX + margin), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + SIZE), (int) posY + SIZE)).isSolid()) {
                posX += velX;
            }
            // Checks if running into Orange Portal from Left
            Point inFront = new Point(posX + SIZE, posY + SIZE/2);
            if(inFront.getX() == Portal.DrawOrangeBegin.getX() && inFront.getY() > Portal.DrawOrangeBegin.getY() && inFront.getY() < Portal.DrawOrangeEnd.getY()) {
                teleport('B');
            }
            // Checks if running into Blue Portal from Left
            if(inFront.getX() == Portal.DrawBlueBegin.getX() && inFront.getY() > Portal.DrawBlueBegin.getY() && inFront.getY() < Portal.DrawBlueEnd.getY()) {
                teleport('O');
            }
        } else if (velX < 0) {
            // Checks if block is going out of map or if the bottom left corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + velX + margin), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + margin),posY + SIZE)).isSolid()) {
                posX += velX;
            }
            // Checks if running into Orange Portal from Right
            Point inFront = new Point(posX, posY + SIZE/2);
            if(inFront.getX() == Portal.DrawOrangeBegin.getX() && inFront.getY() > Portal.DrawOrangeBegin.getY() && inFront.getY() < Portal.DrawOrangeEnd.getY()) {
                teleport('B');
            }
            // Checks if running into Blue Portal from Right
            if(inFront.getX() == Portal.DrawBlueBegin.getX() && inFront.getY() > Portal.DrawBlueBegin.getY() && inFront.getY() < Portal.DrawBlueEnd.getY()) {
                teleport('O');
            }
        }

        // Executes Physics if block is in the air
        if(inAir()) {
            // Check if block is hitting a block above
            if(velY < 0) {
                // margin could cause problems in the future : (
                // Checks if running into Orange Portal Above
                Point above = new Point(posX + SIZE/2, posY - 5);
                //System.out.println(above.getX() + ", " + Portal.DrawOrangeBegin.getX() + ", "  + Portal.DrawOrangeEnd.getX());
                if(above.getY() <= Portal.DrawOrangeBegin.getY() && above.getX() > Portal.DrawOrangeBegin.getX() && above.getX() < Portal.DrawOrangeEnd.getX()) {
                    teleport('B');
                }
                // Checks if running into Blue Portal Above
                if(above.getY() <= Portal.DrawBlueBegin.getY() && above.getX() > Portal.DrawBlueBegin.getX() && above.getX() < Portal.DrawBlueEnd.getX()) {
                    teleport('O');
                }
                if (getCurrBlock(map, above).isSolid()) {
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
        Point below = new Point(posX + SIZE/2, posY + SIZE);
        //System.out.println(above.getX() + ", " + Portal.DrawOrangeBegin.getX() + ", "  + Portal.DrawOrangeEnd.getX());
        // Checks if running into Orange Portal Below
        if(below.getY() == Portal.DrawOrangeBegin.getY() && below.getX() > Portal.DrawOrangeBegin.getX() && below.getX() < Portal.DrawOrangeEnd.getX()) {
            teleport('B');
        }
        // Checks if running into Blue Portal Below
        if(below.getY() == Portal.DrawBlueBegin.getY() && below.getX() > Portal.DrawBlueBegin.getX() && below.getX() < Portal.DrawBlueEnd.getX()) {
            teleport('O');
        }
    }

    // Teleports Cube
    private void teleport(Character portal) {
        // teleporting to Blue
        if(portal == 'B') {
            if(Portal.DRAW_BLUE_PORTAL) {
                if(Portal.currBlueSide == Side.TOP) {
                    posX = (int) Portal.DrawBlueBegin.getX();
                    posY = (int) Portal.DrawBlueBegin.getY() - SIZE - margin;
                } else if (Portal.currBlueSide == Side.BOTTOM) {
                    posX = (int) Portal.DrawBlueBegin.getX();
                    posY = (int) Portal.DrawBlueBegin.getY() + margin;
                } else if(Portal.currBlueSide == Side.RIGHT) {
                    posX = (int) Portal.DrawBlueBegin.getX() + margin;
                    posY = (int) Portal.DrawBlueBegin.getY();
                } else if(Portal.currBlueSide == Side.LEFT) {
                    posX = (int) Portal.DrawBlueBegin.getX() - SIZE - margin;
                    posY = (int) Portal.DrawBlueBegin.getY();
                }
            }
        } else if(portal == 'O') {
            if(Portal.DRAW_ORANGE_PORTAL) {
                if(Portal.currOrangeSide == Side.TOP) {
                    posX = (int) Portal.DrawOrangeBegin.getX();
                    posY = (int) Portal.DrawOrangeBegin.getY() - SIZE - margin;
                } else if (Portal.currOrangeSide == Side.BOTTOM) {
                    posX = (int) Portal.DrawOrangeBegin.getX();
                    posY = (int) Portal.DrawOrangeBegin.getY() + margin;
                } else if(Portal.currOrangeSide == Side.RIGHT) {
                    posX = (int) Portal.DrawOrangeBegin.getX() + margin;
                    posY = (int) Portal.DrawOrangeBegin.getY();
                } else if(Portal.currOrangeSide == Side.LEFT) {
                    posX = (int) Portal.DrawOrangeBegin.getX() - SIZE - margin;
                    posY = (int) Portal.DrawOrangeBegin.getY();
                }
            }
        }
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
    public static Block getCurrBlock(Block[][] map, Point coord) {
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
                velY = -15;
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
