import java.awt.*;
import java.awt.event.MouseEvent;

public class Portal extends Thread {
    public static final int SIZE = 5;
    public static final int ARC_SIZE = 15;
    public static boolean shootingOrange;
    public static boolean shootingBlue;
    public static boolean drawOrangePortal;
    public static boolean drawBluePortal;
    private final int DELAY = 15;
    private final int margin = 5;
    private Player player;
    private Block[][] map;

    // Orange Portal
    private int posXOrange;
    private int posYOrange;
    private int velXOrange;
    private int velYOrange;
    public static Point DrawOrangeBegin;
    public static Point DrawOrangeEnd;

    // Blue Portal
    private int posXBlue;
    private int posYBlue;
    private int velXBlue;
    private int velYBlue;
    public static Point DrawBlueBegin;
    public static Point DrawBlueEnd;


    enum Side {
        TOP, BOTTOM, LEFT, RIGHT;
    }

    public Portal() {
        shootingOrange = false;
        shootingBlue = false;
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

        // X increments
        if (currPosition.getX() > mousePosition.getX()) {
            velXOrange = -5;
        } else if (currPosition.getX() < mousePosition.getX()) {
            velXOrange = 5;
        } else if(currPosition.getX() == mousePosition.getX()) {
            velXOrange = 0;
        }

        // Y increments
        if (currPosition.getY() > mousePosition.getY()) {
            velYOrange = -5;
        } else if (currPosition.getY() < mousePosition.getY()) {
            velYOrange = 5;
        } else if(currPosition.getY() == mousePosition.getY()) {
            velYOrange = 0;
        }

        posXOrange = player.getPosX();
        posYOrange = player.getPosY();
    }

    private void initBlue() {
        Point currPosition = new Point(player.getPosX() + player.SIZE/2, player.getPosY() + player.SIZE/2);
        Point mousePosition = new Point(posXBlue, posYBlue);

        // X increments
        if (currPosition.getX() > mousePosition.getX()) {
            velXBlue = -5;
        } else if (currPosition.getX() < mousePosition.getX()) {
            velXBlue = 5;
        } else if(currPosition.getX() == mousePosition.getX()) {
            velXBlue = 0;
        }

        // Y increments
        if (currPosition.getY() > mousePosition.getY()) {
            velYBlue = -5;
        } else if (currPosition.getY() < mousePosition.getY()) {
            velYBlue = 5;
        } else if(currPosition.getY() == mousePosition.getY()) {
            velYBlue = 0;
        }

        posXBlue = player.getPosX();
        posYBlue = player.getPosY();
    }

    private void shootOrangePortal() {
        Block currBlock = Player.getCurrBlock(map, new Point(posXOrange, posYOrange));
        if(!Player.getCurrBlock(map, new Point(posXOrange, posYOrange)).isSolid()) {
            posXOrange += velXOrange;
            posYOrange += velYOrange;
        } else {
            this.shootingOrange = false;
            if(currBlock.isPortal()) {
                findSide(currBlock.getPixelCoords(), new Point(posXOrange, posYOrange), "O");
            }
        }
    }

    private void shootBluePortal() {
        Block currBlock = Player.getCurrBlock(map, new Point(posXBlue, posYBlue));
        if(!currBlock.isSolid()) {
            posXBlue += velXBlue;
            posYBlue += velYBlue;
        } else {
            this.shootingBlue = false;
            if(currBlock.isPortal()) {
                findSide(currBlock.getPixelCoords(), new Point(posXBlue, posYBlue), "B");
            }
        }
    }

    private void findSide(Point currBlock, Point point, String color) {
        System.out.println(point.getX() + ", " + currBlock.getX());
        if(color.equals("O")){
            if(currBlock.getY() + margin == point.getY()) {
                drawOrangePortal(currBlock, Side.TOP);
            } else if (currBlock.getY() + Block.SIZE == point.getY()) {
                drawOrangePortal(currBlock, Side.BOTTOM);
            } else if (currBlock.getX() + margin >= point.getX()) {
                drawOrangePortal(currBlock, Side.LEFT);
            } else if (currBlock.getX() + Block.SIZE + margin >= point.getX()){
                drawOrangePortal(currBlock, Side.RIGHT);
            }
        } else {
            if(currBlock.getY() + margin == point.getY()) {
                drawBluePortal(currBlock, Side.TOP);
            } else if (currBlock.getY() + Block.SIZE == point.getY()) {
                drawBluePortal(currBlock, Side.BOTTOM);
            } else if (currBlock.getX() + margin >= point.getX()) {
                drawBluePortal(currBlock, Side.LEFT);
            } else if (currBlock.getX() + Block.SIZE + margin >= point.getX()){
                drawBluePortal(currBlock, Side.RIGHT);
            }
        }

    }

    private void drawOrangePortal(Point currBlock, Side side) {
        if (side == Side.TOP) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawOrangeEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
        } else if (side == Side.BOTTOM) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
            DrawOrangeEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY()+ Block.SIZE);
        } else if (side == Side.LEFT) {
            DrawOrangeBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawOrangeEnd =  new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.RIGHT){
            DrawOrangeBegin = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
            DrawOrangeEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        }
        this.drawOrangePortal = true;
    }

    private void drawBluePortal(Point currBlock, Side side) {
        if (side == Side.TOP) {
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawBlueEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
        } else if (side == Side.BOTTOM) {
            System.out.println("check");
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
            DrawBlueEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY()+ Block.SIZE);
        } else if (side == Side.LEFT) {
            DrawBlueBegin = new Point((int) currBlock.getX(), (int) currBlock.getY());
            DrawBlueEnd =  new Point((int) currBlock.getX(), (int) currBlock.getY() + Block.SIZE);
        } else if (side == Side.RIGHT){
            DrawBlueBegin = new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY());
            DrawBlueEnd =  new Point((int) currBlock.getX() + Block.SIZE, (int) currBlock.getY() + Block.SIZE);
        }
        this.drawBluePortal = true;

    }

    private void cycle() {
        if(Portal.shootingOrange) {
            shootOrangePortal();
        } else if (Portal.shootingBlue) {
            shootBluePortal();
        }
    }

    public void mouseClicked(MouseEvent e) {
        int key = e.getButton();
        if(key == MouseEvent.BUTTON1) {
            if(!shootingBlue) {
                posXBlue = e.getX();
                posYBlue = e.getY();
                shootingBlue = true;
                initBlue();
            }
        }
        if(key == MouseEvent.BUTTON3) {
            if(!shootingOrange) {
                posXOrange = e.getX();
                posYOrange = e.getY();
                shootingOrange = true;
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
