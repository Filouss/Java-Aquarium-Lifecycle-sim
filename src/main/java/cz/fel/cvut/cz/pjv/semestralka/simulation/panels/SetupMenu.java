package cz.fel.cvut.cz.pjv.semestralka.simulation.panels;

import cz.fel.cvut.cz.pjv.semestralka.simulation.controller.SimulatorController;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * Setup menu which appears before the start of the main simulation allowing the user to adjust animal and environmental factors
 */
public class SetupMenu extends Dialog<Void> {
    private static final Logger logger = Logger.getLogger(SetupMenu.class.getName());
    private final TextField simPlaygroundWidth = new TextField();
    private final TextField simPh = new TextField();
    private final TextField simTemp = new TextField();
    private final TextField waterRatio = new TextField();

    private CheckBox loggingToggle = new CheckBox();
    private final TextField simPlaygroundHeight = new TextField();
    private ArrayList<TextField> inputs = new ArrayList<>();
    public SetupMenu() {
        logger.setUseParentHandlers(false);
        Handler stdout = new StreamHandler(System.out, new SimpleFormatter()){
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(stdout);

        setTitle("Setup parameters to simulate");
        setHeaderText(null);

        DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().add(ButtonType.OK);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        //add fields for Aquarium parameters

        // set event to checkbox
        simPlaygroundWidth.setText(String.valueOf(AquariumModel.width));
        simPlaygroundWidth.setPrefWidth(160);
        simPlaygroundHeight.setText(String.valueOf(AquariumModel.height));
        simPlaygroundHeight.setPrefWidth(160);
        waterRatio.setText(String.valueOf(AquariumModel.waterRatio));
        waterRatio.setPrefWidth(160);
        waterRatio.setId("waterRatio");
        simPh.setText(String.valueOf(AquariumModel.phLevel));
        simPh.setPrefWidth(160);
        simTemp.setText(String.valueOf(AquariumModel.tempCelsius));
        simTemp.setPrefWidth(160);
        simTemp.setId("simTemp");
        simPh.setId("simPh");
        simPlaygroundHeight.setId("simPlaygroundHeight");
        simPlaygroundWidth.setId("simPlaygroundWidth");
        inputs.add(waterRatio);
        inputs.add(simPh);
        inputs.add(simTemp);
        inputs.add(simPlaygroundHeight);
        inputs.add(simPlaygroundWidth);
        grid.add(new Label("Playground Width"), 0, 0);
        grid.add(simPlaygroundWidth, 1, 0);
        grid.add(new Label("Playground Height"), 0, 1);
        grid.add(simPlaygroundHeight, 1, 1);
        grid.add(new Label("Playground ph level"), 0, 2);
        grid.add(simPh, 1, 2);
        grid.add(new Label("Playground temperature"), 0, 3);
        grid.add(simTemp, 1, 3);
        grid.add(new Label("Enable logging"), 0, 4);
        grid.add(loggingToggle,1,4);
        grid.add(new Label("Water ratio"), 0, 5);
        grid.add(waterRatio, 1, 5);

        String[][] allAnimalAttributeValues = {
                // Tuna
                {String.valueOf(Tuna.getTunaSpeed()), String.valueOf(Tuna.getTunaHungerCycleSec()), String.valueOf(Tuna.getTunaReproLimit()),
                        String.valueOf(Tuna.getTunaPhLevelMax()), String.valueOf(Tuna.getTunaPhLevelMin()), String.valueOf(Tuna.getTunaTempCelsiusMax()), String.valueOf(Tuna.getTunaTempCelsiusMin())},
                // Shark
                {String.valueOf(Shark.getSharkSpeed()), String.valueOf(Shark.getSharkHungerCycleSec()), String.valueOf(Shark.getSharkReproLimit()),
                        String.valueOf(Shark.getSharkPhLevelMax()), String.valueOf(Shark.getSharkPhLevelMin()), String.valueOf(Shark.getSharkTempCelsiusMax()), String.valueOf(Shark.getSharkTempCelsiusMin())},
                // Crab
                {String.valueOf(Crab.getCrabSpeed()), String.valueOf(Crab.getCrabHungerCycleSec()), String.valueOf(Crab.getCrabReproLimit()),
                        String.valueOf(Crab.getCrabPhLevelMax()), String.valueOf(Crab.getCrabPhLevelMin()), String.valueOf(Crab.getCrabTempCelsiusMax()), String.valueOf(Crab.getCrabTempCelsiusMin())},
                // Carp
                {String.valueOf(Carp.getCarpSpeed()), String.valueOf(Carp.getCarpHungerCycleSec()), String.valueOf(Carp.getCarpReproLimit()),
                        String.valueOf(Carp.getCarpPhLevelMax()), String.valueOf(Carp.getCarpPhLevelMin()), String.valueOf(Carp.getCarptempCelsiusMax()), String.valueOf(Carp.getCarptempCelsiusMin())},
                // Sea Urchin
                {String.valueOf(SeaUrchin.getSeaUrchinSpeed()), String.valueOf(SeaUrchin.getSeaUrchinHungerCycleSec()), String.valueOf(SeaUrchin.getSeaUrchinReproLimit()),
                        String.valueOf(SeaUrchin.getSeaUrchinPhLevelMax()), String.valueOf(SeaUrchin.getSeaUrchinPhLevelMin()), String.valueOf(SeaUrchin.getSeaUrchinTempCelsiusMax()), String.valueOf(SeaUrchin.getSeaUrchinTempCelsiusMin())}
        };
        String[] attributeNames = {"Speed", "Hunger", "Repro", "PhMax", "PhMin", "TempMax", "TempMin"};
        String[] animals = {"Tuna", "Shark", "Crab", "Carp", "SeaUrchin"};
        int column = 2;

        //go through each animal attribute and create label and textfield with id and correct value
        for (int animalIndex = 0; animalIndex < allAnimalAttributeValues.length; animalIndex++) {

            List<TextField> animalTextFields = new ArrayList<>();

            String[] attributeValues = allAnimalAttributeValues[animalIndex];
            int index = 0;
            for (String value : attributeValues) {
                TextField textField = new TextField();
                textField.setText(value);
                textField.setPrefWidth(160); // Set preferred width
                textField.setId(animals[animalIndex]+attributeNames[index]);
                animalTextFields.add(textField);
                index++;
                inputs.add(textField);
            }

            String animal = animals[animalIndex];
            //add them to grid
            for (int i = 0; i < attributeNames.length; i++) {

                grid.add(new Label(animal + " " + attributeNames[i]), column, i);
                grid.add(animalTextFields.get(i), column+1, i);
            }
            column+=2;
        }

        dialogPane.setContent(grid);


        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setOnAction(e -> {
            if (!loggingToggle.isSelected()) {
                turnOffLogging();
            }
            updateSim();
            updateAnimalAttributes();
            close();
        });
    }

    private void updateSim(){
        int width = Integer.parseInt(getTextFieldByID("simPlaygroundWidth").getText());
        int height = Integer.parseInt(getTextFieldByID("simPlaygroundHeight").getText());
        float pH = Float.parseFloat(getTextFieldByID("simPh").getText());
        float temp = Float.parseFloat(getTextFieldByID("simTemp").getText());
        float ratio = Float.parseFloat(getTextFieldByID("waterRatio").getText());
        AquariumModel.width = width;
        AquariumModel.height = height;
        AquariumModel.tempCelsius = temp;
        AquariumModel.phLevel = pH;
        AquariumModel.waterRatio = ratio;
    }

    /**
     * for each animal update it's attributes
     */
    public void updateAnimalAttributes() {
        int speed = Integer.parseInt(getTextFieldByID("TunaSpeed").getText());
        int hungerLimit = Integer.parseInt(getTextFieldByID("TunaHunger").getText());
        int reproLimit = Integer.parseInt(getTextFieldByID("TunaRepro").getText());
        float phLevelMax = Float.parseFloat(getTextFieldByID("TunaPhMax").getText());
        float phLevelMin = Float.parseFloat(getTextFieldByID("TunaPhMin").getText());
        float tempLevelMax = Float.parseFloat(getTextFieldByID("TunaTempMax").getText());
        float tempLevelMin = Float.parseFloat(getTextFieldByID("TunaTempMin").getText());
        Tuna.setTunaSpeed(speed);
        Tuna.setTunaHungerCycleSec(hungerLimit);
        Tuna.setTunaReproLimit(reproLimit);
        Tuna.setTunaPhLevelMax(phLevelMax);
        Tuna.setTunaPhLevelMin(phLevelMin);
        Tuna.setTunaTempCelsiusMax(tempLevelMax);
        Tuna.setTunaTempCelsiusMin(tempLevelMin);

        // Update attributes for Shark
        speed = Integer.parseInt(getTextFieldByID("SharkSpeed").getText());
        hungerLimit = Integer.parseInt(getTextFieldByID("SharkHunger").getText());
        reproLimit = Integer.parseInt(getTextFieldByID("SharkRepro").getText());
        phLevelMax = Float.parseFloat(getTextFieldByID("SharkPhMax").getText());
        phLevelMin = Float.parseFloat(getTextFieldByID("SharkPhMin").getText());
        tempLevelMax = Float.parseFloat(getTextFieldByID("SharkTempMax").getText());
        tempLevelMin = Float.parseFloat(getTextFieldByID("SharkTempMin").getText());
        Shark.setSharkSpeed(speed);
        Shark.setSharkHungerCycleSec(hungerLimit);
        Shark.setSharkReproLimit(reproLimit);
        Shark.setSharkPhLevelMax(phLevelMax);
        Shark.setSharkPhLevelMin(phLevelMin);
        Shark.setSharkTempCelsiusMax(tempLevelMax);
        Shark.setSharkTempCelsiusMin(tempLevelMin);

        // Update attributes for Crab
        speed = Integer.parseInt(getTextFieldByID("CrabSpeed").getText());
        hungerLimit = Integer.parseInt(getTextFieldByID("CrabHunger").getText());
        reproLimit = Integer.parseInt(getTextFieldByID("CrabRepro").getText());
        phLevelMax = Float.parseFloat(getTextFieldByID("CrabPhMax").getText());
        phLevelMin = Float.parseFloat(getTextFieldByID("CrabPhMin").getText());
        tempLevelMax = Float.parseFloat(getTextFieldByID("CrabTempMax").getText());
        tempLevelMin = Float.parseFloat(getTextFieldByID("CrabTempMin").getText());
        Crab.setCrabSpeed(speed);
        Crab.setCrabHungerCycleSec(hungerLimit);
        Crab.setCrabReproLimit(reproLimit);
        Crab.setCrabPhLevelMax(phLevelMax);
        Crab.setCrabPhLevelMin(phLevelMin);
        Crab.setCrabTempCelsiusMax(tempLevelMax);
        Crab.setCrabTempCelsiusMin(tempLevelMin);

        // Update attributes for Carp (Already provided in the question)
        speed = Integer.parseInt(getTextFieldByID("CarpSpeed").getText());
        hungerLimit = Integer.parseInt(getTextFieldByID("CarpHunger").getText());
        reproLimit = Integer.parseInt(getTextFieldByID("CarpRepro").getText());
        phLevelMax = Float.parseFloat(getTextFieldByID("CarpPhMax").getText());
        phLevelMin = Float.parseFloat(getTextFieldByID("CarpPhMin").getText());
        tempLevelMax = Float.parseFloat(getTextFieldByID("CarpTempMax").getText());
        tempLevelMin = Float.parseFloat(getTextFieldByID("CarpTempMin").getText());
        Carp.setCarpSpeed(speed);
        Carp.setCarpHungerCycleSec(hungerLimit);
        Carp.setCarpReproLimit(reproLimit);
        Carp.setCarpPhLevelMax(phLevelMax);
        Carp.setCarpPhLevelMin(phLevelMin);
        Carp.setCarptempCelsiusMax(tempLevelMax);
        Carp.setCarptempCelsiusMin(tempLevelMin);

        // Update attributes for Sea Urchin
        speed = Integer.parseInt(getTextFieldByID("SeaUrchinSpeed").getText());
        hungerLimit = Integer.parseInt(getTextFieldByID("SeaUrchinHunger").getText());
        reproLimit = Integer.parseInt(getTextFieldByID("SeaUrchinRepro").getText());
        phLevelMax = Float.parseFloat(getTextFieldByID("SeaUrchinPhMax").getText());
        phLevelMin = Float.parseFloat(getTextFieldByID("SeaUrchinPhMin").getText());
        tempLevelMax = Float.parseFloat(getTextFieldByID("SeaUrchinTempMax").getText());
        tempLevelMin = Float.parseFloat(getTextFieldByID("SeaUrchinTempMin").getText());
        SeaUrchin.setSeaUrchinSpeed(speed);
        SeaUrchin.setSeaUrchinHungerCycleSec(hungerLimit);
        SeaUrchin.setSeaUrchinReproLimit(reproLimit);
        SeaUrchin.setSeaUrchinPhLevelMax(phLevelMax);
        SeaUrchin.setSeaUrchinPhLevelMin(phLevelMin);
        SeaUrchin.setSeaUrchinTempCelsiusMax(tempLevelMax);
        SeaUrchin.setSeaUrchinTempCelsiusMin(tempLevelMin);
        logger.info("Updated animal attributes");
    }



    private TextField getTextFieldByID(String ID) {
        for (TextField tf : inputs) {
            if (tf.getId().equals(ID)) {
                return tf;
            }
        }
        //couldn't find correct textfield
        TextField errorTextField = new TextField();
        errorTextField.setText("nenalezl sem");
        logger.log(Level.SEVERE,"No textfield with this id found");
        return errorTextField;
    }

    private void turnOffLogging(){
        // get the root logger and all the handlers, and remove them
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        //set the last remaining logger to off
        rootLogger.setLevel(Level.OFF);
    }
}
