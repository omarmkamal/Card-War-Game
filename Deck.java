import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        for (Shape s : Shape.values()) {
            for (Rank r : Rank.values()) {
                cards.add(new Card(r, s));
            }
        }
    }

    public void shuffle() { 
        Collections.shuffle(cards); 
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}