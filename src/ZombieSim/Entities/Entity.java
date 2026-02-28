package ZombieSim.Entities;

import java.awt.*;
import java.util.Random;

public class Entity {
    public Point p;
    public int size;
    Random rand = new Random();

    public Entity() {
    }

    public void setPosition(Point p) {
        this.p = new Point(p);
    }

    public void setBound(int size){this.size = size;}
    public int getX() {return p.x;}
    public int getY() {return p.y;}
    public Point getLocation() {return new Point(p);}

    //Add Movement "ai" that chases other entities

    //die

    //convert


}
