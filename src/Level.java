import javax.swing.JFrame;
import javax.swing.JPanel;

public class Level extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public Level(String path) {
		
	}

	private static void runGame() {
		Level level = new Level("levels/level7.txt");
		JFrame frame = new JFrame("Level 7");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(level);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGame();
			}
		});
	}
}
