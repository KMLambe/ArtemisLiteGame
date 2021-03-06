package artemis.lite;

/**
 * Base class for virtual board square. This object can only contain a name and position.
 *
 * @author John Young 40030361
 */
public class Square {

	// instance vars

	/**
	 * holds a value of type string for square name
	 */
	private String squareName;

	/**
	 * holds a value of type string for square position
	 */
	private int squarePosition;


	// constructors

	/**
	 * default constructor
	 */
	public Square() {

	}

	/**
	 * Constructor for creating square object with only the name of the square
	 *
	 * @param squareName the name of the square which will be shown to the player.
	 */
	public Square(String squareName) {
		this.setSquareName(squareName);
	}

	// methods

	/**
	 * @return the squareName
	 */
	public String getSquareName() {
		return squareName;
	}


	/**
	 * @param squareName the squareName to set
	 */
	public void setSquareName(String squareName) throws IllegalArgumentException {
		// validate name length
		if (squareName.length() <= 0 || squareName.length() > Game.MAXIMUM_NAME_LENGTH) {
			throw new IllegalArgumentException("Invalid name - must be between 1-" + Game.MAXIMUM_NAME_LENGTH +
					" characters long");
		}

		this.squareName = squareName;
	}


	/**
	 * @return the squarePosition
	 */
	public int getSquarePosition() {
		return squarePosition;
	}


	/**
	 * Position of square must be within the range of 1-12 inclusive
	 *
	 * @param squarePosition the squarePosition to set
	 */
	public void setSquarePosition(int squarePosition) throws IllegalArgumentException {

		if ((squarePosition >= 0) && (squarePosition <= Game.MAXIMUM_SQUARES - 1)) {
			this.squarePosition = squarePosition;
		} else {
			throw new IllegalArgumentException("Invalid Position");
		}
	}

	/**
	 * Displays all details associated with the object.
	 * Game.delay() is used to delay each output slightly for the benefit of readability for the end-user.
	 */
	public void displayAllDetails() {
		int maximumLineLength = 60;

		System.out.println();

		for (int loop = 0; loop < maximumLineLength; loop++) {
			System.out.print("-");
		}

		System.out.println("\nDISPLAYING SQUARE DETAILS...");
		Game.delay(200);
		System.out.printf("\n%-25s %-20s\n", "SQUARE NAME:", squareName);
		Game.delay(200);
		System.out.printf("%-25s %-20s\n", "SQUARE POSITION:", squarePosition);

		for (int loop = 0; loop < maximumLineLength; loop++) {
			System.out.print("-");
		}

		System.out.println();
		System.out.println();
	}

	/**
	 * @return the name of the square
	 */
	@Override
	public String toString() {
		return squareName;
	}
}
