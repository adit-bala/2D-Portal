import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public static final int SIZE = 30;
    // Pixel coordinates (upper left corner of cube)
    private int posX;
    private int posY;
    private final int MARGIN = 1;
    private final int PORTAL_MARGIN = 1;
    private final int GRAVITY = 2;
    private double velX = 0;
    private double velY = 0;
    private Block[][] map;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setMap(Block[][] map) {
        this.map = map;
    }

    /* FIXME: - Phase through side of Block when jumping
    *         - Sometimes phase through floor when gliding down block
    */
    public void move() {
        // Checking for block going right
        if (velX > 0) {
            // Checks if block is going out of map or if the bottom right corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + SIZE + velX + MARGIN), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + SIZE), (int) posY + SIZE)).isSolid()) {
                posX += velX;
            }
            // Checks if running into Orange Portal from Left
            Point inFront = new Point(posX + SIZE + MARGIN, posY + SIZE/2);
            if(!inAir() && inFront.getX() >= Portal.DrawOrangeBegin.getX() && inFront.getX() <= Portal.DrawOrangeBegin.getX() + PORTAL_MARGIN && inFront.getY() > Portal.DrawOrangeBegin.getY() && inFront.getY() < Portal.DrawOrangeEnd.getY()) {

                teleport('B');
            }
            // Checks if running into Blue Portal from Left
            if(!inAir() && inFront.getX() >= Portal.DrawBlueBegin.getX() && inFront.getX() <= Portal.DrawBlueBegin.getX() + PORTAL_MARGIN && inFront.getY() > Portal.DrawBlueBegin.getY() && inFront.getY() < Portal.DrawBlueEnd.getY()) {
                teleport('O');
            }
        } else if (velX < 0) {
            // Checks if block is going out of map or if the bottom left corner of cube is hitting a solid block
            if (getCurrBlock(map, new Point((int) (posX + velX + MARGIN), (int) posY + SIZE)) != null && !getCurrBlock(map, new Point((int) (posX + velX + MARGIN),posY + SIZE)).isSolid()) {
                posX += velX;
            }
            // Checks if running into Orange Portal from Right
            Point inFront = new Point(posX - MARGIN, posY + SIZE/2);
            if(!inAir() && inFront.getX() <= Portal.DrawOrangeBegin.getX() && inFront.getX() >= Portal.DrawOrangeBegin.getX() - PORTAL_MARGIN && inFront.getY() > Portal.DrawOrangeBegin.getY() && inFront.getY() < Portal.DrawOrangeEnd.getY()) {
                teleport('B');
            }
            // Checks if running into Blue Portal from Right
            if(!inAir() && inFront.getX() <= Portal.DrawBlueBegin.getX() && inFront.getX() >= Portal.DrawBlueBegin.getX() - PORTAL_MARGIN && inFront.getY() > Portal.DrawBlueBegin.getY() && inFront.getY() < Portal.DrawBlueEnd.getY()) {
                teleport('O');
            }
        }

        // Executes Physics if block is in the air
        if(inAir()) {
            // Check if block is hitting a block above
            if(velY < 0) {
                // MARGIN could cause problems in the future : (
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
            if(!getCurrBlock(map, new Point(posX, (int) (posY + SIZE + velY + MARGIN))).getColor().equals(Color.white) || !getCurrBlock(map, new Point(posX + SIZE, (int) (posY + SIZE + velY + MARGIN))).getColor().equals(Color.white)){
                    Point gap = new Point(posX,  (posY + SIZE + MARGIN));
                    Point bottom = getCurrBlock(map, new Point(posX, (int) (posY + SIZE + velY + MARGIN))).getPixelCoords();
                    posY += bottom.getY() - gap.getY() + MARGIN;
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
                    if(Portal.currOrangeSide == Side.LEFT) {
                        rotate(Math.PI * 1.5);
                    } else if(Portal.currOrangeSide == Side.RIGHT) {
                        rotate(Math.PI * .5);
                    } else if(Portal.currOrangeSide == Side.BOTTOM) {
                        rotate(Math.PI);
                    }
                    posX = (int) Portal.DrawBlueBegin.getX();
                    posY = (int) Portal.DrawBlueBegin.getY() - SIZE - MARGIN;
                } else if (Portal.currBlueSide == Side.BOTTOM) {
                    if(Portal.currOrangeSide == Side.LEFT) {
                        rotate(Math.PI * .5);
                    } else if(Portal.currOrangeSide == Side.RIGHT) {
                        rotate(Math.PI * 1.5);
                    } else if(Portal.currOrangeSide == Side.TOP) {
                        rotate(Math.PI);
                    }
                    posX = (int) Portal.DrawBlueBegin.getX();
                    posY = (int) Portal.DrawBlueBegin.getY() + MARGIN;
                } else if(Portal.currBlueSide == Side.RIGHT) {
                    if(Portal.currOrangeSide == Side.LEFT) {
                        rotate(Math.PI);
                    } else if(Portal.currOrangeSide == Side.BOTTOM) {
                        rotate(Math.PI * .5);
                    } else if(Portal.currOrangeSide == Side.TOP) {
                        rotate(Math.PI * 1.5);
                    }
                    posX = (int) Portal.DrawBlueBegin.getX() + MARGIN;
                    posY = (int) Portal.DrawBlueBegin.getY();
                } else if(Portal.currBlueSide == Side.LEFT) {
                    if(Portal.currOrangeSide == Side.RIGHT) {
                        rotate(Math.PI);
                    } else if(Portal.currOrangeSide == Side.BOTTOM) {
                        rotate(Math.PI * 1.5);
                    } else if(Portal.currOrangeSide == Side.TOP) {
                        rotate(Math.PI * .5);
                    }
                    posX = (int) Portal.DrawBlueBegin.getX() - SIZE - MARGIN;
                    posY = (int) Portal.DrawBlueBegin.getY();
                }
            }
        } else if(portal == 'O') {
            if(Portal.currOrangeSide == Side.TOP) {
                if(Portal.currBlueSide == Side.LEFT) {
                    rotate(Math.PI * 1.5);
                } else if(Portal.currBlueSide == Side.RIGHT) {
                    rotate(Math.PI * .5);
                } else if(Portal.currBlueSide == Side.BOTTOM) {
                    rotate(Math.PI);
                }
                posX = (int) Portal.DrawOrangeBegin.getX();
                posY = (int) Portal.DrawOrangeBegin.getY() - SIZE - MARGIN;
            } else if (Portal.currOrangeSide == Side.BOTTOM) {
                if(Portal.currBlueSide == Side.LEFT) {
                    rotate(Math.PI * .5);
                } else if(Portal.currBlueSide == Side.RIGHT) {
                    rotate(Math.PI * 1.5);
                } else if(Portal.currBlueSide == Side.TOP) {
                    rotate(Math.PI);
                }
                posX = (int) Portal.DrawOrangeBegin.getX();
                posY = (int) Portal.DrawOrangeBegin.getY() + MARGIN;
            } else if(Portal.currOrangeSide == Side.RIGHT) {
                if(Portal.currBlueSide == Side.LEFT) {
                    rotate(Math.PI);
                } else if(Portal.currBlueSide == Side.BOTTOM) {
                    rotate(Math.PI * .5);
                } else if(Portal.currBlueSide == Side.TOP) {
                    rotate(Math.PI * 1.5);
                }
                posX = (int) Portal.DrawOrangeBegin.getX() + MARGIN;
                posY = (int) Portal.DrawOrangeBegin.getY();
            } else if(Portal.currOrangeSide == Side.LEFT) {
                if(Portal.currBlueSide == Side.RIGHT) {
                    rotate(Math.PI);
                } else if(Portal.currBlueSide == Side.BOTTOM) {
                    rotate(Math.PI * 1.5);
                } else if(Portal.currBlueSide == Side.TOP) {
                    rotate(Math.PI * .5);
                }
                posX = (int) Portal.DrawOrangeBegin.getX() - SIZE - MARGIN;
                posY = (int) Portal.DrawOrangeBegin.getY();
            }
        }
    }

    private void rotate(double angle) {
        double oldVelX = velX;
        double oldVelY = velY;
        velX = (oldVelX * Math.cos(angle)) + (oldVelY * Math.sin(angle));
        velY = (-1 * oldVelX * Math.sin(angle)) + (oldVelY + Math.cos(angle));
        velY *= -1;
    }

    // Checks below cube to see if the color is not white (or in space)
    private boolean inAir() {
        if(!getCurrBlock(map, new Point(posX, posY + SIZE + MARGIN)).getColor().equals(Color.white) ||
           !getCurrBlock(map, new Point(posX + SIZE, posY + SIZE + MARGIN)).getColor().equals(Color.white)) {
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
