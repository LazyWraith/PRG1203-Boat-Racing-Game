package TrackObject;

import java.io.Serializable;

/**
 * The superclass for Current and Trap objects
 * @author Lee Boon Hoe
 *
 */
public class TrackObject implements Serializable {
	
	/**
	 * Direction of the TrackObject, Trap is -1, Current is 1
	 */
	private int direction;
	
	/**
	 * The number of steps the player's boat get pushed when landed on a TrackObject
	 * <p>It will be between 1 and 3 for both Trap and Current
	 */
	private int strength;
	
	/**
	 * The position of TrackObject within the Track array
	 */
	private int position;
	
	/**
	 * Different symbols that represents each strength of both Trap and Current
	 */
	private String symbol;
	
	/**
	 * An empty constructor to create an empty TrackObject
	 * <p>It is used for easier logical checking when creating Track array
	 */
	public TrackObject(int p) {
		setPosition(p);
		setSymbol(" ");
	}
	
	/**
	 * An overloaded constructor to create TrackObjects with desired direction, strength and position
	 * @param d - Default direction for Traps is -1 and for Currents is 1
	 * @param st - the number of steps the player's boat get pushed when landed on
	 * @param p - the position of the Current in the Track
	 */
	public TrackObject(int d, int st, int p) {
		setDirection(d);
		setStrength(st);
		setPosition(p);
	}
	
	//setter/getters
	/**
	 * Sets the direction of TrackObject
	 * @param d - Default direction for Trap is -1 and for Current is 1
	 */
	private void setDirection(int d) {
		this.direction = d;
	}
	
	/**
	 * Gets the direction of TrackObject
	 * @return Direction of TrackObject
	 */
	public int getDirection() {
		return this.direction;
	}
	
	/**
	 * Sets the strength of TrackObject
	 * @param st - Strength of TrackObject
	 */
	private void setStrength(int st) {
		this.strength = st;
	}
	
	/**
	 * Gets the strength of TrackObject
	 * @return Strength of TrackObject
	 */
	public int getStrength() {
		return this.strength;
	}
	
	/**
	 * Sets the position of TrackObject
	 * @param p - Position of TrackObject
	 */
	private void setPosition(int p) {
		this.position = p;
	}
	
	/**
	 * Gets the position of TrackObject
	 * @return Position of TrackObject
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the symbol of TrackObject
	 * @param sy - Symbol of TrackObject
	 */
	protected void setSymbol(String sy) {
		this.symbol = sy;
	}
	
	/**
	 * Gets the Symbol of TrackObject
	 * @return Symbol of TrackObject
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	//toString
	/**
	 * A toString method is used when printing each TrackObject into the Track array, so users can differentiate between them
	 */
	public String toString() {
		return String.format("%s", this.symbol);
	}
}


