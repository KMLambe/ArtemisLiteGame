/**
 * 
 */
package artemis.lite;

import java.util.Scanner;

/**
 * This class facilitates the creation of component objects. This class is a
 * subclass of Square. Component - a square which can be owned by a player.
 * 
 * @author Kieran Lambe 40040696
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
	private boolean fullyDeveloped; // NEW

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
	 * @param squareName
	 * @param componentCost
	 * @param costToDevelop
	 * @param costForLanding
	 * @param componentOwner
	 * @param componentSystem
	 */
	public Component(String squareName, int componentCost, int costToDevelop, int costForLanding, Player componentOwner,
			ArtemisSystem componentSystem) {
		super(squareName);
		this.developmentStage = MINIMUM_DEVELOPMENT_LEVEL;
		this.componentCost = componentCost;
		this.costToDevelop = costToDevelop;
		this.costForLanding = costForLanding;
		this.componentOwner = componentOwner;
		this.componentSystem = componentSystem;
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
	}

	/**
	 * @return the componentSystem
	 */
	public ArtemisSystem getComponentSystem() {
		return componentSystem;
	}

	/**
	 * @param componentSystem the componentSystem to set
	 */
	public void setComponentSystem(ArtemisSystem componentSystem) {
		this.componentSystem = componentSystem;
	}

	/**
	 * @return the fullyDeveloped
	 */
	public boolean isFullyDeveloped() {
		return fullyDeveloped;
	}

	/**
	 * @param fullyDeveloped the fullyDeveloped to set
	 */
	public void setFullyDeveloped(boolean fullyDeveloped) {
		this.fullyDeveloped = fullyDeveloped;
	}

	/**
	 * This method facilitates the development of the component to the next stage
	 * 
	 * @param currentPlayer
	 * @throws IllegalArgumentException
	 */
	public void developComponent(Player currentPlayer) throws IllegalArgumentException {

		if (!fullyDeveloped && componentSystem.getSystemOwner() == currentPlayer
				&& currentPlayer.getResourceBalance >= this.costToDevelop) {

			// deduct development fee from current player
			currentPlayer.setResourceBalance(currentPlayer.getResourceBalance - this.costToDevelop);

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
				this.setFullyDeveloped(true);
				break;
			default:
				// nothing happens
			}

		} else {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Deducts the required fee from the current player upon landing on an owned
	 * component
	 * 
	 * @param currentPlayer - the current player
	 */
	public void chargePlayerForLanding(Player currentPlayer) {

		int ownerResponse = 0;

		// notify players of cost for landing
		System.out.println("The cost of landing on this component is " + this.costForLanding);
		System.out.println(this.getComponentOwner().playerName
				+ " is the owner of this component - meaning they can choose whether they want to take their fee or not!");

		if (currentPlayer.getResourceBalance() < this.costForLanding) {
			System.out.println(
					"WARNING: If " + this.getComponentOwner().playerName + " decides to request their fee then "
							+ currentPlayer.getPlayerName() + " will run out of experts and the game will end!");
		}

		do {
			
			// set up scanner to allow user input
			Scanner scanner = new Scanner(System.in);
			
			// confirm if owner wishes to take their fee
			System.out.println(this.getComponentOwner().playerName + ", do you require experts from "
					+ currentPlayer.getPlayerName() + "?");
			System.out.println("Type 1 and press enter if you wish to receive experts.");
			System.out.println("Type 2 and press enter if do NOT wish to receive experts.");

			ownerResponse = scanner.nextInt();

			if (ownerResponse != 1 || ownerResponse != 2) {
				System.out.println("Oops - that's not a valid response.");
			} else if (ownerResponse == 1) {
				if (currentPlayer.getResourceBalance() < this.costForLanding) {
					Game.endGame();
				} else {
					currentPlayer.setResourceBalance(currentPlayer.getResourceBalance() - this.costForLanding);
					this.getComponentOwner().setResourceBalance(this.getComponentOwner().getResourceBalance() + this.costForLanding);
					System.out.println(currentPlayer.getPlayerName + ", your new resource balance is: "
							+ currentPlayer.getResourceBalance());
					System.out.println(this.getComponentOwner().getPlayerName + ", your new resource balance is: "
							+ currentPlayer.getResourceBalance());
				}
			} else if (ownerResponse == 2) {
				System.out.println(this.getComponentOwner().getPlayerName + " has decided not to request experts.");
			}
			
			scanner.close();

		} while (ownerResponse != 1 || ownerResponse != 2);

	}
	
	/**
	 * DISPLAY ALL DETAILS METHOD TO BE ADDED - WILL OVERRIDE METHOD FROM SUPERCLASS
	 */

}
