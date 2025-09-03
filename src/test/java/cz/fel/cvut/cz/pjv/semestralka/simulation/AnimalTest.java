package cz.fel.cvut.cz.pjv.semestralka.simulation;

import cz.fel.cvut.cz.pjv.semestralka.simulation.model.Animal;
import cz.fel.cvut.cz.pjv.semestralka.simulation.model.AquariumModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnimalTest {

    @BeforeEach
    public void initGrid(){
        AquariumModel model = new AquariumModel();
    }

    @Test
    public void constructorTest(){
        Animal animal = new Animal();
        Assertions.assertEquals(animal.getState(), Animal.State.ALIVE);
    }

    @Test
    public void constructorTestWithParams(){
        Animal animal = new Animal(1,2);
        Assertions.assertEquals(animal.getX(),1);
        Assertions.assertEquals(animal.getY(),2);
    }

    @Test
    public void move_x5Y7_correctCoords(){
        Animal animal = new Animal();
        animal.move(5,7);
        Assertions.assertEquals(animal.getX(),5);
        Assertions.assertEquals(animal.getY(),7);
    }
}
