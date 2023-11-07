package TrackObject;

/**
 * Creating Trap object using inheritance of TrackObject
 * @author Lee Boon Hoe
 *
 */
public class Trap extends TrackObject{

	/**
	 * Calling constructor of superclass
	 * <p>
	 * Direction for Trap by default is -1
	 * <p>
	 * Also sets the specialised symbols for each strength here
	 * @param st - the number of steps the player's boat get pushed when landed on
	 * @param p - the position of the Trap in the Track
	 */
	public Trap(int st, int p) {
		super(-1, st, p);
		switch(st) {
		case 1:
			setSymbol("(");
			break;
		case 2:
			setSymbol("[");
			break;
		case 3:
			setSymbol("{");
			break;
		}
	}
}
