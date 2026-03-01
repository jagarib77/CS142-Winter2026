// CS 142 xSoldier
// ----------------------------------------------------------------------------
// xSoldier objects
// Color  : BLUE
// Move   : random direction(N,E,S,W)
// ----------------------------------------------------------------------------

import java.awt.*;
import java.util.Random;

// For reusing all methods, states of xEntity, need to be extends xEntity
public class xSoldier extends xHuman {
	// State / Field of xSoldier
    private Direction currDir;
    private int bullets; // the maximum number of food

    // -----------------------OVERRIDE METHODS---------------------------------
    // 0. Constructor not inheritance from xEntity
    public xSoldier(int x, int y){
        super(x,y);
        bullets = 1;
    }

    // 1. Color: BLUE
    public Color getColor() {
        return Color.BLUE;
    }


    public void updateBullets() {
        this.bullets -= 1;
    }

    public int getBullets() {
        return bullets;
    }
    /*
    // 2. Move random direction(N,E,S,W),
    public Direction move() {
        // Update currDir for 6 steps
        Random rand = new Random();
        int randomInt = rand.nextInt(4);
        if(randomInt == 0){
            currDir = Direction.NORTH;
        } else if (randomInt == 1) {
            currDir = Direction.EAST;
        } else if (randomInt == 2) {
            currDir = Direction.SOUTH;
        } else {
            currDir = Direction.WEST;
        }
        return currDir;
    }*/
    // ----------------------- END OVERRIDE METHODS ---------------------------
}