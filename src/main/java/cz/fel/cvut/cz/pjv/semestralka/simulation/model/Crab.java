package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * The animal subclass for crab, herbivore animal, that can move only down or on cells adjacent to ground
 */
public class Crab extends Animal{
    private static final Logger logger = Logger.getLogger(Crab.class.getName());
    public static float crabPhLevelMax = 9F;
    public static float crabPhLevelMin = 6.5F;
    public static float crabTempCelsiusMax = 30;
    public static float crabTempCelsiusMin = 20;
    public static int crabSpeed = 800;
    public static int crabHungerCycleSec = 10;
    public static int crabReproLimit = 3;
    public static AtomicInteger crabAmount = new AtomicInteger(0);
    public Crab() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        ArrayList<GameCell> viableCells = getViableCells();
        if (viableCells.isEmpty()) {
            this.die();
        }
        crabAmount.incrementAndGet();
        int size = viableCells.size();
        GameCell gameCell = viableCells.get(rand.nextInt(0,size));
        gameCell.setAnimal(this);
        x = gameCell.getX();
        y = gameCell.getY();
        setX(x);
        setY(y);
        phLevelMax = crabPhLevelMax;
        phLevelMin = crabPhLevelMin;
        tempCelsiusMax = crabTempCelsiusMax;
        tempCelsiusMin = crabTempCelsiusMin;
        speed = crabSpeed;
        hungerCycle = crabHungerCycleSec;
        reproductionLimit = reproductionValue = crabReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Crab species spawned");
    }

    public Crab(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        crabAmount.incrementAndGet();
        this.x = x;
        this.y = y;
        setCellAnimal(this,x,y);
        phLevelMax = crabPhLevelMax;
        phLevelMin = crabPhLevelMin;
        tempCelsiusMax = crabTempCelsiusMax;
        tempCelsiusMin = crabTempCelsiusMin;
        speed = crabSpeed;
        hungerCycle = crabHungerCycleSec;
        reproductionLimit = reproductionValue = crabReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Crab species spawned from repro");
    }

    public static ArrayList<GameCell> getViableCells(){
        ArrayList<GameCell> viableCells = new ArrayList<>();
        for (GameCell[] cellRow : AquariumModel.getMapGrid()) {
            for (GameCell cell : cellRow) {
                for (GameCell adjCell : cell.getAdjCells()){
                    if (!adjCell.isWater() && cell.getAnimal() == null && cell.isWater() && !viableCells.contains(cell)){
                        viableCells.add(cell);
                    }
                }
            }
        }
        return viableCells;
    }

    @Override
    public void move(){
        canEat(x,y);
        int direction = rand.nextInt(3) + 1;
        // make the previous cell empty
        switch (direction){
            case 1:
                if (x>0 && AquariumModel.getCellByCoords(x-1,y).isNextToGround() && AquariumModel.getCellByCoords(x-1,y).isWater() && AquariumModel.getCellByCoords(x-1,y).getAnimal() == null) {
                    setCellAnimal(null,x,y);
                    setX(getX() - 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 2:
                if (x<AquariumModel.width-1 && AquariumModel.getCellByCoords(x+1,y).isNextToGround() && AquariumModel.getCellByCoords(x+1,y).isWater() && AquariumModel.getCellByCoords(x+1,y).getAnimal() == null) {
                    setCellAnimal(null,x,y);
                    setX(getX() + 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 3:
                if (y>0 && AquariumModel.getCellByCoords(x,y-1).isWater() && AquariumModel.getCellByCoords(x,y-1).getAnimal() == null) {
                    setCellAnimal(null,x,y);
                    setY(getY() - 1);
                    setCellAnimal(this, x, y);
                }
                break;
        }
    }

    @Override
    protected void findFood() {
        GameCell gameCell;
        for (int i = 1; i <= 5; i++) {
            gameCell = AquariumModel.getCellByCoords(x+i , y);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(gameCell.getAnimal().getClass())) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass()) && gameCell.getAnimal() == null )){
                move(x+1,y);
                return;
            }
        }
        for (int i = -1; i >= -5; i--) {
            gameCell = AquariumModel.getCellByCoords(x +i, y);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(gameCell.getAnimal().getClass())) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass()) && gameCell.getAnimal() == null )){
                move(x-1,y);
                return;
            }
        }
        for (int i = -1; i >= -5; i--) {
            gameCell = AquariumModel.getCellByCoords(x , y+i);
            if (gameCell == null || !gameCell.isWater()){break;}
            if ((gameCell.getAnimal() != null && edibleFood.contains(gameCell.getAnimal().getClass())) ||
                    (gameCell.getFood() != null && edibleFood.contains(gameCell.getFood().getClass()) && gameCell.getAnimal() == null )) {
                move(x, y - 1);
                return;
            }
        }
        move();
    }

    @Override
    void die() {
        if (isDead){
            return;
        }
        super.die();
        crabAmount.decrementAndGet();
        logger.info("Crab died");
    }

    public static float getCrabPhLevelMax() {
        return crabPhLevelMax;
    }

    public static void setCrabPhLevelMax(float crabPhLevelMax) {
        Crab.crabPhLevelMax = crabPhLevelMax;
    }

    public static float getCrabPhLevelMin() {
        return crabPhLevelMin;
    }

    public static void setCrabPhLevelMin(float crabPhLevelMin) {
        Crab.crabPhLevelMin = crabPhLevelMin;
    }

    public static float getCrabTempCelsiusMax() {
        return crabTempCelsiusMax;
    }

    public static void setCrabTempCelsiusMax(float crabTempCelsiusMax) {
        Crab.crabTempCelsiusMax = crabTempCelsiusMax;
    }

    public static float getCrabTempCelsiusMin() {
        return crabTempCelsiusMin;
    }

    public static void setCrabTempCelsiusMin(float crabTempCelsiusMin) {
        Crab.crabTempCelsiusMin = crabTempCelsiusMin;
    }

    public static int getCrabSpeed() {
        return crabSpeed;
    }

    public static void setCrabSpeed(int crabSpeed) {
        Crab.crabSpeed = crabSpeed;
    }

    public static int getCrabHungerCycleSec() {
        return crabHungerCycleSec;
    }

    public static void setCrabHungerCycleSec(int crabHungerCycleSec) {
        Crab.crabHungerCycleSec = crabHungerCycleSec;
    }

    public static int getCrabReproLimit() {
        return crabReproLimit;
    }

    public static void setCrabReproLimit(int crabReproLimit) {
        Crab.crabReproLimit = crabReproLimit;
    }
}
