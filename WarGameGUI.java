import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class WarGameGUI extends JFrame {
    private Player p1, p2;
    private Deck deck;
    private int round = 0;

    private JLabel p1NameLabel, p2NameLabel;
    private JLabel p1CardLabel, p2CardLabel;
    private JLabel p1ShapesLabel, p2ShapesLabel;
    private JLabel roundLabel, statusLabel;
    private JButton drawButton;

    public WarGameGUI(String name1, String name2) {
        initGame(name1, name2);
        setupUI();
    }

    private void initGame(String name1, String name2) {
        p1 = new Player(name1);
        p2 = new Player(name2);
        deck = new Deck();
        deck.shuffle();
        round = 0;
    }

    private void setupUI() {
        setTitle("Advanced Card War Game 🃏");
        setSize(750, 550); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(34, 139, 34)); 

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        roundLabel = new JLabel("Round: 0", SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 24));
        roundLabel.setForeground(Color.WHITE);
        
        statusLabel = new JLabel("Game Started! May the best player win.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        statusLabel.setForeground(Color.YELLOW);
        
        topPanel.add(roundLabel);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        centerPanel.add(createPlayerPanel(p1, true));
        centerPanel.add(createPlayerPanel(p2, false));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        drawButton = new JButton("Draw Cards 🎮");
        drawButton.setFont(new Font("Arial", Font.BOLD, 20));
        drawButton.setBackground(new Color(255, 215, 0));
        drawButton.addActionListener(new DrawAction());
        bottomPanel.add(drawButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createPlayerPanel(Player p, boolean isPlayer1) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel cardLabel = new JLabel("🂠", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 100));
        cardLabel.setBackground(Color.WHITE); 
        cardLabel.setOpaque(true); 
        cardLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3, true), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cardLabel.setPreferredSize(new Dimension(160, 240)); 

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(cardLabel);
        panel.add(cardWrapper, BorderLayout.CENTER);

        JLabel shapesLabel = new JLabel("Shapes: (0/4)", SwingConstants.CENTER);
        shapesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        shapesLabel.setForeground(Color.CYAN); 
        panel.add(shapesLabel, BorderLayout.SOUTH);

        if (isPlayer1) {
            p1NameLabel = nameLabel; p1CardLabel = cardLabel; p1ShapesLabel = shapesLabel;
        } else {
            p2NameLabel = nameLabel; p2CardLabel = cardLabel; p2ShapesLabel = shapesLabel;
        }
        return panel;
    }

    private class DrawAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (deck.isEmpty()) { endGame(); return; }

            round++;
            roundLabel.setText("Round: " + round);

            Card c1 = deck.drawCard();
            Card c2 = deck.drawCard();

            if (c1 == null || c2 == null) { endGame(); return; }

            updateCardDisplay(p1CardLabel, c1);
            updateCardDisplay(p2CardLabel, c2);

            if (c1.compareTo(c2) > 0) {
                p1.addCard(c1); p1.addCard(c2);
                statusLabel.setText(p1.getName() + " wins the round 🥳");
            } else if (c1.compareTo(c2) < 0) {
                p2.addCard(c1); p2.addCard(c2);
                statusLabel.setText(p2.getName() + " wins the round 🥳");
            } else {
                p1.addCard(c1); p2.addCard(c2);
                statusLabel.setText("Tie round 🫩");
            }

            p1ShapesLabel.setText("Shapes: " + p1.getShapesString());
            p2ShapesLabel.setText("Shapes: " + p2.getShapesString());

            if (p1.hasAllShapes() || p2.hasAllShapes()) { endGame(); }
        }
    }

    private void updateCardDisplay(JLabel label, Card card) {
        label.setText(card.getFace());
        label.setForeground(card.getShape().getColor());
    }

    private void resetGame() {
        initGame(p1.getName(), p2.getName());
        p1CardLabel.setText("🂠");
        p1CardLabel.setForeground(Color.BLACK);
        p2CardLabel.setText("🂠");
        p2CardLabel.setForeground(Color.BLACK);
        p1ShapesLabel.setText("Shapes: (0/4)");
        p2ShapesLabel.setText("Shapes: (0/4)");
        roundLabel.setText("Round: 0");
        statusLabel.setText("Game Reset! Good Luck.");
        drawButton.setEnabled(true);
    }

    private void endGame() {
        drawButton.setEnabled(false);
        String winner;

        if (p1.hasAllShapes() && !p2.hasAllShapes()) winner = p1.getName();
        else if (p2.hasAllShapes() && !p1.hasAllShapes()) winner = p2.getName();
        else if (p1.getShapeCount() > p2.getShapeCount()) winner = p1.getName();
        else if (p2.getShapeCount() > p1.getShapeCount()) winner = p2.getName();
        else winner = "Nobody (It's a Tie!)";

        statusLabel.setText("GAME OVER!");

        // 🔊 TRIGGER THE WIN SOUND
        playSound("win.wav");

        int choice = JOptionPane.showConfirmDialog(this,
                "🎉 " + winner.toUpperCase() + " WINS! 🎉\n\nWould you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    // 🔊 SOUND PLAYER METHOD
    private void playSound(String fileName) {
        try {
            // Check in /src/ folder first
            File file = new File("src/" + fileName);
            
            // If not found in /src/, check the root folder
            if (!file.exists()) {
                file = new File(fileName);
            }

            if (file.exists()) {
                System.out.println("Playing sound from: " + file.getAbsolutePath());
                AudioInputStream ai = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ai);
                clip.start();
            } else {
                System.out.println("❌ SOUND FILE NOT FOUND!");
                System.out.println("Put 'win.wav' inside your 'src' folder.");
                Toolkit.getDefaultToolkit().beep(); // System beep if file is missing
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String n1 = JOptionPane.showInputDialog(null, "Player 1 Name:");
        String n2 = JOptionPane.showInputDialog(null, "Player 2 Name:");
        if (n1 == null || n1.isEmpty()) n1 = "Player 1";
        if (n2 == null || n2.isEmpty()) n2 = "Player 2";

        String f1 = n1; String f2 = n2;
        SwingUtilities.invokeLater(() -> new WarGameGUI(f1, f2).setVisible(true));
    }
}