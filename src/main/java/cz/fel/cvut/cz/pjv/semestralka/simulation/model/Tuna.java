package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * The animal subclass for Tuna, contains tuna stats
 */
public class Tuna extends Animal{
    private static final Logger logger = Logger.getLogger(Tuna.class.getName());
    public static float tunaPhLevelMax = 8F;
    public static float tunaPhLevelMin = 7.5F;
    public static float tunaTempCelsiusMax = 30;
    public static float tunaTempCelsiusMin = 20;
    public static int tunaSpeed = 400;
    public static int tunaHungerCycleSec = 3;
    public static int tunaReproLimit = 3;
    public static AtomicInteger tunaAmount = new AtomicInteger(0);

    public Tuna() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        tunaAmount.incrementAndGet();
        phLevelMax = tunaPhLevelMax;
        phLevelMin = tunaPhLevelMin;
        tempCelsiusMax = tunaTempCelsiusMax;
        tempCelsiusMin = tunaTempCelsiusMin;
        speed = tunaSpeed;
        hungerCycle = tunaHungerCycleSec;
        reproductionLimit = reproductionValue = tunaReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Tuna species spawned");
    }

    public Tuna(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        tunaAmount.incrementAndGet();
        this.x = x;
        this.y = y;
        setCellAnimal(this,x,y);
        phLevelMax = tunaPhLevelMax;
        phLevelMin = tunaPhLevelMin;
        tempCelsiusMax = tunaTempCelsiusMax;
        tempCelsiusMin = tunaTempCelsiusMin;
        speed = tunaSpeed;
        hungerCycle = tunaHungerCycleSec;
        reproductionLimit = reproductionValue = tunaReproLimit;
        edibleFood.add(Coral.class);
        edibleFood.add(Remainings.class);
        logger.info("Tuna species spawned");
    }
    void die() {
        if (isDead){
            return;
        }
        super.die();
        tunaAmount.decrementAndGet();
        logger.info("Tuna died");
    }

    public static float getTunaPhLevelMax() {
        return tunaPhLevelMax;
    }

    public static void setTunaPhLevelMax(float tunaPhLevelMax) {
        Tuna.tunaPhLevelMax = tunaPhLevelMax;
    }

    public static float getTunaPhLevelMin() {
        return tunaPhLevelMin;
    }

    public static void setTunaPhLevelMin(float tunaPhLevelMin) {
        Tuna.tunaPhLevelMin = tunaPhLevelMin;
    }

    public static float getTunaTempCelsiusMax() {
        return tunaTempCelsiusMax;
    }

    public static void setTunaTempCelsiusMax(float tunaTempCelsiusMax) {
        Tuna.tunaTempCelsiusMax = tunaTempCelsiusMax;
    }

    public static float getTunaTempCelsiusMin() {
        return tunaTempCelsiusMin;
    }

    public static void setTunaTempCelsiusMin(float tunaTempCelsiusMin) {
        Tuna.tunaTempCelsiusMin = tunaTempCelsiusMin;
    }

    public static int getTunaSpeed() {
        return tunaSpeed;
    }

    public static void setTunaSpeed(int tunaSpeed) {
        Tuna.tunaSpeed = tunaSpeed;
    }

    public static int getTunaHungerCycleSec() {
        return tunaHungerCycleSec;
    }

    public static void setTunaHungerCycleSec(int tunaHungerCycleSec) {
        Tuna.tunaHungerCycleSec = tunaHungerCycleSec;
    }

    public static int getTunaReproLimit() {
        return tunaReproLimit;
    }

    public static void setTunaReproLimit(int tunaReproLimit) {
        Tuna.tunaReproLimit = tunaReproLimit;
    }
}
