kyle// AntSimGUI.java

// Creates an interactive GUI and displays an animation of a world of ants.
// I don't really know much about JFrame, so feel free to change anything. - Kyle Hamasaki

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class AntSimGUI extends JFrame {
    // The WorldGrid
    private WorldGrid grid;
    // The entire interface of the program
    private JFrame frame;
    // A label that contains an image of the world
    private JLabel imageContainer;
    // Used to draw in imageContainer
    private Graphics g;

    public AntSimGUI() {
        this.grid = grid;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Ant Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates an area on the frame for the animation.
        BufferedImage worldImage = new BufferedImage(grid.getWidth() * 10, grid.getHeight() * 10,
                BufferedImage.TYPE_INT_RGB);

        imageContainer = new JLabel(new ImageIcon(worldImage));
        g = worldImage.getGraphics();

        // Creates an area on the frame for the buttons.
        // Note that the buttons do not do anything yet, and that more buttons can be added.
        JPanel buttonContainer = new JPanel(new FlowLayout());

        // Button that speeds up the animation.
        JButton speedUpButton = new JButton("Speed Up");
        buttonContainer.add(speedUpButton);

        // Button that slows down the animation.
        JButton slowDownButton = new JButton("Slow down");
        buttonContainer.add(slowDownButton);

        frame.add(imageContainer, BorderLayout.CENTER);
        frame.add(buttonContainer, BorderLayout.SOUTH);
        // It might be better to preset the frame to a certain size than calling the pack() method.
        frame.pack();

        drawWorld();
        frame.setVisible(true);
    }

    // Draws the WorldGrid as a picture.
    public void drawWorld() {
        // I have no idea how to draw the world, so feel free to write any code.
    }

    // Updates the WorldGrid and draws a new picture of the WorldGrid.
    public void update() {
        // Maybe WorldGrid should have an update() method, which this method could call.
        // grid.update();
        drawWorld();
    }
}
