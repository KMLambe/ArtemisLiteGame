/**
 * 
 */
package artemis.lite;

import java.util.Comparator;

/**
 * This comparator facilitates the comparison of Components by position - low to high.
 * @author Kieran Lambe 40040696
 *
 */
public class CompareByPosition implements Comparator<Component> {

	@Override
	public int compare(Component component1, Component component2) {
		try {
			return component1.getSquarePosition()-component2.getSquarePosition();
		} catch (NullPointerException nullPointerException) {
			System.out.println("Cannot compare null components");
			return -1;
		}
		
	}

	
}
