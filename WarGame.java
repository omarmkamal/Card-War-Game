public class WarGame {
    private final Player p1;
    private final Player p2;
    private final Deck deck;
    private int round = 0;

    public WarGame(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.deck = new Deck();
        deck.shuffle();
    }

    public Player getP1() { return p1; }
    public Player getP2() { return p2; }
    public Deck getDeck() { return deck; }
}