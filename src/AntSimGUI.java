// Creates an interactive GUI and displays an animation of a world of ants.
// I don't really know much about JFrame, so feel free to change anything.
// By Kyle Hamasaki

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class AntSimGUI extends JFrame {
    // The size of the square cells containing the Terrain, Ant, and WorldObject
    private final int cellSize = 10;
    // The Ant simulator
    private AntSim simulator;
    // The entire interface of the program
    private JFrame frame;
    // A label that contains an image of the world
    private JLabel imageContainer;
    // Used to draw in imageContainer
    private Graphics g;

    public AntSimGUI(AntSim simulator) {
        this.simulator = simulator;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Ant Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The WorldGrid based on simulator
        WorldGrid world = simulator.getWorld();
        // Creates an area on the frame for the animation.
        BufferedImage worldImage = new BufferedImage(world.getWidth() * cellSize, world.getHeight() * cellSize,
                BufferedImage.TYPE_INT_RGB);

        imageContainer = new JLabel(new ImageIcon(worldImage));
        g = worldImage.getGraphics();

        // Creates an area on the frame for the buttons.
        // Note that the buttons do not do anything yet, and that more buttons can be added.
        JPanel buttonContainer = new JPanel();
        // The buttons that will be added to the GUI
        List<JButton> buttons = new ArrayList<>();
        ActionListener listener = new MyActionListener();

        // Button that stops the animation
        buttons.add(new JButton("Stop"));

        // Button that starts the animation
        buttons.add(new JButton("Start"));

        // Button that ticks the animation to the next step
        buttons.add(new JButton("Tick"));

        // Button that speeds up the animation
        buttons.add(new JButton("Speed up"));

        // Button that slows down the animation
        buttons.add(new JButton("Slow down"));

        // Adds all the buttons into buttonContainer and makes them functional.
        for (JButton button : buttons) {
            buttonContainer.add(button);
            button.addActionListener(listener);
        }
        buttonContainer.setLayout(new GridLayout(1, -1));

        frame.add(imageContainer, BorderLayout.CENTER);
        frame.add(buttonContainer, BorderLayout.SOUTH);
        // It might be better to preset the frame to a certain size than calling the pack() method.
        frame.pack();

        drawWorld();
        frame.setVisible(true);

    }

    // Draws the WorldGrid as a picture.
    // By Kyle Hamasaki
    public void drawWorld() {
        // The WorldGrid based on simulator
        WorldGrid world = simulator.getWorld();

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                Point currentLocation = new Point(i, j);

                // Draws the Terrain
                Terrain terrain = world.getTerrainAt(currentLocation);
                char terrainSymbol = terrain.getSymbol();
                if (terrainSymbol == '#') {
                    // The color if the Terrain is Dirt
                    g.setColor(new Color(0x785B4C));
                    // The color if the Terrain is Rock
                } else if (terrainSymbol == 'R') {
                    g.setColor(new Color(0xFF6C6E6E));
                } else if (terrainSymbol == '.') {
                    // The color if the Terrain is Tunnel
                    g.setColor(new Color(0x514031));
                } else {
                    // The color if the Terrain is Air/Terrain
                    g.setColor(new Color(0x74A5EF));
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);

                // Draws the WorldObject
                WorldObject worldObject = world.getObjectAt(currentLocation);
                if (worldObject != null) {
                    char worldObjectSymbol = worldObject.getSymbol();
                    // Draws the character of the WorldObject in the middle of the cell.
                    g.drawString(Character.toString(worldObjectSymbol), (cellSize / 2) + (i * cellSize), (cellSize / 2) + (j * cellSize));
                }
            }
        }

        // Draws the Ants
        List<Ant> ants = simulator.getAnts();
        for (Ant a : ants) {
            int xPos = a.getX();
            int yPos = a.getY();
            g.drawString(Character.toString(a.getSymbol()), (cellSize / 2) + (xPos * cellSize), (cellSize / 2) + (yPos * cellSize));
        }
    }

    // Updates the WorldGrid and draws a new picture of the WorldGrid.
    public void update() {
        simulator.step();
        drawWorld();
        frame.repaint();
    }

    // Used to make buttons functional. The buttons do not do their respective function yet.
    //TODO: create a timer and make the buttons perform their respective functions
    public class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getActionCommand().equals("Stop")) {
                System.out.println("Stop");
            } else if (event.getActionCommand().equals("Start")) {
                System.out.println("Start");
            } else if (event.getActionCommand().equals("Tick")) {
                System.out.println("Tick");
            } else if (event.getActionCommand().equals("Speed up")) {
                System.out.println("Speed up");
            } else if (event.getActionCommand().equals("Slow down")) {
                System.out.println("Slow down");
            }
        }
    }
}
