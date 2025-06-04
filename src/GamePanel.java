import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePanel extends JPanel {
    private List<PlayerPanel> playerPanels = new ArrayList<>();
    private JTextArea logArea;
    private GameLogic gameLogic;
    private JPanel communityPanel;
    private JLabel potLabel;
    private int stage = 0; // 0: Pre-flop, 1: Flop, 2: Turn, 3: River, 4: Showdown
    private JButton betBtn;
    private JButton callBtn;
    private JButton foldBtn;
    private Image mainImage;

    public GamePanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.mainImage = new ImageIcon(getClass().getResource("/img/main.jpg")).getImage()
        .getScaledInstance(1600, 1200, Image.SCALE_SMOOTH);
        //setLayout(new BorderLayout());
        setOpaque(false);
        List<Player> players = gameLogic.getPlayers();
        setLayout(new BorderLayout());
        potLabel = new JLabel("Pot: 0");
        potLabel.setFont(new Font("Serif", Font.BOLD, 24));
        potLabel.setForeground(Color.WHITE);
        add(potLabel, BorderLayout.EAST);
        communityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 14));
        add(communityPanel, BorderLayout.NORTH);
        JPanel tablePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        for (Player player : players) {
            PlayerPanel panel = new PlayerPanel(player);
            playerPanels.add(panel);
            tablePanel.add(panel);
            communityPanel.setOpaque(false);
            tablePanel.setOpaque(false);
        }

        add(tablePanel, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        startNewGameFlow();
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        betBtn = new JButton("Bet/Raise");
        callBtn = new JButton("Check");
        foldBtn = new JButton("Fold");

        betBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter bet amount:", "Bet", JOptionPane.PLAIN_MESSAGE);
            try {
                int amount = Integer.parseInt(input);
                Player you = gameLogic.getPlayers().get(0);
                if (amount <= 0 || amount > you.getChips()) {
                    log("Invalid bet amount.");
                    return;
                }
                you.bet(amount);
                gameLogic.setCurrentBet(you.getCurrentBet());
                gameLogic.addToPot(amount);
                log("You bet: " + amount);
                updatePot();
                updateGameState();
            } catch (Exception ex) {
                log("Invalid bet amount.");
            }
        });

        callBtn.addActionListener(e -> {
            Player you = gameLogic.getPlayers().get(0);
            int toCall = gameLogic.getCurrentBet() - you.getCurrentBet();
            you.bet(toCall);
            gameLogic.addToPot(toCall);
            updatePot();
            log("You call: " + toCall);
            updateGameState();
        });

        foldBtn.addActionListener(e -> {
            gameLogic.getPlayers().get(0).fold();
            log("You fold.");
            betBtn.setEnabled(false);
            callBtn.setEnabled(false);
            foldBtn.setEnabled(false);
            updateGameState();
        });

        controlPanel.add(betBtn);
        controlPanel.add(callBtn);
        controlPanel.add(foldBtn);

        logArea = new JTextArea(4, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        controlPanel.add(scrollPane);

        return controlPanel;
    }

    public void log(String message) {
        logArea.append(message + "\n");
    }

    public void startNewGameFlow() {
        stage = 0;
        gameLogic.hideAllHands();
        gameLogic.startNewRound();
        betBtn.setEnabled(true);
        callBtn.setEnabled(true);
        foldBtn.setEnabled(true);
        updatePlayers();
        updateCommunityCards();
        log("New round started. Place your bet!");
    }

public void updateGameState() {
    // Delay for realism
    new Timer(500, e -> {
        ((Timer) e.getSource()).stop();

        // Keep letting bots act until the round is over
        boolean botsActed;
        do {
            botsActed = false;
            for (Player player : gameLogic.getPlayers()) {
                if (player instanceof Bots bot && !bot.hasFolded() && bot.getChips() > 0) {
                    int prevBet = bot.getCurrentBet();
                    bot.takeAction(gameLogic.getCurrentBet(), gameLogic.getCommunityCards());
                    if (bot.getCurrentBet() != prevBet) {
                        botsActed = true;
                    }
                }
            }
            updatePlayers();
            updateCommunityCards();
        } while (!gameLogic.isRoundOver() && botsActed);
         int activePlayers = 0;
        Player lastActive = null;
        for (Player p : gameLogic.getPlayers()) {
            if (!p.hasFolded()) {
                activePlayers++;
                lastActive = p;
            }
        }
        if (activePlayers == 1) {
            lastActive.winPot(gameLogic.getPot());
            gameLogic.resetPot();
            updatePot();
            updatePlayers();
            log("Winner by default: " + lastActive.getName());
            JOptionPane.showMessageDialog(this, "Winner by default: " + lastActive.getName());
            new Timer(3000, evt -> {
                ((Timer) evt.getSource()).stop();
                startNewGameFlow();
            }).start();
            return;
        }
        if (gameLogic.isRoundOver()) {
            advanceGameFlow();
        }
    }).start();
}

    private void advanceGameFlow() {
        stage++;
        if (stage == 1) {
            // Deal flop
            gameLogic.dealFlop();
            log("Flop dealt.");
            updateCommunityCards();
            resetBets();
        } else if (stage == 2) {
            // Deal turn
            gameLogic.dealTurn();
            log("Turn dealt.");
            updateCommunityCards();
            resetBets();
        } else if (stage == 3) {
            // Deal river
            gameLogic.dealRiver();
            log("River dealt.");
            updateCommunityCards();
            resetBets();
        } else {
            // Showdown
            gameLogic.revealAllHands();
            updatePlayers();
            Map<String, List<Card>> playerHands = new HashMap<>();
            for (Player p : gameLogic.getPlayers()) {
                if(!p.hasFolded()){
                    List<Card> fullHand = new ArrayList<>(p.getHand());
                    fullHand.addAll(gameLogic.getCommunityCards());
                    playerHands.put(p.getName(), fullHand);
                }
            
        }
        List<String> winners = Winner.determineWinners(playerHands);
        int share = gameLogic.getPot() / winners.size();
        for (Player p : gameLogic.getPlayers()) {
        if (winners.contains(p.getName())) {
            p.winPot(share);
        }
        }
        gameLogic.resetPot();
        updatePot();
        updatePlayers();

        log("Winner(s): " + String.join(", ", winners));
        JOptionPane.showMessageDialog(this, "Winner(s): " + String.join(", ", winners));
        gameLogic.getPlayers().removeIf(p -> p.getChips() <= 0);
        //Game Over condition
        if (gameLogic.getPlayers().size() == 1) {
            Player winner = gameLogic.getPlayers().get(0);
            log("Game Over! " + winner.getName() + " wins the game!");
            JOptionPane.showMessageDialog(this, "Game Over! " + winner.getName() + " wins the game!");
            betBtn.setEnabled(false);
            callBtn.setEnabled(false);
            foldBtn.setEnabled(false);
            return;
        }
            // Start new round after short delay
            new Timer(3000, e -> {
                ((Timer) e.getSource()).stop();
                startNewGameFlow();
            }).start();
            return;
        }
        // After dealing new community cards, let bots act again
        updatePlayers();
        updateCommunityCards();
        log("Next betting round. Place your bet!");
        if(gameLogic.getPlayers().get(0).hasFolded()){
            updateGameState();
        }
        
    }

    private void resetBets() {
        for (Player p : gameLogic.getPlayers()) {
            p.resetBet();
        }
        gameLogic.setCurrentBet(0);
    }

    public void updatePlayers() {
        for (PlayerPanel panel : playerPanels) {
            panel.updateDisplay();
        }
    }

    public void updateCommunityCards() {
        communityPanel.removeAll();
        for (Card card : gameLogic.getCommunityCards()) {
            ImageIcon icon = card.getImageIcon();
            Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            communityPanel.add(label);
        }
        communityPanel.revalidate();
        communityPanel.repaint();
    }
    public void updatePot() {
    potLabel.setText("Pot: " + gameLogic.getPot());
}
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (mainImage != null) {
        g.drawImage(mainImage, 0, 0, getWidth(), getHeight(), this);
    }
}

}