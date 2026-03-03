import java.util.ArrayList;
import java.util.List;

import src.main.java.SeniorZombie;
import src.main.java.Soldier;

public class SimulationModel {
    
    private Entity[][] grid;
    private int rows;
    private int cols;

    // 10% have human, 2% have soldier at first
    public void initializeGrid(int r, int c){
        
        rows=r;
        cols=c;
        grid=new Entity[rows][cols];

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){
                
                int z=(int)(Math.random()*100);
                
                if(z<10){
                    grid[x][y]=new Citizen(x, y, 100);
                }
                else if(z<12){
                    grid[x][y]=new Soldier(x, y, 150);
                }
                else if(z<15){
                    int z1=(int)(Math.random()*100);
                    if(z1<10){
                        grid[x][y]=new LordOfZombie(x, y, 150);
                    }
                    else if(z1<11){
                        grid[x][y]=new SeniorZombie(x, y, 200);
                    }
                    grid[x][y]=new NormalZombie(x, y, 120);
                }
                else{
                    grid[x][y]=null;
                }
            }
        }
    }
    
    public void update(){

        // clear grid
        Entity[][] newGrid = new Entity[rows][cols];

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){

                Entity e=grid[x][y];

                if(e!=null){
                    e.step(grid); 
                }
            }
        }

        // put into grid
        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){

                Entity e=grid[x][y];

                if(e!=null){

                    int newX = e.getX();
                    int newY = e.getY();

                    newGrid[newX][newY] = e;
                }
            }
        }

        grid=newGrid;
    }

    public Entity[][] getGrid(){
        return grid;
    }

    // citizen: C / Soldier: S / Doctor and MiracleDoctor: D / norman and senior zombie: Z / LordofZombie: L
    public void printGrid(){

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){

                Entity e=grid[x][y];
                
                if(e instanceof Citizen){
                    System.out.print(" C ");
                }
                else if(e instanceof Soldier){
                    System.out.print(" S ");
                }
                else if(e instanceof Doctor){
                    System.out.print(" D ");
                }
                else if(e instanceof LordOfZombie){
                    System.out.print(" L ");
                }
                else if(e instanceof Zombie){
                    System.out.print(" Z ");
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }
}
