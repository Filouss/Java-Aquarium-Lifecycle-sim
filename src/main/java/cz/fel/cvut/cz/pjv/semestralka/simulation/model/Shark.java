package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * The animal subclass for Shark, a carnivore animal with the ability to eat other animals
 */
public class Shark extends Animal{
    private static final Logger logger = Logger.getLogger(Shark.class.getName());
    public static float sharkPhLevelMax = 9F;
    public static float sharkPhLevelMin = 6.5F;
    public static float sharkTempCelsiusMax = 30;
    public static float sharkTempCelsiusMin = 10;
    public static int sharkSpeed = 1200;
    public static int sharkHungerCycleSec = 5;
    public static int sharkReproLimit = 3;
    public static AtomicInteger sharkAmount = new AtomicInteger(0);


    public Shark() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        sharkAmount.incrementAndGet();
        phLevelMax = sharkPhLevelMax;
        phLevelMin = sharkPhLevelMin;
        tempCelsiusMax = sharkTempCelsiusMax;
        tempCelsiusMin = sharkTempCelsiusMin;
        speed = sharkSpeed;
        hungerCycle = sharkHungerCycleSec;
        reproductionLimit = reproductionValue = sharkReproLimit;
        edibleFood.add(Animal.class);
        edibleFood.add(Remainings.class);
        logger.info("Shark species spawned");
    }

    public Shark(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        sharkAmount.incrementAndGet();
        this.x = x;
        this.y = y;
        setCellAnimal(this,x,y);
        phLevelMax = sharkPhLevelMax;
        phLevelMin = sharkPhLevelMin;
        tempCelsiusMax = sharkTempCelsiusMax;
        tempCelsiusMin = sharkTempCelsiusMin;
        speed = sharkSpeed;
        hungerCycle = sharkHungerCycleSec;
        reproductionLimit = reproductionValue = sharkReproLimit;
        edibleFood.add(Animal.class);
        edibleFood.add(Remainings.class);
        logger.info("Shark species spawned from repro");
    }



    void die() {
        if (isDead){
            return;
        }
        super.die();
        sharkAmount.decrementAndGet();
        logger.info("Shark died");

    }

    public static float getSharkPhLevelMax() {
        return sharkPhLevelMax;
    }


    public static void setSharkPhLevelMax(float sharkPhLevelMax) {
        Shark.sharkPhLevelMax = sharkPhLevelMax;
    }

    public static float getSharkPhLevelMin() {
        return sharkPhLevelMin;
    }

    public static void setSharkPhLevelMin(float sharkPhLevelMin) {
        Shark.sharkPhLevelMin = sharkPhLevelMin;
    }

    public static float getSharkTempCelsiusMax() {
        return sharkTempCelsiusMax;
    }

    public static void setSharkTempCelsiusMax(float sharkTempCelsiusMax) {
        Shark.sharkTempCelsiusMax = sharkTempCelsiusMax;
    }

    public static float getSharkTempCelsiusMin() {
        return sharkTempCelsiusMin;
    }

    public static void setSharkTempCelsiusMin(float sharkTempCelsiusMin) {
        Shark.sharkTempCelsiusMin = sharkTempCelsiusMin;
    }

    public static int getSharkSpeed() {
        return sharkSpeed;
    }

    public static void setSharkSpeed(int sharkSpeed) {
        Shark.sharkSpeed = sharkSpeed;
    }

    public static int getSharkHungerCycleSec() {
        return sharkHungerCycleSec;
    }

    public static void setSharkHungerCycleSec(int sharkHungerCycleSec) {
        Shark.sharkHungerCycleSec = sharkHungerCycleSec;
    }

    public static int getSharkReproLimit() {
        return sharkReproLimit;
    }

    public static void setSharkReproLimit(int sharkReproLimit) {
        Shark.sharkReproLimit = sharkReproLimit;
    }
}
