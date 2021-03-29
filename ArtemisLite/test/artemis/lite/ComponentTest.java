package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Test class comprising unit tests to check the functionality of the Component class.
 * @author Kieran Lambe 40040696
 *
 */
class ComponentTest {

	// test players
	Player player1;
	String validPlayerName1;
	int validResourceBalance1, validCurrentBoardPosition1;

	Player player2;
	String validPlayerName2;
	int validResourceBalance2, validCurrentBoardPosition2;

	Player player3;
	String validPlayerName3;
	int validResourceBalance3, validCurrentBoardPosition3;
	
	// arraylist to store players
	ArrayList<Player> playerList;

	// test board
	Board board1;

	// variables for test systems, squares and components
	String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
	String validSquareName1, validSquareName2;
	String validComponentName1, validComponentName2, validComponentName3, validComponentName4, validComponentName5,
			validComponentName6, validComponentName7, validComponentName8, validComponentName9, validComponentName10;
	int validComponentCost, validCostToDevelop, validCostForLanding;

	// Component to be assigned an owner
	Component testComponent;

	// test scanner
	Scanner scannerAffirmative = new Scanner("Yes");
	Scanner scannerNegative = new Scanner("No");

	// test system
	ArtemisSystem testSystem1;

	// test development stage values
	int developmentStageValidLower, developmentStageValidMid, developmentStageValidUpper, developmentStageInvalidLower,
			developmentStageInvalidUpper;

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
		validResourceBalance1 = 1000;
		validCurrentBoardPosition1 = 1;

		validPlayerName2 = "validPlayerName2";
		validResourceBalance2 = 200;
		validCurrentBoardPosition2 = 2;

		validPlayerName3 = "validPlayerName3";
		validResourceBalance3 = 1;
		validCurrentBoardPosition3 = 3;

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

		// set board as the Game var
		Game.setBoard(board1);

		player1 = new Player();
		player1.setPlayerName(validPlayerName1);
		player1.setResourceBalance(validResourceBalance1);

		player2 = new Player();
		player2.setPlayerName(validPlayerName2);
		player2.setResourceBalance(validResourceBalance2);

		player3 = new Player();
		player3.setPlayerName(validPlayerName3);
		player3.setResourceBalance(validResourceBalance3);
		
		playerList = new ArrayList<>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		
		Game.setPlayers(playerList);

		// set up the component
		testComponent = (Component) board1.getSquares()[1];

		// set the owner
		testComponent.setComponentOwner(player1);

		testSystem1 = system1;
		system1.setSystemOwner(player1);

		// set development stage values
		developmentStageValidLower = 0;
		developmentStageValidUpper = 4;
		developmentStageInvalidLower = -1;
		developmentStageInvalidUpper = 5;

	}

	@Test
	void testComponentDefaultConstructor() {
		testComponent = new Component();
		assertNotNull(testComponent);
	}

	@Test
	void testComponentConstructorWithArguments() {

		assertEquals(validComponentName1, testComponent.getSquareName());
		assertEquals(0, testComponent.getDevelopmentStage());
		assertEquals(validComponentCost, testComponent.getComponentCost());
		assertEquals(validCostToDevelop, testComponent.getCostToDevelop());
		assertEquals(validCostForLanding, testComponent.getCostForLanding());
		assertEquals(testSystem1, testComponent.getComponentSystem());
		
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().size()==5);
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().contains(0));
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().contains(1));
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().contains(2));
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().contains(3));
		assertTrue(testComponent.getDevelopmentStageNamesMap().keySet().contains(4));

		assertTrue(testComponent.getDevelopmentStageNamesMap().size()==5);
		assertTrue(testComponent.getDevelopmentStageNamesMap().containsValue(DevelopmentStage.ANALYSING_REQUIREMENTS));
		assertTrue(testComponent.getDevelopmentStageNamesMap().containsValue(DevelopmentStage.DESIGNING));
		assertTrue(testComponent.getDevelopmentStageNamesMap().containsValue(DevelopmentStage.BUILDING));
		assertTrue(testComponent.getDevelopmentStageNamesMap().containsValue(DevelopmentStage.TESTING));
		assertTrue(testComponent.getDevelopmentStageNamesMap().containsValue(DevelopmentStage.MAINTAINING));
		
		assertEquals(0, testComponent.getTotalResourcesDevotedToComponent());
	}

	@Test
	void testDevelopComponentMinorDevelopmentValid() {

		int actualDevelopmentStage;
		int expectedDevelopmentStage = 1;

		int actualResourceBalance;
		int expectedResourceBalance = 950;

		int actualCostForLanding;
		int expectedCostForLanding = 30;

		int actualCostToDevelop;
		int expectedCostToDevelop = 60;

		int actualCostToPurchase;
		int expectedCostToPurchase = 110;

		int actualActionPoints;
		int expectedActionPoints = 1;

		// develop component to stage 1
		testComponent.developComponent();

		// check that component has been developed to stage 1
		actualDevelopmentStage = testComponent.getDevelopmentStage();
		assertEquals(expectedDevelopmentStage, actualDevelopmentStage);

		// check that player has been deducted development cost
		actualResourceBalance = player1.getResourceBalance();
		assertEquals(expectedResourceBalance, actualResourceBalance);

		// check that player has been deducted action point
		actualActionPoints = player1.getActionPoints();
		assertEquals(expectedActionPoints, actualActionPoints);

		// check that cost for landing has been updated
		actualCostForLanding = testComponent.getCostForLanding();
		assertEquals(expectedCostForLanding, actualCostForLanding);

		// check that cost to develop has been updated
		actualCostToDevelop = testComponent.getCostToDevelop();
		assertEquals(expectedCostToDevelop, actualCostToDevelop);

		// check that purchase cost has been updated
		actualCostToPurchase = testComponent.getComponentCost();
		assertEquals(expectedCostToPurchase, actualCostToPurchase);

	}

	@Test
	void testDevelopComponentMajorDevelopmentValid() {

		int actualDevelopmentStage;
		int expectedDevelopmentStage = 4;

		int actualResourceBalance;
		int expectedResourceBalance = 730;

		int actualCostForLanding;
		int expectedCostForLanding = 70;

		int actualCostToDevelop;
		int expectedCostToDevelop = 999999;

		int actualCostToPurchase;
		int expectedCostToPurchase = 150;

		int actualActionPoints;
		int expectedActionPoints = 6;

		// set action points to sufficient level for test purposes
		player1.setActionPoints(10);

		// develop component to stage 1 (minor development)
		testComponent.developComponent();
		// develop component to stage 2 (minor development)
		testComponent.developComponent();
		// develop component to stage 3 (minor development)
		testComponent.developComponent();
		// develop component to stage 4 (major development)
		testComponent.developComponent();

		// check that component has been developed to stage 4
		actualDevelopmentStage = testComponent.getDevelopmentStage();
		assertEquals(expectedDevelopmentStage, actualDevelopmentStage);

		// check that player has been deducted development cost
		actualResourceBalance = player1.getResourceBalance();
		assertEquals(expectedResourceBalance, actualResourceBalance);

		// check that player has been deducted action points
		actualActionPoints = player1.getActionPoints();
		assertEquals(expectedActionPoints, actualActionPoints);

		// check that cost for landing has been updated
		actualCostForLanding = testComponent.getCostForLanding();
		assertEquals(expectedCostForLanding, actualCostForLanding);

		// check that cost to develop has been updated
		actualCostToDevelop = testComponent.getCostToDevelop();
		assertEquals(expectedCostToDevelop, actualCostToDevelop);

		// check that purchase cost has been updated
		actualCostToPurchase = testComponent.getComponentCost();
		assertEquals(expectedCostToPurchase, actualCostToPurchase);

	}

	@Test
	void testDevelopComponentAlreadyFullyDevelopedInvalid() {

		int actualDevelopmentStage;
		int expectedDevelopmentStage = 4;

		int actualResourceBalance;
		int expectedResourceBalance = 1000;

		int actualCostForLanding;
		int expectedCostForLanding = 20;

		int actualCostToDevelop;
		int expectedCostToDevelop = 50;

		int actualCostToPurchase;
		int expectedCostToPurchase = 100;

		int actualActionPoints;
		int expectedActionPoints = 2;

		// set component to maximum development stage
		testComponent.setDevelopmentStage(4);

		// attempt to develop component - this should be unsuccessful as cannot develop
		// beyond maximum stage
		testComponent.developComponent();

		// check development stage has NOT been increased
		actualDevelopmentStage = testComponent.getDevelopmentStage();
		assertEquals(expectedDevelopmentStage, actualDevelopmentStage);

		// check that player has NOT been deducted development cost
		actualResourceBalance = player1.getResourceBalance();
		assertEquals(expectedResourceBalance, actualResourceBalance);

		// check that player has NOT been deducted action points
		actualActionPoints = player1.getActionPoints();
		assertEquals(expectedActionPoints, actualActionPoints);

		// check that cost for landing has NOT been updated
		actualCostForLanding = testComponent.getCostForLanding();
		assertEquals(expectedCostForLanding, actualCostForLanding);

		// check that cost to develop has NOT been updated
		actualCostToDevelop = testComponent.getCostToDevelop();
		assertEquals(expectedCostToDevelop, actualCostToDevelop);

		// check that purchase cost has NOT been updated
		actualCostToPurchase = testComponent.getComponentCost();
		assertEquals(expectedCostToPurchase, actualCostToPurchase);

	}

	@Test
	void testDevelopComponentInsufficientActionPointsInvalid() {

		int actualDevelopmentStage;
		int expectedDevelopmentStage = 0;

		int actualResourceBalance;
		int expectedResourceBalance = 1000;

		int actualCostForLanding;
		int expectedCostForLanding = 20;

		int actualCostToDevelop;
		int expectedCostToDevelop = 50;

		int actualCostToPurchase;
		int expectedCostToPurchase = 100;

		int actualActionPoints;
		int expectedActionPoints = 0;

		// set action points to zero for test purposes
		player1.setActionPoints(0);

		// attempt to develop component - this should be unsuccessful as cannot develop
		// beyond maximum stage
		testComponent.developComponent();

		// check development stage has NOT been increased
		actualDevelopmentStage = testComponent.getDevelopmentStage();
		assertEquals(expectedDevelopmentStage, actualDevelopmentStage);

		// check that player has NOT been deducted development cost
		actualResourceBalance = player1.getResourceBalance();
		assertEquals(expectedResourceBalance, actualResourceBalance);

		// check that player has NOT been deducted action points
		actualActionPoints = player1.getActionPoints();
		assertEquals(expectedActionPoints, actualActionPoints);

		// check that cost for landing has NOT been updated
		actualCostForLanding = testComponent.getCostForLanding();
		assertEquals(expectedCostForLanding, actualCostForLanding);

		// check that cost to develop has NOT been updated
		actualCostToDevelop = testComponent.getCostToDevelop();
		assertEquals(expectedCostToDevelop, actualCostToDevelop);

		// check that purchase cost has NOT been updated
		actualCostToPurchase = testComponent.getComponentCost();
		assertEquals(expectedCostToPurchase, actualCostToPurchase);

	}

	@Test
	void testChargePlayerForLandingAffirmativeTransferResources() {

		int expectedResourceBalanceCurrentPlayer = 180;
		int expectedResourceBalanceComponentOwner = 1020;

		// transfer resources
		testComponent.chargePlayerForLanding(player2, true);

		// check resources have been deducted from current player
		assertEquals(expectedResourceBalanceCurrentPlayer, player2.getResourceBalance());

		// check resources have been credited to component owner
		assertEquals(expectedResourceBalanceComponentOwner, player1.getResourceBalance());

	}

	@Test
	void testChargePlayerForLandingAffirmativeEndGame() {

		// attempt to transfer resources - player 3 has no resources so game should end
		testComponent.chargePlayerForLanding(player3, true);
		
		// check that player's resources have been set to 0
		assertEquals(0, player3.getResourceBalance());

	}

	@Test
	void testChargePlayerForLandingNegativeNoTransferAndGameContinues() {

		int expectedResourceBalanceCurrentPlayer = 200;
		int expectedResourceBalanceComponentOwner = 1000;

		// transfer resources
		testComponent.chargePlayerForLanding(player2, false);

		// check resources have NOT been deducted from current player
		assertEquals(expectedResourceBalanceCurrentPlayer, player2.getResourceBalance());

		// check resources have NOT been credited to component owner
		assertEquals(expectedResourceBalanceComponentOwner, player1.getResourceBalance());

	}

	@Test
	void testCheckFullyDeveloped() {
		// set component to maximum development stage for testing purposes
		testComponent.setDevelopmentStage(4);

		// run the check
		assertTrue(testComponent.checkFullyDeveloped());

		// set component to no longer be fully developed for testing purposes
		testComponent.setDevelopmentStage(3);

		// run the check
		assertFalse(testComponent.checkFullyDeveloped());
	}

	@Test
	void testCheckComponentCanBeDevelopedValid() {

		// component is within development range and its owner also owns the system.
		// Expect method to return true
		assertTrue(testComponent.checkComponentCanBeDeveloped());

	}

	@Test
	void testCheckComponentCanBeDevelopedInvalidComponentOwnerDoesNotOwnSystem() {

		// reset system ownership for testing purposes
		testSystem1.setSystemOwner(null);

		// component is within development however its owner does not own the system.
		// Expect method to return false
		assertFalse(testComponent.checkComponentCanBeDeveloped());

	}

	@Test
	void testCheckComponentCanBeDevelopedInvalidComponentOwnerIsNull() {

		// set component owner to null for testing purposes
		testComponent.setComponentOwner(null);

		// Expect method to return false as component owner is null
		assertFalse(testComponent.checkComponentCanBeDeveloped());

	}

	@Test
	void testCheckComponentCanBeDevelopedInvalidOutsideDevelopmentRangeLower() {

		// set component owner to invalid development stage for testing purposes
		testComponent.setDevelopmentStage(developmentStageInvalidLower);

		// Expect method to return false as component owner is null
		assertFalse(testComponent.checkComponentCanBeDeveloped());

	}

	@Test
	void testCheckComponentCanBeDevelopedInvalidOutsideDevelopmentRangeUpper() {

		// set component owner to invalid development stage for testing purposes
		testComponent.setDevelopmentStage(developmentStageInvalidUpper);

		// Expect method to return false as component owner is null
		assertFalse(testComponent.checkComponentCanBeDeveloped());

	}

	@Test
	void testGetSetDevelopmentStage() {
		
		testComponent.setDevelopmentStage(1);
		assertEquals(1, testComponent.getDevelopmentStage());
		
	}

	@Test
	void testGetSetComponentCost() {
		testComponent.setComponentCost(1);
		assertEquals(1, testComponent.getComponentCost());
	}

	@Test
	void testGetSetCostToDevelop() {
		testComponent.setCostToDevelop(1);
		assertEquals(1, testComponent.getCostToDevelop());
	}

	@Test
	void testGetSetCostForLanding() {
		testComponent.setCostForLanding(1);
		assertEquals(1, testComponent.getCostForLanding());
	}

	@Test
	void testGetSetComponentOwner() {
		testComponent.setComponentOwner(player1);
		assertEquals(player1, testComponent.getComponentOwner());
	}

	@Test
	void testGetSetComponentSystem() {
		testComponent.setComponentSystem(testSystem1);
		assertEquals(testSystem1, testComponent.getComponentSystem());
	}

}
