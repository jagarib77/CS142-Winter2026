//zombie.java

public class Zombie extends LivingEntity{

    public Zombie(int x, int y, int health) {
        super(x, y, health);
    }

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
    @Override
    public void step(Entity[][] grid){

        int rows=grid.length;
        int cols=grid[0].length;

        int x=getX();
        int y=getY();

        // spread Infection
        for(int dx=-1;dx<=1;dx++){
            for(int dy=-1;dy<=1;dy++){

                if(dx==0 && dy==0){
                    continue;
                } 
                int nx=x+dx;
                int ny=y+dy;

                if(nx>=0 && nx<rows && ny>=0 && ny<cols){
                    if(grid[nx][ny] instanceof Human){

                        Human h=(Human)grid[nx][ny];
                        h.infect();
                        return;
                    }
                }
            }
        }

        // no human, move random
        int newX=x;
        int newY=y;

        int d=(int)(Math.random()*4);

        if(d==0){
            newX--;
        } 
        if(d==1){
            newX++;
        } 
        if(d==2){
            newY--;
        } 
        if(d==3){
            newY++;
        } 

        if(newX>=0 && newX<rows && newY>=0 && newY<cols && grid[newX][newY]==null){
            setX(newX);
            setY(newY);
        }
    }
}