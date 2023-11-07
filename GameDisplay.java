import TrackObject.*;
import java.util.ArrayList;

import LeaderboardObject.Leaderboard;
import LeaderboardObject.Score;
import PlayerObject.*;

/**
 * Display engine for tha main game. Contains all assets and texts.
 * @author AlphaWoof
 */
public class GameDisplay {
    
    // Display settings
    private static int displayWidth = 116; // windows console default W*H: 120x30
    private static int displayHeight = 30;
    private static int tableWidth = 48;
    private static int tableHeight = 5 * 2 + 3; // 2n+3, shows top n amount of players in the leaderboard
    private static int tableMidLine1 = 7;
    private static int tableMidLine2 = 9;
    private static boolean showDebugInfo = false;
    private static boolean showCenterMarker = true;

    // Resources and tools (do not modify these!)
    private static int offsetHeight;
    private static String frame;
    private static ArrayList<String> frameBuffer = new ArrayList<String>();
    private static int repeatFrameCount = 0;
    private static String repeatMessage = "";

    // Special character bank
    private static final char[] shading = {' ', '░', '▒', '▓', '█'};
    private static final char[] thiccBox = {'╔','╗','╝','╚','═','║','╣','╠','╩','╦','╬'};
    private static final char[] thinBox = {'┌','┐','┘','└','─','│','┤','├','┴','┬','┼'};

    // Special strings
    private static final String[] difficultyName = {"Easy", "Normal", "Hard", "Unfair"};

    private static final String[][] dices = {
        {" _________", "/_______/│", "│       ││", "│   o   ││", "│       │/", "└───────┘ "}, 
        {" _________", "/_______/│", "│ o     ││", "│       ││", "│     o │/", "└───────┘ "}, 
        {" _________", "/_______/│", "│ o     ││", "│   o   ││", "│     o │/", "└───────┘ "}, 
        {" _________", "/_______/│", "│ o   o ││", "│       ││", "│ o   o │/", "└───────┘ "}, 
        {" _________", "/_______/│", "│ o   o ││", "│   o   ││", "│ o   o │/", "└───────┘ "}, 
        {" _________", "/_______/│", "│ o   o ││", "│ o   o ││", "│ o   o │/", "└───────┘ "}
    };

    private static final String[] gameOver = {
        "  ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗  ",
        " ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗ ",
        " ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝ ",
        " ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗ ",
        " ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝ ╚████╔╝ ███████╗██║  ██║ ",
        "  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═╝ ",
    };

    private static final String[] boatArt1 = {
        "                          |----.___",
        "                                 |----.___',",
        "               ._________________|_______________.",
        "               |####|    |####|    |####|   |####|",
        "               |####|    |####|    |####|    |####|       .",
        "               |####|    |####|    |####|    |####|     /|_____.",
        "   __          |####|    |####|    |####|    |####|   |  o  ..|",
        " (  '.         '####|    '####|    '####|    '####|   '.  .vvv'",
        "  '@ |          |####.    |####.    |####.    |####|    ||",
        "   | |          '####.    '####.    '####.    '####.    ||",
        "  /  |         /####.    /####.    /####.    /####.     |'.",
        " |    |       '####/    '####/    '####/    '####/      |  |",
        " |     |  .  /####|____/####|____/####|____/####|      |    |",
        " |      |//   .#'#. .*'*. .#'#. .*'*. .#'#. .*'*.     |      |",
        "  |     //-...#'#'#-*'*'*-#'#'#-*'*'*-#'#'#-*'*'*-...'        |",
        "   |   //     '#'#' '*'*' '#'#' '*'*' '#'#' '*'*'             |",
        "    './/                                                     .'",
        "    _//'._                                                _.'",
        "   /  /   '----------------------------------------------'",
    };

    private static final String[] leaderboardText = {
        "▒█░░░ █▀▀ █▀▀█ █▀▀▄ █▀▀ █▀▀█ █▀▀▄ █▀▀█ █▀▀█ █▀▀█ █▀▀▄ ",
        "▒█░░░ █▀▀ █▄▄█ █░░█ █▀▀ █▄▄▀ █▀▀▄ █░░█ █▄▄█ █▄▄▀ █░░█ ",
        "▒█▄▄█ ▀▀▀ ▀░░▀ ▀▀▀░ ▀▀▀ ▀░▀▀ ▀▀▀░ ▀▀▀▀ ▀░░▀ ▀░▀▀ ▀▀▀░"
    };

    private static final String[] hardStuckEventMessage = {
        "This is so sad Alexa played Despacito.",
        "Are you okay? Because you might have EMOTIONAL DAMAGE!",
        "Okay I'm joking... I sincerely hope you can get through this...",
        "HA SIKE, you are back here again. How often can I see you back here?",
        "Okay seriously though, how can one be so unlucky?",
        "Okay I'm going to stop now, maybe you will get through it if I stop."
    };

    private static final String[] currentEventMessage = {
        "Your boat received the force of the current, moved forward once.",
        "The god of the water blessed you with some luck, moved forward again.",
        "Wow, with this luck you can hit a jackpot without even betting, moved forward again.",
        "Did you bribe the game developer, this is IMPOSSIBLE!",
        "You must be cheating..."
    };

    private static final String[] trapEventMessage = {
        "Your boat hit an imaginary rock, moved backward once.",
        "This is not your lucky day I guess, moved backward again.",
        "You better not go outside today, every bird will gift you a prize, moved backward thrice.",
        "Even the game developer feels bad for you. Here have some luck.",
        "You must be cheating... right? Why are torturing yourself."
    };

    // menu messages
    protected static final String menu_newGame1 = "Options: \n[ENTER] Start new game   [L] Load game   [H] Help   [Q] Quit";
    protected static final String menu_newGame2 = "Options: \n[ENTER] Play again   [L] Load game   [H] Help   [Q] Quit";
    protected static final String menu_inGame = "[ENTER] Roll dice   [S] Save game   [L] Load game   [Q] Quit";
    protected static final String menu_newWithPrev = "Start new game with previous settings? (This includes names and difficulty choice)\n[ENTER] Yes   [N] No";
    protected static final String menu_playerAmountSelector = "Enter player amount (leave blank for default: 2): ";
    protected static final String menu_playerAmountInvalid = "Invalid player amount!";
    protected static final String menu_difficultyHelp = "\n\nDifficulty: \n [0] Easy (fewer traps and more currents, ideal for babies)\n [1] Normal (The default mode)\n [2] Hard (fewer currents and more traps)\n [3] Unfair (no currents at all, traps everywhere)\n";
    protected static final String menu_difficultySelector = "Choose Difficulty (leave blank for default: 1): ";
    protected static final String menu_difficultyInvalid = "Invalid difficulty!";
    protected static final String menu_help = "Controls:\n[ENTER] Play game/Next step\n[S] Save game to file\n[L] Load a saved game\n[H] Show this menu\n[N] Next page\n[P] Previous page\n[Q] Quit game\n\nCommands\n/fw [number]: Move player in focus forwards specified amount. Effective only after game has started.\n/bw [number]: Move player in focus backwards specified amount. Effective only after game has started.\n/next: Focus next player (also increments turn count). Effective only after game started.\n/leaderboard [set | list | remove | find | help]: /leaderboard help for more info. Command also works without starting a new game.\n/animationlength [number]: Sets animation speed. Lower number is faster. Effective only after game has started.\n/messagelength [number]: Sets how long messages should display. Effective only after game has started.\n/autopilot: Automatically advances the game until the end.\n/setwidth (width): Sets display width (default = 116).\n/setheight (height): Sets display height (default = 30).\n/end: End current game\n";
    protected static final String menu_leaderboardHelp = "Usage: /leaderboard [set | list | remove | find | help]\n    set: Updates a score in the leaderboard. New scores will be added if username is not found\n        Usage: /leaderboard set (playername) (turnCount) (difficulty)\n    list: Lists leaderboard\n        Usage: /leaderboard list (difficulty)\n    remove: Remove specific index or player from leaderboard.\n        Usage: /leaderboard remove [player | index] (playername) (difficulty)\n    find: Find a specific player's leaderboard score.\n        Usage: /leaderboard find (playername) (difficulty)\n";
    protected static final String menu_unknownCommand = "Unknown command";
    protected static final String menu_savingGame = "Saving game...";
    protected static final String menu_savedGame = "Saved game";
    protected static final String menu_loadingGame = "Loading game...";
    protected static final String menu_loadedGame = "Loaded game. Press [ENTER] to start!";
    protected static final String menu_loadGameFailMismatch = "Game version mismatch found! Unable to load game save.";
    
    /**
     * Writes display data.<p>
     * Has 3 overloaded methods because we cant set default parameter values in Java.
     * @param targetPosition
     * @throws Exception
     */
    public static void displayRiver(Object[] displayData, float targetPosition) throws Exception {
        displayRiver(displayData, targetPosition, -1, false, -1);
    }

    /**
     * Writes display data.<p>
     * @param displayData
     * @param targetPosition
     * @param currentPlayer
     * @throws Exception
     */
    public static void displayRiver(Object[] displayData, float targetPosition, int currentPlayer) throws Exception {
        displayRiver(displayData, targetPosition, currentPlayer, false, -1);
    }
    
    /**
     * Writes display data.<p>
     * @param displayData
     * @param targetPosition
     * @param currentPlayer
     * @param overlayDice
     * @param diceNumber
     * @throws Exception
     */
    public static void displayRiver(Object[] displayData, float targetPosition, int currentPlayer, boolean overlayDice, int diceNumber) throws Exception {
        frame = riverFrameDrawer(displayData, targetPosition, currentPlayer, overlayDice, diceNumber);
        System.out.println(frame);
    }

    /**
     * Generates a frame using given parameters
     * @param displayData
     * @param targetPosition
     * @param currentPlayer
     * @param overlayDice
     * @param diceNumber
     * @return frame
     * @throws Exception
     */
    private static String riverFrameDrawer(Object[] displayData, float targetPosition, int currentPlayer, boolean overlayDice, int diceNumber) throws Exception {
        // Fetch data from GameEngine
        Track track = null;
        Player[] player = null;
        for (Object object : displayData) {
            if (object instanceof Track) {
                track = ((Track) object);
            }
            if (object instanceof Player[]) {
                player = ((Player[]) object);
            }
        }
        if (track == null || player == null) {
            throw new Exception("Display data or Game Engine not ready (displayData may not be correctly initialized)");
        }
        //draw river
        frameBuffer.clear();
        int displayHeight = GameDisplay.displayHeight - player.length;
        offsetHeight = player.length;
        char[][] dispYX = new char[displayHeight][displayWidth];
        // make sure the river doesn't move outside of screen
        targetPosition = (targetPosition + (float) 0.5) * 4;
        int offset = 0;
        offset = targetPosition > (track.getTrackCount()*4 - displayWidth/2 + 5) ? (int) (track.getTrackCount()*4 - displayWidth + 5) : (targetPosition >= displayWidth/2 ? (int) targetPosition - (displayWidth/2) : 0);

        for (int i = 0; i < displayHeight; i++) {
            for (int j = 0; j < displayWidth; j++) {
                dispYX[i][j] = ' '; // create empty canvas

                //river
                int pos = j + offset;
                if (i == displayHeight - 4 && pos/4 < 100) {
                    //draw track
                    
                    char symbol = ' ';
                    if (track.getTrackArray()[pos/4] instanceof Current || track.getTrackArray()[pos/4] instanceof Trap) {
                        symbol = track.getTrackArray()[pos/4].getSymbol().charAt(0);
                    }
                    dispYX[i][j] = symbol;
                    if ((pos+1)%2 == 0) dispYX[i][j] = ' ';
                    if (pos%4 == 0) dispYX[i][j] = ' ';
                }
                
                // border
                if (i == displayHeight - 3) dispYX[i][j] = thinBox[4]; // horizontal
                if (i == displayHeight - 3 && pos%4 == 0) dispYX[i][j] = thinBox[8];

                // center marker
                if (showCenterMarker) {
                    if (i == displayHeight - 1 && (j == (displayWidth+2)/2 || j == (displayWidth-2)/2)) dispYX[i][j] = ' ';
                    if (i == displayHeight - 1 && j == displayWidth/2) dispYX[i][j] = '^';
                }

                // boat
                for (int k = 0; k < player.length; k++) {
                    if (currentPlayer != k && i == displayHeight - 4 - player.length + k && (pos == player[k].getBoat().getPosition()*4+3 || pos == player[k].getBoat().getPosition()*4+1)) dispYX[i][j] = thinBox[5];
                    if (currentPlayer == k && i == displayHeight - 4 - player.length + k && (pos == targetPosition+1 || pos == targetPosition-1)) dispYX[i][j] = thiccBox[5];
                    int nickIndex = 0;
                    if (player[k].getUsername().length() >= 8 && player[k].getUsername().substring(0, 7).equals("Player ")) nickIndex = 7;
                    if (currentPlayer != k && i == displayHeight - 4 - player.length + k && pos == player[k].getBoat().getPosition()*4+2) dispYX[i][j] = player[k].getUsername().toUpperCase().charAt(nickIndex);
                    if (currentPlayer == k && i == displayHeight - 4 - player.length + k && pos == targetPosition) dispYX[i][j] = player[k].getUsername().toUpperCase().charAt(nickIndex);
                }
                
            }
            if (i == displayHeight - 2) for (int j = 0; j < displayWidth; j++) {
                // track number label
                int pos = j + offset;
                if (i == displayHeight - 2 && ((float)pos/4%5) == 0) {
                    for (int k = 0; k < String.valueOf(pos/4).length(); k++) {
                        try {
                            if (String.valueOf(pos/4).length() >= 3) dispYX[i][j+k+4-String.valueOf(pos/4).length()] = String.valueOf(pos/4).charAt(k);
                            else dispYX[i][j+k+3-String.valueOf(pos/4).length()] = String.valueOf(pos/4).charAt(k);
                        } catch (Exception e) {
                            //TODO: handle exception
                        }
                    }
                }
            }
            if (overlayDice) {
                //draw dice
                if (i >= displayHeight - player.length - 10 && i < displayHeight - player.length - 4) for (int j = 0; j < dices[0][0].length(); j++) {
                    dispYX[i][j + (displayWidth/2 - 4 - 16)] = dices[diceNumber-1][i - displayHeight + player.length + 10].charAt(j);
                }
            }
            frameBuffer.add(String.valueOf(dispYX[i]));
        }
        int totalTurns = 0;
        for (Player p : player) {
            frameBuffer.add(p.getUsername() + "'s position: " + p.getBoat().getPosition() + "   Turn count: " + p.getTurnCount());
            totalTurns += p.getTurnCount();
        }
        frameBuffer.add("Round: " + totalTurns);
        if (showDebugInfo) frameBuffer.add("Target: " + targetPosition + "   Offset: " + offset);
        else frameBuffer.add("");
        frameBuffer.add(menu_inGame);
        if (repeatFrameCount-- > 0) {
            // inject messages on top of generated frame
            displayMessage(repeatMessage);
        }
        frame = String.join("\n", frameBuffer);
        return frame;
    }

    /**
     * Overlays the endscreen card on top of previous frame, then print the frame
     * @param subText
     */
    public static void displayEndScreen(String subText) {
        int go_offset = (displayWidth - gameOver[0].length()) / 2; // game over text align center position
        int subText_offset = (displayWidth - subText.length()) / 2;// game over subtext align center position
        String offsetGO = "";
        String offsetSubText = "";
        String emptyLine = "";
        for (int i = 0; i < go_offset; i++) offsetGO += " ";
        for (int i = 0; i < subText_offset; i++) offsetSubText += " ";
        for (int i = 0; i < displayWidth; i++) emptyLine += " ";
        for (int i = 0; i < gameOver.length; i++) {
            frameBuffer.set(frameBuffer.size() - 15 - 2 * offsetHeight + i, (offsetGO + gameOver[i]));
        }
        frameBuffer.set(frameBuffer.size() - 9 - 2 * offsetHeight, (offsetSubText + subText)); // print game over text
        frameBuffer.set(frameBuffer.size() - 8 - 2 * offsetHeight, emptyLine); // print game over subtext
        frameBuffer.set(frameBuffer.size() - 1, emptyLine); // print an empty line
        frameBuffer.set(frameBuffer.size() - 1, "[ENTER] Continue"); // show menu
        frame = String.join("\n", frameBuffer);
        System.out.println(frame);
    }

    /**
     * Displays game event messages. Message overlays on top of last frame<p>
     * Message can be configured to continue display for specified amount of frames. 
     * @param messageType
     * <pre>&nbsp;0: Current event messages
     * 1: Trap event messages
     * 2: Trap chain event messages</pre>
     * @param messageIndex Index of speficied message.
     * @param frames Shows the message for speficied amount of frames
     */
    public static void queueEventMessage(int messageType,int messageIndex, int frames) {
        String[] message = messageType == 0? currentEventMessage : (messageType == 1? trapEventMessage : hardStuckEventMessage);
        if (messageIndex > message.length - 1) messageIndex = message.length - 1;
        repeatFrameCount = frames;
        repeatMessage = message[messageIndex];
        //displayMessage(message[messageIndex]);
    }

    /**
     * Displays message for a single frame by overlaying on top of last frame
     * @param message
     */
    public static void displayMessage(String message) {
        repeatMessage = message.equals(repeatMessage) ? repeatMessage : message;
        int subText_offset = (displayWidth - message.length()) / 2;
        String offsetSubText = "";
        for (int i = 0; i < subText_offset; i++) offsetSubText += " ";
        int offset = offsetHeight + (offsetHeight - 2);
        frameBuffer.set(frameBuffer.size() - 10 - offset, (offsetSubText + message));
        frame = String.join("\n", frameBuffer);
        System.out.println(frame);
    }

    /**
     * Displays leaderboard
     * @param leaderboard
     * @param showCurrentScore
     * @param player
     */
    public static void displayleaderboard(Leaderboard leaderboard, boolean showCurrentScore, Player player) {
        String[] leaderboardTable = leaderboard(leaderboard, showCurrentScore, player);
        displayWidth = 116;
        // leaderboard page controls
        String lb_nav_p_section = "[P] Previous page";
        String lb_nav_n_section = "[N] Next page";
        int boatArtOffsetX = 9;
        int boatArtOffsetY = 0;
        // leaderboard title position offset
        int leaderboardTextOffsetX = 5;
        int leaderboardTextOffsetY = displayWidth/2 - leaderboardText[0].length()/2;
        // leaderboard table position offset
        int leaderboardTableOffsetX = 12;
        int leaderboardTableOffsetY = displayWidth - tableWidth;

        // create ampty canvas
        frameBuffer.clear();
        char[][] lframe = new char[displayHeight][displayWidth];
        for (int i = 0; i < displayHeight; i++) {
            for (int j = 0; j < displayWidth; j++) {
                lframe[i][j] = ' ';
            }
            frameBuffer.add(String.valueOf(lframe[i]));
        }
        editFrameBuffer(boatArt1, boatArtOffsetX, boatArtOffsetY); // draw boat art
        editFrameBuffer(leaderboardText, leaderboardTextOffsetX, leaderboardTextOffsetY); // draw leaderboard text
        editFrameBuffer(leaderboardTable, leaderboardTableOffsetX, leaderboardTableOffsetY); // draw leaderboard table
        int difficulty = leaderboard.getLeaderboardDifficulty();
        if (difficulty > 0) editFrameBuffer(lb_nav_p_section, leaderboardTableOffsetX - 2, leaderboardTableOffsetY); // leaderboard page controls
        if (difficulty < 3) editFrameBuffer(lb_nav_n_section, leaderboardTableOffsetX - 2, leaderboardTableOffsetY + leaderboardTable[1].length() - lb_nav_n_section.length()); // leaderboard page controls

        frame = String.join("\n", frameBuffer);
        System.out.println(frame);
    }

    /**
     * Modifies the content of frame buffer
     * @param object Object(s) to print. Supports {@code String[]}, {@code String} and {@code int}
     * @param offsetY Offsets the location vertically from top left corner
     * @param offsetX Offsets the location horizontally from top left corner
     */
    public static void editFrameBuffer(Object object, int offsetY, int offsetX) {
        // edit stuff in frame buffer
        // supports input such as String[], String, int
        int height = 1;
        if (object instanceof String[]) height = ((String[])object).length;

        for (int i = 0; i < height; i++) {
            StringBuilder modifyLine = new StringBuilder(frameBuffer.get(i + offsetY));
            if (object instanceof String[]) for (int j = 0; j < ((String[])object)[i].length(); j++) modifyLine.setCharAt(offsetX + j, ((String[])object)[i].charAt(j));
            else if (object instanceof String) for (int j = 0; j < ((String)object).length(); j++) modifyLine.setCharAt(offsetX + j, ((String)object).charAt(j));
            else if (object instanceof Integer) for (int j = 0; j < String.valueOf(((Integer)object)).length(); j++) modifyLine.setCharAt(offsetX + j, String.valueOf(((Integer)object)).charAt(j));
            frameBuffer.set(i + offsetY, modifyLine.toString());
        }
    }

    /**
     * Generates the leaderboard table. Output is generated line by line.
     * @param leaderboard
     * @param showCurrentScore
     * @param player
     * @return {@code String[]}
     */
    public static String[] leaderboard(Leaderboard leaderboard, boolean showCurrentScore, Player player) {
        frameBuffer.clear();
        frameBuffer.add(String.format("Track difficulty: %s", difficultyName[leaderboard.getLeaderboardDifficulty()]));
        int tableHeight = GameDisplay.tableHeight;
        if (showCurrentScore) tableHeight += 3;
        char[][] table = new char[tableHeight][tableWidth];
        for (int i = 0; i < tableHeight; i++) {
            for (int j = 0; j < tableWidth; j++) {
                table[i][j] = ' ';
                // draw table outline
                if (j == 0 || j == tableWidth - 1 || j == tableWidth - tableMidLine2 || j == tableMidLine1) table[i][j] = thinBox[5]; // vertical line
                if (i % 2 == 0) table[i][j] = thinBox[4]; // horizontal line for 0, 2, 4, ..., 12.
                if (showCurrentScore && (i == tableHeight - 1 || i == tableHeight - 3)) table[i][j] = thinBox[4];
                
                // mid border joints
                if (i % 2 == 0 && j == 0) table[i][j] = thinBox[7];
                if (i % 2 == 0 && (j == tableMidLine1 || j == tableWidth - tableMidLine2)) table[i][j] = thinBox[10];
                if (i % 2 == 0 && j == tableWidth - 1) table[i][j] = thinBox[6];

                // top border joints
                if (i == 0 && j == 0) table[i][j] = thinBox[0];
                if (i == 0 && (j == tableMidLine1 || j == tableWidth - tableMidLine2)) table[i][j] = thinBox[9];
                if (i == 0 && j == tableWidth - 1) table[i][j] = thinBox[1];

                // bottom border joints
                int offset = showCurrentScore ? 4 : 1; // moves the leaedrboard bottom joints upwards to draw current player's score if needed
                if (i == tableHeight - offset && j == 0) table[i][j] = thinBox[3];
                if (i == tableHeight - offset && (j == tableMidLine1 || j == tableWidth - tableMidLine2)) table[i][j] = thinBox[8];
                if (i == tableHeight - offset && j == tableWidth - 1) table[i][j] = thinBox[2];

                // show current player's score?
                if (showCurrentScore) {
                    // draw a tiny table right below the leaderboard table
                    if (i == tableHeight - 2) table[i][j] = ' ';
                    if (i == tableHeight - 1) table[i][j] = thinBox[4];
                    if (i == tableHeight - 2 && (j == 0 || j == tableWidth - 1 || j == tableWidth - tableMidLine2 || j == tableMidLine1)) table[i][j] = thinBox[5];
                    if (i == tableHeight - 3 && j == 0) table[i][j] = thinBox[0];
                    if (i == tableHeight - 3 && (j == tableMidLine1 || j == tableWidth - tableMidLine2)) table[i][j] = thinBox[9];
                    if (i == tableHeight - 3 && j == tableWidth - 1) table[i][j] = thinBox[1];
                    if (i == tableHeight - 1 && j == 0) table[i][j] = thinBox[3];
                    if (i == tableHeight - 1 && (j == tableMidLine1 || j == tableWidth - tableMidLine2)) table[i][j] = thinBox[8];
                    if (i == tableHeight - 1 && j == tableWidth - 1) table[i][j] = thinBox[2];
                }
            }
            frameBuffer.add(String.valueOf(table[i]));
        }
        String header_rank = "Rank";
        String header_name = "Player Name";
        String header_score = "Turns";
        String[] rank = {"1st", "2nd", "3rd", "4th", "5th"};
        editFrameBuffer(header_rank, 2, 2);
        editFrameBuffer(header_name, 2, 2 + tableMidLine1);
        editFrameBuffer(header_score, 2, 2 + tableWidth - tableMidLine2);
        for (int i = 1; i < rank.length + 1; i++) {
            try {
                editFrameBuffer(rank[i-1], i*2+2, 2);
                editFrameBuffer(leaderboard.getScoreByIndex(i-1).getUsername(), i*2+2, 2 + tableMidLine1);
                editFrameBuffer(String.valueOf(leaderboard.getScoreByIndex(i-1).getTurnCount()), i*2+2, 2 + tableWidth - tableMidLine2);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        if (showCurrentScore) {
            try {
                Score current_user = leaderboard.getScoreByPlayername(player.getUsername());
                int current_rank = leaderboard.getScorelist().indexOf(leaderboard.getScoreByPlayername(player.getUsername())) + 1;
                editFrameBuffer(current_rank, tableHeight - 1, 2);
                editFrameBuffer(player.getUsername(), tableHeight - 1, 2 + tableMidLine1);
                editFrameBuffer(String.valueOf(current_user.getTurnCount()), tableHeight - 1, 2 + tableWidth - tableMidLine2);
            } catch (Exception e) {
                //TODO: handle exception
            }

        }
        
        String[] lframe = new String[frameBuffer.size()];
        for (int i = 0; i < frameBuffer.size(); i++) {
            lframe[i] = frameBuffer.get(i);
        }
        frame = String.join("\n", frameBuffer);
        return lframe;
    }

    /**
     * Get display width
     * @return
     */
    public static int getDisplayWidth() {
        return displayWidth;
    }
    
    /**
     * Sets the display width manually
     * @param width
     */
    public static void setDisplayWidth(int width) {
        displayWidth = width;
    }

    /**
     * Get display height
     * @return
     */
    public static int getDisplayHeight() {
        return displayHeight;
    }
    
    /**
     * Sets the display height manually
     * @param width
     */
    public static void setDisplayHeight(int height) {
        displayHeight = height;
    }
}
