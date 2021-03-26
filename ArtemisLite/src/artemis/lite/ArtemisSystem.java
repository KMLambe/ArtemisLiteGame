/**
 *
 */
package artemis.lite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Kieran Lambe 40040696, John Young 40030361
 *
 */
public class ArtemisSystem {

    private String systemName;
    private Player systemOwner;
    private ArrayList<Component> componentsInSystem = new ArrayList<>();

    /**
     * Default constructor
     */
    public ArtemisSystem() {

    }

    /**
     * Overloaded constructor that only needs system name to instantiate
     * @param systemName the name of the system
     */
    public ArtemisSystem(String systemName) {
        this.setSystemName(systemName);
    }

    /**
     * @return the systemName
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * @param systemName the systemName to set
     */
    public void setSystemName(String systemName) throws IllegalArgumentException {
        // validate name length
        if (systemName.length() <= 0 || systemName.length() > Game.MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException("Invalid name - must be between 1-" + Game.MAXIMUM_NAME_LENGTH +
                    " characters long");
        }

        this.systemName = systemName;
    }
    
    /**
     * This method checks if all components in this Artemis System are developed 
     * @return - the method returns true if all components are developed, otherwise it returns false
     */
    public boolean checkFullyDeveloped() {
    	
    	int fullyDevelopedCounter = 0;
    	
    	for (Component component : componentsInSystem) {
    		if (component.checkFullyDeveloped()) {
    			fullyDevelopedCounter++;
    		}
    	}
    	
    	return fullyDevelopedCounter == componentsInSystem.size();
    	
    }

    /**
     * @return the systemOwner
     */
    public Player getSystemOwner() {
        return systemOwner;
    }

    /**
     * @param systemOwner the systemOwner to set
     */
    public void setSystemOwner(Player systemOwner) {
        this.systemOwner = systemOwner;
    }

    /**
     * @return the componentsInSystem
     */
    public ArrayList<Component> getComponentsInSystem() {
        return componentsInSystem;
    }

    public void addComponent(Component component) throws IllegalArgumentException {
        if (component == null) {
            throw new IllegalArgumentException("Invalid component");
        }

        // add to arraylist
        componentsInSystem.add(component);
        // update component's system
        if (!component.getComponentSystem().equals(this)) {
            component.setComponentSystem(this);
        }
    }
    
    /**
     * This method checks if all components contained in this Artemis System are owned by a single player and returns the result as a boolean.
     * @return - the method returns true if all components in the system are owned, otherwise it returns false.
     */
    public boolean checkSystemIsOwnedByOnePlayer() {
    	
    	boolean ownedByOnePlayer = false;
    	List<Player> componentOwners = new ArrayList<Player>();
    	
    	int counter = 0;
    	
    	for (Component component : componentsInSystem) {
    		if (component.isOwned()) {
    			// add component owners to list
    			componentOwners.add(component.getComponentOwner());
    			counter++;
    		}
    	}
    	
    	// continue if all components in system are owned
    	if (counter == componentsInSystem.size()) {
    		
        	// convert list to HashSet since all values must be distinct in HashSet
        	HashSet<Player> componentOwnersSet = new HashSet<Player>(componentOwners);
        	
        	// if HashSet contains only one value then one player must own all components in system
        	ownedByOnePlayer = (componentOwnersSet.size()==1);
    	}
    	
    	return ownedByOnePlayer;
    }
    
    /**
     * Method to print out the owner of the system for win game
     */
    public void displaySystemOwnerForEndGame() {
    	System.out.print("\n" + this.systemName + " was managed and fully develop by " + this.systemOwner +" with the help of their experts.");
    }

   /**
    * Displays to screen information about this Artemis System
    */
    public void displayAllDetails() {

        System.out.println("System name:           \t" + systemName);

        if (systemOwner != null) {
            System.out.println("System owner:     \t" + systemOwner.getPlayerName());
        } else {
            System.out.println("System owner:     \t This system is currently unowned.");
        }

        if (checkFullyDeveloped()) {
            System.out.println("Development Status:\t This system is fully developed. That's a big step towards the completion of the Artemis project.");
        } else {

			int totalDeveloped = 0;
			
			for (Component component : componentsInSystem) {
				if (component.checkFullyDeveloped()) {
					System.out.println(component.getSquareName() + " is fully developed.");
					totalDeveloped++;
				} else {
					System.out.println(component.getSquareName() + " is not yet fully developed. It is at development stage: " + component.getDevelopmentStage());
				}
			}
			
			System.out.println(totalDeveloped + " out of " + componentsInSystem.size() + " components in this system are fully developed.");
			
		}
		
		
	}
}