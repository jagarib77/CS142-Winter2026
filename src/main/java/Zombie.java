//zombie.java
package src.main.java;

public class Zombie extends LivingEntity{
    
    protected int damage;

    //senior zombie is faster
    protected int speed;

    public void moveTowardsHuman(Entity[][] grid){
        
        Human closestHuman=null;
        int minDistance=Integer.MAX_VALUE;

        //scanning hole grid to find closest human
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                Entity e= grid[x][y];
                
                // is it human?
                if(e instanceof Human){
                    
                    // distance between this and human
                    int distance=Math.abs(this.getX()-x)+Math.abs(this.getY()-y);

                    //find min distance
                    if(minDistance>distance){
                        minDistance=distance;
                        closestHuman=(Human)e;
                    }
                }  
            }
        }
        // if no human, move random
        if(closestHuman==null){
            moveRandom(grid);
        }

        else if(minDistance<=1){
            attack(closestHuman);
        }

        //move to closest human
        else{
            moveToward(closestHuman, grid);
        }

    }

    public void attack(Human h){
        // the number also can change 
        h.takeDamage(damage);   
    }
    
    //only move if it's alive
    public void step(Entity[][] grid, Entity[][] newGrid, int x, int y){

        int rows=grid.length;
        int cols=grid[0].length;

        // spreadInfection
        for(int dx=-1;dx<=1;dx++){
            for(int dy=-1;dy<=1;dy++){

                // cross self
                if(dx == 0 && dy == 0){
                    continue;
                }

                int nx=x+dx;
                int ny=y+dy;

                // check edge
                if(nx>=0 && nx<rows && ny>=0 && ny<cols){

                    if(grid[nx][ny] instanceof Human){

                        newGrid[nx][ny]=new Zombie();
                        newGrid[x][y]=this;
                        return;
                    }
                }
            }
        }

        //no human, move random
        int x1=x;
        int y1=y;

        int d=(int)(Math.random()*4);

        if(d==0){
            x1--;
        } 
        if(d==1){
            x1++;
        } 
        if(d==2){
            y1--;
        } 
        if(d==3){
            y1++;
        } 

        if(x1>=0 && x1<rows && y1>=0 && y1<cols && newGrid[x1][y1]==null){
            newGrid[x1][y1]=this;
        } 
        else{
            newGrid[x][y]=this;
        }
    }
}
