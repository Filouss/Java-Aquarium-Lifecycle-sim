package cz.fel.cvut.cz.pjv.semestralka.simulation.controller;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel;
import cz.fel.cvut.cz.pjv.semestralka.simulation.panels.SetupMenu;
import cz.fel.cvut.cz.pjv.semestralka.simulation.view.SimulatorView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class, that starts the whole app, containing communication between model and view
 */

public class SimulatorController extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) {
        // create the setup menu
        SetupMenu setupMenu = new SetupMenu();
        setupMenu.showAndWait();
        beginSimulation(stage);
    }

    /**
     * method that initiates the creation of model and view, and calls map drawing method in view
     * @param stage
     */
    private void beginSimulation(Stage stage){
        // create model, pass to view and draw the map
        AquariumModel model = new AquariumModel();
        SimulatorView view = new SimulatorView(model,stage);
        view.initMapDrawing();
    }
}
