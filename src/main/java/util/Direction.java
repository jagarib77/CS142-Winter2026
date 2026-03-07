package util;// util.Direction.java
// Cardinal movement directions used by ants and pheromone spreading.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

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

    // Returns a Direction to move from the current Point to the destination Point.
    // By Kyle
    public static Direction moveToPoint(Point current, Point destination) {
        int dx = destination.x - current.x;
        int dy = destination.y - current.y;

        // If the current Point and destination Point are at the same location, then CENTER will
        // be returned.
        if (dx == 0 && dy == 0) {
            return CENTER;
        }

        // If the magnitudes of the horizontal and vertical distances between the Points are
        // equal, then it will return a random Direction towards the destination.
        if (Math.abs(dx) == Math.abs(dy)) {
            if (Math.random() >= 0.5) {
                dx = 0;
            } else {
                dy = 0;
            }
        }

        // If the magnitude of the horizontal distance between the Points is larger than the
        // vertical, then it will return a horizontal Direction towards the destination.
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0) {
                return WEST;
            } else {
                return EAST;
            }
        } else {
            // If the magnitude of the vertical distance between the Points is larger than the
            // horizontal, then it will return a vertical Direction towards the destination.
            if (dy < 0) {
                return NORTH;
            } else {
                return SOUTH;
            }
        }
    }
}
