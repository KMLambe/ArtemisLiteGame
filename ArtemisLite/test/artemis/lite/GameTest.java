package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

	// test player
	Player player1;
	String validPlayerName1;
	int validResourceBalance1, validCurrentBoardPosition1;

	// test board
	Board board1;

	// variables for test systems, squares and components
	String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
	String validSquareName1,validSquareName2;
	String validComponentName1, validComponentName2, validComponentName3, validComponentName4,validComponentName5,validComponentName6,validComponentName7,validComponentName8,validComponentName9,validComponentName10;
	int validComponentCost, validCostToDevelop, validCostForLanding;

	// variables for dice roll
	int validDiceRollLower, validDiceRollMid, validDiceRollUpper, invalidDiceRollLower, invalidDiceRollUpper, randomCombinedDiceRoll, minimumCombinedDiceRoll, maximumCombinedDiceRoll;

	// variables for number of players in game
		int validNumberOfPlayersUpper, validNumberOfPlayersMid, validNumberOfPlayersLower, invalidNumberOfPlayersUpper, numberOfPlayers;
		int invalidNumberOfPlayersLower;  
	
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
		
		// number of players in game boundaries
				validNumberOfPlayersLower = 2;
				validNumberOfPlayersMid = 3;
				validNumberOfPlayersUpper = 4;
				invalidNumberOfPlayersLower = 1;
				invalidNumberOfPlayersUpper = 5;

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
	
	assertEquals(validNumberOfPlayersLower,Game.playersInTheGame(new Scanner("2")) );
	assertEquals(validNumberOfPlayersMid, Game.playersInTheGame(new Scanner("3")));
	assertEquals(validNumberOfPlayersUpper, Game.playersInTheGame(new Scanner("4")));
	}
	
	@Test
	void testCreatePlayers() {
		fail("Not yet implemented");
	}

	@Test
	void testGeneratePlayerOrder() {
		fail("Not yet implemented");
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
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{
			Game.updatePlayerPosition(player1, board1, invalidDiceRollLower);
		});
		
		// check correct message is shown
		assertTrue(exception.getMessage().contains("must be between"));
		
		// expect an exception to be thrown when upper invalid dice roll is passed
		exception = assertThrows(IllegalArgumentException.class, ()->{
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
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{
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
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{
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
	void testGetNextPlayer() {
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
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{
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

}
