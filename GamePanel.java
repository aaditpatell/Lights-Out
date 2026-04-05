import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * GamePanel - custom JPanel that draws the Lights Out grid and handles
 * mouse interactions. extends JPanel to override paintComponent for 2D graphics.
 */


public class GamePanel extends JPanel {
    private boolean[][] grid;
    private int gridSize;
    private int cellSize;
    private LightsOut gameController;
    private Random random;
    
    // colours for drawing
    private static final Color LIGHT_ON = new Color(255, 200, 0);  // yellow (on)
    private static final Color LIGHT_OFF = new Color(50, 50, 50);  // dark gray (off)
    private static final Color GRID_BORDER = new Color(100, 100, 100);
    
    
    // constructor initializes the panel with grid size and game controller reference.
    // sets up mouse listener for click handling.
     
    public GamePanel(int gridSize, LightsOut controller) {
        this.gridSize = gridSize;
        this.gameController = controller;
        this.random = new Random();
        this.grid = new boolean[gridSize][gridSize];
        
        // initialize with random state
        initializeRandomGrid();
        
        // set preferred size and background
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(20, 20, 20));
        
        // add mouse listener for click handling
        addMouseListener(new GridMouseListener());
    }
    
    /**
     * initializes the grid with a solvable puzzle configuration.
     * generates a puzzle by simulating random valid moves from the solved state (all off).
     */

    public void initializeRandomGrid() {
        // start with all lights off (solved state)
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = false;
            }
        }
        
        // generate puzzle
        int numMoves = random.nextInt(3) + 2;  
        for (int move = 0; move < numMoves; move++) {
            int row = random.nextInt(gridSize);
            int col = random.nextInt(gridSize);
            toggleCell(row, col);
        }
        
        repaint();
    }
    
    
    // provides a hint by finding moves that reduce the total number of lights on (use if struggling)
     
    public void applyHint() {
        // count total lights currently on
        int lightsOn = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j]) lightsOn++;
            }
        }
        
        // if 0 lights on, puzzle is solved
        if (lightsOn == 0) {
            return;
        }
        
        int bestRow = -1;
        int bestCol = -1;
        int minLightsAfter = lightsOn + 1;  // start with worse than current state
        
        // try each possible cell and see what happens
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // simulate toggling this cell
                toggleCell(i, j);
                
                // count lights after this move
                int lightsAfter = 0;
                for (int row = 0; row < gridSize; row++) {
                    for (int col = 0; col < gridSize; col++) {
                        if (grid[row][col]) lightsAfter++;
                    }
                }
                
                // if this move reduces lights consider it
                if (lightsAfter < minLightsAfter) {
                    minLightsAfter = lightsAfter;
                    bestRow = i;
                    bestCol = j;
                }
                
                // undo the toggle to test next cell
                toggleCell(i, j);
            }
        }
        
        // applies the best move found
        if (bestRow != -1) {
            toggleCell(bestRow, bestCol);
        }
        
        repaint();
    }
    
    
    // Checks if all lights are off (grid solved).
     
    public boolean isGameWon() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    // toggles a cell and its four adjacent neighbors (up, down, left, right)..
     
    private void toggleCell(int row, int col) {
        grid[row][col] = !grid[row][col];
        
        // toggle neighboring cells
        if (row > 0) grid[row - 1][col] = !grid[row - 1][col];
        if (row < gridSize - 1) grid[row + 1][col] = !grid[row + 1][col];
        if (col > 0) grid[row][col - 1] = !grid[row][col - 1];
        if (col < gridSize - 1) grid[row][col + 1] = !grid[row][col + 1];
    }
    
    /*
     converts mouse coordinates to grid row and column indices.
     returns -1 for row/col if click is outside the grid area.
     */
    private int[] getGridCoordinates(int mouseX, int mouseY) {
        int startX = (getWidth() - gridSize * cellSize) / 2;
        int startY = (getHeight() - gridSize * cellSize) / 2;
        
        int col = (mouseX - startX) / cellSize;
        int row = (mouseY - startY) / cellSize;
        
        if (row >= 0 && row < gridSize && col >= 0 && col < gridSize) {
            return new int[] {row, col};
        }
        return new int[] {-1, -1};
    }
    
    /*
     overrides paintComponent to draw the grid of lights.
     uses 2D graphics to render rectangles for each cell.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // calculate cell size based on panel dimensions
        cellSize = Math.min(getWidth(), getHeight()) / gridSize - 2;
        int startX = (getWidth() - gridSize * cellSize) / 2;
        int startY = (getHeight() - gridSize * cellSize) / 2;
        
        // Draw grid cells
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int x = startX + j * cellSize;
                int y = startY + i * cellSize;
                
                // sets colour based on cell state
                if (grid[i][j]) {
                    g2d.setColor(LIGHT_ON);
                } else {
                    g2d.setColor(LIGHT_OFF);
                }
                
                // draws filled rectangle for cell
                g2d.fillRect(x, y, cellSize, cellSize);
                
                // draws border
                g2d.setColor(GRID_BORDER);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
    
    /*
     inner class MouseListener for handling grid clicks
     implements MouseListener interface to respond to mouse events
     */
    private class GridMouseListener extends MouseAdapter {
        /*
         handles mouse click events on the grid
         converts click coordinates to grid indices and toggles that cell
         */
        @Override
        public void mousePressed(MouseEvent e) {
            int[] coords = getGridCoordinates(e.getX(), e.getY());
            
            if (coords[0] != -1 && coords[1] != -1) {
                toggleCell(coords[0], coords[1]);
                gameController.recordMove();
                repaint();
            }
        }
    }
}