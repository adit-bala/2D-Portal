import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
	private Thread animator;


	// constructs the map based on the text file and adds Key Adapters
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
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setFocusable(true);


		this.addKeyListener(new TAdapter());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMap(g);
		drawPlayer(g);
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
		g2d.setColor(Color.orange);
		g2d.fillRect(player.getPosX(), player.getPosY(), player.SIZE, player.SIZE);

	}

	// Called every delay
	private void step() {
		player.move();

		// Small optimization to paint around Player
		repaint(player.getPosX()-1, player.getPosY()-1,
				player.SIZE+2, player.SIZE+2);
	}

	// Class to use Player's KeyAdapter
	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
	}

	// Thread tings
	@Override
	public void addNotify() {
		super.addNotify();

		animator = new Thread(this);
		animator.start();
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

