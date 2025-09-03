package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.logging.*;

/**
 * This object appears when an animal dies of starvation or other factors, it serves as food for any other Animal
 */
public class Remainings extends Food{
    private static final Logger logger = Logger.getLogger(Remainings.class.getName());

    public Remainings(int x, int y) {
        super(x, y);
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        GameCell gameCell = AquariumModel.getCellByCoords(x, y);
        gameCell.setFood(this);
        logger.log(Level.INFO,"Remainings created: X={0}, Y={1}", new String[]{String.valueOf(x), String.valueOf(y)});
    }
}
