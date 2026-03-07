//SimulationGui.java

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationGUI extends JPanel{
    
    private SimulationModel model;
    //one block's size is 20
    private int cellSize=20;

    private Timer timer;
    private int turn=500; 

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
            }
        };

        timer=new Timer(turn, listener);
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
                
                    // 1. 優先判斷是否被感染（視覺化進度）
                    if (h.isInfected()){
                        g.setColor(Color.MAGENTA); // 紫色：代表感染中
                    }
                    // color it depends on it career
                    else if (h instanceof MiracleDoctor){
                        g.setColor(new Color(0, 255, 0)); 
                    }
                    else if (h instanceof Doctor){
                        g.setColor(new Color(0, 150, 150)); 
                    }
                    else if (h instanceof Soldier){
                        g.setColor(new Color(0, 0, 255));
                    } 
                    //citizen
                    else {
                        g.setColor(new Color(0, 0, 150));
                    }
                }
                //Lord of zombie is orange
                else if(grid[x][y] instanceof LordOfZombie){
                    g.setColor(Color.RED);
                }
                //all zombie is red
                else if(grid[x][y] instanceof Zombie){
                    g.setColor(Color.ORANGE);
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
        JFrame frame=new JFrame("Zombie Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        this.startSimulation();
    }
}
