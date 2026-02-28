// ColonyAnt.java

import java.util.Random;

public class ColonyAnt extends Ant {
    private final Point home;
    private Point foodStore;

    public ColonyAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy);
        if (home == null) throw new IllegalArgumentException("home");
        this.home = home;
    }

    public final Point getHome() { return home; }

    public final Point getFoodStore() { return foodStore; }

    public final void setFoodStore(Point foodStore) { this.foodStore = foodStore; }

    public Direction returnHome() { return pathFind(home); }

    public Direction depositFood() {
        if (foodStore == null) return Direction.CENTER;
        //TODO: handle logic when foodStore is null better
        return pathFind(foodStore);
    }
}