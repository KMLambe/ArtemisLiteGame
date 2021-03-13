/**
 * 
 */
package artemis.lite;

/**
 * @author Kieran Lambe 40040696
 *
 */
public class ArtemisSystem {

    private String systemName;
    private Player systemOwner;
    private boolean fullyDeveloped;
    private Square[] componentsInSystem;

    /**
     * Default constructor
     */
    public ArtemisSystem() {

    }

    /**
     * Constructor with arguments
     * @param systemName
     */
    public ArtemisSystem(String systemName, Square[] componentsInSystem) {
        this.setSystemName(systemName);
        this.componentsInSystem = componentsInSystem;
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
        if (systemName.length() > 0) {
            this.systemName = systemName;
        } else {
            throw new IllegalArgumentException();
        }

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
     * @return the componentsInSystem
     */
    public Square[] getComponentsInSystem() {
        return componentsInSystem;
    }

    /**
     * @param componentsInSystem the componentsInSystem to set
     */
    public void setComponentsInSystem(Square[] componentsInSystem) {
        this.componentsInSystem = componentsInSystem;
    }

    public void displayAllDetails() {

        System.out.println("System name:\t" + this.systemName);

        if (this.systemOwner.getPlayerName().length() > 0) {
            System.out.println("System owner:\t" + this.systemOwner.getPlayerName());
        } else {
            System.out.println("This system is currently unowned.");
        }

        if (this.fullyDeveloped) {
            System.out.println("This system is fully developed. That's a big step towards the completion of the Artemis project.");
        } else {

			int totalDeveloped = 0;
			
			for (Component component : this.componentsInSystem) {
				if (component.isFullyDeveloped()) {
					System.out.println(component.getSquareName() + " is fully developed.");
					totalDeveloped++;
				} else {
					System.out.println(component.getSquareName() + " is not yet fully developed. It is at development stage: " + component.getDevelopmentStage());
				}
			}
			
			System.out.println(totalDeveloped + " out of " + this.componentsInSystem.length() + " components in this system are fully developed.");
			
		}
		
		
	}
	
	

}
