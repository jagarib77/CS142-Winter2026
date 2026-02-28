import java.awt.Color;

import java.awt.*;

public class xWall extends xEntity {


    public Direction move() {
        return Direction.CENTER;
    }


    public Color getColor() {
        return Color.GRAY;
    }


    public boolean canEnter(xEntity other) {
        return false;   // blocks everyone
    }


    public boolean pass() {
        return false;
    }
    public String toString() {
        return "W";
    }
}