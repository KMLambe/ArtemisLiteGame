package artemis.lite;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final static int MINIMUM_PLAYERS = 2;
    private final static int MAXIMUM_PLAYERS = 4;
    private final static int DEFAULT_RESOURCES = 100;
    public final static int DEFAULT_ACTION_POINTS = 2;

    //player array
    static int[] players = new int[]{};
    //board

    /**
     * @param args
     */
    public static void main(String[] args) {

    }


		public static void startGame() {
			System.out.println("Mission Brief");
			createPlayers();
			generatePlayerOrder();
		}
		
		public static void createPlayers() {
			int numberOfPlayers=0;
			Scanner enterPlayers = new Scanner(System.in);
			while (numberOfPlayers<MINIMUM_PLAYERS || numberOfPlayers>MAXIMUM_PLAYERS) {
				System.out.println("How many players are there?");
			numberOfPlayers=enterPlayers.nextInt();
			if (numberOfPlayers>=MINIMUM_PLAYERS && numberOfPlayers<=MAXIMUM_PLAYERS){
				System.out.println("There are "+numberOfPlayers + " players in the game.");
			} else {
				System.out.println("Invalid input");
			}
		    }
			
			
			String Players[] = new String[numberOfPlayers];
			
			//for(int loop=1; loop<=Players.length; loop++) {
			//System.out.println("Run getPlayerName");
		//}
			
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
		
		 public static void rollDice() {

		    int dice1=(int)(Math.random()*6+1);
		    int dice2=(int)(Math.random()*6+1);
		    int sumOfDice= dice1 + dice2;

		    System.out.println("You have rolled " +dice1 +" and " +dice2 + ". Move " + sumOfDice + " spaces"); 
		 }
		 
		 public static void updatePlayerPosition() {
			 // currentBoardPosition + sumOfDice
			 //System.out.println("You have landed on " + currentBoardPosition);
			 //modulus operator
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
			 playerChoice=scanner.nextInt();
			 
			 //while (actionPoints>0) 
			 
			 /**
			  * 
			 switch (playerChoice) {
			 case 1: purchaseComponent;
			 break;
			 case 2: offerComponentToOtherPlayers;
			 break;
			 case 3: developComponent;
			 break;
			 case 4: developSystem;
			 break;
			 case 5: tradeComponent;
			 break;
			 case 6; showAllComponentOwners;
			 break;
			 case 7; getResourceBalance;
			 break;
			 case 8: getNextPlayer();
			 break;
			 case 9: endGame();
			 break;
			 default:
				 System.out.println("Please enter a valid option");
			 }
			 */
			 
			 //loop ends run getNextPlayer
		 }
		 
		 public static void playTurn() {
			 //container for other methods
			 rollDice();
			 updatePlayerPosition();
			 displayMenu();
			 
		 }
		 
		 public static void getNextPlayer() {
			 // may need to store a current player variable 
			 // run a loop to move between the players in the array 
			 // with an if statement to deal with when the loop moves to the last player in the array
			 
			 // or could use a boolean of active player as a var for all players
			 // use a loop to change the boolean at the end of a players turn
		 }
		 
		 public static void allocateResources() {
			 // resourceBalance + DEFAULT_RESOURCES
			 // run using an if statement after updated player position 
			 // to see if the player has moved enough to qualify 
			 
			 //counter if square=start 
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
			 //needs to terminate loop of the game 
			 System.out.println("The mission has failed");
		 }
	}

