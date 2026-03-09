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

        JPanel topPanel = new JPanel(); // not implemented yet - will display info like sim time/steps
        JPanel leftPanel = new JPanel(); // not implemented yet - will be the editor panel
        JPanel rightPanel = new JPanel(); // not implemented yet - will be object info panel(e.g. ant info)
        JPanel bottomPanel = new JPanel(new BorderLayout()); // Sim controller buttons

        worldPanel = new WorldPanel(sim, editorState, editorController);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(worldPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

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

        // right
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

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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