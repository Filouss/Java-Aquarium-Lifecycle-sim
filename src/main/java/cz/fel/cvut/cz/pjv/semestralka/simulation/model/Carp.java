package cz.fel.cvut.cz.pjv.semestralka.simulation.model;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * The animal subclass for carp containing its stats
 */
public class Carp extends Animal {
    private static final Logger logger = Logger.getLogger(Carp.class.getName());
    public static float CarpPhLevelMax = 9F;
    public static float carpPhLevelMin = 6.5F;
    public static float carptempCelsiusMax = 30;
    public static float carptempCelsiusMin = 20;
    public static int CarpSpeed = 900;
    public static int CarpHungerCycleSec = 5 ;
    public static int CarpReproLimit = 3;
    public static AtomicInteger CarpAmount = new AtomicInteger(0);


    public Carp() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        CarpAmount.incrementAndGet();
        phLevelMax = CarpPhLevelMax;
        phLevelMin = carpPhLevelMin;
        tempCelsiusMax = carptempCelsiusMax;
        tempCelsiusMin = carptempCelsiusMin;
        speed = CarpSpeed;
        hungerCycle = CarpHungerCycleSec;
        reproductionLimit = reproductionValue =  CarpReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Carp species spawned");
    }

    public Carp(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        CarpAmount.incrementAndGet();
        this.x = x;
        this.y = y;
        setCellAnimal(this,x,y);
        phLevelMax = CarpPhLevelMax;
        phLevelMin = carpPhLevelMin;
        tempCelsiusMax = carptempCelsiusMax;
        tempCelsiusMin = carptempCelsiusMin;
        speed = CarpSpeed;
        hungerCycle = CarpHungerCycleSec;
        reproductionLimit = reproductionValue = CarpReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Carp species spawned from repro");
    }

    @Override
    void die() {
        if (isDead){
            return;
        }
        super.die();
        CarpAmount.decrementAndGet();
        logger.info("Carp died");

    }


    public static float getCarpPhLevelMax() {
        return CarpPhLevelMax;
    }

    public static void setCarpPhLevelMax(float carpPhLevelMax) {
        Carp.CarpPhLevelMax = carpPhLevelMax;
    }

    public static float getCarpPhLevelMin() {
        return carpPhLevelMin;
    }

    public static void setCarpPhLevelMin(float carpPhLevelMin) {
        Carp.carpPhLevelMin = carpPhLevelMin;
    }

    public static float getCarptempCelsiusMax() {
        return carptempCelsiusMax;
    }

    public static void setCarptempCelsiusMax(float carptempCelsiusMax) {
        Carp.carptempCelsiusMax = carptempCelsiusMax;
    }

    public static float getCarptempCelsiusMin() {
        return carptempCelsiusMin;
    }

    public static void setCarptempCelsiusMin(float carptempCelsiusMin) {
        Carp.carptempCelsiusMin = carptempCelsiusMin;
    }

    public static int getCarpSpeed() {
        return CarpSpeed;
    }

    public static void setCarpSpeed(int carpSpeed) {
        CarpSpeed = carpSpeed;
    }

    public static int getCarpHungerCycleSec() {
        return CarpHungerCycleSec;
    }

    public static void setCarpHungerCycleSec(int carpHungerCycleSec) {
        CarpHungerCycleSec = carpHungerCycleSec;
    }

    public static int getCarpReproLimit() {
        return CarpReproLimit;
    }

    public static void setCarpReproLimit(int carpReproLimit) {
        CarpReproLimit = carpReproLimit;
    }

}








