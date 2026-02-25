public class Card implements Comparable<Card> {
    private final Rank rank;
    private final Shape shape;

    public Card(Rank rank, Shape shape) {
        this.rank = rank;
        this.shape = shape;
    }

    public int getValue() { return rank.getValue(); }
    public Shape getShape() { return shape; }
    public String getFace() { return rank.getDisplay() + shape.getSymbol(); }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.getValue(), other.getValue());
    }
}