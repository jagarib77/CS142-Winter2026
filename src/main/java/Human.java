import java.util.Random;

public abstract class Human extends LivingEntity {
    protected boolean infected = false;
    protected static Random rand = new Random();

    public Human(int x, int y, int health) {
        super(x, y, health);
    }

    // Move randomly to a neighboring cell within grid bounds
    public void moveRandom(Entity[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int newX = getX();
        int newY = getY();

        // Try to move one step in random direction
        int dir = rand.nextInt(4); // 0: up, 1: down, 2: left, 3: right
        switch (dir) {
            case 0: if(newY > 0) newY--; break;
            case 1: if(newY < cols - 1) newY++; break;
            case 2: if(newX > 0) newX--; break;
            case 3: if(newX < rows - 1) newX++; break;
        }

        // Check if the new cell is empty
        if(grid[newX][newY] == null) {
            setX(newX);
            setY(newY);
        }
        // else: stay in place (collision)
    }

    // Check infection status
    public boolean isInfected() {
        return infected;
    }

    // Infect this human
    public void infect() {
        infected = true;
    }

    // Step method: will be implemented in subclasses
    @Override
    public abstract void step(Entity[][] grid);
}