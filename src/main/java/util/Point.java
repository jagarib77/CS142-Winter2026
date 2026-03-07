package util;// util.Point.java
// Immutable integer (x, y) coordinate used throughout the grid.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

/**
 * Immutable coordinate pair for grid positions.
 * Points are value objects and should not be mutated after creation.
 */
public final class Point {
    public final int x;
    public final int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new util.Point offset by the direction's dx and dy.
     *
     * @param dir direction offset
     * @return new point equal to this + dir
     */
    public Point add(Direction dir){
        return new Point(x+dir.dx, y+dir.dy);
    }

    // Returns a boolean based on if the Object in the parameter is a Point object and ahs the
    // same fields as this Point.
    public boolean equals(Object o) {
        if (o != null || getClass() == o.getClass()) {
            Point other = (Point) o;
            return x == other.x && y == other.y;
        } else {
            return false;
        }
    }

    // Returns a Direction to move from the current Point to the target Point.
    // By Kyle
    public Direction moveToPoint(Point target) {
        int dx = target.x - x;
        int dy = target.y - y;

        // If the current Point and target Point are at the same location, then CENTER will
        // be returned.
        if (dx == 0 && dy == 0) {
            return Direction.CENTER;
        }

        // If the magnitudes of the horizontal and vertical distances between the Points are
        // equal, then it will return a random Direction towards the target.
        if (Math.abs(dx) == Math.abs(dy)) {
            if (Math.random() >= 0.5) {
                dx = 0;
            } else {
                dy = 0;
            }
        }

        // If the magnitude of the horizontal distance between the Points is larger than the
        // vertical, then it will return a horizontal Direction towards the target.
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0) {
                return Direction.WEST;
            } else {
                return Direction.EAST;
            }
        } else {
            // If the magnitude of the vertical distance between the Points is larger than the
            // horizontal, then it will return a vertical Direction towards the target.
            if (dy < 0) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        }
    }

    // Returns a Direction to move from the current Point away from the target Point.
    // By Kyle
    public Direction moveAwayFromPoint(Point target) {
        return moveToPoint(new Point(-target.x, -target.y));
    }

    // Returns the distance between this Point and the other Point in the parameter.
    // By Kyle
    public double getDistanceBetween(Point other) {
        int dx = other.x - x;
        int dy = other.y - y;

        return Math.sqrt(dx * dx + dy * dy);
    }
}
