// AntSim.java

import java.util.ArrayList;
import java.util.List;
import java.util.*;

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

    // Prints the introduction - kyle
    public static void printIntro() {
        System.out.println("Welcome to the Ant Colony Simulator!");
        System.out.println("This program creates a world with ant colonies and simulates ants'");
        System.out.println("behaviors like foraging, fighting, and procreating.");
        System.out.println();
    }

    public WorldGrid getWorld() { return world; }

    public List<Ant> getAnts() { return ants; }

    // simulation tick
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

    // setup helpers
    private void setupWorld() {
        int width = world.getWidth();
        int height = world.getHeight();

        // Sets up the Terrain grid - Kyle
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if ((y>=height/16*7 && y<=height/16*9) && (x>=width/16*7 && x<=width/16*9)) {
                    // Sets Terrain to Tunnel
                    world.setTerrain(new Point(x, y), new Tunnel());
                } else if (y<=5) {
                    // Sets Terrain to Air
                    world.setTerrain(new Point(x, y), new Air());
                } else {
                    // Sets Terrain to Dirt
                    world.setTerrain(new Point(x, y), new Dirt());
                }
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

    private void setupWorldObjects() {
        //TODO: need to spawn WorldObjects like sugar, unless if none should be spawned
    }

    private void setupAnts() {
        //TODO: need to spawn the first ants in the sim (at least a queen)
    }

    // Asks for and returns a number between start and end (inclusive).
    public static int getIntegerBetweenRange(Scanner console, int start, int end) {
        System.out.print("Input an integer between " + start + " to " + end + " (inclusive): ");
        String userInput = console.next();
        try {
            int num = Integer.parseInt(userInput);

            if (num >= start && num <= end) {
                System.out.println();
                return num;
            } else {
                System.out.println("Integer not in range.");
                return getIntegerBetweenRange(console, start, end);
            }
        } catch (Exception e){
            System.out.println("Invalid input.");
            return getIntegerBetweenRange(console, start, end);
        }
    }

    // main file - runs the program
    // create the world sim then pass that world to AntSimGUI.java to run the
    // GUI so we can see what happens
    public static void main(String[] args) {
        AntSim sim = new AntSim(50, 50);

        printIntro();

        /*
        I did not know if we want to make the user be able to input some variables, like the size
        of the simulation and the number of ants, but I included this if you do want to. If you
        do not want to, then you can delete this. - Kyle
        Scanner console = new Scanner(System.in);

        System.out.println("How many queen ants would you like?");
        int queenAntNum = getIntegerBetweenRange(console, 1, 3);

        System.out.println("How many guard ants would you like?");
        int guardAntNum = getIntegerBetweenRange(console, 1, 35);

        System.out.println("How many worker ants would you like?");
        int workerAntNum = getIntegerBetweenRange(console, 1, 70);
        */

        new AntSimGUI(sim);

    }
}
