// SimulationModel.java
import java.util.ArrayList;
import java.util.List;


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
                    if(z<1){
                        int z1=(int)(Math.random()*10);
                        if(z1<1){
                            grid[x][y]=new MiracleDoctor(x, y, 150);
                        }
                        else{
                            grid[x][y]=new Doctor(x, y,100);
                        }
                    }
                    else{
                        grid[x][y]=new Citizen(x, y, 100);
                    }
                }
                else if(z<12){
                    grid[x][y]=new Soldier(x, y, 150);
                }
                else if(z<15){
                    int z2=(int)(Math.random()*100);
                    if(z2<10){
                        grid[x][y]=new LordOfZombie(x, y, 150);
                    }
                    else if(z2<11){
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

    //update 
    public void update(){

        // clear grid
        Entity[][] newGrid = new Entity[rows][cols];

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){

                Entity e=grid[x][y];

                if(e!=null){
                    if (e instanceof Human){
                        Human h=(Human)e;
                        // if this human be infect for too long.
                        // this value can be change(10)
                        if(h.isInfected() && h.getInfectionSteps()>=10){
                            e=new NormalZombie(x, y, 100);
                        }
                    }
                    e.step(grid); 

                    // put into grid
                    int nx=e.getX();
                    int ny=e.getY();

                    //check is this block nothing?
                    if (newGrid[nx][ny]==null){
                        newGrid[nx][ny]=e;
                    }
                    // if there are any entity in this block
                    else{
                        newGrid[x][y]=e;
                        e.setX(x); 
                        e.setY(y);
                    }
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

    //End if there is no human or no zombie
    public String checkGameOver(){
        int numH=0;
        int numZ=0;

        for(int x=0;x<rows;x++){
            for(int y=0;y<cols;y++){
                if(grid[x][y] instanceof Human){
                    numH++;
                }
                else if(grid[x][y] instanceof Zombie){
                    numZ++;
                }
            }
        }
        
        if(numH==0){
            return "DOOMSDAY";
        }
        if(numZ==0){
            return "HUMAN WIN";
        }
        return null;
    }

    public int[] getStats(){
        int h=0;
        int z=0;
        for (int x=0;x<rows;x++) {
            for (int y=0;y<cols;y++) {
                if (grid[x][y] instanceof Human){
                    h++;
                }
                else if(grid[x][y] instanceof Zombie){
                    z++;
                }
            }
        }
        return new int[]{h, z};
    }
}
