package artemis.lite;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author Peter McMahon, Gavin Taylor
 * 
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

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

    public static void startGame() {
        System.out.println("Mission Brief");
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
        // systems are created
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
     * @param players 
     * shuffles order of players
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

    public static void cast() {
        ArrayList<Player> players = createPlayers(new Scanner(System.in));
        generatePlayerOrder(players);
        for (Player player : players) {
            System.out.println(player.getPlayerName());
        }
            getNextPlayer(players, player);
        
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

    /**
	 * Displays the component the current player has landed on once position is updated.
	 * takes a user response and allows player to purchase component if user enters yes and
	 * component is offered to other players if user enters no.
	 * 
	 * @param scanner
	 * @param currentPlayer
	 * @param component
	 * @param board
	 * @param players
	 * @param purchasableComponents
	 */
	public static void displayPurchasableComponent(Scanner scanner, Player currentPlayer, Component component,
			Board board, ArrayList<Player> players, List<Square> purchasableComponents) {

		String response = null;
		Square[] squares = board.getSquares();
		Square playerPosition = squares[currentPlayer.getCurrentBoardPosition()];

		// purchasableComponents(currentPlayerPosition, board, currentPlayer);

		component.displayAllDetails();
		System.out.println(currentPlayer + " do you want to purchase " + playerPosition + "?");
		System.out.println("Please enter yes or no");
		response = scanner.next();

		do {
			if (response.equalsIgnoreCase("Yes")) {
				purchaseComponentOption(currentPlayer, board, purchasableComponents, scanner, players);
				displayMenu(players);
			} else if (response.equalsIgnoreCase("No")) {
				currentPlayer.offerComponentToOtherPlayers((Component) playerPosition);
				displayMenu(players);
			} else {
				System.out.println("Invalid input - please respond with yes or no");
				response = scanner.next();
				System.out.println();
			}
		} while (!response.equalsIgnoreCase("yes") || (!response.equalsIgnoreCase("no")));

		// displayMenu(players);
	}

	/**
	 * Allows the position of the current player to be purchased only if the component is an
	 * instance of a square and component is not owned by another player
	 * 
	 * @param currentPlayer
	 * @param board
	 * @param purchasableComponents
	 * @param scanner
	 * @param players
	 */
	public static void purchaseComponentOption(Player currentPlayer, Board board, List<Square> purchasableComponents,
			Scanner scanner, ArrayList<Player> players) {

		Square[] squares = board.getSquares();
		Square playerPosition = squares[currentPlayer.getCurrentBoardPosition()];
		Component component;

		// component.displayAllDetails();

		for (Square square : board.getSquares()) {
			if (square instanceof Component) {
				component = (Component) square;

				if (currentPlayer.checkComponentIsNotOwned(component) && purchasableComponents.contains(component)) {
					currentPlayer.purchaseComponent(component);
				} else {
					System.out.println("This Component is already owned by " + component.getComponentOwner());
					displayMenu(players);
				}
			}
		}

	}

	/**
	 * controls the number of actions a player can take per turn.
	 * displays all the actions applicable to the current player in a menu form.
	 * takes a user response to allow user to select an option from the menu that takes
	 * method calls.
	 * Once out of action points or end turn selected the loop will break and 
	 * next player will run
	 * 
	 * @param players takes an arraylist of players
	 */
	public static void displayMenu(List<Player> players) {
		
		// switch statement running through a loop
		System.out.println("1. Develop Component");
		System.out.println("2. Develop System");
		System.out.println("3. Trade components");
		System.out.println("4. Show all component owners");
		System.out.println("5. Show resource balance");
		System.out.println("6. End turn");
		System.out.println("7. Leave game");

		int playerChoice;
		int actionPoints = 2;
		boolean endGame = false;
		Scanner scanner = new Scanner(System.in);
		playerChoice = scanner.nextInt();

		// ignore - put in place to test menu functionality for
		// displayPurchasableComponent
		ArtemisSystem system = (ArtemisSystem) board.getSystems()[1];
		Component component = (Component) board.getSquares()[1];

		while ((actionPoints > 0) && (endGame == false)) {
			switch (playerChoice) {
			case 1:
				//player.develop(component);
				break;
			case 2:
				//player.develop(system);
				break;
			case 3:
				//displayTradeMenu(player, board, scanner);
				break;
			case 4:
				//player.getOwnedComponents();
				break;
			case 5:
				//player.getResourceBalance();
				break;
			case 6:
				endGame(); // outside of loop
				break;
			default:
				throw new IllegalArgumentException("Invalid option - please try again");
			}
		}

		// loop ends run getNextPlayer
		//getNextPlayer(players);
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
            System.out.println("There are no available components for you to purchase. This is either because you do not"
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
     * return null
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

    public static void playTurn() {
        // container for other methods
        rollDice();
//		updatePlayerPosition();
//		displayMenu();

    }

    /**
	 * Displays to the players that the game has been won along with stats about the game
	 */
	public static void winGame(ArrayList<Player> players){
		// As soon as development is complete, announce the path ahead: 
		//this will be like a summary of future events at the end of a movie(an epilogue).  
		//Display the successful outcome dynamically as a sequence of headlines: 
		// e.g. in2021[...], then in 2022 [...]until finally a successful landing is achieved,
		// with congratulations all round!  Also give the final state of play that made it possible.
		
		int totalNumberOfExperts = 0;
		
		
		    System.out.print("Congratulations ");
		for(int loop=0; loop< players.size(); loop++) { 
			if(loop<players.size()-1) {
				System.out.print(players.get(loop).getPlayerName() + ", ");
		} else {
			System.out.print("and " + players.get(loop).getPlayerName() + " ");
		}
		}
			System.out.print("the Artemis system has successfully launched.");
			
			for(ArtemisSystem system: board.getSystems()) {
				system.displaySystemOwnerForEndGame();
			}
			
			for(Player player:players) {
				totalNumberOfExperts += player.getResourceBalance();
			}
			
			System.out.println("There were " +totalNumberOfExperts + " experts needed to launch the Artemis Project.");
			
			
		// Summary of future events 
		
		// Adding total number of experts number of experts committed to a component
		// Final state of play, Remaining experts 
		// (Total amount of experts taken to win the game, remaining player experts plus all costs
		// the shuttle successfully launched thanks to the work of getPlayer name and their experts	
		// Who choose to not get resources from other players most
			
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

    public static void endGame() {
        // needs to terminate loop of the game
        System.out.println("The mission has failed");
    }

    /**
     * This accepts a list of player objects and invokes a dice roll for them. It will return the player with the highest
     * roll of the dice. If multiple players roll the highest number, it will roll again only for those with the highest
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
            // if already looped through all players then it means more than one player had highest roll
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
        } while (!(playersWithHighestRoll.size() == 1 && playerCounter >= activeList.size() && playerCounter >= playersWithHighestRoll.size()));

        // only one player remains
        Player winner = playersWithHighestRoll.get(0);

        announce(winner.getPlayerName() + " wins the roll with a " + highestRoll + " [" + roundCounter +
                " ROUND(S)]");

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
