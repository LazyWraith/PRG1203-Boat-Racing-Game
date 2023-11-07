import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import PlayerObject.Player;
/**
 * This class initializes GameEngine and starts the game sequence. This class handles user interactions and commands.
 * @author AlphaWoof
 */
public class Game {

    // default settings
    private GameEngine gameEngine;
    private Scanner input = new Scanner(System.in);
    private int playerCount = 2;
    private int difficulty = 1;
    private boolean gameStarted = false;
    private boolean autopilot = false;
    private String[] playername = null;
    private int animationLength = 50;
    private int messageLength = 100;
    private int leaderboardPage = 1;

    // settings
    private boolean displayLeaderboardOnStartup = true;


    /**
     * Starts a new game with specified player count.<p>
     * Players will be asked for their playernames.
     * @param playerCount
     * @throws Exception
     */
    // public void start(int playerCount) throws Exception {
    //     this(playerCount, 1);
    // }
    public void launch() throws Exception {
        if(displayLeaderboardOnStartup) GameEngine.dispLeaderboard(difficulty, false, null);
        waitInteractionEvent();
    }

    /**
     * Quickly start a game without having to enter player names and player count.<p>
     * In this mode, the game defaults to two players, Player 1 & Player 2.
     * @throws Exception
     */
    public void startDefaultAll() throws Exception {
        String[] playername = new String[GameEngine.getDefaultPlayerCount()];
        for (int i = 0; i < playername.length; i++) {
            playername[i] = "Player " + i;
            //playername[i] = String.valueOf(i+1);
        }
        gameEngine = new GameEngine(playername, 1);
        waitInteractionEvent();
    }

    /**
     * Wait for user input to start next game sequence.
     * Handles most player interactions and input. Also checks user input for hotkeys and commands.
     * @throws Exception
     */
    public void waitInteractionEvent() throws Exception {
        while (true) {
            // special menu items if played more than once
            if (!gameStarted) {
                if (playername == null) System.out.println(GameDisplay.menu_newGame1);
                else System.out.println(GameDisplay.menu_newGame2);
            }

            // wait for user input
            String usrInput = input.nextLine();
            
            if (usrInput == "" || usrInput.toLowerCase().equals("y")) {

                // action if game not started: start new game
                if (!gameStarted) {
                    boolean startWithPrevSettings = false;
                    if (playername != null) {
                        System.out.println(GameDisplay.menu_newWithPrev);
                        usrInput = input.nextLine();
                        if (usrInput == "" || usrInput.toLowerCase().equals("y")) startWithPrevSettings = true;
                        else if (!usrInput.toLowerCase().equals("n")) System.out.println("I'll take that as a no");
                    }
                    // if start new game were selected.
                    // skips player & difficulty selection if start with previous settings were selected.
                    if (!startWithPrevSettings) {
                        // Player count selector loop
                        while (true) {
                            System.out.println(GameDisplay.menu_playerAmountSelector);
                            String userInput = input.nextLine();
                            if (userInput == "") {
                                playerCount = 2;
                                break;
                            }
                            playerCount = Integer.parseInt(userInput);
                            
                            if (playerCount == 0) System.out.println("No.");
                            else if (playerCount == 1) System.out.println("You need one more friend to play this game. Too bad you don't have one.");
                            else if (playerCount >= 2 && playerCount < 12) break;
                            else if (playerCount >= 12) System.out.println("Too many players!");
                            else System.out.println(GameDisplay.menu_playerAmountInvalid);
                        }
                        // Difficulty selector loop
                        while (true) {
                            try {
                                System.out.println(GameDisplay.menu_difficultyHelp);
                                System.out.println(GameDisplay.menu_difficultySelector);
                                String userInput = input.nextLine();
                                if (userInput == "") {
                                    difficulty = 1;
                                    break;
                                }
                                difficulty = Integer.parseInt(userInput);
                                if (difficulty >= -1 && difficulty <= 4) break;
                                else System.out.println(GameDisplay.menu_difficultyInvalid);
                            } catch (Exception e) {
                                //TODO: handle exception
                            }
                        }
                        // playername input loop
                        playername = new String[playerCount];
                        for (int i = 0; i < playerCount; i++) {
                            System.out.println("Enter player " + (i + 1) + "'s name (leave blank for default): ");
                            String name = input.nextLine();
                            playername[i] = name == "" ? "Player " + (i + 1) : name;
                        }
                    }
                    leaderboardPage = difficulty;
                    gameEngine = new GameEngine(playername, difficulty);
                    gameStarted = true;
                    //apply if defaults have changed or resuming last settings
                    if (startWithPrevSettings && animationLength != 50) gameEngine.setAnimationlengthScale(animationLength);
                    else animationLength = 50;
                    if (startWithPrevSettings && messageLength != 100) gameEngine.setMessagelengthScale(messageLength);
                    else messageLength = 100;
                }
                else {
                    // autopilot hack lmao
                    // also runs once regardless of autopilot settings
                    do {
                        if (gameEngine.nextSequence()) {
                            // reset some flags after game has ended
                            gameStarted = false;
                            autopilot = false;
                        }
                    } while (autopilot);
                }
            }
            // game commands & hotkeys

            // show help
            else if (usrInput.toLowerCase().equals("h")) System.out.println(GameDisplay.menu_help);

            // save game
            else if (usrInput.toLowerCase().equals("s") && gameStarted) saveGameState();

            // load a saved game
            else if (usrInput.toLowerCase().equals("l")) loadGameState();

            // register slash commands
            else if (usrInput.length() > 1 && usrInput.toLowerCase().substring(0, 1).equals("/")) {
                String[] command = usrInput.substring(1).split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // move current player forwards /fw (int)
                if (command[0].toLowerCase().equals("fw") && command.length == 2 && gameStarted) gameEngine.moveBoatSmooth(Integer.parseInt(command[1]), 1, 400/6);
                
                // move current player backwards /bw (int)
                else if (command[0].toLowerCase().equals("bw") && command.length == 2 && gameStarted) gameEngine.moveBoatSmooth(Integer.parseInt(command[1]), -1, 400/6);
                
                // focus on next player /next
                else if (command[0].toLowerCase().equals("next") && gameStarted) gameEngine.incrCurrentTurnPlayer();

                // leaderboard command
                else if (command[0].toLowerCase().equals("leaderboard")) {
                    //load again in case the leaderboard is updated when the game is running
                    GameEngine.loadLeaderboard();
                    
                    // Set leaderboard manually /leaderboard set (playername) (turnCount) (difficulty)
                    if (command.length == 5 && command[1].toLowerCase().equals("set")) {
                        GameEngine.removeLeaderboardScore(command[2].replaceAll("\"", ""), Integer.parseInt(command[4]));
                        GameEngine.updateLeaderboard(command[2].replaceAll("\"", ""), Integer.parseInt(command[3]), Integer.parseInt(command[4]));
                    }

                    // list all scores (in text form) /leaderboard list
                    else if (command.length == 2 && command[1].toLowerCase().equals("list")) GameEngine.printLeaderboard();

                    // list specific difficulty's leaderboard /leaderboard list (difficulty)
                    else if (command.length == 3 && command[1].toLowerCase().equals("list")) GameEngine.dispLeaderboard(Integer.parseInt(command[2]), false, null);

                    // find a player in leaderboard /leaderboard find (playername) (difficulty)
                    else if (command.length == 4 && command[1].toLowerCase().equals("find")) try {
                        GameEngine.dispLeaderboard(Integer.parseInt(command[3]), true, new Player(command[2].replaceAll("\"", "")));
                    } catch (Exception e) {
                        System.out.println("Player not found!");
                    }

                    // remove a player from leaderboard /leaderboard remove [player | index] (playername) (difficulty)
                    else if (command.length == 5 && command[1].toLowerCase().equals("remove")) {
                        if (command[2].toLowerCase().equals("player")) GameEngine.removeLeaderboardScore(command[3].replaceAll("\"", ""), Integer.parseInt(command[4]));
                        else if (command[2].toLowerCase().equals("index")) GameEngine.removeLeaderboardScore(Integer.parseInt(command[3]), Integer.parseInt(command[4]));
                    }

                    // show leaderboard command help
                    else System.out.println(GameDisplay.menu_leaderboardHelp);
                }

                // auto advance without pressing [Enter]
                else if (command[0].toLowerCase().equals("autopilot")) autopilot = true;

                //set animation length /animationlength (int)
                else if (command.length == 2 && command[0].toLowerCase().equals("animationlength") && gameStarted) {
                    gameEngine.setAnimationlengthScale(Integer.parseInt(command[1]));
                    animationLength = Integer.parseInt(command[1]);
                }

                // set message length /messagelength (int)
                else if (command.length == 2 && command[0].toLowerCase().equals("messagelength") && gameStarted) {
                    gameEngine.setMessagelengthScale(Integer.parseInt(command[1]));
                    messageLength = Integer.parseInt(command[1]);
                }

                // end current game /end
                else if (command.length == 1 && command[0].toLowerCase().equals("end")) gameStarted = false;

                // set displayWidth
                else if (command.length == 2 && command[0].toLowerCase().equals("setwidth")) {
                    GameDisplay.setDisplayWidth(Integer.parseInt(command[1]));
                    if (gameStarted) gameEngine.gameResumed(); // refreshes the screen in game
                }

                // set displayWHeight
                else if (command.length == 2 && command[0].toLowerCase().equals("setheight")) {
                    GameDisplay.setDisplayHeight(Integer.parseInt(command[1]));
                    if (gameStarted) gameEngine.gameResumed(); // refreshes the screen in game
                }

                // no command found or invalid arguments
                else System.out.println("Unknown command");
            }
            // leaderboard navigation
            else if (usrInput.equals("n")) GameEngine.dispLeaderboard(++leaderboardPage > 3 ? leaderboardPage = 3 : leaderboardPage, false, null);
            else if (usrInput.equals("p")) GameEngine.dispLeaderboard(--leaderboardPage < 0 ? leaderboardPage = 0 : leaderboardPage, false, null);
            // quit game
            else if (usrInput.equals("q")) break;
        }
    }

    /**
     * Saves the current game
     */
    public void saveGameState() {
        System.out.println(GameDisplay.menu_savingGame);
        try {
            FileOutputStream fos = new FileOutputStream("save.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // write object to file
            oos.writeObject(gameEngine);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(GameDisplay.menu_savedGame);
    }

    /**
     * Loads a saved game
     */
    public void loadGameState() {
        System.out.println(GameDisplay.menu_loadingGame);
        try {
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream("save.dat"));
            gameEngine = (GameEngine)fis.readObject();
            gameEngine.gameResumed();
            gameStarted = true;
            fis.close();
            System.out.println(GameDisplay.menu_loadedGame);
        } catch (InvalidClassException e) {
            System.out.println(GameDisplay.menu_loadGameFailMismatch);
            e.printStackTrace();
            System.out.println(GameDisplay.menu_newGame1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
