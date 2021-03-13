package artemis.lite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests written to ensure the functionality within the Board object is performing as expected.
 *
 * @author John Young 40030361
 */
class BoardTest {
    String validSqName1, validSqName2, validSqName3, validSqName4, validSqName5, validSqName6, validSqName7,
            validSqName8, validSqName9, validSqName10, validSqName11, validSqName12, sysName1, sysName2, sysName3,
            sysName4, invalidName1, invalidName2, invalidName3;
    int validCost1, validCost2, validLandingCost1, validLandingCost2, validDevelopmentCost1, validDevelopmentCost2;
    Square square1, square2, square3, square4, square5, square6, square7, square8, square9, square10, square11, square12;
    ArtemisSystem s1, s2, s3, s4;
    Board b;

    @BeforeEach
    void setUp() {
        // square test data
        validSqName1 = "valid1";
        validSqName2 = "valid2";
        validSqName3 = "valid3";
        validSqName4 = "valid4";
        validSqName5 = "valid5";
        validSqName6 = "valid6";
        validSqName7 = "valid7";
        validSqName8 = "valid8";
        validSqName9 = "valid9";
        validSqName10 = "valid10";
        validSqName11 = "valid11";
        validSqName12 = "valid12";

        invalidName1 = "invalidSquareNameinvalidSquareNameinvalidSquareNameinvalidSquareName";
        invalidName2 = "";
        invalidName3 = " ";

        // system test data
        sysName1 = "validSystem1";
        sysName2 = "validSystem2";
        sysName3 = "validSystem3";
        sysName4 = "validSystem4";

        // component test data
        validCost1 = 100;
        validCost2 = 33;
        validLandingCost1 = 15;
        validLandingCost2 = 5;
        validDevelopmentCost1 = 10;
        validDevelopmentCost2 = 3;

        // create test objects
        b = new Board();
    }

    // tests for creating systems
    @Test
    void createSystemsValid() {
        s1 = b.createSystem(sysName1);
        // check that system was created
        assertEquals(sysName1, s1.getSystemName());

        // add component to system
        square1 = b.createSquare(validSqName2, validCost1, validDevelopmentCost1, validLandingCost1, s1);
        // recast square as component
        Component component = (Component) square1;
        s1.addComponent(component);

        // system should now contain the component
        assertTrue(s1.getComponentsInSystem().contains(square1));
        // component should have the system set
        assertEquals(s1, component.getComponentSystem());
    }

    @Test
    void createSystemInvalidName() {
        // create squares and store as individual variables
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            b.createSystem(invalidName1);
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("invalid name"));

        assertThrows(IllegalArgumentException.class, () -> {
            b.createSystem(invalidName2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.createSystem(invalidName3);
        });
    }

    @Test
    void createSystemInvalidTooManySystems() {
        // create all valid squares first
        for (int systemNumber = 0; systemNumber < Game.MAXIMUM_SYSTEMS; systemNumber++) {
            b.createSystem(sysName1);
        }

        // next square will be over the limit and should through an exception
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            b.createSystem(sysName1);
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("too many systems added"));
    }


    // tests for creating squares
    @Test
    void createSquaresValid() {
        // create systems
        s1 = new ArtemisSystem(sysName1);
        s2 = new ArtemisSystem(sysName2);
        s3 = new ArtemisSystem(sysName3);
        s4 = new ArtemisSystem(sysName4);

        // check squares array is the correct size
        assertEquals(Game.MAXIMUM_SQUARES, b.getSquares().length);

        // create squares and store as individual variables
        square1 = b.createSquare(validSqName1);
        square2 = b.createSquare(validSqName2, validCost1, validDevelopmentCost1, validLandingCost1, s1);
        square3 = b.createSquare(validSqName3, validCost1, validDevelopmentCost1, validLandingCost1, s1);
        square4 = b.createSquare(validSqName4);
        square5 = b.createSquare(validSqName5, validCost2, validDevelopmentCost2, validLandingCost2, s2);
        square6 = b.createSquare(validSqName6, validCost2, validDevelopmentCost2, validLandingCost2, s2);
        square7 = b.createSquare(validSqName7, validCost2, validDevelopmentCost2, validLandingCost2, s2);
        square8 = b.createSquare(validSqName8, validCost2, validDevelopmentCost2, validLandingCost2, s3);
        square9 = b.createSquare(validSqName9, validCost2, validDevelopmentCost2, validLandingCost2, s3);
        square10 = b.createSquare(validSqName10, validCost2, validDevelopmentCost2, validLandingCost2, s4);
        square11 = b.createSquare(validSqName11, validCost2, validDevelopmentCost2, validLandingCost2, s4);
        square12 = b.createSquare(validSqName12, validCost2, validDevelopmentCost2, validLandingCost2, s4);

        // check sample of values match
        assertEquals(validSqName1, square1.getSquareName());
        assertEquals(0, square1.getSquarePosition());

        assertEquals(validSqName6, square6.getSquareName());
        assertEquals(5, square6.getSquarePosition());

        assertEquals(validSqName12, square12.getSquareName());
        assertEquals(11, square12.getSquarePosition());

        // check type of objects
        assertTrue(square2 instanceof Component);
        assertTrue(square7 instanceof Component);
        assertTrue(square12 instanceof Component);
        assertFalse(square1 instanceof Component);
        assertFalse(square4 instanceof Component);
    }

    @Test
    void createSquareInvalidName() {
        // create squares and store as individual variables
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            b.createSquare(invalidName1);
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("invalid name"));

        assertThrows(IllegalArgumentException.class, () -> {
            b.createSquare(invalidName2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.createSquare(invalidName3);
        });
    }

    @Test
    void createSquaresInvalidTooManySquares() {
        // create all valid squares first
        for (int squareNumber = 0; squareNumber < Game.MAXIMUM_SQUARES; squareNumber++) {
            b.createSquare(validSqName1);
        }

        // next square will be over the limit and should through an exception
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            b.createSquare(validSqName1);
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("too many squares added"));
    }
}