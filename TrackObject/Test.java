package TrackObject;

import java.util.ArrayList;
import java.util.Collections;

public class Test{
	public static void main(String[] arg) {

		System.out.println("Program starting...");
		Track track = new Track(4);
		ArrayList<Integer> max = new ArrayList<Integer>();
		System.out.println(track);
		
		int trap = 0;
		int current = 0;
		for (int i = 0; i < 100; i++) {
			if (track.getTrackArray()[i] instanceof Trap) {
				trap++;
			}else if (track.getTrackArray()[i] instanceof Current) {
				current++;
			}
		}	
		System.out.println("Trap: " + trap +", " + "Current: " + current);

		for (int[] i: track.getTrackChain()) {
			max.add(i[1]);
		}
		int max1 = Collections.max(max);
		System.out.println("Max: " + max1);
	}
}