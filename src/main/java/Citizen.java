public class Citizen extends Human {

    public Citizen(int x, int y, int health) {
        super(x, y, health);
    }

    @Override
    public void step(Entity[][] grid) {
        // 1. Move randomly
        moveRandom(grid);

        // 2. Check nearby humans to spread infection
        int rows = grid.length;
        int cols = grid[0].length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = getX() + dx;
                int ny = getY() + dy;

                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    Entity e = grid[nx][ny];
                    if (e instanceof Human) {
                        Human h = (Human) e;

                        // If this citizen is infected, chance to infect neighbor
                        if (this.isInfected() && !h.isInfected()) {
                            double chance = 0.25; // 25% infection chance
                            if (Math.random() < chance) {
                                h.infect();
                            }
                        }
                    }
                }
            }
        }
    }
}