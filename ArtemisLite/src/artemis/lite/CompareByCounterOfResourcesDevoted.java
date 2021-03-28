/**
 * 
 */
package artemis.lite;

import java.util.Comparator;

/**
 * This comparator is used to compare two Components by the total number of resources devoted to them.
 * @author Kieran Lambe 40040696
 *
 */
public class CompareByCounterOfResourcesDevoted implements Comparator<Component> {

	@Override
	public int compare(Component component1, Component component2) {
		try {
			return component2.getTotalResourcesDevotedToComponent() - component1.getTotalResourcesDevotedToComponent();
		} catch (NullPointerException nullPointerException) {
			System.out.println("Error: Cannot compare null components");
			return -1;
		}
	}

}
