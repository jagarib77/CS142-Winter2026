

public class LordOfZombie extends SeniorZombie {

    public LordOfZombie(int x, int y, int health) {
        super(x, y, health);
        // Stronger and faster than SeniorZombie
        this.damage = 25;   // more damage
        this.speed = 3;     // faster movement
    }
}
