//Names: Sophie Pallanck, ALex Schwendeman
//This class represents the Scene Cards players will flip
//and is responsible for keeping tracking of the scene information
//such as the roles associated with the scene and the budget of the movie.
import java.util.*;
import javax.swing.JLabel;

public class SceneCard {

    private ArrayList<Role> sceneRoles;
    private int budget;
    private int sceneNum;
    private boolean flipped;
    private String sceneName;
    private String sceneInfo;
    private String cardImg;
    private JLabel cardBack;
    private JLabel cardFront;

    // SceneCard constructor that creates a SceneCard object with the provided roles listed on the scene card, the budget of the film, the scene card number, the name of the scene, and the written information about the scene.
    public SceneCard(ArrayList<Role> sceneRoles, int budget, int sceneNum, String sceneName, String sceneInfo, String cardImg) {
        this.sceneRoles = sceneRoles;
        this.sceneNum = sceneNum;
        this.budget = budget;
        this.sceneName = sceneName;
        this.sceneInfo = sceneInfo;
        this.cardImg = cardImg;
        flipped = false;
    }

    // Gets the name of the scene.
    public String getSceneName() {
        return sceneName;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    // Gets the scene card number.
    public int getSceneNum() {
        return sceneNum;
    }

    // Gets the budget of the film.
    public int getBudget() {
        return budget;
    }

    // Checks whether the scene card has been flipped over yet.
    public boolean checkCardFlipped() {
        return flipped;
    }

    // Flips the scene card over to be face-up.
    public void flipCard() {
        this.flipped =true;
    }

    // Gets the written information about the scene.
    public String getSceneInfo() {
        return sceneInfo;
    }

    // 
    public void setCardBack(JLabel cardback) {
        this.cardBack = cardback;
    }

    // 
    public JLabel getCardBack() {
        return this.cardBack;
    }

    // 
    public void setCardFront(JLabel cardfront) {
        this.cardFront = cardfront;
    }

    // 
    public JLabel getCardFront() {
        return this.cardFront;
    }

    // Returns true if the dice roll is greater than the budget
    // and false otherwise.
    public boolean checkRoll(int roll) {
        return (roll < budget);
    }

    // Gets the number of roles on the scene card.
    public int getNumRoles() {
        return sceneRoles.size();
    }

    // Lists the roles that are on the scene card.
    public ArrayList<Role> listRoles() {
        return sceneRoles;
    }

}
