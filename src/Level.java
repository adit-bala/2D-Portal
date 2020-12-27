import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Level extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int FRAME_WIDTH;
	private int FRAME_HEIGHT;
	private final int DELAY = 10;
	private Player player;
	private Block[][] map;
	private Thread animator;


	
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
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setFocusable(true);



		addKeyListener(new TAdapter());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMap(g);
		drawPlayer(g);
	}

	private void drawMap(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				Block block = map[i][j];
				g2.setColor(block.getColor());
				g2.fillRect(j * Block.SIZE, i * Block.SIZE, Block.SIZE, Block.SIZE);
			}
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawPlayer(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.orange);
		g2d.fillRect(player.getPosX(), player.getPosY(), player.SIZE, player.SIZE);

	}


	private void step() {

		player.move(map);

		repaint(player.getPosX()-1, player.getPosY()-1,
				player.SIZE+2, player.SIZE+2);
	}

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

				JOptionPane.showMessageDialog(this, msg, "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			beforeTime = System.currentTimeMillis();
		}
	}
}

