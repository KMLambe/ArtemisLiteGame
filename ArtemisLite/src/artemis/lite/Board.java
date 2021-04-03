package artemis.lite;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a virtual board which contains Systems and Squares. These are stored in an array.
 *
 * @author John Young 40030361
 */
public class Board {

	private final Square[] squares = new Square[Game.MAXIMUM_SQUARES];
	private final ArtemisSystem[] systems = new ArtemisSystem[Game.MAXIMUM_SYSTEMS];
	private List<ArtemisSystem> developedSystems = new ArrayList<ArtemisSystem>();

	/**
	 * Default constructor
	 */
	public Board() {

	}

	/**
	 * Output the name and system of all squares contained within this object.
	 */
	public void displayAllSquares() {
		System.out.printf("%-10s %-40s %-30s %-15s %s\n", "POSITION", "NAME", "SYSTEM", "OWNER", "DEVELOPMENT STAGE");
		for (Square square : squares) {
			if (square != null) {
				String owner = "N/A";
				String system = "N/A";
				String stage = "N/A";

				if (square instanceof Component) {
					Component component = (Component) square;
					owner = (component.getComponentOwner() == null ? "Unowned" : component.getComponentOwner().getPlayerName());
					system = (component.getComponentSystem() == null ? "None" : component.getComponentSystem().getSystemName());
					stage = (component.getDevelopmentStage() < 0 ? "None" : component.getDevelopmentStageName());
				}

				System.out.printf("%-10s %-40s %-30s %-15s %s\n", square.getSquarePosition(), square.getSquareName(),
						system.toUpperCase(), owner.toUpperCase(), stage.toUpperCase());
			}
		}
	}

	/**
	 * Output the name and system of all squares contained within this object.
	 */
	public void displayAllSystems() {
		for (ArtemisSystem system : systems) {
			if (system != null) {
				System.out.println("[" + system.getSystemName() + "] contains " +
						system.getComponentsInSystem().size() + " components");
			}
		}
	}

	/**
	 * Creates a new Square object and adds it to the board's squares array.
	 *
	 * @param squareName - the name of the square
	 * @return - the newly created Square object.
	 */
	public Square createSquare(String squareName) {
		// remove whitespace
		squareName = squareName.trim();

		// loop through array to the first empty element
		for (int index = 0; index < squares.length; index++) {
			// if element is empty then populate and break loop
			if (squares[index] == null) {
				squares[index] = new Square(squareName);
				// update position integer to match array index
				squares[index].setSquarePosition(index);

				return squares[index];
			}

			// if code gets to here then the array is full, i.e. trying to add too many squares
			if (index == squares.length - 1) {
				throw new IllegalArgumentException("Too many squares added - cannot add the square (" + squareName + ")");
			}
		}
		return null;
	}

	/**
	 * Creates a new Component object and adds it to the board's squares array.
	 *
	 * @param componentName  - the name of the component
	 * @param componentCost  - the cost to purchase the component
	 * @param costToDevelop  - the cost to develop the component
	 * @param costForLanding - the cost to another player for landing on this component
	 * @param system         - the object of the ArtemisSystem this component belongs to
	 * @return - the newly created Component object
	 */
	public Square createSquare(String componentName, int componentCost, int costToDevelop,
							   int costForLanding, ArtemisSystem system) {

		if (system == null) {
			throw new IllegalArgumentException("The system passed through is null - cannot create square.");
		}

		// remove whitespace
		componentName = componentName.trim();

		// loop through array to the first empty element
		for (int index = 0; index < squares.length; index++) {
			// if element is empty then populate and break loop
			if (squares[index] == null) {
				squares[index] = new Component(componentName, componentCost, costToDevelop, costForLanding, system);

				// update position integer to match array index
				squares[index].setSquarePosition(index);

				return squares[index];
			}

			// if code gets to here then the array is full, i.e. trying to add too many squares
			if (index == squares.length - 1) {
				throw new IllegalArgumentException("Too many squares added - cannot add square, " + componentName);
			}
		}
		return null;
	}


	/**
	 * Creates a new ArtemisSystem object and adds it to the board's systems array.
	 *
	 * @param systemName - the name of the system
	 * @return an ArtemisSystem object
	 */
	public ArtemisSystem createSystem(String systemName) {
		// remove whitespace
		systemName = systemName.trim();

		// loop through array to the first empty element
		for (int index = 0; index < systems.length; index++) {
			// if element is empty then populate and break loop
			if (systems[index] == null) {
				systems[index] = new ArtemisSystem(systemName);
				return systems[index];
			}

			// if code gets to here then the array is full, i.e. trying to add too many squares
			if (index == systems.length - 1) {
				throw new IllegalArgumentException("Too many systems added - cannot add the system (" + systemName + ")");
			}
		}
		return null;
	}


	/**
	 * @return an array of Square objects.
	 * NOTE: you can add index notation suffix to get a specific element of the array.
	 */
	public Square[] getSquares() {
		return squares;
	}


	/**
	 * @return an array of System objects.
	 * NOTE: you can add index notation suffix to get a specific element of the array.
	 */
	public ArtemisSystem[] getSystems() {
		return systems;
	}

	/**
	 * Adds a fully developed system to the Board class variable fullyDevelopedSystems and makes an announcement to
	 * the Game.
	 *
	 * @param system the system to add (subject to being fully developed)
	 * @return true - if system was added; false - if not
	 * @throws IllegalArgumentException
	 */
	public boolean addDevelopedSystem(ArtemisSystem system) throws IllegalArgumentException {
		if (system == null) {
			throw new IllegalArgumentException("System is null");
		}

		if (system.checkFullyDeveloped() && !developedSystems.contains(system)) {
			developedSystems.add(system);

			int numFullyDevelopedSystems = developedSystems.size();
			Player systemOwner = system.getSystemOwner();

			if (numFullyDevelopedSystems > 0) {
				Game.announce(systemOwner + "'s team of experts has fully developed " +
						system.getSystemName() + "! That's a big step towards completion of the project.");
				Game.announce(String.format("IMPORTANT STATUS UPDATE: %s out of %s systems are now developed", numFullyDevelopedSystems, systems.length));
			}
			return true;
		}
		return false;
	}

	/**
	 * @return a list of ArtemisSystems which are fully developed
	 */
	public List<ArtemisSystem> getDevelopedSystems() {
		return developedSystems;
	}
}

