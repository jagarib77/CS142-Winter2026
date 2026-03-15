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
    private ArrayList<Point> safeZoneList = new ArrayList<>();
    private ArrayList<Point> weaponList = new ArrayList<>();

    // for read from file we just take
    public xLifeModel(String fileName) throws FileNotFoundException {
        loadFile(fileName);
        this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }
    private void loadFile(String fileName) throws FileNotFoundException {
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
                        safeZoneList.add(new Point(i, j));
                    } else  if(line.charAt(j) == 'W'){
                        weaponList.add(new Point(i,j));
                    }
                    grid[i][j] = line.charAt(j);
                }
                i++;
            }
        }
    }

    public boolean checkMoveValid(int x, int y){
        //System.out.println(x + " - " + y);
        if(y >= cols || y < 0){ return false; }
        if(x < 0 || x >= rows){ return false; }
        if(grid[x][y] == 'X'){ return false; } // Wall
        return true;
    }

    // Can use for all object to get move
    // Base on what return from method move of target object, update and check it valid on grid
    public Point getNewPosition(xEntity target,int x, int y){
        int x1 = 0;
        int y1 = 0;
        boolean isMove = false;
        // get new move until it valid
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
                // CENTER
                x1 = x;
                y1 = y;
            }
            isMove = checkMoveValid(x1,y1);
            // Zombie Can't go into safezone and can't step on weapon
            if(target instanceof xZombie){
                if(isMove){
                    if(isProtect(new Point(x1,y1)) || grid[x1][y1] == 'W'){
                        isMove = false;
                    }
                }
            }
        }
        return new Point(x1,y1);
    }

    public boolean isProtect(Point pos){
        for(int i = 0; i< safeZoneList.size(); i++){
            if(pos.x== safeZoneList.get(i).x && pos.y== safeZoneList.get(i).y){
                return true;
            }
        }
        return false;
    }

    public void updateSoldierMove(){
        for (int i = 0; i< soldierList.size();i++){
            xSoldier target = soldierList.get(i);
            int x = target.getX(); // rows
            int y = target.getY(); // cols
            // Remove current position from map
            grid[x][y] = '-'; // remove current pos of S
            Point newPos = getNewPosition(target,x,y);
            soldierList.get(i).setX(newPos.x);
            soldierList.get(i).setY(newPos.y);
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
            humanList.get(i).setX(newPos.x);
            humanList.get(i).setY(newPos.y);
            //grid[newPos.x][newPos.y] = 'H'; // YOON you need to change it
        }
    }

    void updateCheckSafeZoneAction(){
        for(int i = 0; i < soldierList.size();i++){
            xSoldier target = soldierList.get(i);
            int x = target.getX();
            int y = target.getY();
            if(isProtect(new Point(x,y))){
                Point safePos = safeZoneList.get(safeZoneList.size()-1-safeCount);
                grid[safePos.x][safePos.y] = 'P';
                soldierList.remove(target);
                safeCount++;
                i--;
            }
        }

        for(int i = 0; i < humanList.size();i++){
            xHuman target = humanList.get(i);
            int x = target.getX();
            int y = target.getY();
            if(isProtect(new Point(x,y))){
                Point safePos = safeZoneList.get(safeZoneList.size()-1-safeCount);
                grid[safePos.x][safePos.y] = 'P';
                humanList.remove(target);
                safeCount++;
                i--;
            }
        }
    }

    private int hasWeapon(Point pos){
        for(int i = 0; i< weaponList.size(); i++){
            if(pos.x== weaponList.get(i).x && pos.y== weaponList.get(i).y){
                return i;
            }
        }
        return -1;
    }
    void updateWeaponAction() {
        for (int i = 0; i < humanList.size(); i++) {
            xHuman target = humanList.get(i);
            int x = target.getX();
            int y = target.getY();
            int pos = hasWeapon(new Point(x, y));
            if (pos!=-1) {
                grid[x][y] = 'S';
                soldierList.add(new xSoldier(x,y));
                humanList.remove(i);
                weaponList.remove(pos);
                i--;
            }else{
                grid[x][y] = 'H';
            }
        }
    }
    // ----------------------- SOLDIER PROCESS START--------------------------------
    public void updateSoldierAction(){
        for(int i = 0; i < soldierList.size();i++ ){
            xSoldier s = soldierList.get(i);
            if(isShooting(s) && s.getBullets()==0){
                grid[s.getX()][s.getY()]='H';
                humanList.add(new xHuman(s.getX(),s.getY()));
                soldierList.remove(i);
                i--;
            }else{
                grid[s.getX()][s.getY()]='S';
            }
        }
    }
    public int searchZombie(int x, int y){
        for(int i = 0;i< zombieList.size();i++){
            if(x==zombieList.get(i).getX() && y==zombieList.get(i).getY()){
                return i;
            }
        }
        return -1;
    }
    // like check neighbor on Project num 5 - JUST CHECK 1 RANGE (-1) to (1)
    private boolean isShooting(xSoldier target) {
        int count = 0;
        int r = target.getX();
        int c = target.getY();
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == r && j == c)) {
                    int pos = searchZombie(i,j);
                    if (pos != -1){
                        // Kill Zoombie
                        zombieList.remove(pos);
                        grid[i][j] = '-';
                        target.updateBullets();
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // ----------------------- SOLDIER PROCESS END ------------------------------

    private void checkEndGame(){

    }

    // Using ANIMATION HERE
    public void update() {
        // ******************************  MOVE *******************************
        // 1. Update all move of all Human Object
        // get new move of all soldiers
        updateSoldierMove();
        //updateZombieMove();
        updateHumanMove();
        // paintImmediately(0, 0, getWidth(), getHeight()); // just  for debug mode

        // ************************** AFTER MOVE ******************************
        // 2. Check who win()
        // Q1. What happen when Zoombie, human move to the weapon cell? Zoombie win or Human win?
        // Q2. What if human and soldier move to the same cell? // NOT FINISH
        // Q3. What if a lot of Zombie around soldier?

        // order: Human and Soldier in safeZone > Human promote Soldier > Soldier shoot zombie > Zombie tag Human

        // Human and Soldier are in safeZone
        updateCheckSafeZoneAction();

        // Human promote Soldier (get weapon), also update Human on map
        updateWeaponAction();

        // Soldier shoot zombie
        updateSoldierAction();

        // Zombie tag human
        updateZombieAction();

        checkEndGame();
        // Human and Soldier is in protect --> WIN
        // No Human on map --> gameover
        //flag


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

    // Zombie logic by Leehuot Lay

    // ----------------------- ZOMBIE PROCESS START ------------------------------

    // Search Humans
    public void updateZombieAction() {
        int originalSize = zombieList.size();

        for (int i = 0; i < originalSize; i++) {
            xZombie z = zombieList.get(i);
            z.act(humanList, zombieList, grid, rows, cols, this);
        }
    }

    // ----------------------- ZOMBIE PROCESS END ------------------------------


    public void reset(String fileName) throws FileNotFoundException {
        zombieList.clear();
        humanList.clear();
        soldierList.clear();
        safeZoneList.clear();
        weaponList.clear();
        safeCount = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = '-';
            }
        }
        loadFile(fileName);
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

        /*
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        String text = "Game Over";
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);

        //g.drawString("Game Over", 150, 200);

        g.setColor(new Color(0,0,0,150));
        g.fillRect(0,0,getWidth(),getHeight());
        */


    }
}