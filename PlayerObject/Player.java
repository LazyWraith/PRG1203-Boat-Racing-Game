package PlayerObject;

import java.io.Serializable;

/**
 * The Player class
 * @author Ng Shang Jun
 */
public class Player implements Serializable {
    private Boat boat = new Boat();
    private String username;
    private int turnCount;
    
    //constructors
    /**
     * Create a Player instance with the default username
     */
    public Player() {
    	this("Player");
    }
    
    /**
     * Create a Player instance with their own username
     * @param uname - In-game name of Player
     */
    public Player(String uname) {
    	username = uname;
    }

    //setters & getters
    /**
     * To get the player boat
     * @return The Player's boat
     */
    public Boat getBoat() {
    	return boat;
    }
    
	/**
	 * To get the Player's username
	 * @return the Player's in-game name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * To set the Player's username
	 * @param username - The Player's in-game name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * To get the number of turns passed
	 * @return The number of turns passed
	 */
	public int getTurnCount() {
		return turnCount;
	}

	/**
	 * To set the number of turns passed
	 * @param turnCount - The number of turns passed
	 */
	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}

	/**
	 * Increase turn count
	 * @param turnCount - The number of turns passed
	 */
	public void incrTurnCount() {
		turnCount++;
	}
}
