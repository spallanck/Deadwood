
//Names: Sophie Pallanck, Alex Schwendeman 
//This class keeps track of where each player is, if they're working a role or not,
//and how much money they have
import java.util.*;
import javax.swing.JLabel;

public class Player {

    public String name;
    private int rank;
    private int money;
    private int credits;
    private int rehearsalChips;
    private String color;
    private Set playerSet;
    private Role playerRole;
    private SceneCard playerScene;
    private boolean onCardRole;
    private JLabel playerDie;
    private boolean moved;
    private boolean turnOver;

    

    // Player constructor that creates a Player object with the provided rank, number of credits, name, and location on the board.
    public Player(int rank, int credits, String name, Set location, String color) {
        this.rank = rank;
        this.money = 0;
        this.credits = credits;
        this.name = name;
        this.rehearsalChips = 0;
        this.color = color;
        this.playerSet = location;
        this.turnOver = false;
        this.moved = false;
    }

    // Returns true if this player is working a role on the card.
    public boolean getOnCard() {
        return onCardRole;
    }

    // Sets the boolean of if the player is working on the card or not.
    public void setOnCard(Boolean onCard) {
        onCardRole = onCard;
    }

    // Updates the players credit amounts.
    public void setPlayerCredits(int amt) {
        credits -= amt;
    }


    //Sets the players scene to the given scene.
    public void setPlayerScene(SceneCard scene) {
        playerScene= scene;
    }

    //Sets the player's role to the given role.
    public void setPlayerRole(Role role) {
        playerRole= role;
    }

    //Returns true or false if the Player's turn is over.
    public boolean getTurnOver() {
        return turnOver;
    }

    //Sets the Player's turn to over.
    public void endTurn() {
        this.turnOver = true;
    }

    //Returns if the player has moved or not.
    public boolean getMoved() {
        return moved;
    }

    //Sets the boolean value indicating if the player has moved yet.
    public void setMoved(Boolean status) {
        this.moved = status;
    }

    //Returns the SceneCard the Player is currently on.
    public SceneCard getPlayerScene() {
        return playerScene;
    }
    
    // Gets the name of a player.
    public String getPlayerName() {
        return name;
    }

    // Gets the rank of a player.
    public int getPlayerRank() {
        return rank;
    }
        
    // Gets the player's color.
    public String getPlayerColor() {
        return color;
    }

    //Sets the Player's set to the given set.
    public void setPlayerSet(Set other) {
        this.playerSet = other;
    }
        
    // Gets the amount of money a player has.
    public int getPlayerMoney() {
        return money;
    }

    // Updates the players money.
    public void setPlayerMoney(int amt) {
        this.money -= amt;
    }
        
    // Gets the amount of credits a player has.
    public int getPlayerCredits() {
        return credits;
    }

    // Returns the set the player is currently at.
    public Set getPlayerSet() {
        return playerSet;
    }

    // Returns the role the player is working on.
    public Role getPlayerRole() {
        return playerRole;
    }

    // Returns the amount of rehearsal chips a player has.
    public int getRehearsalChips() {
        return rehearsalChips;
    }

    // Resets the number of rehearsal chips a player has.
    public void resetChips() {
        this.rehearsalChips = 0;
    }

    

    // Sets the player's die to the given JLabel.
    public void setPlayerDie(JLabel playerdie) {
        this.playerDie = playerdie;
    }

    //Sets the player's rank to the given rank.
    public void setPlayerRank(int rank) {
        this.rank = rank;
    }

    //Returns the JLabel associated with the players die.
    public JLabel getPlayerDie() {
        return this.playerDie;
    }

    // Prints all available commands a player can enter on their turn as well as preconditions that must be true for the player to use the command.
    public void turnOptions() {
        System.out.println("Avaiable commands:");
        System.out.println("\"where am I\"");
        System.out.println("\"move\" - Player cannot be working a role, and cannot have already moved this turn.");
        System.out.println("\"act\" - Player must be working a role.");
        System.out.println("\"rehearse\" - Player must be working a role.");
        System.out.println("\"upgrade rank\" - Player must be in the Casting Office.");
        System.out.println("\"take role\" - Player cannot be working a role.");
        System.out.println("\"end\" - Player cannot be working a role.");
    }

    // Handles a player taking a turn and prints information about the decisions they make.
    public void playerTurn(String choice) {
        boolean turnOver = false;
        boolean moved = false;
        while (!turnOver) {
            
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("It is " + name + "'s turn. What would you like to do?");
            //String choice = sc.nextLine();
        
            if (choice.equalsIgnoreCase("help")) {
                System.out.println();
                turnOptions();
            } else if (choice.equalsIgnoreCase("where am I")) {
                System.out.println();
                locationInfo();
            } else if (choice.equalsIgnoreCase("move") && !moved && playerRole == null) {
                System.out.println();
                System.out.println("Which room would you like to move to?");
                playerSet.listNeighbors();
                System.out.println();
                String room = sc.nextLine();
                moved = move(room);
                if (moved && playerScene != null) {
                    if (!playerScene.checkCardFlipped()) {
                        playerScene.flipCard();
                    }
                }
            } else if (choice.equalsIgnoreCase("take role") && playerRole == null) {
                System.out.println();
                System.out.println("Which role would you like?");
                System.out.println();
                //playerSet.displayAvailableRoles();
                System.out.println();
                String role = sc.nextLine();
                boolean foundRole = takeRoleOnCard(role);
                if (!foundRole) {
                    foundRole = takeRoleOffCard(role);
                }
                if (!foundRole) {
                    System.out.println("That is not a valid role. Try again.");
                } 
                if (foundRole) { //player has taken a role 
                    System.out.println(name + " took the role " + playerRole.getRoleTitle() + ".");
                    turnOver = true;
                }
            } else if (choice.equalsIgnoreCase("act") && playerRole != null) {
                System.out.println();
                act();
                turnOver = true;
            } else if (choice.equalsIgnoreCase("rehearse") && playerRole != null) {
                System.out.println();
                boolean rehearsed = rehearse();
                if (rehearsed) {
                  turnOver = true;  
                }  
            } else if (choice.equalsIgnoreCase("upgrade rank") && playerSet.getSetName().equals("Casting Office") && rank != 6) {
                System.out.println();
                System.out.println("Which level would you like to upgrade to? (Input a number 2-6)");
                int desiredRank = sc.nextInt();
                sc.nextLine();
                System.out.println();
                if (desiredRank < rank) {
                    System.out.println("You cannot downgrade your rank.");
                } else if (desiredRank == rank) {
                    System.out.println("You are already this rank.");
                } else {
                    System.out.println("Displaying all upgrade options:");
                    listReqs();
                    System.out.println();
                    System.out.println("You have $" + money + " and " + credits + " credits.");
                    System.out.println("Which currency would you like to pay with? (Enter credits or dollars)");
                    String currency = sc.nextLine();
                    upgradeRank(currency, desiredRank);
                }   
            } else if (choice.equalsIgnoreCase("end") && playerRole == null) {
                System.out.println();
                turnOver = true;
            } else {
                System.out.println();
                System.out.println("This is not a valid command. Try using 'help'.");
            }
            
        }
        System.out.println(name + "'s turn is over.");
        System.out.println();
    }

    // Prints the information about the set the player is at, including the scene card at that set, the scene number, and the scene budget (if there is a scene at that set).
    public void locationInfo() {
        System.out.println(name + " is at " + playerSet.getSetName() + ".");
        if (playerSet.sceneExists()) {
            System.out.println("Scene: " + playerSet.getSceneCardName());
            System.out.println("Scene Number: " + playerSet.getSceneCard().getSceneNum());
            System.out.println("Scene Info: " + playerSet.getSceneCard().getSceneInfo());
            System.out.println("Budget: $" + playerSet.getSceneCard().getBudget() + "-million");
            System.out.println("Shots left: " + playerSet.getShotsLeft());
        } else {
            System.out.println("There is no scene at this location.");
        }
    }

    // Prints general information about the active player such as their rank, money, credits, and what role they are working (if they are currently working on a role).
    public void playerInfo() {
        System.out.print("The active player is " + name +  ". They have a rank of " + rank + ". They have $" + money + " and " + credits + " credits.");
        if (playerRole == null) {
            System.out.print(" " + name + " is not currently working a role.");
            System.out.println();
        } else {
            System.out.print(" " + name + " is working " + playerRole.getRoleTitle() + ", \"" + playerRole.getRoleLine() + "\".");
        }
    }

    // Takes the name of a set and returns true if this set is a neighboring set
    // and moves the player to the set, and false otherwise.
    public boolean move(String neighborSet) {
        Set isANeighbor = playerSet.checkNeighbor(neighborSet);
        if (isANeighbor == null) {
            System.out.println(neighborSet + " is not a neighbor of " + playerSet.getSetName() + ".");
            return false;
        } else {
            System.out.println(name + " has moved from " + playerSet.getSetName() + " to " + isANeighbor.getSetName() + ".");
            playerSet = isANeighbor;
            return true;
        }
    }

    // Takes the name of an off card role the player wants to take and takes the role
    // and returns true if this is a valid role, and false otherwise.
    public boolean takeRoleOffCard(String roleName) {
        if (!playerSet.sceneExists()) {
            System.out.println("There is no scene at this location.");
            return false;
        } else {
            Role openRole = playerSet.checkOffCardRole(roleName);
            if (openRole == null) {
                return false;
            } else {
                if (openRole.getReqRank() > rank) {
                    System.out.println("The rank required for this role is higher than your rank. Try again.");
                    return false;
                } else {
                  this.playerRole = openRole;
                    playerRole.takePart(this); 
                    this.playerScene = playerSet.getSceneCard();
                    onCardRole = false;
                    return true;  
                }
                
            }
        }
    }

    // Takes the name of an on card role the player wants to take and takes the role
    // and returns true if this is a valid role, and false otherwise.
    public boolean takeRoleOnCard(String roleName) {
        if (!playerSet.sceneExists()) {
            System.out.println("There is no scene at this location.");
            return false;
        } else {
            Role openRole = playerSet.checkOnCardRole(roleName);
            if (openRole == null) {
                return false;
            } else { //taking a role on the card
                if (openRole.getReqRank() > rank) {
                    System.out.println("The rank required for this role is higher than your rank. Try again.");
                    return false;
                } else {
                    this.playerRole = openRole;
                    playerRole.takePart(this); 
                    this.playerScene = playerSet.getSceneCard();
                    onCardRole = true;
                    return true;
                }
               
            }
            
        }
    }

    // Pre-condition: Player is working on a role
    // Rolls a die and pays the player accordingly based on their roll.
    public void act() {
        Dice budgetDie = new Dice();
        int roll = budgetDie.roll();
        System.out.println("The budget of the film is $" + playerSet.getSceneCard().getBudget() + "-million.");
        System.out.println("You rolled a " + roll + ".");
        BoardView.showMessage("You rolled a " + roll + ".");
        System.out.println("You have " + rehearsalChips + " rehearsal chips.");
        int total = roll + rehearsalChips;
        if (total >= playerScene.getBudget()) {
            if (onCardRole) {
                credits += 2;
                BoardView.showMessage("Congratulations! You've succeeded! You receive two credits. You now have " + credits + " credits.");
                System.out.println("Congratulations! You've succeeded! You receive two credits. You now have " + credits + " credits.");
            } else {
                money++;
                credits++;
                BoardView.showMessage("Congratulations! You've succeeded! You receive one credit and one dollar. You now have $" + money + " and " + credits + " credits.");
                System.out.println("Congratulations! You've succeeded! You receive one credit and one dollar. You now have $" + money + " and " + credits + " credits.");
                
            }
            
            int shotsLeft = playerSet.removeShot(); 
            BoardView.removeShot(playerSet);
            System.out.println("Shots remaining: " + shotsLeft);
            if (shotsLeft == 1) {
                rehearsalChips = 0;
                if (playerSet.playerOnCard()) {
                    List<Integer> payOutDice = budgetDie.rollPayout(playerScene.getBudget());
                    playerSet.wrapScene(payOutDice);
                } else {
                    playerSet.wrapScene();
                    playerRole = null;
                }
            }
        } else {
            if (onCardRole) {
                System.out.println("You failed! You receive nothing.");

            } else {
                money++;
                System.out.println("You failed! But you still receive a dollar. You now have $" + money + ".");
                 
            }
            int shotsLeft = playerSet.getShotsLeft();
            System.out.println("Shots remaining: " + shotsLeft);
        }
    }

    // Pre-condition: Player is working on a role
    // Adds a rehearsal chip to the players count 
    public boolean rehearse() {
        if ((rehearsalChips + 1) == playerScene.getBudget()) {
            System.out.println("You already have enough rehearsal chips to guarantee success. You cannot rehearse.");
            BoardView.showMessage("You already have enough rehearsal chips to guarantee success. You cannot rehearse.");
            return false;
        } else {
            rehearsalChips++;
            BoardView.showMessage("You have rehearsed! You now have " + rehearsalChips + " rehearsal chips.");
            System.out.println("You have rehearsed! You now have " + rehearsalChips + " rehearsal chips.");
            return true;
        }
    }

    // Displays the cost for each rank upgrade to the player.
    public void listReqs() {
        HashMap<Integer, Integer> upgradeDollars = playerSet.getUpgradeReqs("dollar");
        HashMap<Integer, Integer> upgradeCredits = playerSet.getUpgradeReqs("credit");
        for (Integer level : upgradeDollars.keySet()) {
            System.out.println("Level: " + level);
            System.out.println("Cost in dollars: $" + upgradeDollars.get(level));
        }
        System.out.println();
        for (Integer level : upgradeCredits.keySet()) {
            System.out.println("Level: " + level);
            System.out.println("Cost in credits: " + upgradeDollars.get(level));
        }
    }

    // Pre-condition: Player is at the casting office
    // Takes a string indicating the type of currency to play with
    // and the rank a player wants to upgrade to and changes the player's
    // rank if they have the funds for it.
    public void upgradeRank(String currency, int desiredRank) {
        HashMap<Integer, Integer> upgradeInfo = playerSet.getUpgradeReqs(currency);
        int reqPayment = upgradeInfo.get(desiredRank);
        if (currency.equals("credits")) {
            if (credits >= reqPayment) {
                credits = credits - reqPayment;
                this.rank = desiredRank;
            } else {
                System.out.println("Insufficient funds, cannot upgrade to requested rank!");
            }
        } else if (currency.equals("dollars")) {
            if (money >= reqPayment) {
                money = money - reqPayment;
                this.rank = desiredRank;
                System.out.println("Upgrade rank was sucessful! Your new rank is: " + rank);
            } else {
                System.out.println("Insufficient funds, cannot upgrade to requested rank!");
            }
        } else {
            System.out.println("This is not a valid currency. Try again.");
        }
    }


    // Pays a player the number of dollars equivalent
    // to the role they are working.
    public void payBonus() {
        System.out.println(name + " has received $" + playerRole.getReqRank() + " for this scene." );
        money += playerRole.getReqRank();
    }

    // Pays a player the specified amount.
    public void payOnCard(int pay) {
        BoardView.showMessage(name + " has received $" + pay + " for this scene.");
        System.out.println(name + " has received $" + pay + " for this scene." );
        money += pay;
    }

    // Calculates the score of a player based on the player's rank and the amount of money and credits they have.
    public int calcPlayerScore() {
        int score = (rank * 5) + money + credits;
        BoardView.showMessage(name + " has a score of " + score + " points.");
        System.out.println(name + " has a score of " + score + " points.");
        return score;
    }
    
    // Resets the role of a player to be null.
    public void removeRole() {
        this.playerRole = null;
    }

    // Moves a player back to the trailers set.
    public void moveToTrailers(Board b) {
        this.playerSet = b.getSet("Trailers");
        this.playerRole = null;
    }

}
