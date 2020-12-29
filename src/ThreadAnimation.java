import javax.swing.*;
import java.awt.*;

public class ThreadAnimation extends JFrame {

    public static JFrame level;

    public ThreadAnimation() {

        initUI();
    }

    private void initUI() {

        add(new Level("stages/level1.txt"));

        setResizable(false);
        pack();

        setTitle("Portal");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            level = new ThreadAnimation();

            level.setVisible(true);
        });
    }
}
