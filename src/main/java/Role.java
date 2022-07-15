import java.awt.Rectangle;

//Names: Sophie Pallanck, Alex Schwendeman
//Keeps track of the details of a role and which Player object is associated 
//with the role
public class Role {

    private int reqRank;
    private String roleTitle;
    private Player actor;
    private String line;
    private Rectangle loc;

    // Role constructor that creates a Role object with the provided title of the role, the line said by the role, and the required rank needed to take the role.
    public Role (String roleTitle, String line, int reqRank, Rectangle loc) {
        this.roleTitle = roleTitle;
        this.reqRank = reqRank;
        this.line = line;
        this.loc = loc;
    }

    // Gets the title of the role.
    public String getRoleTitle() {
        return roleTitle;
    }

    // Gets the required rank needed to take the role.
    public int getReqRank() {
        return reqRank;
    }

    // Gets the name of the player working the role.
    public Player getPlayerOnRole() {
        return actor;
    }

    // Gets the line said by the role.
    public String getRoleLine() {
        return line;
    }

    // Prints information about the role, including who is working the role (or no one if the role is available), the title of the role, and the line said by the role.
    public String roleInfo() {
        String result;
        if (actor != null) {
            result = actor.name + " is working " + roleTitle + ", \"" + line + "\"";
        } else {
            result = "No one is working this role: " + roleTitle + ", \"" + line + "\"";
        }
        return result;
    }

    // Assigns a player to the role.
    public void takePart(Player player) {
        this.actor = player;
    }

    // Resets the player who is working the role to be null.
    // This will be called when the scene is wrapped.
    public void removeActor() {
        this.actor = null;
    }

    public Rectangle getRoleLoc() {
        return loc;
    }

}
