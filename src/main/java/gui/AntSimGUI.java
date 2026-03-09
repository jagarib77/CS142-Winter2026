package gui;// gui.AntSimGUI.java
// Creates an interactive GUI and displays an animation of a world of ants.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import javax.swing.*;
import java.awt.*;
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());

        worldPanel = new WorldPanel(sim, editorState, editorController);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(worldPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel speedPanel = new JPanel();
        JPanel pausePanel = new JPanel();
        JPanel stepPanel = new JPanel();

        bottomPanel.add(speedPanel, BorderLayout.WEST);
        bottomPanel.add(pausePanel, BorderLayout.CENTER);
        bottomPanel.add(stepPanel, BorderLayout.EAST);

        JButton minus1 = new JButton("-1");
        JButton minus5 = new JButton("-5");
        JTextField speedField = new JTextField(String.valueOf(ticksPerSecond), 4);
        JButton plus5 = new JButton("+5");
        JButton plus1 = new JButton("+1");

        JButton pauseButton = new JButton("Pause");
        JButton stepButton = new JButton("Step");

        speedPanel.add(minus1);
        speedPanel.add(minus5);
        speedPanel.add(speedField);
        speedPanel.add(plus5);
        speedPanel.add(plus1);

        pausePanel.add(pauseButton);
        stepPanel.add(stepButton);

        timer = new Timer(1000 / ticksPerSecond, e -> {
            sim.step();
            worldPanel.repaint();
        });
        timer.start();

        minus1.addActionListener(e -> setTicksPerSecond(ticksPerSecond - 1, speedField));
        minus5.addActionListener(e -> setTicksPerSecond(ticksPerSecond - 5, speedField));
        plus5.addActionListener(e -> setTicksPerSecond(ticksPerSecond + 5, speedField));
        plus1.addActionListener(e -> setTicksPerSecond(ticksPerSecond + 1, speedField));

        speedField.addActionListener(e -> {
            try {
                int value = Integer.parseInt(speedField.getText().trim());
                setTicksPerSecond(value, speedField);
            } catch (NumberFormatException ex) {
                speedField.setText(String.valueOf(ticksPerSecond));
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

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setTicksPerSecond(int newValue, JTextField speedField) {
        if (newValue < 1) newValue = 1;
        ticksPerSecond = newValue;
        timer.setDelay(1000 / ticksPerSecond);
        speedField.setText(String.valueOf(ticksPerSecond));
    }

    public boolean isPaused() {
        return !timer.isRunning();
    }
}