package resources;

import util.Point;

/**
 * Placeable colony object.
 */
public class Colony extends WorldObject {
    private final int colonyId;
    private final Point home;

    public Colony(int colonyId, Point home) {
        if (home == null) throw new IllegalArgumentException("home is null");
        this.colonyId = colonyId;
        this.home = home;
    }

    public int getColonyId() {
        return colonyId;
    }

    public Point getHome() {
        return home;
    }

    @Override
    public char getSymbol() {
        return 'C';
    }
}