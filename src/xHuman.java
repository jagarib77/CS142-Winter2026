import java.awt.*;
import java.util.Random;

public class xHuman extends xEntity {

    private boolean acted = false;
    private Random rand = new Random();
    private boolean inSafeZone = false;

    public Direction move() {
        // Human can only act once per turn
        if (acted) {
            return Direction.CENTER;
        }

        acted = true;
        // Human don't move when is SafeZone
        if (inSafeZone) {
            return Direction.CENTER;
        }
        // Random movement
        int r = rand.nextInt(4);
        switch (r) {
            case 0: return Direction.NORTH;
            case 1: return Direction.SOUTH;
            case 2: return Direction.EAST;
            case 3: return Direction.WEST;
        }
        return Direction.CENTER;
    }
    public
    // this is when the human steps onto a safe zone tile
    public void enterSafeZone() {
        inSafeZone = true;
    }

    public Color getColor() {
        return Color.BLUE;   // Human color
    }


    public boolean hasActed() {
        return acted;
    }

    // Reset each simulation step
    public void resetActed() {
        acted = false;
    }

    public String toString() {
        return "H";
    }
}