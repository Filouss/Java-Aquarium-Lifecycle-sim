package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import cz.fel.cvut.cz.pjv.semestralka.simulation.view.SimulatorView;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * This class represents the aquarium, its width, height, list of entities and other attributes
 */

public class AquariumModel {

    private static final Logger logger = Logger.getLogger(AquariumModel.class.getName());
    public static int height = 15;
    public static int width = 15;
    public static int cell_size = 55;

    public static float waterRatio = 0.5F;
    public static float phLevel = 8F;

    public static float tempCelsius = 33;
    public int coralSpawnRateMs = 1000;

    public static CopyOnWriteArrayList<Animal> animalList = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Animal> animalListeaten = new CopyOnWriteArrayList<>();
    public static ArrayList<Food> foodList = new ArrayList<>();
    public static ArrayList<Food> foodListeaten = new ArrayList<>();
    private static GameCell[][] mapGrid;
    private int waterCells = (int) ((width*(height-1))*waterRatio);

    public AquariumModel() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);

        initializeGrid();
        createAnimals();
        updateAnimals();
        spawnCorals();
        logger.info("started spawning corals");
    }

    private void updateAnimals() {
        // for each animal check if the ph level and temperature affect its attributes
        for (Animal animal : animalList){
            float phMax = animal.getPhLevelMax();
            float phMin = animal.getPhLevelMin();
            if (phLevel<phMin  || phLevel>phMax){animal.setReproductionValue(animal.getReproductionValue()+1);}
            if (phLevel<phMin-4  || phLevel>phMax+4){animal.die();}
            float tempMax = animal.getTempCelsiusMax();
            float tempMin = animal.getTempCelsiusMin();
            if (tempCelsius<tempMin || tempCelsius>tempMax){animal.setSpeed(animal.getSpeed()*2);}
            if (tempCelsius<tempMin-10 || tempCelsius>tempMax+10){animal.die();}
        }
        logger.info("animals updated");
    }

    private void createAnimals(){
        //map containing subclass of the animal and amount to spawn
        Map<Class<? extends Animal>, Integer> animals = new HashMap<>();
        animals.put(Carp.class, waterCells / 3 / 5);
        animals.put(Tuna.class, waterCells / 3 / 5);
        animals.put(Shark.class, waterCells / 3 / 5);
        animals.put(Crab.class,waterCells / 3 / 5);
        animals.put(SeaUrchin.class, waterCells / 3 / 5);

        //iterate through each animal subclass
        for (Map.Entry<Class<? extends Animal>, Integer> entry : animals.entrySet()) {
            Class<? extends Animal> animalClass = entry.getKey();
            int count = entry.getValue();
            //create the desired amount of animals
            for (int i = 0; i < count; i++) {
                try{
                Animal animal = animalClass.getDeclaredConstructor().newInstance();
                if (AquariumModel.animalList.contains(animal)){continue;}
                addToAnimalList(animal);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    logger.log(Level.SEVERE,null, e);
                }
            }

        }
        for (Animal animal :animalList){
            Thread animalThread = new Thread(animal);
            animalThread.start();
        }
        logger.info("Initial animals created");
    }

    private void initializeGrid(){
        mapGrid  = new GameCell[width][height];
        int waterCellCount = 0;
        int groundCellCount = 0;
        int groundCells = (width*(height-1))- waterCells;

        for (int i = height-1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                // check if the cell will be water or not
                boolean waterCell = Math.random() < 0.5;
                //make the bottom cells all ground
                if (i == 0) {waterCell = false;}
                else {
                    // check iff the amount of cells aren\t bigger than the ratio
                    if (waterCellCount < waterCells && groundCellCount < groundCells){
                        //if the cell is water, increase water count, else increase ground count
                        if (waterCell){waterCellCount++;}
                        else {groundCellCount++;}
                        //if by adding a water cell the watercell limit is too high, fill the rest with ground
                    } else if (waterCellCount < waterCells) {
                        waterCell = true;
                        waterCellCount++;
                    } else {
                        waterCell = false;
                        groundCellCount++;
                    }
                }
                //create the cell and add it to the grid and list of cells in view
                GameCell gameCell = new GameCell(waterCell,j,i);
                mapGrid[j][i] = gameCell;
                SimulatorView.cellGroup.getChildren().add(gameCell);
            }
        }
        //assign each cell neighbour cells
        assignNeighbourCells();
        logger.info("Grid map initiated succesfully");
    }

    private void assignNeighbourCells() {
        for (GameCell[] cellRow : mapGrid) {
            for (GameCell cell : cellRow) {
                cell.adjacentCells();
            }
        }
    }

    private void spawnCorals(){
        // periodically create new Corals on viable locations
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (!Coral.getViableCells().isEmpty()) {
                Coral coral = new Coral();
                foodList.add(coral);
            }
        }, 0, coralSpawnRateMs, TimeUnit.MILLISECONDS);
    }

    /**
     * This returns gamecell by desired  x and y coords or null if the cell was not found or in the limits of the grid
     * @param x x-axis coords of the cell
     * @param y y-axis coords of the cell
     * @return Gamecell that has coords equal to the coords in param or null if the cell wasn't found
     */
    public static GameCell getCellByCoords(int x, int y){
        for (GameCell[] cellRow : mapGrid) {
            for (GameCell cell : cellRow) {
                if (cell.getX() == x && cell.getY() == y && x >= 0 && x<AquariumModel.width && y>=0 && y<AquariumModel.height) {return cell;}
                }
            }
        return null;
    }

    /**
     * synchronized method to add to foodlist to avoid concurrent modification exception
     * @param food food to add to the list
     */
    public static synchronized void addToFoodList(Food food) {
        foodList.add(food);
    }

    /**
     * synchronized method to add to animallist to avoid concurrent modification exception
     * @param animal animal to add to the list
     */
    public static synchronized void addToAnimalList(Animal animal) {
        animalList.add(animal);
    }

    /**
     * synchronized method to remove from foodlist to avoid concurrent modification exception
     * @param food food to remove from the list
     */
    public static synchronized void removeFromFoodList(Food food) {
        foodList.remove(food);
    }

    /**
     * synchronized method to remove from animallist to avoid concurrent modification exception
     * @param animal animal to remove from the list
     */
    public static synchronized void removeFromAnimalList(Animal animal) {
        animalList.remove(animal);
    }

    public static synchronized void addToEaten(Animal animal){animalListeaten.add(animal);}


    public static GameCell[][] getMapGrid() {
        return mapGrid;
    }
}
