package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * This class tests the functionality of CompareByCounterOfTimesPlayerDeclinedResources
 * @author Kieran Lambe 40040696
 *
 */
class CompareByCounterOfTimesPlayerDeclinedResourcesTest {

	Comparator<Player> testComparator;

	Player testPlayer1, testPlayer2, testPlayer3;
	int counterOfDeclinedResourcesValid1,counterOfDeclinedResourcesValid2;

	@BeforeEach
	void setUp() throws Exception {

		testComparator = new CompareByCounterOfTimesPlayerDeclinedResources();

		testPlayer1 = new Player();
		testPlayer2 = new Player();
		testPlayer3 = null;
		
		counterOfDeclinedResourcesValid1 = 1;
		counterOfDeclinedResourcesValid2 = 2;

		testPlayer1.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid1);
		testPlayer2.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid2);

	}

	@Test
	void testCompareByCounterOfTimesPlayerDeclineResourcesValid() {
		
		// testPlayer2 > testPlayer1
		
		int expectedValue, actualValue;
		expectedValue = 1;
		actualValue = testComparator.compare(testPlayer1, testPlayer2);
		
		assertEquals(expectedValue, actualValue);
		
		// testPlayer1 > testPlayer2
		
		testPlayer1.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid2);
		testPlayer2.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid1);
		
		expectedValue = -1;
		
		actualValue = testComparator.compare(testPlayer1, testPlayer2);
		assertEquals(expectedValue, actualValue);
		
		// testPlayer1 == testPlayer2
		
		testPlayer1.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid2);
		testPlayer2.setCountOfTimesPlayerDeclinedResources(counterOfDeclinedResourcesValid2);
		
		expectedValue = 0;
		
		actualValue = testComparator.compare(testPlayer1, testPlayer2);
		assertEquals(expectedValue, actualValue);
		
		assertEquals(expectedValue, actualValue);
		
	}
	
	@Test
	void testCompareByCounterOfTimesPlayerDeclineResourcesNullInvalid() {
		// passing null players as parameter argument
		int actualValue = testComparator.compare(testPlayer3, testPlayer3);
		
		assertEquals(-1, actualValue);
	}

}
