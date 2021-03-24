package artemis.lite;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This implements the Player object for the game. It retains player information
 * and records player owned objects.
 *
 * @author John Young 40030361
 * @author Kieran Lambe 40040696
 */
public class Player {
	private String playerName;
	private int resourceBalance;
	private int currentBoardPosition;
	private int actionPoints = Game.DEFAULT_ACTION_POINTS;
	private List<Component> ownedComponents = new ArrayList<>();
	private List<ArtemisSystem> ownedSystems = new ArrayList<>();
	private int CountOfTimesPlayerDeclinedResources;

	/**
	 * Default constructor - accepts no arguments
	 */
	public Player() {
	}

	/**
	 * Constructor with arguments to set instance values
	 *
	 * @param playerName           the value for the player's name
	 * @param resourceBalance      the player's initial resource balance
	 * @param currentBoardPosition the player's starting position
	 */
	public Player(String playerName, int resourceBalance, int currentBoardPosition) {
		this.playerName = playerName;
		this.resourceBalance = resourceBalance;
		this.currentBoardPosition = currentBoardPosition;
	}

	// methods

	/**
	 * Update the component owner to the player that invokes the method
	 *
	 * @param component the object to be updated
	 */
	private void updateOwner(Component component) {
		component.setComponentOwner(this);
	}

	/**
	 * Update the system owner to the player that invokes the method
	 *
	 * @param system the object to be updated
	 */
	private void updateOwner(ArtemisSystem system) {
		system.setSystemOwner(this);
	}

	// TODO - additional validation to ensure that this is not already owned -
	// likely dealt with within
	// TODO - game announcement

	/**
	 * Updates the player's ownedComponents to add the specified component. This
	 * will only add components that do not already exist within the ArrayList.
	 *
	 * @param component the object to be added
	 * @throws NullPointerException if component is null
	 */
	private void addComponent(Component component) throws NullPointerException {
		if (component == null) {
			throw new NullPointerException("Cannot add null component to player's owned components");
		}

		// make sure player does not already own this component
		if (!ownedComponents.contains(component)) {
			ownedComponents.add(component);
		}
	}

	// TODO - game announcement

	/**
	 * Updates the player's ownedComponents to remove the specified component. This
	 * will only remove components that exist within the ArrayList.
	 *
	 * @param component the object to be removed
	 * @throws NullPointerException if component is null
	 */
	private void removeComponent(Component component) throws NullPointerException {
		if (component == null) {
			throw new NullPointerException("Cannot remove null component to player's owned components");
		}

		// can only be removed if it does not already exist
		ownedComponents.remove(component);
	}

	/**
	 * This method determines which of the player's owned components can be
	 * developed and adds them to a HashMap.
	 * 
	 * @return - the method returns a HashMap of the owned components that can be
	 *         developed.
	 */
	public Map<Integer, Component> getOwnedComponentsThatCanBeDeveloped() {

		Map<Integer, Component> componentsThatCanBeDeveloped = new HashMap<Integer, Component>();

		int menuNumber = 1;

		for (Component component : ownedComponents) {
			if (component.checkComponentCanBeDeveloped()) {
				componentsThatCanBeDeveloped.put(menuNumber++, component);
			}
		}

		return componentsThatCanBeDeveloped;

	}

	// TODO - additional validation
	// TODO - game announcement

	/**
	 * Add a system to the player's owned systems
	 *
	 * @param system the object to be added
	 * @throws NullPointerException if system is null
	 */
	public void addSystem(ArtemisSystem system) throws NullPointerException {
		if (system == null) {
			throw new NullPointerException("Cannot add null to player's owned systems");
		}

		// make sure player does not already own this
		if (!ownedSystems.contains(system)) {
			ownedSystems.add(system);
		}
	}

	// TODO - game announcement

	/**
	 * Remove a system from the player's ownership
	 *
	 * @param system the object to be removed
	 * @throws NullPointerException if system is null
	 */
	private void removeSystem(ArtemisSystem system) throws NullPointerException {
		if (system == null) {
			throw new NullPointerException("Cannot remove null from player's owned systems");
		}

		// can only be removed if it does not already exist
		ownedSystems.remove(system);
	}

	/**
	 * Determines whether the component already has an owner
	 *
	 * @param component to be checked
	 * @return true - when a component does not have an owner
	 */
	private boolean checkComponentIsNotOwned(Component component) {
		return component.getComponentOwner() == null;
	}

	/**
	 * Determines whether the player owns the component
	 *
	 * @param component the object to be checked
	 * @return true if player owns the component
	 */
	public boolean checkPlayerOwns(Component component) {
		return (ownedComponents.contains(component));
	}

	/**
	 * Determines whether a player has sufficient resources
	 *
	 * @param resourcesRequired integer value of resources needed
	 * @return true if player has resources greater than or equal to that specified
	 */
	public boolean checkSufficientResources(int resourcesRequired) {
		return (resourceBalance >= resourcesRequired);
	}

	/**
	 * @return true if the player has more than zero actionPoints
	 */
	public boolean checkHasActionPoints() {
		return (actionPoints > 0);
	}

	/**
	 * Updates Player and Component objects to reflect new owner, providing that all
	 * validation checks were successful.
	 *
	 * @param component - the component that will be purchased
	 * @return true - if the component was successfully purchased and ownership
	 *         changed
	 * @throws Exception - if any validation conditions fail
	 */
	public void purchaseComponent(Component component) throws IllegalArgumentException {

		// perform validation
		if (!checkComponentIsNotOwned(component)) {
			throw new IllegalArgumentException(component.getSquareName() + " is already owned.");
		} else if (!checkSufficientResources(component.getComponentCost())) {
			throw new IllegalArgumentException("Insufficient resources to purchase " + component.getSquareName());
		} else if (!checkHasActionPoints()) {
			throw new IllegalArgumentException("Insufficient action points cannot perform this action.");
		}

		// player has enough resources, action points, and component is not already
		// owned

		// update action points
		consumeActionPoint(1);
		// update player resources
		updateResources(-component.getComponentCost());
		// update count of resources devoted to component (for end game notification)
		component.updateTotalResourcesDevotedToComponent(component.getComponentCost()); // TODO - incorporate into
																						// testing
		// add component to player's components
		addComponent(component);
		// update component owned
		component.setComponentOwner(this);
		// announce to all game players what just happened
		String message;
		message = getPlayerName() + " has purchased " + component.getSquareName() + " for "
				+ component.getComponentCost();
		message += " " + Game.RESOURCE_NAME + " and has " + this.getResourceBalance() + " " + Game.RESOURCE_NAME
				+ " remaining";
		Game.announce(message);
		// additional message if player now owns entire system in which component is
		// contained
		if (component.getComponentSystem().checkSystemIsOwnedByOnePlayer()) {
			Game.announce("now owns all of " + component.getComponentSystem().getSystemName()
					+ " and can develop any of its components", this);
		}
	}

	/**
	 * Overloaded method that casts a Square object into a Component object and
	 * invokes purchaseComponent(Component) method
	 *
	 * @param square a square object
	 */
	public void purchaseComponent(Square square) {
		if (square instanceof Component) {
			// cast square as Component
			purchaseComponent((Component) square);
		} else {
			System.out.println(square.getSquareName() + " is not purchasable");
		}
	}

	/**
	 * Enables the active player to propose a trade with another player. The other
	 * player can reject the trade. When a trade is proposed, the trade resource
	 * amount is the component cost.
	 *
	 * @param component - the object that will be proposed for a trade
	 * @param scanner   - used to pass through a scanner input. - NOTE: this should
	 *                  be Scanner(System.in) outside of testing.
	 * @throws IllegalArgumentException
	 */
	public void tradeComponent(Component component, Scanner scanner) throws IllegalArgumentException {
		// if player already owns this exit method
		if (component.getComponentOwner() == this) {
			return;
		}

		if (checkComponentIsNotOwned(component)) {
			throw new IllegalArgumentException(component.getSquareName() + " cannot be traded as it is not owned.");
		} else if (!checkSufficientResources(component.getComponentCost())) {
			throw new IllegalArgumentException("Insufficient resources to purchase " + component.getSquareName());
		} else if (!checkHasActionPoints()) {
			throw new IllegalArgumentException("Insufficient action points cannot perform this action.");
		}

		Player componentOwner = component.getComponentOwner();

		// announcement
		String message;
		message = getPlayerName() + " has initiated a trade with " + componentOwner.getPlayerName() + "...\n";
		message += "[" + componentOwner.getPlayerName() + "] Do you wish to trade the component "
				+ component.getSquareName() + " for " + component.getComponentCost() + " " + Game.RESOURCE_NAME
				+ "?\n\n";
		message += "Input 'yes' to accept, or, 'no' to reject";

		Game.announce(message);

		// get user input
		System.out.print("Decision: ");
		String playerInput = scanner.next();
		System.out.println();

		String additionalMessage = "";

		if (playerInput.equalsIgnoreCase("yes")) {
			message = componentOwner.getPlayerName() + " has accepted the trade with " + getPlayerName() + "\n\n";
			message += "\tTransferring " + component.getSquareName() + " from " + componentOwner.getPlayerName()
					+ " to " + this.getPlayerName() + "\n";

			Game.announce(message);
			// update resources
			transferResources(componentOwner, component.getComponentCost());
			// update owner
			component.setComponentOwner(this);
			// remove from componentOwner's components
			componentOwner.removeComponent(component);
			// add component to player's components
			addComponent(component);

			message = "Transfer completed."; // this will be announced outside the if-else block below

			// check if the new component owner now owns the system
			if (component.getComponentSystem().checkSystemIsOwnedByOnePlayer()) {
				additionalMessage = "now owns all of " + component.getComponentSystem().getSystemName()
						+ " and can develop any of its components";
			}

		} else {
			// trade rejected (input was not yes)
			message = componentOwner.getPlayerName() + " has rejected the trade with " + getPlayerName();
		}

		Game.announce(message);
		if (additionalMessage.length() > 1) {
			Game.announce(additionalMessage, this);
		}
		consumeActionPoint(1);
		// scanner.close(); KL - commented out for now as was causing IllegalStateException during playtesting
	}

	/**
	 * Facilitates the transfer of resources from one player to another. This will
	 * transfer from currentPlayer to otherPlayer and vice-versa, dependent on the
	 * value of resources (positive or negative).
	 *
	 * @param otherPlayer - the player on the other side of the transfer
	 * @param resources   - a positive balance transfers resources TO otherPlayer, a
	 *                    negative balance transfers resources TO the currentPlayer
	 */

	// TODO - test transferResources() method

	public void transferResources(Player otherPlayer, int resources) throws IllegalArgumentException {
		Player transferFrom, transferTo;
		if (resources >= 0) {
			transferFrom = this;
			transferTo = otherPlayer;
		} else {
			transferFrom = otherPlayer;
			transferTo = this;
		}

		resources = Math.abs(resources); // get absolute value

		if (!transferFrom.checkSufficientResources(resources)) {
			throw new IllegalArgumentException(
					transferFrom + " does not have enough " + Game.RESOURCE_NAME + " to complete this action");
		}

		transferFrom.updateResources(-1 * resources); // multiplied by -1 to make this a negative adjustment
		transferTo.updateResources(resources);

		String message = "Transferred " + resources + " " + Game.RESOURCE_NAME + " from " + transferFrom.getPlayerName()
				+ " to " + transferTo.getPlayerName();

		Game.announce(message);
	}

	/**
	 * This will offer the component the currentPlayer has landed on to other
	 * players (excluding currentPlayer). This method will only be called if the
	 * currentPlayer rejects the purchase of the component.
	 *
	 * @param component the component the currentPlayer has landed on
	 * @return true - if another player purchased the component; false - if the
	 *         component was not purchased
	 */
	public boolean offerComponentToOtherPlayers(Component component, Scanner scanner) {
		if (component.isOwned()) {
			throw new IllegalArgumentException("Component already has an owner");
		}

		List<Player> players = new ArrayList<>(Game.getPlayers()); // create a copy of the players in the game
		List<Player> playersThatWishToPurchase = new ArrayList<Player>();

		players.remove(this);

		for (Player player : players) {
			if (player.checkSufficientResources(component.getComponentCost())) {
				System.out.println(
						player.getPlayerName() + " do you wish to purchase the component " + component.getSquareName()
								+ " for " + component.getComponentCost() + " " + Game.RESOURCE_NAME + "?");

				String userInput;
				do {
					System.out.println("Enter yes or no... ");
					userInput = scanner.next();

					if (userInput.equalsIgnoreCase("yes")) {
						// include player in array for rolling dice
						playersThatWishToPurchase.add(player);
					} else if (userInput.equalsIgnoreCase("no")) {
						System.out.println(player.getPlayerName() + " does not wish to be considered in the purchase");
					} else {
						System.out.println("Invalid input");
					}

				} while (!userInput.equalsIgnoreCase("yes") && !userInput.equalsIgnoreCase("no"));

				// do while loop
			} else {
				System.out.println(
						player.getPlayerName() + " does not have enough resources to participate in the bidding...");
			}
			System.out.println();
		}

		if (playersThatWishToPurchase.size() > 1) {
			Player playerWithHighestRoll = Game.getHighestRoll(playersThatWishToPurchase);

			playerWithHighestRoll.purchaseComponent(component);

		} else if (playersThatWishToPurchase.size() == 1) {
			playersThatWishToPurchase.get(0).purchaseComponent(component);
		} else {
			Game.announce(
					"No players wish to purchase " + component.getSquareName() + "... Therefore it remains unowned.");
		}

		return false;
	}

	public boolean develop(Component component) {
		return false;
	}

	public boolean develop(ArtemisSystem system) {
		return false;
	}

	public void displayAllDetails() {

	}

	/**
	 * This method increments the variable countOfTimesPlayerDeclinedResources by 1.
	 * This counter is intended to be included in the post-game information as an
	 * indication of how 'selfless' the player was over the course of the game.
	 */
	public void incrementCountOfTimesPlayerDeclinedResources() {
		CountOfTimesPlayerDeclinedResources++;
	}

	/**
	 * Updates the current resources to reflect the change
	 *
	 * @param resources - the amount to add/subtract from the current resources
	 */
	public int updateResources(int resources) {
		int newBalance = this.getResourceBalance() + resources;
		setResourceBalance(newBalance);

		// used to provide user friendly output of resource change (+ or -)
		String change = (resources >= 0 ? "+" + resources : "" + resources);

		Game.announce(getPlayerName() + " now has " + newBalance + " " + Game.RESOURCE_NAME + " (" + change + ")");

		return newBalance;
	}

	// getters and setters

	/**
	 * @return the value of the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set the name of the player
	 *
	 * @param playerName should be a string
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the value of the player's resources
	 */
	public int getResourceBalance() {
		return resourceBalance;
	}

	/**
	 * Set the value of the player's resources - subject to constraints.
	 *
	 * @param resourceBalance must be zero or greater
	 * @throws IllegalArgumentException if resource balance is less than zero
	 */
	public void setResourceBalance(int resourceBalance) throws IllegalArgumentException {
		if (resourceBalance < 0) {
			throw new IllegalArgumentException("Resource balance must be zero or more");
		}

		this.resourceBalance = resourceBalance;
	}

	/**
	 * @return the integer value representing the player's position as part of the
	 *         Board Squares array
	 */
	public int getCurrentBoardPosition() {
		return currentBoardPosition;
	}

	/**
	 * Update the player's current position on the board. The position is the
	 * element index of the square they currently reside on within the Board
	 * squares[] variable.
	 *
	 * @param currentBoardPosition must be zero or greater, and cannot be greater
	 *                             than the board length-1
	 * @throws IllegalArgumentException if currentBoardPosition is less than zero or
	 *                                  greater than the board length-1
	 */
	public void setCurrentBoardPosition(int currentBoardPosition) throws IllegalArgumentException {
		// TODO - we should put in a restriction that this cannot be greater than length
		// of board squares array
		if (currentBoardPosition < 0) {
			throw new IllegalArgumentException("Player board position invalid");
		}

		this.currentBoardPosition = currentBoardPosition;
	}

	/**
	 * @return the integer value for the number of actions the player has remaining
	 */
	public int getActionPoints() {
		return actionPoints;
	}

	/**
	 * Update the players action points
	 *
	 * @param actionPoints - the number of action points that will be subtracted for
	 *                     a given action
	 */
	public void consumeActionPoint(int actionPoints) {
		setActionPoints(getActionPoints() - actionPoints);
		Game.announce(getPlayerName() + " has consumed " + actionPoints + " action points and has " + this.actionPoints
				+ " remaining for this turn.");
	}

	/**
	 * Sets the number of actions a player can make per turn. This is method is
	 * called each time the player turn is invoked.
	 *
	 * @param actionPoints the integer value representing the number of turns a
	 *                     player can make
	 * @throws IllegalArgumentException if actionPoints is less than zero
	 */
	public void setActionPoints(int actionPoints) throws IllegalArgumentException {
		// TODO - actionPoints should not exceed the maximum actionPoints per turn
		// this should be implemented via constant in Game
		if (actionPoints < 0) {
			throw new IllegalArgumentException("Action points value invalid");
		}
		this.actionPoints = actionPoints;
	}

	/**
	 * @return an ArrayList of Components owned by the player
	 */
	public List<Component> getOwnedComponents() {
		return ownedComponents;
	}

	/**
	 * @return an ArrayList of ArtemisSystems owned by the player
	 */
	public List<ArtemisSystem> getOwnedSystems() {
		return ownedSystems;
	}

	/**
	 * @return the player's name
	 */
	@Override
	public String toString() {
		return playerName;
	}

	/**
	 * Outputs the player's current resources and action points. This is used to
	 * provide an update at the start of each player's turn.
	 */
	public void displayTurnStats() {
		System.out.printf("\n%15s STATS UPDATE\n", playerName.toUpperCase());
		System.out.printf("%15s %6s\n", Game.RESOURCE_NAME, resourceBalance);
		System.out.printf("%15s %6s\n\n", "ACTION POINTS", actionPoints);
	}

	/**
	 * @return the countOfTimesPlayerDeclinedResources
	 */
	public int getCountOfTimesPlayerDeclinedResources() {
		return CountOfTimesPlayerDeclinedResources;
	}

	/**
	 * @param countOfTimesPlayerDeclinedResources the
	 *                                            countOfTimesPlayerDeclinedResources
	 *                                            to set
	 */
	public void setCountOfTimesPlayerDeclinedResources(int countOfTimesPlayerDeclinedResources) {
		CountOfTimesPlayerDeclinedResources = countOfTimesPlayerDeclinedResources;
	}
}