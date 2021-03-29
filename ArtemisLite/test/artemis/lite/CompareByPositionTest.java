package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * This class tests the functionality of CompareByPosition
 * @author Kieran Lambe 40040696
 *
 */
class CompareByPositionTest {
	
	Comparator<Component> testComparator;
	
	Component testComponent1, testComponent2, testComponent3, testComponent4;
	
	int validBoardPosition1,validBoardPosition2;

	@BeforeEach
	void setUp() throws Exception {
		
		testComparator = new CompareByPosition();
		
		testComponent1 = new Component();
		testComponent2 = new Component();
		testComponent3 = new Component();
		testComponent4 = null;
		
		validBoardPosition1 = 1;
		validBoardPosition2 = 2;
		
		testComponent1.setSquarePosition(validBoardPosition1);
		testComponent2.setSquarePosition(validBoardPosition2);
		testComponent3.setSquarePosition(validBoardPosition2);
		
	}

	@Test
	void testCompareByPositionValid() {
	
		// testComponent1 has lower position than testComponent2 - expect minus 1
		assertEquals(-1, testComparator.compare(testComponent1, testComponent2));
		
		// testComponent2 has higher position than testComponent1 - expect 1
		assertEquals(1, testComparator.compare(testComponent2, testComponent1));
		
		// positions are the same, expect 0
		assertEquals(0, testComparator.compare(testComponent2, testComponent3));
		
	}
	
	@Test
	void testCompareByPositionNullInvalid() {
		// passing null components as parameter argument
		int actualValue = testComparator.compare(testComponent4, testComponent4);
		
		assertEquals(-1, actualValue);
	}

}
