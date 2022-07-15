

//Names: Sophie Pallanck, Alex Schwendeman 
//This class initiates a game of Deadwood and manages each player's
//turn and reports who won when the game is over.
import java.util.*;

import javax.swing.JOptionPane;

public class DeadwoodM {
    private static int numPlayers;
    private static Queue<Player> players = new LinkedList<Player>();
    private static Player curPlay;

    

    //Returns the number of players. 
    public static int getNumPlayers() {
        return numPlayers;
    }

    //Sets the number of players to the given integer.
    public static void setNumPlayers(int num) {
        numPlayers = num;
    }

    // Sets up the game, using any special rules depending on the number of players.
    public static Board initalizeGame(Board board, int numPlayers, Queue<String> names) {
       /* Board board = null;
        if (numPlayers == 2 || numPlayers == 3) {
            board = Board.makeBoard(3, cardPath); 
        } else {
            board = Board.makeBoard(4, cardPath);
        }
        Board.setUpSets(boardPath);*/

        int startingRank;
        int startingCredits;
        if (numPlayers == 5) {
            startingRank = 1;
            startingCredits = 2;
        } else if (numPlayers == 6) {
            startingRank = 1;
            startingCredits = 4;
        } else if (numPlayers == 7 || numPlayers == 8) {
            startingRank = 2;
            startingCredits = 0;
        } else {
            startingRank = 1;
            startingCredits = 50;
        }

        String[] colors = {"b", "c", "g", "o", "p", "r", "v", "y"};

        //Scanner sc = new Scanner(System.in);
        for (int i = 0; i < numPlayers; i++) {
            ///System.out.print("Enter the name of Player " + i + ": ");
            ///String playerName = sc.nextLine();

                                                                  // CHANGE THIS BACK
            players.add(new Player(startingRank, startingCredits, names.poll(), board.getSet("Trailers"), colors[i]));
        }
        System.out.println();

        return board;

    }


   

    //
    public static void turnManager(Board b, BoardView view) {
        players = DeadwoodM.getPlayerQueue();
        while (Board.getDaysLeft() != 0) {
            while (Board.getScenesLeft() > 1) {
                Player curPlayer = players.poll();
                curPlay = curPlayer;
                System.out.println("Hello");
                view.activePlayer(curPlayer, Board.getDaysLeft());
                view.showMessage("It is " + curPlayer.getPlayerName() + "'s turn.");

                // Player takes their turn here.
                curPlayer.playerInfo();
                //curPlayer.playerTurn();
                //playerTurn(curPlayer, choice, view);
                
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

    public static void upgradeRank(Player curPlayer, String choice, int requestedRank, BoardView view) {
        System.out.println(choice);
        Set curSet = curPlayer.getPlayerSet();
        HashMap<Integer, Integer> upgradeReqs = curSet.getUpgradeReqs(choice);
        if (curPlayer.getPlayerSet().getSetName().equals("Casting Office") && curPlayer.getPlayerRank() != 6) {
            System.out.println("hiii");
            if (choice.equalsIgnoreCase("credit")) {
                System.out.println(curPlayer.getPlayerCredits());
                System.out.println(upgradeReqs.get(requestedRank));
                if (curPlayer.getPlayerCredits() >= upgradeReqs.get(requestedRank)) {
                    int cost = upgradeReqs.get(requestedRank);
                    curPlayer.setPlayerCredits(cost);
                    curPlayer.setPlayerRank(requestedRank);
                    System.out.println(curPlayer.getPlayerName());
                    System.out.println(curPlayer.getPlayerRank());
                    System.out.println(curPlayer.getPlayerColor());
                    view.setPlayerIcon(curPlayer);
                } else {
                    view.showMessage("Insufficient funds, cannot upgrade to requested rank!");
                    System.out.println("Insufficient funds, cannot upgrade to requested rank!");
                }
            } else if (choice.equalsIgnoreCase("dollar")) {
                if (curPlayer.getPlayerMoney() >= upgradeReqs.get(requestedRank)) {
                    int cost = upgradeReqs.get(requestedRank);
                    curPlayer.setPlayerMoney(cost);
                    curPlayer.setPlayerRank(requestedRank);
                    view.setPlayerIcon(curPlayer);
                    System.out.println(curPlayer.getPlayerName());
                    System.out.println(curPlayer.getPlayerRank());
                    System.out.println(curPlayer.getPlayerColor());
                } else {
                    view.showMessage("Insufficient funds, cannot upgrade to requested rank!");
                    System.out.println("Insufficient funds, cannot upgrade to requested rank!");
                }
            }
        }
    }

    //Handles player choices.
    public static void playerTurn(Player curPlayer, String choice, BoardView view) {
            view.showMessage("It is " + curPlayer.getPlayerName() + "'s turn. What would you like to do?");
            System.out.println("It is " + curPlayer.getPlayerName() + "'s turn. What would you like to do?");
            if (choice.equals("act") && curPlayer.getPlayerRole() != null) {
                System.out.println("Current role is " + curPlayer.getPlayerRole().getRoleTitle());
                curPlayer.act();
            }  else if (choice.equals("rehearse") && curPlayer.getPlayerRole() != null) {
                boolean rehearsed = curPlayer.rehearse();
            }  else  if (choice.equalsIgnoreCase("upgrade rank") && curPlayer.getPlayerSet().getSetName().equals("Casting Office") && curPlayer.getPlayerRank() != 6) {
                view.showMessage("Rehearse");
            }     
            Scanner sc = new Scanner(System.in);
            System.out.println();
            
            
            if (choice.equalsIgnoreCase("help")) {
                System.out.println();
                //turnOptions();
            } else if (choice.equalsIgnoreCase("where am I")) {
                System.out.println();
                //locationInfo();
            } else if (choice.equalsIgnoreCase("move") && !curPlayer.getMoved() && curPlayer.getPlayerRole() == null) {
                System.out.println();
                System.out.println("Which room would you like to move to?");
                ArrayList<Set> neighbors = curPlayer.getPlayerSet().getNeighbors();
                String[] setOptions = new String[neighbors.size()];
                for (int i = 0; i < setOptions.length; i++) {
                    setOptions[i] = neighbors.get(i).getSetName();
                }
                int option =  JOptionPane.showOptionDialog(null, "Which set would you like to move to?", "Text Here",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, setOptions, setOptions[0]);

                if (option != JOptionPane.CLOSED_OPTION) { //player has selected a set
                    int setNameLength = 0;
                    for (int i = 0; i < setOptions[option].length(); i++) {
                        if (setOptions[option].contains(" ")) {
                            setNameLength = 3; //two word set name
                        } else {
                            setNameLength = 2;
                        }
                    }
                        String[] eachWord = setOptions[option].split("\\s+");
                        String result = "";
                        if (setNameLength == 2) {
                            result = eachWord[0];
                        } else {
                            result = eachWord[0] + " " + eachWord[1];
                        }
                        System.out.println("You chose " + result);

                    move(result, curPlayer);
                    //curPlayer.getPlayerScene().flipCard();
                    System.out.println("Remove back now");
                    view.removeCardBack(curPlayer.getPlayerSet());
                    Board b = Board.parse();
                        int days = b.getDaysLeft();
                        view.activePlayer(curPlayer, days);
                }

            } else if (choice.equalsIgnoreCase("take role") && curPlayer.getPlayerRole() == null) {
                System.out.println();
                System.out.println("Which role would you like?");
                System.out.println();
                ArrayList<Role> setRoles = curPlayer.getPlayerSet().listRoles();
                ArrayList<Role> cardRoles = curPlayer.getPlayerSet().getSceneCard().listRoles();
                System.out.println(cardRoles.size());
                String[] roleNames = new String[setRoles.size() + cardRoles.size()];
                int index = 0;
                for (int i = 0; i < setRoles.size(); i++) {
                    roleNames[index] = setRoles.get(i).getRoleTitle();
                    index++;
                }
                for (int i = 0; i < cardRoles.size(); i++) {
                    roleNames[index] = cardRoles.get(i).getRoleTitle();
                    index++;
                }
                Object chosenScene = JOptionPane.showInputDialog(null, "Which role would you like?", "Options", JOptionPane.DEFAULT_OPTION, null, roleNames, roleNames[0]);
                if (chosenScene != null) {
                    String stringVersion = chosenScene.toString(); 
                    boolean foundRole = takeRoleOnCard(stringVersion, curPlayer);
                   
                    if (!foundRole) {
                        foundRole = takeRoleOffCard(stringVersion, curPlayer);
                    }
                    if (!foundRole) {
                        System.out.println("That is not a valid role. Try again.");
                    } 
                    if (foundRole) { //player has taken a role 
                        System.out.println(curPlayer.getPlayerName() + " took the role " + curPlayer.getPlayerRole().getRoleTitle() + ".");
                        
                        //turnOver = true;
                }
            } else if (choice.equalsIgnoreCase("act") && curPlayer.getPlayerRole() != null) {
                System.out.println("Test AGAIN");
                curPlayer.act();
                //turnOver = true;
            } else if (choice.equalsIgnoreCase("rehearse") && curPlayer.getPlayerRole() != null) {
                System.out.println();
                boolean rehearsed = curPlayer.rehearse();
                if (rehearsed) {
                  //turnOver = true;  
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
                System.out.println("hello");
                Player newPlayer = nextPlayer();
                view.showMessage("Move clicked.");
                //turnOver = true;
            } else {
                System.out.println();
                System.out.println("This is not a valid command. Try using 'help'.");
            }
            
        //}
        System.out.println(curPlayer.getPlayerName() + "'s turn is over.");
        System.out.println();
            }
    }

    public static boolean move(String neighborSet, Player curPlayer) {
        Set isANeighbor = curPlayer.getPlayerSet().checkNeighbor(neighborSet);
        if (isANeighbor == null) {
            BoardView.showMessage("You have selected a set that is not nearby.");
            System.out.println(neighborSet + " is not a neighbor of " + curPlayer.getPlayerSet().getSetName() + ".");
            return false;
        } else {
            BoardView.removePlayerDie(curPlayer);
            curPlayer.setPlayerSet(isANeighbor);
            //System.out.println(curPlayer.getPlayerSet().getSceneCardName());
            BoardView.movePlayerDie(curPlayer, curPlayer.getPlayerSet(), curPlayer.getPlayerSet().getSetLoc());
            System.out.println(curPlayer.getPlayerName() + " has moved from " + curPlayer.getPlayerSet().getSetName() + " to " + isANeighbor.getSetName() + ".");
            //System.out.println(curPlayer.getPlayerSet().getSceneCardName());
            if (!curPlayer.getPlayerSet().getSetName().equals("Casting Office") && !curPlayer.getPlayerSet().getSetName().equals("Trailers")) {
                System.out.println("Is this being hit");
               curPlayer.getPlayerSet().getSceneCard().flipCard(); 
               //view.BoardView.removeCardBack(curPlayer.getPlayerSet());
            }
            
            
            curPlayer.setMoved(true);
            BoardView.showMessage("You are now in " + neighborSet);
            return true;
        }
    }

    // Takes the name of an off card role the player wants to take and takes the role
    // and returns true if this is a valid role, and false otherwise.
    public static boolean takeRoleOffCard(String roleName, Player curPlayer) {
        if (!curPlayer.getPlayerSet().sceneExists()) {
            System.out.println("There is no scene at this location.");
            return false;
        } else {
            Role openRole = curPlayer.getPlayerSet().checkOffCardRole(roleName);
            if (openRole == null) {
                return false;
            } else {
                if (openRole.getReqRank() > curPlayer.getPlayerRank()) {
                    System.out.println("The rank required for this role is higher than your rank. Try again.");
                    BoardView.showMessage("The rank required for this role is higher than your rank. Try again.");
                    return false;
                } else {
                    curPlayer.setPlayerRole(openRole);
                    curPlayer.getPlayerRole().takePart(curPlayer); 
                    curPlayer.setPlayerScene(curPlayer.getPlayerSet().getSceneCard());
                    //this.playerScene = playerSet.getSceneCard();
                    //curPlayer.setOnCard(true); 
                    BoardView.removePlayerDie(curPlayer);
                    BoardView.movePlayerDieCard(curPlayer, curPlayer.getPlayerRole().getRoleLoc(), curPlayer.getPlayerSet().getSetLoc());
                    return true;
                }
                
            }
        }
    }

    // Takes the name of an on card role the player wants to take and takes the role
    // and returns true if this is a valid role, and false otherwise.
    public static boolean takeRoleOnCard(String roleName, Player curPlayer) {
        if (!curPlayer.getPlayerSet().sceneExists()) {
            System.out.println("There is no scene at this location.");
            return false;
        } else {
            Role openRole = curPlayer.getPlayerSet().checkOnCardRole(roleName);
            if (openRole == null) {
                return false;
            } else { //taking a role on the card
                if (openRole.getReqRank() > curPlayer.getPlayerRank()) {
                    System.out.println("The rank required for this role is higher than your rank. Try again.");
                    BoardView.showMessage("The rank required for this role is higher than your rank. Try again.");
                    return false;
                } else {
                    curPlayer.setPlayerRole(openRole);
                    curPlayer.getPlayerRole().takePart(curPlayer); 
                    curPlayer.setPlayerScene(curPlayer.getPlayerSet().getSceneCard());
                    //this.playerScene = playerSet.getSceneCard();
                    curPlayer.setOnCard(true); 
                    BoardView.removePlayerDie(curPlayer);
                    BoardView.onCardMove(curPlayer, curPlayer.getPlayerRole().getRoleLoc(), curPlayer.getPlayerSet().getSetLoc());
                    return true;
                }
               
            }
            
        }
    }


    // End the game of Deadwood and prints the winner of the game.
    public static void endGame() {
            int maxScore = players.peek().calcPlayerScore();
            Player winner = players.peek();
    
            players.poll();
    
            for (int i = 1; i < players.size(); i++) {
                Player curPlayer = players.peek();
                int curPlayerScore = players.peek().calcPlayerScore();
                if (curPlayerScore > maxScore) {
                    maxScore = curPlayerScore;
                    winner = curPlayer;
                }
            }
    
            System.out.println("The winner is " + winner.getPlayerName() + "!");
            BoardView.showMessage("The winner is " + winner.getPlayerName() + "!");  
            BoardView.showMessage("The game is over. Please close the game.");
            
        }    

    public static Queue<Player> getPlayerQueue() {
        return players;
    }

    public static Player getCurPlayer() {
        curPlay= players.peek();
        return curPlay;
    }

    public static Player nextPlayer() {
        curPlay= players.poll();
        curPlay.setMoved(false);
        //view.BoardView.showMessage("It is now " + curPlay.getPlayerName() + "'s turn");
        players.add(curPlay);
        return curPlay;
    }

    

}

