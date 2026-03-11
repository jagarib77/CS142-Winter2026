public class MiracleDoctor extends Doctor {

    // Constructor: x, y, health
    public MiracleDoctor(int x, int y, int health) {
        super(x, y, health);
    }

    @Override
    public void step(Entity[][] grid) {
        // 1. Move randomly
        moveRandom(grid);

        int rows = grid.length;
        int cols = grid[0].length;

        // 2. Heal nearby humans
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = getX() + dx;
                int ny = getY() + dy;

                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    Entity e = grid[nx][ny];

                    if (e instanceof Human) {
                        Human h = (Human) e;

                        // Heal human if infected or low health
                        if (h.isAlive() && (h.isInfected() || h.getHealth() < 100)) {
                            heal(h); // call Doctor's heal()
                        }
                    }
                }
            }
        }
    }

    @Override
    public void heal(Human h) {
        // MiracleDoctor heals stronger than normal Doctor
        // For example, heal 20 health points
        if (h.isAlive()) {
            h.heal(20);
            // Optionally, reduce infection status
            if (h.isInfected()) {
                h.infected = false; // MiracleDoctor can cure infection
            }
        }
    }
}