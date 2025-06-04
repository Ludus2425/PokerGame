import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hand_Rankings {
    public static PokerResult determineHandRanking(List<Card> hand) {
        // This method should analyze the hand and return a string representing the hand ranking
        // For simplicity, let's assume we only check for pairs, three of a kind, and flushes.
        Map<String, Integer> rankCount = new HashMap<>();
        Map<String, Integer> suitCount = new HashMap<>();

        // Use 'hand' instead of 'cards'
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
            suitCount.put(card.getSuit(), suitCount.getOrDefault(card.getSuit(), 0) + 1);
        }
        boolean isFlush = suitCount.values().stream().anyMatch(count -> count >= 5);
        boolean isStraight = (isStraight(rankCount));
        // Checking for a straight

        int pairs = 0;
        int three = 0;
        int four = 0;
        for (int count : rankCount.values()) {
            if (count == 2) pairs++;
            if (count == 3) three++;
            if (count == 4) four++;
        }
        if (isFlush && isRoyalFlush(hand)) {
            return new PokerResult(10," Royal Flush");
        }
         else if (isStraight && isFlush) {
            return new PokerResult(9,"Straight Flush");
        }
        //Four of a Kind
        else if (four == 1){
            return new PokerResult(8,"Four of a Kind");
        }
        //full house
        else if (three == 1 && pairs == 1){
            return new PokerResult(7,"Full House");
        }
        //Flush 
        else if (isFlush) {
            return new PokerResult(6,"Flush");
        }
         //Straight
         else if (isStraight) {
            return new PokerResult(5,"Straight");
        }
        
         //Three of a Kind
         else if (three == 1) {
            return new PokerResult(4,"Three of a Kind");
        }
        //Two Pair
        else if (pairs == 2) {
            return new PokerResult(3,"Two Pair");
        }
        //One Pair
         else if (pairs == 1) {
            return new PokerResult(2,"One Pair");
        } else {
            return new PokerResult(1,"High Card");
        }
    }
    private static int rankToValue(String rank) {
        return switch (rank) {
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "10" -> 10;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            case "A" -> 14;
            default -> -1;
        };
    }
        private static boolean isStraight(Map<String, Integer> rankCount) {
        Set<Integer> values = new HashSet<>();

        for (String rank : rankCount.keySet()) {
            int val = (rankToValue(rank));
            values.add(val);
            if (val == 14) {
                values.add(1);  // Ace as low
            }
        }

        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        for (int i = 0; i <= sorted.size() - 5; i++) {
            boolean straight = true;
            for (int j = 1; j < 5; j++) {
                if (sorted.get(i + j) != sorted.get(i) + j) {
                    straight = false;
                    break;
                }
            }
            if (straight) return true;
        }

        return false;
    }
    private static boolean isRoyalFlush(List<Card> hand) {
    Set<String> royalRanks = Set.of("10", "J", "Q", "K", "A");

    // Group cards by suit
    Map<String, Set<String>> suitToRanks = new HashMap<>();
    for (Card card : hand) {
        suitToRanks
            .computeIfAbsent(card.getSuit(), k -> new HashSet<>())
            .add(card.getRank());
    }

    // Check if any suit contains all royal ranks
    for (Set<String> ranks : suitToRanks.values()) {
        if (ranks.containsAll(royalRanks)) {
            return true;
        }
    }

    return false;
}
}