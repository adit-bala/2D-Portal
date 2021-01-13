import java.awt.*;
import java.awt.event.MouseEvent;

public class Portal extends Thread {
    public static final int SIZE = 5;
    public static final int ARC_SIZE = 15;
    public static boolean SHOOTING_ORANGE;
    public static boolean SHOOTING_BLUE;
    public static boolean DRAW_ORANGE_PORTAL;
    public static boolean DRAW_BLUE_PORTAL;
    private final int DELAY = 15;
    private final int margin = 5;
    private Player player;
    private Block[][] map;

    // Orange Portal
    private int posXOrange;
    private int posYOrange;
    private double velXOrange;
    private double velYOrange;
    public static Point DrawOrangeBegin;
    public static Point DrawOrangeEnd;
    public static Side currOrangeSide;

    // Blue Portal
    private int posXBlue;
    private int posYBlue;
    private double velXBlue;
    private double velYBlue;
    public static Point DrawBlueBegin;
    public static Point DrawBlueEnd;
    public static Side currBlueSide;

    public Portal() {
        SHOOTING_ORANGE = false;
        SHOOTING_BLUE = false;
        velXOrange = 0;
        velYOrange = 0;
        DrawBlueBegin = new Point();
        DrawBlueEnd = new Point();
        DrawOrangeBegin = new Point();
        DrawOrangeEnd = new Point();
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMap(Block[][] map) {
        this.map = map;
    }

    private void initOrange() {
        Point currPosition = new Point(player.getPosX(), player.getPosY());
        Point mousePosition = new Point(posXOrange, posYOrange);
        double slope = 1;
        if ((mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX()) != Double.POSITIVE_INFINITY || (mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX()) != Double.NEGATIVE_INFINITY) {
            slope = (mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX());
        }
        // X increments
        if (currPosition.getX() > mousePosition.getX()) {
            velXOrange = -5;
            velYOrange = 5 * -(slope);
            System.out.println(slope);
        } else if (currPosition.getX() < mousePosition.getX()) {
            velYOrange = 5 * slope;
            velXOrange = 5;
        } else if (currPosition.getX() == mousePosition.getX()) {
            if(currPosition.getY() < mousePosition.getY()) {
                velYOrange = -1;
            } else {
                velYOrange = 1;
            }
            velXOrange = 0;
        }

        posXOrange = player.getPosX();
        posYOrange = player.getPosY();
    }

    private void initBlue() {
        Point currPosition = new Point(player.getPosX() + player.SIZE / 2, player.getPosY() + player.SIZE / 2);
        Point mousePosition = new Point(posXBlue, posYBlue);
        double slope = 1;
        if ((mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX()) != Double.POSITIVE_INFINITY || (mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX()) != Double.NEGATIVE_INFINITY) {
            slope = (mousePosition.getY() - currPosition.getY()) / (mousePosition.getX() - currPosition.getX());
        }
        // X increments
        if (currPosition.getX() > mousePosition.getX()) {
            velXBlue = -5;
            velYBlue = 5 * -(slope);
            System.out.println(slope);
        } else if (currPosition.getX() < mousePosition.getX()) {
            velYBlue = 5 * slope;
            velXBlue = 5;
        } else if (currPosition.getX() == mousePosition.getX()) {
            if(currPosition.getY() < mousePosition.getY()) {
                velYBlue = -1;
            } else {
                velYBlue = 1;
            }
            velXBlue = 0;
        }

        posXBlue = player.getPosX();
        posYBlue = player.getPosY();
    }

    private void shootOrangePortal() {
        Block currBlock = Player.getCurrBlock(map, new Point(posXOrange, posYOrange));
        if (Player.getCurrBlock(map, new Point(posXOrange, posYOrange)) != null && !Player.getCurrBlock(map, new Point(posXOrange, posYOrange)).isSolid()) {
            posXOrange += velXOrange;
            posYOrange += velYOrange;
        } else {
            this.SHOOTING_ORANGE = false;
            if (currBlock != null && currBlock.isPortal()) {
                findSide(currBlock.getPixelCoords(), new Point(posXOrange, posYOrange), 'O');
            }
        }
    }

    private void shootBluePortal() {
        Block currBlock = Player.getCurrBlock(map, new Point(posXBlue, posYBlue));
        if (!currBlock.isSolid()) {
            posXBlue += velXBlue;
            posYBlue += velYBlue;
        } else {
            this.SHOOTING_BLUE = false;
            if (currBlock.isPortal()) {
                findSide(currBlock.getPixelCoords(), new Point(posXBlue, posYBlue), 'B');
            }
        }
    }

    private void findSide(Point currBlock, Point point, Character color) {
        //System.out.println(point.getX() + ", " + currBlock.getX());
        if (color == 'O') {
            if (currBlock.getY() + margin == point.getY()) {
                drawOrangePortal(currBlock, Side.TOP);
            } else if (currBlock.getY() + Block.SIZE == point.getY()) {
                drawOrangePortal(currBlock, Side.BOTTOM);
            } else if (currBlock.getX() + margin >= point.getX()) {
                drawOrangePortal(currBlock, Side.LEFT);
            } else if (currBlock.getX() + Block.SIZE + margin >= point.getX()) {
                drawOrangePortal(currBlock, Side.RIGHT);
            }
        } else {
            if (currBlock.getY() + margin == point.getY()) {
                drawBluePortal(currBlock, Side.TOP);
            } else if (currBlock.getY() + Block.SIZE == point.getY()) {
                drawBluePortal(currBlock, Side.BOTTOM);
            } else if (currBlock.getX() + margin >= point.getX()) {
                drawBluePortal(currBlock, Side.LEFT);
            } else if (currBlock.getX() + Block.SIZE + margin >= point.getX()) {
                drawBluePortal(currBlock, Side.RIGHT);
            }
        }

    }

    private void drawOrangePortal(Point currBlock, Side side) {
        if (side == Side.TOP) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawOrangeEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
        } else if (side == Side.BOTTOM) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
            DrawOrangeEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.LEFT) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawOrangeEnd = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.RIGHT) {
            DrawOrangeBegin = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
            DrawOrangeEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        }
        if (!(DrawOrangeBegin.getX() == DrawBlueBegin.getX() && DrawOrangeBegin.getY() == DrawBlueBegin.getY())) {
            this.DRAW_ORANGE_PORTAL = true;
            currOrangeSide = side;
        } else {
            this.DRAW_ORANGE_PORTAL = false;
            DrawOrangeBegin.setLocation(0, 0);
            DrawOrangeEnd.setLocation(0, 0);
        }
    }

    private void drawBluePortal(Point currBlock, Side side) {
        if (side == Side.TOP) {
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawBlueEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
        } else if (side == Side.BOTTOM) {
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
            DrawBlueEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.LEFT) {
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawBlueEnd = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.RIGHT) {
            DrawBlueBegin = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
            DrawBlueEnd = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        }

        if (!(DrawBlueBegin.getX() == DrawOrangeBegin.getX() && DrawBlueBegin.getY() == DrawOrangeBegin.getY())) {
            this.DRAW_BLUE_PORTAL = true;
            currBlueSide = side;
        } else {
            this.DRAW_BLUE_PORTAL = false;
            DrawBlueBegin.setLocation(0, 0);
            DrawBlueEnd.setLocation(0, 0);
        }

    }

    private void cycle() {
        if (Portal.SHOOTING_ORANGE) {
            shootOrangePortal();
        } else if (Portal.SHOOTING_BLUE) {
            shootBluePortal();
        }
    }

    public void mouseClicked(MouseEvent e) {
        int key = e.getButton();
        if (key == MouseEvent.BUTTON1) {
            if (!SHOOTING_BLUE) {
                posXBlue = e.getX();
                posYBlue = e.getY();
                SHOOTING_BLUE = true;
                initBlue();
            }
        }
        if (key == MouseEvent.BUTTON3) {
            if (!SHOOTING_ORANGE) {
                posXOrange = e.getX();
                posYOrange = e.getY();
                SHOOTING_ORANGE = true;
                initOrange();
            }
        }
    }


    public int getPosXOrange() {
        return posXOrange;
    }

    public int getPosXBlue() {
        return posXBlue;
    }

    public int getPosYOrange() {
        return posYOrange;
    }

    public int getPosYBlue() {
        return posYBlue;
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                System.out.println("ERROR WITH THREAD");
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}
