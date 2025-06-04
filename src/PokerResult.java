public class PokerResult {
    private final int rankValue;
    private final String name;

    public PokerResult(int rankValue, String name) {
        this.rankValue = rankValue;
        this.name = name;
    }

    public int getRankValue() { return rankValue; }
    public String getName() { return name; }
    //public List<Integer> getHighCards() { return highCards; }
}
