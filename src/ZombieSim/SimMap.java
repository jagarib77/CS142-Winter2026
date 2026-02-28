package ZombieSim;

import ZombieSim.Entities.*;

import java.awt.*;

public class SimMap {
    private final int size;
    private MapTile[][] simMap;

    public SimMap(int size) {
        this.size = size;
        createMap();
    }

    private void createMap() {
        simMap = new MapTile[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                simMap[row][col] = new MapTile(new Point(col + 1, size - row));
            }
        }
    }

    public void spawn(Entity unit, Point p) {
        if(outBounds(p)) {return;}
        if(getUnit(p) != null) {return;}
        tileAt(p).setEntity(unit);
        unit.setPosition(new Point(p));
        unit.setBound(size);
    }

    public boolean move(Entity unit, Direction direction) {
        Point from = unit.getLocation();

        Point to = new Point(
                from.x + direction.dx(),
                from.y + direction.dy()
        );

        if(outBounds(to)) {return false;}
        if(!isEmpty(to)) {return false;}

        tileAt(from).setEntity(null);
        tileAt(to).setEntity(unit);

        unit.setPosition(new Point(to));

        return true;
    }

    public boolean outBounds(Point p) {
        if(p == null) {return true;}
        return (p.x < 1 || p.x > size) || (p.y < 1 || p.y > size);
    }

    private MapTile tileAt(Point p) {
        int col = p.x-1;
        int row = size-p.y;
        return simMap[row][col];
    }

    public Entity getUnit(Point p) {
        if(outBounds(p)) {return null;}
        return tileAt(p).getEntity();
    }

    public boolean isEmpty(Point p) {
        return tileAt(p).getEntity() == null;
    }

    public int size(){
        return size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = size; y >=1; y--) {
            for (int x = 1; x <= size; x++) {
                Entity e = getUnit(new Point(x,y));
                if(e != null)
                    sb.append(e);
                else
                    sb.append("-");
            }
            if(y > 1) {sb.append("\n");}
        }
        return sb.toString();
    }

    public static class MapTile {

        private final Point p;

        Entity tileUnit;

        MapTile(Point p) {
            this.p = new Point(p);
            tileUnit = null;
        }
        public Entity getEntity() {
            return tileUnit;
        }
        public void setEntity(Entity tileUnit) {
            this.tileUnit = tileUnit;
        }
        public int getX() {return p.x;}
        public int getY() {return p.y;}
        public Point getPoint() {return new Point(p);}

    }
}
