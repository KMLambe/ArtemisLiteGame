package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArtemisSystemTest {

	// test board
	Board board1;
	
	// test players
	Player player1, player2;
	
	// variables for player
	String validPlayerName1, validPlayerName2;
	int validResourceBalance1,validResourceBalance2, validCurrentBoardPosition1,validCurrentBoardPosition2;

	// variables for test systems, squares and components
	String validSystemName1, validSystemName2, validSystemName3, validSystemName4;
	String validSquareName1, validSquareName2;
	String validComponentName1, validComponentName2, validComponentName3, validComponentName4, validComponentName5,
			validComponentName6, validComponentName7, validComponentName8, validComponentName9, validComponentName10;
	int validComponentCost, validCostToDevelop, validCostForLanding;

	// Components
	Component testComponent1, testComponent2, testComponent3;

	// test system
	ArtemisSystem testSystem1;

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
		
		// set up the components
		testComponent1 = (Component) board1.getSquares()[1];
		testComponent2 = (Component) board1.getSquares()[2];
		testComponent3 = (Component) board1.getSquares()[3];

		validPlayerName1 = "validPlayerName1";
		validResourceBalance1 = 1000;
		validCurrentBoardPosition1 = 1;
		
		validPlayerName2 = "validPlayerName2";
		validResourceBalance2 = 2000;
		validCurrentBoardPosition2 = 2;
		
		testSystem1 = system1;
		
		player1 = new Player(validPlayerName1, validResourceBalance1, validCurrentBoardPosition1);
		player2 = new Player(validPlayerName2, validResourceBalance2, validCurrentBoardPosition2);
		
	}

	@Test
	void testArtemisSystem() {
		fail("Not yet implemented");
	}

	@Test
	void testArtemisSystemString() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSystemName() {
		fail("Not yet implemented");
	}

	@Test
	void testSetSystemName() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckFullyDeveloped() {
		
		// set two components as fully developed
		testComponent1.setDevelopmentStage(4);
		testComponent2.setDevelopmentStage(4);
		
		// false expected as not all components fully developed
		assertFalse(testSystem1.checkFullyDeveloped());
		
		// set final component in system to fully developed
		testComponent3.setDevelopmentStage(4);
		
		// all components in system have been set to fully developed - true expected
		assertTrue(testSystem1.checkFullyDeveloped());

	}
	
	@Test
	void testCheckSystemIsOwnedByOnePlayer() {
		
		// set player1 to own all components in system
		testComponent1.setComponentOwner(player1);
		testComponent2.setComponentOwner(player1);
		testComponent3.setComponentOwner(player1);
		
		/*
		 * Included for visualisation purposes
		 *  
		for (Component component : testSystem1.getComponentsInSystem()) {
		System.out.println(component.getSquareName() + " - " + component.getComponentOwner());
		}
		*/
		
		// expect true
		boolean actualValue = testSystem1.checkSystemIsOwnedByOnePlayer();
		assertTrue(actualValue);
		
		// set player2 as owner of one of the components
		testComponent2.setComponentOwner(player2);
		
		// now expect false
		actualValue = testSystem1.checkSystemIsOwnedByOnePlayer();
		
		assertFalse(actualValue);
		
		/*
		 * Included for visualisation purposes
		 *  
		for (Component component : testSystem1.getComponentsInSystem()) {
		System.out.println(component.getSquareName() + " - " + component.getComponentOwner());
		}
		*/
		
	}

	@Test
	void testGetSystemOwner() {
		fail("Not yet implemented");
	}

	@Test
	void testSetSystemOwner() {
		fail("Not yet implemented");
	}

	@Test
	void testGetComponentsInSystem() {
		fail("Not yet implemented");
	}

	@Test
	void testAddComponent() {
		fail("Not yet implemented");
	}

	@Test
	void testDisplayAllDetails() {
		fail("Not yet implemented");
	}

}
