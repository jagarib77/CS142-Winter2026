// AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main simulation model for the ant colony project.
 * Owns world state and the list of ants, and advances the simulation each tick.
 */
public class AntSim {
    private final WorldGrid world;
    private final Random rng;
    private final List<Ant> ants;

    /**
     * Creates a simulation with the given grid size and a default Random generator.
     *
     * @param width world width in tiles
     * @param height world height in tiles
     */
    public AntSim(int width, int height) {
        this(width, height, new Random());
    }

    /**
     * Creates a simulation with the given grid size and Random generator.
     * Sets up terrain and initial ants.
     *
     * @param width world width in tiles
     * @param height world height in tiles
     * @param rng random generator used for world setup and behaviors (non-null)
     */
    public AntSim(int width, int height, Random rng) {
        if (rng == null) throw new IllegalArgumentException("rng");

        this.world = new WorldGrid(width, height);
        this.rng = rng;
        this.ants = new ArrayList<>();

        setupWorld();
        setupAnts();
    }

    /**
     * Prints a short introduction to the console.
     * Written by Kyle.
     */
    public static void printIntro() {
        System.out.println("Welcome to the Ant Colony Simulator!");
        System.out.println("This program creates a world with ant colonies and simulates ants'");
        System.out.println("behaviors like foraging, fighting, and procreating.");
    }

    public WorldGrid getWorld() { return world; }

    public List<Ant> getAnts() { return ants; }

    /**
     * Advances the simulation by one tick.
     * Intended responsibilities:
     * - update ant behaviors (movement, actions, combat, etc.)
     * - spawn new ants from queens
     * - remove dead ants
     * - update pheromones (spread and decay)
     */
    public void step() {
        //TODO: implement queen spawning other ants loop through all Ants in ants
        // and check if ant is queenAnt then use queenAnt.spawnAnt(); to create
        // a new ant and .add() it to the ant list

        //TODO: remove dead ants from ant list
        // Removes dead ants and makes queen ants spawn other ants, but does not make them move
        // - Kyle
        for (int i = ants.size() - 1; i >= 0; i--) {
            Ant ant = ants.get(i);

            // Removes the Ant from the list if it is not alive.
            if (!ant.isAlive()) {
                ants.remove(i);
            }

            // If the Ant is a Queen Ant, then the Queen Ant will try to spawn a new Ant, which
            // will be added to the list.
            if (ant.getSymbol() == 'Q') {
                Ant newAnt = ((QueenAnt) ant).spawnAnt();
                if (newAnt != null) {
                    ants.add(newAnt);
                }
            }
        }

        //TODO: implement logic for ants to move
        // don't worry about this yet, we need to get pheromones working
        // follow pheromones when possible otherwise move random and wait for trigger

        // pheromones update
        world.spreadPheromones(.01); // 1% spread per tick
        world.decayPheromones(.99); // 1% loss per tick
    }

    /**
     * Initializes world terrain layout (nest area, surface air area and obstacles).
     * This method is called once during construction.
     */
    private void setupWorld() {
        int width = world.getWidth();
        int height = world.getHeight();

        // carve a rectangular room of tunnel so ants can move
        for (int y=height/16*7; y<=height/16*9; ++y) {
            for (int x=width/16*7; x<=width/16*9; ++x) {
                world.setTerrain(new Point(x, y), new Tunnel());
            }
        }

        // carve a rectangular room of air for the surface world
        for (int y=0; y<=5; ++y) {
            for (int x=0; x<width; ++x) {
                world.setTerrain(new Point(x, y), new Air());
            }
        }

        // Add a couple rocks as obstacles
        for (int r=0; r<25; ++r){
            Point rand = new Point(rng.nextInt(width), rng.nextInt(height));
            if (world.getTerrainAt(rand) instanceof Dirt){
                world.setTerrain(rand, new Rock());
            } else { --r; } // try again
        }
    }

    /**
     * Spawns the initial ants for the simulation (at minimum, a queen).
     * This method is called once during construction.
     */
    private void setupAnts() {
        //TODO: need to spawn the first ants in the sim (at least a queen)
        Point home = new Point(world.getWidth()/2, world.getHeight()/2);
        ants.add(QueenAnt.spawn(world, rng, home, 200, home));
    }

    /**
     * Program entry point. Creates the simulation and launches the GUI.
     * @param args unused
     */
    public static void main(String[] args) {
        printIntro();
        AntSim sim = new AntSim(50, 50);
        new AntSimGUI(sim);
    }
}
