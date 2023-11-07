import java.util.Random;

/**
 * The Dice class
 * @author Ng Shang Jun
 */

public class Dice {
	private static int number; // the number is the dice value
    
	//constructor
	/**
	 * Create a Dice instance with a default value of zero
	 * @param number - The Dice value
	 */
	public Dice() {
		number = 0; //default value
	}

	//setters & getters
    /**
     * Gets the Dice value
     * @return The Dice current value
     */
    public static int getNumber() {
		return number;
	}

	/**
	 * Sets the Dice value
	 * @param number - The Dice value
	 */
	public static void setNumber(int number) {
		Dice.number = number;
	}

	//other methods
	/**
	 * Generate a random number from 1 to 6
	 * @return A random Dice value
	 */
	public static int rollDice() {
		Random r = new Random();
        setNumber(r.nextInt(6) + 1);
		return getNumber();
    }
}
