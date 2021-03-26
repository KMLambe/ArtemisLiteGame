/**
 * 
 */
package artemis.lite;

import java.util.Comparator;

/**
 * @author kmlam
 *
 */
public class CompareByCounterOfResourcesDevoted implements Comparator<Component> {

	@Override
	public int compare(Component component1, Component component2) {
		return component2.getTotalExpertsDevotedToComponent() - component1.getTotalExpertsDevotedToComponent();
	}

}
