import java.util.Random;

public class Doctor extends Human {

    // The range within which the doctor can heal infected humans
    private static final int HEAL_RANGE = 1;

    private Random rand = new Random();

    // Constructor: initializes doctor position using Human constructor
    public Doctor(int x, int y) {
        super(x, y, 100);
    }

    // Called each simulation step
    // The doctor first attempts to heal nearby infected humans,
    // then moves randomly on the grid
    @Override
    public void step(Entity[][] grid) {

        // 1. Attempt to heal infected humans in surrounding cells
        for (int dr = -HEAL_RANGE; dr <= HEAL_RANGE; dr++) {
            for (int dc = -HEAL_RANGE; dc <= HEAL_RANGE; dc++) {

                int newX = getX() + dr;
                int newY = getY() + dc;

                // Check if the position is inside the grid
                if (isValid(grid, newX, newY)) {
                    Entity e = grid[newX][newY];

                    // If the entity is a Human and infected, heal them
                    if (e instanceof Human) {
                        Human h = (Human) e;

                        if (h.isInfected()) {
                            heal(h);
                        }
                    }
                }
            }
        }

        // 2. Move randomly after attempting to heal
        moveRandom(grid);
    }

    // Heals an infected human by resetting infection status
    public void heal(Human h) {
        h.infected = false;
        
    }

    // Checks whether a grid position is within bounds
    private boolean isValid(Entity[][] grid, int r, int c) {
        return r >= 0 && r < grid.length &&
            c >= 0 && c < grid[0].length;
    }
}
