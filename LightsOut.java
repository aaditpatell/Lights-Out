import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LightsOut - Main controller class for the Lights Out puzzle game.
 * Sets up the JFrame with Swing components and manages game flow.
 */
public class LightsOut {
    private JFrame frame;
    private GamePanel gamePanel;
    private JButton newGameButton;
    private JButton hintButton;
    private JLabel statusLabel;
    private int gridSize;
    private int moveCount;
    
    /**
     * constructor initializes the game with a default 3x3 grid (changed from original 5x5 to make easier)
     */
    public LightsOut() {
        this.gridSize = 3;
        this.moveCount = 0;
        initializeGUI();
    }
    
    /**
     * Sets up the JFrame and all Swing components.
     * Creates the main window with buttons, labels, and game panel.
     */
    private void initializeGUI() {
        frame = new JFrame("Lights Out - Toggle Grid Puzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLocationRelativeTo(null);
        
        // Create main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel with title and status
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Lights Out - Turn Off All Lights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel = new JLabel("Moves: 0 | Status: Playing");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with game grid
        gamePanel = new GamePanel(gridSize, this);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> resetGame());
        
        hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> provideHint());
        
        buttonPanel.add(newGameButton);
        buttonPanel.add(hintButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    /**
     * Resets the game to a new random puzzle state.
     * Called when the New Game button is clicked.
     */
    private void resetGame() {
        moveCount = 0;
        gamePanel.initializeRandomGrid();
        updateStatusLabel();
    }
    
    /**
     * Provides a hint by toggling a specific cell that helps progress.
     * Increments move counter and updates display.
     */
    private void provideHint() {
        gamePanel.applyHint();
        moveCount++;
        updateStatusLabel();
    }
    
    /**
     * Updates the status label with current move count and game state.
     */
    public void updateStatusLabel() {
        if (gamePanel.isGameWon()) {
            statusLabel.setText("Moves: " + moveCount + " | Status: WON!");
        } else {
            statusLabel.setText("Moves: " + moveCount + " | Status: Playing");
        }
    }
    
    /**
     * Increments move count when a valid move is made on the grid.
     */
    public void recordMove() {
        moveCount++;
        updateStatusLabel();
    }
    
    /**
     * Main method - creates and runs the game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LightsOut());
    }
}