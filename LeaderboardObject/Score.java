package LeaderboardObject;

import java.io.Serializable;

public class Score implements Serializable{
	
	//attributes
	private final String username;
	private int turnCount;
	private final int difficulty;
	
	//constructors
	public Score() {
		username = "Not Available";
		turnCount = 0;
		difficulty = 0;
	}
	
	public Score(String name, int count, int diff) {
		username = name;
		turnCount = count;
		difficulty = diff;
	}
	
	//setter/getter
	public String getUsername() {
		return username;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}

	public void updateTurnCount(int turnCount) {
		if (turnCount < this.turnCount) this.turnCount = turnCount;
	}

	public int getTurnCount() {
		return turnCount;
	}

	public int getDifficulty() {
		return difficulty;
	}

	//other methods
	@Override
	public String toString() {
		return String.format("Score [username=%s, turnCount=%s, difficulty=%s]", username, turnCount, difficulty);
	}
	


	
}
