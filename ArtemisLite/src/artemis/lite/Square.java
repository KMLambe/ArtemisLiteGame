package artemis.lite;

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
	
	/**
	 * represents the system a square is allocated to
	 */
	private ArtemisSystem squareSystem;
	
	
	
	// constructors
	
	/**
	 * default constructor
	 */
	public Square() {
		
	}
	
	
	/**
	 * constructor with args returning squareName and squarePosition
	 * for each object
	 * Business rules applied
	 * @param squareName
	 * @param squarePosition
	 */
	public Square(String squareName, int squarePosition, ArtemisSystem squareSystem) {
		this.setSquareName(squareName);
		this.setSquarePosition(squarePosition);
		this.squareSystem = squareSystem;
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
		
		if (squareName.length() > 0) {
			this.squareName = squareName;
		} else {
			throw new IllegalArgumentException("Invalid Square Name");
		}
	}
	
	
	/**
	 * @return the squarePosition
	 */
	public int getSquarePosition() {
		return squarePosition;
	}
	
	
	/**
	 * Position of square must be within the range of 1-12 inclusive
	 * @param squarePosition the squarePosition to set
	 */
	public void setSquarePosition(int squarePosition) throws IllegalArgumentException {
		
		if ((squarePosition >= 1) && (squarePosition <= 12)) {
			this.squarePosition = squarePosition;
		} else {
			throw new IllegalArgumentException("Invalid Position");
		}
	}
	
	
	/**
	 * @return the squareSystem
	 */
	public ArtemisSystem getSquareSystem() {
		return squareSystem;
	}


	/**
	 * @param squareSystem the squareSystem to set
	 */
	public void setSquareSystem(ArtemisSystem squareSystem) {
		this.squareSystem = squareSystem;
	}
	
	
	/**
	 * displays all details associated with the object
	 */
	public void displayAllDetails() {
		System.out.println("Name \t:" + this.squareName);
		System.out.println("Position \t:" + this.squarePosition);
		
		// this method can be inherited by component class
	
	}
}
