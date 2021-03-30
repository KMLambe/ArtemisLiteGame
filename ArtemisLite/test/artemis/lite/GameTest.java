package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * This class tests the functionality of the Game class.
 * @author Kieran Lambe 40040696
 *
 */
class GameTest {

	// test players
	Player player1,player2,player3,player4;
	String validPlayerName1,validPlayerName2,validPlayerName3,validPlayerName4;
	int validResourceBalance1, validCurrentBoardPosition1,validCurrentBoardPosition2,validCurrentBoardPosition3,validCurrentBoardPosition4;
	int validCounterOfTimesPlayerDeclinedResources1,validCounterOfTimesPlayerDeclinedResources2,validCounterOfTimesPlayerDeclinedResources3,validCounterOfTimesPlayerDeclinedResources4;

	// test board
	Board board1;

	// test components
	Component testComponent1, testComponent2, testComponent3;

	// test system
	ArtemisSystem system1;

	// variables for test systems, squares and components
	String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
	String validSquareName1, validSquareName2;
	String validComponentName1, validComponentName2, validComponentName3, validComponentName4, validComponentName5,
			validComponentName6, validComponentName7, validComponentName8, validComponentName9, validComponentName10;
	int validComponentCost, validCostToDevelop, validCostForLanding;

	// variables for dice roll
	int validDiceRollLower, validDiceRollMid, validDiceRollUpper, invalidDiceRollLower, invalidDiceRollUpper,
			randomCombinedDiceRoll, minimumCombinedDiceRoll, maximumCombinedDiceRoll;

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

		validPlayerName2 = "validPlayerName2";
		validCurrentBoardPosition2 = 2;
		validPlayerName3 = "validPlayerName3";
		validCurrentBoardPosition2 = 3;
		validPlayerName4 = "validPlayerName4";
		validCurrentBoardPosition2 = 4;
		
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

		// set this as the var in game
		Game.setBoard(board1);

		// dice roll boundaries
		minimumCombinedDiceRoll = 2;
		maximumCombinedDiceRoll = 12;
		validDiceRollLower = 2;
		validDiceRollMid = 8;
		validDiceRollUpper = 12;
		
		// declined resources counters
		validCounterOfTimesPlayerDeclinedResources1 = 1;
		validCounterOfTimesPlayerDeclinedResources2 = 2;
		validCounterOfTimesPlayerDeclinedResources3 = 3;
		validCounterOfTimesPlayerDeclinedResources4 = 4;

		player1 = new Player(validPlayerName1, validResourceBalance1, validCurrentBoardPosition1);
		player2 = new Player(validPlayerName2, validResourceBalance1, validCurrentBoardPosition2);
		player3 = new Player(validPlayerName3, validResourceBalance1, validCurrentBoardPosition3);
		player4 = new Player(validPlayerName4, validResourceBalance1, validCurrentBoardPosition4);

		testComponent1 = (Component) board1.getSquares()[1];
		testComponent2 = (Component) board1.getSquares()[2];
		testComponent3 = (Component) board1.getSquares()[3];

		system1.setSystemOwner(player1);
		
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		Game.setPlayers(players);

		// let game class know it is running tests
		Game.setTestMode(true);
	}

	@Test
	void playersInTheGameValid() {
		// variables for number of players in game
		int validNumberOfPlayersLower = 2;
		int validNumberOfPlayersMid = 3;
		int validNumberOfPlayersUpper = 4;

		// test that valid number of players is accepted
		assertEquals(validNumberOfPlayersLower, Game.playersInTheGame(new Scanner("2")));
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
		// tests that Array return at correct size with names and starting resources and
		// board position
		assertEquals(2, players.size());
		assertEquals("player1", player1.getPlayerName());
		assertEquals(500, player1.getResourceBalance());
		assertEquals(0, player1.getCurrentBoardPosition());
		assertEquals("player2", player2.getPlayerName());
		assertEquals(500, player2.getResourceBalance());
		assertEquals(0, player2.getCurrentBoardPosition());

	}

	@Test
	void createPlayersValidWithDuplicateName() {
		// test showing duplicate name not accepted for second player and having to
		// enter a third input to set player2 name
		ArrayList<Player> players = Game.createPlayers(new Scanner("2 name1 name1 name2"));
		Player player1 = players.get(0);
		Player player2 = players.get(1);
		// tests
		assertEquals(2, players.size());
		assertEquals("name1", player1.getPlayerName());
		assertEquals("name2", player2.getPlayerName());
	}

	@Test
	void testGeneratePlayerOrderValid() {
		// test array for create players
		ArrayList<Player> playersTest = new ArrayList<Player>();
		int STARTING_RESOURCES = 500;
		int STARTING_POSITION = 0;
		String validPlayer1, validPlayer2;

		// player info to pass into array for player test
		validPlayer1 = "name1";
		validPlayer2 = "name2";

		playersTest.add(new Player(validPlayer1, STARTING_RESOURCES, STARTING_POSITION));
		playersTest.add(new Player(validPlayer2, STARTING_RESOURCES, STARTING_POSITION));

		// run method
		Game.generatePlayerOrder();
		// test that Array List still contains same amount of items
		assertEquals(2, playersTest.size());
	}

	@Test
	void testGetNextPlayerValid() {
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		Game.setPlayers(players);
		Player currentPlayer;
		
		// set first player in arrayList to last player in arraylist -> so that
		// nextPlayer will be the first player
		currentPlayer = players.get(players.size() - 1);

		// test that the method will get the first player
		currentPlayer = Game.getNextPlayer(currentPlayer);
		assertEquals("validPlayerName1", currentPlayer.getPlayerName());
		// test that the method will get the second player
		currentPlayer = Game.getNextPlayer(currentPlayer);
		assertEquals("validPlayerName2", currentPlayer.getPlayerName());
		// test that the method will revert back to the first player after it reaches
		// the end of the list
		currentPlayer = Game.getNextPlayer(currentPlayer);
		assertEquals("validPlayerName1", currentPlayer.getPlayerName());
	}

	@Test
	void testRollDice() {

		randomCombinedDiceRoll = Game.rollDice();

		assertTrue(
				randomCombinedDiceRoll >= minimumCombinedDiceRoll && randomCombinedDiceRoll <= maximumCombinedDiceRoll);
	}

	@Test
	void testUpdatePlayerPositionValidDiceRoll() {

		int expectedPosition, actualPosition, expectedResourceBalance, actualResourceBalance;

		// test updating player position without passing recruitment square

		// update player position
		Game.updatePlayerPosition(player1, validDiceRollMid);

		// player started at position 1, now expected to be at position 9 following
		// update
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
		Game.updatePlayerPosition(player1, validDiceRollLower);

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
			Game.updatePlayerPosition(player1, invalidDiceRollLower);
		});

		// check correct message is shown
		assertTrue(exception.getMessage().contains("must be between"));

		// expect an exception to be thrown when upper invalid dice roll is passed
		exception = assertThrows(IllegalArgumentException.class, () -> {
			Game.updatePlayerPosition(player1, invalidDiceRollUpper);
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
			Game.updatePlayerPosition(player1, validDiceRollMid);
		});

		// check correct message is shown
		assertTrue(exception.getMessage().equalsIgnoreCase("Current player cannot be null"));

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
	void testGetSetPlayers() {
		
		ArrayList<Player> testPlayerList = new ArrayList<>();
		testPlayerList.add(player1);
		testPlayerList.add(player2);
		
		Game.setPlayers(testPlayerList);
		
		assertEquals(testPlayerList, Game.getPlayers());
		
	}


	@Test
	void testDisplayComponentIfPurchasable() throws InterruptedException {

		Square[] squares = board1.getSquares();

		player1.setCurrentBoardPosition(1);
		Component testComponent1 = (Component) squares[1];
		Component testComponent2 = (Component) squares[2];
		Component testComponent3 = (Component) squares[3];
		Component testComponent4 = (Component) squares[4];
		Player expectedPlayerName = player1;
		Player expectedPlayerName2 = player2;
		player1.setResourceBalance(1000);
		player2.setResourceBalance(1000);
		
		// testing that player1 purchases component and is updated as component owner
		Game.displayComponentIfPurchasable(player1, testComponent1, new Scanner("yes"));
		// expected is player 1 owns component
		assertEquals(expectedPlayerName, testComponent1.getComponentOwner());
		
		player1.setCurrentBoardPosition(2);
		
		// testing that player1 doesn't purchase and offers to other player
		Game.displayComponentIfPurchasable(player1, testComponent2, new Scanner("no yes"));
		// expected is player 2 to own component
		assertEquals(expectedPlayerName2, testComponent2.getComponentOwner());
		
		player1.setCurrentBoardPosition(3);
		
		// testing invalid input then opting to purchase
		Game.displayComponentIfPurchasable(player1, testComponent3, new Scanner("invalid yes"));
		assertEquals(expectedPlayerName, testComponent3.getComponentOwner());
		
		player1.setCurrentBoardPosition(4);
		
		// testing invalid input then opting to offer to other player
		Game.displayComponentIfPurchasable(player1, testComponent4, new Scanner("invalid no yes"));
		assertEquals(expectedPlayerName2, testComponent4.getComponentOwner());
	}

	// tradeable component testing
	@Test
	void testTradeableComponentsValid() {
		Player player2 = new Player("Player2", 500, 0);
		// manually override default action points to allow more components to be
		// purchase
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
		Map<Integer, Component> player1ComponentsAvailable = Game.getComponentsForTrading(player1);

		// player1 should see player2's components
		assertEquals(3, player1ComponentsAvailable.size());

		assertTrue(player1ComponentsAvailable.containsValue(squares[1]));
		assertTrue(player1ComponentsAvailable.containsValue(squares[2]));
		assertTrue(player1ComponentsAvailable.containsValue(squares[4]));
		assertFalse(player1ComponentsAvailable.containsValue(squares[7]));

		// player2 should see player1's components
		Map<Integer, Component> player2ComponentsAvailable = Game.getComponentsForTrading(player2);

		assertEquals(1, player2ComponentsAvailable.size());
		assertTrue(player2ComponentsAvailable.containsValue(squares[7]));
	}

	@Test
	void testTradeMenuUserInputBackToMainMenu() {
		Player player2 = new Player("Player2", 500, 0);
		// manually override default action points to allow more components to be
		// purchase
		player2.setActionPoints(6);
		Component component;

		// player2 purchases number of components
		Square[] squares = board1.getSquares();

		player2.purchaseComponent(squares[1]);
		player2.purchaseComponent(squares[2]);
		player2.purchaseComponent(squares[4]);

		player1.purchaseComponent(squares[7]);
		player1.setResourceBalance(500); // manually override so that components appear

		Map<Integer, Component> player1ComponentsAvailable = Game.getComponentsForTrading(player1);

		// simulate user inputting wrong data, followed by 'end', which should return
		// null (indicating no component selected)
		Component componentForTrade = Game.getPlayerComponentSelection(new Scanner("na end"),
				player1ComponentsAvailable);

		assertNull(componentForTrade);
	}


	@Test
	void testGetHighestRollerValidMultiplePlayers() {
		Player p1 = new Player("Player1", validResourceBalance1, validCurrentBoardPosition1);
		Player p2 = new Player("Player2", validResourceBalance1, validCurrentBoardPosition1);
		Player p3 = new Player("Player3", validResourceBalance1, validCurrentBoardPosition1);
		Player p4 = new Player("Player4", validResourceBalance1, validCurrentBoardPosition1);

		List<Player> playerList = new ArrayList<Player>(Arrays.asList(p1, p2, p3, p4));

		Player winner = Game.getHighestRoll(playerList);

		assertTrue(playerList.contains(winner));
		assertNotNull(winner);
	}

	@Test
	void testGetHighestRollerValidOnePlayer() {
		Player p1 = new Player("Player1", validResourceBalance1, validCurrentBoardPosition1);

		List<Player> playerList = new ArrayList<>(Arrays.asList(p1));

		Player winner = Game.getHighestRoll(playerList);

		assertEquals(p1, winner);
	}

	@Test
	void testGetHighestRollerInvalidNoPlayers() {
		List<Player> playerList = new ArrayList<>();

		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
			Game.getHighestRoll(playerList);
		});

		assertTrue(illegalArgumentException.getMessage().equalsIgnoreCase("empty list of players"));
	}


	
	@Test
	void testDisplayComponentsPlayerCanDevelop() {

		// TEMPORARY TEST CREATED WHEN DESIGNING FUNCTIONALITY - KL
		
		player1.setActionPoints(10);

		player1.purchaseComponent(testComponent1);

		System.out.println("Add 1000 experts");
		player1.setResourceBalance(1000);

		player1.purchaseComponent(testComponent2);

		player1.purchaseComponent(testComponent3);
		
		Game.displayDevelopComponentMenu(player1, new Scanner("2"));
	}
	
	@Test
	void testSortPlayersByCounterOfTimesDeclinedResourcesValid() {
		
		// update values of players' counters
		player1.setCountOfTimesPlayerDeclinedResources(validCounterOfTimesPlayerDeclinedResources4); // most times declined resources
		player2.setCountOfTimesPlayerDeclinedResources(validCounterOfTimesPlayerDeclinedResources2);
		player3.setCountOfTimesPlayerDeclinedResources(validCounterOfTimesPlayerDeclinedResources3);
		player4.setCountOfTimesPlayerDeclinedResources(validCounterOfTimesPlayerDeclinedResources1); // least times declined resources
		
		// group players in list for testing purposes
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		
		// set players in Game to newly-created list
		Game.setPlayers(playerList);
		
		// perform the sort
		List<Player> sortedList = Game.sortPlayersByCounterOfTimesDeclinedResources();
		
		// check that array list contains expected number of objects
		assertEquals(4, sortedList.size());
		
		// check that the order is as expected
		assertEquals(player1,sortedList.get(0));
		assertEquals(player3,sortedList.get(1));
		assertEquals(player2,sortedList.get(2));
		assertEquals(player4,sortedList.get(3));
		
		// checking display method
		Game.displayTimesDeclinedResourcesStats(sortedList);
	}
	
	@Test
	void testSortPlayersByCounterOfTimesDeclinedResourcesInvalidNullList() {
		
		// create null list
		ArrayList<Player> emptyPlayerList = null;
		
		Game.setPlayers(emptyPlayerList);
		
		// perform the sort - expected exception
		Exception exception = assertThrows(NullPointerException.class, ()->{
		Game.sortPlayersByCounterOfTimesDeclinedResources();
		});
		
		// check that message is as expected
		assertTrue(exception.getMessage().contains("cannot be null"));
		
	}
	
	@Test
	void testConfirmPlayerWantsToLeaveGame() throws InterruptedException {

		Player currentPlayer = player1;
		boolean playerWantsToLeave = true;
		boolean playerWontLeave = false;

		// check game will end when user inputs yes
		assertEquals(playerWantsToLeave, Game.confirmPlayerWantsToLeave(currentPlayer, new Scanner("yes")));
		// check game will not end when user inputs no
		assertEquals(playerWontLeave, Game.confirmPlayerWantsToLeave(currentPlayer, new Scanner("no")));
		// check invalid input will ask again to leave game
		assertEquals(playerWantsToLeave, Game.confirmPlayerWantsToLeave(currentPlayer, new Scanner("invalid yes")));
	}
	
	@Test
	void testcheckAllSystemsFullyDevelopedValid() {
		
		Player currentPlayer;
		// Set expected outcomes
		boolean actualResult;
		boolean expectedOutcome1=false;
		boolean expectedOutcome2=true;
		Square[] squares = board1.getSquares();
		//set resources and balances higher so this doesn't stop test
		player1.setResourceBalance(5000);
		player2.setResourceBalance(5000);
		player1.setActionPoints(10);
		player2.setActionPoints(10);
		
		// purchase all Components
		currentPlayer=player1;
		
		player1.purchaseComponent(squares[1]);
		player1.purchaseComponent(squares[2]);
		player1.purchaseComponent(squares[3]);
		player1.purchaseComponent(squares[7]);
		player1.purchaseComponent(squares[8]);
		player1.purchaseComponent(squares[9]);
	
		currentPlayer=player2;
		player2.purchaseComponent(squares[4]);
		player2.purchaseComponent(squares[5]);
		player2.purchaseComponent(squares[10]);
		player2.purchaseComponent(squares[11]);
		
		// test that all systems developed is false
		actualResult=Game.checkAllSystemsFullyDeveloped();
		assertEquals(expectedOutcome1, actualResult);
		
		// fully develop all components
		Component testComponent1 = (Component) squares[1];
		testComponent1.setDevelopmentStage(4);
		Component testComponent2 = (Component) squares[2];
		testComponent2.setDevelopmentStage(4);
		Component testComponent3 = (Component) squares[3];
		testComponent3.setDevelopmentStage(4);
		Component testComponent4 = (Component) squares[4];
		testComponent4.setDevelopmentStage(4);
		Component testComponent5 = (Component) squares[5];
		testComponent5.setDevelopmentStage(4);
		Component testComponent7 = (Component) squares[7];
		testComponent7.setDevelopmentStage(4);
		Component testComponent8 = (Component) squares[8];
		testComponent8.setDevelopmentStage(4);
		Component testComponent9 = (Component) squares[9];
		testComponent9.setDevelopmentStage(4);
		Component testComponent10 = (Component) squares[10];
		testComponent10.setDevelopmentStage(4);
		Component testComponent11 = (Component) squares[11];
		testComponent11.setDevelopmentStage(4);
		
		//test that all components have been fully developed
		actualResult=Game.checkAllSystemsFullyDeveloped();
		assertEquals(expectedOutcome2, actualResult);
	}


	@Test
	void getSetTestMode() {
		Game.setTestMode(false);
		assertFalse(Game.isTestMode());

		Game.setTestMode(true);
		assertTrue(Game.isTestMode());
	}
}
