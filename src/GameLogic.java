import java.util.*;

public class GameLogic {
    private Deck deck;
    private List<Player> players;
    private List<Card> communityCards;
    private int currentBet = 0;
    private int pot = 0;
    public int getPot() {
    return pot;
    }

    public void addToPot(int amount) {
    pot += amount;
    }

    public void resetPot() {
    pot = 0;
    }
    public GameLogic() {
        deck = new Deck();
        players = new ArrayList<>();

        // One human player
        players.add(new Player("You", 1000));

        // Three bot players
        players.add(new Bots("Bot 1", 1000, this));
        players.add(new Bots("Bot 2", 1000, this));
        players.add(new Bots("Bot 3", 1000, this));

        communityCards = new ArrayList<>();
    }

    public void startNewRound() {
        deck = new Deck();
        communityCards.clear();
        for (Player p : players) {
            p.clearHand();
            p.resetBet();
        }
        currentBet = 0;
        dealHoleCards();
    }

    public void dealHoleCards() {
        for (int i = 0; i < 2; i++) {
            for (Player p : players) {
                p.receiveCard(deck.dealCard());
            }
        }
    }

    public void dealFlop() {
        deck.dealCard(); // Burn
        for (int i = 0; i < 3; i++) {
            communityCards.add(deck.dealCard());
        }
    }

    public void dealTurn() {
        deck.dealCard(); // Burn
        communityCards.add(deck.dealCard());
    }

    public void dealRiver() {
        deck.dealCard(); // Burn
        communityCards.add(deck.dealCard());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int amount) {
        currentBet = amount;
    }

public void botsTakeActions() {
    for (Player player : players) {
        if (player instanceof Bots bot && !bot.hasFolded()) {
            bot.takeAction(currentBet, communityCards);
        }
    }
}

    public void revealAllHands() {
        for (Player p : players) {
            p.setRevealCards(true); // Add this in your Player class
        }
    }

    public boolean isRoundOver() {
        for (Player player : players) {
            if (!player.hasFolded() && player.getCurrentBet() < currentBet) {
                return false; // 
            }
        }
        return true;
    }
public void hideAllHands() {
    for (Player p : players) {
        p.setRevealCards(false);
    }
}
}

