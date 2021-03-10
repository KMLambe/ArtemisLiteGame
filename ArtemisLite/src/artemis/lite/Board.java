package artemis.lite;

public class Board {
	
	private Square squares[];
	private System systems[];
	
	/**
	 * 
	 */
	public Board() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	public void createSquares() {
		
		// create squares
		Square start = new Square("Recruitment", 1, ArtemisProjectArea.START);
		Square sq2 = new Square("Cargo Hold", 2, ArtemisProjectArea.PROJECTAREA1);
		Square sq3 = new Square("Exploration Upper Stage + Core Stage", 3, ArtemisProjectArea.PROJECTAREA1);
		Square sq4 = new Square("Solid Rocket Boosters", 4, ArtemisProjectArea.PROJECTAREA1);
		Square sq5 = new Square("Lunar Rovers", 5, ArtemisProjectArea.PROJECTAREA2);
		Square sq6 = new Square("Science Experiments", 6, ArtemisProjectArea.PROJECTAREA2);
		Square bonding = new Square("Team Bonding", 7, ArtemisProjectArea.BONDING);
		Square sq8 = new Square("Crew Member", 8, ArtemisProjectArea.PROJECTAREA3);
		Square sq9 = new Square("Service Module", 9, ArtemisProjectArea.PROJECTAREA3);
		Square sq10 = new Square("Launch Abort System", 10, ArtemisProjectArea.PROJECTAREA3);
		Square sq11 = new Square("Power and Propulsion Element", 11, ArtemisProjectArea.PROJECTAREA4);
		Square sq12 = new Square("Habitation and Logistics Outpost", 12, ArtemisProjectArea.PROJECTAREA4);
		
		// Not sure if Array or Arraylist works better here
		Square[] squares = new Square[] {start, sq2, sq3, sq4, sq5, sq6, bonding, sq8, sq9, sq10, sq11, sq12};
		
		
		/*
		// Thought about an arraylist
		ArrayList<Square> squares = new ArrayList<Square>();
		squares.add(start);
		squares.add(sq2);
		squares.add(sq3);
		squares.add(sq4);
		squares.add(sq5);
		squares.add(sq6);
		squares.add(bonding);
		squares.add(sq8);
		squares.add(sq9);
		squares.add(sq10);
		squares.add(sq11);
		squares.add(sq12);
		
		ArrayList<Square> squareSystems = searchByProjectArea(squares, ArtemisProjectArea.PROJECTAREA1);

		if (squareSystems.isEmpty()) {
			System.out.println("No searches match...");
		} else {
			for (Square square : squares) {
				System.out.println(square.getSquareName());
				System.out.println();
			}
		}
		
		
	}
	
	public static ArrayList<Square> searchByProjectArea(ArrayList<Square> squares, ArtemisProjectArea squareSystem){
		  
	  	ArrayList<Square> searchMatches = new ArrayList<Square>();
			for (Square square : squares) {
			if (square.getSquareSystem() == squareSystem) {
				System.out.println(squareSystem + " " + square.getSquareName());
				System.out.println();
				searchMatches.add(square);
			}
		}

			return searchMatches;

	}
		*/
		
		
	}
	
	
	public void createArtemisSystems() {
		
		// create systems
		ArtemisSystem projectArea1 = new ArtemisSystem("Project Area 1", this.setSquares(sq2));
		
		/*
		 *  Need some advice on this method, I tried the enum way (after looking in to Aidan's train challenge) of allocating a system to a square
		 *  and can see how it works in its simplicity.
		 *  
		 *  From what i can see you could do a searchByProjectArea(ArrayList<Square> squares, ArtemisProjectArea squareSystem) method
		 *  This method however, will only show the project area for the selected system... how would you show all?
		 *  
		 *  I created the enum class of system allocation and created an instance of it in square with a getter and setter
		 *  As you can see from the Square constructor with args i have allocated a system to each square and placed null
		 *  for the squares outside the systems.
		 *  
		 *  The only problem i find is how we know the system is then fully developed and
		 *  how to then complete a major development? Would that be as simple as applying a
		 *  business rule
		 *  
		 *  I seem to be confused also with the relationships here as components in system are yet to be established
		 *  This can be discussed in pair programming session and the reason i chose to work with Kieran first as he has both
		 *  system and component
		 *  
		 *  Also, if you think the way we proposed is better just let me know, just thought i would suggest this...
		 *  Any suggestions let me know please
		 */
		
		
	}
	
	
	/**
	 * @return the squares
	 */
	public Square[] getSquares() {
		return squares;
	}
	
	
	/**
	 * @param squares the squares to set
	 */
	public void setSquares(Square[] squares) {
		this.squares = squares;
	}
	
	
	/**
	 * @return the systems
	 */
	public System[] getSystems() {
		return systems;
	}
	
	
	/**
	 * must be only 4 systems within the board
	 * @param systems the systems to set
	 */
	public void setSystems(System[] systems) throws IllegalArgumentException {
		this.systems = systems;
	}
	
	
}

