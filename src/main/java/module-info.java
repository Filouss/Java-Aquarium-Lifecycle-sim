module cz.fel.cvut.cz.pjv.semestralka.simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens cz.fel.cvut.cz.pjv.semestralka.simulation to javafx.fxml;
    exports cz.fel.cvut.cz.pjv.semestralka.simulation.controller;
}