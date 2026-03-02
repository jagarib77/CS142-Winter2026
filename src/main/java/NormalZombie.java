//NormalZombie.java

package src.main.java;

public class NormalZombie extends Zombie{ 
    
    public NormalZombie(){
        super(x, y, 100);
        //can change these two value  
        this.damage=15;
        this.speed=1;
    }
}
