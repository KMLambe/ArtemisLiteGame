package artemis.lite;

import java.util.*;

/**
 * Creates and manages the ArtemisLite game.
 *
 * @author Peter McMahon
 * @author Gavin Taylor
 * @author John Young 40030361
 * @author Kieran Lambe 40040696
 */
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

	// player
//	temporarily removed global variables to adjust scope
//    private static Player player;
//    static Player currentPlayer;

	private static List<Player> players = new ArrayList<Player>();

	// board
	private static Board board;
	private static boolean endGame = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		setupGame(scanner);

		gameLoop(scanner);
	}

	/**
	 * Responsible for running the setup sequence, i.e. make sure the board is
	 * created, players have been created, and randomise the player order.
	 */
	public static void setupGame(Scanner scanner) {

		// 1. create board
		board = new Board();

		createBoard(board);

		// 2. get players
		ArrayList<Player> players = createPlayers(scanner);

		// 3. shuffle players
		generatePlayerOrder(players);

		announce("Game setup complete...time to get rolling!");

		// TODO - call mission brief
	}

	/**
	 * Responsible for handling all player interactions with the game.
	 * <p>
	 * It handles each player's turn, enabling them to perform any permissible
	 * action so long as they have more than 0 action points. When the player runs
	 * out of action points, the game moves on to the next player in the sequence.
	 * <p>
	 * At the end of a player's turn, the currentPlayer is set to the next player in
	 * the sequence and their action points are set to the default amount (i.e.
	 * reset).
	 * <p>
	 * The loop will end on any player's turn if a player leaves (setting endGame to
	 * true).
	 *
	 * @param scanner a scanner object
	 */
	public static void gameLoop(Scanner scanner) {
		// set currentplayer to the first player in the arraylist
		Player currentPlayer = players.get(0);

		// AD HOC TEST DATA USED TO TEST DEVELOP COMPONENTS MENU - KL
		// TODO - remove
		/*
		 * currentPlayer.setActionPoints(5); currentPlayer.setResourceBalance(1000);
		 * currentPlayer.purchaseComponent(board.getSquares()[1]);
		 * currentPlayer.purchaseComponent(board.getSquares()[2]);
		 * currentPlayer.purchaseComponent(board.getSquares()[3]);
		 * players.get(1).purchaseComponent(board.getSquares()[3]);
		 */
		// board.getSystems()[0].setSystemOwner(currentPlayer);

		while (currentPlayer.getActionPoints() > 0 && !endGame) {
			int rollDice = rollDice();

			// let everyone know the player has moved
			announce("rolled a " + rollDice + " and moves accordingly.", currentPlayer);
			updatePlayerPosition(currentPlayer, board, rollDice);

			displayMenu(currentPlayer, scanner);

			// current player's turn is over, get the nextPlayer and set to currentPlayer
			// nextPlayer will be the currentPlayer on the next iteration of loop
			currentPlayer = getNextPlayer(players, currentPlayer);
			// make sure player has action points before starting loop
			currentPlayer.setActionPoints(DEFAULT_ACTION_POINTS);
		}

		if (endGame) {
			try {
				endGame(players, currentPlayer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("GAME HAS ENDED");
		}
	}

	/**
	 * Creates a virtual board for players to move around, and populates it with
	 * squares and systems. This method must be called before players are able to
	 * take a turn.
	 *
	 * @param board pass through a board object to store squares and systems
	 * @return the populated board object with squares and systems
	 * @throws IllegalArgumentException if the board object is null
	 */
	private static Board createBoard(Board board) throws IllegalArgumentException {

		if (board == null) {
			throw new IllegalArgumentException("Board object is null");
		}

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
		// systems are created
		board.displayAllSystems();
		board.displayAllSquares();

		return board;
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
				return -1;
			}
		}
		return numberOfPlayers;
	}

	/**
	 * Sets the player names, starting position and starting resources
	 *
	 * @param scanner
	 * @return
	 */
	public static ArrayList<Player> createPlayers(Scanner scanner) {
		int numberOfPlayers;
		do {
			numberOfPlayers = playersInTheGame(scanner);
		} while (numberOfPlayers < 0);

		ArrayList<String> playerNames = new ArrayList<String>(numberOfPlayers);
		ArrayList<Player> players = new ArrayList<>(numberOfPlayers);
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
		setPlayers(players); // updates static List<Player> players (global scope)
		return players;
	}

	/**
	 * Randomises the list of players so that player turns are not based on the
	 * order they were created.
	 *
	 * @param players list of player objects
	 */
	public static void generatePlayerOrder(ArrayList<Player> players) {
		Collections.shuffle(players);
	}

	/**
	 * Returns the next player object in the players list.
	 *
	 * @param players       list of players in the game
	 * @param currentPlayer the current player object
	 * @return player object that represents the next player
	 */
	public static Player getNextPlayer(List<Player> players, Player currentPlayer) {
		return players.get((players.indexOf(currentPlayer) + 1) % players.size());
	}

	/**
	 *
	 */
	public static void cast(Player currentPlayer) {
		ArrayList<Player> players = createPlayers(new Scanner(System.in));
		generatePlayerOrder(players);
		for (Player player : players) {
			System.out.println(player.getPlayerName());
			getNextPlayer(players, player);
		}
	}

	/**
	 * This method simulates the rolling of two dice and returns the sum of the two
	 * values as an integer.
	 *
	 * @return sumOfDice - the sum of the two dice rolls.
	 */
	public static int rollDice() {
		int dice1 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1);
		int dice2 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1);
		int sumOfDice = dice1 + dice2;

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
		if (movementCalculation >= boardLength) {
			allocateResources(currentPlayer);
		}

		// get name of square on which player has landed
		squareName = board.getSquares()[newBoardPosition].getSquareName();

		// announce new board position
		positionChangeAnnouncement = "has landed on " + squareName;
		announce(positionChangeAnnouncement, currentPlayer);

		// check if player landed on owned square
		// TODO - relocate this functionality to a more fitting method
		if (board.getSquares()[newBoardPosition] instanceof Component) {
			Component currentComponent = (Component) board.getSquares()[newBoardPosition];
			if (currentComponent.getComponentOwner() != null && currentComponent.getComponentOwner() != currentPlayer) {
				Scanner scanner = new Scanner(System.in);
				currentComponent.checkOwnerWantsResources(currentPlayer, scanner);
				// scanner.close();
			} else {
				// TODO - offer player chance to take charge of component
			}
		}

	}

	/**
	 * Displays the component the current player has landed on once position is
	 * updated. takes a user response and allows player to purchase component if
	 * user enters yes and component is offered to other players if user enters no.
	 *
	 * @param scanner
	 * @param currentPlayer
	 * @param component
	 * @param board
	 * @param players
	 */
	public static void displayPurchasableComponent(Scanner scanner, Player currentPlayer, Component component,
			Board board, ArrayList<Player> players) {

		String response = null;
		Square[] squares = board.getSquares();
		Square playerPosition = squares[currentPlayer.getCurrentBoardPosition()];

//         purchasableComponents(currentPlayerPosition, board, currentPlayer);

		component.displayAllDetails();
		System.out.println(currentPlayer + " do you want to purchase " + playerPosition + "?");

		do {
			System.out.println("Please enter yes or no");
			response = scanner.next();

			if (response.equalsIgnoreCase("Yes")) {
//                purchaseComponentOption(currentPlayer, board, purchasableComponents, scanner, players);
			} else if (response.equalsIgnoreCase("No")) {
				currentPlayer.offerComponentToOtherPlayers((Component) playerPosition, scanner);
			} else {
				System.out.println("Invalid input");
				System.out.println();
			}
		} while (!(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")));
	}

	/**
	 * Allows the position of the current player to be purchased only if the
	 * component is an instance of a square and component is not owned by another
	 * player
	 *
	 * @param currentPlayer
	 * @param board
	 * @param scanner
	 * @param players
	 * @param component
	 */
	public static void purchaseComponentOption(Player currentPlayer, Board board, Scanner scanner,
			ArrayList<Player> players, Component component) {

		Square[] squares = board.getSquares();
		Square playerPosition = squares[currentPlayer.getCurrentBoardPosition()];

		playerPosition.displayAllDetails();

		for (Square square : board.getSquares()) {
			if (square instanceof Component) {
				component = (Component) square;

				currentPlayer.purchaseComponent(component);
				/*
				 * Note on the below - purchaseComponent already checks if the component is
				 * owned
				 * 
				 * JY: not sure I understand what this method is doing - need to discuss
				 */
//                if (currentPlayer.checkComponentIsNotOwned(component) && purchasableComponents.contains(component)) {
//                } else {
//                    System.out.println("This Component is already owned by " + component.getComponentOwner());
//                    displayMenu(currentPlayer);
//                }
			}
		}

	}

	/**
	 * controls the number of actions a player can take per turn. displays all the
	 * actions applicable to the current player in a menu form. takes a user
	 * response to allow user to select an option from the menu that takes method
	 * calls. Once out of action points or end turn selected the loop will break and
	 * next player will run
	 *
	 * @param currentPlayer takes an arraylist of players
	 */

	public static void displayMenu(Player currentPlayer, Scanner scanner) {
		// TODO - menu needs to be contextual...
		// a) purchase should not show if component is already owned
		// b) some positions are not components so should not be treated as a component
		// (e.g. team bonding)
		// c) trade components should only show if another player owns a component,
		// otherwise it will be blank
		// d) Show all component owners -> the code implemented gets the currentPlayer's
		// components but doesn't do
		// anything with it
		// e) Show resource balance -> the code implemented gets the resources but
		// doesn't do anything with it
		// f) I would not recommend throwing an exception for an invalid input,
		// otherwise you need to catch it and then
		// display the menu again. I'd recommend outputting invalid option then display
		// again
		// g) Develop Component and Develop System - these should show a list of
		// components/systems that the current
		// player can develop. The way it's coded at the minute invokes a development
		// without asking which
		// h) End turn invokes getNextPlayer but this is handled by the game loop. What
		// you should do is break out of
		// the loop, or set actionPoints to zero
		// i) Leave game -> this calls the endGame method, but the endGame method is for
		// dealing when the game is over,
		// but this is a player leaving, it should confirm the player wants to leave and
		// then call a different method
		// that is specific to a player leaving -> endGame will be called by the game
		// loop when conditions are met
		int playerChoice;

		String[] menuOptions = { "...MENU...", "1. Develop Component", "2. Trade components", "3. Display board status",
				"4. Display my components", "5. End turn", "6. Leave game", "Selection..." };

		while (currentPlayer.getActionPoints() > 0 && !endGame) {
			currentPlayer.displayTurnStats();

			// loop through string array and output to screen using method
			for (String option : menuOptions) {
				announce(option, currentPlayer);
			}

			playerChoice = scanner.nextInt();
			System.out.println();

			switch (playerChoice) {
			case 1:
				announce("wants to develop a component they own", currentPlayer);
				displayDevelopComponentMenu(currentPlayer, scanner);
				break;
			case 2:
				subMenuTrade(currentPlayer, scanner);
				break;
			case 3:
				announce("wants to trade resources for another player's component", currentPlayer);
				break;
			case 4:
				announce("wants to see all component owners", currentPlayer); // JY: not sure i understand this one
				break;
			case 5:
				announce("wants to view their resource balance", currentPlayer);
				break;
			case 6:
				announce("has ended their turn", currentPlayer);
				currentPlayer.setActionPoints(0);
				break;
			case 7:
				announce("has left the game", currentPlayer);
				// System.out.println("GAME OVER");
				try {
					endGame(players, currentPlayer);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				announce("Invalid option inputted", currentPlayer);
			}
		}
	}

	/**
	 * Displays components the player can trade for, captures their input and
	 * initiates the trade sequence.
	 *
	 * @param currentPlayer the player object of the current player
	 * @param scanner       a scanner object
	 */
	private static void subMenuTrade(Player currentPlayer, Scanner scanner) {
		announce("you can trade for the following components...\n", currentPlayer);

		Map<Integer, Component> componentsForTrading = getComponentsForTrading(currentPlayer, board);

		displayComponentsForTrading(componentsForTrading);

		String userInput;
		boolean validInput;

		do {
			announce("input the REF of the component you wish to trade for", currentPlayer);
			userInput = scanner.next();

			try {
				validInput = componentsForTrading.containsKey(Integer.parseInt(userInput));
			} catch (NumberFormatException numberFormatException) {
				validInput = false;
			}

			if (!validInput) {
				System.out.println("Invalid input");
			}

		} while (!validInput);

		Component selectedComponent = componentsForTrading.get(Integer.parseInt(userInput));

		// initiate trade with component owner
		currentPlayer.tradeComponent(selectedComponent, scanner);
	}

	/**
	 * Creates an map of components which the active player can purchase. The
	 * components included are subject to a number of constraints: 1) the component
	 * must have an owner 2) the owner must not be the current player 3) the current
	 * player must have sufficient resources to purchase the component
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

				if (component.getComponentOwner() != null && component.getComponentOwner() != player
						&& player.checkSufficientResources(component.getComponentCost())) {
					componentsWithOwners.put(counter++, component);
				}
			}
		}

		return componentsWithOwners;
	}

	/**
	 * Displays components that the currentPlayer does not own but ARE owned by
	 * other players, so long as the currentPlayer has sufficient resources to trade
	 * for them
	 *
	 * @param components a Map containing components available for trading
	 */
	public static void displayComponentsForTrading(Map<Integer, Component> components) {
		Component component;

		System.out.println();

		if (components.size() == 0) {
			System.out
					.println("There are no available components for you to purchase. This is either because you do not"
							+ "have enough resources and/or there are no components owned by other players at present.");
			return;
		}

		System.out.printf("%-5s %-40s %-20s %-4s\n", "REF", "COMPONENT NAME", "OWNER", "COST");

		for (Map.Entry<Integer, Component> componentEntry : components.entrySet()) {
			component = componentEntry.getValue();
			System.out.printf("%-5s %-40s %-20s %-4s\n", componentEntry.getKey(), component,
					component.getComponentOwner(), component.getComponentCost());
		}

	}

	/**
	 * Takes a map object and uses it to prompt the user to select a valid component
	 * to perform an action on.
	 *
	 * @param scanner    a scanner object
	 * @param components a map containing components as the value
	 * @return a component object if a valid selection was made, otherwise will
	 *         return null
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
	 * Outputs a list of components which the player can trade resources for, the
	 * user is then prompted to select one of the list of components. If a valid
	 * selection is made, the purchaseComponent method is invoked.
	 *
	 * @param player  the current player
	 * @param scanner a scanner object
	 */
	public static void displayTradeMenu(Player player, Board board, Scanner scanner) {
		Map<Integer, Component> componentsAvailable = getComponentsForTrading(player, board);

		// display components
		displayComponentsForTrading(componentsAvailable);

		// get user input
		Component playerSelection = getPlayerComponentSelection(scanner, componentsAvailable);

		// player did not select a component - return to main main
		if (playerSelection == null) {
			return;
		}

		System.out.println(player + " has selected to trade with " + playerSelection.getComponentOwner() + " for "
				+ playerSelection);
		// process the trade
		player.tradeComponent(playerSelection, scanner);
	}

	/**
	 * This method prints to screen a menu of components that can be developed
	 * 
	 * @param components - a HashMap of Components with associated Integer keys
	 *                   representing menu numbers
	 */
	public static void displayComponentsPlayerCanDevelop(Map<Integer, Component> components) {

		Component component;

		System.out.println();

		if (components.size() == 0) {
			announce("You do not own any Artemis Systems containing components available for development");
			return;
		}

		System.out.printf("%-5s %-40s %-30s %-20s %-30s %-25s\n", "REF", "COMPONENT NAME", "SYSTEM", "OWNER",
				"DEVELOPMENT STAGE", Game.RESOURCE_NAME + " REQUIRED TO DEVELOP");

		for (Map.Entry<Integer, Component> componentEntry : components.entrySet()) {
			component = componentEntry.getValue();
			System.out.printf("%-5s %-40s %-30s %-20s %-30s %-25s\n", componentEntry.getKey(), component,
					component.getComponentSystem().getSystemName(), component.getComponentOwner(),
					component.getDevelopmentStage() + " - "
							+ component.getDevelopmentStageNamesMap().get(component.getDevelopmentStage()),
					component.getCostToDevelop());
		}

		System.out.println();

	}

	/**
	 * Outputs a list of components the player can develop. The user is then
	 * prompted to select one from the list of components. If a valid selection is
	 * made, the developComponent method is invoked.
	 * 
	 * @param player
	 * @param scanner
	 */
	public static void displayDevelopComponentMenu(Player player, Scanner scanner) {

		Map<Integer, Component> componentsAvailable = player.getOwnedComponentsThatCanBeDeveloped();

		// display components
		displayComponentsPlayerCanDevelop(componentsAvailable);

		// if no components are available then return to menu
		if (componentsAvailable.size() == 0) {
			return;
		}

		// get user input
		Component playerSelection = getPlayerComponentSelection(scanner, componentsAvailable);

		// player did not select a component - return to main main
		if (playerSelection == null) {
			return;
		}

		System.out.println(player + " has decided to develop  " + playerSelection + ".");

		// process the development
		playerSelection.developComponent();
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
	 * Outputs a message to the screen for all players to view.
	 *
	 * @param message       the message to be outputted
	 * @param currentPlayer the player object representing the current player
	 */
	public static void announce(String message, Player currentPlayer) {
		String playerName = String.format("[%s]", currentPlayer.getPlayerName().toUpperCase());
		System.out.printf("%s %s\n", playerName, message);
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
	 * Displays to the players that the game has been won along with stats about the
	 * game
	 *
	 * @param players
	 */
	public static void winGame(ArrayList<Player> players) {

		// As soon as development is complete, announce the path ahead:
		// this will be like a summary of future events at the end of a movie(an
		// epilogue).
		// Display the successful outcome dynamically as a sequence of headlines:
		// e.g. in2021[...], then in 2022 [...]until finally a successful landing is
		// achieved,
		// with congratulations all round! Also give the final state of play that made
		// it possible.

		int totalNumberOfExperts = 0;

		System.out.print("Congratulations ");
		for (int loop = 0; loop < players.size(); loop++) {
			if (loop < players.size() - 1) {
				System.out.print(players.get(loop).getPlayerName() + ", ");
			} else {
				System.out.print("and " + players.get(loop).getPlayerName() + " ");
			}
		}
		System.out.print("the Artemis system has successfully launched.");

		for (ArtemisSystem system : board.getSystems()) {
//            system.displaySystemOwnerForEndGame();
		}

		for (Player player : players) {
			totalNumberOfExperts += player.getResourceBalance();
		}

		System.out.println("There were " + totalNumberOfExperts + " experts needed to launch the Artemis Project.");

		// Summary of future events

		// Adding total number of experts number of experts committed to a component
		// Final state of play, Remaining experts
		// (Total amount of experts taken to win the game, remaining player experts plus
		// all costs
		// the shuttle successfully launched thanks to the work of getPlayer name and
		// their experts

		// KL COMPLETED (PENDING PLAYTESTING): WHO DECLINED RESOURCES ON THE MOST
		// OCCASIONS
		// i.e. who put the needs of the project above their own

		// get the sorted list
		List<Player> listOfPlayersSortedByTimesDeclinedResources = sortPlayersByCounterOfTimesDeclinedResources(
				players);
		// display the sorted list
		displayTimesDeclinedResourcesStats(listOfPlayersSortedByTimesDeclinedResources);

	}

	/**
	 * This post-game method sorts the final list of players according to how many
	 * times they declined resources from other players during the course of the
	 * game. It uses the CompareByCounterOfTimesPlayerDeclinedResources comparator
	 * to perform the sort and returns a sorted ArrayList of Player objects.
	 * 
	 * @param playerList - the list of players at the end of the game
	 * @return sortedList - the sorted list of players, ranked from most times
	 *         declined resources to least times
	 */
	public static List<Player> sortPlayersByCounterOfTimesDeclinedResources(List<Player> playerList) {

		List<Player> sortedList = playerList;

		if (playerList == null) {
			throw new NullPointerException("Error: Player list cannot be null");
		}

		// perform the sort
		if (playerList.size() <= MAXIMUM_PLAYERS && playerList.size() >= MINIMUM_PLAYERS) {
			CompareByCounterOfTimesPlayerDeclinedResources compareByNumberOfTimesDeclinedResources = new CompareByCounterOfTimesPlayerDeclinedResources();
			Collections.sort(playerList, compareByNumberOfTimesDeclinedResources);
		} else {
			System.out.println("Error: The player list must be between " + MINIMUM_PLAYERS + " and " + MAXIMUM_PLAYERS
					+ " (inclusive)");
		}

		return sortedList;

	}

	/**
	 * This post-game method displays to screen all player names alongside a count
	 * of how many times they declined resources over the course of the game.
	 * 
	 * TODO - make more flexible by taking comparator as parameter argument etc.
	 * Could be used to display other endgame stats.
	 * 
	 * @param playerList
	 */
	public static void displayTimesDeclinedResourcesStats(List<Player> playerList) {

		// column headers
		System.out.printf("%-20s %-10s\n", "PLAYER", "NUMBER OF TIMES DECLINED RESOURCES");

		// print stats to screen
		for (Player player : playerList) {
			System.out.printf("%-20s %-10s\n", player.getPlayerName(), player.getCountOfTimesPlayerDeclinedResources());
		}

		// display player(s) who declined most often
		String nameOfPlayersWithMostDeclines = null;
		String messageText, additionalText;
		int mostTimesDeclinedNumber, numberOfPlayersWithHighestNumberOfDeclines, playersAddedToText;
		CompareByCounterOfTimesPlayerDeclinedResources declineResourcesComparator = new CompareByCounterOfTimesPlayerDeclinedResources();

		mostTimesDeclinedNumber = Collections.min(playerList, declineResourcesComparator)
				.getCountOfTimesPlayerDeclinedResources();

		if (mostTimesDeclinedNumber > 0) {
			numberOfPlayersWithHighestNumberOfDeclines = 0;

			for (Player player : playerList) {
				if (player.getCountOfTimesPlayerDeclinedResources() == mostTimesDeclinedNumber) {
					numberOfPlayersWithHighestNumberOfDeclines++;
				}
			}

			playersAddedToText = 0;

			for (int loop = 0; loop < playerList.size(); loop++) {
				Player player = playerList.get(loop);
				if (player.getCountOfTimesPlayerDeclinedResources() == mostTimesDeclinedNumber
						&& nameOfPlayersWithMostDeclines == null) {
					nameOfPlayersWithMostDeclines = player.getPlayerName();
					playersAddedToText++;
				} else if (player.getCountOfTimesPlayerDeclinedResources() == mostTimesDeclinedNumber
						&& nameOfPlayersWithMostDeclines.length() > 0
						&& playersAddedToText == numberOfPlayersWithHighestNumberOfDeclines - 1) {
					additionalText = " and " + player.getPlayerName();
					nameOfPlayersWithMostDeclines += additionalText;
					playersAddedToText++;
				} else if (player.getCountOfTimesPlayerDeclinedResources() == mostTimesDeclinedNumber) {
					additionalText = ", " + player.getPlayerName();
					nameOfPlayersWithMostDeclines += additionalText;
					playersAddedToText++;
				}
			}

			messageText = nameOfPlayersWithMostDeclines + " declined " + RESOURCE_NAME + " on the most occasions.";

			System.out.println("\n" + messageText);
		} else {
			System.out.println("At no point in the game did a player refuse to accept resources");
		}

	}

	/**
	 * This end of game method gives the final state of play when the game has
	 * ended. It displays to screen the final resources available to players before game ended, 
	 * owned components and systems and player who refused to accept resources most.
	 * 
	 * @param players
	 * @param currentPlayer
	 * @throws InterruptedException
	 */
	public static void endGame(List<Player> players, Player currentPlayer) throws InterruptedException {
		
		// TODO create dialogue for game ending
		// TODO why did game end - did player leave by choice or due to lack of resources - complete
		// TODO Show all Components owned before end of game
		// TODO show all systems owned before end of game
		// TODO Final state of play - Player details and remaining experts
		// TODO Who choose to not get resources from other players most - complete
		
		Thread thread = new Thread();
		int totalNumberOfExperts = 0;
		
		// print generic response for mission failure and announce end of game
		announce("Houston... we've had a problem");
		System.out.println("MISSION ABORTED!");
		thread.sleep(1000);

		// loop through the players to find the player with no resources left and print
		// message to screen
		if (currentPlayer.getResourceBalance() <= 0) {
			System.out.println("Mission has failed due to " + currentPlayer + " running out of " + RESOURCE_NAME);
		} else {
			System.out.println(currentPlayer.getPlayerName() + " has chosen to abort the mission");
		}
		thread.sleep(1000);
	
		// loop through players to get resources required to win game
		for (Player player : players) {
			totalNumberOfExperts += player.getResourceBalance();
		}
		System.out.println("There were " + totalNumberOfExperts + " experts needed to launch the Artemis Project.");
		thread.sleep(1000);
		
		System.out.printf("\n%-20s %-10s\n", "PLAYER", "REMAINING RESOURCES");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("%-20s %-10s\n", player.getPlayerName(), player.getResourceBalance());
		}
		thread.sleep(1000);
		
		System.out.printf("\n%-20s %-10s\n", "PLAYER", "OWNED COMPONENTS");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("\n%-20s %-10s\n", player.getPlayerName(), player.getOwnedComponents());
		}
		thread.sleep(1000);
		
		System.out.printf("\n%-20s %-10s\n", "PLAYER", "OWNED SYSTEMS");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("\n%-20s %-10s\n", player.getPlayerName(), player.getOwnedSystems());
		}
		thread.sleep(1000);
		
		// get the sorted list
		System.out.println();
		List<Player> listOfPlayersSortedByTimesDeclinedResources = sortPlayersByCounterOfTimesDeclinedResources(
				players);
		// display the sorted list
		displayTimesDeclinedResourcesStats(listOfPlayersSortedByTimesDeclinedResources);
		
	}

	/**
	 * This accepts a list of player objects and invokes a dice roll for them. It
	 * will return the player with the highest roll of the dice. If multiple players
	 * roll the highest number, it will roll again only for those with the highest
	 * roll. This will continue until only one player is returned.
	 *
	 * @param playersToRoll list of player objects that will be iterated through
	 * @return the player object corresponding to the player with the highest roll
	 * @throws IllegalArgumentException when the list of players inputted is empty
	 */
	public static Player getHighestRoll(List<Player> playersToRoll) throws IllegalArgumentException {
		if (playersToRoll.size() < 1) {
			throw new IllegalArgumentException("Empty list of players");
		} else if (playersToRoll.size() == 1) {
			System.out.println("Only one player passed through...no need to roll the dice");
			return playersToRoll.get(0);
		}

		List<Player> activeList = playersToRoll;
		List<Player> playersWithHighestRoll = new ArrayList<>();
		int highestRoll = -1;
		int playerCounter = 0;
		int roundCounter = 1;

		// -----testing-----
//        highestRoll = 12;
//        playersWithHighestRoll.add(new Player("manualPlayer1", Game.DEFAULT_RESOURCES, Game.STARTING_POSITION));
//        playersWithHighestRoll.add(new Player("manualPlayer2", Game.DEFAULT_RESOURCES, Game.STARTING_POSITION));

		// -----testing-----

		announce("Rolling dice to find out who rolls the highest...");
		do {
			// if already looped through all players then it means more than one player had
			// highest roll
			if (playerCounter >= activeList.size()) {
				System.out.printf("[ROUND %s] There are %s players that rolled a %s\n", roundCounter,
						playersWithHighestRoll.size(), highestRoll);
				System.out.printf("[ROUND %s] We need a winner...let's roll again!\n\n", roundCounter);

				activeList.clear();
				activeList.addAll(playersWithHighestRoll);

				playerCounter = 0; // start again
				highestRoll = -1;
				roundCounter++;
			}

			Player player = activeList.get(playerCounter);

			int playerRoll = Game.rollDice();
			System.out.printf("[ROUND %s] %s rolled a %s\n\n", roundCounter, player.getPlayerName(), playerRoll);

			if (playerRoll > highestRoll) {
				highestRoll = playerRoll;

				playersWithHighestRoll.clear();
				playersWithHighestRoll.add(player);
			} else if (playerRoll == highestRoll) {
				playersWithHighestRoll.add(player);
			}

			playerCounter++;
			//
		} while (!(playersWithHighestRoll.size() == 1 && playerCounter >= activeList.size()
				&& playerCounter >= playersWithHighestRoll.size()));

		// only one player remains
		Player winner = playersWithHighestRoll.get(0);

		announce(winner.getPlayerName() + " wins the roll with a " + highestRoll + " [" + roundCounter + " ROUND(S)]");

		return winner;
	}

	// getters and setters
	public static List<Player> getPlayers() {
		return players;
	}

	public static void setPlayers(List<Player> players) {
		Game.players = players;
	}
}