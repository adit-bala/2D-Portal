import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Level extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int FRAME_WIDTH;
	private int FRAME_HEIGHT;
	private final int INITIAL_X = -40;
	private final int INITIAL_Y = -40;
	private final int DELAY = 25;
	private Block[][] map;

	private Thread animator;
	private int x, y;
	
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
						map[i][j] = new SpaceBlock();
					} else if (block == 'S') {
						map[i][j] = new SolidBlock();
					} else if (block == 'P') {
						map[i][j] = new PortalBlock();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
	}

	@Override
	public void addNotify() {
		super.addNotify();

		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMap(g);
	}

	private void drawMap(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				Block block = map[i][j];
				g2.setColor(block.getColor());
				g2.fillRect(j * Block.SIZE, i * Block.SIZE, Block.SIZE, Block.SIZE);
			}
		}
		g.drawString("hello", x, y);
		Toolkit.getDefaultToolkit().sync();
	}

	private void cycle() {

		x += 1;
		y += 1;

		if (y > FRAME_HEIGHT) {

			y = INITIAL_Y;
			x = INITIAL_X;
		}
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

