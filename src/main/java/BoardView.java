//Names: Sophie Pallanck, Alex Schwendeman
//This class contains information about the GUI of the game,
//including methods to add and remove images from the board and
//listen to buttons being clicked
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JLayeredPane;


import java.util.*;


import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Rectangle;

public class BoardView extends JFrame {
    
    private static JLabel boardL;
    private static Board board;
    private static JLabel cardL;
    private static JButton move;
    private static JButton takeRole;
    private static JButton act;
    private static JButton rehearse;
    private static JButton upgrade;
    private static JButton end;

    private static JLayeredPane pane;
    private static JPanel playerInfo;

    private JPanel activePlayer;
    private static ImageIcon background;

    
    private static JLabel daysLeft = new JLabel();
    private static JLabel playerName = new JLabel();
    private static JLabel playerRank = new JLabel();
    private static JLabel playerDollars = new JLabel();
    private static JLabel playerCredits = new JLabel();
    private static JLabel playerRehearsalChips = new JLabel();
    private static JLabel playerRoom = new JLabel();
    private static JLabel playerRole = new JLabel();
    private static JLabel playerL;
    

    //Constructs a basic board, adding the buttons, board, scene
    //cards, and shot markers.
    public BoardView(Board board) {
        super("Deadwood");
        this.board = board;
        Set jail = board.getSet("Jail");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pane = getLayeredPane();
        boardL = new JLabel();
        background = new ImageIcon("src\\main\\resources\\img\\board.png");
        boardL.setIcon(background);
        boardL.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());

        playerInfo = new JPanel();
        pane.add(boardL, new Integer(0));
        setSize(background.getIconWidth() + 200, 100 + background.getIconHeight());

        activePlayer = new JPanel();
        activePlayer.setLayout(null);
        activePlayer.setBounds(background.getIconWidth() + 10, 390, 450, 500);
        activePlayer.setBackground(Color.WHITE);

        addSceneCards();
        System.out.println("Have scenes been added");
        addShotMarkers();

        addButtons(background);

    }

    // This method adds scene cards (both fronts and backs) to the display of the board.
    public static void addSceneCards() {
        System.out.println("Adding scene cards");
        TreeMap<String, Set> map = board.getSetMap();
        System.out.println(map.size());
        for (String setName : map.keySet()) {
            Set curSet = map.get(setName);
            if (setName != "Casting Office" && setName != "Trailers") {
                SceneCard curSceneCard = curSet.getSceneCard();
                System.out.println("Set " + curSet.getSetName() + " has the scene " + curSet.getSceneCardName());
                Rectangle area = curSet.getCardLoc();
                placeSceneCards(curSceneCard, area);
                placeSceneCardBack(curSceneCard, area);
            }
        }
    }

    // This method adds scene card fronts to the display of the board.
    public static void placeSceneCards(SceneCard scene, Rectangle area) {
        cardL = new JLabel();
        System.out.println(scene.getCardImg());
        ImageIcon cardImg = new ImageIcon("src\\main\\resources\\img\\" + scene.getCardImg());
        cardL.setIcon(cardImg);
        cardL.setBounds((int) area.getX(), (int) area.getY(), cardImg.getIconWidth(), cardImg.getIconHeight());
        cardL.setOpaque(true);
        scene.setCardFront(cardL);
        pane.add(cardL, new Integer(2));
    }

    // This method adds scene card backs to the display of the board.
    public static void placeSceneCardBack(SceneCard scene, Rectangle area) {
        cardL = new JLabel();
        ImageIcon cardBack = new ImageIcon("src\\main\\resources\\img\\cardback.png");
        cardL.setIcon(cardBack);
        cardL.setBounds((int) area.getX(), (int) area.getY(), cardBack.getIconWidth(), cardBack.getIconHeight());
        cardL.setOpaque(true);
        scene.setCardBack(cardL);
        pane.add(cardL, new Integer(3));
    }

    // This method asks the user how many players they are playing with.
    public int getNumPlayers() {
        String[] playerOptions = new String[] {"2", "3", "4", "5", "6", "7", "8"};
        int choices = JOptionPane.showOptionDialog(null, "How many players?\n", "Welcome to Deadwood Studios, home of the million-movie month.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, playerOptions, playerOptions[0]);
        int numPlayers = (Integer.parseInt(playerOptions[choices]));
        return numPlayers;
    }

    // This method allows a user to input the names of players who
    // will be playing the game.
    public Queue<String> startGame(int numPlayers) {
        Object[] inputMessages = new Object[numPlayers * 2];
        JTextField [] playerNames = new JTextField[numPlayers];
       
       for(int i = 0; i < numPlayers; i++){
         playerNames[i] = new JTextField();
         inputMessages[2 * i] = "player " + (i + 1);
         inputMessages[2 * i + 1] = playerNames[i];
       }
      
        String name;
        Queue<String> names = new LinkedList<>();
        int option = JOptionPane.showConfirmDialog(null, inputMessages, "Welcome To Deadwood!",JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            for(int i = 0; i < numPlayers; i++){
                name = playerNames[i].getText();
                names.add(name);
            
            } 

        } else {
            throw new InputMismatchException("Quit Game!");
        }
        return names;
    }
        
    // This method displays information about the state of the game which inclues: days left, the current player, their rank, their dollars and credits, and the set they're at.
    public static void activePlayer(Player curPlayer, int days) {

        int heightOffset = 380;

        daysLeft.setText("Days Left: " + Integer.toString(days));
        daysLeft.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(daysLeft, new Integer(2));
        heightOffset += 30;

        playerName.setText("Current Player: " + curPlayer.getPlayerName());
        playerName.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerName, new Integer(2));
        heightOffset += 30;
        
        playerRank.setText("Player Rank: " + curPlayer.getPlayerRank());
        playerRank.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerRank, new Integer(2));
        heightOffset += 30;

        playerDollars.setText("Player Dollars: " + curPlayer.getPlayerMoney());
        playerDollars.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerDollars, new Integer(2));
        heightOffset += 30;

        playerCredits.setText("Player Credits: " + curPlayer.getPlayerCredits());
        playerCredits.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerCredits, new Integer(2));
        heightOffset += 30;

        playerRehearsalChips.setText("Rehearsal Chips: " + curPlayer.getRehearsalChips());
        playerRehearsalChips.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerRehearsalChips, new Integer(2));
        heightOffset += 30;

        playerRoom.setText("Player Room: " + curPlayer.getPlayerSet().getSetName());
        playerRoom.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerRoom, new Integer(2));
        heightOffset += 30;

        playerRole.setBounds(background.getIconWidth() + 20, heightOffset, 250, 30);
        pane.add(playerRole, new Integer(2));
        heightOffset += 30;

    }

    // This method displays a given string to the user.
    public static void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    // This method adds shot markers for each scene to the display of the board.
    public static void addShotMarkers() {
        TreeMap<String, Set> map = board.getSetMap();
        for (String setName : map.keySet()) {
            Set curSet = map.get(setName);
            if ((setName != "Casting Office" & setName != "Trailers")) {
                HashMap<Integer, Rectangle> shotLocs = curSet.getShotLocs();
                int shotIndex = curSet.getShotsLeft(); //will be starting shots
                int start = 1;
                for (int take : shotLocs.keySet()) {
                    cardL = new JLabel();
                    ImageIcon shotImg = new ImageIcon("src\\main\\resources\\img\\shot.png");
                    Rectangle shotLoc = shotLocs.get(take);
                    curSet.putJLabelTake(shotLoc, cardL);
                    curSet.setShot(cardL);
                    curSet.setShot(cardL);
                    cardL.setIcon(shotImg);
                    System.out.println("Check");
                    cardL.setBounds((int) shotLoc.getX(), (int) shotLoc.getY(), shotImg.getIconWidth(), shotImg.getIconHeight());
                    cardL.setOpaque(true);
                    pane.add(cardL, new Integer(2));
                }
            }
        } 
    }

    // This method re-adds scene cards to the board after a day ends.
    public static void replaceSceneCard() {
        Board b = Board.parse();
        TreeMap<String, Set> map = b.getSetMap();
        for (String setName : map.keySet()) {
            Set curSet = map.get(setName);
            if (setName != "Casting Office" && setName != "Trailers") {
                SceneCard curSceneCard = curSet.getSceneCard();
                Rectangle area = curSet.getCardLoc();
                placeSceneCards(curSceneCard, area);
                placeSceneCardBack(curSceneCard, area);
                System.out.println(curSet.getSceneCardName());
            }
        }
    }


    // This method takes a Set and removes a particular shot from the given Set.
    public static void removeShot(Set set) {
        HashMap<Rectangle, JLabel> shots = set.getJLabelTake();
        int num = set.getShotsLeft();
        Rectangle shotLoc = set.getShotLocs().get(num);
        JLabel shotL = shots.get(shotLoc);
        pane.remove(shotL);
        pane.revalidate();
        pane.repaint();
    }

    // This method takes a Player and sets their icon according to their color,
    // and rank.
    public void setPlayerIcon(Player curPlayer) {
        int rank = curPlayer.getPlayerRank();
            String color = curPlayer.getPlayerColor();
            ImageIcon playerDie = new ImageIcon("src\\main\\resources\\img\\dice_" + color + rank + ".png");
            JLabel playerL = curPlayer.getPlayerDie();
            playerL.setIcon(playerDie);
            curPlayer.setPlayerDie(playerL);
            pane.add(playerL, new Integer(3));
    }

    // This method places all of the players into the Trailers with a die that indicates an individual player's color and rank.
    public static void putPlayersInTrailers() {

        
        Queue<Player> players = DeadwoodM.getPlayerQueue();
        System.out.println("Number of players: " + players.size());
        // Variables to keep track of the offsets of where each die will be placed.
        // This is so all of the players can be seen in the Trailers at the same time.
        int widthOffset = 0;
        int heightOffset = 0;

        for (int i = 0; i < players.size(); i++) {

            // Information about the player's rank and color is saved.
            Player curPlayer = players.poll();
            int rank = curPlayer.getPlayerRank();
            String color = curPlayer.getPlayerColor();

            // The correct player die image is found using the player's specific rank and color and placed into the Trailers.
            playerL = new JLabel();
            ImageIcon playerDie = new ImageIcon("src\\main\\resources\\img\\dice_" + color + rank + ".png");
            playerL.setIcon(playerDie);
            playerL.setBounds(1000 + widthOffset, 275 + heightOffset, playerDie.getIconWidth(), playerDie.getIconHeight());
            curPlayer.setPlayerDie(playerL);
            pane.add(playerL, new Integer(3));
            players.add(curPlayer);

            // widthOffset updated to put the next die to the right of the previous one.
            widthOffset += playerDie.getIconWidth();

            // widthOffset reset to 0 and heightOffset updated to put the next die underneath the first player die.
            // Done if i is 3 so that if there are more than 4 players, the first row has 4 players before starting the next row of player die.
            if (i == 3) {
                widthOffset = 0;
                heightOffset += playerDie.getIconHeight();
            }
        }
    }

    // This method adds buttons to the right of the board.
    // These buttons include options for the player to move, take a role, act, rehearse, upgrade their rank, and end their turn.
    public void addButtons(ImageIcon img) {

        // Variables to keep track of the offsets of where each button will be placed in the view.
        int widthOffset = img.getIconWidth() + 20;
        int heightOffset = 20;

        move = new JButton("MOVE");
        move.setBackground(new Color(255, 244, 233));
        move.setBounds(widthOffset, heightOffset, 200, 25);
        move.addMouseListener(new MouseListenerTest());
        pane.add(move, new Integer(2));
        heightOffset += 50;

        takeRole = new JButton("TAKE ROLE");
        takeRole.setBackground(new Color(255, 244, 233));
        takeRole.setBounds(widthOffset, heightOffset, 200, 25);
        takeRole.addMouseListener(new MouseListenerTest());
        pane.add(takeRole, new Integer(2));
        heightOffset += 50;

        act = new JButton("ACT");
        act.setBackground(new Color(255, 244, 233));
        act.setBounds(widthOffset, heightOffset, 200, 25);
        act.addMouseListener(new MouseListenerTest());
        pane.add(act, new Integer(2));
        heightOffset += 50;

        rehearse = new JButton("REHEARSE");
        rehearse.setBackground(new Color(255, 244, 233));
        rehearse.setBounds(widthOffset, heightOffset, 200, 25);
        rehearse.addMouseListener(new MouseListenerTest());
        pane.add(rehearse, new Integer(2));
        heightOffset += 50;

        upgrade = new JButton("UPGRADE RANK");
        upgrade.setBackground(new Color(255, 244, 233));
        upgrade.setBounds(widthOffset, heightOffset, 200, 25);
        upgrade.addMouseListener(new MouseListenerTest());
        pane.add(upgrade, new Integer(2));
        heightOffset += 50;

        end = new JButton("END TURN");
        end.setBackground(new Color(255, 244, 233));
        end.setBounds(widthOffset, heightOffset, 200, 25);
        end.addMouseListener(new MouseListenerTest());
        pane.add(end, new Integer(2));
        heightOffset += 50;
        
    }

    // Removes the back of a scene card to simulate a scene card being flipped over.
    public static void removeCardBack(Set set) {
        System.out.println("we're here");
        SceneCard curSceneCard = set.getSceneCard();
        JLabel cardL = curSceneCard.getCardBack();
        pane.remove(cardL);
        pane.revalidate();
        pane.repaint();
    }

    // Removes the scene card to simulate a scene being wrapped and removed from the board.
    public static void removeCardFront(Set set) {
        SceneCard curSceneCard = set.getSceneCard();
        JLabel cardL = curSceneCard.getCardFront();
        pane.remove(cardL);
        pane.revalidate();
        pane.repaint();
    }

    // This method resets the players to the Trailers after a day has concluded.
    public static void resetPlayers() {
        Queue<Player> players = DeadwoodM.getPlayerQueue();
        System.out.println("Number of players: " + players.size());
        int widthOffset = 0;
        int heightOffset = 0;

        for (int i = 0; i < players.size(); i++) {

            // Information about the player's rank and color is saved.
            Player curPlayer = players.poll();
            JLabel playerL = curPlayer.getPlayerDie();
            Icon playerDie = playerL.getIcon();
            playerL.setIcon(playerDie);
            playerL.setBounds(1000 + widthOffset, 275 + heightOffset, playerDie.getIconWidth(), playerDie.getIconHeight());
            
            pane.add(playerL, new Integer(3));
            players.add(curPlayer);

            // widthOffset updated to put the next die to the right of the previous one.
            widthOffset += playerDie.getIconWidth();

            // widthOffset reset to 0 and heightOffset updated to put the next die underneath the first player die.
            // Done if i is 3 so that if there are more than 4 players, the first row has 4 players before starting the next row of player die.
            if (i == 3) {
                widthOffset = 0;
                heightOffset += playerDie.getIconHeight();
            }
        }

    }

    // Removes the player's die image from the display of the board.
    public static void removePlayerDie(Player player) {
        JLabel playerL = player.getPlayerDie();
        pane.remove(playerL);
        pane.revalidate();
        pane.repaint();
    }
    
    public static void onCardMove(Player player, Rectangle cardArea, Rectangle roomArea) {
        JLabel playerL = player.getPlayerDie();
        Icon playerDie = playerL.getIcon();
        playerL.setBounds((int)cardArea.getX()+(int)roomArea.getX(),(int)cardArea.getY()+(int)roomArea.getY(),
        playerDie.getIconWidth(),playerDie.getIconHeight());
        pane.add(playerL,new Integer(4));
    }

    // Simulates moving a player die into a new room on the board.
    public static void movePlayerDie(Player player, Set set, Rectangle area) {
        int numPlayers = set.getPlayersOnSet();
        JLabel playerL = player.getPlayerDie();
        Icon playerDie = playerL.getIcon();
        playerL.setBounds(((int) area.getX() + (playerDie.getIconWidth() * numPlayers)), (int) area.getY(), playerDie.getIconWidth(), playerDie.getIconHeight());
        set.addPlayerOnSet();
        pane.add(playerL, new Integer(4));
    }

    // Simulates moving a player die into a new room and onto the scene card on the board.
    public static void movePlayerDieCard(Player player, Rectangle cardLoc, Rectangle setLoc) {
        JLabel playerL = player.getPlayerDie();
        Icon playerDie = playerL.getIcon();
        playerL.setBounds((int) cardLoc.getX(), (int) cardLoc.getY(), playerDie.getIconWidth(), playerDie.getIconHeight());
        pane.add(playerL, new Integer(4));
    }

    //
    public void showMsg(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    //This inner class handles how the program responds to buttons
    //being pressed.
    public class MouseListenerTest implements MouseListener {

        public void mousePressed(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        // This method hands what happens when each button gets pressed.
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == move) {
                //model.Player.playerTurn("move");
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "move", BoardView.this);
            } else if (e.getSource() == takeRole) {
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "take role", BoardView.this);
                DeadwoodM.nextPlayer();
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "end", BoardView.this);
            } else if (e.getSource() == act) {
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "act", BoardView.this);
                DeadwoodM.nextPlayer();
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "end", BoardView.this);
            } else if (e.getSource() == rehearse) {
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "rehearse", BoardView.this);
                DeadwoodM.nextPlayer();
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "end", BoardView.this);

            } else if (e.getSource() == upgrade) {
                upgrade(DeadwoodM.getCurPlayer());
                DeadwoodM.nextPlayer();
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "end", BoardView.this);

            } else if (e.getSource() == end) {
                System.out.println("End");
                showMessage("End clicked.");
                DeadwoodM.nextPlayer();
                DeadwoodM.playerTurn(DeadwoodM.getCurPlayer(), "end", BoardView.this);
            }
        }

    
        // This method takes a current player as a parameter and asks the player information
        // about which rank they would like to upgrade to.
        public void upgrade(Player curPlayer) {
            String[] desiredRank;
            String[] currencyChoices = new String[] {"Credit", "Dollar"};
            int selected =  JOptionPane.showOptionDialog(null, "Which currency would you like to pay with?", "Text",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, currencyChoices, currencyChoices[0]);
            String choice = currencyChoices[selected];
            
            String[] ranks = new String[] {"2", "3", "4", "5", "6"};
            int option =  JOptionPane.showOptionDialog(null, "Which level would you like?", "Text",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, ranks, ranks[0]);
            int requestedRank = Integer.parseInt(ranks[option]);
            DeadwoodM.upgradeRank(curPlayer, choice, requestedRank, BoardView.this);

        }

    }

}
