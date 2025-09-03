package cz.fel.cvut.cz.pjv.semestralka.simulation;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CrabTest {

    @BeforeEach
    public void initGrid(){
        AquariumModel model = new AquariumModel();
    }

    @Test
    public void constructorTest(){
        Crab crab = new Crab();
        Assertions.assertEquals(1,Crab.crabAmount);
        Assertions.assertEquals(crab.getState(), Animal.State.ALIVE);
    }

    @Test
    public void constructorTestWithParams(){
        Crab crab = new Crab(1,2);
        Assertions.assertEquals(crab.getX(),1);
        Assertions.assertEquals(crab.getY(),2);
    }

    @Test
    public void move_movesLeftRightOrDown(){
        Crab crab = new Crab();
        int x = crab.getX();
        int y = crab.getY();
        crab.move();
        Assertions.assertTrue(((crab.getX() == x+1 || crab.getX() == x-1) && y == crab.getY()) || (y == crab.getY()+1 && x == crab.getX()));
    }


}

