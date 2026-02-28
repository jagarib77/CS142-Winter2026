// Creates an interactive GUI and displays an animation of a world of ants.
// By Kyle Hamasaki
// Edited by Harrison Butler

import java.awt.*;
import javax.swing.*;
import java.util.List; // don't import util.* as that creates timer conflicts

public class AntSimGUI extends JFrame {
    private final AntSim sim;
    private final Timer timer;

    private static final int TILE_SIZE = 15;
    private int TICKS_PER_SECOND = 15;

    private final WorldPanel worldPanel;

    // since AntSimGUI is an extension of JFrame a new frame doesn't need to be created
    // AntSimGUI is a JFrame with extended functionally
    public AntSimGUI(AntSim sim) {
        super("Ant Simulation"); // makes JFrame, everything after is custom functionally
        if (sim == null) throw new IllegalArgumentException("sim");
        this.sim = sim;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // world drawing panel (CENTER)
        worldPanel = new WorldPanel();
        add(worldPanel, BorderLayout.CENTER);

        // buttons (SOUTH)
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(buttonContainer, BorderLayout.SOUTH);

        JButton speedUpButton = new JButton("Speed Up");
        JButton slowDownButton = new JButton("Slow Down");
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
            if (TICKS_PER_SECOND == 1) return; // don't make tick rate 0
            TICKS_PER_SECOND += 1;
            timer.setDelay(1000/TICKS_PER_SECOND);
        });

        slowDownButton.addActionListener(e -> {
            TICKS_PER_SECOND -= 1;
            timer.setDelay(1000 / TICKS_PER_SECOND);
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

        // size window based on world size and tile size
        WorldGrid w = sim.getWorld();
        int worldW = w.getWidth()*TILE_SIZE;
        int worldH = w.getHeight()*TILE_SIZE;
        worldPanel.setPreferredSize(new Dimension(worldW, worldH));

        pack(); // fits border to grid size
        setLocationRelativeTo(null); // centers canvas
        setVisible(true); // displays the panel
    }

    // dedicated panel for swing to draw the screen
    private class WorldPanel extends JPanel {
        public WorldPanel() {
            setBackground(Color.WHITE);
            setDoubleBuffered(true);
        }

        // just adds drawWorld() functionality, correctly draws the world
        // during the sequence of rendering
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawWorld(g);
        }
    }

    // Updates the WorldGrid and draws a new picture of the WorldGrid.
    private void drawWorld(Graphics g) {
        WorldGrid w = sim.getWorld();
        drawTerrain(g, w);
        drawObjects(g, w);
        drawAnts(g);
    }

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

    private void drawObjects(Graphics g, WorldGrid w){
        for (int y=0; y<w.getHeight(); ++y){
            for (int x=0; x<w.getWidth(); ++x){
                Point pos = new Point(x, y);
                WorldObject obj = w.getObjectAt(pos);
                if (obj == null) continue;

                int px = pos.x*TILE_SIZE;
                int py = pos.y*TILE_SIZE;

                g.fillOval(px+3, py+3, TILE_SIZE-6, TILE_SIZE-6);

                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(obj.getSymbol()),
                        px + (TILE_SIZE/2) - 4,
                        py + (TILE_SIZE/2) + 4);
            }
        }
    }

    // ants store their own data so they don't need WorldGrid
    private void drawAnts(Graphics g){
        List<Ant> ants = sim.getAnts();

        for (Ant a:ants){
            if (!a.isAlive()) continue;
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