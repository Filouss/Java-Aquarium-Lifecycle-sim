package cz.fel.cvut.cz.pjv.semestralka.simulation;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.GameCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AquariumModelTest {

    @Test
    public void constructorTest(){
        AquariumModel model = new AquariumModel();
        Assertions.assertEquals(model.width,20);
        Assertions.assertEquals(model.height,15);
        Assertions.assertEquals(model.cell_size,55);
        Assertions.assertFalse(AquariumModel.animalList.isEmpty());
    }

    @Test
    public void getCellByCoords_validCell_correctCell(){
        AquariumModel model = new AquariumModel();
        GameCell gameCell = AquariumModel.getMapGrid()[1][5];
        GameCell gameCell1 = AquariumModel.getCellByCoords(1,5);
        Assertions.assertEquals(gameCell1,gameCell);
    }
    @Test
    public void getCellByCoords_invalidCell_null(){
        AquariumModel model = new AquariumModel();

        GameCell gameCell1 = AquariumModel.getCellByCoords(100,5);
        Assertions.assertNull(gameCell1);
    }
}
