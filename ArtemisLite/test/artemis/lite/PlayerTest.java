package artemis.lite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private ArtemisSystem s1;
    private Component c1, c2;
    private Player p1, p2;

    private String validPlayerName1, validPlayerName2, validComponentName1, validComponentName2, validSystemName;
    private int validResourceBalance1, validResourceBalance2, validBoardPosition1, validBoardPosition2,
            validComponentCost1, validComponentCost2, validCostToDevelop1, validCostToDevelop2, validCostForLanding1,
            validCostForLanding2;

    @BeforeEach
    void setUp() {
        // system test data
        validSystemName = "validSystem";

        // component test data
        validComponentName1 = "validComponent1";
        validComponentName2 = "validComponent2";
        validComponentCost1 = 80;
        validComponentCost2 = 50;
        validCostToDevelop1 = 20;
        validCostToDevelop2 = 30;
        validCostForLanding1 = 10;
        validCostForLanding2 = 15;

        // player test data
        validPlayerName1 = "validPlayer1";
        validPlayerName2 = "validPlayer2";
        validResourceBalance1 = 100;
        validResourceBalance2 = 50;
        validBoardPosition1 = 0;
        validBoardPosition2 = 10;

        // create test objects
        s1 = new ArtemisSystem(validSystemName);

        c1 = new Component(validComponentName1, validComponentCost1, validCostToDevelop1, validCostForLanding1, s1);
        c2 = new Component(validComponentName2, validComponentCost2, validCostToDevelop2, validCostForLanding2, s1);

        p1 = new Player(validPlayerName1, validResourceBalance1, validBoardPosition1);
        p2 = new Player(validPlayerName2, validResourceBalance2, validBoardPosition2);
    }

    @Test
    void purchaseComponentValid() throws Exception {

        // owner is null
        assertEquals(null, c1.getComponentOwner());

        // player ownedComponents is empty
        assertTrue(p1.getOwnedComponents().size() == 0);

        // valid purchase
        p1.purchaseComponent(c1);

        // owner is now p1
        assertEquals(p1, c1.getComponentOwner());

        // player components contains new component
        assertTrue(p1.getOwnedComponents().contains(c1));
    }

    @Test
    void purchaseComponentUpdatesResources() {
        // check opening balance
        assertEquals(validResourceBalance1, p1.getResourceBalance());

        p1.purchaseComponent(c1);

        // expect new resources value to equal opening balance minus cost of component
        int expected = validResourceBalance1 - c1.getComponentCost();
        assertEquals(expected, p1.getResourceBalance());

        // check that the values have changed - if c1.componentCost=0 the above statements would still be true
        // but below would not
        assertFalse(p1.getResourceBalance() == validResourceBalance1);
    }

    @Test
    void purchaseComponentUpdatesActionPoints() {
        // check default action points applied
        assertEquals(Game.DEFAULT_ACTION_POINTS, p1.getActionPoints());
        // make sure user has action points
        assertTrue(p1.getActionPoints() > 0);

        p1.purchaseComponent(c1);

        // expect action points to be minus 1
        int expected = Game.DEFAULT_ACTION_POINTS - 1;
        assertEquals(expected, p1.getActionPoints());
    }

    @Test
    void purchaseComponentInvalidInsufficientResources() {

        // owner is null
        assertEquals(null, c1.getComponentOwner());

        // p2 does not have sufficient resources - expect to fail
        Exception exception = assertThrows(Exception.class, () -> {
            p2.purchaseComponent(c1);
        });

        // check that the error message is shown
        assertTrue(exception.getMessage().toLowerCase().contains("insufficient resources"));
    }

    @Test
    void purchaseComponentInvalidNoActionPoints() {
        // set to zero
        p1.setActionPoints(0);
        assertEquals(0, p1.getActionPoints());

        // try to purchase
        Exception exception = assertThrows(Exception.class, () -> {
            p1.purchaseComponent(c1);
        });

        // check that the error message is shown
        assertTrue(exception.getMessage().toLowerCase().contains("insufficient action points"));
    }

    @Test
    void purchaseComponentInvalidAlreadyOwned() {
        // p1 owns c2
        p1.purchaseComponent(c2);
        assertEquals(p1, c2.getComponentOwner());

        // p2 tries to purchase c2
        Exception exception = assertThrows(Exception.class, () -> {
            p2.purchaseComponent(c2);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("already owned"));
    }
}