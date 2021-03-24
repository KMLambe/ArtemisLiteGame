/**
 * 
 */
package artemis.lite;

import java.util.Comparator;

/**
 * @author kmlam
 *
 */
public class CompareByCounterOfTimesPlayerDeclinedResources implements Comparator<Player> {

	@Override
	public int compare(Player player1, Player player2) {
		
		return player2.getCountOfTimesPlayerDeclinedResources() - player1.getCountOfTimesPlayerDeclinedResources();
	}

}
