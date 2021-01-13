import javax.swing.*;
import java.awt.*;
/*
This class initializes the JFrame
 */
public class Main extends JFrame {

    public static JFrame level;

    public Main() {

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
            level = new Main();
            level.setVisible(true);
        });
    }
}
