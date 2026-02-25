import java.awt.Color;

public enum Shape {
    HEARTS("♥", Color.RED),
    DIAMONDS("♦", Color.RED),
    CLUBS("♣", Color.BLACK),
    SPADES("♠", Color.BLACK);

    private final String symbol;
    private final Color color;

    Shape(String symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol() { return symbol; }
    public Color getColor() { return color; }
}