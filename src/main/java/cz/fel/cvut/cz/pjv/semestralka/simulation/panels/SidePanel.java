package cz.fel.cvut.cz.pjv.semestralka.simulation.panels;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

/**
 * Side panel next to the map containing statistics of the simulation
 */
public class SidePanel extends VBox{
    private static final Logger logger = Logger.getLogger(SidePanel.class.getName());
    static int animals = AquariumModel.animalList.size();
    static AtomicInteger carps = Carp.CarpAmount;
    static AtomicInteger crabs = Crab.crabAmount;
    static AtomicInteger urchins = SeaUrchin.seaUrchinAmount;
    static AtomicInteger sharks = Shark.sharkAmount;
    static AtomicInteger tunas = Tuna.tunaAmount;
    static int food = Food.foodAmount;
    static int dead = Animal.deadAnimals;
    static Label animalLabel = new Label("Animal amount: " + animals);
    static Label carpLabel = new Label("Carp amount: " + carps);
    static Label crabLabel = new Label("Crab amount: " + crabs);
    static Label urchinLabel = new Label("Sea Urchin amount: " + urchins);
    static Label sharkLabel = new Label("Shark amount: " + sharks);
    static Label tunaLabel = new Label("Tuna amount: " + tunas);
    static Label phLabel = new Label("pH level: " + AquariumModel.phLevel);
    static Label tempLabel = new Label("temperature level: " + AquariumModel.tempCelsius);
    static Label foodLabel = new Label("Food level: " + food);
    static Label deadlabel = new Label("Dead animals: " + dead);
    private int height = 50;
    private ArrayList<Label> labels = new ArrayList<>();

    /**
     * Creates the sidepanel at the side of the simulation holding statistics
     *
     * @return Vbox with resized and relocated labels
     */
    public VBox createSidePanel(){
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);
        //create the Vbox object and configure styling
        VBox sidepanel = new VBox();
        sidepanel.setPrefWidth(200);
        sidepanel.setStyle("-fx-padding: 10 0 0 10;");
        logger.info("created sidepanel space");
        //add all labels
        sidepanel.getChildren().addAll(animalLabel,carpLabel,crabLabel,urchinLabel,sharkLabel,tunaLabel,phLabel,tempLabel,foodLabel,deadlabel);
        labels.add(carpLabel);
        labels.add(animalLabel);
        labels.add(crabLabel);
        labels.add(urchinLabel);
        labels.add(sharkLabel);
        labels.add(tunaLabel);
        labels.add(phLabel);
        labels.add(tempLabel);
        labels.add(foodLabel);
        labels.add(deadlabel);
        //resize the labels
        setLabelHeight();
        logger.info("added and setup Labels");
        //resize the Vbox
        sidepanel.setLayoutX(AquariumModel.width * AquariumModel.cell_size);
        sidepanel.setLayoutY(0);
        return sidepanel;
    }

    /**
     * method to update the stats inside the labels(gets called with the update of view)
     */
    public static void updateStats(){
        //for each label get the correct numbers
        carps = Carp.CarpAmount;
        animals = AquariumModel.animalList.size();
        crabs = Crab.crabAmount;
        urchins = SeaUrchin.seaUrchinAmount;
        sharks = Shark.sharkAmount;
        tunas = Tuna.tunaAmount;
        food = Food.foodAmount;
        dead = Animal.deadAnimals;
        //reassign the m to theri labels
        carpLabel.setText("Carp amount: " + carps);
        animalLabel.setText("Animal amount: " + animals);
        crabLabel.setText("Crab amount: " + crabs);
        urchinLabel.setText("Sea Urchin amount: " + urchins);
        sharkLabel.setText("Shark amount: " + sharks);
        tunaLabel.setText("Tuna amount: " + tunas);
        foodLabel.setText("Food amount: " + food);
        deadlabel.setText("Dead animals: " + dead);
    }

    private void setLabelHeight(){
        //for each label set correct height and font height
        for (Label label : labels){
            label.setPrefHeight(height);
            label.setFont(new Font(15.0));
        }
    }

}
