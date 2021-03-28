package artemis.lite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompareByCounterOfResourcesDevotedTest {
	
	Comparator<Component> testComparator;
	
	Component testComponent1, testComponent2, testComponent3;
	int resourcesDevotedValid1 = 1;
	int resourcesDevotedValid2 = 2;

	@BeforeEach
	void setUp() throws Exception {
		
		testComparator = new CompareByCounterOfResourcesDevoted();
		
		testComponent1 = new Component();
		testComponent2 = new Component();
		testComponent3 = null;
		
		testComponent1.setTotalExpertsDevotedToComponent(resourcesDevotedValid1);
		testComponent2.setTotalExpertsDevotedToComponent(resourcesDevotedValid2);
		
	}

	@Test
	void testCompareByCounterOfResourcesDevotedValid() {
		
		// testComponent2 > testComponent1
		
		int expectedValue, actualValue;
		expectedValue = 1;
		actualValue = testComparator.compare(testComponent1, testComponent2);
		
		assertEquals(expectedValue, actualValue);
		
		// testComponent1 > testComponent2
		
		testComponent1.setTotalExpertsDevotedToComponent(resourcesDevotedValid2);
		testComponent2.setTotalExpertsDevotedToComponent(resourcesDevotedValid1);
		
		expectedValue = -1;
		
		actualValue = testComparator.compare(testComponent1, testComponent2);
		assertEquals(expectedValue, actualValue);
		
		// testComponent1 == testComponent2
		
		testComponent1.setTotalExpertsDevotedToComponent(resourcesDevotedValid2);
		testComponent2.setTotalExpertsDevotedToComponent(resourcesDevotedValid2);
		
		expectedValue = 0;
		
		actualValue = testComparator.compare(testComponent1, testComponent2);
		assertEquals(expectedValue, actualValue);
		
		assertEquals(expectedValue, actualValue);
		
	}
	
	@Test
	void testCompareByCounterOfResourcesDevotedNullInvalid() {
		// passing null components as parameter argument
		int actualValue = testComparator.compare(testComponent3, testComponent3);
		
		assertEquals(-1, actualValue);
	}

}
