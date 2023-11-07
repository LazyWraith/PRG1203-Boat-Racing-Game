/**
 * Basically a game launcher. Does nothing except launching the game.
 * @author AlphaWoof
 */
public class Main {
	public static void main(String[] args) {
		try {
			Game game = new Game();
			game.launch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}