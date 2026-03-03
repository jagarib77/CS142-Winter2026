// AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
        // basic random movement
        for (Ant a:ants){
            if (a == null) continue;
            a.move(Direction.randDir(rng));
        }

        // random chance for each queen to spawn an ant
        ListIterator<Ant> it = ants.listIterator(); // safe iterator add
        while (it.hasNext()) {
            Ant a = it.next();
            if (a instanceof QueenAnt q) {
                if (rng.nextInt(50) == 0) {
                    it.add(q.spawnAnt()); // safe add
                }
            }
        }

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

        //TODO: if all ants are dead then end sim and print to GUI the message ->
        // (all ants are dead) goodbye

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

        // Carve a centered square room of Tunnel tiles.
        // 3x3 if size is odd, 4x4 if size is even.
        int roomSize = (width%2 == 0) ? 4 : 3;
        int half = roomSize/2;

        // For odd (3): cx = w/2, start = cx-1, end = cx+1
        // For even (4): cx = w/2, start = cx-2, end = cx+1
        int startX = (width/2) - half;
        int startY = (height/2) - half;
        int endX = startX + roomSize-1;
        int endY = startY + roomSize-1;

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
                world.setTerrain(new Point(x, y), new Tunnel());
            }
        }

        // tunnel from starting nest to surface, either 1 or 2 wide depending on size of world
        startX = (width/2)-1;
        startY = 0; // top of map
        endX = startX+half-1;
        endY = (width/2);

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
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
        AntSim sim = new AntSim(51, 51);
        new AntSimGUI(sim);
    }
}
