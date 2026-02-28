// Ant.java

import java.util.Random;

public abstract class Ant {
    protected static final int DEFAULT_MAX_ENERGY = 50;

    private final WorldGrid world;
    private final Random rng;

    private int currentEnergy;
    private final int maxEnergy;

    private int x;
    private int y;

    private WorldObject heldItem = null;
    private boolean alive = true;

    public Ant(WorldGrid world, Random rng, Point pos, int maxEnergy){
        if (world == null) throw new IllegalArgumentException("world is null");
        if (rng == null) throw new IllegalArgumentException("rng is null");
        if (pos == null) throw new IllegalArgumentException("pos is null");
        if (maxEnergy <= 0) throw new IllegalArgumentException("maxEnergy is <= 0");

        this.world = world;
        this.rng = rng;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
        this.x = pos.x;;
        this.y = pos.y;
    }

    protected final WorldGrid world() { return world; }
    protected final Random rng() { return rng; }

    public final int getX() { return x; }
    public final int getY() { return y; }
    public final Point getPoint() { return new Point(x, y); }

    public final boolean isAlive() { return alive; }
    public final int getEnergy() { return currentEnergy; }
    public char getSymbol() { return 'A'; }
    public WorldObject getHeldItem(){ return heldItem; }

    protected final void changeEnergy(int delta) {
        currentEnergy += delta;
        if (currentEnergy <= 0) kill();
    }

    public final void kill(){
        if (!alive) return;
        if (heldItem != null) dropItem();
        alive = false;
    }

    public boolean dropItem(){
        if (heldItem == null || world.getObjectAt(getPoint()) != null) return false;
        world.setObjectAt(getPoint(), heldItem);
        heldItem = null;
        return true;
    }

    // returns true if the ant moved
    public boolean move(Direction dir){
        Point self = getPoint();
        if (world.canMoveTo(self.add(dir))){
            x += dir.dx;
            y += dir.dy;
            // costs extra to move while holding an item
            changeEnergy(heldItem == null ? -1 : -2);
            return true;
        }
        return false;
    }

    // picks up an object from the ant's current tile.
    // returns true if pickup succeeded.
    public boolean pickupObject() {
        if (heldItem != null || !world.getObjectAt(getPoint()).isCarryable()) return false;
        heldItem = world.takeObject(getPoint());
        changeEnergy(-5);
        return true;
    }

    // returns true if the ant ate
    public boolean eat(){
        if (!heldItem.isEdible()) return false;
        changeEnergy(heldItem.energyValue());
        heldItem = null;
        return true;
    }

    public Direction pathFind(Point target){
        //TODO: complete pathfinding
        return Direction.CENTER;
    }
}