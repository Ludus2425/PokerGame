import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private static final String[] RANKS = {"A", "J", "Q", "K", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public Deck() {
        cards = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }
    public void shuffle() {
        Collections.shuffle(cards);
    }
    public Card dealCard() {
        if (cards.isEmpty()) {
            return null; 
        }
        return cards.remove(cards.size() - 1); 
    }


}
