import java.util.*;

public class Winner {

    public static List<String> determineWinners(Map<String, List<Card>> playerHands) {
        List<String> winners = new ArrayList<>();
        PokerResult bestHand = null;

        Map<String, PokerResult> playerBestResults = new HashMap<>();

        for (Map.Entry<String, List<Card>> entry : playerHands.entrySet()) {
            String player = entry.getKey();
            List<Card> cards = entry.getValue();

            PokerResult best = null;
            // Generate all 5-card combinations from the 7 cards
            List<List<Card>> combos = combinations(cards, 5);
            for (List<Card> combo : combos) {
                PokerResult result = Hand_Rankings.determineHandRanking(combo);
                if (best == null || compareHands(result, best) > 0) {
                    best = result;
                }
            }
            playerBestResults.put(player, best);

            if (bestHand == null || compareHands(best, bestHand) > 0) {
                bestHand = best;
                winners.clear();
                winners.add(player);
            } else if (compareHands(best, bestHand) == 0) {
                winners.add(player);
            }
        }

        // Optionally, you can log/show each player's best hand:
        for (var entry : playerBestResults.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue().getName());
        }

        return winners;
    }

    // Helper to generate all k-combinations of a list
    private static <T> List<List<T>> combinations(List<T> list, int k) {
        List<List<T>> result = new ArrayList<>();
        combineHelper(list, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static <T> void combineHelper(List<T> list, int k, int start, List<T> temp, List<List<T>> result) {
        if (temp.size() == k) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            temp.add(list.get(i));
            combineHelper(list, k, i + 1, temp, result);
            temp.remove(temp.size() - 1);
        }
    }

    private static int compareHands(PokerResult h1, PokerResult h2) {
        return Integer.compare(h1.getRankValue(), h2.getRankValue());
    }
}