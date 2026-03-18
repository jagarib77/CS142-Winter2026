package sim;// sim.WorkerAnt.java
// Colony ant role intended for foraging and nest expansion behaviors.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import util.Point;

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
    
    // Dmytro and Harrisson 
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
    //Dmytro and Harrisson 
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