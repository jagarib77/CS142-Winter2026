// Zombie Method by Leehuot Lay


public class xZombie{
    char[][] grid;
    private int rows, cols, humanX, humanY;

    // Zombie move first, check if have human. yes, go hunt and change human else end turns
    public xZombie(int r, int c, char[][] grid) {
        this.rows = r;
        this.cols = c;
        this.grid = grid;
        boolean humanFound = search(r, c);
    }
        // searching for human
        public boolean search ( int r, int c){
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;

                    int nr = r + dr;  //neighbor row
                    int nc = c + dc;  //neighbor col

                    //check bounds (edge of grid)
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        if (grid[nr][nc] == 'H') // H represents human
                            humanX = nr; humanY = nc;
                        return true; // return true = found human, continue to tag them
                    }
                }
            }
            return false; // return false = no human found, ends turn
        }
    }
