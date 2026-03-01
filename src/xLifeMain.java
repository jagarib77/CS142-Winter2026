import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TimerTask;

public class xLifeMain {
    public static void main(String[] args) throws FileNotFoundException {

        // For those of you who are running your GUI on OSX, you may want to include
        // the following try/catch. Otherwise, you will not be able to see the result
        // of setting any background colors.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("An error occurred when setting the look and feel");
        }

        // 1. Read Map from file
        xLifeModel myGrid =  new xLifeModel("xMap.txt");

        // 2. Setup GUI
        // 2.1. Setup Screen
        JFrame window = new JFrame("Game of Life");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2.2. Create button
        JButton animateButton = new JButton("Animate");
        JButton stickButton = new JButton("Stick");
        JButton closeButton = new JButton("Close");
        JButton resetButton = new JButton("Reset");
        //JButton saveButton = new JButton("Save");

        // 2.2.1 Create Listener
        MyActionListener listener = new MyActionListener(myGrid);

        // 2.2.2. Create the Timer
        // Create the timer and pass the handler into it
        Timer gameTimer = new Timer(50,listener);
        listener.setTimer(gameTimer);
        gameTimer.setActionCommand("Timer");


        // 2.2.3 Add behavior to the button
        stickButton.addActionListener(listener);
        closeButton.addActionListener(listener);
        animateButton.addActionListener(listener);
        resetButton.addActionListener(listener);

        // 2.3  Create 2 panel, top for grid and bottom for buttons
        JPanel topPanel = new JPanel();
        topPanel.add(myGrid);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(animateButton);
        bottomPanel.add(stickButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(closeButton);

        //bottomPanel.add(saveButton);

        // 2.4 Add Panel to Window
        window.add(topPanel,BorderLayout.NORTH);
        window.add(bottomPanel,BorderLayout.SOUTH);

        // 2.5 Display Window
        // Setting property of Window (JFrame) ----------
        // Calculates the size of the window
        window.pack();
        // Set origin at center, default (0,0) top left
        window.setLocationRelativeTo(null);
        // Display
        window.setVisible(true);
        // ----------------------------------------------
        //System.out.print(myGrid.toString());
    }

    // Nested classes
    // Listener
    private static class MyActionListener implements ActionListener {
        private xLifeModel myGrid;
        private Timer gameTimer; // Added so the listener can control the timer

        public MyActionListener(xLifeModel model) {
            this.myGrid = model;
        }
        public void setTimer(Timer timer) {
            this.gameTimer = timer;
        }
        public void actionPerformed(ActionEvent event) {
            String buttonText = event.getActionCommand();
            if(buttonText.equals("Animate")) {
                if (gameTimer.isRunning()) {
                    gameTimer.stop();
                } else {
                    gameTimer.start();
                }
            } else if(buttonText.equals("Stick")) {
                gameTimer.stop();
                myGrid.update(); // Update the grid
                myGrid.repaint();  // Redraw the screen
            } else if(buttonText.equals("Timer")) {
                myGrid.update(); // Update the grid
                myGrid.repaint();  // Redraw the screen
            } else if(buttonText.equals("Close")) {
                System.exit(0);
            }else if(buttonText.equals("Reset")) {
                try {
                    gameTimer.stop();
                    myGrid.reset("xMap.txt");
                    myGrid.repaint();

                    //myGrid.paintImmediately(0, 0, myGrid.getWidth(), myGrid.getHeight());

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}