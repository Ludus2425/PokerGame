import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private final Player player;
    private JLabel chipLabel;
    private JPanel cardPanel;
    private boolean revealCards = false; // controls if cards are shown

    public PlayerPanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout());
        setOpaque(false);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        topPanel.add(nameLabel, BorderLayout.NORTH);

        chipLabel = new JLabel("Chips: " + player.getChips());
        chipLabel.setForeground(Color.WHITE);
        chipLabel.setFont(new Font("Serif", Font.BOLD, 20));
        chipLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(chipLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        cardPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        cardPanel.setOpaque(false);
        add(cardPanel, BorderLayout.CENTER);
        
    }

    public void updateCardDisplay() {
        cardPanel.removeAll();
for (Card card : player.getHand()) {
    JLabel label = new JLabel();
    if (!(player instanceof Bots) || player.isRevealCards()) {
        ImageIcon icon = card.getImageIcon();
        Image scaled = icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaled));
    } else {
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/img/back.png"));
        Image scaledBack = backIcon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledBack));
    }
    cardPanel.add(label);
}
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void updateDisplay() {
        chipLabel.setText("Chips: " + player.getChips());
        updateCardDisplay();
    }

    public void setRevealCards(boolean reveal) {
        this.revealCards = reveal;
        updateCardDisplay();
    }
}
