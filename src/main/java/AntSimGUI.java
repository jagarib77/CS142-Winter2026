// AntSimGUI.java
// Creates an interactive GUI and displays an animation of a world of ants.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

import java.awt.*;
import javax.swing.*;
import java.util.List; // don't import util.* as that creates timer conflicts

/**
 * Swing GUI for visualizing the ant simulation.
 * Owns a timer that advances the simulation and repaints the world at a target tick rate.
 */
public class AntSimGUI extends JFrame {
    private final AntSim sim;
    private final Timer timer;

    private static final int TILE_SIZE = 15;
    private int TICKS_PER_SECOND = 15;

    private final WorldPanel worldPanel;

    /**
     * Builds the GUI window, creates drawing and button panels and starts the simulation timer.
     * UI layout:
     * - CENTER: world drawing panel
     * - SOUTH: control buttons (speed up, slow down, pause/resume and step)
     * @param sim simulation model to display and advance (non-null)
     */
    public AntSimGUI(AntSim sim) {
        super("Ant Simulation"); // makes JFrame, everything after is custom functionally
        if (sim == null) throw new IllegalArgumentException("sim");
        this.sim = sim;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // world drawing panel (CENTER)
        worldPanel = new WorldPanel(); // JPanel extension
        add(worldPanel, BorderLayout.CENTER);

        // buttons (SOUTH)
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(buttonContainer, BorderLayout.SOUTH);

        JButton speedUpButton = new JButton("Speed Up: "+TICKS_PER_SECOND);
        JButton slowDownButton = new JButton("Slow Down: "+TICKS_PER_SECOND);
        JButton pauseButton = new JButton("Pause");
        JButton stepButton = new JButton("Step");

        buttonContainer.add(speedUpButton);
        buttonContainer.add(slowDownButton);
        buttonContainer.add(pauseButton);
        buttonContainer.add(stepButton);

        int delayMs = 1000/TICKS_PER_SECOND;
        timer = new Timer(delayMs, e -> {
            sim.step(); // advance simulation
            worldPanel.repaint(); // redraw everything
        });
        timer.start();

        speedUpButton.addActionListener(e -> {
            TICKS_PER_SECOND += 1;
            timer.setDelay(1000/TICKS_PER_SECOND);
            speedUpButton.setText("Speed Up: "+TICKS_PER_SECOND);
        });

        slowDownButton.addActionListener(e -> {
            if (TICKS_PER_SECOND == 1) return; // don't make tick rate 0
            TICKS_PER_SECOND -= 1;
            timer.setDelay(1000/TICKS_PER_SECOND);
            slowDownButton.setText("Slow Down: "+TICKS_PER_SECOND);
        });

        pauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Resume");
            } else {
                timer.start();
                pauseButton.setText("Pause");
            }
        });

        stepButton.addActionListener(e -> {
            // single step even if paused
            if (timer.isRunning()) timer.stop();
            pauseButton.setText("Resume");
            sim.step();
            worldPanel.repaint();
        });

        pack(); // fits border to grid size
        setLocationRelativeTo(null); // centers canvas
        setVisible(true); // displays the panel
    }

    /**
     * Dedicated JPanel used for painting the world.
     * Swing calls paintComponent during repaint and we delegate to drawWorld.
     */
    private class WorldPanel extends JPanel {
        /**
         * Configures panel defaults for smoother rendering.
         * Double buffering reduces flicker during frequent repaints.
         */
        public WorldPanel() {
            setBackground(Color.WHITE);
            setDoubleBuffered(true); // prevents flickering
        }

        /**
         * Swing paint callback. Always calls super first, then draws the simulation state.
         * @param g graphics context provided by Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawWorld(g);
        }

        /**
         * Provides a preferred size so pack() can size the frame to fit the grid.
         * @return preferred pixel size of the drawing area
         */
        @Override
        public Dimension getPreferredSize(){
            int width = TILE_SIZE*sim.getWorld().getWidth();
            int height = TILE_SIZE*sim.getWorld().getHeight();
            return new Dimension(width, height);
        }
    }

    /**
     * Draws a full frame of the simulation including terrain, objects and ants.
     * Rendering is separated into helper methods for clarity.
     *
     * @param g graphics context
     */
    private void drawWorld(Graphics g) {
        WorldGrid w = sim.getWorld();
        drawTerrain(g, w);
        drawObjects(g, w);
        drawAnts(g);
    }

    /**
     * Draws terrain tiles as colored rectangles and grid lines.
     *
     * @param g graphics context
     * @param w world grid being rendered
     */
    private void drawTerrain(Graphics g, WorldGrid w){
        for (int y=0; y<w.getHeight(); ++y){
            for (int x=0; x<w.getWidth(); ++x){
                Point pos = new Point(x, y);
                Terrain t = w.getTerrainAt(pos);
                if (t == null) continue;

                switch (t) {
                    // new Color(150, 75, 0) - brown color
                    case Dirt dirt -> g.setColor(new Color(150, 75, 0));
                    case Air air -> g.setColor(Color.WHITE);
                    case Rock rock -> g.setColor(Color.GRAY);
                    case Tunnel tunnel -> g.setColor(Color.LIGHT_GRAY);
                    default -> g.setColor(Color.MAGENTA); // error color
                }

                int px = x*TILE_SIZE;
                int py = y*TILE_SIZE;

                // fills in grid with rectangles
                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);

                // fills in grid lines
                g.setColor(Color.BLACK);
                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Draws world objects (resources, items, etc.) on top of terrain.
     *
     * @param g graphics context
     * @param w world grid being rendered
     */
    private void drawObjects(Graphics g, WorldGrid w){
        for (int y=0; y<w.getHeight(); ++y){
            for (int x=0; x<w.getWidth(); ++x){
                Point pos = new Point(x, y);
                WorldObject obj = w.getObjectAt(pos);
                if (obj == null) continue;

                int px = pos.x*TILE_SIZE;
                int py = pos.y*TILE_SIZE;

                g.fillOval(px+3, py+3, TILE_SIZE-6, TILE_SIZE-6);

                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(obj.getSymbol()),
                        px + (TILE_SIZE/2) - 4,
                        py + (TILE_SIZE/2) + 4);
            }
        }
    }

    /**
     * Draws all living ants from the simulation list.
     * Ant type controls rendering color and symbol.
     *
     * @param g graphics context
     */
    private void drawAnts(Graphics g){
        List<Ant> ants = sim.getAnts();

        for (Ant a:ants){
            if (a == null || !a.isAlive()) continue;
            Point pos = a.getPoint();

            switch (a) {
                case QueenAnt queenAnt -> g.setColor(Color.CYAN);
                case WorkerAnt workerAnt -> g.setColor(Color.YELLOW);
                case GuardAnt guardAnt -> g.setColor(Color.GRAY);
                default -> g.setColor(Color.MAGENTA); // error color
            }

            int px = pos.x*TILE_SIZE;
            int py = pos.y*TILE_SIZE;

            g.fillOval(px+2, py+2, TILE_SIZE-4, TILE_SIZE-4);

            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(a.getSymbol()),
                    px + (TILE_SIZE/2) - 4,
                    py + (TILE_SIZE/2) + 4);
        }
    }
}
