import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This model create the GUID for this simulator
 * It also includes main Method
 */
public class xLifeMain {
    private static String curMapFile = "xMapSmall.txt";
    /**
     * the simulation start from this method
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // For those of you who are running your GUI on OSX, you may want to include
        // the following try/catch. Otherwise, you will not be able to see the result
        // of setting any background colors.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("An error occurred when setting the look and feel");
        }
        int remainMove = 50;
        // 1. Read Map from file
        xLifeModel myGrid = new xLifeModel(curMapFile,remainMove);

        // 2. Setup GUI
        // 2.1. Setup Screen
        JFrame window = new JFrame("Game of Survival");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2.2. Create button
        JButton loadButton = new JButton("Load Map");
        JButton animateButton = new JButton("Animate");
        JButton stickButton = new JButton("Stick");
        JButton resetButton = new JButton("Reset");
        JButton saveButton = new JButton("Save");
        JButton closeButton = new JButton("Close");

        String rulesText = "<html>" +
                " +1 Human Point if Soldier kills Zombie: <br>" +
                " +1 Human Point if Human/Zombie reach Safezone <br>" +
                " +1 Zombie Point if Zombie tags Human" +
                "</html>";

        //stepLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel scoreLabel = new JLabel("<html>Humans: 0 vs Zombies: 0 <br> End Game after: "+ remainMove +" steps </html>");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel rulesLabel = new JLabel(rulesText);
        rulesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rulesLabel.setBorder(BorderFactory.createTitledBorder("Rules: Game ends when Steps reach 0 "));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0,1));
        topPanel.add(scoreLabel,BorderLayout.NORTH);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(rulesLabel,BorderLayout.SOUTH);

        // 2.2.1 Create Listener
        MyActionListener listener = new MyActionListener(myGrid, window, scoreLabel);

        // 2.2.2. Create the Timer
        // Create the timer and pass the handler into it
        Timer gameTimer = new Timer(50, listener);
        listener.setTimer(gameTimer);
        gameTimer.setActionCommand("Timer");

        // 2.2.3 Add behavior to the button
        stickButton.addActionListener(listener);
        closeButton.addActionListener(listener);
        animateButton.addActionListener(listener);
        resetButton.addActionListener(listener);
        loadButton.addActionListener(listener);
        saveButton.addActionListener(listener);

        // 2.3  Create 2 panel, top for grid and bottom for buttons
        // Create the label with initial text
        JPanel midPanel = new JPanel();
        midPanel.add(myGrid);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(loadButton);
        bottomPanel.add(animateButton);
        bottomPanel.add(stickButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(closeButton);

        // 2.4 Add Panel to Window
        window.add(topPanel, BorderLayout.NORTH);
        window.add(midPanel, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);

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
    /**
     * This is  Nested classes MyActionListener
     * For listening the action from the GUI
     */
    public static class MyActionListener implements ActionListener {
        private xLifeModel myGrid;
        private JFrame window;
        private JLabel scoreLabel;
        private Timer gameTimer; // Added so the listener can control the timer

        /**
         * @param model update grid when move change
         * @param window  update window when change map
         * @param scoreLabel update scoreLabel for every move ( stick button + animate is running)
         */
        public MyActionListener(xLifeModel model, JFrame window, JLabel scoreLabel) {
            this.myGrid = model;
            this.window = window;
            this.scoreLabel = scoreLabel;
        }

        /**
         * This method use for animation button, every 50s run 1 times,
         * @param timer add timer to listener
         */
        public void setTimer(Timer timer) {
            this.gameTimer = timer;
        }

        /**
         * Base on the text from button, give the action for proper button
         * "Animate": start and stop timmer
         * "Stick": stop timer, update move , redraw the grid and update score
         * "Timer": every 50s run timer 1 times, update move, update score, grid and check ending
         * "Close": exit the simulation
         * "Save": open Dialog for asking name of the output file, then save current map to file
         * "Load Map": Load new file, update all object - window - grid - status
         * "Reset" : Load current map again and reset all variable
         * @param event the event the listener get
         */
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
                updateScore();

            } else if(buttonText.equals("Timer")) {
                myGrid.update(); // Update the grid
                myGrid.repaint();  // Redraw the screen
                updateScore();
                if(myGrid.getRemainMoves()<=0){
                    gameTimer.stop();

                }
            } else if(buttonText.equals("Close")) {
                System.exit(0);
            } else if(buttonText.equals("Save")) {
                try {
                    saveMapDialog();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if(buttonText.equals("Load Map")) {
                curMapFile = geFile();
                if(curMapFile != null){
                    gameTimer.stop();
                    try {
                        myGrid.reset(curMapFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Update current Window
                    updateScore();
                    window.pack();
                    window.setLocationRelativeTo(null);
                    window.repaint();
                    window.revalidate();
                }
            }else if(buttonText.equals("Reset")) {
                try {
                    gameTimer.stop();
                    myGrid.reset(curMapFile);
                    // not move
                    updateScore();
                    myGrid.repaint(); // Redraw the screen
                    //myGrid.paintImmediately(0, 0, myGrid.getWidth(), myGrid.getHeight());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } // end actionPerformed

        /**
         * Update score after all object moving
         * Get points from grid
         */
        public void updateScore() {
            scoreLabel.setText("<html>Humans: " + myGrid.getHumanPoint() + " vs Zombies: " + myGrid.getZombiePoint() +
                    "<br> End Game after: "+ myGrid.getRemainMoves() +" steps </html>");
        }

        /**
         * Open FileDialog allow user can select map
         * @return path of file select from FileDialog
         */
        public String geFile() {
            FileDialog fd = new FileDialog(window, "Choose a file", FileDialog.LOAD);
            fd.setVisible(true);

            String directory = fd.getDirectory();
            String file = fd.getFile();

            if (file != null) {
                return directory + file;
            }
            // when user select cancel
            return null;
        }

        /**
         * Open FileDialog allow user set up output file name
         * Call toString method of xLifeModel to get current map
         * save the string to file
         * @throws IOException
         */
        public void saveMapDialog() throws IOException  {
            // Create a FileDialog in SAVE mode
            FileDialog dialog = new FileDialog(window, "Save Map As", FileDialog.SAVE);
            dialog.setVisible(true);

            // Get the chosen file name and directory
            String directory = dialog.getDirectory();
            String fileName = dialog.getFile();

            // If user canceled, fileName will be null
            if (fileName != null) {
                // Optional: add .txt if not present
                if (!fileName.endsWith(".txt")) {
                    fileName = fileName + ".txt";
                }
                File file = new File(directory, fileName);

                saveMapToFile(file);
                JOptionPane.showMessageDialog(window, "Map saved to " +
                        file.getAbsolutePath() + " successfully!");

            }
        }

        /**
         * Write file process
         * @param file
         * @throws IOException
         */
        public void saveMapToFile(File file) throws IOException {
            // Using try-with-resources to ensure the file closes automatically
            try (FileWriter fw = new FileWriter(file)) {
                int rows = myGrid.getRows();
                int cols = myGrid.getCols();
                // 1. Write dimensions (Header)
                fw.write(rows + "\n");
                fw.write(cols + "\n");
                // 2. Write the map string
                fw.write(myGrid.toString());
            }
        }
    }

}