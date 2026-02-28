import java.awt.*;

public class xSafeZone extends xEntity {


    public Direction move() {
        return Direction.CENTER;   // does not move
    }


    public Color getColor() {
        return Color.CYAN;
    }

    public boolean canEnter(xEntity other) {
        return (other instanceof xHuman) || (other instanceof xSoldier);// only human and soldier can enter
    } //public boolean canEnter(xEntity other) {return pass();} in xEntity

    public boolean pass() {
        return false;
    }
}