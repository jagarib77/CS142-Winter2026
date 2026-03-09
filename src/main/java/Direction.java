// Direction.java
// Cardinal movement directions used by ants and pheromone spreading.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

import java.util.Random;

/**
 * Represents a movement direction on the grid.
 * Each direction includes dx and dy offsets for coordinate updates.
 */
public enum Direction {
    NORTH(0,-1),
    SOUTH(0,1),
    EAST(1, 0),
    WEST(-1, 0),
    CENTER(0, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns a random direction
     *
     * @return random Direction of NORTH, SOUTH, EAST and WEST
     */
    public static Direction randDir(Random rng){
        Direction[] all = allDirections();
        return all[rng.nextInt(all.length)];
    }

    /**
     * Returns the four cardinal movement directions (no CENTER).
     *
     * @return array of NORTH, SOUTH, EAST and WEST
     */
    public static Direction[] allDirections(){
        return new Direction[]{NORTH, SOUTH, EAST, WEST};
    }
}
