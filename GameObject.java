import java.util.Random;

public abstract class GameObject {
    private Location location;
    private TextGrid grid;
    private final char[] directions = {'u', 'd', 'l', 'r'};
    private char direction = 'd';

    public GameObject(Location loc, TextGrid g) {
        location = loc;
        grid = g;
    }

    public Location getLocation() {
        return location;
    }

    public boolean equals(GameObject other) {
        return equals(new Location(other.getLocation().getRow(), other.getLocation().getCol()));
    }
    public boolean equals(Location other) {
        return (location.getRow() == other.getRow())
            && (location.getCol() == other.getCol());
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public boolean inGrid(Location loc) {
        return (loc.getCol() >= 0 && loc.getCol() < grid.getWidth())
            && (loc.getRow() >= 0 && loc.getRow() < grid.getHeight());
    }

    public TextGrid getGrid() {
        return grid;
    }

    public void kill() {
        grid.set(location, '-');
    }

    // scan locations around snake to find a valid location
    public Location getValidLocation() {

        Location newLoc;
        Random rand = new Random();
        int counter = rand.nextInt(4);

        while (true) {
            if (rand.nextInt(5) == 0) { // random [low] chance to change directions
                direction = directions[counter];
            }

            if (direction == 'u') {
                newLoc = new Location(location.getRow() - 1, location.getCol());
            } else if (direction == 'd') {
                newLoc = new Location(location.getRow() + 1, location.getCol());
            } else if (direction == 'l') {
                newLoc = new Location(location.getRow(), location.getCol() - 1);
            } else {
                newLoc = new Location(location.getRow(), location.getCol() + 1);
            }

            counter++;

            if (counter == 4) {
                counter = 0;
            }

            if (inGrid(newLoc)) {
                return newLoc;
            } else {
                direction = directions[counter];
            }
        }
    }

    public abstract void move(Location newLoc);

}
