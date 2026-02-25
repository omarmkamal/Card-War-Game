import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private final String name;
    private final ArrayList<Card> collectedCards = new ArrayList<>();

    public Player(String name) { 
        this.name = name; 
    }
    
    public String getName() { 
        return name; 
    }

    public void addCard(Card c) { 
        collectedCards.add(c); 
    }

    public Set<Shape> getCollectedShapes() {
        Set<Shape> shapes = new HashSet<>();
        for (Card c : collectedCards) {
            shapes.add(c.getShape());
        }
        return shapes;
    }

    public int getShapeCount() { 
        return getCollectedShapes().size(); 
    }
    
    public boolean hasAllShapes() { 
        return getShapeCount() == 4; 
    }

    public String getShapesString() {
        StringBuilder sb = new StringBuilder();
        for (Shape s : getCollectedShapes()) {
            sb.append(s.getSymbol()).append(" ");
        }
        return sb.toString().trim();
    }
}