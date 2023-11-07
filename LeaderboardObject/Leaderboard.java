package LeaderboardObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard implements Serializable{
	
	//attributes
	private ArrayList<Score> scorelist = new ArrayList<Score>();
	private int leaderboardDifficulty;
	
	//constructors
	//constructor when no input
	public Leaderboard() {
		leaderboardDifficulty = -1;
	}
	
	public Leaderboard(int difficulty) {
		leaderboardDifficulty = difficulty;
	}
	
	//constructor, insert as many scores as you want
	public Leaderboard(int diff, Score...scores) {
		//makes sure that every score is of the same difficulty as the leaderboard
		this.leaderboardDifficulty = diff;
		for (Score s: scores) {
			if (s.getDifficulty() == diff) {
				scorelist.add(s);
			}			
		}
		
		//sorts the array by turncount
		Collections.sort(scorelist, new Comparator<Score>() {
			public int compare(Score score1, Score score2) {
				return Integer.valueOf(score1.getTurnCount()).compareTo(score2.getTurnCount());
			}
		});
	}
	
	//setter,getter
	public int getLeaderboardDifficulty() {
		return this.leaderboardDifficulty;
	}
	
	public void setLeaderboardDifficulty(int diff) {
		this.leaderboardDifficulty = diff;
	}	
	
	//Get's the entire leaderboard
	public ArrayList<Score> getScorelist(){
		return this.scorelist;
	}
	
	//Get's a specific score within the leaderboard by index
	public Score getScoreByIndex(int index){
		return this.scorelist.get(index);
	}
	
	//Get's a specific score within the leaderboard by playername
	public Score getScoreByPlayername(String playername){
		int index = -1;
		for (int i = 0; i < scorelist.size(); i++) {
			if (scorelist.get(i).getUsername().equals(playername)) {
				index = i;
				break;
			}
		}
		String exceptionMessage = String.format("%s's score cannot be found", playername);
		if (index == -1) throw new IndexOutOfBoundsException(exceptionMessage);
		return this.scorelist.get(index);
	}
	
	//remove a score from the leaderboard
	public void removeScore(int index) {
		scorelist.remove(index);
	}
	
	//remove a player from the leaderboard
	public void removeScore(String playername) {
		int index = -1;
		for (int i = 0; i < scorelist.size(); i++) {
			if (scorelist.get(i).getUsername().equals(playername)) {
				index = i;
				break;
			}
		}
		String exceptionMessage = String.format("%s's score cannot be found", playername);
		if (index == -1) throw new IndexOutOfBoundsException(exceptionMessage);
		else scorelist.remove(index);
	}
	
	//other methods
	public void updateLeaderboard(Score newscore) throws Exception {
		if (newscore.getDifficulty() == this.leaderboardDifficulty) {

			// check if player exists in leaderboard
			boolean isNewPlayer = true;
			for (int i = 0; i < scorelist.size(); i++) {
				if (scorelist.get(i).getUsername().equals(newscore.getUsername())) {
					scorelist.get(i).updateTurnCount(newscore.getTurnCount());
					isNewPlayer = false;
				}
			}
			if (isNewPlayer) scorelist.add(newscore);
				
			Collections.sort(scorelist, new Comparator<Score>() {
				public int compare(Score score1, Score score2) {
					return Integer.valueOf(score1.getTurnCount()).compareTo(score2.getTurnCount());
				}
			});
		}
		//if newscore is not of the same difficulty as the leaderboard.
		else {
			throw new Exception("Invalid score difficulty");
			//System.out.println("This score is not compatible with this difficulty!");
		}
			
	}


	@Override
	public String toString(){
		switch(scorelist.size()) {
		case 0:
			return String.format("Difficulty level: %s\nLeaderboard:\nNo one played the game :(", getLeaderboardDifficulty());
		case 1:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n", getLeaderboardDifficulty(), scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount());
		case 2:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n2. %s %d turns\n", getLeaderboardDifficulty(),scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount(), scorelist.get(1).getUsername(), scorelist.get(1).getTurnCount());
		case 3:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n2. %s %d turns\n3. %s %d turns\n", getLeaderboardDifficulty(),scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount(), scorelist.get(1).getUsername(), scorelist.get(1).getTurnCount(), scorelist.get(2).getUsername(), scorelist.get(2).getTurnCount());
		case 4:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n2. %s %d turns\n3. %s %d turns\n4. %s %d turns\n", getLeaderboardDifficulty(),scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount(), scorelist.get(1).getUsername(), scorelist.get(1).getTurnCount(), scorelist.get(2).getUsername(), scorelist.get(2).getTurnCount(), scorelist.get(3).getUsername(), scorelist.get(3).getTurnCount());
		case 5:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n2. %s %d turns\n3. %s %d turns\n4. %s %d turns\n5. %s %d turns\n", getLeaderboardDifficulty(),scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount(), scorelist.get(1).getUsername(), scorelist.get(1).getTurnCount(), scorelist.get(2).getUsername(), scorelist.get(2).getTurnCount(), scorelist.get(3).getUsername(), scorelist.get(3).getTurnCount(), scorelist.get(4).getUsername(), scorelist.get(4).getTurnCount());
		default:
			return String.format("Difficulty Level: %s\nLeaderboard:\n1. %s %d turns\n2. %s %d turns\n3. %s %d turns\n4. %s %d turns\n5. %s %d turns\n", getLeaderboardDifficulty(),scorelist.get(0).getUsername(), scorelist.get(0).getTurnCount(), scorelist.get(1).getUsername(), scorelist.get(1).getTurnCount(), scorelist.get(2).getUsername(), scorelist.get(2).getTurnCount(), scorelist.get(3).getUsername(), scorelist.get(3).getTurnCount(), scorelist.get(4).getUsername(), scorelist.get(4).getTurnCount());
		}
	}
	
	
}
