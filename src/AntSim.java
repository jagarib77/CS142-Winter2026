// AntSim.java

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntSim {
    private final WorldGrid world;
    private final Random rng;
    private final List<Ant> ants;

    public AntSim(int width, int height) {
        this(width, height, new Random());
    }

    public AntSim(int width, int height, Random rng) {
        if (rng == null) throw new IllegalArgumentException("rng");

        this.world = new WorldGrid(width, height);
        this.rng = rng;
        this.ants = new ArrayList<>();

        setupWorld();
        setupAnts();
    }

    public WorldGrid getWorld() { return world; }

    public List<Ant> getAnts() { return ants; }

    // simulation tick
    public void step() {
        //TODO: implement queen spawning other ants loop through all Ants in ants
        // and check if ant is queenAnt then use queenAnt.spawnAnt(); to create
        // a new ant and .add() it to the ant list

        //TODO: remove dead ants from ant list

        //TODO: implement logic for ants to move
        // don't worry about this yet, we need to get pheromones working
        // follow pheromones when possible otherwise move random and wait for trigger

        // pheromones update
        world.spreadPheromones(.01); // 1% spread per tick
        world.decayPheromones(.99); // 1% loss per tick
    }

    // setup helpers
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
            for (int x=0; x<=width; ++x) {
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

    private void setupAnts() {
        //TODO: need to spawn the first ants in the sim (at least a queen)
    }

    // main file - runs the program
    // create the world sim then pass that world to AntSimGUI.java to run the
    // GUI so we can see what happens
    public static void main(String[] args) {
        AntSim sim = new AntSim(50, 50);
        new AntSimGUI(sim);
    }
}