/**
 * 
 */
package artemis.lite;

import java.util.Comparator;

/**
 * This class allows Player objects to be compared according to the number of times they declined resources - high to low.
 * @author Kieran Lambe 40040696
 *
 */
public class CompareByCounterOfTimesPlayerDeclinedResources implements Comparator<Player> {

	@Override
	public int compare(Player player1, Player player2) {
		
		return player2.getCountOfTimesPlayerDeclinedResources() - player1.getCountOfTimesPlayerDeclinedResources();
	}

}
