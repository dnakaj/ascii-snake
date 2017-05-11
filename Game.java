import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Game
{

    private TextGrid grid;
    private Snake snake;
    private ArrayList<Mongoose> mongooses = new ArrayList<>(1);
    private Location newLocation; // this makes it so the snake continues in the same direction until a new key is pressed

    private int msElapsed;
    private int score;
    private int speed = 100;

    private char direction;
    private boolean continueGame = true;
    private final int DEFAULT_SIZE = 5;
    private int oldSize = DEFAULT_SIZE;
    private int foodOnScreen;
    private final int MAX_FOOD = 10;
    private int invincibility;
    private char SNAKE = 'o';
    private char GRID = '-';
    private char FOOD = 'F';



    public Game()
    {
        grid = new TextGrid(20, 60);
        // Any way to have a fullscreen title
        //grid.setTitle("");
        //grid.pause(1000);
        //grid.setImage(new Location(0, 0), null);
        //grid.close();
        //grid = new Grid(60, 60);

        msElapsed = 0;
        grid.print();

        //mongooses.add(new Mongoose(new Location(5, 5), grid)); // add 1 mongoose to start

        snake = new Snake(new Location(10, 10), grid);
        //for (int i=0; i<DEFAULT_SIZE-1; i++) {
        //    snake.grow();
        //}
        play();
    }

    public static void main(String[] args) {
        Game gg = new Game();
    }

    public void setGrid(TextGrid g) {
        //grid.close();
        grid = g;
    }

    public void play() {

        try {
            grid.getListener().setTerminalToCBreak();

            // TODO : Just consolidate key and direction into only key
            int key = 100;
            char direction = 'r';
            newLocation = new Location(snake.getLocation().getRow() + 1, snake.getLocation().getCol());
            snake.move(newLocation);

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (System.in.available() > 0) {
                    key = System.in.read();
                }

                populateScreen();

                //int key = System.in.read();
                //System.in.reset();

                if (key != 27) {
                    //System.out.println("key="+key);

                    //System.out.println("DIRECTION="+direction);

                    if (key == 119) { // up
                        direction = 'u';
                    } else if (key == 115) { // down
                        direction = 'd';
                    } else if (key == 97) { // left
                        direction = 'l';
                    } else if (key == 100) { // right (100)
                        direction = 'r';
                    }
                }

                if (direction == 'u') {
                    newLocation = new Location(newLocation.getRow() - 1, newLocation.getCol());
                } else if (direction == 'd') {
                    newLocation = new Location(newLocation.getRow() + 1, newLocation.getCol());
                } else if (direction == 'l') {
                    newLocation = new Location(newLocation.getRow(), newLocation.getCol() - 1);
                } else if (direction == 'r') {
                    newLocation = new Location(newLocation.getRow(), newLocation.getCol() + 1);
                }
                char nextItem = grid.get(newLocation);
                if (nextItem == FOOD) {
                    snake.grow();
                }

                snake.move(newLocation);
                if (!grid.inbounds(snake.getSnake().get(0))) {
                    System.out.println("YOU LOSE!");
                    break;
                }

                for (int i=0; i<10; i++) {
                    System.out.println();
                }
                grid.print();

                if ( System.in.available() != 0 ) {
                    int c = System.in.read();
                    if ( c == 0x1B ) {
                        System.out.println(c);
                        System.out.println("no longer available");
                        break;
                    }
                }
            } // end while
        }
        catch (IOException e) {
            System.err.println("IOException");
        }
        catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
        finally {
            try {
                grid.getListener().stty(grid.getListener().getConfig().trim() );
            }
            catch (Exception e) {
                System.err.println("Exception restoring tty config");
            }
        }
    }

    public void populateScreen()
    {
        Random rand = new Random();
        int row = rand.nextInt(grid.getHeight());
        int col = rand.nextInt(grid.getWidth());
        Location loc = new Location(row, col);
        int randInt = rand.nextInt(20);

        if (foodOnScreen < MAX_FOOD && grid.get(loc) == GRID) {
            if (randInt < 10) {
                grid.set(loc, FOOD);
                foodOnScreen ++;

            }
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
    public void handleKeyPress()
    {
        // Reminder: make sure to have an error checker so player cant go off the screen
        // Reminder: possible to check if game ended by simply try-catching the RuntimeException that occurs when the player tries to move out of bounds?
        int key = grid.checkLastKeyPressed();

        if (key == 38 && !(direction == 'd' && snake.getSize() > 1)) { // UP
            direction = 'u';
        } else if (key == 40 && !(direction == 'u' && snake.getSize() > 1)) { // DOWN
            direction = 'd';
        } else if (key == 37 && !(direction == 'r' && snake.getSize() > 1)) { // LEFT
            direction = 'l';
        } else if (key == 39 && !(direction == 'l' && snake.getSize() > 1)) { // RIGHT
            direction = 'r';
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
    public void updateScore()
    {
        if (snake.getSize() != oldSize) {
            score += snake.getSize();
        }
        if (score < 0) {
            score = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public void updateTitle()
    {
        updateScore();
        grid.setTitle("Size: " + snake.getSize() + " | Food needed to grow: " + snake.getStomachSize() + "/10 | Score: "+score+ " | Invincibility: "+invincibility);
    } */
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
