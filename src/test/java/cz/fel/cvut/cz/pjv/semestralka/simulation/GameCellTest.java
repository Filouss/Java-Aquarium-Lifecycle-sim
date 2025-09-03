package cz.fel.cvut.cz.pjv.semestralka.simulation;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.Coral;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.GameCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameCellTest {

    @Test
    public void adjacentCells_ListOfCellsNextToThis(){
        AquariumModel model = new AquariumModel();
        GameCell gameCell = AquariumModel.getCellByCoords(2,2);
        assertEquals(4, gameCell.getAdjCells().size());

        assertTrue(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[1][2]));
        assertTrue(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[2][1]));
        assertTrue(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[3][2]));
        assertTrue(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[2][3]));

        assertFalse(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[0][2]));
        assertFalse(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[2][0]));
        assertFalse(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[4][2]));
        assertFalse(gameCell.getAdjCells().contains(AquariumModel.getMapGrid()[2][4]));
    }

    @Test
    void testIsNextToGround() {
        GameCell waterCell = new GameCell(true, 2, 2);
        GameCell groundCell = new GameCell(false, 3, 2);

        // Add groundCell to the adjacent cells of waterCell
        waterCell.getAdjCells().add(groundCell);

        // Test when there is at least one adjacent ground cell
        assertTrue(waterCell.isNextToGround());

        // Test when there are no adjacent ground cells
        assertFalse(groundCell.isNextToGround());
    }

}

