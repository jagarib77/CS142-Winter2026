package pheromones;// pheromones.PheromoneType.java
// Enumerates the pheromone channels used by the simulation.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Pheromone channels used by ants for navigation and behavior decisions.
 * The integer field is used as an index into the pheromone array.
 */
public enum PheromoneType {
    DANGER(0),
    WALKING_TRAIL(1),
    FOOD(2);

    public final int type;

    PheromoneType(int type){
        this.type = type;
    }
}