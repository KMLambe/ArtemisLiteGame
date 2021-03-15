package artemis.lite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private ArtemisSystem s1;
    private Component c1, c2, c3;
    private Player p1, p2, p3;

    private String validPlayerName1, validPlayerName2, validComponentName1, validComponentName2, validSystemName,
            validPlayerName3, validComponentName3;
    private int validResourceBalance1, validResourceBalance2, validBoardPosition1, validBoardPosition2,
            validComponentCost1, validComponentCost2, validCostToDevelop1, validCostToDevelop2, validCostForLanding1,
            validCostForLanding2, validResourceBalance3, validBoardPosition3, validComponentCost3, validCostToDevelop3,
            validCostForLanding3;

    @BeforeEach
    void setUp() {
        // system test data
        validSystemName = "validSystem";

        // component test data
        validComponentName1 = "validComponent1";
        validComponentName2 = "validComponent2";
        validComponentName3 = "validTradeComponent3";
        validComponentCost1 = 80;
        validComponentCost2 = 50;
        validComponentCost3 = 20;

        validCostToDevelop1 = 20;
        validCostToDevelop2 = 30;
        validCostToDevelop3 = 30;

        validCostForLanding1 = 10;
        validCostForLanding2 = 15;
        validCostForLanding3 = 15;

        // player test data
        validPlayerName1 = "validPlayer1";
        validPlayerName2 = "validPlayer2";
        validPlayerName3 = "validPlayer3";

        validResourceBalance1 = 100;
        validResourceBalance2 = 50;
        validResourceBalance3 = 30;

        validBoardPosition1 = 0;
        validBoardPosition2 = 10;
        validBoardPosition3 = 3;

        // create test objects
        s1 = new ArtemisSystem(validSystemName);

        c1 = new Component(validComponentName1, validComponentCost1, validCostToDevelop1, validCostForLanding1, s1);
        c2 = new Component(validComponentName2, validComponentCost2, validCostToDevelop2, validCostForLanding2, s1);
        c3 = new Component(validComponentName3, validComponentCost3, validCostToDevelop3, validCostForLanding3, s1);

        p1 = new Player(validPlayerName1, validResourceBalance1, validBoardPosition1);
        p2 = new Player(validPlayerName2, validResourceBalance2, validBoardPosition2);
        p3 = new Player(validPlayerName3, validResourceBalance3, validBoardPosition3);


        // setup component for trading
        p3.purchaseComponent(c3);
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

    // tradeComponent testing
    @Test
    void tradeComponentValidOtherPlayerAgrees() {
        Component componentBeingTraded = c3;
        Player originalComponentOwner = p3;
        Player tradeInitiator = p1;

        int openingResourcesComponentOwner = originalComponentOwner.getResourceBalance();
        int openingResourcesTradeInitiator = tradeInitiator.getResourceBalance();
        int openingActionPointsTradeInitiator = tradeInitiator.getActionPoints();

        // make sure component is not already owned by player
        assertTrue(componentBeingTraded.getComponentOwner() != tradeInitiator);

        // invoke trade
        tradeInitiator.tradeComponent(componentBeingTraded, new Scanner("yes"));

        // component owner should be updated
        assertEquals(tradeInitiator, componentBeingTraded.getComponentOwner());

        // player resources should be updated be updated to reflect purchase and sale
        int expected = openingResourcesTradeInitiator - validComponentCost3;
        assertEquals(expected, tradeInitiator.getResourceBalance());

        expected = openingResourcesComponentOwner + validComponentCost3;
        assertEquals(expected, originalComponentOwner.getResourceBalance());

        // tradeInitiator action points has reduced
        expected = openingActionPointsTradeInitiator - 1;
        assertEquals(expected, tradeInitiator.getActionPoints());

        // originalComponentOwner should no longer have the component
        assertFalse(originalComponentOwner.getOwnedComponents().contains(componentBeingTraded));

        // tradeIntiator should now have component as part of their components
        assertTrue(tradeInitiator.getOwnedComponents().contains(componentBeingTraded));
    }


    @Test
    void tradeComponentValidOtherPlayerDoesNotAgree() {
        Component component = c3;
        Player componentOwner = component.getComponentOwner();
        Player tradeInitiator = p1;

        // values before trade is initiated
        int openingResourcesComponentOwner = componentOwner.getResourceBalance();
        int openingResourcesTradeInitiator = tradeInitiator.getResourceBalance();
        int openingActionPointsTradeInitiator = tradeInitiator.getActionPoints();

        // player1 starts trade, but component owner rejects it
        tradeInitiator.tradeComponent(component, new Scanner("no"));

        // make sure the owner hasn't changed
        assertEquals(componentOwner, c3.getComponentOwner());

        // player's resources should be unchanged
        assertEquals(openingResourcesComponentOwner, componentOwner.getResourceBalance());
        assertEquals(openingResourcesTradeInitiator, tradeInitiator.getResourceBalance());

        // tradeInitiator action points has reduced
        int expected = openingActionPointsTradeInitiator - 1;
        assertEquals(expected, tradeInitiator.getActionPoints());

        // originalComponentOwner should still have component
        assertTrue(componentOwner.getOwnedComponents().contains(component));

        // tradeIntiator should not have the component
        assertFalse(tradeInitiator.getOwnedComponents().contains(component));
    }

    @Test
    void tradeComponentInvalidInsufficientResources() {
        Component component = c3;
        Player componentOwner = component.getComponentOwner();
        Player tradeInitiator = p1;

        // set player1's resources to below the cost of the component
        tradeInitiator.setResourceBalance(15);

        // values before trade is initiated
        int openingResourcesComponentOwner = componentOwner.getResourceBalance();
        int openingResourcesTradeInitiator = tradeInitiator.getResourceBalance();
        int openingActionPointsTradeInitiator = tradeInitiator.getActionPoints();

        // now try to trade the component
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            p1.tradeComponent(c3, new Scanner("no"));
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("insufficient resources"));

        // check that values have not changed
        // player's resources should be unchanged
        assertEquals(openingResourcesComponentOwner, componentOwner.getResourceBalance());
        assertEquals(openingResourcesTradeInitiator, tradeInitiator.getResourceBalance());

        // tradeInitiator action points should not have changed
        assertEquals(openingActionPointsTradeInitiator, tradeInitiator.getActionPoints());
    }


    @Test
    void tradeComponentInvalidComponentHasNoOwner() {
        Component component = c3;
        Player tradeInitiator = p1;

        // update component so it has no owner
        component.setComponentOwner(null);

        // now try to trade the component
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            tradeInitiator.tradeComponent(component, new Scanner("yes"));
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("cannot be traded"));
    }

    @Test
    void tradeComponentInvalidPlayerInsufficientActionPoints() {
        Component component = c3;
        Player tradeInitiator = p1;

        // update player's action points
        tradeInitiator.setActionPoints(0);

        // now try to trade the component
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            tradeInitiator.tradeComponent(component, new Scanner("yes"));
        });

        assertTrue(illegalArgumentException.getMessage().toLowerCase().contains("insufficient action points"));
    }

    @Test
    void tradeComponentInvalidPlayerAlreadyOwns() {
        Component component = c3;
        Player tradeInitiator = component.getComponentOwner();

        // values before trade is initiated
        int openingResourcesTradeInitiator = tradeInitiator.getResourceBalance();
        int openingActionPointsTradeInitiator = tradeInitiator.getActionPoints();

        tradeInitiator.tradeComponent(component, new Scanner("yes"));

        // values should be unchanged
        assertEquals(tradeInitiator, component.getComponentOwner());
        assertEquals(openingActionPointsTradeInitiator, tradeInitiator.getActionPoints());
        assertEquals(openingResourcesTradeInitiator, tradeInitiator.getResourceBalance());
    }

}