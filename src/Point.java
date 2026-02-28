// Point.java

public final class Point {
    public final int x;
    public final int y;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point add(Direction dir){
        return new Point(x+dir.dx, y+dir.dy);
    }
}