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
    public void step(Entity[][] grid){
        //is zombie alive?
        if(!isAlive()){
            return;
        }
        moveTowardsHuman(grid);
    }
}
