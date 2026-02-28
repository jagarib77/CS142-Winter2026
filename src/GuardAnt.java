// GuardAnt.java
// Colony ant role intended for defending the nest and attacking threats.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

import java.util.Random;

/**
 * Guard ant role. Intended responsibilities include patrol and combat.
 * Currently provides a spawn helper and symbol override.
 */
public class GuardAnt extends ColonyAnt {

    /**
     * Creates a guardAnt ant with a home location.
     */
    public GuardAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy, home);
    }

    /**
     * Convenience factory used by the queen to create a guard ant.
     *
     * @return newly created GuardAnt instance
     */
    public static GuardAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        return new GuardAnt(world, rng, pos, maxEnergy, home);
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
    public boolean attack(){
        return true;
    }
}