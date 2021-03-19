package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

class GameTest {

    // test player
    Player player1;
    String validPlayerName1;
    int validResourceBalance1, validCurrentBoardPosition1;

    // test board
    Board board1;

    // variables for test systems, squares and components
    String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
    String validSquareName1, validSquareName2;
    String validComponentName1, validComponentName2, validComponentName3, validComponentName4, validComponentName5, validComponentName6, validComponentName7, validComponentName8, validComponentName9, validComponentName10;
    int validComponentCost, validCostToDevelop, validCostForLanding;

    // variables for dice roll
    int validDiceRollLower, validDiceRollMid, validDiceRollUpper, invalidDiceRollLower, invalidDiceRollUpper, randomCombinedDiceRoll, minimumCombinedDiceRoll, maximumCombinedDiceRoll;


    @BeforeEach
    void setUp() throws Exception {

        board1 = new Board();

        validSystemName1 = "validSystemName1";
        validSystemName2 = "validSystemName2";
        validSystemName3 = "validSystemName3";
        validSystemName4 = "validSystemName4";

        validSquareName1 = "validSquareName1";
        validSquareName2 = "validSquareName1";

        validComponentName1 = "validComponentName1";
        validComponentName2 = "validComponentName2";
        validComponentName3 = "validComponentName3";
        validComponentName4 = "validComponentName4";
        validComponentName5 = "validComponentName5";
        validComponentName6 = "validComponentName6";
        validComponentName7 = "validComponentName7";
        validComponentName8 = "validComponentName8";
        validComponentName9 = "validComponentName9";
        validComponentName10 = "validComponentName10";

        validComponentCost = 100;
        validCostToDevelop = 50;
        validCostForLanding = 20;

        validPlayerName1 = "validPlayerName1";
        validResourceBalance1 = 100;
        validCurrentBoardPosition1 = 1;

        // create test systems
        ArtemisSystem system1 = board1.createSystem(validSystemName1);
        ArtemisSystem system2 = board1.createSystem(validSystemName2);
        ArtemisSystem system3 = board1.createSystem(validSystemName3);
        ArtemisSystem system4 = board1.createSystem(validSystemName4);


        // create squares and components. Add them to board's squares array
        board1.createSquare(validSquareName1);

        board1.createSquare(validComponentName1, validComponentCost, validCostToDevelop, validCostForLanding, system1);
        board1.createSquare(validComponentName2, validComponentCost, validCostToDevelop, validCostForLanding, system1);
        board1.createSquare(validComponentName3, validComponentCost, validCostToDevelop, validCostForLanding, system1);

        board1.createSquare(validComponentName4, validComponentCost, validCostToDevelop, validCostForLanding, system2);
        board1.createSquare(validComponentName5, validComponentCost, validCostToDevelop, validCostForLanding, system2);

        board1.createSquare(validSquareName2);

        board1.createSquare(validComponentName6, validComponentCost, validCostToDevelop, validCostForLanding, system3);
        board1.createSquare(validComponentName7, validComponentCost, validCostToDevelop, validCostForLanding, system3);
        board1.createSquare(validComponentName8, validComponentCost, validCostToDevelop, validCostForLanding, system3);

        board1.createSquare(validComponentName9, validComponentCost, validCostToDevelop, validCostForLanding, system4);
        board1.createSquare(validComponentName10, validComponentCost, validCostToDevelop, validCostForLanding, system4);


        // dice roll boundaries
        minimumCombinedDiceRoll = 2;
        maximumCombinedDiceRoll = 12;
        validDiceRollLower = 2;
        validDiceRollMid = 8;
        validDiceRollUpper = 12;

 

        player1 = new Player(validPlayerName1, validResourceBalance1, validCurrentBoardPosition1);
    }

    @Test
    void testMain() {
        fail("Not yet implemented");
    }

    @Test
    void testStartGame() {
        fail("Not yet implemented");
    }

    @Test
	void playersInTheGameValid() {
		// variables for number of players in game
		int validNumberOfPlayersLower = 2;
		int validNumberOfPlayersMid = 3;
		int validNumberOfPlayersUpper = 4;
				
	// test that valid number of players is accepted
	assertEquals(validNumberOfPlayersLower,Game.playersInTheGame(new Scanner("2")) );
	assertEquals(validNumberOfPlayersMid, Game.playersInTheGame(new Scanner("3")));
	assertEquals(validNumberOfPlayersUpper, Game.playersInTheGame(new Scanner("4")));
	
	}
	
	@Test
	void playersInTheGameInvalid() {
		
		// test that invalid number of players will not be accepted
		int invalidNumberOfPlayers = -1;
		assertEquals(invalidNumberOfPlayers, Game.playersInTheGame(new Scanner("1")));
		assertEquals(invalidNumberOfPlayers, Game.playersInTheGame(new Scanner("5")));				
	}
	
	@Test
	void createPlayersValid() {
	    ArrayList<Player> players = Game.createPlayers(new Scanner("2 player1 player2"));
	    Player player1 = players.get(0);
	    Player player2 = players.get(1);
	    // tests that Array return at correct size with names and starting resources and board position
	    assertEquals(2, players.size());
	    assertTrue(player1.getPlayerName().equals("player1"));
	    assertEquals(500, player1.getResourceBalance());
	    assertEquals(0, player1.getCurrentBoardPosition());
	    assertTrue(player2.getPlayerName().equals("player2"));
	    assertEquals(500, player2.getResourceBalance());
	    assertEquals(0, player2.getCurrentBoardPosition());
	    
	}

	@Test
	void createPlayersValidWithDuplicateName() {
	    // test showing duplicate name not accepted for second player and having to enter a third input to set player2 name
	    ArrayList<Player> players = Game.createPlayers(new Scanner("2 name1 name1 name2"));
	    Player player1 = players.get(0);
	    Player player2 = players.get(1);
	    // tests
	    assertEquals(2, players.size());
	    assertTrue(player1.getPlayerName().equals("name1"));
	    assertTrue(player2.getPlayerName().equals("name2"));
	}
	
	@Test
	void testGeneratePlayerOrderValid() {
		//test array for create players
		ArrayList<Player> playerstest = new ArrayList<Player>();
		int STARTING_RESOURCES = 500;
		int STARTING_POSITION =0;
		String validPlayer1, validPlayer2;
		
		// player info to pass into array for player test
				validPlayer1 = "name1";
				validPlayer2 = "name2";
				
				playerstest.add(new Player(validPlayer1, STARTING_RESOURCES, STARTING_POSITION));
				playerstest.add(new Player(validPlayer2, STARTING_RESOURCES, STARTING_POSITION));
				
				// run method
				Game.generatePlayerOrder(playerstest);
				// test that Array List still contains same amount of items
				assertEquals(2, playerstest.size());
	}
	
	@Test
	void testGetNextPlayerValid() {
		
		ArrayList<Player> playerstest = new ArrayList<Player>();
		int STARTING_RESOURCES = 500;
		int STARTING_POSITION =0;
		String validPlayer1, validPlayer2, validPlayer3, validPlayer4;
		Player currentPlayer;
		
		// player info to pass into array for player test
				validPlayer1 = "name1";
				validPlayer2 = "name2";
				validPlayer3 = "name3";
				validPlayer4 = "name4";
				
				playerstest.add(new Player(validPlayer1, STARTING_RESOURCES, STARTING_POSITION));
				playerstest.add(new Player(validPlayer2, STARTING_RESOURCES, STARTING_POSITION));
				playerstest.add(new Player(validPlayer3, STARTING_RESOURCES, STARTING_POSITION));
				playerstest.add(new Player(validPlayer4, STARTING_RESOURCES, STARTING_POSITION));
				
	
				// test that the method will get the first player
				currentPlayer=Game.getNextPlayer(playerstest);		
				assertEquals("name1", currentPlayer.getPlayerName());
				// test that the method will get the second player
				currentPlayer=Game.getNextPlayer(playerstest);		
				assertEquals("name2", currentPlayer.getPlayerName());
				// test that the method will get the third player
				currentPlayer=Game.getNextPlayer(playerstest);		
				assertEquals("name3", currentPlayer.getPlayerName());
				// test that the method will get the fourth player
				currentPlayer=Game.getNextPlayer(playerstest);		
				assertEquals("name4", currentPlayer.getPlayerName());
				// test that the method will revert back to the first player after it reaches the end of the list
				currentPlayer=Game.getNextPlayer(playerstest);		
				assertEquals("name1", currentPlayer.getPlayerName());
	}
	
    

    @Test
    void testRollDice() {

        randomCombinedDiceRoll = Game.rollDice();

        assertTrue(randomCombinedDiceRoll >= minimumCombinedDiceRoll &&
                randomCombinedDiceRoll <= maximumCombinedDiceRoll);
    }

    @Test
    void testUpdatePlayerPositionValidDiceRoll() {

        int expectedPosition, actualPosition, expectedResourceBalance, actualResourceBalance;

        // test updating player position without passing recruitment square

        // update player position
        Game.updatePlayerPosition(player1, board1, validDiceRollMid);

        // player started at position 1, now expected to be at position 9 following update
        expectedPosition = 9;
        actualPosition = player1.getCurrentBoardPosition();
        assertEquals(actualPosition, expectedPosition);

        // check that player has not been allocated resources
        expectedResourceBalance = validResourceBalance1;
        actualResourceBalance = player1.getResourceBalance();

        assertEquals(expectedResourceBalance, actualResourceBalance);

        // test updating player position including passing recruitment square

        // reset player position to 11
        player1.setCurrentBoardPosition(11);

        // update player position
        Game.updatePlayerPosition(player1, board1, validDiceRollLower);

        // player started at position 12, now expected to be at position 1
        expectedPosition = 1;
        actualPosition = player1.getCurrentBoardPosition();
        assertEquals(actualPosition, expectedPosition);

        // check that player has been credited with resources for passing recruitment
        expectedResourceBalance = 200;
        actualResourceBalance = player1.getResourceBalance();
        assertEquals(expectedResourceBalance, actualResourceBalance);

    }

    @Test
    void testUpdatePlayerPositionInvalidDiceRoll() {

        // expect an exception to be thrown when lower invalid dice roll is passed
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Game.updatePlayerPosition(player1, board1, invalidDiceRollLower);
        });

        // check correct message is shown
        assertTrue(exception.getMessage().contains("must be between"));

        // expect an exception to be thrown when upper invalid dice roll is passed
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Game.updatePlayerPosition(player1, board1, invalidDiceRollUpper);
        });

        // check correct message is shown
        assertTrue(exception.getMessage().contains("must be between"));

    }

    @Test
    void testUpdatePlayerPositionInvalidPlayer() {

        // set player to null
        player1 = null;

        // expect an exception to be thrown when lower invalid player is passed
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Game.updatePlayerPosition(player1, board1, validDiceRollMid);
        });

        // check correct message is shown
        assertTrue(exception.getMessage().equalsIgnoreCase("Current player cannot be null"));

    }

    @Test
    void testUpdatePlayerPositionInvalidBoard() {

        // set player to null
        board1 = null;

        // expect an exception to be thrown when lower invalid player is passed
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Game.updatePlayerPosition(player1, board1, validDiceRollMid);
        });

        // check correct message is shown
        assertTrue(exception.getMessage().contains("Board cannot"));

    }

    @Test
    void testDisplayMenu() {
        fail("Not yet implemented");
    }

    @Test
    void testPlayTurn() {
        fail("Not yet implemented");
    }


    @Test
    void testAllocateResourcesValidPlayer() {

        int expectedResourceBalance, actualResourceBalance;

        expectedResourceBalance = 200;

        // allocate resources
        Game.allocateResources(player1);

        actualResourceBalance = player1.getResourceBalance();

        // check updated resource balance
        assertEquals(expectedResourceBalance, actualResourceBalance);

    }


    @Test
    void testAllocateResourcesInvalidPlayer() {

        // set player1 to null
        player1 = null;

        // expect an exception to be thrown when resources are allocated
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Game.allocateResources(player1);
        });

        // check message is as expected
        assertTrue(exception.getMessage().equalsIgnoreCase("Current player cannot be null"));

    }

    @Test
    void testGetPlayers() {
        fail("Not yet implemented");
    }

    @Test
    void testSetPlayers() {
        fail("Not yet implemented");
    }

    @Test
    void testEndGame() {
        fail("Not yet implemented");
    }
    
    @Test
    void testDisplayPurchasableComponentValid() {
    	String validInputToPurchase = "Yes";
    	String validInputNotToPurchase = "No";
    	
    	Player player1 = new Player("Player1", 400, 2);
		Component component = (Component) board1.getSquares()[2];
		
		assertEquals(validInputToPurchase, new Scanner("Yes"));
		assertEquals(validInputNotToPurchase, new Scanner("No"));
		
    }
	
	@Test
	void testDisplayPurchasableComponentInvalid() {
		
		// test that invalid input will not be accepted
		String invalidInputToPurchase = "";
		
		assertEquals(invalidInputToPurchase, new Scanner("Yes"));
	}


    // tradeable component testing
    @Test
    void testTradeableComponentsValid() {
        Player player2 = new Player("Player2", 500, 0);
        //manually override default action points to allow more components to be purchase
        player2.setActionPoints(6);
        Component component;

        // player2 purchases number of components
        Square[] squares = board1.getSquares();

        player2.purchaseComponent(squares[1]);
        player2.purchaseComponent(squares[2]);
        player2.purchaseComponent(squares[4]);

        player1.purchaseComponent(squares[7]);
        player1.setResourceBalance(500); // manually override so that components appear

        // get list of tradeable components returned for the player
        Map<Integer, Component> player1ComponentsAvailable = Game.getComponentsForTrading(player1, board1);


        // player1 should see player2's components
        assertEquals(3, player1ComponentsAvailable.size());

        assertTrue(player1ComponentsAvailable.containsValue(squares[1]));
        assertTrue(player1ComponentsAvailable.containsValue(squares[2]));
        assertTrue(player1ComponentsAvailable.containsValue(squares[4]));
        assertFalse(player1ComponentsAvailable.containsValue(squares[7]));

        // player2 should see player1's components
        Map<Integer, Component> player2ComponentsAvailable = Game.getComponentsForTrading(player2, board1);

        assertEquals(1, player2ComponentsAvailable.size());
        assertTrue(player2ComponentsAvailable.containsValue(squares[7]));
    }


    @Test
    void testTradeMenuUserInputBackToMainMenu() {
        Player player2 = new Player("Player2", 500, 0);
        //manually override default action points to allow more components to be purchase
        player2.setActionPoints(6);
        Component component;

        // player2 purchases number of components
        Square[] squares = board1.getSquares();

        player2.purchaseComponent(squares[1]);
        player2.purchaseComponent(squares[2]);
        player2.purchaseComponent(squares[4]);

        player1.purchaseComponent(squares[7]);
        player1.setResourceBalance(500); // manually override so that components appear

        Map<Integer, Component> player1ComponentsAvailable = Game.getComponentsForTrading(player1, board1);

        // simulate user inputting wrong data, followed by 'end', which should return null (indicating no component selected)
        Component componentForTrade = Game.getPlayerComponentSelection(new Scanner("na end"), player1ComponentsAvailable);

        assertTrue(componentForTrade == null);
    }

    @Test
    void testTradeMenuValidSelection() {

    }

    @Test
    void testTradeMenuToPlayerTradeIntegration() {
        fail("TBC");
    }

}