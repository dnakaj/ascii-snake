import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Runtime;
import java.lang.Thread;

public class Game {

    private TextGrid grid;
    private Snake snake;
    private Location newLocation; // this makes it so the snake continues in the same direction until a new key is pressed

    private int msElapsed;
    private int score;
    private int speed = 100;

    private char direction;
    private boolean continueGame = true;
    private final int DEFAULT_SIZE = 5;
    private int oldSize = DEFAULT_SIZE;
    private int foodOnScreen;
    private final int MAX_FOOD = 1;
    private int invincibility;
    public static final char SNAKE = 'o';
    public static final char GRID = ' ';
    public static final char FOOD = 'F';
    private static final char OUT_BOUNDS = '!';
    private final int UP = 119; // abs = 4
    private final int DOWN = 115;
    private final int LEFT = 97; // abs = 3
    private final int RIGHT = 100;


    public Game() {
        grid = new TextGrid(20, 60);

        msElapsed = 0;
        grid.print();

        //mongooses.add(new Mongoose(new Location(5, 5), grid)); // add 1 mongoose to start

        snake = new Snake(new Location(10, 10), grid);
        grid.setScore(snake.getSize());

        for (int i=0; i<DEFAULT_SIZE-1; i++) {
            snake.grow();
        }

        start();
    }

    public static void main(String[] args) {
        Game gg = new Game();
    }

    /**
     *  Runs the game
     * [terminal code from: https://www.darkcoding.net/software/non-blocking-console-io-is-not-possible/]
     **/
    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                restoreTerminal();
            }
        });

        try {
            grid.getListener().setTerminalToCBreak();

            // TODO : Just consolidate key and direction into only key
            int key = 100;
            int last = key;
            //char direction = 'r';
            newLocation = new Location(snake.getLocation().getRow() + 1, snake.getLocation().getCol());
            snake.move(newLocation);

            while (true) {

                try {
                    Thread.sleep(150);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (System.in.available() > 0) {
                    key = System.in.read();
                    System.in.skip(System.in.available());
                }

                populateScreen();

                if (key != 27) {
                    if (!nextMoveIsValid(key, last)) {
                        key = last;
                    }

                    if (key == UP) {
                        newLocation = new Location(newLocation.getRow() - 1, newLocation.getCol());
                    } else if (key == DOWN) {
                        newLocation = new Location(newLocation.getRow() + 1, newLocation.getCol());
                    } else if (key == LEFT) {
                        newLocation = new Location(newLocation.getRow(), newLocation.getCol() - 1);
                    } else if (key == RIGHT) {
                        newLocation = new Location(newLocation.getRow(), newLocation.getCol() + 1);
                    }
                }

                //if (isMoveKey(key)) {
                    last = key;
                //}

                char nextItem = grid.get(newLocation);

                if (nextItem == FOOD) {
                    snake.grow();
                    grid.setScore(snake.getSize());
                    foodOnScreen --;
                } else if (nextItem == SNAKE || nextItem == OUT_BOUNDS) { //bug: game ends if you press any other key
                    //System.out.println("next="+SNAKE+" : "+OUT_BOUNDS + " | "+nextItem+" :: "+key);
                    System.out.println("Game Over - You Lose!");
                    break;
                }

                snake.move(newLocation);

                for (int i=0; i<10; i++) {
                    System.out.println();
                }
                grid.print();
            } // end while
        }
        catch (IOException e) {
            System.err.println("IOException");
        }
        catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
    }

    /**
     *  Checks if the snakes next location is valid
     **/
    private boolean nextMoveIsValid(int current, int next) {
        if (current == LEFT && next == RIGHT ||
            current == RIGHT && next == LEFT ||
            current == UP && next == DOWN ||
            current == DOWN && next == UP
            ) {
                return false;
            }
        return true;
    }

    /**
     * Checks that a key is a movement key
     **/
    private boolean isMoveKey(int key) {
        return key == UP || key == DOWN || key == RIGHT || key == LEFT;
    }

    /**
     *  Places a piece of food ("F") at a random location on the screen
     **/
    private void populateScreen() {
        Random rand = new Random();
        int row = rand.nextInt(grid.getHeight());
        int col = rand.nextInt(grid.getWidth());
        Location loc = new Location(row, col);
        int randInt = rand.nextInt(20);

        if (foodOnScreen < MAX_FOOD && grid.get(loc) == GRID) {
            grid.set(loc, FOOD);
            foodOnScreen ++;
        }
    }

    /**
     *  Takes terminal out of single-char mode
     **/
    private void restoreTerminal() {
        System.out.println("Restoring terminal.");
        try {
            grid.getListener().stty(grid.getListener().getConfig().trim() );
        }
        catch (Exception e) {
            System.err.println("Exception restoring tty config");
        }
    }

/*
    public void play()
    {

        direction = 'r'; // starts the snake off moving upwards

        newLocation = new Location(snake.getLocation().getRow() - 1, snake.getLocation().getCol());
        snake.move(newLocation);

        while (continueGame)
        {
            int oldSize = snake.getSize();

            grid.pause(speed);

            populateScreen();
            handleKeyPress();

            if (direction == 'u') {
                newLocation = new Location(newLocation.getRow() - 1, newLocation.getCol());
            } else if (direction == 'd') {
                newLocation = new Location(newLocation.getRow() + 1, newLocation.getCol());
            } else if (direction == 'l') {
                newLocation = new Location(newLocation.getRow(), newLocation.getCol() - 1);
            } else {
                newLocation = new Location(newLocation.getRow(), newLocation.getCol() + 1);
            }

            if (!snake.inGrid(newLocation)) {
                continueGame = false;
            } else {
                handleCollision(newLocation);
                snake.move(newLocation);

                for (Mongoose m : mongooses) {
                    m.move();
                }

                if (oldSize != snake.getSize() && speed > 20) {
                    speed -= 5;
                }

                if (invincibility > 0) {
                    invincibility -= speed;
                    if (invincibility < 0) {
                        invincibility = 0;
                    }
                }

                updateTitle();
            }
            msElapsed += speed;
        }
    }*/

/*
    // this is a bad method and should be redone
    public void populateScreen()
    {
        Random rand = new Random();
        int row = rand.nextInt(grid.getNumRows());
        int col = rand.nextInt(grid.getNumCols());
        Location loc = new Location(row, col);
        int randInt = rand.nextInt(20);

        if (foodOnScreen < MAX_FOOD && grid.getImage(loc) == null) {
            if (randInt < 10) {
                grid.setImage(loc, "apple.png");
                foodOnScreen ++;

            } else if (randInt == 15) {
                grid.setImage(loc, "banana.png");
                foodOnScreen ++;
            }

        }
    }
    */
    /*
    // need to handle when mongoose moves into snake
    public void handleCollision(Location loc)
    {
        String img = grid.getImage(loc);
        if (img != null) {
            if (img == "snake.png") {
                continueGame = false;
            } else if (img == "mongoose.png") {
                if (invincibility > 0) {
                    grid.setImage(loc, null);
                    for (int i=mongooses.size()-1; i>=0; i--) {
                        if (mongooses.get(i).equals(loc)) {
                            mongooses.remove(i).kill();
                            return;
                        }
                    }
                } else {
                    continueGame = false;
                }
            } else if (img == "banana.png") {
                foodOnScreen--;
                snake.eat(3);

                if (oldSize != snake.getSize()) {
                    updateScore();
                    oldSize = snake.getSize();
                }

                invincibility = 20000;

            } else if (img == "apple.png") {
                foodOnScreen--;
                snake.eat(8);

                if (oldSize != snake.getSize()) {

                    updateScore();
                    oldSize = snake.getSize();

                    if (snake.getSize() % 2 == 0 && speed > 20) {
                        speed -= 5;

                        Random rand = new Random();
                        Location newLoc = new Location(rand.nextInt(grid.getNumRows()), rand.nextInt(grid.getNumCols()));
                        while (grid.getImage(newLoc) != null) {
                            newLoc = new Location(rand.nextInt(grid.getNumRows()), rand.nextInt(grid.getNumCols()));
                        }
                        mongooses.add(new Mongoose(newLoc, grid));
                    }
                }
            } else { // handles when mongoose moves into snake
                return;
            }
        }

        for (Mongoose m : mongooses) {
            if (m.equals(snake)) {
                continueGame = false;
                break;
            } else if (snake.containsLocation(m.getLocation())) {
                int preTrimSize = snake.getSize();

                snake.trim(m.getLocation());

                speed += 2 * snake.getSize();
                if (speed > 100) {
                    speed = 100;
                }

                oldSize = snake.getSize();
                score -= 10 * (preTrimSize - snake.getSize());
                break;
            }
        }
    }*/
/*
    public static void test()
    {
        while (true) {
            Game game = new Game();
            game.play();

            TextGrid gameOver = new Grid(1, 1);

            //game.setGrid(gameOver);
            //gameOver.setImage(new Location(0, 0), "snake_gameover.png");
            //gameOver.setTitle("Final Score: "+game.getScore());

            while (true) {
                //gameOver.pause(100);
                //int key = gameOver.checkLastKeyPressed();

                if (key == 88) {
                    System.exit(0);
                }
                if (key == 10) {
                    break;
                }
            }

            gameOver.setImage(new Location(0, 0), null);
            gameOver.close();
        }
    }

    public static void main(String[] args)
    {
        test();
    }*/
}
