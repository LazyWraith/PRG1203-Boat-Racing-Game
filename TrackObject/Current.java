package TrackObject;

/**
 * Creating Current object using inheritance of TrackObject
 * @author Lee Boon Hoe
 *
 */
public class Current extends TrackObject{
    
	/**
	 * Calling constructor of superclass
	 * <p>
	 * Direction for Current by default is 1
	 * <p>
	 * Also sets the specialised symbols for each strength here
	 * @param st - the number of steps the player's boat get pushed when landed on
	 * @param p - the position of the Current in the Track
	 */
	public Current(int st, int p) {
		super(1, st, p);
		switch(st) {
		case 1:
			setSymbol("-");
			break;
		case 2:
			setSymbol("=");
			break;
		case 3:
			setSymbol("≡");
			break;
		}
	}
}
