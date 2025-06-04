import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private ImageIcon backgroundImage;

    public Menu(JFrame frame) {
        setLayout(null);

        // Load background
        backgroundImage = new ImageIcon(getClass().getResource("/img/background.jpg"));
        Image scaledBackground = backgroundImage.getImage().getScaledInstance(1600, 1200, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledBackground));
        backgroundLabel.setBounds(0, 0, 1600, 1200);

        // Load and scale Start button
        ImageIcon startIcon = new ImageIcon(getClass().getResource("/img/start.png"));
        Image scaledStart = startIcon.getImage().getScaledInstance(512 , 512, Image.SCALE_SMOOTH);
        JButton startButton = new JButton(new ImageIcon(scaledStart));
        startButton.setBounds(650, 400, 261, 261);
        styleButton(startButton);
        add(startButton);

        // Load and scale Exit button
        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/img/exit.png"));
        Image scaledExit = exitIcon.getImage().getScaledInstance(391, 391, Image.SCALE_SMOOTH);
        JButton exitButton = new JButton(new ImageIcon(scaledExit));
        exitButton.setBounds(640, 480, 261, 261);
        styleButton(exitButton);
        add(exitButton);

        // Add event listeners
        startButton.addActionListener(e -> {
            GameLogic logic = new GameLogic();
            logic.startNewRound();
            GamePanel gamePanel = new GamePanel(logic);
            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.repaint();
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add background last
        add(backgroundLabel);
        setComponentZOrder(backgroundLabel, getComponentCount() - 1); // Send to back
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
