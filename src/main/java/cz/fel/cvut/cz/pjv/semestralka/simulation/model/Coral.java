package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.*;

/**
 * Corals that appear on the water tile adjacent to ground tiles, they serve as food for herbivore organisms
 */
public class Coral extends Food{
    private static final Logger logger = Logger.getLogger(Coral.class.getName());
    static {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
    }
    Random rand = new Random();
    public Coral() {
        ArrayList<GameCell> viableCells = getViableCells();
        int size = viableCells.size();
        int coralLocation = rand.nextInt(0,size);
        GameCell gameCell = viableCells.get(coralLocation);
        viableCells.remove(coralLocation);
        gameCell.setFood(this);
        x = gameCell.getX();
        y = gameCell.getY();
        logger.log(Level.INFO,"Coral created: X={0}, Y={1}", new String[]{String.valueOf(x), String.valueOf(y)});
    }

    /**
     * method that returns list of cells, that are adjacent to ground cell, are water and don't have a food object in them
     * @return
     */
    public static ArrayList<GameCell> getViableCells (){
        ArrayList<GameCell> viableCells = new ArrayList<>();
        //go through each cell
        for (GameCell[] cellRow : AquariumModel.getMapGrid()) {
            for (GameCell cell : cellRow) {
                //for each cell check if the adjacent cell is ground and if this cell is water and without food
                for (GameCell adjCell : cell.getAdjCells()){
                    if (!adjCell.isWater() && cell.getFood() == null && cell.isWater() && !viableCells.contains(cell)){
                        viableCells.add(cell);
                    }
                }
            }
        }
        return viableCells;
    }

    
}
