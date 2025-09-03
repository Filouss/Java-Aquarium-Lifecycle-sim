package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

public class SeaUrchin extends Animal{
    private static final Logger logger = Logger.getLogger(SeaUrchin.class.getName());
    public static float seaUrchinPhLevelMax = 8F;
    public static float seaUrchinPhLevelMin = 6.5F;
    public static float seaUrchinTempCelsiusMax = 23;
    public static float seaUrchinTempCelsiusMin = 10;
    public static int seaUrchinSpeed = 400;
    public static int seaUrchinHungerCycleSec = 3;
    public static int seaUrchinReproLimit = 3;
    public static AtomicInteger seaUrchinAmount = new AtomicInteger(0);

    public SeaUrchin() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        ArrayList<GameCell> viableCells = Crab.getViableCells();
        int size = viableCells.size();
        if (viableCells.isEmpty()) {
            this.die();
        }
        seaUrchinAmount.incrementAndGet();
        GameCell gameCell = viableCells.get(rand.nextInt(0,size));
        gameCell.setAnimal(this);
        x = gameCell.getX();
        y = gameCell.getY();
        setX(x);
        setY(y);
        phLevelMax = seaUrchinPhLevelMax;
        phLevelMin = seaUrchinPhLevelMin;
        tempCelsiusMax = seaUrchinTempCelsiusMax;
        tempCelsiusMin = seaUrchinTempCelsiusMin;
        speed = seaUrchinSpeed;
        hungerCycle = seaUrchinHungerCycleSec;
        reproductionLimit = reproductionValue = seaUrchinReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Sea urchin species spawned");
    }

    public SeaUrchin(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        seaUrchinAmount.incrementAndGet();
        this.x = x;
        this.y = y;
        setCellAnimal(this,x,y);
        phLevelMax = seaUrchinPhLevelMax;
        phLevelMin = seaUrchinPhLevelMin;
        tempCelsiusMax = seaUrchinTempCelsiusMax;
        tempCelsiusMin = seaUrchinTempCelsiusMin;
        speed = seaUrchinSpeed;
        hungerCycle = seaUrchinHungerCycleSec;
        reproductionLimit = reproductionValue = seaUrchinReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Sea urchin species spawned from repro");
    }
    @Override
    void die() {
        if (isDead){
            return;
        }
        super.die();
        seaUrchinAmount.decrementAndGet();
        logger.info("Sea urchin died");
    }

    @Override
    public void move(){
        canEat(x,y);
        int direction = rand.nextInt(3) + 1;
        // make the previous cell empty
        switch (direction){
            case 1:
                if (x>0 && AquariumModel.getCellByCoords(x-1,y).isNextToGround() && AquariumModel.getCellByCoords(x-1,y).isWater()&& AquariumModel.getCellByCoords(x-1,y).getAnimal() == null) {
                    setCellAnimal(null,x,y);
                    setX(getX() - 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 2:
                if (x<AquariumModel.width-1 && AquariumModel.getCellByCoords(x+1,y).isNextToGround() && AquariumModel.getCellByCoords(x+1,y).isWater()&& AquariumModel.getCellByCoords(x+1,y).getAnimal() == null) {
                    setCellAnimal(null,x,y);
                    setX(getX() + 1);
                    setCellAnimal(this, x, y);
                }
                break;
            case 3:
                if (y>0 && AquariumModel.getCellByCoords(x,y-1).isWater()&& AquariumModel.getCellByCoords(x,y-1).getAnimal() == null) {
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
    public static float getSeaUrchinPhLevelMax() {
        return seaUrchinPhLevelMax;
    }

    public static void setSeaUrchinPhLevelMax(float seaUrchinPhLevelMax) {
        SeaUrchin.seaUrchinPhLevelMax = seaUrchinPhLevelMax;
    }

    public static float getSeaUrchinPhLevelMin() {
        return seaUrchinPhLevelMin;
    }

    public static void setSeaUrchinPhLevelMin(float seaUrchinPhLevelMin) {
        SeaUrchin.seaUrchinPhLevelMin = seaUrchinPhLevelMin;
    }

    public static float getSeaUrchinTempCelsiusMax() {
        return seaUrchinTempCelsiusMax;
    }

    public static void setSeaUrchinTempCelsiusMax(float seaUrchinTempCelsiusMax) {
        SeaUrchin.seaUrchinTempCelsiusMax = seaUrchinTempCelsiusMax;
    }

    public static float getSeaUrchinTempCelsiusMin() {
        return seaUrchinTempCelsiusMin;
    }

    public static void setSeaUrchinTempCelsiusMin(float seaUrchinTempCelsiusMin) {
        SeaUrchin.seaUrchinTempCelsiusMin = seaUrchinTempCelsiusMin;
    }

    public static int getSeaUrchinSpeed() {
        return seaUrchinSpeed;
    }

    public static void setSeaUrchinSpeed(int seaUrchinSpeed) {
        SeaUrchin.seaUrchinSpeed = seaUrchinSpeed;
    }

    public static int getSeaUrchinHungerCycleSec() {
        return seaUrchinHungerCycleSec;
    }

    public static void setSeaUrchinHungerCycleSec(int seaUrchinHungerCycleSec) {
        SeaUrchin.seaUrchinHungerCycleSec = seaUrchinHungerCycleSec;
    }

    public static int getSeaUrchinReproLimit() {
        return seaUrchinReproLimit;
    }

    public static void setSeaUrchinReproLimit(int seaUrchinReproLimit) {
        SeaUrchin.seaUrchinReproLimit = seaUrchinReproLimit;
    }
}
