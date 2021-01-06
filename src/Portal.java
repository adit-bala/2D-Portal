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
    private int posXBlue;
    private int posYBlue;

    public Portal() {
        shootingOrange = false;
        shootingBlue = false;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMap(Block[][] map) {
        this.map = map;
    }

    private void shootOrangePortal() {
        int slope = Math.floorDiv(posXOrange - player.getPosY(), posYOrange - player.getPosX());
        System.out.println(posXOrange + ", " + posYOrange);
        /*while(!Player.getCurrBlock(map, new Point(posXOrange, posYOrange)).isSolid()) {
            posYOrange = (posXOrange*slope);
            posXOrange++;
        }*/
    }

    private void shootBluePortal() {

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
            }
        }
        if(key == MouseEvent.BUTTON3) {
            if(!shootingOrange) {
                posXOrange = e.getX();
                posYOrange = e.getY();
                shootingOrange = true;
            }
        }
    }
}
