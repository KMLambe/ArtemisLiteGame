package artemis.lite;

import java.util.HashMap;
import java.util.Map;

/**
 * This class facilitates the creation of component objects. This class is a
 * subclass of Square. Component - a square which can be owned by a player.
 *
 * @author Kieran Lambe 40040696
 * @author John Young 40030361
 */
public class Component extends Square {

	/**
	 * Constants to define the minimum and maximum development levels for all
	 * components
	 */
	private final static int MINIMUM_DEVELOPMENT_LEVEL = 0;
	private final static int MAXIMUM_DEVELOPMENT_LEVEL = 4;
	private final static int MINOR_DEVELOPMENT_ADDITIONAL_PURCHASE_COST = 10;
	private final static int MAJOR_DEVELOPMENT_ADDITIONAL_PURCHASE_COST = 20;
	private final static int MINOR_DEVELOPMENT_ADDITIONAL_DEVELOPMENT_COST = 10;
	private final static int MAJOR_DEVELOPMENT_ADDITIONAL_DEVELOPMENT_COST = 20;
	private final static int MAXIMUM_COST_TO_DEVELOP = 999999;
	private final static int INCREASE_COST_OF_LANDING_MINOR_DEVELOPMENT = 10;
	private final static int INCREASE_COST_OF_LANDING_MAJOR_DEVELOPMENT = 20;

	private int developmentStage;

	private int componentCost;
	private int costToDevelop;
	private int costForLanding;
	private Player componentOwner;
	private ArtemisSystem componentSystem;
	private Map<Integer, DevelopmentStage> developmentStageNamesMap;

	private int totalResourcesDevotedToComponent;

	/**
	 * Default constructor
	 */
	public Component() {

	}

	/**
	 * Constructor with arguments
	 *
	 * @param squareName      - the name of the component
	 * @param componentCost   - the cost a player will need to pay to own
	 * @param costToDevelop   - the cost to develop
	 * @param costForLanding  - the cost for any player that isn't the owner landing
	 *                        on this component
	 * @param componentSystem - the ArtemisSystem object that this component belongs
	 *                        to
	 */
	public Component(String squareName, int componentCost, int costToDevelop, int costForLanding,
					 ArtemisSystem componentSystem) {
		super(squareName);
		this.developmentStage = MINIMUM_DEVELOPMENT_LEVEL;
		this.componentCost = componentCost;
		this.costToDevelop = costToDevelop;
		this.costForLanding = costForLanding;
		setComponentSystem(componentSystem);

		developmentStageNamesMap = new HashMap<Integer, DevelopmentStage>();
		developmentStageNamesMap.put(0, DevelopmentStage.ANALYSING_REQUIREMENTS);
		developmentStageNamesMap.put(1, DevelopmentStage.DESIGNING);
		developmentStageNamesMap.put(2, DevelopmentStage.BUILDING);
		developmentStageNamesMap.put(3, DevelopmentStage.TESTING);
		developmentStageNamesMap.put(4, DevelopmentStage.MAINTAINING);

		totalResourcesDevotedToComponent = 0;

	}

	/**
	 * This method facilitates the development of the component to the next stage
	 * Component development stages must be between
	 * {@value #MINIMUM_DEVELOPMENT_LEVEL} and {@value #MAXIMUM_DEVELOPMENT_LEVEL}
	 * inclusive
	 */
	public void developComponent() {

		Player currentPlayer = this.getComponentOwner();
		String developmentAnnouncement = null;
		int actionPointsToDeduct = 1;

		if (!this.checkFullyDeveloped() && currentPlayer != null && currentPlayer.getResourceBalance() >= costToDevelop
				&& currentPlayer.checkHasActionPoints()) {

			// deduct development fee from current player
			currentPlayer.updateResources(-costToDevelop);

			// add to total number of resources devoted to this component across the game
			updateTotalResourcesDevotedToComponent(costToDevelop);

			// deduct action point
			currentPlayer.consumeActionPoint(actionPointsToDeduct);

			// increase the development level to the next stage
			incrementDevelopmentStage();

			// check new development level and adjust cost for landing
			// and also check and adjust cost to develop
			if (developmentStage < (MAXIMUM_DEVELOPMENT_LEVEL - 1)) {

				increaseCostToPurchase(MINOR_DEVELOPMENT_ADDITIONAL_PURCHASE_COST);
				increaseCostOfLanding(INCREASE_COST_OF_LANDING_MINOR_DEVELOPMENT);
				increaseCostToDevelop(MINOR_DEVELOPMENT_ADDITIONAL_DEVELOPMENT_COST);

			} else if (developmentStage == (MAXIMUM_DEVELOPMENT_LEVEL - 1)) {

				increaseCostToPurchase(MINOR_DEVELOPMENT_ADDITIONAL_PURCHASE_COST);
				increaseCostOfLanding(INCREASE_COST_OF_LANDING_MINOR_DEVELOPMENT);
				increaseCostToDevelop(MAJOR_DEVELOPMENT_ADDITIONAL_DEVELOPMENT_COST);

			} else if (developmentStage == MAXIMUM_DEVELOPMENT_LEVEL) {

				increaseCostToPurchase(MAJOR_DEVELOPMENT_ADDITIONAL_PURCHASE_COST);
				increaseCostOfLanding(INCREASE_COST_OF_LANDING_MAJOR_DEVELOPMENT);
				setCostToDevelopToMaximum();

			}

			// determine announcement to output to players

			switch (developmentStage) {
				case 1:
					developmentAnnouncement = this.getSquareName() + " has advanced to the "
							+ developmentStageNamesMap.get(1) + " stage.";
					break;
				case 2:
					developmentAnnouncement = this.getSquareName() + " has advanced to the "
							+ developmentStageNamesMap.get(2) + " stage.";
					break;
				case 3:
					developmentAnnouncement = this.getSquareName() + " has advanced to the "
							+ developmentStageNamesMap.get(3) + " stage.";
					break;
				case 4:
					developmentAnnouncement = this.getSquareName() + " has advanced to the "
							+ developmentStageNamesMap.get(4) + " stage.";
					break;
				default:
					// nothing happens
			}

			// announce development
			Game.announce(developmentAnnouncement);

			// additional announcement if fully developed
			if (checkFullyDeveloped()) {
				String fullyDevelopedAnnouncement = this.getSquareName()
						+ " has advanced to the maximum development level and can be developed no further.";
				Game.announce(fullyDevelopedAnnouncement);
			}

			// additional announcement if system is fully developed
			if (getComponentSystem().checkFullyDeveloped()) {
				Game.announce("'s team of experts has fully developed " + getComponentSystem().getSystemName() + "! That's a big step towards completion of the project.", currentPlayer);
			}

		} else {
			System.out.println("ERROR - Component cannot be developed.");
		}

	}

	/**
	 * Asks component owners if they wish to receive resources when another player
	 * lands on a component they own. The component owner may decide to accept or
	 * decline their right to receive a fee and their response is returned as either
	 * true or false
	 *
	 * @param currentPlayer - the current player who has landed on an owned component
	 */
	public void checkOwnerWantsResources(Player currentPlayer) {
		// notify players of cost for landing
		System.out.println("The cost of landing on this component is " + costForLanding);
		Game.announce("is the owner of this component - meaning they can choose whether they want to take their fee or not!", componentOwner);

		if (currentPlayer.getResourceBalance() < costForLanding) {
			System.out.println("WARNING: If " + componentOwner.getPlayerName() + " decides to request their fee then "
					+ currentPlayer.getPlayerName() + " will run out of " + Game.RESOURCE_NAME.toLowerCase()
					+ " and the game will end!");
		}

		// confirm if owner wishes to take their fee
		Game.announce("do you require " + Game.RESOURCE_NAME + " from " + currentPlayer.getPlayerName() + "?", componentOwner);
		boolean ownerResponse = Game.getPlayerConfirmation();

		if (!ownerResponse) {
			// rejected the resources
			componentOwner.incrementCountOfTimesPlayerDeclinedResources();
		}

		chargePlayerForLanding(currentPlayer, ownerResponse);
	}

	/**
	 * Deducts the required fee from the current player upon landing on an owned
	 * component
	 *
	 * @param currentPlayer - the current player
	 * @param ownerResponse - the component owner's decision as to whether they wish
	 *                      to receive resources or not. This is the result of the
	 *                      checkOwnerWantsResources() method.
	 */
	public void chargePlayerForLanding(Player currentPlayer, boolean ownerResponse) {
		if (ownerResponse) {
			if (currentPlayer.getResourceBalance() < costForLanding) {
				currentPlayer.setResourceBalance(0);
				Game.endGame();
			} else {
				currentPlayer.transferResources(componentOwner, costForLanding);
			}
		} else {
			String ownerDeclinesResources = (getComponentOwner().getPlayerName() + " has decided not to request "
					+ Game.RESOURCE_NAME + ".");
			Game.announce(ownerDeclinesResources);
		}
	}

	/**
	 * Increases the cost to purchase a component by the amount passed as a
	 * parameter argument
	 *
	 * @param amountToIncreaseCostToPurchase - the amount by which the cost to
	 *                                       purchase will be increased
	 */
	public void increaseCostToPurchase(int amountToIncreaseCostToPurchase) {
		componentCost += amountToIncreaseCostToPurchase;
	}

	/**
	 * Increases the landing fee of a component by the amount passed as a parameter
	 * argument
	 *
	 * @param amountToIncreaseCostForLanding - the amount by which the cost to
	 *                                       purchase will be increased
	 */
	public void increaseCostOfLanding(int amountToIncreaseCostForLanding) {
		costForLanding += amountToIncreaseCostForLanding;
	}

	/**
	 * Increases the cost to develop a component by the amount passed as a parameter
	 * argument
	 *
	 * @param amountToIncreaseCostToDevelop - the amount by which the cost to develop will be
	 *                                      increased
	 */
	public void increaseCostToDevelop(int amountToIncreaseCostToDevelop) {
		costToDevelop += amountToIncreaseCostToDevelop;
	}

	/**
	 * Increases the component's development stage by 1.
	 */
	public void incrementDevelopmentStage() {
		developmentStage++;
	}

	/**
	 * Sets the component's cost to develop to {@value #MAXIMUM_COST_TO_DEVELOP}
	 * This is intended as a protection when a component has already reached the
	 * maximum development stage.
	 */
	public void setCostToDevelopToMaximum() {
		costToDevelop = MAXIMUM_COST_TO_DEVELOP;
	}

	/**
	 * @return the totalResourcesDevotedToComponent
	 */
	public int getTotalResourcesDevotedToComponent() {
		return totalResourcesDevotedToComponent;
	}

	/**
	 * @param totalResourcesDevotedToComponent the totalResourcesDevotedToComponent
	 *                                         to set
	 */
	public void setTotalResourcesDevotedToComponent(int totalResourcesDevotedToComponent) {
		this.totalResourcesDevotedToComponent = totalResourcesDevotedToComponent;
	}

	/**
	 * Updates the total resources devoted to a component by the value passed as a
	 * parameter argument. totalResourcesDevotedToComponent is intended to be a
	 * counter of all resources expended (including purchases, trades and
	 * developments) spent on this component throughout the game.
	 *
	 * @param numberOfResources - the value by which the total will be updated.
	 */
	public void updateTotalResourcesDevotedToComponent(int numberOfResources) {
		this.totalResourcesDevotedToComponent += numberOfResources;
	}

	/**
	 * Outputs to screen the following information about the component: position,
	 * name, system in which the component is contained, and development stage.
	 */
	public void displaySquarePositionNameSystemAndDevelopmentStage() {
		System.out.printf("%-12s %-40s %-30s %-30s\n", getSquarePosition(), getSquareName(),
				componentSystem.getSystemName(),
				developmentStage + " - " + developmentStageNamesMap.get(this.developmentStage));
	}

	/**
	 * Outputs to screen the following information about the component: name, system and total resources devoted over the course of the game.
	 */
	public void displayNameSystemAndTotalResourcesDevoted() {
		System.out.printf("%-40s %-30s %-30s\n", getSquareName(),
				componentSystem.getSystemName(), totalResourcesDevotedToComponent);
	}

	/**
	 * Displays to screen the total resources devoted to this component across the
	 * current game's lifetime. Intended as a support method for post-game epilogue.
	 */
	public void displayTotalResourcesDevotedToComponent() {
		System.out.println("A total of " + totalResourcesDevotedToComponent + Game.RESOURCE_NAME
				+ " have been devoted to the completion of " + this.getSquareName());
	}

	/**
	 * Checks if a component meets the criteria to permit development i.e. 1) it is
	 * owned by a player 2) its owner also owns the system in which the component is
	 * located, and 3) the development stage is above or equal to
	 * {@value #MINIMUM_DEVELOPMENT_LEVEL} and less than {@value #MAXIMUM_DEVELOPMENT_LEVEL}
	 *
	 * @return - this method returns true if the component can be developed, false
	 * if otherwise.
	 */
	public boolean checkComponentCanBeDeveloped() {
		return (componentOwner != null && componentOwner.equals(componentSystem.getSystemOwner())
				&& developmentStage >= MINIMUM_DEVELOPMENT_LEVEL && developmentStage < MAXIMUM_DEVELOPMENT_LEVEL);
	}

	/**
	 * This method checks if the component is at the maximum development level and
	 * returns a boolean value
	 *
	 * @return - this method returns either true or false
	 */
	public boolean checkFullyDeveloped() {
		return developmentStage == MAXIMUM_DEVELOPMENT_LEVEL;
	}

	/**
	 * @return the developmentStage
	 */
	public int getDevelopmentStage() {
		return developmentStage;
	}

	/**
	 * @param developmentStage the developmentStage to set
	 */
	public void setDevelopmentStage(int developmentStage) {
		this.developmentStage = developmentStage;
	}

	/**
	 * @return the componentCost
	 */
	public int getComponentCost() {
		return componentCost;
	}

	/**
	 * @param componentCost the componentCost to set
	 */
	public void setComponentCost(int componentCost) {
		this.componentCost = componentCost;
	}

	/**
	 * @return the costToDevelop
	 */
	public int getCostToDevelop() {
		return costToDevelop;
	}

	/**
	 * @param costToDevelop the costToDevelop to set
	 */
	public void setCostToDevelop(int costToDevelop) {
		this.costToDevelop = costToDevelop;
	}

	/**
	 * @return the costForLanding
	 */
	public int getCostForLanding() {
		return costForLanding;
	}

	/**
	 * @param costForLanding the costForLanding to set
	 */
	public void setCostForLanding(int costForLanding) {
		this.costForLanding = costForLanding;
	}

	/**
	 * @return the componentOwner
	 */
	public Player getComponentOwner() {
		return componentOwner;
	}

	/**
	 * @param componentOwner the componentOwner to set
	 */
	public void setComponentOwner(Player componentOwner) {
		this.componentOwner = componentOwner;

		if (componentSystem.checkSystemIsOwnedByOnePlayer()) {
			// set component owner as system owner
			componentSystem.setSystemOwner(componentOwner);
			// add system to list of owned systems
			componentOwner.addSystem(componentSystem);
		}

	}

	/**
	 * @return the componentSystem
	 */
	public ArtemisSystem getComponentSystem() {
		return componentSystem;
	}

	/**
	 * @param componentSystem the ArtemisSystem which this Component belongs to
	 */
	public void setComponentSystem(ArtemisSystem componentSystem) {
		this.componentSystem = componentSystem;

		// update the system to include this
		if (!componentSystem.getComponentsInSystem().contains(this)) {
			componentSystem.addComponent(this);
		}
	}

	/**
	 * Determines whether a component already has an owner or not.
	 *
	 * @return true if componentOwned is not null, otherwise return false
	 */
	public boolean isOwned() {
		return (componentOwner != null);
	}

	/**
	 * This method prints to screen key information about this component.
	 * Game.delay() is used to delay each output slightly for readability.
	 */
	@Override
	public void displayAllDetails() {

		int maximumLineLength = 60;

		System.out.println();

		for (int loop = 0; loop < maximumLineLength; loop++) {
			System.out.print("-");
		}

		System.out.println("\nDISPLAYING COMPONENT DETAILS...");
		Game.delay(200);
		System.out.printf("\n%-25s %-20s\n", "COMPONENT NAME:", getSquareName());
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "COMPONENT SYSTEM:", getComponentSystem().getSystemName());
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "BOARD POSITION:", getSquarePosition());
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "DEVELOPMENT STAGE:", getDevelopmentStageName());
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "COMPONENT NAME:", getSquareName());
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "COMPONENT COST:", componentCost);
		if (this.checkFullyDeveloped()) {
			Game.delay(200);
			System.out.printf("%-25s %-20s\n", "COST TO DEVELOP:", "This component is fully developed");
		} else {
			Game.delay(200);
			System.out.printf("%-25s %-20s\n", "COST TO DEVELOP:", costToDevelop);
		}
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "COST FOR LANDING:", costForLanding);
		if (componentOwner != null) {
			Game.delay(200);
			System.out.printf("%-25s %-20s\n", "COMPONENT OWNER:", componentOwner.getPlayerName());
		} else {
			Game.delay(200);
			System.out.printf("%-25s %-20s\n", "COMPONENT OWNER:", "UNOWNED");
		}

		for (int loop = 0; loop < maximumLineLength; loop++) {
			System.out.print("-");
		}

		System.out.println();
		System.out.println();
	}

	/**
	 * @return the developmentStageNamesMap
	 */
	public Map<Integer, DevelopmentStage> getDevelopmentStageNamesMap() {
		return developmentStageNamesMap;
	}


	/**
	 * @return a formatted human readable version of development stage (e.g. 01 - ANALYSING_REQUIREMENTS)
	 */
	public String getDevelopmentStageName() {
		return String.format("%s - %s", developmentStage, developmentStageNamesMap.get(developmentStage));
	}


	/**
	 * @return the name of the component
	 */
	@Override
	public String toString() {
		return getSquareName();
	}
}