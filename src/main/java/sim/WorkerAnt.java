package sim;// sim.WorkerAnt.java
// Colony ant role intended for foraging and nest expansion behaviors.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler and Dmytro Shyliuk

import resources.WorldObject;
import util.Point;

import java.util.List;
import java.util.Random;
import terrain.Dirt;
import terrain.Terrain;
import terrain.Tunnel;
import util.Direction;

/**
 * Worker ant role. Intended responsibilities include foraging and digging tunnels.
 * Currently provides a spawn helper and symbol override.
 */
public class WorkerAnt extends ColonyAnt {
    /**
     * Creates a worker ant at a position with a home location.
     */
    public WorkerAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        super(world, rng, pos, maxEnergy, home, colonyID);
    }

    /**
     * Initializes a worker ant with a given starting position, maximum energy capacity, current
     * energy, held item, whether the ant is alive, its previous locations, its home's location,
     * its colonyID, and its food storage's location.
     *
     * @param world world the ant lives in (non-null)
     * @param rng random generator used for decisions (non-null)
     * @param pos initial position (non-null)
     * @param maxEnergy maximum energy capacity, must be > 0
     * @param heldItem the WorldObject the nt is holding
     * @param alive boolean for whether the ant is alive
     * @param lastLocations list storing ant's last locations in the world
     * @param home location of the home point
     * @param colonyID a number used to identify ants of its own colony
     * @param foodStore location of the food store (always at home point currently)
     */
    public WorkerAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, int currentEnergy,
                    WorldObject heldItem, boolean alive, List<Point> lastLocations, Point home,
                    int colonyID, Point foodStore) {
        super(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive, lastLocations, home, colonyID, foodStore);
    }

    /**
     * Convenience factory used by the queen to create a worker ant.
     *
     * @return newly created sim.WorkerAnt instance
     */
    public static WorkerAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        return new WorkerAnt(world, rng, pos, maxEnergy, home, colonyID);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    public boolean forage(){
        if(getHeldItem() != null) return false;
        return pickupObject();
    }

    // Dmytro
    // Worker ant can move to a point that contains dirt and tunnel. Fixes the problem
    // of worker ants being stuck when holding sugar; returning home. Or just in general
    // traveling.
    @Override
    public boolean canMoveTo(Point p) {
        if (!world().inBounds(p)) return false;

        Terrain t = world().getTerrainAt(p);

        // tunnel and dirt is always walkable for Worker Ants
        // air and rock is always not walkable
        return t instanceof Tunnel || t instanceof Dirt;
    }

    /**
     * Attempts to move one tile in the given direction based on world movement rules.
     * Movement costs energy: -1 if not carrying an item, -2 if carrying an item.
     *
     * @param dir direction to move
     * @return true if the move succeeded, false otherwise
     */
    //Dmytro
    //Overidden move function, that in addition calls the dig function to
    //remove dirt to move to a location
    public boolean move(Direction dir){
        Point self = getPoint();
        Point next = self.add(dir);
        // If failed to dig the dirt up, return false. Either out of bounds or energy
        if (world().getTerrainAt( next ) instanceof Dirt) {
            if (!world().dig(this, next))
                return false;
        }
        return super.move(dir);
    }

}
