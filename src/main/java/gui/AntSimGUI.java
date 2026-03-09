package gui;// gui.AntSimGUI.java
// Creates an interactive GUI and displays an animation of a world of ants.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import terrain.*;
import editor.EditorController;
import editor.EditorState;
import sim.AntSim;
/**
 * Swing GUI for visualizing the ant simulation.
 * Owns a timer that advances the simulation and repaints the world at a target tick rate.
 */
public class AntSimGUI extends JFrame {
    private final AntSim sim;
    private final Timer timer;
    private int ticksPerSecond = 15;
    private final EditorState editorState;
    private final EditorController editorController;
    private final WorldPanel worldPanel;

    private boolean seePheromones;
    private boolean dangerPhermoneSwitch;
    private boolean walkingTrailPhermoneSwitch;
    private boolean foodPhermoneSwitch;

    /**
     * Builds the GUI window, creates drawing and button panels and starts the simulation timer.
     * UI layout:
     * - CENTER: world drawing panel
     * - SOUTH: control buttons (speed up, slow down, pause/resume and step)
     * @param sim simulation model to display and advance (non-null)
     */
    public AntSimGUI(AntSim sim) {
        super("Ant Simulation");
        if (sim == null) throw new IllegalArgumentException("sim is null");
        this.sim = sim;
        this.editorState = new EditorState();
        this.editorController = new EditorController(sim, editorState);
        seePheromones = false;
        dangerPhermoneSwitch = false;
        walkingTrailPhermoneSwitch = false;
        foodPhermoneSwitch = false ;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(); // not implemented yet - will display info like sim time/steps
        JPanel leftPanel = new JPanel(); // editor controls
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel rightPanel = new JPanel(); // not implemented yet - will be object info panel(e.g. ant info)
        JPanel bottomPanel = new JPanel(new BorderLayout()); // Sim controller

        worldPanel = new WorldPanel(sim, editorState, editorController);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(worldPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // left panel
        // border factory handles busy work of creating a border object
        leftPanel.setBorder(BorderFactory.createTitledBorder("Editor"));

        JLabel currentBrushLabel = new JLabel("Current Brush:");
        JTextField currentBrushField = new JTextField(12);
        currentBrushField.setEditable(false);

        JButton dirtButton = new JButton("Dirt");
        JButton rockButton = new JButton("Rock");
        JButton airButton = new JButton("Air");
        JButton tunnelButton = new JButton("Tunnel");
        JButton sugarButton = new JButton("Sugar");

        JLabel brushSizeLabel = new JLabel("Brush Radius:");
        JSpinner brushRadiusSpinner = new JSpinner(
                new SpinnerNumberModel(editorState.getBrushRadius(), 0, 50, 5)
        );

        JLabel foodEnergyLabel = new JLabel("Food Energy:");
        JSpinner foodEnergySpinner = new JSpinner(
                new SpinnerNumberModel(editorState.getFoodEnergy(), 1, 1000, 10)
        );

        JLabel foodCountLabel = new JLabel("Food Count:");
        JSpinner foodCountSpinner = new JSpinner(
                new SpinnerNumberModel(editorState.getFoodCount(), 1, 1000, 10)
        );

        JToggleButton editingToggle = new JToggleButton("Editing: ON", editorState.isEditingEnabled());

        // runnable is a class with the run() functions, it does nothing until overridden
        // everything needs to update the display so that's what this does.
        // lambda function - makes new runnable object then defines run()
        Runnable updateBrushDisplay = () -> {
            String text;
            switch (editorState.getBrushMode()) {
                case TERRAIN -> text = "Terrain: " + editorState.getTerrainKind();
                case FOOD -> text = "Food: " + editorState.getFoodKind();
                case COLONY -> text = "Colony";
                default -> text = "Unknown";
            }
            currentBrushField.setText(text);
        };

        // terrain buttons
        dirtButton.addActionListener(e -> {
            editorState.setBrushMode(editor.BrushMode.TERRAIN);
            editorState.setTerrainKind(editor.TerrainKind.DIRT);
            updateBrushDisplay.run(); // updates display
            worldPanel.requestFocusInWindow(); // unfocuses the button
        });

        rockButton.addActionListener(e -> {
            editorState.setBrushMode(editor.BrushMode.TERRAIN);
            editorState.setTerrainKind(editor.TerrainKind.ROCK);
            updateBrushDisplay.run();
            worldPanel.requestFocusInWindow();
        });

        airButton.addActionListener(e -> {
            editorState.setBrushMode(editor.BrushMode.TERRAIN);
            editorState.setTerrainKind(editor.TerrainKind.AIR);
            updateBrushDisplay.run();
            worldPanel.requestFocusInWindow();
        });

        tunnelButton.addActionListener(e -> {
            editorState.setBrushMode(editor.BrushMode.TERRAIN);
            editorState.setTerrainKind(editor.TerrainKind.TUNNEL);
            updateBrushDisplay.run();
            worldPanel.requestFocusInWindow();
        });

        // food buttons - only sugar at the moment
        sugarButton.addActionListener(e -> {
            editorState.setBrushMode(editor.BrushMode.FOOD);
            editorState.setFoodKind(editor.FoodKind.SUGAR);
            updateBrushDisplay.run();
            worldPanel.requestFocusInWindow();
        });

        // spinner listeners - whenever a change occurs -> update the editorState value
        brushRadiusSpinner.addChangeListener(e ->
                editorState.setBrushRadius((Integer)brushRadiusSpinner.getValue())
        );

        foodEnergySpinner.addChangeListener(e ->
                editorState.setFoodEnergy((Integer)foodEnergySpinner.getValue())
        );

        foodCountSpinner.addChangeListener(e ->
                editorState.setFoodCount((Integer)foodCountSpinner.getValue())
        );

        // toggle button
        editingToggle.addActionListener(e -> {
            editorState.setEditingEnabled(editingToggle.isSelected());
            editingToggle.setText(editingToggle.isSelected() ? "Editing: ON" : "Editing: OFF");
            worldPanel.requestFocusInWindow();
        });

        // align all elements
        currentBrushLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentBrushField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dirtButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        rockButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        airButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        tunnelButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sugarButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        brushSizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brushRadiusSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodEnergyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodEnergySpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodCountSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        editingToggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(currentBrushLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(currentBrushField);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(new JLabel("Terrain"));
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(dirtButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(rockButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(airButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(tunnelButton);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(new JLabel("Food"));
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(sugarButton);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(brushSizeLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(brushRadiusSpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(foodEnergyLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(foodEnergySpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(foodCountLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(foodCountSpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(editingToggle);

        // bottomPanel
        JPanel speedPanel = new JPanel();
        JPanel pausePanel = new JPanel();
        JPanel stepPanel = new JPanel();

        // left is sim speed, middle is pause/unpause, right is sim step
        bottomPanel.add(speedPanel, BorderLayout.WEST);
        bottomPanel.add(pausePanel, BorderLayout.CENTER);
        bottomPanel.add(stepPanel, BorderLayout.EAST);

        // left
        JButton minus1 = new JButton("-1");
        JButton minus5 = new JButton("-5");
        JTextField speedField = new JTextField(String.valueOf(ticksPerSecond), 4);
        JButton plus5 = new JButton("+5");
        JButton plus1 = new JButton("+1");

        // middle
        JButton pauseButton = new JButton("Pause");
        JButton stepButton = new JButton("Step");

        speedPanel.add(minus1);
        speedPanel.add(minus5);
        speedPanel.add(speedField);
        speedPanel.add(plus5);
        speedPanel.add(plus1);

        pausePanel.add(pauseButton);

        stepPanel.add(stepButton);

        timer = new Timer(1000/ticksPerSecond, e -> {
            sim.step();
            worldPanel.repaint();
        });
        timer.start();

        minus1.addActionListener(e -> setTicksPerSecond(ticksPerSecond-1, speedField));
        minus5.addActionListener(e -> setTicksPerSecond(ticksPerSecond-5, speedField));
        plus5.addActionListener(e -> setTicksPerSecond(ticksPerSecond+5, speedField));
        plus1.addActionListener(e -> setTicksPerSecond(ticksPerSecond+1, speedField));

        speedField.addActionListener(e -> {
            try {
                setTicksPerSecond(Integer.parseInt(speedField.getText().trim()), speedField);
                worldPanel.requestFocusInWindow(); // unfocus after hitting enter
            }
            catch (NumberFormatException ex) { // ignore erroneous input
                speedField.setText(String.valueOf(ticksPerSecond));
                worldPanel.requestFocusInWindow();
            }
        });

        // if you click out of the text field save the value and update speed
        speedField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                speedField.postActionEvent();
            }
        });

        // highlight whole field when clicked
        speedField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                speedField.selectAll();
            }
        });

        pauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Unpause");
            } else {
                timer.start();
                pauseButton.setText("Pause");
            }
        });

        stepButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Unpause");
            }
            sim.step();
            worldPanel.repaint();
        });


        // 1. Create JRadioButton instances
        JRadioButton off = new JRadioButton("Off");
        JRadioButton danger = new JRadioButton("Danger");
        JRadioButton wT = new JRadioButton("Walking Trail");
        JRadioButton food = new JRadioButton("Food");

        // Add action listeners
        off.addActionListener(e -> {
            seePheromones = false;
        });

        danger.addActionListener(e -> {
            seePheromones = true;
            dangerPhermoneSwitch = true;
            walkingTrailPhermoneSwitch = false;
            foodPhermoneSwitch = false;
        });

        wT.addActionListener(e -> {
            seePheromones = true;
            dangerPhermoneSwitch = false;
            walkingTrailPhermoneSwitch = true;
            foodPhermoneSwitch = false;
        });

        food.addActionListener(e -> {
            seePheromones = true;
            dangerPhermoneSwitch = false;
            walkingTrailPhermoneSwitch = false;
            foodPhermoneSwitch = true;
        });

        // 2. Create a ButtonGroup
        ButtonGroup group = new ButtonGroup();

        // 3. Add radio buttons to the group
        group.add(off);
        group.add(danger);
        group.add(wT);
        group.add(food);

        // 4. Add radio buttons to the frame
        buttonContainer.add(off);
        buttonContainer.add(danger);
        buttonContainer.add(wT);
        buttonContainer.add(food);


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
        if (seePheromones) {
            if (dangerPhermoneSwitch) {
                drawPheromones (g, w, w.getPheromones(), 0);
            } else if (walkingTrailPhermoneSwitch) {
                drawPheromones (g, w, w.getPheromones(), 1);
            } else {
                drawPheromones (g, w, w.getPheromones(), 2);
            }
        } else {
            drawTerrain(g, w);
            drawObjects(g, w);
            drawAnts(g);
        }
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

                if (obj instanceof Sugar) {
                    // The color of Sugar
                    g.setColor(Color.WHITE);
                } else {
                    // The color of anything else (error color)
                    g.setColor(Color.MAGENTA);
                }

                g.fillOval(px+3, py+3, TILE_SIZE-6, TILE_SIZE-6);
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

    private void setTicksPerSecond(int newValue, JTextField speedField) {
        if (newValue<1) newValue = 1; // sim speed floor of 1 tick
        ticksPerSecond = newValue;
        timer.setDelay(1000/ticksPerSecond);
        speedField.setText(String.valueOf(ticksPerSecond));
    }

    public boolean isPaused() {
        return !timer.isRunning();
    }
}