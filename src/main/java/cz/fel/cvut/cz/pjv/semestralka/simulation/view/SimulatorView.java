package cz.fel.cvut.cz.pjv.semestralka.simulation.view;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.*;
import cz.fel.cvut.cz.pjv.semestralka.simulation.panels.SidePanel;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import static cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel.getCellByCoords;

/**
 * class responsible for rendering and updating the app GUI
 */
public class SimulatorView {
    private static final Logger logger = Logger.getLogger(SimulatorView.class.getName());
    private AquariumModel model;
    private Pane root = new Pane();
    public static Group cellGroup = new Group();
    public static Group entitiygroup = new Group();
    private Stage primaryStage;


    public SimulatorView(AquariumModel model, Stage stage) {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);

        this.model = model;
        this.primaryStage = stage;
        logger.info("View object created");
    }

    /**
     * creates the scene and starts the map drawing process
     */
    public void initMapDrawing(){
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Aquarium ecosystem simulation");
        Scene scene = new Scene(createMap());
        primaryStage.setScene(scene);
        primaryStage.show();
        for (Animal animal : AquariumModel.animalList){
            animal.lockNotify();
        }
        updateEntities();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        logger.info("GUI of map created");
    }



    private Parent createMap(){
        // set size of the window and setup each cell
        root.setPrefSize(AquariumModel.width *AquariumModel.cell_size + 250,AquariumModel.height*AquariumModel.cell_size);
        for (int i = AquariumModel.height - 1; i >= 0; i--) {
            for (int j = 0; j < AquariumModel.width; j++) {
                GameCell cell = AquariumModel.getMapGrid()[j][i];
                setupCell(cell);
            }
        }
        logger.info("Cells relocated and setup");
        // draw entities
        drawAnimals();
        drawFood();
        //create the sidepanel holding stats
        SidePanel sidePanel = new SidePanel();
        VBox sideView = sidePanel.createSidePanel();
        //add everything to the stage
        root.getChildren().addAll(cellGroup,entitiygroup, sideView);
        logger.info("Map drawn");
        return root;
    }


    private void drawAnimals() {
        List<Animal> animals = AquariumModel.animalList;
        synchronized (AquariumModel.animalList) {
            animals = new ArrayList<>(AquariumModel.animalList);
        }
        //for each animal create Imageview Node and add it to entity group
        for(Animal animal :  animals){
            try {
                Node animalNode = createAnimalNode(animal);
                entitiygroup.getChildren().add(animalNode);
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE,"Image not found",e);
            }
        }
        logger.info("Animals drawn");
    }

    private void drawFood()  {
        //copy the array before iterating to prevent concurrent modification
        ArrayList<Food> foodCopy = new ArrayList<>(AquariumModel.foodList);
        for(Food food : foodCopy){
            ImageView imageView = food.getImageView();
            try {
                //get the correct food and create its image
                if (food instanceof Remainings) {
                    imageView.setImage(new Image(new FileInputStream(ImageConsts.REMAININGS_PATH)));
                } else {
                    imageView.setImage(new Image(new FileInputStream(ImageConsts.CORAL_PATH)));
                }
                //resize the image and relocate to be on the correct coords
                imageView.setFitHeight(AquariumModel.cell_size * 0.8);
                imageView.setFitWidth(AquariumModel.cell_size * 0.8);
                imageView.relocate(food.getX() * AquariumModel.cell_size, (AquariumModel.height - food.getY() - 1) * AquariumModel.cell_size);
                //check if its already in the group
                if (!entitiygroup.getChildren().contains(food.getImageView())) {
                    entitiygroup.getChildren().add(food.getImageView());
                }
            }catch (FileNotFoundException e){
                logger.log(Level.SEVERE,"Image not found",e);
            }
        }
        logger.info("food drawn");
    }

    private Node createAnimalNode(Animal animal) throws FileNotFoundException {
        ImageView imageView = animal.getImageView();
        // create animal imageView Node bsaed on its class
        switch (animal) {
            case Carp carp -> imageView.setImage(new Image(new FileInputStream(ImageConsts.CARP_PATH)));
            case Shark shark -> imageView.setImage(new Image(new FileInputStream(ImageConsts.SHARK_PATH)));
            case Crab crab -> imageView.setImage(new Image(new FileInputStream(ImageConsts.CRAB_PATH)));
            case Tuna tuna -> imageView.setImage(new Image(new FileInputStream(ImageConsts.TUNA_PATH)));
            default -> imageView.setImage(new Image(new FileInputStream(ImageConsts.SEAURCHIN_PATH)));
        }
        // resize to fit the cell height
        imageView.setFitHeight(AquariumModel.cell_size * 0.8);
        imageView.setFitWidth(AquariumModel.cell_size * 0.8);
        logger.info("Created animal GUI");
        return imageView;
    }

    private void updateEntities(){
        //update entities periodically using the Javafx timer thread
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                SidePanel.updateStats();
                // no animals, end the sim
                if (AquariumModel.animalList.isEmpty()){
                    this.stop();
                    showEndStage();}
                //go through each dead animal and hide it's gui
                for (Animal animal : AquariumModel.animalListeaten){
                    animal.getImageView().setVisible(false);
                }
                for (Animal animal : AquariumModel.animalList) {
                    //check for new animals without an imageview
                    if (animal.getImageView().getImage() == null && animal.getState() == Animal.State.ALIVE) {
                        try {
                            entitiygroup.getChildren().add(createAnimalNode(animal));
                        } catch (FileNotFoundException e) {
                            logger.log(Level.SEVERE,"Image not found",e);
                        }
                    }
                    //relocate if the animal moved
                    Node animalView = animal.getImageView();
                    animalView.relocate(animal.getX() * AquariumModel.cell_size, (AquariumModel.height - animal.getY() - 1) * AquariumModel.cell_size);
                }
                drawFood();
                ArrayList<Food> fCopy = AquariumModel.foodListeaten;
                for (Food food : fCopy){
                    if (getCellByCoords(food.getX(), food.getY()) == null) {
                        food.visibility = Food.Visibility.HIDDEN;
                    }
                    food.getImageView().setVisible(false);
                }
            }
        };
        timer.start();
    }

    private void showEndStage(){
        //create new stage with end message
        Stage newStage = new Stage();
        VBox vBox = new VBox();
        Label endMessage = new Label("  Všechna zvířata zemřela, konec simulace");
        endMessage.setPrefHeight(100);
        endMessage.setFont(new Font(25));
        vBox.getChildren().add(endMessage);

        Scene stageScene = new Scene(vBox, 500, 100);
        newStage.setScene(stageScene);
        newStage.show();
    }

    private void setupCell(GameCell cell){
        ImageView imageView;
        //assign correct image if the cell is water or ground
        if (cell.isWater()){
            try {
                imageView = new ImageView(new Image(new FileInputStream(ImageConsts.WATER_PATH)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                imageView = new ImageView(new Image(new FileInputStream(ImageConsts.GROUND_PATH)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        //resize and add borders
        imageView.setFitWidth(AquariumModel.cell_size);
        imageView.setFitHeight(AquariumModel.cell_size);

        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setStyle("-fx-border-color: black; -fx-border-width: 3;"); // Set border properties

        root.getChildren().add(borderPane);
        //relocate to the correct coords
        borderPane.relocate(cell.getX() *AquariumModel.cell_size,(AquariumModel.height - cell.getY() - 1) *AquariumModel.cell_size);
    }
}
