package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.logging.*;


/**
 * The single cell which the map is created of. Can either be ground or water
 */
public class GameCell extends StackPane {
    private static final Logger logger = Logger.getLogger(GameCell.class.getName());
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
    private int x,y;
    private Animal animal;
    private Food food;
    private boolean isWater;
    private ArrayList<GameCell> adjCells = new ArrayList<>();

    public boolean isWater() {
            return isWater;
    }

    public synchronized Food getFood() {
            return food;
    }

    public synchronized void setFood(Food food) {
            this.food = food;
    }

    public GameCell(boolean isWater,int x, int y){

        this.isWater = isWater;
        this.x = x;
        this.y = y;
        logger.log(Level.INFO,"Cell created: X={0}, Y={1}", new String[]{String.valueOf(x), String.valueOf(y)});
    }

    /**
     * Assigns adjacent(up,down,left,right) cells to this cell
     */
    public void adjacentCells(){

        int [][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
        //for each direction, get the adjacent cell and add it to the list if its in model limits
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (newX >= 0 && newX<AquariumModel.width && newY>=0 && newY<AquariumModel.height) {
                adjCells.add(AquariumModel.getMapGrid()[newX][newY]);
            }
        }
    }

    /**
     * @return true if any adjacent cell is ground false otherwise
     */
    public boolean isNextToGround(){
        for (GameCell Gcell : this.getAdjCells()){
            if (!Gcell.isWater){return true;}
        }
        return false;
    }

    public synchronized Animal getAnimal() {
            return animal;
    }

    public synchronized void setAnimal(Animal animal) {
            this.animal = animal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<GameCell> getAdjCells() {
        return adjCells;
    }
}
