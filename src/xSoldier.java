// CS 142 xSoldier
// ----------------------------------------------------------------------------
// xSoldier objects
// Color  : BLUE
// Move   : random direction(N,E,S,W)
// ----------------------------------------------------------------------------

import java.awt.*;
import java.util.Random;
/**
 * This class represents a Soldier in the game.
 * A soldier is a type of human that can use bullets.
 * It moves like a human but has extra abilities.
 */
// For reusing all methods, states of xHuman, need to be extends xHuman
public class xSoldier extends xHuman {
    /** The current direction the soldier is moving */
    private Direction currDir;
    /** Number of bullets the soldier has */
    private int bullets; // the maximum number of food

    // -----------------------OVERRIDE METHODS---------------------------------
    /**
     * Create a soldier at a specific position.
     *
     * @param x the x position
     * @param y the y position
     */
    public xSoldier(int x, int y){
        super(x,y);
        bullets = 2;
    }

    /**
     * Get the color of the soldier.
     *
     * @return BLUE color
     */
    public Color getColor() {
        return Color.BLUE;
    }

    /**
     * Use one bullet.
     * This decreases the number of bullets by 1.
     */
    public void updateBullets() {
        this.bullets -= 1;
    }

    /**
     * Get the number of bullets left.
     *
     * @return number of bullets
     */
    public int getBullets() {
        return bullets;
    }

    // ----------------------- END OVERRIDE METHODS ---------------------------
}