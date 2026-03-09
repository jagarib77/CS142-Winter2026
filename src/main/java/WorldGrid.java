// WorldGrid.java
// Represents the 2D simulation world grid, storing terrain, world objects and pheromones.
// Provides methods for querying and modifying tiles, handling movement rules and managing
// interactions.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Base class for all world objects.
 * Subclasses define whether they are carryable, edible, their display symbol and energy value.
 */
public class WorldGrid {
    private final WorldObject[][] objects;
    private final Terrain[][] terrain;
    private final Pheromones pheromones;

    private final int width;
    private final int height;

    public WorldGrid(int width, int height){
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid grid size");
        }

        this.width = width;
        this.height = height;
        terrain = new Terrain[height][width];
        objects = new WorldObject[height][width];
        pheromones = new Pheromones(width, height);

        // Default: fill with Dirt
        for (int y=0; y<height; ++y) {
            for (int x=0; x<width; ++x) {
                terrain[y][x] = new Dirt();
            }
        }
        // objects is already null filled
        // pheromones is already null filled
    }

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public Pheromones getPheromones(){ return pheromones; }

    public void setTerrain(Point pos, Terrain type){
        if (!inBounds(pos) || type == null) return;
        terrain[pos.y][pos.x] = type;
    }

    public Terrain getTerrainAt(Point pos){
        if (!inBounds(pos)) return null;
        return terrain[pos.y][pos.x];
    }

    /**
     * TODO: Implement digging behavior.
     *
     * Intended logic (high-level outline):
     * - Check that the position is within bounds.
     * - Get the terrain at the position.
     * - If the terrain is Dirt:
     *   - Replace the terrain with a Tunnel (carve the space).
     *   - create a Dirt object and give it to the ant (if inventory allows).
     *   - apply an energy cost to the ant for digging.
     * - If the terrain is not Dirt:
     *   - Do nothing and return false.
     *
     * Considerations:
     * - Ants may only be able to carry one item at a time.
     * - Decide whether digging is restricted to certain ant types (e.g., WorkerAnt).
     * - Ensure terrain updates do not conflict with objects already on the tile.
     *
     * @param p position to dig
     * @return true if dirt was successfully dug and changed, false otherwise
     */
    // I worked on the dig method, but I am unsure if my code works or if I have the right
    // parameters, so I did not erase the instructions. Let me know or modify the code if something
    // is wrong.
    // - Kyle
    public boolean dig(WorkerAnt a, Point p){
        //TODO: check if Terrain at p is dirt, remove the dirt, then add dirt
        // into ant inventory. Need to make sure ant has room for dirt
        if (!inBounds(p)) {
            throw new IllegalArgumentException("Point is not within bounds.");
        }

        // Checks if the Terrain at the point is Dirt.
        if (terrain[p.y][p.x] instanceof Dirt) {
            // Checks if the WorkerAnt is carrying nothing and has enough energy.
            if (a.getHeldItem() == null && a.getEnergy() >= 15) {
                setTerrain(p, new Tunnel());

                // Reduces the Worker Ant's energy by 5 (technically 10, because it also picks up
                // the object, which costs 5 energy).
                a.changeEnergy(-5);
                a.move(a.getPoint().moveToPoint(p));

                // TODO: Write code to make the Ant pick up the Dirt (the code below does not work)
                // Picks up the WorldObject at the Point (Dirt).
                //a.pickupObject();

                return true;
            }
        }

        return false;
    }

    public void decayPheromones(double amount) {
        pheromones.decay(amount); // 1% loss each tick
    }

    public void spreadPheromones(double amount) {
        pheromones.spread(amount); // 1% loss each tick
    }

    public WorldObject getObjectAt(Point pos){
        if (!inBounds(pos)) return null;
        return objects[pos.y][pos.x];
    }

    public void setObjectAt(Point pos, WorldObject obj){
        if (!inBounds(pos)) return;
        objects[pos.y][pos.x] = obj;
    }

    /**
     * Removes and returns the object at the given position if it is carryable.
     *
     * @param pos position of the object
     * @return the removed object, or null if none exists or it is not carryable
     */
    public WorldObject takeObject(Point pos){
        if (!inBounds(pos)) return null;
        WorldObject obj = getObjectAt(pos);
        if (obj == null || !obj.isCarryable()) return null;
        setObjectAt(pos, null);
        return obj;
    }

    /**
     * Determines whether an ant can move into the given position based on terrain rules.
     *
     * @param p destination position
     * @return true if the position is traversable, false otherwise
     */
    public boolean canMoveTo(Point p) {
        if (!inBounds(p)) return false;

        Terrain t = terrain[p.y][p.x];

        // tunnel is always walkable
        if (t instanceof Tunnel) return true;

        // air is only walkable if supported from below
        if (t instanceof Air) {
            Point below = p.add(Direction.SOUTH);
            if (!inBounds(below)) return false;

            Terrain belowTerrain = terrain[below.y][below.x];
            return (belowTerrain != null) && (belowTerrain.isSolid());
        }

        return false;
    }

    public boolean inBounds(Point p){
        return  p != null &&
                p.x >= 0  && p.x < width &&
                p.y >= 0  && p.y < height;
    }

}
