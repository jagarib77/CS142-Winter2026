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
        JPanel leftPanel = buildEditorPanel(); // editor controls
        JPanel rightPanel = new JPanel(); // not implemented yet - will be object info panel(e.g. ant info)
        JPanel bottomPanel = buildSimControlPanel(); // Sim controller

        worldPanel = new WorldPanel(sim, editorState, editorController);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(worldPanel, BorderLayout.CENTER);

        timer = new Timer(1000/ticksPerSecond, e -> {
            sim.step();
            worldPanel.repaint();
        });
        timer.start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel buildSimControlPanel(){
        JPanel bottomPanel = new JPanel();
        setLayout(new BorderLayout());

        // bottom panel is clipping with edge so it needs a buffer
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(50, 20, 20, 20))
        );

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

        // right
        JButton stepButton = new JButton("Step");

        speedPanel.add(minus1);
        speedPanel.add(minus5);
        speedPanel.add(speedField);
        speedPanel.add(plus5);
        speedPanel.add(plus1);

        pausePanel.add(pauseButton);

        stepPanel.add(stepButton);

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
        return bottomPanel;
    }

    private JPanel buildEditorPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

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
        currentBrushField.setMaximumSize(new Dimension(1000, 20));
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
        brushRadiusSpinner.setMaximumSize(new Dimension(1000, 20));
        leftPanel.add(brushRadiusSpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(foodEnergyLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        foodEnergySpinner.setMaximumSize(new Dimension(1000, 20));
        leftPanel.add(foodEnergySpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(foodCountLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        foodCountSpinner.setMaximumSize(new Dimension(1000, 20));
        leftPanel.add(foodCountSpinner);

        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(editingToggle);

        // handles any initialization errors
        updateBrushDisplay.run();

        return leftPanel;
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