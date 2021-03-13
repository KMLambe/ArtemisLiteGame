package artemis.lite;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final static int MINIMUM_PLAYERS = 2;
    private final static int MAXIMUM_PLAYERS = 4;
    private final static int DEFAULT_RESOURCES = 100;
    public final static int DEFAULT_ACTION_POINTS = 2;
	private final static int MAXIMUM_DICE_ROLL = 6;
	public final static int MAXIMUM_SQUARES = 12;
	public final static int MAXIMUM_SYSTEMS = 4;
	public final static int MAXIMUM_NAME_LENGTH = 50;

	// player array
	static int[] players = new int[] {};
	// board
	private static Board board; 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}


	public static void startGame() {
		System.out.println("Mission Brief");
		createBoard();
		createPlayers();
		generatePlayerOrder();
	}

	/**
	 *
	 */
	private static void createBoard() {
		board = new Board();

		// create systems
		ArtemisSystem system1 = board.createSystem("SPACE LAUNCH SYSTEM");
		ArtemisSystem system2 = board.createSystem("PRE-STAGING SYSTEM");
		ArtemisSystem system3 = board.createSystem("ORION SPACECRAFT");
		ArtemisSystem system4 = board.createSystem("GATEWAY LUNAR SYSTEM");

		// create squares and components
		board.createSquare("RECRUITMENT");
		// system1
		board.createSquare("CARGO HOLD", 100, 50, 20, system1);
		board.createSquare("EXPLORATION UPPER STAGE & CORE STAGE", 100, 50, 20, system1);
		board.createSquare("SOLID ROCKET BOOSTERS", 100, 50, 20, system1);
		// system2
		board.createSquare("LUNAR ROVERS", 50, 25, 10, system2);
		board.createSquare("SCIENCE EXPERIMENTS", 50, 25, 10, system2);
		// non-system
		board.createSquare("TEAM BONDING");
		// system3
		board.createSquare("CREW MODULE", 150, 75, 35, system3);
		board.createSquare("SERVICE MODULE", 150, 75, 35, system3);
		board.createSquare("LAUNCH ABORT SYSTEM", 150, 75, 35, system3);
		// system4
		board.createSquare("POWER AND PROPULSION ELEMENT", 200, 100, 50, system4);
		board.createSquare("HABITATION AND LOGISTICS OUTPOST", 200, 100, 50, system4);

		// TODO - remove below display methods - temporarily here to show squares and systems are created
		board.displayAllSystems();
		board.displayAllSquares();

	}

	public static void createPlayers() {
		int numberOfPlayers = 0;
		Scanner enterPlayers = new Scanner(System.in);
		while (numberOfPlayers < MINIMUM_PLAYERS || numberOfPlayers > MAXIMUM_PLAYERS) {
			System.out.println("How many players are there?");
			numberOfPlayers = enterPlayers.nextInt();
			if (numberOfPlayers >= MINIMUM_PLAYERS && numberOfPlayers <= MAXIMUM_PLAYERS) {
				System.out.println("There are " + numberOfPlayers + " players in the game.");
			} else {
				System.out.println("Invalid input");
			}
		}

		String Players[] = new String[numberOfPlayers];

		// for(int loop=1; loop<=Players.length; loop++) {
		// System.out.println("Run getPlayerName");
		// }

	}

	public static void generatePlayerOrder() {
		Random rand = new Random();

		for (int i = 0; i < players.length; i++) {
			int randomSwap = rand.nextInt(players.length);
			int temp = players[randomSwap];
			players[randomSwap] = players[i];
			players[i] = temp;
		}
		System.out.println(Arrays.toString(players));

	}

	/**
	 * This method simulates the rolling of two dice
	 * and returns the sum of the two values as an integer.
	 * @return sumOfDice - the sum of the two dice rolls.
	 */
	public static int rollDice() {
		
		String rollAnnouncement;

		int dice1 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1); // KL - Added use of constants
		int dice2 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1); // KL - Added use of constants
		int sumOfDice = dice1 + dice2;
		
		rollAnnouncement = "You have rolled " + dice1 + " and " + dice2 + ". Move " + sumOfDice + " spaces."; // KL

		System.out.println(rollAnnouncement); // KL Question: Is this something that would instead be passed to the announcement method discussed previously?
	
		return sumOfDice;
	}

	/**
	 * This method updates the current player's board position
	 * based on the sum of the two dice rolled during the current turn
	 * @param currentPlayer - the current player, passed as a parameter argument
	 * @param sumOfDice - the sum of two dice returned by the rollDice() method, passed as a parameter argument
	 */
	public static void updatePlayerPosition(Player currentPlayer, int sumOfDice) {
		
		String positionChangeAnnouncement, squareName;
		int movementCalculation, newBoardPosition;
		int boardLength = board.getSquares().length;
		
		// calculate new board position
		movementCalculation = currentPlayer.getCurrentBoardPosition() + sumOfDice;
		newBoardPosition = movementCalculation % boardLength;
		
		// update player's position
		currentPlayer.setCurrentBoardPosition(newBoardPosition);
		
		// get name of square on which player has landed
		squareName = board.getSquares()[newBoardPosition].getSquareName();
		
		// announce new board position
		positionChangeAnnouncement = currentPlayer.getPlayerName() + " has landed on " + squareName;
		System.out.println(positionChangeAnnouncement);
		
	}

	public static void displayMenu() {
		// switch statement running through a loop
		System.out.println("1. Purchase Component");
		System.out.println("2. Decline purchase offer to other players");
		System.out.println("3. Develop Component");
		System.out.println("4. Develop System");
		System.out.println("5. Trade components");
		System.out.println("6. Show all component owners");
		System.out.println("7. Show resource balance");
		System.out.println("8. End turn");
		System.out.println("9. Leave game");

		int playerChoice;
		Scanner scanner = new Scanner(System.in);
		playerChoice = scanner.nextInt();

		// while (actionPoints>0)

		/**
		 * 
		 * switch (playerChoice) { case 1: purchaseComponent; break; case 2:
		 * offerComponentToOtherPlayers; break; case 3: developComponent; break; case 4:
		 * developSystem; break; case 5: tradeComponent; break; case 6;
		 * showAllComponentOwners; break; case 7; getResourceBalance; break; case 8:
		 * getNextPlayer(); break; case 9: endGame(); break; default:
		 * System.out.println("Please enter a valid option"); }
		 */

		// loop ends run getNextPlayer
	}

	public static void playTurn() {
		// container for other methods
		rollDice();
//		updatePlayerPosition();
		displayMenu();

	}

	public static void getNextPlayer() {
		// may need to store a current player variable
		// run a loop to move between the players in the array
		// with an if statement to deal with when the loop moves to the last player in
		// the array

		// or could use a boolean of active player as a var for all players
		// use a loop to change the boolean at the end of a players turn
	}

	public static void allocateResources() {
		// resourceBalance + DEFAULT_RESOURCES
		// run using an if statement after updated player position
		// to see if the player has moved enough to qualify

		// counter if square=start
	}



	/**
	 * @return the players
	 */
	public static int[] getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public static void setPlayers(int[] players) {
		players = players;
	}

	public static void endGame() {
		// needs to terminate loop of the game
		System.out.println("The mission has failed");
	}
}
