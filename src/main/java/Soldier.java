//Soldier.java


public class Soldier extends Human{
    
    public Soldier(int x, int y, int health) {
        super(x, y, health);
    }

    public void step(Entity[][] grid){
        super.step(grid);
        
        Zombie closestZombie=null;
        int minDistance=Integer.MAX_VALUE;
        
        //scanning hole grid to find closest zombie
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                Entity e= grid[x][y];
                
                // is it zombie?
                if(e instanceof Zombie){
                    
                    // distance between this and zombie
                    int distance=Math.abs(this.getX()-x)+Math.abs(this.getY()-y);

                    //find min distance
                    if(minDistance>distance){
                        minDistance=distance;
                        closestZombie=(Zombie)e;
                    }
                }  
            }
        }
        // if no zombie, move random
        if(closestZombie==null){
            moveRandom(grid);
        }
        else if(minDistance<=1){
            attack(closestZombie);
        }
        //move to closest zombie
        else{
            moveToward(closestZombie, grid);
        }
    }
    public void attack(Zombie z){
        // can change the number of damage
        z.takeDamage(10);
    }

    // a auxiliary methods, move to zombies
    private void moveToward(Zombie z, Entity[][] grid){

        int x=z.getX()-this.getX();

        int y=z.getY()-this.getY();

        if(Math.abs(x)>Math.abs(y)){
            if(x>0){
                this.setX(this.getX()+2);
            }
            else{
                this.setX(this.getX()-2);
            }
        }
        else{
            if(y>0){
                this.setY(this.getY()+2);
            }
            else{
                this.setY(this.getY()-2);
            }
        }
    }
}

