import java.util.*;

//Names: Sophie Pallanck, Alex Schwendeman 
//This class initiates a game of Deadwood and manages each player's
//turn and reports who won when the game is over.
public class Deadwood {
    private static int numPlayers;
    private static Queue<Player> players;

    //Starts the game and the GUI
    public static void main(String[] args) {
        String boardXMLFile = null;
        String cardXMLFile = null;
        boardXMLFile = "src\\main\\resources\\xml\\board.xml";
        cardXMLFile = "src\\main\\resources\\xml\\cards.xml";
        numPlayers = 2;

        if (numPlayers < 2 || numPlayers > 8) {
            System.out.println("Incorrect number of players, please enter a number between 2 and 8!");
            throw new InputMismatchException();
        }
        startModel(cardXMLFile, boardXMLFile);
    }

    //Creates the board and sets up the view.
    public static void startModel(String cardPath, String boardPath) {
        Board board = null;
        if (numPlayers == 2 || numPlayers == 3) {
            board = Board.makeBoard(3, cardPath); 
        } else {
            board = Board.makeBoard(4, cardPath);
        }
        Board.setUpSets(boardPath);
        BoardView view = new BoardView(board);
        int numPlayers = view.getNumPlayers();
        Queue<String> names = view.startGame(numPlayers);
        board = DeadwoodM.initalizeGame(board, numPlayers, names);
        players = DeadwoodM.getPlayerQueue();
        view.putPlayersInTrailers();
        view.setVisible(true);
        //model.Deadwood.turnManager(board, view);
        //model.Player curPlayer = players.poll();
        Player curPlayer = DeadwoodM.getCurPlayer();
        System.out.println("Hello");
        view.activePlayer(curPlayer, board.getDaysLeft());
        view.showMessage("It is " + curPlayer.getPlayerName() + "'s turn.");
         
    }

    // Handles the text based implementation of turns.
    public static void turnManager(Board b, BoardView view) {
        players = DeadwoodM.getPlayerQueue();
        while (Board.getDaysLeft() != 0) {
            while (Board.getScenesLeft() > 1) {
                Player curPlayer = players.poll();
                view.activePlayer(curPlayer, Board.getDaysLeft());
                view.showMessage("It is " + curPlayer.getPlayerName() + "'s turn.");

                // Player takes their turn here.
                curPlayer.playerInfo();

                
                players.add(curPlayer);
            }
            System.out.println();
            System.out.println("New Day! All actors back to the Trailers.");
            Board.nextDay();
            System.out.println("Days left: " + Board.getDaysLeft());
            
            for (int i = 0; i < numPlayers; i++) {
                Player curPlayer = players.remove();
                curPlayer.resetChips();
                curPlayer.moveToTrailers(b);
                players.add(curPlayer);
            }
        }
        //endGame();
    }

    //Handles player choices.
    public void playerTurn(Player curPlayer, String choice) {
        boolean turnOver = false;
        boolean moved = false;
        while (!turnOver) {
            
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("It is " + curPlayer.getPlayerName() + "'s turn. What would you like to do?");
            //String choice = sc.nextLine();
            
            if (choice.equalsIgnoreCase("help")) {
                System.out.println();
                //turnOptions();
            } else if (choice.equalsIgnoreCase("where am I")) {
                System.out.println();
                //locationInfo();
            } else if (choice.equalsIgnoreCase("move") && !moved && curPlayer.getPlayerRole() == null) {
                System.out.println();
                System.out.println("Which room would you like to move to?");
                curPlayer.getPlayerSet().listNeighbors();
                System.out.println();
                String room = sc.nextLine();
                moved = curPlayer.move(room);
                if (moved && curPlayer.getPlayerScene() != null) {
                    if (!curPlayer.getPlayerScene().checkCardFlipped()) {
                        curPlayer.getPlayerScene().flipCard();
                    }
                }
            } else if (choice.equalsIgnoreCase("take role") && curPlayer.getPlayerRole() == null) {
                System.out.println();
                System.out.println("Which role would you like?");
                System.out.println();
                curPlayer.getPlayerSet().displayAvailableRoles();
                System.out.println();
                String role = sc.nextLine();
                boolean foundRole = curPlayer.takeRoleOnCard(role);
                if (!foundRole) {
                    foundRole = curPlayer.takeRoleOffCard(role);
                }
                if (!foundRole) {
                    System.out.println("That is not a valid role. Try again.");
                } 
                if (foundRole) { //player has taken a role 
                    System.out.println(curPlayer.getPlayerName() + " took the role " + curPlayer.getPlayerRole().getRoleTitle() + ".");
                    turnOver = true;
                }
            } else if (choice.equalsIgnoreCase("act") && curPlayer.getPlayerRole() != null) {
                System.out.println();
                curPlayer.act();
                turnOver = true;
            } else if (choice.equalsIgnoreCase("rehearse") && curPlayer.getPlayerRole() != null) {
                System.out.println();
                boolean rehearsed = curPlayer.rehearse();
                if (rehearsed) {
                  turnOver = true;  
                }  
            } else if (choice.equalsIgnoreCase("upgrade rank") && curPlayer.getPlayerSet().getSetName().equals("Casting Office") && curPlayer.getPlayerRank() != 6) {
                System.out.println();
                System.out.println("Which level would you like to upgrade to? (Input a number 2-6)");
                int desiredRank = sc.nextInt();
                sc.nextLine();
                System.out.println();
                if (desiredRank < curPlayer.getPlayerRank()) {
                    System.out.println("You cannot downgrade your rank.");
                } else if (desiredRank == curPlayer.getPlayerRank()) {
                    System.out.println("You are already this rank.");
                } else {
                    System.out.println("Displaying all upgrade options:");
                    curPlayer.listReqs();
                    System.out.println();
                    System.out.println("You have $" + curPlayer.getPlayerMoney() + " and " + curPlayer.getPlayerCredits() + " credits.");
                    System.out.println("Which currency would you like to pay with? (Enter credits or dollars)");
                    String currency = sc.nextLine();
                    curPlayer.upgradeRank(currency, desiredRank);
                }   
            } else if (choice.equalsIgnoreCase("end") && curPlayer.getPlayerRole() == null) {
                System.out.println();
                turnOver = true;
            } else {
                System.out.println();
                System.out.println("This is not a valid command. Try using 'help'.");
            }
            
        }
        System.out.println(curPlayer.getPlayerName() + "'s turn is over.");
        System.out.println();
    }

       

    // Returns the choice that the player made.
    public String getChoice(String choice) {
        return choice;
    }

}
