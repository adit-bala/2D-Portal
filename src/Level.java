import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
/*
Main class that stores every level and the thread
 */
public class Level extends JPanel implements Runnable {
	//Java recommended this lel
	private static final long serialVersionUID = 1L;
	private int FRAME_WIDTH;
	private int FRAME_HEIGHT;
	//Delay in ms before each iteration of the level
	private final int DELAY = 15;
	private Player player;
	private Block[][] map;
	//Thread used to animate
	private Thread level;
	private Portal portal;


	// constructs the map based on the text file and adds Key Adapters
	/* TODO: - Add Laser Block
	 * 		 - Add Lever Block
	 * 		 - Add Picking up Cube
	 */

	public Level(String path) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(path));
			map = new Block[lines.size()][lines.get(0).length()];
			FRAME_WIDTH = lines.get(0).length()*Block.SIZE;
			FRAME_HEIGHT = lines.size()*Block.SIZE;
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
					} else if (block == 'O') {
						map[i][j] = new PlayerBlock(j, i);
						player = new Player(j*Block.SIZE, i*Block.SIZE+20);

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.setMap(map);

		portal = new Portal();
		portal.start();
		portal.setPlayer(player);
		portal.setMap(map);
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setFocusable(true);


		this.addKeyListener(new MoveAdapter());
		this.addMouseListener(new PortalAdapter());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMap(g);
		drawPlayer(g);
		drawPortalMovement(g);
		drawPortals(g);
	}

	// Draws the map every frame
	private void drawMap(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				Block block = map[i][j];
				g2.setColor(block.getColor());
				g2.fillRect(j * Block.SIZE, i * Block.SIZE, Block.SIZE, Block.SIZE);
			}
		}
		// So Java does not crash :`)
		Toolkit.getDefaultToolkit().sync();
	}

	// Draws Player
	private void drawPlayer(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(56, 235, 190));
		g2d.fillRect(player.getPosX(), player.getPosY(), player.SIZE, player.SIZE);
	}

	private void drawPortalMovement(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if(Portal.shootingOrange) {
			g2d.setColor(Color.orange);
			g2d.fillRoundRect(portal.getPosXOrange(), portal.getPosYOrange(), portal.SIZE, portal.SIZE, portal.ARC_SIZE, portal.ARC_SIZE);
		}
		if (Portal.shootingBlue) {
			g2d.setColor(Color.blue);
			g2d.fillRoundRect(portal.getPosXBlue(), portal.getPosYBlue(), portal.SIZE, portal.SIZE, portal.ARC_SIZE, portal.ARC_SIZE);
		}
	}

	private void drawPortals(Graphics g) {
		if (Portal.drawOrangePortal) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(255, 150, 38));
			g2.setStroke(new BasicStroke(2));
			g2.drawLine( (int) Portal.DrawOrangeBegin.getX(), (int) Portal.DrawOrangeBegin.getY(), (int) Portal.DrawOrangeEnd.getX(), (int) Portal.DrawOrangeEnd.getY());
		}
		if (Portal.drawBluePortal) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.blue);
			g2.drawLine( (int) Portal.DrawBlueBegin.getX(), (int) Portal.DrawBlueBegin.getY(), (int) Portal.DrawBlueEnd.getX(), (int) Portal.DrawBlueEnd.getY());
		}
	}

	// Called every delay
	private void step() {
		player.move();

		// Small optimization to paint around Player
		repaint(player.getPosX()-1, player.getPosY()-1,
				player.SIZE+2, player.SIZE+2);
		if(Portal.shootingOrange) {
			repaint(portal.getPosXOrange()-1, portal.getPosYOrange()-1,
					portal.SIZE+2, portal.SIZE+2);
		} else if (Portal.shootingBlue) {
			repaint(portal.getPosXBlue()-1, portal.getPosYBlue()-1,
					portal.SIZE+2, portal.SIZE+2);
		}

	}

	// Class to use Player's KeyAdapter
	private class MoveAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
	}

	private class PortalAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			portal.mouseClicked(e);
		}
	}



	// Thread tings
	@Override
	public void addNotify() {
		super.addNotify();

		level = new Thread(this);
		level.start();


	}

	// Thread calls this method every delay
	private void cycle() {
		step();
	}

	// Running Application
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

				JOptionPane.showMessageDialog(this, msg, "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			beforeTime = System.currentTimeMillis();
		}
	}
}

