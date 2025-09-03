package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import javafx.scene.image.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;

/**
 * Superclass of all animals, containing all dynamic reusable methods that interact with the animal object on the side of logic.
 * Implements runnable to be able to create subclasses and also implement multithreading
 */

public class Animal implements Runnable {
    private static final Logger logger = Logger.getLogger(Animal.class.getName());

    volatile protected int x, y;
    State state;
    protected ImageView imageView = new ImageView();
    private float elapsedSeconds = 0;
    protected int speed;
    private int hungerLimit = 2;
    protected float hungerCycle;
    protected int reproductionLimit;
    protected int reproductionValue = reproductionLimit;
    List<Class<?>> edibleFood = new ArrayList<>();

    public static float phLevelMax;
    public static float phLevelMin;
    public static int deadAnimals = 0;
    public static float tempCelsiusMax;
    public static float tempCelsiusMin;
    private UUID uuid;
    private Thread animalThread;
    protected volatile boolean isDead = false;
    public final ReentrantLock lock = new ReentrantLock();

    public Thread getAnimalThread() {
        return animalThread;
    }

    Random rand = new Random();

    public enum State {
        DEAD, ALIVE
    }

    public Animal() {

        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        uuid = UUID.randomUUID();
        // get cells viable for spawning and choose one randomly
        ArrayList<GameCell> viableCells = getSpawnableCells();
        int size = viableCells.size();
        GameCell gameCell = viableCells.get(rand.nextInt(0,size));
        //set the animal to the random cell
        gameCell.setAnimal(this);
        x = gameCell.getX();
        y = gameCell.getY();
        setCellAnimal(this, x, y);
        state = State.ALIVE;
        logger.log(Level.INFO,"Animal created: UUID={0}, X={1}, Y={2}", new String[]{String.valueOf(uuid), String.valueOf(x), String.valueOf(y)});

    }

    public Animal(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        this.x = x;
        this.y = y;
        setCellAnimal(this, x, y);
        state = State.ALIVE;
        logger.log(Level.INFO,"Animal created: UUID={0}, X={1}, Y={2}", new String[]{String.valueOf(uuid), String.valueOf(x), String.valueOf(y)});
    }

    @Override
    public void run() {
        animalThread = Thread.currentThread();
        //wait for view
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE,null, e);
            }
        }
        while (this.state == State.ALIVE) {
            // check if one hnger cycle has passed and start looking for food if so
            if (hungerLimit <= 1) {findFood();}
            //move randomly
            else {move();}
            // decrease hunger each period of hunger cycle
            if (++elapsedSeconds >= (hungerCycle / ((float) speed / 1000))) {
                decreaseHunger();
                elapsedSeconds = 0;
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * This method represents pathfinding to edible food object, if there is none in any direction, the animal moves randomly
     */
    protected void findFood() {
        GameCell gameCell;
        //check right for any edible objects
        for (int i = 1; i <= 5; i++) {
            gameCell = AquariumModel.getCellByCoords(x+i , y);
            if (gameCell == null || !gameCell.isWater()){break;}
            //if on any cell in that direction is edible object, move by 1 cell in that direction
            if ((gameCell.getAnimal() != null && edibleFood.contains(Animal.class)) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass()) && gameCell.getAnimal() == null )){
                move(x+1,y);
                return;
            }
        }
        // check left
        for (int i = -1; i >= -5; i--) {
            gameCell = AquariumModel.getCellByCoords(x +i, y);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(Animal.class)) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass()))&&gameCell.getAnimal() == null){
                move(x-1,y);
                return;
            }
        }
        // check up
        for (int i = 1; i <= 5; i++) {
            gameCell = AquariumModel.getCellByCoords(x , y+i);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(Animal.class)) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass())&&gameCell.getAnimal() == null)){
                move(x,y+1);
                return;
            }
        }
        //check down
        for (int i = -1; i >= -5; i--) {
            gameCell = AquariumModel.getCellByCoords(x , y+i);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(Animal.class)) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass())&&gameCell.getAnimal() == null)) {
                move(x, y - 1);
                return;
            }
        }
        //no food found, move in random dir
        move();
    }

    /**
     * unlocks the thread when view is rendered
     */

    public void lockNotify(){
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * this method assign animal attribute of a cell
     * @param animal animal object, that resides on the cell, or null if the animal is no longer on the cell
     * @param x x-axis coords of the cell
     * @param y y-axis coords of the cell
     */
    void setCellAnimal(Animal animal, int x, int y) {
        //get the cell by coords and assign animal
        GameCell cellLocation = AquariumModel.getCellByCoords(x, y);
        cellLocation.setAnimal(animal);

    }

    /**
     * Method for moving to a distinct location during pathfinding
     *
     * @param x represents new coords on x-axis
     * @param y represents new coords on y-axis
     */
    public void move(int x, int y) {
        //check if there is an animal on the cell already
        if (!this.edibleFood.contains(Animal.class)){
            if (AquariumModel.getCellByCoords(x,y).getAnimal() != null){return;}}
        //check if the new location has any edible food
        canEat(x, y);
        //set old location animal to null and update the new location
        setCellAnimal(null, this.x, this.y);
        setX(x);
        setY(y);
        setCellAnimal(this, x, y);
    }

    /**
     * Move the entity to the adjacent cell in a random direction
     */
    public void move() {
        // if there is any food on this cell ,eat
        canEat(x, y);
        //get random direction
        int direction = rand.nextInt(4) + 1;
        switch (direction) {
            //check for each direction if the cell is in the grid limits
            case 1:
                if (checkCell(x, getY() + 1) && AquariumModel.getCellByCoords(x,y+1).getAnimal() == null) {
                    setCellAnimal(null, x, y);
                    setY(getY() + 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 2:
                if (checkCell(getX() + 1, y) && AquariumModel.getCellByCoords(x+1,y).getAnimal() == null) {
                    setCellAnimal(null, x, y);
                    setX(getX() + 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 3:
                if (checkCell(x, getY() - 1) && AquariumModel.getCellByCoords(x,y-1).getAnimal() == null) {
                    setCellAnimal(null, x, y);
                    setY(getY() - 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 4:
                if (checkCell(getX() - 1, y)&& AquariumModel.getCellByCoords(x-1,y).getAnimal() == null) {
                    setCellAnimal(null, x, y);
                    setX(getX() - 1);
                    setCellAnimal(this, x, y);
                }
                break;
        }
    }

    /**
     * Check if a cell has any edible food for this animal
     *
     * @param x x coords for the cell
     * @param y y coords for the cell
     */
    synchronized void canEat(int x, int y) {
        //get the cell and check if its in limits of the grid
        GameCell gameCell = AquariumModel.getCellByCoords(x, y);
        if (gameCell == null || gameCell.getAnimal() == this) {
            return;
        }
        //if there is an edible food or animal, call eat
        if (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass())) {
            eat(gameCell.getFood());
            gameCell.setFood(null);
        }
        if (gameCell.getAnimal() != null && edibleFood.contains(Animal.class)) {
            eat(gameCell.getAnimal());
            gameCell.setAnimal(this);
        }
    }

    /**
     * if the reproduction limit is met, create a new animal of this class
     */
    public void reproduce() {
        //restart the reproduction value
        reproductionValue = reproductionLimit;
        //50% chance for reproduction
        if (rand.nextBoolean()) {
            try {
                // Get the class of the current animal
                Class<?> animalClass = this.getClass();

                // Create new animal of the same subclass on adjacent location to this animal if possible
                for (GameCell cell : AquariumModel.getCellByCoords(x, y).getAdjCells()) {
                    if (cell != null && cell.isWater() && cell.getAnimal() == null) {
                        Animal animal = (Animal) animalClass.getDeclaredConstructor(int.class, int.class).newInstance(cell.getX(), cell.getY());
                        // if the new animal has valid coords, add to animal list and start the thread
                        AquariumModel.addToAnimalList(animal);
                        Thread animalThread = new Thread(animal);
                        animalThread.start();
                        animal.lockNotify();
                        return;
                    }
                }
                logger.info("Animal reproduced");
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                logger.log(Level.SEVERE,null, e);
            }
        }
    }

    /**
     * Method that allows animal to eat a Food object
     * @param food food that is going to be eaten
     */
    private void eat(Food food) {
        Food.foodAmount--;
        // set the food value of the cell to zero, and hide the food in view
        AquariumModel.getCellByCoords(food.getX(),food.getY()).setFood(null);
        food.visibility = Food.Visibility.HIDDEN;
        //wait for view to render the change
        AquariumModel.removeFromFoodList(food);
        AquariumModel.foodListeaten.add(food);
        hungerLimit++;
        // if reproduction value is 0, call reproduce
        if (reproductionValue < 1) {
                reproduce();
        }
        reproductionValue--;
        logger.info("Animal ate food");
    }

    /**
     * Method that allows carnivore animal to eat a Food object
     * @param animal the animal that is going to be eaten
     */
    private void eat(Animal animal) {
        if (AquariumModel.animalListeaten.contains(animal)){
            animal.state = State.DEAD;
            return;
        }
        if (animal == this) {
            return;
        }
        //make the eaten animal die
        animal.die();
        hungerLimit++;
        if (reproductionValue < 1) {
               reproduce();
        }
        reproductionValue--;
        logger.info("Animal ate animal");

    }

    /**
     * this method makes animal die, terminating its thread and creating remainings on its location
     */
     void die() {
        // Exit if already dead
        if (isDead) {return;}
         // Mark as dead
        isDead = true;
        setCellAnimal(null, x, y);
        deadAnimals++;
        this.state = State.DEAD;
        // Interrupt the correct thread
        if (animalThread != null) {
            animalThread.interrupt();
        }

        AquariumModel.removeFromAnimalList(this);
        AquariumModel.addToEaten(this);

        AquariumModel.addToFoodList(new Remainings(x, y));
        logger.info("Animal died");
    }

    /**
     * this method periodically decreases hungerLimit of an animal and checks if its not zero, if so the animal dies
     */

    private void decreaseHunger() {
        if (hungerLimit >= 1) {
            hungerLimit--;
        } else {
            die();
        }
    }

    /**
     * method for checking if a cell is water and within the grid limits
     * @param x x coords for the cell
     * @param y y coords for the cell
     * @return true if is in the grid and is water, false otherwise
     */
    public boolean checkCell(int x, int y) {
        GameCell gameCell = AquariumModel.getCellByCoords(x, y);
        return x >= 0 && x < AquariumModel.width && y >= 0 && y < AquariumModel.height && gameCell.isWater();
    }

    /**
     * This method creates a list of spawnable cells for an animal
     * @return
     */
    public static ArrayList<GameCell> getSpawnableCells(){
        ArrayList<GameCell> viableCells = new ArrayList<>();
        // go through each cell
        for (GameCell[] cellRow : AquariumModel.getMapGrid()) {
            for (GameCell cell : cellRow) {
                // go through each
                for (GameCell adjCell : cell.getAdjCells()){
                    if (cell.getAnimal() == null && cell.isWater() && !viableCells.contains(cell)){
                        viableCells.add(cell);
                    }
                }
            }
        }
        return viableCells;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setReproductionValue(int reproductionValue) {
        this.reproductionValue = reproductionValue;
    }

    public float getPhLevelMax() {
        return phLevelMax;
    }

    public float getPhLevelMin() {
        return phLevelMin;
    }

    public float getTempCelsiusMax() {
        return tempCelsiusMax;
    }

    public float getTempCelsiusMin() {
        return tempCelsiusMin;
    }

    public int getReproductionValue() {
        return reproductionValue;
    }

    public int getSpeed() {
        return speed;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public State getState() {
        return state;
    }

}