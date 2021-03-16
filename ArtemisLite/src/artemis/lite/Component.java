/**
 *
 */
package artemis.lite;

import java.util.Scanner;

/**
 * This class facilitates the creation of component objects. This class is a
 * subclass of Square. Component - a square which can be owned by a player.
 *
 * @author Kieran Lambe 40040696, John Young 40030361
 *
 */
public class Component extends Square {

	/**
	 * Constants to define the minimum and maximum development levels for all
	 * components
	 */
	private final int MINIMUM_DEVELOPMENT_LEVEL = 0; // NEW
	private final int MAXIMUM_DEVELOPMENT_LEVEL = 4; // NEW

	private int developmentStage; // NEW
	// private boolean fullyDeveloped; REPLACED by checkFullyDeveloped() method

	private int componentCost;
	private int costToDevelop;
	private int costForLanding;
	private Player componentOwner;
	private ArtemisSystem componentSystem;

	private int costForLandingAtDevelopmentStage0; // NEW - this will be zero
	private int costForLandingAtDevelopmentStage1; // NEW
	private int costForLandingAtDevelopmentStage2; // NEW
	private int costForLandingAtDevelopmentStage3; // NEW
	private int costForLandingAtDevelopmentStage4; // NEW

	private int costToDevelopAtDevelopmentStage0; // NEW
	private int costToDevelopAtDevelopmentStage1; // NEW
	private int costToDevelopAtDevelopmentStage2; // NEW
	private int costToDevelopAtDevelopmentStage3; // NEW
	private int costToDevelopAtDevelopmentStage4; // NEW - this will not be possible in the game (set to huge number?)

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
	}

	/**
	 * This method facilitates the development of the component to the next stage
	 *
	 * @param currentPlayer
	 * @throws IllegalArgumentException
	 */
	public void developComponent(Player currentPlayer) throws IllegalArgumentException {

		if (!this.checkFullyDeveloped() && componentSystem.getSystemOwner() == currentPlayer
				&& currentPlayer.getResourceBalance() >= costToDevelop) {

			// deduct development fee from current player
			currentPlayer.setResourceBalance(currentPlayer.getResourceBalance() - costToDevelop);

			// increase the development level to the next stage
			this.developmentStage++;

			// check new development level and adjust cost for landing
			// and also check and adjust cost to develop
			switch (this.developmentStage) {
			case 1:
				this.setCostForLanding(costForLandingAtDevelopmentStage1);
				this.setCostToDevelop(costToDevelopAtDevelopmentStage1);
				break;
			case 2:
				this.setCostForLanding(costForLandingAtDevelopmentStage2);
				this.setCostToDevelop(costToDevelopAtDevelopmentStage2);
				break;
			case 3:
				this.setCostForLanding(costForLandingAtDevelopmentStage3);
				this.setCostToDevelop(costToDevelopAtDevelopmentStage3);
				break;
			case 4:
				this.setCostForLanding(costForLandingAtDevelopmentStage4);
				this.setCostToDevelop(costToDevelopAtDevelopmentStage4); // Should be impossible to develop here
//				this.setFullyDeveloped(true);
				break;
			default:
				// nothing happens
			}

		} else {
			throw new IllegalArgumentException();
		}

	}

	public boolean checkOwnerWantsResources(Player currentPlayer, Scanner scanner) {
		
		// TODO - implement tests

		String ownerResponse;
		boolean decision = false;
		
		// notify players of cost for landing
		System.out.println("The cost of landing on this component is " + costForLanding);
		System.out.println(this.getComponentOwner().getPlayerName()
				+ " is the owner of this component - meaning they can choose whether they want to take their fee or not!");

		if (currentPlayer.getResourceBalance() < costForLanding) {
			System.out.println("WARNING: If " + componentOwner.getPlayerName() + " decides to request their fee then "
					+ currentPlayer.getPlayerName() + " will run out of " + Game.RESOURCE_NAME.toLowerCase()
					+ " and the game will end!");
		}
		
		

		do {
			// confirm if owner wishes to take their fee
			System.out.println(componentOwner.getPlayerName() + ", do you require " + Game.RESOURCE_NAME.toLowerCase()
					+ " from " + currentPlayer.getPlayerName() + "?");
			System.out.println("Type Yes and press enter if you wish to receive " + Game.RESOURCE_NAME.toLowerCase() + ".");
			System.out
					.println("Type No and press enter if do NOT wish to receive " + Game.RESOURCE_NAME.toLowerCase() + ".");

			ownerResponse = scanner.next();

			if (!ownerResponse.equalsIgnoreCase("Yes") && !ownerResponse.equalsIgnoreCase("No")) {
				System.out.println("Oops - that's not a valid response.");
			} else if (ownerResponse.equalsIgnoreCase("Yes")) {
				decision = true;
			} else {
				decision = false;
			}

		} while (!ownerResponse.equalsIgnoreCase("Yes") && !ownerResponse.equalsIgnoreCase("No"));
		
		// scanner.close();
		
		return decision;
	}

	/**
	 * Deducts the required fee from the current player upon landing on an owned
	 * component
	 *
	 * @param currentPlayer - the current player
	 */
	public void chargePlayerForLanding(Player currentPlayer, boolean ownerResponse) {
		
		// TODO - incorporate announcement method for balance updates
		// TODO - incorporate method to transfer resources

		if (ownerResponse == true) {
			if (currentPlayer.getResourceBalance() < costForLanding) {
				Game.endGame();
			} else {
				currentPlayer.setResourceBalance(currentPlayer.getResourceBalance() - costForLanding);
				getComponentOwner().setResourceBalance(getComponentOwner().getResourceBalance() + costForLanding);
				System.out.println(currentPlayer.getPlayerName() + ", your new resource balance is: "
						+ currentPlayer.getResourceBalance());
				System.out.println(getComponentOwner().getPlayerName() + ", your new resource balance is: "
						+ currentPlayer.getResourceBalance());
			}
		} else {
			System.out.println(getComponentOwner().getPlayerName() + " has decided not to request "
					+ Game.RESOURCE_NAME + ".");
		}
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
	 * DISPLAY ALL DETAILS METHOD TO BE ADDED - WILL OVERRIDE METHOD FROM SUPERCLASS
	 */

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
	
	@Override
    public void displayAllDetails() {
        System.out.println("Name: \t" + this.getSquareName());
        System.out.println("Position: \t" + this.getSquarePosition());
        System.out.println("Development Stage: \t" + developmentStage);
        System.out.println("Component Cost: \t" + componentCost);
        System.out.println("Cost to Develop to Next Stage: \t" + costToDevelop); // TO DO - code around component being fully developed
        System.out.println("Cost for Landing: \t" + costForLanding);
        System.out.println("Component Owner: \t" + componentOwner.getPlayerName());
        System.out.println("Component System: \t" + componentSystem.getSystemName());
    }

}
