public enum Direction8 {
    UP(-1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    LEFT(0, -1),
    RIGHT(0, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    final int dx;
    final int dy;

    Direction8(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
}
