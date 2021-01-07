import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Portal extends Thread {
    public static final int SIZE = 5;
    public static final int ARC_SIZE = 15;
    public static boolean shootingOrange;
    public static boolean shootingBlue;
    private final int DELAY = 25;
    private Player player;
    private Block[][] map;

    private int posXOrange;
    private int posYOrange;
    private int velXOrange;
    private int velYOrange;

    private int posXBlue;
    private int posYBlue;
    private int velXBlue;
    private int velYBlue;

    public Portal() {
        shootingOrange = false;
        shootingBlue = false;
        velXOrange = 0;
        velYOrange = 0;
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
        if(!Player.getCurrBlock(map, new Point(posXOrange, posYOrange)).isSolid()) {
            posXOrange += velXOrange;
            posYOrange += velYOrange;
        } else {
            this.shootingOrange = false;
        }
    }

    private void shootBluePortal() {
        if(!Player.getCurrBlock(map, new Point(posXBlue, posYBlue)).isSolid()) {
            posXBlue += velXBlue;
            posYBlue += velYBlue;
        } else {
            this.shootingBlue = false;
        }
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
