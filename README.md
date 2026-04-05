# Lights-Out
Puzzle game using Swing GUI and 2D graphics


program implements the puzzle game using Swing GUI and 2D graphics.

user is presented with a 3x3 grid of cells, each either on (yellow) or off (dark gray).

objective is to turn all lights off by clicking cells. The core mechanic: clicking a cell

toggles that cell and its four neighbors (up, down, left & right). The game tracks

number of moves made and indicates when the puzzle is solved (all lights off).

program demonstrates object-oriented design with a main controller class (LightsOut)

that manages the JFrame and game state, and a custom JPanel subclass (GamePanel) that handles

all rendering and mouse interaction. This separation cleanly divides GUI control from game logic.
