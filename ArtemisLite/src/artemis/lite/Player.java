package artemis.lite;

import java.util.ArrayList;

/**
 * This implements the Player object for the game. It retains player information
 * and records player owned objects.
 *
 * @author John Young
 */
public class Player {
    private String playerName;
    private int resourceBalance;
    private int currentBoardPosition;
    private int actionPoints;
    private ArrayList<Component> ownedComponents = new ArrayList<>();
    private ArrayList<ArtemisSystem> ownedSystems = new ArrayList<>();


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

    // TODO - additional validation to ensure that this is not already owned - likely dealt with within
    // TODO - game announcement

    /**
     * Updates the player's ownedComponents to add the specified component. This will only add components that do
     * not already exist within the ArrayList.
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
     * Updates the player's ownedComponents to remove the specified component. This will only remove components that
     * exist within the ArrayList.
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

    // TODO - additional validation
    // TODO - game announcement

    /**
     * Add a system to the player's owned systems
     *
     * @param system the object to be added
     * @throws NullPointerException if system is null
     */
    private void addSystem(ArtemisSystem system) throws NullPointerException {
        if (system == null) {
            throw new NullPointerException("Cannot add null component to player's owned systems");
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
            throw new NullPointerException("Cannot remove null component to player's owned systems");
        }

        // can only be removed if it does not already exist
        ownedSystems.remove(system);
    }

    /**
     * Determines whether the component already has an owner
     *
     * @param component to be checked
     * @return true if the component has an owner, as specified by the component's instance var
     */
    private boolean isComponentOwned(Component component) {
        return !component.getComponentOwner().equals(null);
    }

    /**
     * Determines whether the player owns the component
     *
     * @param component the object to be checked
     * @return true if player owns the component
     */
    private boolean checkPlayerOwns(Component component) {
        return (ownedComponents.contains(component));
    }

    /**
     * Determines whether a player has sufficient resources
     *
     * @param resourcesRequired integer value of resources needed
     * @return true if player has resources greater than or equal to that specified
     */
    private boolean checkSufficientResources(int resourcesRequired) {
        return (resourceBalance >= resourcesRequired);
    }

    /**
     * @return true if the player has more than zero actionPoints
     */
    private boolean checkHasActionPoints() {
        return (actionPoints > 0);
    }


    // TODO - check that component is not already owned
    // TODO - check that player does not already own component
    // TODO - check that player has enough resources to purchase
    // TODO - check that player has enough action points to perform action
    // TODO - change ownership of component to this player
    // TODO - update player's ownedComponents to include component
    public boolean purchaseComponent(Component component) {

        return false;
    }

    public boolean tradeComponent(Component component) {
        return false;
    }

    public boolean offerComponentToOtherPlayers(Component component) {
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
     * @return the integer value representing the player's position as part of the Board Squares array
     */
    public int getCurrentBoardPosition() {
        return currentBoardPosition;
    }

    /**
     * Update the player's current position on the board. The position is the element index of the square they
     * currently reside on within the Board squares[] variable.
     *
     * @param currentBoardPosition must be zero or greater, and cannot be greater than the board length-1
     * @throws IllegalArgumentException if currentBoardPosition is less than zero or greater than the board length-1
     */
    public void setCurrentBoardPosition(int currentBoardPosition) throws IllegalArgumentException {
        // TODO - we should put in a restriction that this cannot be greater than length of board squares array
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
     * Sets the number of actions a player can make per turn. This is method is called each time the player turn is
     * invoked.
     *
     * @param actionPoints the integer value representing the number of turns a player can make
     * @throws IllegalArgumentException if actionPoints is less than zero
     */
    public void setActionPoints(int actionPoints) throws IllegalArgumentException {
        // TODO - actionPoints should not exceed the maximum actionPoints per turn
        //  this should be implemented via constant in Game
        if (actionPoints < 0) {
            throw new IllegalArgumentException("Action points value invalid");
        }
        this.actionPoints = actionPoints;
    }

    /**
     * @return an ArrayList of Components owned by the player
     */
    public ArrayList<Component> getOwnedComponents() {
        return ownedComponents;
    }

    /**
     * @return an ArrayList of ArtemisSystems owned by the player
     */
    public ArrayList<ArtemisSystem> getOwnedSystems() {
        return ownedSystems;
    }
}