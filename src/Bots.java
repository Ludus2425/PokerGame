import java.util.List;
import java.util.ArrayList;
public class Bots extends Player {
    private GameLogic gameLogic;
    public Bots(String name, int initialChips, GameLogic gameLogic) {
        super(name, initialChips);
        this.gameLogic = gameLogic;
    }

public void takeAction(int currentBet, List<Card> communityCards) {
    if (hasFolded()) return;

    List<Card> fullHand = new ArrayList<>(getHand());
    fullHand.addAll(communityCards);

    PokerResult result = Hand_Rankings.determineHandRanking(fullHand);

    int handStrength = result.getRankValue(); // from 1 to 10
    int callAmount = currentBet - getCurrentBet();

    System.out.println(getName() + " has: " + result.getName());

    if (handStrength >= 6) { // Flush or better
        int raiseAmount = callAmount + 50;
        if (raiseAmount > getChips()) raiseAmount = getChips();
        removeChips(raiseAmount);
        setCurrentBet(getCurrentBet() + raiseAmount);
        gameLogic.setCurrentBet(getCurrentBet());
        gameLogic.addToPot(raiseAmount);
        System.out.println(getName() + " raises to " + getCurrentBet());
    } else if (handStrength >= 1) { // One Pair to Straight
        if (callAmount <= getChips()) {
            removeChips(callAmount);
            setCurrentBet(getCurrentBet() + callAmount);
            gameLogic.addToPot(callAmount);
            System.out.println(getName() + " calls " + callAmount);
        } else {
            fold();
            System.out.println(getName() + " folds (can't afford to call).");
        }
}
}
}