import javax.swing.*;
import java.awt.*;

public class ThreadAnimation extends JFrame {

	public static JFrame level;
	private int x;

	public ThreadAnimation(int x) {
		initUI(x);
	}

	private void initUI(int x) {
		add(new Level("stages/level" + x +".txt", x));
		setResizable(false);
		pack();
		setTitle("Portal");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			level = new ThreadAnimation(6);
			level.setVisible(true);
		});
	}
}