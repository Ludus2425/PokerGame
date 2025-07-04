import javax.swing.ImageIcon;

public class Card {
    private String suit;
    private String rank;
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }
    public String getSuit() {
        return suit;
    }
    public String getRank() {
        return rank;
    }
    public String getImagePath() {
        // Example: "/images/Hearts_A.png"
        return "/img/" + suit + "_" + rank + ".png";
    }
    public ImageIcon getImageIcon() {
        return new ImageIcon(getClass().getResource(getImagePath()));
    }
    
    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}
