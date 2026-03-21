import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents the game model.
 * It stores the grid, all entities, and game data like score and moves.
 * It also loads the map and controls the game state.
 */
public class xLifeModel extends JPanel {
    /** 2D grid that represents the map */
    private char[][] grid;
    /** Number of rows in the grid */
    private int rows;
    /** Number of columns in the grid */
    private int cols;
    /** Size of each cell in pixels */
    private int cellSize = 20;
    /** Number of safe zones */
    private int safeCount = 0;
    /** Points earned by humans */
    private int humanPoint = 0;
    /** Points earned by zombies */
    private int zombiePoint = 0;
    /** Maximum number of moves allowed for check end game*/
    private int lastMove;
    /** Number of moves */
    private int moves = 0;
    /** List of soldiers in the game */
    private ArrayList<xSoldier> soldierList = new ArrayList<>();
    /** List of zombies in the game */
    private ArrayList<xZombie> zombieList = new ArrayList<>();
    /** List of humans in the game */
    private ArrayList<xHuman> humanList = new ArrayList<>();
    /** List of safe zone positions */
    private ArrayList<Point> safeZoneList = new ArrayList<>();
    /** List of weapon positions */
    private ArrayList<Point> weaponList = new ArrayList<>();

    /**
     * Create the game model and load the map from a file.
     *
     * @param fileName the name of the map file
     * @param lastMove the maximum number of moves allowed in the game
     * @throws FileNotFoundException if the file cannot be found
     */
    public xLifeModel(String fileName, int lastMove) throws FileNotFoundException {
        humanPoint = 0;
        zombiePoint = 0;
        this.lastMove = lastMove;
        loadFile(fileName);
        this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }

    /**
     * Get the number of columns in the grid.
     *
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in the grid.
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the current human score.
     *
     * @return human points
     */
    public int getHumanPoint() {
        return humanPoint;
    }
    /**
     * Get the current zombie score.
     *
     * @return zombie points
     */
    public int getZombiePoint() {
        return zombiePoint;
    }
    /**
     * Increase the human score by 1.
     */
    public void increaseHumanPoint(){
        humanPoint++;
    }
    /**
     * Increase the zombie score by 1.
     */
    public void increaseZombiePoint(){
        zombiePoint++;
    }

    /**
     * Get how many moves are left in the game.
     *
     * @return remaining moves
     */
    public int getRemainMoves() {
        return lastMove - moves;
    }

    /**
     * Load the game map from a file.
     * This method reads the number of rows and columns,
     * then fills the grid and creates game objects
     * (soldiers, humans, zombies, safe zones, weapons).
     *
     * @param fileName the name of the file that contains the map
     * @throws FileNotFoundException if the file cannot be found
     */
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

    /**
     * Check if a move to a specific position is valid.
     * A move is valid if:
     * - It is inside the grid
     * - It is not a wall ('X')
     *
     * @param x the row position
     * @param y the column position
     * @return true if the move is valid, false otherwise
     */
    public boolean checkMoveValid(int x, int y){
        //System.out.println(x + " - " + y);
        if(y >= cols || y < 0){ return false; }
        if(x < 0 || x >= rows){ return false; }
        if(grid[x][y] == 'X'){ return false; } // Wall
        return true;
    }

    /**
     * Get a new valid position for an entity based on its movement.
     * The method keeps trying until it finds a valid move on the grid.
     *
     * Rules:
     * - Uses the entity's move() method to decide direction
     * - Checks if the new position is valid
     * - Zombies cannot move into safe zones or weapons
     *
     * @param target the entity that is moving
     * @param x current x position
     * @param y current y position
     * @return the new valid position as a Point
     */
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

    /**
     * Check if a position is inside a safe zone.
     *
     * @param pos the position to check
     * @return true if the position is in a safe zone, false otherwise
     */
    public boolean isProtect(Point pos){
        for(int i = 0; i< safeZoneList.size(); i++){
            if(pos.x== safeZoneList.get(i).x && pos.y== safeZoneList.get(i).y){
                return true;
            }
        }
        return false;
    }

    /**
     * Update movement for all soldiers.
     * For each soldier:
     * - Remove its current position from the grid
     * - Get a new valid position
     * - Update its position
     */
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

//    public void updateZombieMove(){
//        for (int i = 0; i< zombieList.size();i++){
//            xZombie target = zombieList.get(i);
//            int x = target.getX(); // rows
//            int y = target.getY(); // cols
//            // Remove current position from map
//            grid[x][y] = '-';
//            Point newPos = getNewPosition(target,x,y);
//            zombieList.get(i).setX(newPos.x);
//            zombieList.get(i).setY(newPos.y);
//        }
//    }
    /**
     * Update movement for all humans.
     * For each human:
     * - Remove its current position from the grid
     * - Calculate a new valid position
     * - Update the human's position
     */
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
        }
    }
    /**
     * Check and process when soldiers or humans enter a safe zone.
     *
     * If a soldier or human reaches a safe zone:
     * - Mark the safe zone as occupied ('P')
     * - Remove the entity from its list
     * - Increase the safe zone counter
     * - Update human points (for soldiers only)
     *
     * Note:
     * - The method loops through both soldierList and humanList
     * - Uses safeCount to track used safe zones
     */
    void updateCheckSafeZoneAction(){
        // Check soldiers
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
                increaseHumanPoint();
            }
        }
        // Check humans
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

    /**
     * Check if a position has a weapon.
     *
     * @param pos the position to check
     * @return the index of the weapon in the list if found, otherwise -1
     */
    private int hasWeapon(Point pos){
        for(int i = 0; i< weaponList.size(); i++){
            if(pos.x== weaponList.get(i).x && pos.y== weaponList.get(i).y){
                return i;
            }
        }
        return -1;
    }

    /**
     * Update weapon interaction for humans.
     *
     * If a human steps on a weapon:
     * - The human becomes a soldier
     * - The weapon is removed from the map
     * - The human is removed from the human list
     *
     * If no weapon is found:
     * - The position is marked as a human ('H')
     */
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
    /**
     * Update actions for all soldiers.
     *
     * If a soldier:
     * - Can shoot AND has no bullets left:
     *   - The soldier becomes a human again
     *   - It is removed from the soldier list
     *
     * Otherwise:
     * - The soldier stays as a soldier ('S') on the grid
     */
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

    /**
     * Search for a zombie at a specific position.
     *
     * @param x the row position
     * @param y the column position
     * @return the index of the zombie in the list if found, otherwise -1
     */
    public int searchZombie(int x, int y){
        for(int i = 0;i< zombieList.size();i++){
            if(x==zombieList.get(i).getX() && y==zombieList.get(i).getY()){
                return i;
            }
        }
        return -1;
    }
    /**
     * Check if a soldier can shoot a zombie nearby.
     *
     * The soldier checks a 5x5 area around its position.
     * If a zombie is found:
     * - The zombie is removed
     * - The grid is updated
     * - The soldier uses one bullet
     * - Human score increases
     *
     * @param target the soldier to check
     * @return true if a zombie was found and shot, false otherwise
     */
    // like check neighbor on Project num 5 - JUST CHECK 1 RANGE (-1) to (1)
    private boolean isShooting(xSoldier target) {
        int count = 0;
        int r = target.getX();
        int c = target.getY();
        // Check surrounding 5x5 area
        for (int i = r - 2; i <= r + 2; i++) {
            for (int j = c - 2; j <= c + 2; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == r && j == c)) {
                    int pos = searchZombie(i,j);
                    if (pos != -1){
                        // Kill Zoombie
                        zombieList.remove(pos);
                        grid[i][j] = '-';
                        // Use bullet and increase score
                        target.updateBullets();
                        increaseHumanPoint();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ----------------------- SOLDIER PROCESS END ------------------------------
    /**
     * Main update method for one step of the simulation.
     *
     * This method controls the full game logic in the correct order:
     *
     * 1. Move all entities (soldiers, humans, zombies)
     * 2. Check interactions after movement:
     *    - Safe zone (human/soldier reaches safe zone)
     *    - Weapon (human becomes soldier)
     *    - Soldier shoots zombie
     *    - Zombie tags human
     *
     * The method also updates the number of moves in the game.
     */
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

        this.moves++;

    }

    /**
     * Convert the current map (grid) into a string format.
     *
     * This method is used to:
     * - Save the current map
     * - Display or export the final map
     *
     * Each row is written on a new line.
     *
     * @return a string representation of the grid
     */
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
    /**
     * Update actions for all zombies.
     *
     * Each zombie performs its action by calling its act() method.
     * The zombie can:
     * - Move
     * - Interact with humans
     * - Affect the grid
     *
     * @note The original size is stored to avoid issues if the list changes
     *       while zombies act.
     */
    // Search Humans
    public void updateZombieAction() {
        int originalSize = zombieList.size();
        for (int i = 0; i < originalSize; i++) {
            xZombie z = zombieList.get(i);
            z.act(humanList, zombieList, grid, rows, cols, this);
        }
    }

    // ----------------------- ZOMBIE PROCESS END ------------------------------
    /**
     * Reset the game to its initial state and reload data from a file.
     *
     * This method:
     * - Clears all entity lists (zombies, humans, soldiers, safe zones, weapons)
     * - Resets counters and scores
     * - Clears the grid
     * - Reloads the map from the given file
     * - Updates the panel size
     *
     * @param fileName the file used to reload the initial map
     * @throws FileNotFoundException if the file cannot be found
     */
    public void reset(String fileName) throws FileNotFoundException {
        zombieList.clear();
        humanList.clear();
        soldierList.clear();
        safeZoneList.clear();
        weaponList.clear();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = '-';
            }
        }
        // Reset counter
        safeCount = 0;
        humanPoint = 0;
        zombiePoint = 0;
        moves = 0;
        loadFile(fileName);
        this.setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }

    /**
     * Draw the game grid and all entities on the screen.
     *
     * This method:
     * - Draws each cell based on its type (wall, weapon, safe zone, etc.)
     * - Uses different colors for different entities
     * - Draws grid lines
     * - Displays "Game Over" or "Victory" when the game ends
     *
     * @param g the Graphics object used for drawing
     */
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
        if(lastMove-moves<=0) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            String text = "Game Over!!!";
            if (humanPoint >= zombiePoint){
                //g.setColor(Color.WHITE);
                text = "Victory!!!";
            }
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, x, y);
            //g.drawString("Game Over", 150, 200);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}