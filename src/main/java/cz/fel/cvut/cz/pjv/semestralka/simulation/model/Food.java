package cz.fel.cvut.cz.pjv.semestralka.simulation.model;

import javafx.scene.image.ImageView;

import java.util.logging.*;

/**
 * superclass for edible items, which are not animals
 */
public class Food {
    private static final Logger logger = Logger.getLogger(Food.class.getName());
    public Visibility visibility;
     int x,y;
     public volatile static int foodAmount = 0;
     protected ImageView imageView = new ImageView();

    public enum Visibility {
        VISIBLE, HIDDEN
    }

    public Food(int x, int y) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        foodAmount++;
        visibility = Visibility.VISIBLE;
        this.x = x;
        this.y = y;
        logger.info("new food object created");
    }

    public Food() {
        foodAmount++;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
