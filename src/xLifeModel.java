import ZombieSim.Entities.Zombie;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class xLifeModel extends JPanel {
    private char[][] grid;
    private int rows;
    private int cols;
    private int cellSize = 20;
    private xZombie zombie;

    public xLifeModel(String fileName) throws FileNotFoundException {

        Scanner input = new Scanner(new File(fileName));
        this.rows = input.nextInt();
        this.cols = input.nextInt();
        grid = new char[rows][cols];

        int i = 0;
        while (input.hasNextLine() && i <= rows ) {
            String line = input.nextLine();
            if(!line.isEmpty() && line.charAt(0)!='#'){
                for(int j = 0; j< cols;j++){
                    grid[i][j] = line.charAt(j);
                }
                i++;
            }
        }

        //this.zombie = new xZombie(rows,cols,grid);
        this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }
    public void setCell(int r, int c, char value) { grid[r][c] = value; }

    public void update() {
        char[][] nextGen = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Need to check move all object
                // Then update nextGen

                // Zombies
                if (grid[i][j] == 'Z') {
                    //zombie.search(i,j);
                }
            }
        }
        grid = nextGen;
    }

    // for saving current Map and final MAP
    @Override
    public String toString() {
        String retStr = "";

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                retStr+= grid[r][c];
            }
            // Add a newline character at the end of each row
            retStr+="\n";
        }
        return retStr;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Wall
                if (grid[r][c]=='X') {
                    g.setColor(Color.GRAY);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                // Weapon
                if (grid[r][c]=='W') {
                    g.setColor(Color.ORANGE);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                // SafeZone
                if (grid[r][c]=='O') {
                    g.setColor(Color.CYAN);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                // Zombies
                if (grid[r][c]=='Z') {
                    g.setColor(Color.RED);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                // Human
                if (grid[r][c]=='H') {
                    g.setColor(Color.GREEN);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                // Soldier
                if (grid[r][c]=='S') {
                    g.setColor(Color.BLUE);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }

                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}