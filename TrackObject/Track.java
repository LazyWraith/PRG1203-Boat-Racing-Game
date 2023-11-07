package TrackObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The class for Track array
 * <p>It will be the main playing area in the game
 * @author Lee Boon Hoe
 *
 */
public class Track implements Serializable {
	
	/**
	 * Each Track object will have an array of TrackObjects
	 */
	private TrackObject[] track;
	
	/**
	 * The size of the Track array, default amount is 100
	 */
	private int trackCount;
	
	/**
	 * The amount of Traps in the Track array, amount varies between difficulties
	 */
	private int trapCount;
	
	/**
	 * The amount of Currents in the Track array, amount will be 10 for Easy, Medium, Hard difficulty
	 */
	private int currentCount;
	
	/**
	 * The difficulty of the Boat Racing game, the user can choose between 0(Easy), 1(Medium), 2(Hard) or 3(Unfair)
	 */
	private int difficulty;

	/**
	 * The amount of chain reactions, which means the amount of times the user move backwards consecutively
	 */
	private int chainNumber = 0;
	
	/**
	 * An array of two integers, which the first is the position of the Trap and the second is the amount of chain reactions
	 */
	private int[] trapChainPair = new int[2];
	
	/**
	 * An array just to keep track of the initial Trap that the algorithm has run through
	 */
	private ArrayList<Integer> initialChainArray = new ArrayList<Integer>();
	
	/**
	 * An empty constructor to create a Track array with Easy difficulty
	 */
	public Track() {
		this(1);
	}
	
	/**
	 * An overloaded constructor to create a Track array with user's desired difficulty
	 * @param difficulty - Difficulty of the Boat Racing game
	 */
	public Track(int difficulty) {
		setTrackCount(100);
		track = new TrackObject[getTrackCount()];
		
		if (difficulty == 0) {
			setTrapCount(10);
			setCurrentCount(30);
			setTrackArray(getTrapCount(), getCurrentCount());
		}else if (difficulty == 1) {
			setTrapCount(20);
			setCurrentCount(20);
			setTrackArray(getTrapCount(), getCurrentCount());
		}else if (difficulty == 2) {
			setTrapCount(30);
			setCurrentCount(10);
			setTrackArray(getTrapCount(), getCurrentCount());
		}else if (difficulty == 3) {
			setTrapCount(40);
			setCurrentCount(0);
			setTrackArray(getTrapCount(), getCurrentCount());
		}else if (difficulty == 4) {
			setTrapCount(60);
			setCurrentCount(0);
			setTrackArray(getTrapCount(), getCurrentCount());
		}
	}
	
	/**
	 * Sets the size of the Track array
	 * @param tc - The size of the Track array
	 */
	public void setTrackCount(int tc) {
		this.trackCount = tc;
	}

	/**
	 * Gets the size of the Track array
	 * @return The size of the Track array
	 */
	public int getTrackCount() {
		return this.trackCount;
	}
	
	/**
	 * Sets the amount of Traps
	 * @param traps - The amount of traps
	 */
	public void setTrapCount(int traps) {
		this.trapCount = traps;
	}
	
	/**
	 * Gets the amount of Traps
	 * @return The amount of Traps
	 */
	public int getTrapCount() {
		return this.trapCount;
	}
	
	/**
	 * Sets the amount of Currents
	 * @param currents - The amount of Currents
	 */
	public void setCurrentCount(int currents) {
		this.currentCount = currents;
	}
	
	/**
	 * Gets the amount of Currents
	 * @return The amount of Currents
	 */
	public int getCurrentCount() {
		return this.currentCount;
	}
	
	/**
	 * Sets the difficulty of the Boat Racing game
	 * @param difficulty - The difficulty of the Boat Racing game
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * Gets the difficulty of the Boat Racing game
	 * @return The difficulty of the Boat Racing game
	 */
	public int getDifficulty() {
		return this.difficulty;
	}
	
	/**
	 * Sets the contents of the Track array according to the amount of Traps and Currents
	 * @param traps - The amount of Traps
	 * @param currents - The amount of Currents
	 */
	private void setTrackArray(int traps, int currents) {
		Random number = new Random();
		int totalTrap = 0;
		int totalCurrent = 0;

		while (totalTrap < traps){
			try {
				int index = number.nextInt(getTrackCount());
				int strength = 1 + number.nextInt(3);
				if (!(track[index + strength] instanceof Trap  || (index + strength) == 99 || (index + strength) == 0)) {
					track[index + strength] = new Trap(strength, index + strength);
					for (int i = 0; i < getTrackCount() - 4; i++){
						if (track[i] instanceof Trap && track[i+1] instanceof Trap && track[i+2] instanceof Trap && track[i+3] instanceof Trap && track[i+4] instanceof Trap){
							track[index + strength] = null;
							totalTrap--;
						}
					}
					if (!(track[index] instanceof Trap)) {
						track[index] = new TrackObject(index);
					}
					totalTrap ++;
				}
			}catch(Exception e){
				
			}
		}
		
		while (totalCurrent < currents){
			try {
				int index = number.nextInt(getTrackCount());
				int strength = 1 + number.nextInt(3);
				
				//First checks for duplicates when generating Track object, also avoids loops between Trap in front
				//the second checks for if it lands on Trap after Current
				if (!(track[index - strength] instanceof TrackObject || track[index] instanceof Trap || (index - strength) == 99 || (index - strength) == 0)) {
					track[index - strength] = new Current(strength, index - strength);
					totalCurrent ++;
				}
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * A method to return the Track array to the caller
	 * @return The Track array generated
	 */
	public TrackObject[] getTrackArray() {
		return track;
	}
	
	/**
	 * A method to get the array of all the Trap chains within the Track array
	 * @return A two-dimensional array of the Trap chains
	 */
	public int[][] getTrackChain() {
		int[][] trackChain = new int[getTrackCount()][2];
		for (int i = getTrackCount() - 2; i > 0; i--) {
			
			for (int j = 0; j < recursiveTrackChain(i).length; j++) {
				trackChain[i][j] = recursiveTrackChain(i)[j];
			}
			
		}
		return trackChain;
	}
	
	/**
	 * A recursive method to find the Traps that are chained together
	 * If it detects that it will land on another Trap when moving backwards, it will run this method again
	 * @param i - The position of the Trap that the algorithm wants to check
	 * @return
	 */
	public int[] recursiveTrackChain(int i) {
			if (track[i] instanceof Trap) {
				initialChainArray.add(track[i].getPosition());
				chainNumber++;
				recursiveTrackChain(i - track[i].getStrength());
			}else {
				if (chainNumber > 1) {
					trapChainPair[0] = initialChainArray.get(0);
					trapChainPair[1] = chainNumber;
					chainNumber = 0;
					initialChainArray.clear();
					return trapChainPair;
				}
				chainNumber = 0;
				initialChainArray.clear();
				trapChainPair[0] = 0;
				trapChainPair[1] = chainNumber;
			}
			return trapChainPair;	
	}
	/**
	 * A toString method 
	 */
	public String toString() {
		return String.format("%s", Arrays.toString(track));
	}
}
