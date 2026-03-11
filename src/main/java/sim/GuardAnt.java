package sim;// sim.GuardAnt.java
// Colony ant role intended for defending the nest and attacking threats.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import java.util.List;
import java.util.Random;
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
    public boolean attack(List<Ant> ants){
        for(Ant a:ants){
            if(a == this) continue;
            if(!(a instanceof ColonyAnt enemy)) continue; // only ants with ID's

            if(enemy.getColonyId() == getColonyId()) continue;

            if(enemy.getPoint().equals(getPoint())){
                if(rng().nextDouble() < 0.66) enemy.kill();
                return true;
            }
        }
        return false;
    }
}