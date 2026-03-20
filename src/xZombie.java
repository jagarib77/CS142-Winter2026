import java.awt.Point;
import java.util.ArrayList;

/**
 * Represents a zombie in the simulation.
 *
 * Zombies can move around the board and infect nearby humans.
 * If a zombie finds a human in a neighboring cell, it converts
 * that human into a zombie.
 *
 * @author Lay Leehout
 * @version 1.0
 */
public class xZombie extends xHuman {

    /**
     * Constructs a zombie at the given position.
     *
     * @param r the starting row
     * @param c the starting column
     */
    public xZombie(int r, int c) {
        super(r, c);
    }

    /**
     * Returns the map symbol for a zombie.
     *
     * @return the string "Z"
     */
    public String toString() {
        return "Z";
    }

    /**
     * Searches for a human at the given position in the human list.
     *
     * @param x the row to search
     * @param y the column to search
     * @param humanList the list of humans
     * @return the index of the human if found, or -1 if not found
     */
    public int searchHuman(int x, int y, ArrayList<xHuman> humanList) {
        for (int i = 0; i < humanList.size(); i++) {
            if (x == humanList.get(i).getX() && y == humanList.get(i).getY()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first human in the neighboring cells around the zombie.
     *
     * The search checks all cells in a 1-tile radius around the zombie,
     * excluding the zombie's own position.
     *
     * @param r the zombie's row
     * @param c the zombie's column
     * @param rows the total number of rows in the grid
     * @param cols the total number of columns in the grid
     * @param humanList the list of humans
     * @return the position of the first neighboring human, or null if none found
     */
    public Point findFirstHumanNeighbour(int r, int c, int rows, int cols, ArrayList<xHuman> humanList) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == r && j == c)) {
                    if (searchHuman(i, j, humanList) != -1) {
                        return new Point(i, j);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Performs this zombie's action for one simulation step.
     *
     * The zombie first checks for nearby humans to infect. If a human is found,
     * that human is removed and replaced with a new zombie. If no human is found,
     * the zombie moves randomly to a valid location.
     *
     * @param humanList the list of humans
     * @param zombieList the list of zombies
     * @param grid the simulation grid
     * @param rows the total number of rows
     * @param cols the total number of columns
     * @param model the game model controlling the simulation
     */
    public void act(
            ArrayList<xHuman> humanList,
            ArrayList<xZombie> zombieList,
            char[][] grid,
            int rows,
            int cols,
            xLifeModel model
    ) {
        int r = getX();
        int c = getY();

        Point humanPos = findFirstHumanNeighbour(r, c, rows, cols, humanList);

        if (humanPos != null) {
            int hIndex = searchHuman(humanPos.x, humanPos.y, humanList);
            if (hIndex != -1) {
                humanList.remove(hIndex);
                zombieList.add(new xZombie(humanPos.x, humanPos.y));
                grid[humanPos.x][humanPos.y] = 'Z';
                model.increaseZombiePoint();
            }
            grid[r][c] = 'Z';
            return;
        }

        grid[r][c] = '-';
        Point newPos = model.getNewPosition(this, r, c);
        setX(newPos.x);
        setY(newPos.y);
        grid[newPos.x][newPos.y] = 'Z';
    }
}