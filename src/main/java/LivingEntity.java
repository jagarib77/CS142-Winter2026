public abstract class LivingEntity extends Entity {
    private int health;

    public LivingEntity(int x, int y, int health) {
        super(x, y);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Heal method (optional, useful for doctors later)
     */
    public void heal(int amount) {
        if (isAlive()) {
            health += amount;
        }
    }
}