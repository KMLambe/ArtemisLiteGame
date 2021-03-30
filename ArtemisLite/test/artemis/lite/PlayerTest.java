package artemis.lite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private ArtemisSystem system1;
    private Component c1, c2, c3;
    private List<Component> fullyDevelopedComponents;
    private Square sq1;
    private Player p1, p2, p3;
    private Board board;

    private String validPlayerName1, validPlayerName2, validComponentName1, validComponentName2, validSystemName,
            validPlayerName3, validComponentName3, validSquareName;
    private int validResourceBalance1, validResourceBalance2, validBoardPosition1, validBoardPosition2,
            validComponentCost1, validComponentCost2, validCostToDevelop1, validCostToDevelop2, validCostForLanding1,
            validCostForLanding2, validResourceBalance3, validBoardPosition3, validComponentCost3, validCostToDevelop3,
            validCostForLanding3;

    @BeforeEach
    void setUp() {
        // system test data
        validSystemName = "validSystem";

        validSquareName = "validSquare";

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

        board = new Board();
        Game.setBoard(board);

        // create test objects
        system1 = board.createSystem(validSystemName);

        sq1 = board.createSquare(validSquareName);

        c1 = (Component) board.createSquare(validComponentName1, validComponentCost1, validCostToDevelop1, validCostForLanding1, system1);
        c2 = (Component) board.createSquare(validComponentName2, validComponentCost2, validCostToDevelop2, validCostForLanding2, system1);
        c3 = (Component) board.createSquare(validComponentName3, validComponentCost3, validCostToDevelop3, validCostForLanding3, system1);

        p1 = new Player(validPlayerName1, validResourceBalance1, validBoardPosition1);
        p2 = new Player(validPlayerName2, validResourceBalance2, validBoardPosition2);
        p3 = new Player(validPlayerName3, validResourceBalance3, validBoardPosition3);


        // add players to game list of players
        Game.setPlayers(Arrays.asList(p1, p2, p3));

        // setup component for trading
        p3.purchaseComponent(c3);
        
        // set component development stage
        c3.setDevelopmentStage(3);
        
        // add components to list
        fullyDevelopedComponents = new ArrayList<Component>();
        fullyDevelopedComponents.add(c3);

        // let game class know it is running tests
        Game.setTestMode(true);
    }

    @Test
    void purchaseComponentValid() throws Exception {

        // owner is null
        assertNull(c1.getComponentOwner());

        // player ownedComponents is empty
        assertEquals(p1.getOwnedComponents().size(), 0);

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

        // check that the values have changed - if c1.componentCost=0 the above
        // statements would still be true
        // but below would not
        assertFalse(p1.getResourceBalance() == validResourceBalance1);
    }

    @Test
    void purchaseComponentDoesNotConsumeAnyActionPoints() {
        // check default action points applied
        assertEquals(Game.DEFAULT_ACTION_POINTS, p1.getActionPoints());
        // make sure user has action points
        assertTrue(p1.getActionPoints() > 0);

        p1.purchaseComponent(c1);

        // expect action points to be minus 1
        int expected = Game.DEFAULT_ACTION_POINTS;
        assertEquals(expected, p1.getActionPoints());
    }

    @Test
    void purchaseComponentValidNoActionPoints() {
        // set to zero
        p1.setActionPoints(0);
        assertEquals(0, p1.getActionPoints());

        // try to purchase
        assertDoesNotThrow(() -> {
            p1.purchaseComponent(c1);
        });

        // check that the error message is shown
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
    void purchaseComponentInvalidNotAComponent() {
        // should output a message and not an exception
        assertDoesNotThrow(() -> {
            p1.purchaseComponent(sq1);
        });
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
    void tradeComponentNoOwnedComponents() {
        Player originalComponentOwner = p3;
        Player tradeInitiator = p1;

        // remove ownership of component so there are none to be displayed
        c3.setComponentOwner(null);

        // should not result in an exception
        assertDoesNotThrow(() -> {
            Game.displayComponentsForTrading(Game.getComponentsForTrading(tradeInitiator));
        });
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

    @Test
    void offerComponentToOtherPlayersValid2Yes0No() {
        p2.setResourceBalance(400);
        p3.setResourceBalance(500);

        // make sure component is not already owned
        assertTrue(c1.getComponentOwner() == null);

        p1.offerComponentToOtherPlayers(c1, new Scanner("yes yes"));

        // check that one of the other players now owns the component
        assertTrue(c1.getComponentOwner() == p2 || c1.getComponentOwner() == p3);
        assertTrue(c1.getComponentOwner() != p1);
    }

    @Test
    void offerComponentToOtherPlayersValid1Yes1No() {
        p2.setResourceBalance(400);
        p3.setResourceBalance(500);

        // make sure component is not already owned
        assertNull(c1.getComponentOwner());

        p1.offerComponentToOtherPlayers(c1, new Scanner("no yes"));

        // check that the expected player now owns the component
        assertEquals(p3, c1.getComponentOwner());
    }

    @Test
    void offerComponentToOtherPlayersValid0Yes2No() {
        p2.setResourceBalance(400);
        p3.setResourceBalance(500);

        // make sure component is not already owned
        assertNull(c1.getComponentOwner());

        p1.offerComponentToOtherPlayers(c1, new Scanner("no no"));

        // check that the expected player now owns the component
        assertNull(c1.getComponentOwner());
    }

    @Test
    void offerComponentToOtherPlayersInvalidThenValidInput() {
        p2.setResourceBalance(400);
        p3.setResourceBalance(500);

        // make sure component is not already owned
        assertNull(c1.getComponentOwner());

        // enter anything but yes or no will keep prompting the user until yes/no has
        // been inputted
        p1.offerComponentToOtherPlayers(c1, new Scanner("asdasd no asdasd no"));

        assertNull(c1.getComponentOwner());
    }

    @Test
    void offerComponentToOtherPlayersInvalidEmptyPlayers() {
        // empty player list
        Game.setPlayers(new ArrayList<Player>());

        assertDoesNotThrow(() -> {
            p1.offerComponentToOtherPlayers(c1, new Scanner(""));
        });

        assertNull(c1.getComponentOwner());
    }

    @Test
    void offerComponentToOtherPlayersInvalidNoOtherPlayers() {
        // game only contains the player that is about to invoke offerToOtherPlayers
        Game.setPlayers(Arrays.asList(p1));

        // shouldn't throw any errors
        assertDoesNotThrow(() -> {
            p1.offerComponentToOtherPlayers(c1, new Scanner(""));
        });

        assertNull(c1.getComponentOwner());
    }

    @Test
    void offerComponentToOtherPlayersInvalidComponentAlreadyOwned() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            p3.offerComponentToOtherPlayers(c3, new Scanner("yes yes"));
        });

        String expected = "Component already has an owner";
        assertEquals(expected, illegalArgumentException.getMessage());
    }

    @Test
    void getOwnedComponentsThatCanBeDevelopedValid() {

        // set p3 as system owner for testing purposes
        system1.setSystemOwner(p3);

        // run the method, assign returned HashMap to map variable
        Map<Integer, Component> map = p3.getOwnedComponentsThatCanBeDeveloped();

        // check that map contains the correct number of entries (1)
        assertEquals(1, map.size());

        // check that the map contains the expected values (c3 as it is owned by p3)
        assertTrue(map.containsValue(c3));

    }

    @Test
    void testIncrementCountOfTimesPlayerDeclinedResources() {

        // set initial test count
        p1.setCountOfTimesPlayerDeclinedResources(4);

        // increment the value
        p1.incrementCountOfTimesPlayerDeclinedResources();

        // check that the value is as expected
        assertEquals(5, p1.getCountOfTimesPlayerDeclinedResources());
    }

    @Test
    void transferResourcesValidNegativeResourceAmount() {
        // test that inputting negative resource amount, redirects resources correctly
        int resourcesP1 = p1.getResourceBalance();
        int resourcesP2 = p2.getResourceBalance();
        System.out.println("p2: " + resourcesP2);

        // transfer resources from p2 to p1 using -ve resource amount
        p1.transferResources(p2, -50);

        int expectedP1 = resourcesP1 + 50;
        int expectedP2 = resourcesP2 - 50;

        assertEquals(expectedP1, p1.getResourceBalance());
        assertEquals(expectedP2, p2.getResourceBalance());
    }

    @Test
    void transferResourcesInsufficientResources() {
        p1.setResourceBalance(0);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            // player1 does not have any resources
            p1.transferResources(p2, 500);
        });

        assertTrue(illegalArgumentException.getMessage().contains("does not have enough"));
    }
    
    @Test
    void testGetFullyDevelopedComponents() {

    	List<Component> expectedList = fullyDevelopedComponents;
    	List<Component> emptyList = new ArrayList<Component>();
    	List<Component> actualList = p3.getFullyDevelopedComponents();
    	
    	assertEquals(emptyList, actualList);
    	
    	// develop c3 to max
    	p3.setResourceBalance(10000);
    	c3.developComponent();
    	
    	actualList = p3.getFullyDevelopedComponents();
    	
        assertEquals(expectedList, actualList);
    }


    @Test
    void getSetResourceBalanceValid() {
        p1.setResourceBalance(500);
        assertEquals(500, p1.getResourceBalance());
    }

    @Test
    void setResourceBalanceInvalid() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            p1.setResourceBalance(-1);
        });

        assertTrue(illegalArgumentException.getMessage().equalsIgnoreCase("Resource balance must be zero or more"));
    }

    @Test
    void addRemoveOwnedComponents() {
        p1.addComponent(c1);
        assertTrue(p1.getOwnedComponents().contains(c1));

        p1.removeComponent(c1);
        assertFalse(p1.getOwnedComponents().contains(c1));
    }


    @Test
    void addRemoveOwnedSystems() {
        p1.addSystem(system1);
        assertTrue(p1.getOwnedSystems().contains(system1));

        p1.removeSystem(system1);
        assertFalse(p1.getOwnedSystems().contains(system1));
    }


    @Test
    void getSetCurrentBoardPositionValid() {
        p1.setCurrentBoardPosition(1);

        assertEquals(1, p1.getCurrentBoardPosition());
    }

    @Test
    void getSetCurrentBoardPositionInvalid() {

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            p1.setCurrentBoardPosition(15);
        });

        assertTrue(illegalArgumentException.getMessage().equalsIgnoreCase("invalid board position"));
    }

    @Test
    void getAndIncrementTurnCounter() {
        assertEquals(0, p1.getTurnCounter());
        p1.incrementTurnCounter();
        assertEquals(1, p1.getTurnCounter());
    }

}
