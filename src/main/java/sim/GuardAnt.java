package sim;// sim.GuardAnt.java
// Colony ant role intended for defending the nest and attacking threats.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import java.util.List;
import java.util.Random;

import resources.WorldObject;
import util.Point;

/**
 * Guard ant role. Intended responsibilities include patrol and combat.
 * Currently provides a spawn helper and symbol override.
 */
public class GuardAnt extends ColonyAnt {

    /**
     * Creates a guardAnt ant with a home location.
     */
    public GuardAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        super(world, rng, pos, maxEnergy, home, colonyID);
    }

    /**
     * Initializes a guard ant with a given starting position, maximum energy capacity, current
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
    public GuardAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, int currentEnergy,
                     WorldObject heldItem, boolean alive, List<Point> lastLocations, Point home,
                     int colonyID, Point foodStore) {
        super(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive, lastLocations, home, colonyID, foodStore);
    }

    /**
     * Convenience factory used by the queen to create a guard ant.
     *
     * @return newly created sim.GuardAnt instance
     */
    public static GuardAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        return new GuardAnt(world, rng, pos, maxEnergy, home, colonyID);
    }

    @Override
    public char getSymbol() {
        return 'G';
    }

    /**
     * TODO: Implement combat behavior for guard ants.
     *
     * Intended logic (high-level outline):
     * - Check adjacent tiles for enemy ants or threats.
     * - If a target is found:
     *   - Apply damage or remove the target from the simulation.
     *   - Optionally reduce this ant's energy as a cost of attacking.
     *   - Optionally deposit a DANGER pheromone at the location.
     * - If no target is found:
     *   - Return false (no attack performed).
     *
     * Considerations:
     * - Define what qualifies as an "enemy" (different colony or type).
     * - Decide attack range (currently likely adjacent tiles only).
     * - Ensure interactions do not cause concurrent modification issues
     *   when removing ants from the simulation list.
     *
     * @return true if an attack action occurred, false otherwise
     */
    public boolean attack(Ant a){
        if(!(a instanceof ColonyAnt enemy)) return false;
        if(enemy.getColonyId() == getColonyId()) return false;

        if(enemy.getPoint().equals(getPoint())){
            if(rng().nextDouble() < 0.66) enemy.kill();
            return true;
        }
        return false;
    }
}
