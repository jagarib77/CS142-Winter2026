import java.awt.*; // for color


public class Entity {
    private int x;
    private int y;

    public Direction move() { return Direction.CENTER; }

    public Color getColor() {
        return Color.BLACK;
    }

    public static enum Direction {
        NORTH, SOUTH, EAST, WEST, CENTER
    }

    public boolean hasActed() {
        return false;
    }

    // Returns this entity's current x-coordinate.
    public final int getX() {
        return x;
    }

    // Returns this entity's current y-coordinate.
    public final int getY() {
        return y;
    }
    
}