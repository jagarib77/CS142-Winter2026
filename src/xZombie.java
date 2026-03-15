import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;

public class xZombie extends xHuman {

    public xZombie(int r, int c) {
        super(r, c);
    }

    public String toString() {
        return "Z";
    }

    // Search humans in the list
    public int searchHuman(int x, int y, ArrayList<xHuman> humanList) {
        for (int i = 0; i < humanList.size(); i++) {
            if (x == humanList.get(i).getX() && y == humanList.get(i).getY()) {
                return i;
            }
        }
        return -1;
    }

    // Find first human in  neighbor area
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

    // One zombie takes its turn
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

        // Try tagging first
        Point humanPos = findFirstHumanNeighbour(r, c, rows, cols, humanList);

        if (humanPos != null) {
            int hIndex = searchHuman(humanPos.x, humanPos.y, humanList);
            if (hIndex != -1) {
                humanList.remove(hIndex);
                zombieList.add(new xZombie(humanPos.x, humanPos.y));
                grid[humanPos.x][humanPos.y] = 'Z';
            }
            grid[r][c] = 'Z';
            return;
        }

        // No human found, move randomly
        grid[r][c] = '-';
        Point newPos = model.getNewPosition(this, r, c);
        setX(newPos.x);
        setY(newPos.y);
        grid[newPos.x][newPos.y] = 'Z';
    }
}