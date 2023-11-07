import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

import LeaderboardObject.*;
import PlayerObject.*;
import TrackObject.*;

/**
 * Boat-racing game's core. This class handles the calculation of the game itself, and calls GameDisplay to draw a new frame on update. This class also handles the loading and saving of the game data and leaderboard data.
 * @author AlphaWoof
 */
public class GameEngine implements Serializable {

    // GE resources
    private Object[] displayData = null; // datas to be sent to display driver
    private Player[] player = null; // an array of player objects. Size of array will be declared in constructor
    private int playerCount = 2; // default playerCount. Used to determine whose turn right now
    private int difficulty = 1;
    private static int defaultPlayerCount = 2; // default playerCount
    private Track track = null; // track will be declared in constructor
    private static Leaderboard[] leaderboard = new Leaderboard[4];

    // event message
    private int[] currentChain;
    private int[] trapChain;
    private int[] trapChainCount;
    private TrackObject[] trapChainHead;
    private boolean[] backTracking;
    
    // Indicate whose turn at current time
    private int currentTurn = 0;

    // settings
    private boolean debug = false; // shows debug info
    private boolean hideDiceAnimation = false; // Default: false. Hides rolling dice animation.
    private int fps = 60; // Default: 30. Higher number will make animation smoother, but animation may get choppy.
    private int animationlengthScale = 50; // Default: 50. Lower number will make animation faster, but animation may get choppy.
    private int messagelengthscale = 100; // Default: 100. Determines how long a message should be displayed.
    private int interpolation = 4; // Default: 4. Interpolates some frames. Higher number may make animations smoother, but animation may get choppy

    /**
     * Creates a new game instance with provided usernames and default difficulty (medium)
     * @param playerNames
     */
    public GameEngine(String[] playerNames) {
        this(playerNames, 1);
    }

    /**
     * Creates a new game instance with provided usernames and default difficulty (medium)
     * @param playerNames
     * @param difficulty
     */
    public GameEngine(String[] playerNames, int difficulty) {
        this.difficulty = difficulty;
        playerCount = playerNames.length;
        player = new Player[playerCount];
        currentChain = new int[playerCount];
        trapChain = new int[playerCount];
        trapChainCount = new int[playerCount];
        trapChainHead = new TrackObject[playerCount];
        backTracking = new boolean[playerCount];
        for (int i = 0; i < playerNames.length; i++) {
            player[i] = new Player(playerNames[i]);
            currentChain[i] = 0;
            trapChain[i] = 0;
            trapChainCount[i] = 0; 
            trapChainHead[i] = null;
            backTracking[i] = false;
        }
        GE_setTrack(difficulty);
        initializeDisplayData(this.track, player, currentTurn);
    }

    /**
     * Rolling dice
     * @return Integer between 1 and 6
     */
    public int roll() {
        return Dice.rollDice();
    }

    public void overridePlayerPosition(int playerIndex, int position) {
        player[playerIndex].getBoat().setPosition(position);
    }

    /**
     * Initiates next game sequence
     * @throws Exception
     */
    public boolean nextSequence() throws Exception {
        if (player[currentTurn].getBoat().getPosition() == 100) return hasWon(player[currentTurn]);
        player[currentTurn].incrTurnCount();
        GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
        Thread.sleep(10*animationlengthScale);
        
        int steps = roll();
        //overlay dice, with animations
        if (!hideDiceAnimation) {
            for (int i = 0; i < 4; i++) {
            GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn, true, Dice.rollDice());
            Thread.sleep((i + 1) * animationlengthScale);
            }
        }
        GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn, true, steps);
        Thread.sleep(20*animationlengthScale);

        if (moveBoatSmooth(steps, 1, 5*animationlengthScale)) return true;

        try {
            while (track.getTrackArray()[player[currentTurn].getBoat().getPosition()] instanceof Current || track.getTrackArray()[player[currentTurn].getBoat().getPosition()] instanceof Trap) {
                TrackObject obstacle = track.getTrackArray()[player[currentTurn].getBoat().getPosition()];
                // game event messages
                if (obstacle instanceof Current) {
                	currentChain[currentTurn]++;
                }
                
                if (obstacle instanceof Trap) {
                    backTracking[currentTurn] = true;
                	if (track.getTrackChain()[obstacle.getPosition()][1] >= 2 && trapChain[currentTurn] == 0) {
                		trapChainCount[currentTurn]++;
                        trapChainHead[currentTurn] = obstacle;
                	}
                    if (trapChainCount[currentTurn] != 0 && (player[currentTurn].getBoat().getPosition() > trapChainHead[currentTurn].getPosition())){
                        trapChainHead[currentTurn] = obstacle;
                        trapChainCount[currentTurn] = 1;
                    }
                	trapChain[currentTurn]++;
                }

                if (currentChain[currentTurn] != 0) {
                    if (gameEventMessage(0, obstacle, "currentChain")) return true;
                }
                else if (trapChain[currentTurn] != 0 && trapChainCount[currentTurn] < 2){
                    if (gameEventMessage(1, obstacle, "trapChain")) return true;
                }
                else {
                    Thread.sleep(6*animationlengthScale);
                    if (moveBoatSmooth(obstacle.getStrength(), obstacle.getDirection(), 5*animationlengthScale)) return true;
                }
                // move boat
                GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
            }
            // reset event message index counter
            currentChain[currentTurn] = 0;
            trapChain[currentTurn] = 0;
            if (player[currentTurn].getBoat().getPosition() > trapChainHead[currentTurn].getPosition()){
                trapChainCount[currentTurn] = 0;
            }
            if (trapChainCount[currentTurn] > 1 && player[currentTurn].getBoat().getPosition() < trapChainHead[currentTurn].getPosition() && backTracking[currentTurn] == true) {
            	GameDisplay.queueEventMessage(2, trapChainCount[currentTurn] - 2, 1);
                GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
                Thread.sleep(30*messagelengthscale);
            }
            backTracking[currentTurn] = false;      
        } catch (Exception e) {
            //TODO: handle exception
        }
        Thread.sleep(6*animationlengthScale);
        incrCurrentTurnPlayer();
        return false;
    }

    /**
     * Moves boat smoothly step by step
     * @param steps
     * @param direction
     * @param delayMillis
     * @return
     * @throws Exception
     */
    public boolean moveBoatSmooth(int steps, int direction, int delayMillis) throws Exception {
        for (int i = 0; i < steps; i++) {
            player[currentTurn].getBoat().move(1 * direction);
            for (int j = 0; j < interpolation; j++) {
                float finePosition = direction == 1 ? player[currentTurn].getBoat().getPosition() - 1 + (float)j/interpolation : player[currentTurn].getBoat().getPosition() + 1 - (float)j/interpolation;
                GameDisplay.displayRiver(displayData, finePosition, currentTurn);
                Thread.sleep(delayMillis/interpolation);
            }
            GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
            if (player[currentTurn].getBoat().getPosition() == track.getTrackCount()) {
                return hasWon(player[currentTurn]);
            }
            
        }
        return false;
    }

    /**
     * Displays game evnt messages
     * @param messageType
     * @param obstacle
     * @param eventType
     * @return
     * @throws Exception
     */
    private boolean gameEventMessage(int messageType, TrackObject obstacle, String eventType) throws Exception {
        int messageIndex = eventType.equals("currentChain") ? currentChain[currentTurn] - 1 : trapChain[currentTurn] - 1;
        GameDisplay.queueEventMessage(messageType, messageIndex, (obstacle.getStrength() + 1) * interpolation + 1);
        GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
        Thread.sleep(6*animationlengthScale);
        if (moveBoatSmooth(obstacle.getStrength(), obstacle.getDirection(), 5*animationlengthScale)) return true;
        Thread.sleep(20*messagelengthscale);
        return false;
    }

    /**
     * Pans smoothly to the next target
     * @param start
     * @param end
     * @throws Exception
     */
    private void smoothPan(float start, float end) throws Exception {
        // pans to track edge target instead of player target for smoother experience
        float panMargin = (float) GameDisplay.getDisplayWidth()/8;
        if (start < panMargin) start = (int) panMargin;
        if (end < panMargin) end = (int) panMargin;
        if (start > track.getTrackCount() + 1 - panMargin) start = track.getTrackCount() + 1 - panMargin;
        if (end > track.getTrackCount() + 1 - panMargin) end = track.getTrackCount() + 1 - panMargin;
        float displacement = end - start;
        if (displacement < 0) displacement *= -1;
        float duration = (0.4f + (displacement/25)) * ((float)animationlengthScale/50);

        // skip panning animation if no movement is required
        float delta = end - start;
        if (delta != 0) for (float i = 0; i < 1; i += (1/duration/fps)) {
            float pos = ParametricBlend(i) * delta;
            GameDisplay.displayRiver(displayData, start + pos);
            Thread.sleep(1000/fps);
        }
    }

    /**
     * Set whose turn is right now.
     * Method is unprotected and setting out of bounds value may crash the game!
     * @param index
     */
    public void setCurrentTurnPlayer(int index) {
        currentTurn = index;
    }

    /**
     * Increment turn count and advance to next player
     * @throws Exception
     */
    public void incrCurrentTurnPlayer() throws Exception {
        int prevTurn = currentTurn;
        currentTurn++;
        if (currentTurn >= playerCount) currentTurn = 0;

        // panning animation
        smoothPan(player[prevTurn].getBoat().getPosition(), player[currentTurn].getBoat().getPosition()); // distance influences panning animation length
        GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
        GameDisplay.displayMessage(player[currentTurn].getUsername() + "'s turn!");
    }

    /**
     * Determines what to do after a player has won
     * @param player
     * @return
     * @throws Exception
     */
    private boolean hasWon(Player player) throws Exception {
        updateLeaderboard(player.getUsername(), player.getTurnCount(), this.difficulty);
        String subtext = player.getUsername() + " won!";
        GameDisplay.displayEndScreen(subtext);
        Scanner input = new Scanner(System.in);
        input.nextLine();
        dispLeaderboard(this.difficulty, true, player);
        return true;
    }

    /**
     * Updates leaderboard
     * @param username
     * @param turnCount
     * @param difficulty
     * @throws Exception
     */
    public static void updateLeaderboard(String username, int turnCount, int difficulty) throws Exception {
        loadLeaderboard();
        if (difficulty <= 3) leaderboard[difficulty].updateLeaderboard(new Score(username, turnCount, difficulty));
        saveLeaderboard();
    }

    /**
     * Refresh screen when loading a game from file
     * @throws Exception
     */
    public void gameResumed() throws Exception {
        GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
    }

    /**
     * Displays leaderboard page
     * @param difficulty
     * @param showCurrentScore
     * @param player
     */
    public static void dispLeaderboard(int difficulty, boolean showCurrentScore, Player player) {
        loadLeaderboard();
        GameDisplay.displayleaderboard(leaderboard[difficulty], showCurrentScore, player);
    }

    /**
     * Parametric function
     * Credits: DannyYaroslavski
     * 
     * @param t (range: 0 - 1)
     * @return {@code float}
     */
    private float ParametricBlend(float t)
    {
        float sqt = t * t;
        return sqt / (2.0f * (sqt - t) + 1.0f);
    }

    // setters and getters

    /**
     * Get the default player count
     * @return default player count
     */
    public static int getDefaultPlayerCount() {
        return defaultPlayerCount;
    }

    /**
     * Creates a new Track with specified difficulty
     * @param difficulty
     */
    private void GE_setTrack(int difficulty) {
        this.track = new Track(difficulty);
    }

    /**
     * An array of objects to be passed to Game Display.<p>
     * 
     * Every class in Java extend Object. 
     * Therefore it is possible to be completely non-descriptive when you create the array by declaring it an array of Objects:
     * @param dataToDisplay...
     */
    private void initializeDisplayData(Object... dataToDisplay) {
        this.displayData = new Object[dataToDisplay.length];
        for (int i = 0; i < dataToDisplay.length; i++) {
            displayData[i] = dataToDisplay[i];
        }
        if (debug) System.out.println("Initialized display data");
        try {
            // Generate 1st frame of image when game starts
            GameDisplay.displayRiver(displayData, player[currentTurn].getBoat().getPosition(), currentTurn);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 

    /**
     * Returns Object Array display data<p>
     * <p>
     * <strong>Usage:</strong>
     * <pre>{@code
     * Object[] data = getDisplayData();
     * for (Object obj : data) {
     *     if (obj instanceof ObjectA) {
     *         ((ObjectA) obj).methodA();
     *     }
     *     else if (obj instanceof ObjectB) {
     *         ((ObjectB) obj).methodB();
     *     }
     * }
     * </pre>
     * @return <pre>{@code Object[] displaydata}</pre>
     */
    public Object[] getDisplayData() {
        return displayData;
    }

    /**
     * Load leaderboard data from leaderboard.dat (Any changes to leaderboard.txt will not be reflected here).<p>
     * To modify the leaderboard, use {@code /leaderboard help} in game for more information.
     */
    public static void loadLeaderboard() {
        try {
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream("leaderboard.dat"));
            var data = fis.readObject();
            fis.close();
            leaderboard = (Leaderboard[])data;
        } catch (FileNotFoundException e) {
            System.out.println("Leaderboard not found, creating new leaderboard...");
            for (int i = 0; i < leaderboard.length; i++) {
                leaderboard[i] = new Leaderboard(i);
            }
            saveLeaderboard();
            e.printStackTrace();
        }catch (InvalidClassException e) {
            System.out.println("Leaderboard version mismatch found! Unable to load leaderboard.");
            for (int i = 0; i < leaderboard.length; i++) {
                leaderboard[i] = new Leaderboard(i);
            }
            saveLeaderboard();
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Update & save leaderboard<p>
     * Two files wil be created: <p>
     * - leaderboard.dat (full leaderboard data, cannot be opened in a text editor)<p>
     * - leaderboard.txt (top 5 of each difficulty, to fulfill assignment requirements)
     */
    public static void saveLeaderboard() {
        try {
            FileOutputStream fos = new FileOutputStream("leaderboard.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // write leaderboard object to file
            oos.writeObject(leaderboard);
            oos.close();

            // write leaderboard to readable file.
            BufferedWriter lbfile = new BufferedWriter(new FileWriter("leaderboard.txt"));
            for (int i = 0; i < leaderboard.length; i++) {
                lbfile.write(leaderboard[i].toString());
                lbfile.newLine();
            }
            lbfile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print leaderboard to console
     */
    public static void printLeaderboard() {
        for (int i = 0; i < leaderboard.length; i++) {
            System.out.println(leaderboard[i].toString());;
        }
    }

    /**
     * Removes score from leaderboard
     * @param difficulty
     * @param index
     */
    public static void removeLeaderboardScore(int index,int difficulty) {
        loadLeaderboard();
        try {
            leaderboard[difficulty].removeScore(index);
            saveLeaderboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Removes score from leaderboard
     * @param difficulty
     * @param username
     */
    public static void removeLeaderboardScore(String username, int difficulty) {
        loadLeaderboard();
        try {
            leaderboard[difficulty].removeScore(username);
            saveLeaderboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets animation length scale
     * @param animationlengthScale
     */
    public void setAnimationlengthScale(int animationlengthScale) {
        this.animationlengthScale = animationlengthScale;
    }

    /**
     * Sets how long the message should be displayed
     * @param messagelengthscale
     */
    public void setMessagelengthScale(int messagelengthscale) {
        this.messagelengthscale = messagelengthscale;
    }
}
