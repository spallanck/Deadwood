

//Names: Sophie Pallanck, Alex Schwendemen 
//This class is responsible for parsing information about the Board and 
//the SceneCards and setting up the complete Board.
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.awt.Rectangle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.*;

public class Board {

    private static int daysLeft;
    private static int scenesLeft;
    private static TreeMap <String, Set> setMap = new TreeMap<>();
    private static Board board = new Board();
    private static ArrayList<SceneCard> sceneCards = new ArrayList<>();

    // Returns an instance of the Board and creates the SceneCards for the game.
    public static Board makeBoard(int days, String cardsPath) {
        daysLeft = days;
        setUpScenes(cardsPath);
        return board; 

    }

    public static Board parse() {
        return board;
    }

    public TreeMap<String, Set> getSetMap() {
        return setMap = new TreeMap<String, Set>(setMap);
    }

    // Returns the number of days left in the game.
    public static int getDaysLeft() {
        return daysLeft;
    }

    // Returns the number of scenes left to be shot in a day.
    public static int getScenesLeft() {
        return scenesLeft;
    }

    // Decreases the number of scenes left and prints the updated scenes left.
    public static void updateScenesLeft() {
        scenesLeft--;
        System.out.println("Scenes left: " + scenesLeft);
    }

    // Resets the board for the next day of the game.
    public static void nextDay() {
        for (Set set : setMap.values()) {
            if (!set.getSetName().equals("Casting Office") && !set.getSetName().equals("Trailers")) {
                set.resetShots();
                int index = (int) (Math.random() * sceneCards.size());
                System.out.println("Resetting");
                set.resetSceneCard(sceneCards.get(index));
                System.out.println("Set " + set.getSetName() + "has scene " + set.getSceneCardName());
                sceneCards.remove(index);
                ArrayList<Role> setRoles = set.getSetRoles();
                System.out.println("Removing roles");
                for (int i = 0; i < setRoles.size(); i++) {
                    if (setRoles.get(i).getPlayerOnRole() != null) {
                        setRoles.get(i).getPlayerOnRole().removeRole();
                        setRoles.get(i).removeActor();
                    }
                }

            }
        }
        daysLeft--;
        scenesLeft = 10;

    }

    // Takes a string representing the xml files containing information about
    // the SceneCards and creates all SceneCards.
    public static void setUpScenes(String cardPath) {
        try {
            File input = new File(cardPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            SceneCard thisScene = null;
            doc = db.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodesList = doc.getElementsByTagName("card");
            for (int i = 0; i < nodesList.getLength(); i++) {
                Node node = nodesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element nodeElement = (Element) node;
                    String movieName = nodeElement.getAttribute("name");
                    int budget = Integer.parseInt(nodeElement.getAttribute("budget"));
                    String sceneImg = nodeElement.getAttribute("img");
                    ArrayList<Role> rList = new ArrayList<>();
                    int sceneNum = Integer.parseInt(((Element) nodeElement.getElementsByTagName("scene").item(0)).getAttribute("number"));
                    String movieDesc = nodeElement.getElementsByTagName("scene").item(0).getTextContent().trim();
                    NodeList parts = nodeElement.getElementsByTagName("part");

                    // Parsing the parts for each scene.
                    for (int j = 0; j <  parts.getLength(); j++) {
                        Node partNode = parts.item(j);
                        if (partNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element partElement = (Element) partNode;
                            String roleName = partElement.getAttribute("name");
                            int rank = Integer.parseInt(partElement.getAttribute("level"));
                            String line = partElement.getElementsByTagName("line").item(0).getTextContent().trim();
                            
                            NodeList area = partElement.getElementsByTagName("area");
                            Node areaText = area.item(0);
                            Element areaInfo = (Element) areaText;
                            int x = Integer.parseInt(areaInfo.getAttribute("x"));
                            int y = Integer.parseInt(areaInfo.getAttribute("y"));
                            int h = Integer.parseInt(areaInfo.getAttribute("h"));
                            int w = Integer.parseInt(areaInfo.getAttribute("w"));
                            Rectangle cardLoc = new Rectangle(x, y, w, h);

                            Role nextRole = new Role(roleName, line, rank, cardLoc);
                            rList.add(nextRole);
                        }
                    }
                    thisScene = new SceneCard(rList, budget, sceneNum, movieName, movieDesc, sceneImg);
                    sceneCards.add(thisScene);
                }
            }
        } catch (Exception exc) {
            System.out.println("Error while parsing the scene cards.");
            exc.printStackTrace();
        }
    }

    // Takes a string representing the xml files containing information about
    // the Board and creates all sets.
    public static void setUpSets(String boardPath) {
        

        try {
            File input = new File(boardPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            doc = db.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodesList = doc.getElementsByTagName("board");

            for (int i = 0; i < nodesList.getLength(); i++) {
                Node nextNode = nodesList.item(i);
                if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element root = (Element) nextNode;
                    NodeList sets = root.getElementsByTagName("set");
                    NodeList trailer = root.getElementsByTagName("trailer");
                    NodeList castingOffice = root.getElementsByTagName("office");
                    for (int j = 0; j < sets.getLength(); j++) {
                        Node setInfo = sets.item(j);
                        if (setInfo.getNodeType() == Node.ELEMENT_NODE) {
                            Element setDesc =(Element) setInfo;
                            NodeList parts = setDesc.getElementsByTagName("part");
                            NodeList neighbors = setDesc.getElementsByTagName("neighbor");
                            NodeList takes = setDesc.getElementsByTagName("take");

                            NodeList area = setDesc.getElementsByTagName("area");
                            Element areaInfo = (Element) area.item(0);
                            int x = Integer.parseInt(areaInfo.getAttribute("x"));
                            //System.out.println(x);
                            int y = Integer.parseInt(areaInfo.getAttribute("y"));
                            int h = Integer.parseInt(areaInfo.getAttribute("h"));
                            int w = Integer.parseInt(areaInfo.getAttribute("w"));
                            Rectangle setArea = new Rectangle(x, y, w, h);

                            String setName;
                            if (setDesc.getAttribute("name").equals("trailer")) {
                                setName = "Trailers";
                            } else if (setDesc.getAttribute("name").equals("office")) {
                                setName = "Casting Office";
                            } else {
                                setName = setDesc.getAttribute("name");
                            }
                            int takesCount = 0;
                            int xA = 0;
                            int yA = 0;
                            int hA = 0;
                            int wA = 0;
                            HashMap<Integer, Rectangle> shotLocs = new HashMap<>();
                            for (int l = 0; l < takes.getLength(); l++) {
                                takesCount++;
                                shotLocs.put(takesCount, new Rectangle(xA, yA, wA, hA));
                            }
                            takesCount = 1;
                            for (int n = 0; n < takes.getLength(); n++) {
                                Element takeE = (Element) takes.item(n);
                                NodeList takeA = takeE.getElementsByTagName("area");
                                for (int p = 0; p < takeA.getLength(); p++) {
                                    Element takeArea = (Element) takeA.item(p);
                                    xA = Integer.parseInt(takeArea.getAttribute("x"));
                                    yA = Integer.parseInt(takeArea.getAttribute("y"));
                                    hA = Integer.parseInt(takeArea.getAttribute("h"));
                                    wA = Integer.parseInt(takeArea.getAttribute("w"));
                                }
                                shotLocs.put(takesCount, new Rectangle(xA, yA, wA, hA));
                                takesCount++;
                            }
                            
                            Set thisSet = new Set(setName, takesCount, shotLocs, setArea);
                            int index = (int) (Math.random() * sceneCards.size());
                            thisSet.resetSceneCard(sceneCards.get(index));
                            sceneCards.remove(index);
                            scenesLeft++;

                            for (int k = 0; k < parts.getLength(); k++) {
                                Element partDesc = (Element) parts.item(k);
                                String partTitle = partDesc.getAttribute("name");
                                String line = partDesc.getTextContent().trim();
                                int rank = Integer.parseInt(partDesc.getAttribute("level"));
                                NodeList partArea = partDesc.getElementsByTagName("area");
                                int xP = 0;
                                int yP = 0;
                                int hP = 0;
                                int wP = 0;
                                for (int z = 0; z < partArea.getLength(); z++) {
                                    Element areaI = (Element) partArea.item(z);
                                    xP = Integer.parseInt(areaI.getAttribute("x"));
                                    yP = Integer.parseInt(areaI.getAttribute("y"));
                                    hP = Integer.parseInt(areaI.getAttribute("h"));
                                    wP = Integer.parseInt(areaI.getAttribute("w"));
                                }
                                Rectangle partInfo = new Rectangle (xP, yP, wP, hP);
                                thisSet.addRole(partTitle, line, rank, partInfo);
                                
                            }
                            setMap.put(thisSet.getSetName(), thisSet);
                           
                            
                        }
                    }
                    //setMap.put("Trailers", new Set("Trailers"));
                    //setMap.put("Casting Office", new Set("Casting Office"));
                    
                    for (int g = 0; g < trailer.getLength(); g++) {
                        Node trailerN = trailer.item(g);
                        if (trailerN.getNodeType() == Node.ELEMENT_NODE) {
                            Element trailerE = (Element) trailerN;
                            NodeList trailerA = trailerE.getElementsByTagName("area");
                            Element trailerArea = (Element) trailerA.item(0);
                            int xT = Integer.parseInt(trailerArea.getAttribute("x"));
                            int yT = Integer.parseInt(trailerArea.getAttribute("y"));
                            int hT = Integer.parseInt(trailerArea.getAttribute("h"));
                            int wT = Integer.parseInt(trailerArea.getAttribute("w"));
                            setMap.put("Trailers", new Set("Trailers", new Rectangle(xT, yT, wT, hT)));
                        }
                    }
                    // Parsing upgrade information from the Casting Office.
                    for (int k = 0; k < castingOffice.getLength(); k++) {
                        Node castingNode = castingOffice.item(k);
                        if (castingNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element castingElement = (Element) castingNode;
                            NodeList upgrades = castingElement.getElementsByTagName("upgrade");
                            NodeList officeA = castingElement.getElementsByTagName("area");
                            Element officeInfo = (Element) officeA.item(0);
                            int x = Integer.parseInt(officeInfo.getAttribute("x"));
                            int y = Integer.parseInt(officeInfo.getAttribute("y"));
                            int h = Integer.parseInt(officeInfo.getAttribute("h"));
                            int w = Integer.parseInt(officeInfo.getAttribute("w"));
                            setMap.put("Casting Office", new Set("Casting Office", new Rectangle(x, y, w, h)));
                           
                            for (int n = 0; n < upgrades.getLength(); n++) {
                                Node upgradeNode = castingElement.getElementsByTagName("upgrade").item(n);
                                Element upgradeElement = (Element) upgradeNode;
                                int level = Integer.parseInt(upgradeElement.getAttribute("level"));
                                int cost = Integer.parseInt(upgradeElement.getAttribute("amt"));
                                String currency = upgradeElement.getAttribute("currency");
                                setMap.get("Casting Office").addUpgrade(currency, level, cost);
                            }
                        }
                    }
                    }
                    
                    
                }
                
                makeNeighbors(boardPath);
            
        
        } catch (Exception exc) {
            System.out.println("Error while parsing the board.");
            exc.printStackTrace();
        }

    }

    // Takes a string representing the xml files containing information about
    // the Board and associates sets with their neighbors.
    public static void makeNeighbors(String boardPath) {
        try {
            File input = new File(boardPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            doc = db.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodesList = doc.getElementsByTagName("board");

            for (int i = 0; i < nodesList.getLength(); i++) {
                Node nextNode = nodesList.item(i);
                if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element root = (Element) nextNode;
                    NodeList sets = root.getElementsByTagName("set");
                    NodeList trailer = root.getElementsByTagName("trailer");
                    NodeList castingOffice = root.getElementsByTagName("office");
                    for (int j = 0; j < sets.getLength(); j++) {
                        Node setInfo = sets.item(j);
                        if (setInfo.getNodeType() == Node.ELEMENT_NODE) {
                            Element setDesc =(Element) setInfo;
                            NodeList neighbors = setDesc.getElementsByTagName("neighbor");
                            String setName;
                            if (setDesc.getAttribute("name").equals("trailer")) {
                                setName = "Trailers";
                            } else if (setDesc.getAttribute("name").equals("office")) {
                                setName = "Casting Office";
                            } else {
                                setName = setDesc.getAttribute("name");
                            }
                            
                            for (int m = 0; m < neighbors.getLength(); m++) {
                                Element setNeighbors = (Element) neighbors.item(m);
                                String neighborName = "";
                                if (setNeighbors.getAttribute("name").equals("office")) {
                                    neighborName = "Casting Office";
                                } else if (setNeighbors.getAttribute("name").equals("trailer")) {
                                    neighborName = "Trailers";
                                } else {
                                    neighborName = setNeighbors.getAttribute("name");                              
                                }
                                setMap.get(setName).addNeighbor(setMap.get(neighborName));
                            }
                            
                        }
                    }
                    // Setting up neighbors for Trailers.
                    for (int k = 0; k < trailer.getLength(); k++) {
                        Node trailerNode = trailer.item(k);
                        if (trailerNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element trailerElement = (Element) trailerNode;
                            NodeList neighborsT = trailerElement.getElementsByTagName("neighbor");
                            for (int m = 0; m < neighborsT.getLength(); m++) {
                                Element setNeighbors = (Element) neighborsT.item(m);
                                String neighborName = "";
                                if (setNeighbors.getAttribute("name").equals("office")) {
                                    neighborName = "Casting Office";
                                } else {
                                    neighborName = setNeighbors.getAttribute("name");
                                }
                                setMap.get("Trailers").addNeighbor(setMap.get(neighborName));
                            }
                        }

                    }
                    // Setting up neighbors for the Casting Office.
                    for (int k = 0; k < castingOffice.getLength(); k++) {
                        Node castingNode = castingOffice.item(k);
                        if (castingNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element castingElement = (Element) castingNode;
                            NodeList castingNeighbors = castingElement.getElementsByTagName("neighbor");
                            for (int m = 0; m < castingNeighbors.getLength(); m++) {
                                Element castNeighbors = (Element) castingNeighbors.item(m);
                                String neighborName = "";
                                if (castNeighbors.getAttribute("name").equals("trailer")) {
                                    neighborName = "Trailers";
                                } else {
                                    neighborName = castNeighbors.getAttribute("name");
                                }
                                setMap.get("Casting Office").addNeighbor(setMap.get(neighborName));
                            }
                           
                        }
                    }
                    
                } 
            }
        } catch (Exception exc) {
            System.out.println("Error while parsing the board.");
            exc.printStackTrace();
        }

    }

    // Takes the name of a set and returns the corresponding Set object.
    public Set getSet(String setName) {
        return setMap.get(setName);
    }

}
