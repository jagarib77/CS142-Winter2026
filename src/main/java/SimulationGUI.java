//SimulationGui.java

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
                
                //human is a blue on gui
                //This color can be change
                if(grid[x][y] instanceof Human){
                    g.setColor(Color.BLUE);
                }

                //all zombie is red
                else if(grid[x][y] instanceof Zombie){
                    g.setColor(Color.RED);
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
                    g.drawString(""+le.getHealth(), y*cellSize+5, x*cellSize+10);
                }

                //draw edge
                g.setColor(Color.GRAY);
                g.drawRect(y*cellSize, x*cellSize, cellSize, cellSize);
            }
        }
    }
}
