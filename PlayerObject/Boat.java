package PlayerObject;

import java.io.Serializable;

/**
 * The Boat class
 * @author Ng Shang Jun
 */
public class Boat implements Serializable {
    private int position;
    private int lastposition;
    private String color;
    
    //constructors
    /**
     * Create a Boat instance with default values
     */
    public Boat() {
    	this(0, "white");
    }
    
    /**
     * Create a Boat instance with user values
     * @param p - The Boat initial position
     * @param c - The Boat color
     */
    public Boat(int p, String c) {
    	position = p;
    	color = c;
    }

    //setters & getters
	/**
	 * Gets the Boat current position
	 * @return The Boat current position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the Boat current position
	 * @param position - The Boat current position
	 */
	public void setPosition(int position) {
		setLastPosition(getPosition());
		if (this.position > 100) {
			this.position = 100;
			this.position = position;
		} else {
			this.position = position;
		}
	}

	/**
	 * Gets the Boat previous position
	 * @return The Boat previous position
	 */
	public int getLastPosition() {
		return lastposition;
	}
	
	/**
	 * Update the Boat previous position
	 * @param lastposition - The Boat previous position
	 */
	private void setLastPosition(int lastposition) {
		this.lastposition = lastposition;
	}
	
	/**
	 *Gets the Boat color
	 * @return The Boat color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the Boat color
	 * @param color - The Boat color
	 */
	public void setColor(String color) {
		this.color = color;
	}
    
    //other methods
	/**
	 * Moves the Boat from the current position to another
	 * @param m - Boat move amount
	 */
	public void move(int m) {
		setPosition(getPosition() + m);
	}
}
