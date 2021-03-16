package artemis.lite;

import java.util.*;

public class Game {

    private final static int MINIMUM_PLAYERS = 2;
    private final static int MAXIMUM_PLAYERS = 4;
    private final static int STARTING_POSITION = 0;
    private final static int STARTING_RESOURCES = 500;
    private final static int DEFAULT_RESOURCES = 100;
    public final static int DEFAULT_ACTION_POINTS = 2;
    private final static int MINIMUM_DICE_ROLL = 1;
    private final static int MAXIMUM_DICE_ROLL = 6;
    private final static int NUMBER_OF_DICE = 2;
    public final static int MAXIMUM_SQUARES = 12;
    public final static int MAXIMUM_SYSTEMS = 4;
    public final static int MAXIMUM_NAME_LENGTH = 50;

    // labels - to be used in place of hard coding strings
    public final static String RESOURCE_NAME = "EXPERTS";

    // player array
    private static Player player;
    private static ArrayList<Player> players = new ArrayList<Player>();
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
    private static void createBoard(Board board) {

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

        // TODO - remove below display methods - temporarily here to show squares and
        //  systems are created
        board.displayAllSystems();
        board.displayAllSquares();

    }

	/**
	 * Takes an input from the user to confirm the number of players playing
	 * 
	 * @param scanner
	 * @return
	 */
	public static int playersInTheGame(Scanner scanner) {

		int numberOfPlayers = 0;
		while (numberOfPlayers < MINIMUM_PLAYERS || numberOfPlayers > MAXIMUM_PLAYERS) {
			System.out.println("How many players are there?");
			numberOfPlayers = scanner.nextInt();
			if (numberOfPlayers >= MINIMUM_PLAYERS && numberOfPlayers <= MAXIMUM_PLAYERS) {
				System.out.println("There are " + numberOfPlayers + " players in the game.");
			} else {
				System.out.println("Invalid input please set number of players between 2-4");
			}
		}
		return numberOfPlayers;

	}

	/**
	 * Set the names of each player
	 */
	public static void createPlayers(Scanner scanner) {

		int numberOfPlayers = playersInTheGame(scanner);

		ArrayList<String> playerNames = new ArrayList<String>();
		for (int loop = 1; loop <= numberOfPlayers; loop++) {
			System.out.println("Enter player " + loop + " name");
			String playerName = scanner.next();

			while (playerNames.contains(playerName.toLowerCase())) {
				System.out.println("Player name already exists please enter a different name.");
				playerName = scanner.next();

			}
			playerNames.add(playerName.toLowerCase());
			players.add(new Player(playerName, STARTING_RESOURCES, STARTING_POSITION));
		}
	}

	public static void cast() {
		createPlayers(new Scanner(System.in));
		generatePlayerOrder();
		for (Player player : players) {
			System.out.println(player.getPlayerName());
		}
	}

	/**
	 * Shuffles the order of the players
	 */
	public static void generatePlayerOrder() {
		Collections.shuffle(players);
	}

	/**
	 * This method simulates the rolling of two dice and returns the sum of the two
	 * values as an integer.
	 * 
	 * @return sumOfDice - the sum of the two dice rolls.
	 */
	public static int rollDice() {

		String rollAnnouncement;

		int dice1 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1); // KL - Added use of constants
		int dice2 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1); // KL - Added use of constants
		int sumOfDice = dice1 + dice2;

		rollAnnouncement = "You have rolled " + dice1 + " and " + dice2 + ". Move " + sumOfDice + " spaces."; // KL

		System.out.println(rollAnnouncement); // KL Question: Is this something that would instead be passed to the
												// announcement method discussed previously?

		return sumOfDice;
	}

	/**
	 * This method updates the current player's board position based on the sum of
	 * the two dice rolled during the current turn
	 * 
	 * @param currentPlayer - the current player, passed as a parameter argument
	 * @param sumOfDice     - the sum of two dice returned by the rollDice() method,
	 *                      passed as a parameter argument
	 */
	public static void updatePlayerPosition(Player currentPlayer, Board board, int sumOfDice)
			throws IllegalArgumentException {

		int movementCalculation, newBoardPosition, boardLength;
		String positionChangeAnnouncement, squareName;

		if (currentPlayer == null) {
			throw new IllegalArgumentException("Current player cannot be null");
		} else if (board == null) {
			throw new IllegalArgumentException("Board cannot be null");
		} else if (sumOfDice < MINIMUM_DICE_ROLL * NUMBER_OF_DICE || sumOfDice > MAXIMUM_DICE_ROLL * NUMBER_OF_DICE) {
			throw new IllegalArgumentException("Combined dice roll must be between "
					+ MINIMUM_DICE_ROLL * NUMBER_OF_DICE + " and " + MAXIMUM_DICE_ROLL * NUMBER_OF_DICE);
		}

		boardLength = board.getSquares().length;

		// roll dice
		// currentPlayerDiceRoll = Game.rollDice(); KL - this will be taken as a
		// parameter argument

		movementCalculation = currentPlayer.getCurrentBoardPosition() + sumOfDice;
		newBoardPosition = movementCalculation % boardLength;

		// update player's position
		currentPlayer.setCurrentBoardPosition(newBoardPosition);

		// check if player landed on or passed recruitment
		// allocate resources if condition has been met
		if (movementCalculation > boardLength) {
			allocateResources(currentPlayer);
		}

		// get name of square on which player has landed
		squareName = board.getSquares()[newBoardPosition].getSquareName();

		// announce new board position
		positionChangeAnnouncement = currentPlayer.getPlayerName() + " has landed on " + squareName;
		announce(positionChangeAnnouncement);

		// check if player landed on owned square
		if (board.getSquares()[newBoardPosition] instanceof Component) {
			Component currentComponent = (Component) board.getSquares()[newBoardPosition];
			if (currentComponent.getComponentOwner() != null) {
				Scanner scanner = new Scanner(System.in);
				currentComponent.checkOwnerWantsResources(currentPlayer, scanner);
				scanner.close();
			} else {
				// TO DO - offer player chance to take charge of component
			}
		}

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


    /**
     * Creates an map of components which the active player can purchase. The components included are subject to
     * a number of constraints:
     * 1) the component must have an owner
     * 2) the owner must not be the current player
     * 3) the current player must have sufficient resources to purchase the component
     *
     * @param player the current player
     * @param board  the board object which contains the squares and systems
     * @return an map of identifiers and components that the player can trade for
     */
    public static Map<Integer, Component> getComponentsForTrading(Player player, Board board) {
        Map<Integer, Component> componentsWithOwners = new HashMap<Integer, Component>();
        Component component;

        int counter = 1;

        for (Square square : board.getSquares()) {
            if (square instanceof Component) {
                component = (Component) square;

                if (component.getComponentOwner() != null
                        && component.getComponentOwner() != player
                        && player.checkSufficientResources(component.getComponentCost())) {
                    componentsWithOwners.put(counter++, component);
                }
            }
        }

        return componentsWithOwners;
    }

    /**
     * Displays components that the currentPlayer does not own but ARE owned by other players, so long as the
     * currentPlayer has sufficient resources to trade for them
     *
     * @param componentsForTrading map of integer-component pair values
     */
    public static void outputComponentsForTrading(Map<Integer, Component> componentsForTrading) {
        Component component;

        System.out.println();

        if (componentsForTrading.size() == 0) {
            System.out.println("There are no available components for you to purchase. This is either because you do not" +
                    "have enough resources and/or there are no components owned by other players at present.");
            return;
        }

        System.out.printf("%-5s %-40s %-20s %-4s\n", "REF", "COMPONENT NAME", "OWNER", "COST");

        Set<Map.Entry<Integer, Component>> componentSet = componentsForTrading.entrySet();

        for (Map.Entry<Integer, Component> componentEntry : componentSet) {
            component = componentEntry.getValue();
            System.out.printf("%-5s %-40s %-20s %-4s\n",
                    componentEntry.getKey(), component, component.getComponentOwner(), component.getComponentCost());
        }

    }

    /**
     * Takes a map object and uses it to prompt the user to select a valid component to perform an action on.
     *
     * @param scanner a scanner object
     * @param components a map containing components as the value
     * @return a component object if a valid selection was made, otherwise will return null
     */
    public static Component getPlayerComponentSelection(Scanner scanner, Map<Integer, Component> components) {
        String playerInput;
        int playerSelection = -1;
        Component component;

        do {
            System.out.printf("Input your selection (number only) or type 'end' to go back...");
            playerInput = scanner.next();

            if (playerInput.equalsIgnoreCase("end")) {
                return null;
            }

            try {
                playerSelection = Integer.parseInt(playerInput);
            } catch (NumberFormatException e) {
                // do nothing
            }
            System.out.println();

            component = components.get(playerSelection);
        } while (component == null);

        return component;
    }

    /**
     * Outputs a list of components which the player can trade resources for, the user is then prompted to select one of
     * the list of components. If a valid selection is made, the purchaseComponent method is invoked.
     *
     * @param player the current player
     * @param scanner a scanner object
     */
    public static void displayTradeMenu(Player player, Board board, Scanner scanner) {
        Map<Integer, Component> componentsAvailable = getComponentsForTrading(player, board);

        // display components
        outputComponentsForTrading(componentsAvailable);

        // get user input
        Component playerSelection = getPlayerComponentSelection(scanner, componentsAvailable);

        // player did not select a component - return to main main
        if (playerSelection == null) {
            displayMenu();
            return;
        }

        System.out.println(player+" has selected to trade with "+playerSelection.getComponentOwner()+" for "+playerSelection);
        // process the trade
        player.tradeComponent(playerSelection, scanner);
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

    /**
     * Outputs a message to the screen for all players to view.
     *
     * @param message - the message to be outputted
     */
    public static void announce(String message) {
        System.out.println("----------------------------------");
        System.out.println("ANNOUNCEMENT:");
        System.out.println("\t" + message);
    }

	/**
	 * This method allocates the default number of resources once a player lands on
	 * or passes the Recruitment square
	 *
	 * @param currentPlayer
	 */
	public static void allocateResources(Player currentPlayer) {

		String updatedResourceBalanceAnnouncement;

		if (currentPlayer == null) {
			throw new IllegalArgumentException("Current player cannot be null");
		}

		// add default resources to current player's balance
		currentPlayer.setResourceBalance(currentPlayer.getResourceBalance() + DEFAULT_RESOURCES);

		updatedResourceBalanceAnnouncement = "Recruitment drive! It's time for some fresh ideas. "
				+ currentPlayer.getPlayerName() + " has added " + DEFAULT_RESOURCES + " " + RESOURCE_NAME
				+ " to their team.";

		announce(updatedResourceBalanceAnnouncement);

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
