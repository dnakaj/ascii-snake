

public class TextGrid {
    private ConsoleStty listener;
    private int score;
    private int height;
    private int width;
    char[][] grid;
    private char GRID = '-';
    private final String HEADER_TEXT = "Snake : (c) Dan Nakajima 2017";
    private String SIZE_TEXT = "Size: ";

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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    private String getHeader() {
        StringBuilder header = new StringBuilder();
        String fullHeader = HEADER_TEXT + " : " + SIZE_TEXT + score;
        for (int i=0; i<=width - fullHeader.length(); i++) {
            if (i == (width - fullHeader.length())/2) {
                header.append(fullHeader);
            } else {
                header.append("#");
            }
        }

        return header.toString();
    }

    public void print() {
        System.out.println(getHeader());
        for (char[] row : grid) {
            String line = "";
            for (char col : row) {
                line += col;
            }
            System.out.println(line);
        }
    }
}
