import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int chips;
    private List<Card> hand;
    private boolean hasFolded = false;
    private int currentBet = 0;

    public Player(String name, int initialChips) {
        this.name = name;
        this.chips = initialChips;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int amount) {
        chips += amount;
    }

    public void removeChips(int amount) {
        if (amount <= chips) {
            chips -= amount;
        } else {
            throw new IllegalArgumentException("Not enough chips");
        }
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }
    public boolean hasFolded() {
        return hasFolded;
    }
    public int getCurrentBet() {
        return currentBet;
    }
    public void setCurrentBet(int amount) {
        this.currentBet = amount;
    }
    public void bet(int amount) {
        if (amount > chips) amount = chips; // All-in
        chips -= amount;
        currentBet += amount;
    }

    public void fold() {
        hasFolded = true;
    }

    public void resetBet() {
        currentBet = 0;
    }

    public void winPot(int amount) {
        chips += amount;
    }
    private boolean isBot;

public Player(String name, boolean isBot) {
    this.name = name;
    this.isBot = isBot;
}

public boolean isBot() {
    return isBot;
}
private boolean revealCards = false;
public void setRevealCards(boolean value) {
    this.revealCards = value;
}
public boolean isRevealCards() {
    return revealCards;
}

}
