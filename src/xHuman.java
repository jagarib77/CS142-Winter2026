import java.awt.*;
import java.util.Random;

/**
 * This class represents a Human in the game.
 * Humans move randomly and can stop moving when they are in a safe zone.
 */
public class xHuman extends xEntity {
    /**
     * Create a human at a specific position.
     *
     * @param x the x position
     * @param y the y position
     */
    public xHuman(int x, int y) {
        super();
        setX(x);
        setY(y);
    }

    /**
     * Decide how the human moves.
     * The human moves randomly unless it is in a safe zone.
     *
     * @return direction of movement, or CENTER if not moving
     */
    public Direction move() {
        Random rand = new Random();
        int r = rand.nextInt(4);
        switch (r) {
            case 0: return Direction.NORTH;
            case 1: return Direction.SOUTH;
            case 2: return Direction.EAST;
            case 3: return Direction.WEST;
        }
        return Direction.CENTER;
    }

    /**
     * Get the color of the human.
     *
     * @return Green color
     */
    public Color getColor() {
        return Color.GREEN;
    }

    /**
     * Convert the human to a simple letter.
     *
     * @return "H"
     */
    public String toString() {
        return "H";
    }
}