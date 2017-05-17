import java.util.ArrayList;

public class Snake extends GameObject {

    private ArrayList<Location> snake = new ArrayList<Location>(1);
    private int stomachSize = 0; // every time stomach == 10, snake grows
    private final char SNAKE = Game.SNAKE;
    private final char GRID = Game.GRID;

    public Snake(Location loc, TextGrid g) {
        super(loc, g);
        snake.add(loc);
        getGrid().set(loc, SNAKE);
    }

    // Will move the "head" of the snake first
    public void move(Location loc) {

        Location newHead = new Location(loc.getRow(), loc.getCol());

        ArrayList<Location> tempSnake = new ArrayList<>(snake);

        getGrid().set(snake.get(0), GRID);
        snake.set(0, newHead); // moves the snake's head to the new location
        //snake.setLocation(newHead);
        getGrid().set(snake.get(0), SNAKE);

        // System.out.println("BEFORE"+tempSnake);

        // Only need this code if the snake is larger than 1 (the head has already moved)
        if (snake.size() > 1) {

            for (int i = tempSnake.size()-1; i > 0; i--) {

                getGrid().set(snake.get(i), GRID); // remove image at old snake location
                snake.set(i, tempSnake.get(i-1)); // Shifts the snake over one segment following the path of the segments ahead of it

            }

            // PROBLEM Figure out why it doesnt work when getGrid().setImage(snake.get(i), IMAGE) is placed below snake.set(i, tempSnake.get(i-1));
            // REASON (for above): Each consecutive segment is overwriting [graphically by setting to null] the previous (closer to the end) segment.
            //                     Problem was fixed by using the below for loop (iterating from i=0 to tempSnake.size() instead of the other way around didn't work properly)
            for (Location l : snake) {
                getGrid().set(l, SNAKE);
            }
        }
    }

    // Returns the snake's size
    public int getSize() {
        return snake.size();
    }


    public ArrayList<Location> getSnake() {
        return snake;
    }

    public int getStomachSize() {
        return stomachSize;
    }

    public boolean containsLocation(Location loc) {
        for (Location l : snake) {
            if (l.getRow() == loc.getRow() && l.getCol() == loc.getCol()) {
                return true;
            }
        }
        return false;
    }

    public void trim(Location fromLoc) {

        int counter = 0;

        for (Location l : snake) {
            if (l.getRow() == fromLoc.getRow() && l.getCol() == fromLoc.getCol()) {
                for (int i = snake.size()-1; i >= counter; i--) {
                    getGrid().set(snake.remove(i), SNAKE);
                }
                return;
            }
            counter ++;
        }
    }

    public void eat(int amount) {
        stomachSize += amount;
        if (stomachSize % 10 >= 0 && stomachSize % 10 != stomachSize) {
            stomachSize = stomachSize % 10;
            grow(); //grow the snake
        }
    }

    // TODO make grow somehow fit into the GameObject class if possible
    public void grow() {
        Location newEnd = getValidLocation();

        snake.add(newEnd);
        getGrid().set(newEnd, SNAKE);
    }
}
