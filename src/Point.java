// Point.java
// Immutable integer (x, y) coordinate used throughout the grid.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

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
     * Returns a new Point offset by the direction's dx and dy.
     *
     * @param dir direction offset
     * @return new point equal to this + dir
     */
    public Point add(Direction dir){
        return new Point(x+dir.dx, y+dir.dy);
    }
}