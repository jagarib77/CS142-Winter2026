package gui;
// gui.WorldPanel.java
// Custom JPanel used to interact with AntSimGUI.java
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

import editor.BrushMath;
import editor.EditorController;
import editor.EditorState;
import pheromones.Pheromones;
import resources.*;
import sim.AntSim;
import sim.WorldGrid;
import terrain.*;
import util.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WorldPanel extends JPanel {
    private static final int TILE_SIZE = 15;

    private final AntSim sim;
    private final AntSimGUI gui;
    private final EditorState editorState;
    private final EditorController editorController;

    public WorldPanel(AntSim sim, AntSimGUI gui, EditorState editorState, EditorController editorController) {
        if (sim == null) throw new IllegalArgumentException("sim is null");
        if (editorState == null) throw new IllegalArgumentException("editorState is null");
        if (editorController == null) throw new IllegalArgumentException("editorController is null");

        this.sim = sim;
        this.gui = gui;
        this.editorState = editorState;
        this.editorController = editorController;
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        setFocusable(true);

        // anonymous subclass
        // used for custom functionality when the mouse moves or is clicked
        // mouse adapter is used to avoid implementing several unused functions
        // that mouseMotionListener and mouseListener make you implement

        // left clicks paints, right click toggles editing mode
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoveredTile(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                updateHoveredTile(e);

                if (SwingUtilities.isLeftMouseButton(e) && editorState.isEditingEnabled()) {
                    Point tile = pixelToTile(e.getX(), e.getY());
                    if (tile != null) {
                        editorController.applyBrushAt(tile); // update sim model
                        repaint(); // update screen
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                updateHoveredTile(e);
                requestFocusInWindow();

                if (SwingUtilities.isRightMouseButton(e)) {
                    editorController.toggleEditing();
                    repaint();
                    return; // stop
                }

                if (SwingUtilities.isLeftMouseButton(e) && editorState.isEditingEnabled()) {
                    Point tile = pixelToTile(e.getX(), e.getY());
                    if (tile != null) {
                        editorController.applyBrushAt(tile);
                        repaint();
                    }
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    @Override
    public Dimension getPreferredSize() {
        int width = TILE_SIZE * sim.getWorld().getWidth();
        int height = TILE_SIZE * sim.getWorld().getHeight();
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawWorld(g);
        drawBrushHighlight(g);
    }

    private void drawWorld(Graphics g) {
        WorldGrid w = sim.getWorld();
        if (gui.getSeePheromones()) {
            if (gui.getDangerPhermoneSwitch()) {
                drawPheromones (g, w, w.getPheromones(), 0);
            } else if (gui.getWalkingTrailPhermoneSwitch()) {
                drawPheromones (g, w, w.getPheromones(), 1);
            } else {
                drawPheromones (g, w, w.getPheromones(), 2);
            }
        } else {
            drawTerrain(g, w);
            drawObjects(g, w);
            drawAnts(g);
            drawBrushHighlight(g);
        }
    }

    private void drawPheromones (Graphics g, WorldGrid w, Pheromones p, int spec) {
        double [] [] [] grid = p.getGrid();
        double [] [] pherG = grid[spec];

        // Check dimensions match
        if (pherG.length != w.getWidth() || pherG[0].length != w.getHeight()) {
            return;
        }

        double strongestPheromone = Double.MIN_VALUE;
        double weakestPheromone = Double.MAX_VALUE;

        for (int x = 0; x < pherG.length; x++) {
            for (int y = 0; y < pherG[0].length; y++) {
                if (strongestPheromone < pherG[x][y])
                    strongestPheromone = pherG[x][y];
                if (weakestPheromone > pherG[x][y])
                    weakestPheromone = pherG[x][y];
            }
        }

        double range = strongestPheromone - weakestPheromone;

        //rgb 0-255
        //255 * ( ( strongestPhermone - pherG[x][y] ) / ( strongestPhermone - weakestPhermone ) )
        for (int x=0; x<pherG.length; ++x){
            for (int y=0; y<pherG[0].length; ++y){
                int px = x*TILE_SIZE;
                int py = y*TILE_SIZE;

                //range of r, g or b    0 - 255
                int colorValue;
                if (range == 0) {
                    colorValue = (pherG[x][y] == 0) ? 0 : 255; // If all equal and non-zero, show full color
                } else {
                    // Map from 0-255 based on pheromone intensity
                    colorValue = (int)(255 * ((pherG[x][y] - weakestPheromone) / range));
                }

                // Ensure value stays within 0-255 range
                colorValue = Math.min(255, Math.max(0, colorValue));

                if (spec == 0) { //Danger
                    g.setColor(new Color (colorValue, 0, 0)); //Black to bright red
                } else if (spec == 1) { //walking trail
                    g.setColor(new Color (0, colorValue, 0)); //Black to bright green
                } else if (spec == 2) { //Food
                    g.setColor(new Color (0, 0, colorValue)); //Black to bright blue
                } else {
                    g.setColor(Color.MAGENTA); // error color
                }

                // fills in grid with rectangles
                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);

                // fills in grid lines
                g.setColor(Color.BLACK);
                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            }

        }
    }

    private void drawTerrain(Graphics g, WorldGrid w) {
        for (int y=0; y<w.getHeight(); ++y) {
            for (int x=0; x<w.getWidth(); ++x) {
                Point pos = new Point(x, y);
                var t = w.getTerrainAt(pos);
                if (t == null) continue;

                switch (t) {
                    case Dirt dirt      -> g.setColor(new Color(150, 75, 0));
                    case Air air        -> g.setColor(Color.WHITE);
                    case Rock rock      -> g.setColor(Color.GRAY);
                    case Tunnel tunnel  -> g.setColor(Color.LIGHT_GRAY);
                    default             -> g.setColor(Color.MAGENTA); // debug color
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

    private void drawObjects(Graphics g, WorldGrid w) {
        for (int y=0; y<w.getHeight(); ++y) {
            for (int x=0; x<w.getWidth(); ++x) {
                Point pos = new Point(x, y);
                WorldObject obj = w.getObjectAt(pos);
                if (obj == null) continue;

                int px = x*TILE_SIZE;
                int py = y*TILE_SIZE;

                switch (obj) {
                    case Sugar sugar -> g.setColor(Color.BLUE);
                    default          -> g.setColor(Color.MAGENTA); // debug color
                }

                g.drawString(String.valueOf(obj.getSymbol()),
                        px+(TILE_SIZE/2)-4,
                        py+(TILE_SIZE/2)+4);
            }
        }
    }

    private void drawAnts(Graphics g) {
        var ants = sim.getAnts();

        for (var a:ants) {
            if (a == null || !a.isAlive()) continue;
            Point pos = a.getPoint();

            switch (a) {
                case sim.QueenAnt queenAnt -> g.setColor(Color.CYAN);
                case sim.WorkerAnt workerAnt -> g.setColor(Color.YELLOW);
                case sim.GuardAnt guardAnt -> g.setColor(Color.GRAY);
                default -> g.setColor(Color.MAGENTA); // debug color
            }

            int px = pos.x*TILE_SIZE;
            int py = pos.y*TILE_SIZE;

            g.fillOval(px+2, py+2, TILE_SIZE-4, TILE_SIZE-4);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(a.getSymbol()),
                    px+(TILE_SIZE/2)-4,
                    py+(TILE_SIZE/2)+4);
        }
    }

    private void drawBrushHighlight(Graphics g) {
        if (!editorState.isEditingEnabled()) return; // draw nothing

        Point hovered = editorState.getHoveredTile();
        if (hovered == null) return; // issues with hovered not always being set, not sure why?

        List<Point> validP = BrushMath.pointsInCircle(sim, hovered, editorState.getBrushRadius());

        g.setColor(new Color(128, 128, 128, 80)); // 80% opacity

        for (Point p:validP) {
            int px = p.x*TILE_SIZE;
            int py = p.y*TILE_SIZE;
            g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        }
    }

    private void updateHoveredTile(MouseEvent e) {
        Point tile = pixelToTile(e.getX(), e.getY());
        editorState.setHoveredTile(tile);
        repaint(); // redraw panel so the mouse movement can be seen
    }

    private Point pixelToTile(int pixelX, int pixelY) {
        int tileX = pixelX/TILE_SIZE; // not sure if this works in non-fullscreen modes
        int tileY = pixelY/TILE_SIZE;

        WorldGrid w = sim.getWorld();
        if (tileX < 0 || tileX >= w.getWidth() || tileY < 0 || tileY >= w.getHeight()) {
            return null;
        }

        return new Point(tileX, tileY);
    }
}