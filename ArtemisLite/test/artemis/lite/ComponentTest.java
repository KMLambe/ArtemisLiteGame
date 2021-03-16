package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentTest {

	// test players
	Player player1;
	String validPlayerName1;
	int validResourceBalance1, validCurrentBoardPosition1;

	Player player2;
	String validPlayerName2;
	int validResourceBalance2, validCurrentBoardPosition2;

	// test board
	Board board1;

	// variables for test systems, squares and components
	String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
	String validSquareName1, validSquareName2;
	String validComponentName1, validComponentName2, validComponentName3, validComponentName4, validComponentName5,
			validComponentName6, validComponentName7, validComponentName8, validComponentName9, validComponentName10;
	int validComponentCost, validCostToDevelop, validCostForLanding;

	// test scanner
	Scanner scannerAffirmative = new Scanner("Yes");
	Scanner scannerNegative = new Scanner("No");

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
		validResourceBalance2 = 200;
		validCurrentBoardPosition2 = 2;

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

		player1 = new Player();
		player1.setPlayerName(validPlayerName1);
		player1.setResourceBalance(validResourceBalance1);
		
		player2 = new Player();
		player2.setPlayerName(validPlayerName2);
		player2.setResourceBalance(validResourceBalance2);
	}

	@Test
	void testComponent() {
		fail("Not yet implemented");
	}

	@Test
	void testComponentConstructorWithArguments() {
		fail("Not yet implemented");
	}

	@Test
	void testDevelopComponent() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckOwnerWantsResourcesAffirmative() { // Build in testing for endGame()

		boolean actualResult;
		boolean expectedResult = true;

		// set up the component for the test
		Component testComponent = (Component) board1.getSquares()[1];

		// set the owner
		testComponent.setComponentOwner(player1);
		testComponent.displayAllDetails();

		// run the check
		actualResult = testComponent.checkOwnerWantsResources(player2, scannerAffirmative);

		// compare expected and actual outcomes
		assertEquals(expectedResult, actualResult);

	}
	
	@Test
	void testCheckOwnerWantsResourcesNegative() { // Build in testing for endGame()

		boolean actualResult;
		boolean expectedResult = false;

		// set up the component for the test
		Component testComponent = (Component) board1.getSquares()[1];

		// set the owner
		testComponent.setComponentOwner(player1);
		testComponent.displayAllDetails();

		// run the check
		actualResult = testComponent.checkOwnerWantsResources(player2, scannerNegative);

		// compare expected and actual outcomes
		assertEquals(expectedResult, actualResult);

	}

	@Test
	void testChargePlayerForLanding() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckFullyDeveloped() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDevelopmentStage() {
		fail("Not yet implemented");
	}

	@Test
	void testSetDevelopmentStage() {
		fail("Not yet implemented");
	}

	@Test
	void testGetComponentCost() {
		fail("Not yet implemented");
	}

	@Test
	void testSetComponentCost() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCostToDevelop() {
		fail("Not yet implemented");
	}

	@Test
	void testSetCostToDevelop() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCostForLanding() {
		fail("Not yet implemented");
	}

	@Test
	void testSetCostForLanding() {
		fail("Not yet implemented");
	}

	@Test
	void testGetComponentOwner() {
		fail("Not yet implemented");
	}

	@Test
	void testSetComponentOwner() {
		fail("Not yet implemented");
	}

	@Test
	void testGetComponentSystem() {
		fail("Not yet implemented");
	}

	@Test
	void testSetComponentSystem() {
		fail("Not yet implemented");
	}

}