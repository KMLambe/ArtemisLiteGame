package artemis.lite;

import java.util.*;

/**
 * Creates and manages the ArtemisLite game.
 *
 * @author Peter McMahon
 * @author Gavin Taylor 40314237
 * @author John Young 40030361
 * @author Kieran Lambe 40040696
 */
public class Game {

	/**
	 * Game constants which are used throughout all classes within the game.
	 */
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
	public final static int MAXIMUM_NAME_LENGTH = 50;        // for systems and components
	public final static int MAXIMUM_PLAYER_NAME_LENGTH = 10;
	public final static String RESOURCE_NAME = "EXPERTS";

	/**
	 * Stores a list of Player objects, for those that are playing the game.
	 */
	private static List<Player> players = new ArrayList<Player>();
	/**
	 * Stores the Board object which will contain all Squares, Components, and Systems that will be used by the Game.
	 */
	private static Board board;
	/**
	 * When winGame is true, this will end the loops inside playTurn and gameLoop resulting in the winGame method
	 * being invoked. The value can only be set within the Game class but can be read anywhere using isEndGame().
	 */
	private static boolean gameLost = false;
	/**
	 * When endGame is true, this will end the loops inside playTurn and gameLoop resulting in the endGame method
	 * being invoked. The value can only be set within the Game class but can be read anywhere using isWinGame().
	 */
	private static boolean gameWon = false;
	/**
	 * Stores a class scanner which can be accessed via any methods inside Game, and in other classes via getScanner().
	 */
	private static Scanner scanner = new Scanner(System.in);

	/**
	 * When testMode=true, all calls to delay() will skip the sleep aspect to speed up testing.
	 */
	private static boolean testMode;

	/**
	 * @param args accepts commandline arguments - however, it does not use them for any purpose
	 */
	public static void main(String[] args) {
		// let game know it is not being run as a test
		testMode = false;

		gameBriefing();

		setupGame();

		gameLoop();

		// clear resources
		if (scanner != null) {
			scanner.close();
		}
	}

	/**
	 * Outputs an overview of the game, rules, and mission - provides context before
	 * jumping into gameplay.
	 */
	private static void gameBriefing() {
		String[] mission = {
				"For over half a century humans have yearned to return to the Moon." +
						"\nOur collective spirit of discovery is undiminished by time." +
						"\nWith the Artemis program, we will land the first woman and next man on the Moon by 2024, using innovative technologies to explore more of the lunar surface than ever before.",
				"\nOUR SUCCESS WILL CHANGE THE WORLD\n",
				"But we can't do it without you.",
				"\nHere's how it works:",
				"\t1. We need " + Game.MINIMUM_PLAYERS + "-" + Game.MAXIMUM_PLAYERS
						+ " resourceful leaders who are willing to WORK TOGETHER",
				"\t2. You will need to travel around our sites (Components and Squares)",
				"\t3. If a component does not have an owner taking responsibility for its development, you will have the opportunity to devote EXPERTS to it. This will make it yours.",
				"\nEXPERTS are the currency of ArtemisLite, handle them with care... if you reach zero, you will be jettisoned for mishandling our future.\n",
				"\t4. If someone already has experts deployed on the site you've landed, you may need to help them out by transferring some of your own experts to their cause. This will be decided by the owner of the component.",
				"\t5. If you land on a square you will not be able to develop it, it's considered communal, so put the feet up and enjoy the team bonding or welcome your newest " + RESOURCE_NAME + "!",
				"\t6. It's import to remember that you are allocated " + Game.DEFAULT_ACTION_POINTS
						+ " action points each turn.",
				"\t7. Action points enable you to perform important tasks, once you use them up your turn will end automatically - so be careful how you use them!",
				"\nACTION POINTS ARE CONSUMED BY: Developing and Trading Components. Purchasing a Component will NOT consume an action point.\n",
				"\t8. If you want to develop a component, you will need to make sure you own all the components within the SAME system.",
				"\t\tIf someone has one you need, you can always offer to trade them for it...REMEMBER: we all win when humanity progresses!",
				"\t9. Each component has 4 stages, meaning it can be developed 3 times. The final development constitutes a MAJOR development, meaning you'll need more " + RESOURCE_NAME + " than usual.",
				"\t10. Once you and your comrades develop ALL the components WITHIN ALL THE SYSTEMS - the Artemis project is completed.",
				"\t\tYou have won the opportunity of a lifetime, front row seats to the next generation of humanity...enjoy it. You will have worked hard for this. ",
				"\t11. BE CAREFUL THOUGH... If any of your comrades feel isolated, they may abandon ship and ALL WILL BE LOST - the game will end.",
				"\t12. DON'T LET ANYONE SUFFER... Take care of your comrades, if they run out of experts, this will result in a brain drain - the game will end.",
				"\nTHIS IS YOUR OPPORTUNITY TO LEAD US ALL...", "\nTAKE FORWARD OUR HOPES AND DREAMS... STRAP IN... ",
				"\nBOOTCAMP IS OVER... Time to get stuck in!", "\nPress the enter key to continue..."};

		for (String output : mission) {
			System.out.printf("%s\n", output);
			delay(800);
		}

		// stop execution until user has pressed enter key
		scanner.nextLine();
	}

	/**
	 * Responsible for running the setup sequence, i.e. make sure the board is
	 * created, players have been created, and randomise the player order.
	 */
	private static void setupGame() {

		// 1. create board object and store it in class var
		setBoard(createBoard());

		// 2. get players
		players = createPlayers();

		// 3. shuffle players
		randomiseOrderOfPlayers();

		announce("Game setup complete...time to get rolling!");
	}

	/**
	 * Creates a virtual board for players to move around, and populates it with
	 * squares and systems. This method must be called before players are able to
	 * take a turn.
	 *
	 * @return the populated board object with squares and systems
	 */
	private static Board createBoard() {
		Board board = new Board();

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
		board.createSquare("POWER AND PROPULSION ELEMENT", 250, 100, 50, system4);
		board.createSquare("HABITATION AND LOGISTICS OUTPOST", 250, 100, 50, system4);

		return board;
	}

	/**
	 * Asks the user to confirm the number of players that will be playing the game. This is used to determine the number
	 * of Player objects that should be created before the game starts.
	 *
	 * @return the number of players that need to be created for the game.
	 */
	public static int confirmNumberOfPlayers() {

		int numberOfPlayers = 0;
		while (numberOfPlayers < MINIMUM_PLAYERS || numberOfPlayers > MAXIMUM_PLAYERS) {
			System.out.println("How many players are there?");
			try {
				numberOfPlayers = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next();
			}

			if (numberOfPlayers >= MINIMUM_PLAYERS && numberOfPlayers <= MAXIMUM_PLAYERS) {
				System.out.printf("There are %s players in the game.\n", numberOfPlayers);
			} else {
				System.out.printf("Invalid input please set number of players between %s-%s\n", MINIMUM_PLAYERS,
						MAXIMUM_PLAYERS);
				return -1;
			}
		}

		return numberOfPlayers;
	}

	/**
	 * Creates a list of players for the game based on input from the user.
	 * <p>
	 * This will invoke confirmNumberOfPlayers() to get the number of players that need to be created. This method will
	 * ask the user to input a name for each player and will then create the relevant player object, adding it to the
	 * players list.
	 * <p>
	 * NOTE: duplicate player names are not accepted. If the same name is entered twice, the user will be prompted to
	 * input a different name.
	 *
	 * @return list of player objects that will take part in the game
	 */
	public static List<Player> createPlayers() {
		int numberOfPlayers;
		do {
			numberOfPlayers = confirmNumberOfPlayers();
		} while (numberOfPlayers < 0);

		System.out.println("Player names can have a maximum of " + MAXIMUM_PLAYER_NAME_LENGTH + " characters.");
		ArrayList<String> playerNames = new ArrayList<String>(numberOfPlayers);
		ArrayList<Player> players = new ArrayList<>(numberOfPlayers);
		for (int loop = 1; loop <= numberOfPlayers; loop++) {
			String playerName;
			do {
				System.out.println("Enter player " + loop + " name");
				playerName = scanner.next();
			} while (playerName.length() > MAXIMUM_PLAYER_NAME_LENGTH);

			while (playerNames.contains(playerName.toLowerCase())) {
				System.out.println("Player name already exists please enter a different name.");
				playerName = scanner.next();
			}
			playerNames.add(playerName.toLowerCase());
			players.add(new Player(playerName, STARTING_RESOURCES, STARTING_POSITION));
		}

		return players;
	}

	/**
	 * Shuffles the order of the players list, this is to ensure the order that players take turns is random and not
	 * based on the order they were created.
	 */
	public static void randomiseOrderOfPlayers() {
		Collections.shuffle(players);
	}

	/**
	 * This method simulates the rolling of two dice and returns the sum of the two
	 * values as an integer.
	 *
	 * @return sumOfDice the sum of the two dice rolls.
	 */
	public static int rollDice() {
		int dice1 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1);
		int dice2 = (int) (Math.random() * MAXIMUM_DICE_ROLL + 1);

		return dice1 + dice2;
	}

	/**
	 * Returns the next player object in the players list.
	 *
	 * @param currentPlayer the current player object
	 * @return player object that represents the next player
	 */
	public static Player getNextPlayer(Player currentPlayer) {
		return players.get((players.indexOf(currentPlayer) + 1) % players.size());
	}

	/**
	 * This method updates the current player's board position based on the sum of
	 * the two dice rolled during the current turn
	 *
	 * @param currentPlayer - the current player, passed as a parameter argument
	 * @param sumOfDice     - the sum of two dice returned by the rollDice() method,
	 * @throws IllegalArgumentException when any of the validation criteria is not met.
	 */
	public static Square updatePlayerPosition(Player currentPlayer, int sumOfDice) throws IllegalArgumentException {

		int movementCalculation, newBoardPosition, boardLength;
		String positionChangeAnnouncement, squareName;

		Board board = getBoard();

		if (currentPlayer == null) {
			throw new IllegalArgumentException("Current player cannot be null");
		} else if (board == null) {
			throw new IllegalArgumentException("Board cannot be null");
		} else if (sumOfDice < MINIMUM_DICE_ROLL * NUMBER_OF_DICE || sumOfDice > MAXIMUM_DICE_ROLL * NUMBER_OF_DICE) {
			throw new IllegalArgumentException("Combined dice roll must be between "
					+ MINIMUM_DICE_ROLL * NUMBER_OF_DICE + " and " + MAXIMUM_DICE_ROLL * NUMBER_OF_DICE);
		}

		boardLength = board.getSquares().length;

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

		if (board.getSquares()[newBoardPosition] instanceof Component) {
			Component component = (Component) board.getSquares()[newBoardPosition];
			positionChangeAnnouncement += " which is part of " + component.getComponentSystem().getSystemName() + ".";
		}

		delay(1000);
		announce(positionChangeAnnouncement, currentPlayer);

		return board.getSquares()[newBoardPosition];
	}

	/**
	 * Allows the position of the current player to be purchased only if the
	 * component is an instance of a square and component is not owned by another
	 * player
	 *
	 * @param currentPlayer takes the current player
	 */
	public static void handlePlayerLanding(Player currentPlayer, Square playerPosition) {

		playerPosition.displayAllDetails();

		if (playerPosition instanceof Component) {
			Component component = (Component) playerPosition;

			if (!component.isOwned()) {
				offerComponentForPurchase(currentPlayer, playerPosition);
			} else if (component.isOwned() && component.getComponentOwner() != currentPlayer) {
				component.checkOwnerWantsResources(currentPlayer);
			} else {
				Game.announce("already owns this component", currentPlayer);
			}
		}
	}

	/**
	 * Outputs a list of components the player can develop. The user is then
	 * prompted to select one from the list of components. If a valid selection is
	 * made, the developComponent method is invoked.
	 *
	 * @param player - the current player
	 */
	public static void displayDevelopComponentMenu(Player player) {

		Map<Integer, Component> componentsAvailable = player.getOwnedComponentsThatCanBeDeveloped();
		List<Component> componentsFullyDeveloped = player.getFullyDevelopedComponents();

		if (componentsFullyDeveloped.size() > 0) {
			displayPlayerFullyDevelopedComponents(player);
		}

		if (componentsAvailable.size() == 0) {
			delay(200);
			Game.announce("does not have any components that meet the criteria for development.", player);
		} else {
			delay(200);
			Game.announce("has " + player.getResourceBalance() + " " + RESOURCE_NAME.toLowerCase()
					+ " to commit to developing a component...", player);
			delay(200);
			Game.announce("has the following components to develop:\n", player);

			// display components
			displayComponentsPlayerCanDevelop(componentsAvailable);

			// if no components are available then return to menu
			if (componentsAvailable.size() == 0) {
				return;
			}

			// get user input
			Component playerSelection = getPlayerComponentSelection(componentsAvailable);

			// player did not select a component - return to main main
			if (playerSelection == null) {
				return;
			}

			// account for scenario in which player lacks resources
			if (player.getResourceBalance() <= playerSelection.getCostToDevelop()) {
				Game.announce("doesn't have enough " + RESOURCE_NAME
						+ " to develop this component right now. Remember you need to keep your number of "
						+ RESOURCE_NAME + " above zero or the game is over!", player);
			} else {
				Game.announce("has decided to develop " + playerSelection, player);
				// process the development
				playerSelection.developComponent();
			}
		}
	}

	/**
	 * Outputs a list of components which the player can trade resources for, the
	 * user is then prompted to select one of the list of components. If a valid
	 * selection is made, the purchaseComponent method is invoked.
	 *
	 * @param player the current player
	 */
	public static void displayTradeMenu(Player player) {
		Map<Integer, Component> componentsAvailable = getComponentsForTrading(player);

		boolean menuDisplayed = displayComponentsForTrading(componentsAvailable);
		System.out.println();

		// menu displayed at least one component
		if (menuDisplayed) {
			// get user input
			Component playerSelection = getPlayerComponentSelection(componentsAvailable);

			// player did not select a component - return to main main
			if (playerSelection == null) {
				return;
			}

			System.out.println(player + " has selected to trade with " + playerSelection.getComponentOwner() + " for "
					+ playerSelection);
			// process the trade
			player.tradeComponent(playerSelection);
		}
	}

	/**
	 * Displays key information about the player's owned components
	 *
	 * @param player the player whose components will be displayed
	 */
	public static void displayPlayerComponents(Player player) {

		if (player.getOwnedComponents().isEmpty()) {
			Game.announce("does not own any components", player);
		} else {
			Game.announce("owns the following components:\n", player);

			System.out.printf("%-12s %-40s %-30s %-30s\n", "POSITION", "NAME", "SYSTEM", "DEVELOPMENT STAGE");

			List<Component> componentList = player.getOwnedComponents();

			Collections.sort(componentList, new CompareByPosition());

			for (Component component : player.getOwnedComponents()) {
				component.displaySquarePositionNameSystemAndDevelopmentStage();
			}
		}
	}

	/**
	 * This method confirms if the current player wishes to leave the game. If the
	 * player inputs yes then game will end and if no then game will continue.
	 *
	 * @param currentPlayer is the player opting to leave
	 * @return boolean to accept true or false conditions
	 */
	public static boolean confirmPlayerWantsToLeave(Player currentPlayer) {

		announce("Are you sure you want to leave the game?", currentPlayer);
		boolean playerResponse = getPlayerConfirmation();

		if (playerResponse) {
			delay(1000);
			announce(currentPlayer + " has chosen to abort the mission");
			return true;
		}

		return false;
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
	public static void playTurn(Player currentPlayer) {
		int playerChoice = -1;
		String[] menuOptions = {"...MENU...", "1. Develop Component", "2. Trade components", "3. Display board status",
				"4. Display my components", "5. End turn", "6. Leave game", "Selection..."};

		while (currentPlayer.getActionPoints() > 0 && !gameLost && !gameWon) {
			currentPlayer.displayTurnStats();

			// loop through string array and output to screen using method
			for (String option : menuOptions) {
				announce(option, currentPlayer);
			}

			try {
				playerChoice = scanner.nextInt();
			} catch (InputMismatchException inputMismatchException) {
				System.out.println("Invalid input - please try again");
				scanner.next();
			} catch (NoSuchElementException noSuchElementException) {
				System.out.println("Please input your selection...");
				scanner.next();
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				System.out.println("There was a problem - please try again");
			}

			System.out.println();

			switch (playerChoice) {
				case 1:
					displayDevelopComponentMenu(currentPlayer);
					// winGame will be set to true when all systems fully developed
					gameWon = checkAllSystemsFullyDeveloped();
					break;
				case 2:
					displayTradeMenu(currentPlayer);
					break;
				case 3:
					board.displayAllSquares();
					break;
				case 4:
					displayPlayerComponents(currentPlayer);
					break;
				case 5:
					announce("has ended their turn", currentPlayer);
					currentPlayer.setActionPoints(0);
					break;
				case 6:
					// endGame will be set to true if player wants to leave
					gameLost = confirmPlayerWantsToLeave(currentPlayer);
					break;
				default:
					announce("Invalid option", currentPlayer);
			}

		}
	}

	/**
	 * Displays to the players that the game has been won along with stats about the game
	 */
	public static void missionSuccessful() {
		int totalNumberOfExperts = 0;
		ArtemisSystem spaceLaunchSystem = board.getSystems()[0];
		ArtemisSystem preStagingSystem = board.getSystems()[1];
		ArtemisSystem orionSpacecraft = board.getSystems()[2];
		ArtemisSystem gatewayLunarSystem = board.getSystems()[3];

		System.out.print("Congratulations ");
		for (int loop = 0; loop < players.size(); loop++) {
			if (loop < players.size() - 1) {
				System.out.print(players.get(loop).getPlayerName() + ", ");
			} else {
				System.out.print("and " + players.get(loop).getPlayerName() + " ");
			}
		}
		System.out.print("the Artemis system has successfully launched.\n");
		delay(2000);

		for (ArtemisSystem system : board.getSystems()) {
			system.displaySystemOwnerForEndGame();
			delay(2000);
		}

		displayTotalNumberOfTurns();
		delay(2000);

		for (Square square : board.getSquares()) {
			if (square instanceof Component) {
				Component component = (Component) square;
				totalNumberOfExperts += component.getTotalResourcesDevotedToComponent();
			}
		}

		for (Player player : players) {
			totalNumberOfExperts += player.getResourceBalance();
		}

		System.out.println(
				"\n\nThere were " + totalNumberOfExperts + " experts needed to launch the Artemis Project.\n");
		delay(2000);
		sortComponentsByTotalResourcesDevoted();

		System.out.println("\n");
		delay(4000);
		List<Player> listOfPlayersSortedByTimesDeclinedResources = sortPlayersByCounterOfTimesDeclinedResources();
		displayTimesDeclinedResourcesStats(listOfPlayersSortedByTimesDeclinedResources);

		System.out.println("\n");
		delay(2000);

		announce("In 2021");
		System.out.println(
				"Artemis I launches with an uncrewed spacecraft providing a glimpse of what the project might hold...\n");
		delay(2000);
		System.out.println(
				"The spacecraft travels 280,000 miles from home, passing the moon and returning to Earth without ever touching down on the lunar surface.");
		delay(2000);
		System.out.println("A taste of things to come, a promise for another day...");
		delay(1000);
		Game.announce("Thanks to " + orionSpacecraft.getSystemOwner()
				+ " and their team of resourceful " + RESOURCE_NAME
				+ ", the " + orionSpacecraft + " proves a fitting vessel for future astronauts daring to venture into space.");
		delay(2000);
		announce("In 2022");
		delay(2000);
		System.out.println(
				"Following the success of the crewless expedition, the brave crew of Artemis II participates in the first manned test flight to check critical systems.");
		delay(2000);
		System.out.println("The spacecraft manages to orbit the Moon and its crew returns home safely.");
		delay(2000);
		announce("In 2024");
		delay(2000);
		System.out.println("The time has come for Artemis III...");
		delay(2000);
		Game.announce("Thanks to the skill of " + spaceLaunchSystem.getSystemOwner() + " and their team of " + RESOURCE_NAME + " who worked tirelessly on " + spaceLaunchSystem + ", the Orion spacecraft is able to safely depart our planet");
		delay(2000);
		System.out.println("The Orion is on its way.");
		System.out.println(
				"The crew members of the Orion are preparing for the moment they have been waiting for...");
		delay(2000);
		System.out.println(
				"The moment they've been waiting for since they first stared up at the night sky in wonder.");
		delay(2000);
		System.out.println(
				"However, before they can land on the lunar surface the Orion needs to match the elliptical orbit of "
						+ gatewayLunarSystem + "...");
		delay(2000);
		Game.announce(gatewayLunarSystem.getSystemOwner() + " and their team of " + RESOURCE_NAME
				+ " watch in anticipation...");
		delay(2000);
		System.out.println("Will their countless hours of work have paid off?");
		delay(2000);
		System.out.println("The Orion safely docks with " + gatewayLunarSystem
				+ " and from there the crew descends towards the lunar surface.");
		delay(2000);
		System.out.println("The world holds its breath.");
		delay(2000);
		System.out.println("Finally, after years of dedication, ingenuity and cooperation...");
		delay(2000);
		System.out.println("After half a century of waiting...");
		delay(3000);
		System.out.println("Humans once again walk on the surface of the moon.");
		delay(2000);
		System.out.println("Among them, the first woman to land on the lunar surface.");
		delay(2000);
		System.out.println("This time we plan to stay a while...");
		delay(2000);
		Game.announce("Thanks to the careful planning of " + preStagingSystem.getSystemOwner()
				+ " and their team of " + RESOURCE_NAME + ", everything the crew needs is in place...");
		delay(2000);
		System.out.println("This includes " + preStagingSystem.getComponentsInSystem().get(0)
				+ " and " + preStagingSystem.getComponentsInSystem().get(1) + ".");
		delay(2000);
		System.out.println(
				"The crew members find themselves on the Moon's South Pole. Never before have humans explored this region of the Moon.");
		delay(2000);
		System.out.println(
				"As they begin their experiments they take a moment to glance back towards home, towards Earth.");
		delay(2000);
		System.out.println("They see our home as so few have seen it.\n");
		delay(3000);
		System.out.println("This would not have been possible without you.\n");
		delay(2000);

		for (Player player : players) {
			delay(2000);
			System.out.println(player.getPlayerName());
			System.out.println();
		}
		delay(2000);
		System.out.println("Thank you for playing ArtemisLite...\n");
		delay(2000);
		Game.announce("Mission accomplished.");
	}

	/**
	 * This gives the final state of play when a player has aborted the mission/a player has run out of resources.
	 * It displays a closing reel, outputting each players resources, components and systems, alongside the player who
	 * turned down the most resources.
	 */
	public static void missionFailed() {

		int totalNumberOfExperts = 0;

		delay(1000);
		announce("Houston... we've had a problem");
		delay(1000);

		Player playerWithNoResources = null;

		for (Player player : players) {
			if (player.getResourceBalance() == 0) {
				playerWithNoResources = player;
			}

			totalNumberOfExperts += player.getResourceBalance();
		}

		if (playerWithNoResources != null) {
			announce("The mission failed because " + playerWithNoResources + " mismanaged their " + Game.RESOURCE_NAME);
		} else {
			announce("The mission was aborted due to one of the crew deciding to abandon ship...");
		}

		System.out.println(totalNumberOfExperts + " " + RESOURCE_NAME + " were still available at teh end and could " +
				"have been put to good use. Unfortunately we will never know what might have been, had they been used.");
		delay(1000);

		System.out.printf("\n%-20s %s\n", "PLAYER", "REMAINING RESOURCES");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("%-20s %s\n", player.getPlayerName(), player.getResourceBalance());
		}
		delay(1000);

		System.out.printf("\n%-20s %s\n", "PLAYER", "OWNED COMPONENTS");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("%-20s %s\n", player.getPlayerName(), player.getOwnedComponents());
		}
		delay(1000);

		System.out.printf("\n%-20s %s\n", "PLAYER", "OWNED SYSTEMS");
		System.out.println("-----------------------------------------");
		for (Player player : players) {
			System.out.printf("%-20s %s\n", player.getPlayerName(), player.getOwnedSystems());
		}
		delay(1000);

		// get the sorted list
		System.out.println();
		List<Player> listOfPlayersSortedByTimesDeclinedResources = sortPlayersByCounterOfTimesDeclinedResources();
		displayTimesDeclinedResourcesStats(listOfPlayersSortedByTimesDeclinedResources);

		announce("GAME ENDED");
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
	 */
	public static void gameLoop() {
		// set currentPlayer to the first player in the arraylist
		Player currentPlayer = players.get(0);

		while (currentPlayer.getActionPoints() > 0 && !gameLost && !gameWon) {
			try {
				announce(String.format("Player %s it's your turn...make it count!", currentPlayer));

				currentPlayer.incrementTurnCounter();

				int rollDice = rollDice();

				// let everyone know the player has moved
				announce("is about to roll the two dice...", currentPlayer);
				delay(2000);
				announce("rolled " + rollDice + " and moves accordingly.", currentPlayer);
				delay(1000);
				Square playerPosition = updatePlayerPosition(currentPlayer, rollDice);

				handlePlayerLanding(currentPlayer, playerPosition);

			} catch (IllegalArgumentException illegalArgumentException) {
				System.out.println(illegalArgumentException.getMessage());
			}

			playTurn(currentPlayer); // outside the above try-catch to prevent user from missing their turn


			// current player's turn is over, get the nextPlayer and set to currentPlayer
			// nextPlayer will be the currentPlayer on the next iteration of loop
			currentPlayer = getNextPlayer(currentPlayer);
			// make sure player has action points before starting loop
			currentPlayer.setActionPoints(DEFAULT_ACTION_POINTS);
		}

		if (gameWon) {
			missionSuccessful();
		} else if (gameLost) {
			missionFailed();
		} else {
			announce("The game ended unexpectedly");
		}
	}


	/**
	 * Displays the component the current player has landed on once position is
	 * updated. takes a user response and allows player to purchase component if
	 * user enters yes and component is offered to other players if user enters no.
	 *
	 * @param currentPlayer  takes the current player
	 * @param playerPosition takes the current board position of the current player
	 */
	public static void offerComponentForPurchase(Player currentPlayer, Square playerPosition) {
		currentPlayer.displayTurnStats();

		if (playerPosition instanceof Component) {
			Component component = (Component) playerPosition;

			if (currentPlayer.getResourceBalance() < component.getComponentCost()) {
				announce("you do not have sufficient resources to purchase " + component, currentPlayer);
				return;
			}

			announce("do you want to purchase " + component + "?", currentPlayer);

			boolean playerResponse = getPlayerConfirmation();

			if (playerResponse) {
				currentPlayer.purchaseComponent(playerPosition);
			} else {
				announce(currentPlayer + " decided not to purchase " + component + ". It will now be offered to other players.");
				currentPlayer.offerComponentToOtherPlayers(component);
			}
		}
	}

	/**
	 * Creates an map of components which the active player can purchase. The
	 * components included are subject to a number of constraints: 1) the component
	 * must have an owner 2) the owner must not be the current player 3) the current
	 * player must have sufficient resources to purchase the component
	 *
	 * @param player the current player
	 * @return an map of identifiers and components that the player can trade for
	 * @throws NullPointerException if the board object is null
	 */
	public static Map<Integer, Component> getComponentsForTrading(Player player) throws NullPointerException {
		Map<Integer, Component> componentsWithOwners = new HashMap<Integer, Component>();
		Component component;

		if (board == null) {
			throw new NullPointerException("Board object has not been created");
		}

		int counter = 1;

		for (Square square : board.getSquares()) {
			if (square instanceof Component) {
				component = (Component) square;

				if (component.isOwned() && component.getComponentOwner() != player
						&& player.checkSufficientResources(component.getComponentCost())) {
					componentsWithOwners.put(counter++, component);
				}
			}
		}

		return componentsWithOwners;
	}

	/**
	 * Takes a map object and uses it to prompt the user to select a valid component
	 * to perform an action on.
	 *
	 * @param components a map containing components as the value
	 * @return a component object if a valid selection was made, otherwise will
	 * return null
	 */
	public static Component getPlayerComponentSelection(Map<Integer, Component> components) {
		String playerInput;
		int playerSelection = -1;
		Component component;

		do {
			System.out.print("Input your selection (number only) or type 'end' to go back...");
			playerInput = scanner.next();

			if (playerInput.equalsIgnoreCase("end")) {
				return null;
			}

			try {
				playerSelection = Integer.parseInt(playerInput);
			} catch (NumberFormatException e) {
				System.out.println("Invalid entry");
			}
			System.out.println();

			component = components.get(playerSelection);
		} while (component == null);

		return component;
	}

	/**
	 * This method allocates the default number of resources once a player lands on
	 * or passes the Recruitment square
	 *
	 * @param currentPlayer the player whose resources will be updated
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
	 * Checks if all systems have been fully developed and sets the Game class variable, winGame = true.
	 * <p>
	 * A system is fully developed when all components with the System have reached the maximum attainable development
	 * stage.
	 *
	 * @return true - if all systems are fully developed; false - if systems are not fully developed.
	 */
	public static boolean checkAllSystemsFullyDeveloped() {
		int totalSystems = board.getSystems().length;
		int totalDevelopedSystems = board.getDevelopedSystems().size();

		return totalSystems == totalDevelopedSystems;
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
			System.out.println("Only one player wants to purchase the component...no need to roll the dice");
			return playersToRoll.get(0);
		}

		List<Player> activeList = playersToRoll;
		List<Player> playersWithHighestRoll = new ArrayList<>();
		int highestRoll = -1;
		int playerCounter = 0;
		int roundCounter = 1;

		announce("Rolling dice to find out who rolls the highest...");
		do {
			// if already looped through all players then it means more than one player had
			// highest roll
			if (playerCounter >= activeList.size()) {
				System.out.printf("[ROUND %s] There are %s players that rolled a %s", roundCounter,
						playersWithHighestRoll.size(), highestRoll);
				System.out.printf("[ROUND %s] We need a winner...let's roll again!\n", roundCounter);

				activeList.clear();
				activeList.addAll(playersWithHighestRoll);

				playerCounter = 0; // start again
				highestRoll = -1;
				roundCounter++;
			}

			Player player = activeList.get(playerCounter);

			int playerRoll = Game.rollDice();
			System.out.printf("[ROUND %s] %s rolled %s\n", roundCounter, player.getPlayerName(), playerRoll);

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

	/**
	 * Prompts user to input yes or no to the question posed. This will continue to loop until a valid response is
	 * received.
	 *
	 * @return true if the user inputs yes; false if no.
	 */
	public static boolean getPlayerConfirmation() {
		String playerResponse;

		do {
			System.out.println("Please input yes or no...");
			playerResponse = scanner.next();

		} while (!playerResponse.equalsIgnoreCase("yes") && !playerResponse.equalsIgnoreCase("no"));

		return playerResponse.equalsIgnoreCase("yes");
	}

	/**
	 * Invokes Thread.sleep to pause programme execution for the specified number of milliseconds. This will only
	 * execute if the game is not in test mode, otherwise, the code does not execute (i.e. Thread.sleep is not invoked).
	 * <p>
	 * This method also handles the exceptions so that try-catch blocks are not required each time.
	 *
	 * @param milliseconds the number of milliseconds to pause code execution
	 */
	public static void delay(long milliseconds) {
		if (testMode) {
			return;
		}

		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException interruptedException) {
			System.out.println("Interrupted");
		}
	}

	/**
	 * Outputs a message to the screen for all players to view.
	 *
	 * @param message - the message to be outputted
	 */
	public static void announce(String message) {

		int borderLength = message.length();

		System.out.println();

		for (int loop = 0; loop < borderLength; loop++) {
			System.out.print("-");
		}

		System.out.println("\n" + message);

		for (int loop = 0; loop < borderLength; loop++) {
			System.out.print("-");
		}

		System.out.println();
		System.out.println();
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
	 * Displays components that the currentPlayer does not own but ARE owned by
	 * other players, so long as the currentPlayer has sufficient resources to trade
	 * for them
	 *
	 * @param components a Map containing components available for trading
	 * @return true if there are components to display, otherwise display false
	 */
	public static boolean displayComponentsForTrading(Map<Integer, Component> components) {
		Component component;

		System.out.println();

		if (components.size() > 0) {
			System.out.printf("%-5s %-40s %-30s %-15s %-30s %s\n", "REF", "COMPONENT NAME", "SYSTEM", "OWNER",
					"DEVELOPMENT STAGE", "COST");

			for (Map.Entry<Integer, Component> componentEntry : components.entrySet()) {
				component = componentEntry.getValue();
				System.out.printf("%-5s %-40s %-30s %-15s %-30s %s\n", componentEntry.getKey(), component,
						component.getComponentSystem().getSystemName(), component.getComponentOwner(),
						component.getDevelopmentStageName(), component.getComponentCost());
			}

			return true;
		}

		Game.announce("There are no components available to trade resources for. This is either because you do not "
				+ "have enough resources and/or there are no components owned by other players at present.");
		return false;
	}

	/**
	 * This method prints to screen a menu of components that can be developed to
	 * the next stage.
	 *
	 * @param components - a HashMap of Components with associated Integer keys
	 *                   representing menu numbers
	 */
	public static void displayComponentsPlayerCanDevelop(Map<Integer, Component> components) {

		try {
			Component component;
			System.out.println();
			delay(500);
			if (components.size() == 0) {
				announce("You do not own any Artemis Systems containing components available for development");
				return;
			}
			delay(500);
			System.out.printf("%-5s %-40s %-30s %-20s %-30s %-25s\n", "REF", "COMPONENT NAME", "SYSTEM", "OWNER",
					"DEVELOPMENT STAGE", Game.RESOURCE_NAME + " REQUIRED TO DEVELOP");
			delay(200);
			for (Map.Entry<Integer, Component> componentEntry : components.entrySet()) {
				component = componentEntry.getValue();
				System.out.printf("%-5s %-40s %-30s %-20s %-30s %-25s\n", componentEntry.getKey(), component,
						component.getComponentSystem().getSystemName(), component.getComponentOwner(),
						component.getDevelopmentStage() + " - "
								+ component.getDevelopmentStageNamesMap().get(component.getDevelopmentStage()),
						component.getCostToDevelop());
			}
			System.out.println();

		} catch (NullPointerException nullPointerException) {
			System.out.println("Error: Component map cannot be null");
		}
	}

	/**
	 * Outputs to screen key information about the components the player has developed to the maximum stage.
	 *
	 * @param player - the current player.
	 */
	public static void displayPlayerFullyDevelopedComponents(Player player) {

		List<Component> fullyDevelopedComponents = player.getFullyDevelopedComponents();

		if (fullyDevelopedComponents.isEmpty()) {
			delay(200);
			Game.announce("has not fully developed any components yet.", player);
		} else {
			delay(200);
			Game.announce("has fully developed the following components so far:\n", player);
			delay(200);
			System.out.printf("%-12s %-40s %-30s %-30s\n", "POSITION", "NAME", "SYSTEM", "DEVELOPMENT STAGE");

			fullyDevelopedComponents.sort(new CompareByPosition());

			for (Component component : fullyDevelopedComponents) {
				component.displaySquarePositionNameSystemAndDevelopmentStage();
			}

			System.out.println();
		}
	}

	/**
	 * Adds the total amount of turns taken in the game and gives the average number of turns to develop each system
	 */
	public static void displayTotalNumberOfTurns() {
		int totalNumberOfTurns = 0;
		double averageTurnsToUpgradeASystem;
		for (Player player : players) {
			totalNumberOfTurns += player.getTurnCounter();
		}

		System.out.println("\n\nIt has taken a combined effort of " + totalNumberOfTurns + " turns to successfully launch the system.");
		averageTurnsToUpgradeASystem = (double) totalNumberOfTurns / board.getSystems().length;
		System.out.printf("There was an average of %.2f turns to complete each system.", averageTurnsToUpgradeASystem);
	}

	/**
	 * Lists all components with the number of resources that were devoted to each.
	 *
	 * @param componentList list of components
	 */
	public static void displayAllComponentsNameSystemResourcesDevoted(List<Component> componentList) {

		delay(500);
		System.out.printf("%-40s %-30s %-30s\n", "COMPONENT", "SYSTEM", "TOTAL " + Game.RESOURCE_NAME + " DEVOTED");

		for (Component component : componentList) {
			delay(500);
			component.displayNameSystemAndTotalResourcesDevoted();
		}
	}

	/**
	 * This post-game method displays to screen all player names alongside a count
	 * of how many times they declined resources over the course of the game.
	 * <p>
	 * Could be used to display other endgame stats.
	 *
	 * @param playerList list of players in the game
	 */
	public static void displayTimesDeclinedResourcesStats(List<Player> playerList) {

		// column headers
		delay(1000);
		System.out.printf("%-20s %-10s\n", "PLAYER", "NUMBER OF TIMES DECLINED RESOURCES");

		// print stats to screen
		for (Player player : playerList) {
			delay(500);
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
			delay(1000);
			System.out.println("\n" + messageText);
		} else {
			delay(1000);
			System.out.println("\nAt no point in the game did a player refuse to accept resources");
		}

	}

	/**
	 * Sorts the board's components according to the number of resources devoted.
	 */
	public static void sortComponentsByTotalResourcesDevoted() {

		List<Component> gameComponents = new ArrayList<>();

		for (Square square : board.getSquares()) {
			if (square instanceof Component) {
				Component component = (Component) square;
				gameComponents.add(component);
			}
		}

		Collections.sort(gameComponents, new CompareByCounterOfResourcesDevoted());

		displayAllComponentsNameSystemResourcesDevoted(gameComponents);

	}

	/**
	 * This post-game method sorts the final list of players according to how many
	 * times they declined resources from other players during the course of the
	 * game. It uses the CompareByCounterOfTimesPlayerDeclinedResources comparator
	 * to perform the sort and returns a sorted ArrayList of Player objects.
	 *
	 * @return sortedList - the sorted list of players, ranked from most times
	 * declined resources to least times
	 */
	public static List<Player> sortPlayersByCounterOfTimesDeclinedResources() {

		List<Player> sortedList = players;

		if (players == null) {
			throw new NullPointerException("Error: Player list cannot be null");
		}

		// perform the sort
		if (players.size() <= MAXIMUM_PLAYERS && players.size() >= MINIMUM_PLAYERS) {
			CompareByCounterOfTimesPlayerDeclinedResources compareByNumberOfTimesDeclinedResources = new CompareByCounterOfTimesPlayerDeclinedResources();
			Collections.sort(players, compareByNumberOfTimesDeclinedResources);
		} else {
			System.out.println("Error: The player list must be between " + MINIMUM_PLAYERS + " and " + MAXIMUM_PLAYERS
					+ " (inclusive)");
		}

		return sortedList;

	}

	// getters and setters

	/**
	 * Used to access the list of players outside of the Game object.
	 *
	 * @return list of player objects
	 */
	public static List<Player> getPlayers() {
		return players;
	}

	/**
	 * Set the value of the players variable.
	 *
	 * @param players a list of player objects
	 */
	public static void setPlayers(List<Player> players) {
		Game.players = players;
	}

	/**
	 * Used to access the board object outside of the Game object.
	 *
	 * @return the virtual board being used
	 */
	public static Board getBoard() {
		if (board == null) {
			createBoard();
		}
		return board;
	}

	/**
	 * Sets the value of the board variable.
	 *
	 * @param board should be the board object that contains the virtual board
	 *              (squares and systems)
	 */
	public static void setBoard(Board board) {
		Game.board = board;
	}


	/**
	 * @return the value of testMode
	 */
	public static boolean isTestMode() {
		return testMode;
	}

	/**
	 * Sets the value of testMode. When in testMode all code delays are ignored to enable testing to run quickly.
	 *
	 * @param testMode true -> game is being run in test mode. false -> game is being run normally.
	 */
	public static void setTestMode(boolean testMode) {
		Game.testMode = testMode;
	}

	/**
	 * @return the scanner object
	 */
	public static Scanner getScanner() {
		return scanner;
	}

	/**
	 * Set the scanner object.
	 *
	 * @param scanner must be a scanner object
	 */
	public static void setScanner(Scanner scanner) {
		Game.scanner = scanner;
	}

	/**
	 * @return the value of endGame
	 */
	public static boolean isGameLost() {
		return gameLost;
	}

	/**
	 * This will set the value of gameLost - which will end the game if set to true.
	 * A game will be lost for one of two reasons: 1) a player runs out of resources; 2) a player leaves the game.
	 *
	 * @param gameLost true - if the game has been lost
	 */
	public static void setGameLost(boolean gameLost) {
		Game.gameLost = gameLost;
	}

	/**
	 * This will set the value of gameWon. The purpose of this setter is to reset the gameWon value after before each
	 * test.
	 *
	 * @param gameWon true - if the game has been won
	 */
	public static void setGameWon(boolean gameWon) {
		Game.gameWon = gameWon;
	}

	/**
	 * @return the value of winGame
	 */
	public static boolean isGameWon() {
		return gameWon;
	}
}
