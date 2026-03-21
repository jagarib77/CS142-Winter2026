import java.awt.*; // for Color
/**
 * Represents a generic entity in the simulation grid.
 * Each entity has an (x, y) position and can define movement behavior,
 * color, and whether it has acted in a turn.
 */
public class xEntity {

    /** The x position of the entity */
    private int x;

    /** The y position of the entity */
    private int y;

    /**
     * Determines the movement direction of this entity.
     *
     * @return the direction in which the entity moves (default is CENTER)
     */
    public Direction move() {
        return Direction.CENTER;
    }

    /**
     * Gets the color used to represent this entity.
     *
     * @return the color of the entity (default is WHITE)
     */
    public Color getColor() {
        return Color.WHITE;
    }

    /**
     * Enum representing possible movement directions.
     */
    public static enum Direction {
        NORTH, SOUTH, EAST, WEST, CENTER
    }

    /**
     * Returns this entity's current x position.
     *
     * @return the x position
     */
    public final int getX() {
        return x;
    }

    /**
     * Returns this entity's current y position.
     *
     * @return the y position
     */
    public final int getY() {
        return y;
    }

    /**
     * Sets the y position of this entity.
     *
     * @param y the new y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the x position of this entity.
     *
     * @param x the new x position
     */
    public void setX(int x) {
        this.x = x;
    }
}