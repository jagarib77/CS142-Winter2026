//NormalZombie.java

package src.main.java;

public class NormalZombie extends Zombie{ 
    
    public NormalZombie(int x, int y, int health){
        super(x, y, health);
        //can change these two value  
        this.damage=15;
        this.speed=1;
    }
}
