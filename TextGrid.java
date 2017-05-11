

public class TextGrid {
    private ConsoleStty listener;
    private int height;
    private int width;
    char[][] grid;
    private char GRID = '-';

    public TextGrid(int height, int width) {
        this.listener = new ConsoleStty();
        this.height = height;
        this.width = width;
        this.grid = new char[height][width];
        for (int r=0; r<grid.length; r++) {
            for (int c=0; c<grid[0].length; c++) {
                grid[r][c] = GRID;
            }
        }
    }

    public boolean inbounds(Location loc) {
        if ((loc.getRow() < 0 || loc.getRow() >= height) || (loc.getCol() < 0 || loc.getCol() >= width)) {
            return false;
        }
        return true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ConsoleStty getListener() {
        return listener;
    }

    public char get(Location loc) {
        if (inbounds(loc)) {
            return grid[loc.getRow()][loc.getCol()];
        }
        return '!';
    }

    public void set(Location loc, char c) {
        set (loc.getRow(), loc.getCol(), c);
    }

    public void set(int row, int col, char c) {
        if ((row >= 0 && row < height) && (col >= 0 && col < width)) {
            grid[row][col] = c;
        }
    }

    public void print() {
        for (char[] row : grid) {
            String line = "";
            for (char col : row) {
                line += col;
            }
            System.out.println(line);
        }
    }
}
