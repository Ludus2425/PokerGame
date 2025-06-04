import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Poker Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 1200);
            frame.setLocationRelativeTo(null);

            Menu menuPanel = new Menu(frame);
            frame.setContentPane(menuPanel);
            frame.setVisible(true);
        });
    }
}