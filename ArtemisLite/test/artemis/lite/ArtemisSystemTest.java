package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests to check the functionality of the ArtemisSystem class is working as intended. 
 * @author Kieran Lambe 40040696
 *
 */
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

	// test systems
	ArtemisSystem testSystem1, testSystem2;

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

		// set this as the var in game
		Game.setBoard(board1);
		
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
		testSystem2 = system2;
		
		player1 = new Player(validPlayerName1, validResourceBalance1, validCurrentBoardPosition1);
		player2 = new Player(validPlayerName2, validResourceBalance2, validCurrentBoardPosition2);

		// let game class know it is running tests
		Game.setTestMode(true);
	}

	@Test
	void testArtemisSystemDefaultConstructor() {
		testSystem1 = new ArtemisSystem();
		assertNotNull(testSystem1);
	}

	@Test
	void testArtemisSystemConstructorWithArguments() {
		testSystem2 = new ArtemisSystem(validSystemName2);
		
		assertEquals(validSystemName2, testSystem2.getSystemName());
	}

	@Test
	void testGetSetSystemName() {
		testSystem2.setSystemName(validSystemName1);
		assertEquals(validSystemName1, testSystem2.getSystemName());
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
		
		// expect true
		boolean actualValue = testSystem1.checkSystemIsOwnedByOnePlayer();
		assertTrue(actualValue);
		
		// set player2 as owner of one of the components
		testComponent2.setComponentOwner(player2);
		
		// now expect false
		actualValue = testSystem1.checkSystemIsOwnedByOnePlayer();
		
		assertFalse(actualValue);
		
	}

	@Test
	void testGetSetSystemOwner() {
		testSystem1.setSystemOwner(player2);
		assertEquals(player2, testSystem1.getSystemOwner());
	}
	
	@Test
	void testAddComponent() {
		testSystem2 = new ArtemisSystem();
		testSystem2.addComponent(testComponent1);
		testSystem2.addComponent(testComponent3);
		
		assertEquals(2, testSystem2.getComponentsInSystem().size());
		
		assertTrue(testSystem2.getComponentsInSystem().contains(testComponent1));
		
		assertTrue(testSystem2.getComponentsInSystem().contains(testComponent3));
	}

	@Test
	void testGetComponentsInSystem() {
		
		ArrayList<Component> testComponents = new ArrayList<Component>();
		testComponents.add(testComponent2);
		testComponents.add(testComponent3);
		
		testSystem2 = new ArtemisSystem(validSystemName2);
		testSystem2.addComponent(testComponent2);
		testSystem2.addComponent(testComponent3);
		
		assertEquals(testComponents, testSystem2.getComponentsInSystem());
		
		
	}

	@Test
	void testDisplayAllDetails() {
		// cannot be unit tested
	}

}
