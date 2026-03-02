import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class xHuman extends xEntity {

    private Random rand = new Random();
    private boolean inSafeZone = false;

    // 👇 Human awareness (set by model each update)
    private List<Point> safeZones = new ArrayList<>();
    private List<Point> weapons = new ArrayList<>();

    public xHuman(int x, int y){
        super();
        setX(x);
        setY(y);
    }

    // Model will call this every turn (or once after loading)
    public void setAwareness(List<Point> safeZones, List<Point> weapons) {
        this.safeZones = safeZones;
        this.weapons = weapons;
    }

    @Override
    public Direction move() {
        // If human is in safe zone, don't move
        if (inSafeZone) return Direction.CENTER;

        int r = getX();
        int c = getY();

        // 1) Look for a weapon within 2 tiles (priority)
        Point w = findClosestInRadius(weapons, r, c, 2);
        if (w != null) return stepToward(r, c, w.x, w.y);

        // 2) Otherwise look for a safezone within 2 tiles
        Point s = findClosestInRadius(safeZones, r, c, 2);
        if (s != null) return stepToward(r, c, s.x, s.y);

        // 3) Else random
        int k = rand.nextInt(4);
        switch (k) {
            case 0: return Direction.NORTH;
            case 1: return Direction.SOUTH;
            case 2: return Direction.EAST;
            case 3: return Direction.WEST;
        }
        return Direction.CENTER;
    }

    // Find closest point within a square radius (Chebyshev <= radius)
    private Point findClosestInRadius(List<Point> list, int r, int c, int radius) {
        Point best = null;
        int bestDist = Integer.MAX_VALUE;

        for (Point p : list) {
            int dr = Math.abs(p.x - r);
            int dc = Math.abs(p.y - c);

            // within 2-tile radius (square around you)
            if (dr <= radius && dc <= radius) {
                int manhattan = dr + dc;
                if (manhattan < bestDist) {
                    bestDist = manhattan;
                    best = p;
                }
            }
        }
        return best;
    }

    // Take ONE step that reduces distance to target
    private Direction stepToward(int r, int c, int tr, int tc) {
        int dr = tr - r;
        int dc = tc - c;

        // Choose the axis with bigger distance first
        if (Math.abs(dc) > Math.abs(dr)) {
            return (dc > 0) ? Direction.EAST : Direction.WEST;
        } else if (dr != 0) {
            return (dr > 0) ? Direction.SOUTH : Direction.NORTH;
        } else {
            return Direction.CENTER;
        }
    }

    public void enterSafeZone() {
        inSafeZone = true;
    }

    @Override
    public String toString() {
        return "H";
    }
}