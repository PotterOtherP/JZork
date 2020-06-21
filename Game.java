import java.util.Scanner;

/**
 * This program is my attempt to recreate Zork I as well as possible.
 *
 * 
 *
 * @author Nathan Tryon January 2020 - 
 */
public final class Game {

    // Global variables
	public static boolean gameover = true;
	public static boolean godmode = false;
    public static boolean debug = false;
    public static GameState gameState;


    // Constants
    public static final int MAX_LINE_LENGTH = 80;
	public static final Location STARTING_LOCATION = Location.WEST_OF_HOUSE;




	public static void main(String[] args)
	{
        setMode(args);      

		gameState = new GameState();
        InputParser parser = new InputParser(gameState);
	
		initGame(gameState);

		gameover = false;

		while (!gameover)
		{	
            gameState.resetInput();
			gameState.completePlayerInput = getPlayerText();
            outputLine();

            parser.reset();
            gameState.refreshInventories();
            gameState.fillCurrentObjectList();

            if (debug)
            {
                output("Current object list:\n");

                for (String str : gameState.currentObjects.keySet())
                    output(str);

                output("---------------");
            }


            if (!parser.parsePlayerInput()) continue;
            if (!parser.validateAction()) continue;

            gameState.updateGame();

            if (debug) parser.inputTest();
		}

		endGame(gameState);
		
	}




	public static void initGame(GameState state)
	{	
		new GameSetup(state, godmode, debug);

		// Put the player in the starting location
        Room start = state.worldMap.get(STARTING_LOCATION);
        state.playerLocation = STARTING_LOCATION;
        start.firstVisit = false;
        outputLine();
        start.lookAround(state);

	}

    public static void setMode(String[] args)
    {
        if (args.length > 0 && args[0].equals("debug"))
        {
            debug = true;
            output("Testing mode on.");
        }

        if (args.length > 0 && args[0].equals("godmode"))
        {
            output("God mode enabled.");
            godmode = true;
        }
    }

	public static void prompt() { System.out.print("\n>> "); }
	public static void outputLine() { System.out.println(); }
	public static void output() { System.out.println(); }
	
	public static void output(String s)
    {
        if (s.isEmpty()) return;

        if (s.contains("WEAPON"))
        {
            s = s.replace("WEAPON", (gameState.indirectObject.name));
        }

        if (s.contains("ENEMY"))
        {
            s = s.replace("ENEMY", (gameState.directObject.name));
        }

        String[] lines = s.split("\n");

        for (int i = 0; i < lines.length; ++i)
        {
            String line = lines[i];

            while (line.length() > MAX_LINE_LENGTH)
            {
                char endChar = line.charAt(MAX_LINE_LENGTH);
                int shift = MAX_LINE_LENGTH;
                while (endChar != ' ' && shift > 0)
                {
                    --shift;
                    endChar = line.charAt(shift);
                }

                String chunk = line.substring(0, shift);
                System.out.println(chunk);
                line = line.substring(shift + 1);
            }

            System.out.println(line);
        }

    }

	public static String getPlayerText()
	{
        Scanner scn = new Scanner(System.in);
		String result = "";
		prompt();

		while(result.isEmpty())
		{
			result = scn.nextLine();

			if (result.isEmpty())
			{
				output("I beg your pardon?");
				prompt();
			}
		}

		return result.trim().toLowerCase();
	}




	public static boolean verifyQuit()
	{
		boolean result = false;
		Scanner scn = new Scanner(System.in);
		output("Are you sure you want to quit?");
		String input = scn.nextLine().toLowerCase();
		if (input.equals("y")) result = true;
		if (input.equals("yes")) result = true;
		
		scn.close();
		
		return result;
	}



	public static void endGame(GameState state)
	{

		output("Game has ended.");
		output("Total turns: " + state.turns);

	}



}