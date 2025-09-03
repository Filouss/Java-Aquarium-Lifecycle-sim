package cz.fel.cvut.cz.pjv.semestralka.simulation;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.Coral;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.GameCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoralTest {

    @Test
    public void constructorTest(){
        AquariumModel model = new AquariumModel();
        Coral coral = new Coral();
        Assertions.assertTrue(AquariumModel.getCellByCoords(coral.getX(),coral.getY()).isWater());
    }

    @Test
    public void getViableCells_viableGrid_returnsArrayListOfWater(){
        AquariumModel model = new AquariumModel();
        Coral coral = new Coral();
        for (GameCell cell : Coral.getViableCells()){
            Assertions.assertTrue(cell.isWater() && cell.isNextToGround());
        }
    }
}
