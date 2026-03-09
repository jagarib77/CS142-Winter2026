//SimulationGui.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationGUI extends JPanel{
    
    private SimulationModel model;

    //one block's size is 20
    private int cellSize=20;

    // update every 0.5 secend
    private Timer timer;
    private int turn=500; 

    // label to control
    private JLabel statsLabel;

    private JLabel humanLabel;
    private JLabel zombieLabel;

    public SimulationGUI(SimulationModel model){
        this.model=model;

        Entity[][] grid=model.getGrid();

        //size of hole gui
        int r=grid.length;
        int c=grid[0].length;
        setPreferredSize(new Dimension(c*cellSize, r*cellSize));

        ActionListener listener=new ActionListener(){
            public void actionPerformed(ActionEvent e){
                model.update();
                repaint();
                refreshStats();

                // if human or zombie is gone
                String result= model.checkGameOver();
                if(result!=null){
                    timer.stop();
                    javax.swing.JOptionPane.showMessageDialog(null, result);
                }
            }
        };
        timer=new Timer(turn, listener);

        humanLabel=new JLabel("Human number: 0");
        zombieLabel=new JLabel("Zombie number: 0");
    }
    
    // a button to control
    public void startSimulation(){
        timer.start();
    }

    public void pauseSimulation(){
        timer.stop();
    }

    public void restartSimulation(){
        timer.stop();
        model.reset();
        repaint();
    }

    public void setParameters(int rows, int cols, int time){
        timer.stop();

        this.turn=time;
        timer.setDelay(time);

        model.initialize(rows, cols);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Entity[][] grid=model.getGrid();
        
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                Entity e=grid[x][y];
                
                //Soldier is a blue on gui
                //This color can be change
                if (e instanceof Human) {
                    Human h = (Human) e;
                
                    if (h.isInfected()){
                        g.setColor(Color.MAGENTA); 
                    }
                    // color it depends on it career
                    else if (h instanceof MiracleDoctor){
                        g.setColor(new Color(150, 255, 150)); 
                    }
                    else if (h instanceof Doctor){
                        g.setColor(new Color(50, 255, 255)); 
                    }
                    else if (h instanceof Soldier){
                        g.setColor(new Color(120, 150, 255));
                    } 
                    //citizen
                    else {
                        g.setColor(new Color(210, 220, 255));
                    }
                }
                //Lord of zombie is orange
                else if(grid[x][y] instanceof LordOfZombie){
                    g.setColor(Color.RED);
                }
                //all zombie is red
                else if(grid[x][y] instanceof Zombie){
                    g.setColor(new Color(255, 170, 0));
                }

                //not thing this block show white
                else{
                    g.setColor(Color.WHITE);
                }

                g.fillRect(y*cellSize, x*cellSize, cellSize, cellSize);
                
                // show the name under entity
                if(grid[x][y] instanceof LivingEntity){
                    LivingEntity le=(LivingEntity)grid[x][y];
                    g.setColor(Color.BLACK);
                    g.drawString(""+le.getHealth(), y*cellSize+1, x*cellSize+10);
                }

                //draw edge
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(y*cellSize, x*cellSize, cellSize, cellSize);
            }
        }
    }

    public void display() {
        JFrame frame=new JFrame("Human vs. Zombie Survival Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // the grid in the center
        frame.add(this, BorderLayout.CENTER);

        JPanel controlPanel=new JPanel();
        controlPanel.setLayout(new GridLayout(6, 1));

        // a button to start
        JButton startB=new JButton("START");
        ActionListener listener1=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                startSimulation();
            }
        };
        startB.addActionListener(listener1);

        // a button to pause
        JButton pauseB=new JButton("PAUSE");
        ActionListener listener2=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                pauseSimulation();
            }
        };
        pauseB.addActionListener(listener2);

        // A button to reset
        JButton restartB=new JButton("RESTART");
        ActionListener listener3=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                restartSimulation();
            }
        };
        restartB.addActionListener(listener3);

        controlPanel.add(startB);
        controlPanel.add(pauseB);
        controlPanel.add(restartB);
        controlPanel.add(new JSeparator());
        controlPanel.add(humanLabel);
        controlPanel.add(zombieLabel);
        
        // Right side
        frame.add(controlPanel, BorderLayout.EAST);
        frame.pack();

        refreshStats();
        frame.setVisible(true);
    }

    //show the deta of human and zombie
    public void refreshStats() {
        int[] stats=model.getStats();
        humanLabel.setText("Human number: "+stats[0]);
        zombieLabel.setText("Zombie number: "+stats[1]);
    }
}
