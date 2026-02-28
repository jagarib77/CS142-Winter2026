import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class xLifeModel extends JPanel {
    private char[][] grid;
    private int rows;
    private int cols;
    private int cellSize = 20;
    private int safeCount = 0;
    private ArrayList<xSoldier> soldierList = new ArrayList<>();
    private ArrayList<xZombie> zombieList = new ArrayList<>();
    private ArrayList<xHuman> humanList = new ArrayList<>();
    private ArrayList<Point> safeZone= new ArrayList<>();

    // for read from file we just take
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
                    if(line.charAt(j) == 'S'){
                        soldierList.add(new xSoldier(i,j)) ;
                    }else if(line.charAt(j) == 'H'){
                        humanList.add(new xHuman(i,j)) ;
                    } else if(line.charAt(j) == 'Z'){
                        zombieList.add(new xZombie(i,j)) ;
                    } else if(line.charAt(j) == 'O') {
                        safeZone.add(new Point(i, j));
                    }
                    grid[i][j] = line.charAt(j);
                }
                i++;
            }
        }

        //this.zombie = new xZombie(rows,cols,grid);
        this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }
    public void setCell(int r, int c, char value) { grid[r][c] = value; }
    public void updateMap(int x, int y, char bef, char aft){
    }
    private int countNeighbors(int r, int c) {
        int count = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == r && j == c)) {
                    if (grid[i][j] == 'X') count++;
                }
            }
        }
        return count;
    }

    public boolean checkMoveValid(int x, int y){
        //System.out.println(x + " - " + y);
        if(y >= cols || y < 0){ return false; }
        if(x < 0 || x >= rows){ return false; }
        if(grid[x][y] == 'X'){ return false; } // Wall
        return true;
    }

    // Can use for all object to get move
    public Point getNewPosition(xEntity target,int x, int y){
        int x1 = 0;
        int y1 = 0;
        boolean isMove = false;
        // Check bound + wall
        while (!isMove) {
            xEntity.Direction nextDir = target.move();
            // Calculate
            if (nextDir.equals(xEntity.Direction.SOUTH)) {
                x1 = x-1;
                y1 = y;
            } else if (nextDir.equals(xEntity.Direction.NORTH)) {
                x1 = x+1;
                y1 = y;
            } else if (nextDir.equals(xEntity.Direction.WEST)) {
                x1 = x;
                y1 = y-1;
            } else if (nextDir.equals(xEntity.Direction.EAST)){
                x1 = x;
                y1 = y+1;
            }else{
                x1 = x;
                y1 = y;
            }
            isMove = checkMoveValid(x1,y1);

            // ZOMBIE MUST ADD PROCESS THAT ZOMBIE CAN NOT GO TO SAFEZONE
            // if(target instanceof xZombie){
            //
            // }
        }
        return new Point(x1,y1);
    }

    public void updateSoldierMove(){
        for (int i = 0; i< soldierList.size();i++){
            xSoldier target = soldierList.get(i);
            int x = target.getX(); // rows
            int y = target.getY(); // cols
            // Remove current position from map
            grid[x][y] = '-';
            Point newPos = getNewPosition(target,x,y);
            if(isProtect(newPos)){

                newPos = safeZone.get(safeZone.size()-1-safeCount);

                grid[newPos.x][newPos.y] = 'P';
                soldierList.remove(target);
                safeCount++;
            }else {
                grid[newPos.x][newPos.y] = 'S';
                soldierList.get(i).setX(newPos.x);
                soldierList.get(i).setY(newPos.y);
            }
        }
    }

    public void updateZombieMove(){
        for (int i = 0; i< zombieList.size();i++){
            xZombie target = zombieList.get(i);
            int x = target.getX(); // rows
            int y = target.getY(); // cols
            // Remove current position from map
            grid[x][y] = '-';
            Point newPos = getNewPosition(target,x,y);
            //grid[newPos.x][newPos.y] = 'Z'; // NEED TO CHANGE -  COLLSION CHECK

            // update arraylist
            zombieList.get(i).setX(newPos.x);
            zombieList.get(i).setY(newPos.y);
        }
    }

    public void updateHumanMove(){
        for (int i = 0; i< humanList.size();i++){
            xHuman target = humanList.get(i);
            int x = target.getX(); // rows
            int y = target.getY(); // cols
            // Remove current position from map
            grid[x][y] = '-';
            Point newPos = getNewPosition(target,x,y);
            //grid[newPos.x][newPos.y] = 'H'; // NEED TO CHANGE - COLLSION CHECK
            // update arraylist
            humanList.get(i).setX(newPos.x);
            humanList.get(i).setY(newPos.y);
        }
    }


    public boolean isProtect(Point pos){
        for(int i = 0; i< safeZone.size();i++){
            if(pos.x==safeZone.get(i).x && pos.y==safeZone.get(i).y){
                return true;
            }
        }
        return false;
    }

    // Using ANIMATION HERE
    public void update() {
        // get new move of all soldiers
        updateSoldierMove();
        // NEED TO IMPLEMENT HERE
        //updateZombieMove();  // NEED TO CHANGE - COLLSION CHECK
        //updateHumanMove();   // NEED TO CHANGE - COLLSION CHECK

        // AFTER MOVE
        // HUMAN CHECK GET WEAPON
        // SOLDIER SHOOTING - IF NO BULLET - SOLDIER CHANGE TO HUMAN
        // ZOOMBIE TAG HUMAN

        /*
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
        */

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
                    g.fillRect(c * cellSize+1, r * cellSize+1, cellSize+1, cellSize+1);
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
                // Soldier and Human in SafeZone
                if (grid[r][c]=='P') {
                    g.setColor(Color.PINK);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}