import java.util.ArrayList;
import java.util.List;

public class House {
    private String name;
    private int chips;
    private List<Card> hand;

    public House() {
        this.name = "House";
        this.chips = 0; // House doesn't have chips like players
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
}
