import java.util.ArrayList;
import java.util.List;

public class SimulationModel {
    
    private Entity[][] grid;
    private int rows;
    private int cols;

    // 10% have human, 2% hava zombie at first
    public void initializeGrid(int r, int c){
        
        rows=r;
        cols=c;
        grid=new Entity[rows][cols];

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){
                
                int z=(int)(Math.random()*100);
                
                if(z<10){
                    grid[x][y]=new Human();
                }
                
                //1/6 persen of living is zombie at the beganing
                else if(z<12){
                    grid[x][y]=new Zombie();
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

    // Maybe will delite because it will add on other class
    public void spreadInfection(Entity[][] newGrid){

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){

                if(newGrid[x][y] instanceof Human){

                    if(hasZombieAround(newGrid, x, y)){
                        newGrid[x][y]=new Zombie();
                    }
                }
            }
        }
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
