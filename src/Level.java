import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;
    private int FRAME_WIDTH;
    private int FRAME_HEIGHT;
    private final int DELAY = 15;
    private Player player;
    private Block[][] map;
    private ArrayList<PortalWall> walls = new ArrayList<PortalWall>();
    private Thread animator;
    private int currLevel;

    public Level(String path, int currLevel) {
        this.currLevel = currLevel;
        initUI();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            map = new Block[lines.size()][lines.get(0).length()];
            FRAME_WIDTH = lines.get(0).length() * Block.SIZE;
            FRAME_HEIGHT = lines.size() * Block.SIZE;
            for (int i = 0; i < lines.size(); i++) {
                char[] row = lines.get(i).toCharArray();
                for (int j = 0; j < row.length; j++) {
                    char block = row[j];
                    if (block == '.') {
                        map[i][j] = new SpaceBlock(j, i);
                    } else if (block == 'S') {
                        map[i][j] = new SolidBlock(j, i);
                    } else if (block == 'P') {
                        map[i][j] = new PortalBlock(j, i);
                        for (PortalWall wall : map[i][j].getWalls()) {
                            walls.add(wall);
                        }
                    } else if (block == 'O') {
                        map[i][j] = new PlayerBlock(j, i);
                        player = new Player(j * Block.SIZE, i * Block.SIZE + 20, this);
                    } else if (block == 'X') {
                        map[i][j] = new SpikeBlock(j, i);
                    } else if (block == 'L') {
                        map[i][j] = new LevelBlock(j, i);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setMap(map, walls);
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setFocusable(true);
        addKeyListener(new KeyHandler());
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }


    private void initUI() {
        if (currLevel == 1) {
            JOptionPane.showMessageDialog(this, "Welcome to Portal! I hope you enjoy your stay! " +
                    "Movement is simple. Use W A S D to move and Left / Right Click to Shoot Portals. The rest is for you to explore!");
        }
    }

    public void nextLevel() {
        try {
            animator.interrupt();
            animator.join(0);
        } catch (InterruptedException e) {

        }
        //ThreadAnimation.level = new ThreadAnimation(currLevel + 1);
        ThreadAnimation.level.setContentPane(new Level("stages/level" + (currLevel + 1) + ".txt", currLevel + 1));
        ThreadAnimation.level.validate();


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayer(g);
        drawMap(g);
        this.requestFocusInWindow();
    }

    private void drawMap(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Block block = map[i][j];
                g2.setColor(block.getColor());
                if (!(block instanceof SpaceBlock) && !(block instanceof PlayerBlock)) {
                    g2.fillRect(j * Block.SIZE, i * Block.SIZE, Block.SIZE, Block.SIZE);
                }
            }
        }
        for (PortalWall wall : walls) {
            g2.setColor(wall.getColor());
            g2.drawLine(wall.getA().x, wall.getA().y, wall.getB().x, wall.getB().y);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPlayer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.lightGray);
        g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        g2d.setColor(Color.green);
        g2d.fillRect((int) player.getXPos(), (int) player.getYPos(), player.SIZE, player.SIZE);
    }

    private void step() {
        player.move();
        repaint((int) player.getXPos(), (int) player.getYPos(), player.SIZE, player.SIZE);
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }

    private class MouseHandler implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            player.mouseClicked(e);
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }
    }

    private class MouseMotionHandler implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            player.mouseMoved(e);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    private void cycle() {
        step();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true) {
            cycle();
            repaint();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread interrupted: %s", e.getMessage());
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}