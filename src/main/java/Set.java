

//Names: Sophie Pallanck, Alex Schwendeman
//This class contains information about each set on the board,
//including which scene card is there, and casting office upgrade
//information
import java.util.*;
import java.awt.Rectangle;
import javax.swing.JLabel;

public class Set {

    private String setName;
    private int shotsLeft;
    private int totalShots;
    private SceneCard scene;
    private ArrayList<Set> adjSets = new ArrayList<>();
    private ArrayList<Role> setRoles = new ArrayList<>();;
    private HashMap<Integer, Integer> dollarUpgrade;
    private HashMap<Integer, Integer> creditUpgrade;
    private HashMap<Integer, Rectangle> shotLocations;
    private HashMap<Rectangle, JLabel> shotToLabel = new HashMap<>();
    private Rectangle setLoc;
    private JLabel shot;
    private int playersOnSet;

    // Constructor for sets containing a scene card.
    public Set(String setName, int shots, HashMap<Integer, Rectangle> shotLocations, Rectangle setLoc) {
        this.shotsLeft = shots;
        this.setName = setName;
        this.totalShots = shots;
        this.shotLocations = shotLocations;
        this.setLoc = setLoc;
        playersOnSet = 0;
    } 

    // Returns the HashMap of shots to locations.
    public HashMap<Rectangle, JLabel> getJLabelTake() {
        return shotToLabel;
    }

    // Sets the shot JLabel at a given location
    public void putJLabelTake(Rectangle loc, JLabel shot) {
        shotToLabel.put(loc, shot);
    }

    // Increases the number of players on the Set
    public void addPlayerOnSet() {
        playersOnSet++;
    }

    // Returns the number of players on the Set.
    public int getPlayersOnSet() {
        return playersOnSet;
    }

    // Returns the HashMap of the shot marker location.
    public HashMap<Integer, Rectangle> getShotLocs() {
        return shotLocations;
    }

    // Sets the shot marker JLabel.
    public void setShot(JLabel take) {
        this.shot = take;
    }

    // Returns the Rectangle containing the Sets location.
    public Rectangle getSetLoc() {
        return setLoc;
    }

    // Returns the JLabel of a shot.
    public JLabel getShot() {
        return shot;
    }

    // Constructor for sets without a scene card.
    public Set(String setName, Rectangle setLoc) {
        this.setName = setName;
        dollarUpgrade = new HashMap<>();
        creditUpgrade = new HashMap<>();
        this.setLoc = setLoc;
    }

    // Returns a string containing the name of the set.
    public String getSetName() {
        return setName;
    }

    // Returns the list of neighboring Sets.
    public ArrayList<Set> getNeighbors() {
        return adjSets;
    }

    // Returns the SceneCard associated with this set.
    public SceneCard getSceneCard() {
        return scene;
    }

    // Returns the name of the movie on the SceneCard.
    public String getSceneCardName() {
        return scene.getSceneName();
    }

    // Returns the list of the roles on the Set.
    public ArrayList<Role> getSetRoles() {
        return setRoles;
    }

    // Returns the location of the movie on the SceneCard.
    public Rectangle getCardLoc() {
        return setLoc;
    }

    // Associates the set with a new SceneCard.
    public void resetSceneCard(SceneCard newScene) {
        this.scene = newScene;
    }

    // Returns an ArrayList of all of the roles
    // associated with the set.
    public ArrayList<Role> listRoles() {
        return setRoles;
    }

    // Accepts a string representing dollars or credits and returns 
    // a hash map of the casting office upgrade requirements.
    public HashMap<Integer, Integer> getUpgradeReqs(String currency) {
        if (currency.equalsIgnoreCase("credit")) {
            return creditUpgrade;
        } else {
            return dollarUpgrade;
        }
    }

    // Returns the number of shots left for this scene.
    public int getShotsLeft() {
        return shotsLeft;
    }

    // Takes information about a role and adds it to this set's list of roles.
    public void addRole(String title, String line, int rank, Rectangle roleArea) {
        setRoles.add(new Role(title, line, rank, roleArea));
    }

    // Takes the name of a set and checks to see if that set is a neighbor of
    // this set, returning the set if it is and null otherwise.
    public Set checkNeighbor(String setInQuestion) {
        for (int i = 0; i < adjSets.size(); i++) {
            if (adjSets.get(i).getSetName().equalsIgnoreCase(setInQuestion)) {
                return adjSets.get(i);
            }
        }
        return null;
    }

    // Takes a set as a parameter and adds it to the set's list of neighbors.
    public void addNeighbor(Set neighbor) {
        adjSets.add(neighbor);
    }

    // Prints out the neighboring sets.
    public void listNeighbors() {
        for (int i = 0; i < adjSets.size() - 1; i++) {
            System.out.print(adjSets.get(i).getSetName() + ", ");
        }
        System.out.print(adjSets.get(adjSets.size() - 1).getSetName());
        System.out.println();
    }

    // Takes the name of a role and checks if this role is one of the on card
    // roles at this set.
    public Role checkOnCardRole(String roleName) {
        if (!sceneExists()) {
            System.out.println("There is no scene being shot at this location, try again!");
        } else {
            for (int i = 0; i < scene.getNumRoles(); i++) {
                if (scene.listRoles().get(i).getRoleTitle().equalsIgnoreCase(roleName)) {
                    if (scene.listRoles().get(i).getPlayerOnRole() == null) {
                        //role available
                        System.out.println("This role is available.");
                        return scene.listRoles().get(i);
                    } else { //role not available
                        System.out.println("This role is already taken.");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // Takes the name of a role and checks if this role is one of the off card
    // roles at this set.
    public Role checkOffCardRole(String roleName) {
        if (!sceneExists()) {
            System.out.println("There is no scene being shot at this location, try again!");
        } else {
            for (int i = 0; i < setRoles.size(); i++) {
                if (setRoles.get(i).getRoleTitle().equalsIgnoreCase(roleName)) {
                    if (setRoles.get(i).getPlayerOnRole() == null) {
                        System.out.println("This role is available.");
                        return setRoles.get(i);
                    } else {
                        System.out.println("This role is already taken.");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // Removes a shot from the total shots in this scene and 
    // returns the remaining number of shots.
    public int removeShot() {
        shotsLeft--;
        return shotsLeft;
    }

    // Resets the number of shots remaining to the original
    // number of shots.
    public void resetShots() {
        shotsLeft = totalShots;
    }

    // Returns true if there is an actor working on a SceneCard
    // at this set and false otherwise.
    public boolean playerOnCard() {
        for (int i = 0; i < scene.getNumRoles(); i++) {
            if (scene.listRoles().get(i).getPlayerOnRole() != null){
                return true;
            }
        }
        return false;
    }

    // Returns true if there is an active SceneCard at this set.
    public boolean sceneExists() {
        if (scene == null) {
            return false;
        } else {
            return true;
        }
    }

    // Resets the set once the SceneCard has no remaining shots and there 
    // were no players working a role on this SceneCard.
    public void wrapScene() {
        
        for (int i = 0; i < setRoles.size(); i++) {
            if (setRoles.get(i).getPlayerOnRole() != null) {
                setRoles.get(i).getPlayerOnRole().removeRole();
                setRoles.get(i).removeActor();
            }
        }
        BoardView.removeCardFront(this);
        BoardView.showMessage("This scene has wrapped!");
        
        this.scene = null;

        Board.updateScenesLeft(); 
        int scenesLeft = Board.getScenesLeft();
        if (scenesLeft  == 8) {
            Board.nextDay();
            Board b = Board.parse();
            
            BoardView.showMessage("Days left: " + Board.getDaysLeft());
            System.out.println("Days left: " + Board.getDaysLeft());
            
            Queue<Player> players = DeadwoodM.getPlayerQueue();
            int numPlayers = players.size();
            System.out.println(numPlayers);
            System.out.println(players.size());
            DeadwoodM.setNumPlayers(numPlayers);
            for (int i = 0; i < players.size(); i++) {
                Player curPlayer = players.remove();
                curPlayer.resetChips();
                curPlayer.moveToTrailers(b);
                System.out.println(numPlayers);
                BoardView.removePlayerDie(curPlayer);
                curPlayer.removeRole();
                System.out.println(curPlayer.getPlayerScene() == null);
                players.add(curPlayer);
            }
            BoardView.resetPlayers();
            BoardView.addSceneCards();
            BoardView.addShotMarkers();
            
            System.out.println("this is being hit but nothing happens");
        }

    }

    // Resets the set once the SceneCard has no remaining shots and there 
    // was at least one player working a role on this SceneCard.
    public void wrapScene(List<Integer> payOutDice) {
        BoardView.removeCardFront(this);
        BoardView.showMessage("Getting bonus dice results...");
        System.out.println("Printing bonus dice results...");
        for (int i = 0; i < payOutDice.size(); i++) {
            System.out.print(payOutDice.get(i) + " ");
        }
        System.out.println();
        List<Role> roles = scene.listRoles();
        int roleIndex = roles.size() - 1;
        for (int i = 0; i < payOutDice.size(); i++) {
            if (roles.get(roleIndex).getPlayerOnRole() != null) {
                Player player = roles.get(roleIndex).getPlayerOnRole();
                player.payOnCard(payOutDice.get(i));
            }
            roleIndex--;
            if (roleIndex < 0) { //reset the index to loop around
                roleIndex = roles.size() - 1;
            }
        }
        for (int i = 0; i < setRoles.size(); i++) {
            if (setRoles.get(i).getPlayerOnRole() != null) {
                //pay actor
                setRoles.get(i).getPlayerOnRole().payBonus();
                setRoles.get(i).getPlayerOnRole().removeRole();
                setRoles.get(i).removeActor();
            }
        }
        for (int i = 0; i < scene.getNumRoles(); i++) {
            if (scene.listRoles().get(i).getPlayerOnRole() != null) {
                scene.listRoles().get(i).getPlayerOnRole().removeRole();
                scene.listRoles().get(i).removeActor();
                scene.listRoles().remove(i); 
            }    
        }
        scene = null;
        
        BoardView.showMessage("This scene has wrapped!");

        Board.updateScenesLeft();
        int scenesLeft = Board.getScenesLeft();
        if (scenesLeft  == 8) {
            Board.nextDay();
            BoardView.showMessage("This day is over!");
            BoardView.showMessage("Days left: " + Board.getDaysLeft());
            System.out.println("Days left: " + Board.getDaysLeft());
            int numPlayers = DeadwoodM.getNumPlayers();
            Queue<Player> players = DeadwoodM.getPlayerQueue();
            Board b = Board.parse(); 
            BoardView view = new BoardView(b);
            for (int i = 0; i < numPlayers; i++) {
                Player curPlayer = players.remove();
                curPlayer.resetChips();
                curPlayer.moveToTrailers(b);
                view.removePlayerDie(curPlayer);
                players.add(curPlayer);
            }
            
            view.putPlayersInTrailers();

        }
    }

    // Adds information about the cost on rank upgrades for the Casting Office.
    public void addUpgrade(String currency, int level, int cost) {
        if (currency.equals("dollar")) {
            dollarUpgrade.put(level, cost);
        } else {
            creditUpgrade.put(level, cost);
        }
    }

    // Lists all available roles on the SceneCard and off the SceneCard
    // at this location.
    public void displayAvailableRoles() {
        if (getSceneCard() == null) {
            System.out.println("There is no scene at this location.");
        } else {
            System.out.println("Available off-card roles (if any) listed below: ");
            for (int i = 0; i < setRoles.size(); i++) {
                if (setRoles.get(i).getPlayerOnRole() == null) {
                    String roleName = setRoles.get(i).getRoleTitle();
                    int reqRank = setRoles.get(i).getReqRank();
                    System.out.println(roleName + " has a required rank of: " + reqRank);
                } 
            }
            System.out.println();
            System.out.println("Available on-card roles (if any) listed below: ");
            for (int i = 0; i < scene.getNumRoles(); i++) {
                if (scene.listRoles().get(i).getPlayerOnRole() == null) {
                    String roleName = scene.listRoles().get(i).getRoleTitle();
                    int reqRank = scene.listRoles().get(i).getReqRank();
                    System.out.println(roleName + " has a required rank of: " + reqRank);
                }
            }

        }
    }

}


