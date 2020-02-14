import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A location is any place a moveable object can exist.
 */
enum Location {


    WEST_OF_HOUSE, NORTH_OF_HOUSE, BEHIND_HOUSE, SOUTH_OF_HOUSE,
    ATTIC, KITCHEN, LIVING_ROOM, FOREST_PATH,
    FOREST_WEST, FOREST_EAST, FOREST_NORTHEAST, FOREST_SOUTH,
    CLEARING_NORTH, CLEARING_EAST, UP_TREE,
    CANYON_VIEW, ROCKY_LEDGE, CANYON_BOTTOM, END_OF_RAINBOW,
    STONE_BARROW, INSIDE_STONE_BARROW,

    BIRDS_NEST,
    MAILBOX,
	PLAYER_INVENTORY,
    INSIDE_TROPHY_CASE,
	NULL_LOCATION

	}

/**
 * Actions
 */
enum Action {

	JUMP,
	SHOUT,
	LOOK,
	INVENTORY,
	EXIT_NORTH,
	EXIT_SOUTH,
	EXIT_EAST,
	EXIT_WEST,
    EXIT_NORTHEAST,
    EXIT_NORTHWEST,
    EXIT_SOUTHEAST,
    EXIT_SOUTHWEST,
	EXIT_UP,
	EXIT_DOWN,
	NULL_ACTION,
	GODMODE_TOGGLE,
	GIBBERISH,
	QUIT,
	VERBOSE,
	PROFANITY,
	WAIT,
	DEFEND,
	HIGH_FIVE,

	TAKE,
	DROP,
	SPEAK,
	ACTIVATE,
	RING,
	PLAY,
	OPEN,
	CLOSE,
	UNLOCK,
	LOCK,
	READ,
	KICK,
	SLAP,

	ATTACK,
	TIE

	}

enum ActionType {

	BLANK,
	REFLEXIVE,
	DIRECT,
	INDIRECT
}



/**
 * This program is my attempt to replicate Zork I as closely as possible.
 *
 * @author Nathan Tryon January 2020
 */
public final class Game {


	private static boolean gameover = true;
	private static boolean godmode = false;

	private static HashMap<String, Action> commandOne = new HashMap<String, Action>();
	private static HashMap<String, Action> commandTwo = new HashMap<String, Action>();
	private static HashMap<String, Action> commandThree = new HashMap<String, Action>();

	private static ArrayList<String> dictionary = new ArrayList<String>();

	private static Location initialLocation = Location.WEST_OF_HOUSE;


	public static void main(String[] args)
	{

		GameState gameState = new GameState();
		String playerText = "";

		
		initGame(gameState);


		gameover = false;

		while (!gameover)
		{	
			playerText = getPlayerText();
			parsePlayerInput(gameState, playerText);
			validateAction(gameState);
			updateGame(gameState);

		}


		endGame(gameState);
		
	}


	private static void initGame(GameState state)
	{	
		// Populate the action lists and the dictionary
		createActions();
		fillDictionary();

		// Create all the objects, then add them to the lists, then add their methods
		Feature nullFeature = new Feature();

		Item nullItem = new Item();

		Actor nullActor = new Actor();

        // Passages: 
		Passage nullPassage = new Passage();

        // West of House
        Passage house1 = new Passage(Location.WEST_OF_HOUSE, Location.NORTH_OF_HOUSE);
        Passage house2 = new Passage(Location.WEST_OF_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house3 = new Passage(Location.WEST_OF_HOUSE, Location.STONE_BARROW);
        Passage house4 = new Passage(Location.WEST_OF_HOUSE, Location.FOREST_WEST);

        // North of House
        Passage house5 = new Passage(Location.NORTH_OF_HOUSE, Location.FOREST_PATH);
        Passage house6 = new Passage(Location.NORTH_OF_HOUSE, Location.BEHIND_HOUSE);

        // Behind house
        Passage house7 = new Passage(Location.BEHIND_HOUSE, Location.CLEARING_EAST);
        Passage house8 = new Passage(Location.BEHIND_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house9 = new Passage(Location.BEHIND_HOUSE, Location.KITCHEN);

        // South of House
        Passage house10 = new Passage(Location.SOUTH_OF_HOUSE, Location.FOREST_SOUTH);

        // Kitchen
        Passage house11 = new Passage(Location.KITCHEN, Location.ATTIC);
        Passage house12 = new Passage(Location.KITCHEN, Location.LIVING_ROOM);

        // Forest Path
        Passage forest1 = new Passage(Location.FOREST_PATH, Location.CLEARING_NORTH);
        Passage forest2 = new Passage(Location.FOREST_PATH, Location.FOREST_EAST);
        Passage forest3 = new Passage(Location.FOREST_PATH, Location.FOREST_WEST);
        Passage forest4 = new Passage(Location.FOREST_PATH, Location.UP_TREE);

        // Clearing North
        Passage forest5 = new Passage(Location.CLEARING_NORTH, Location.FOREST_EAST);
        Passage forest6 = new Passage(Location.CLEARING_NORTH, Location.FOREST_WEST);

        // Forest East
        Passage forest7 = new Passage(Location.FOREST_EAST, Location.CLEARING_EAST);
        Passage forest8 = new Passage(Location.FOREST_EAST, Location.FOREST_NORTHEAST);

        // Clearing East
        Passage forest9 = new Passage(Location.CLEARING_EAST, Location.FOREST_SOUTH);
        Passage forest10 = new Passage(Location.CLEARING_EAST, Location.CANYON_VIEW);

        // Forest South
        Passage forest11 = new Passage(Location.FOREST_SOUTH, Location.CANYON_VIEW);
        Passage forest12 = new Passage(Location.FOREST_SOUTH, Location.FOREST_WEST);

        // Canyon View
        Passage canyon1 = new Passage(Location.CANYON_VIEW, Location.ROCKY_LEDGE);

        // Rocky Ledge
        Passage canyon2 = new Passage(Location.ROCKY_LEDGE, Location.CANYON_BOTTOM);

        // Canyon Bottom
        Passage canyon3 = new Passage(Location.CANYON_BOTTOM, Location.END_OF_RAINBOW);

        // Stone Barrow
        Passage barrow1 = new Passage(Location.STONE_BARROW, Location.INSIDE_STONE_BARROW);





        // Rooms: Name, description, ID
        Room westOfHouse = new Room("West of House", GameStrings.DESC_WEST_OF_HOUSE, Location.WEST_OF_HOUSE);
        westOfHouse.addExit(Action.EXIT_NORTH, house1);
        westOfHouse.addExit(Action.EXIT_NORTHEAST, house1);
        westOfHouse.addExit(Action.EXIT_SOUTH, house2);
        westOfHouse.addExit(Action.EXIT_SOUTHEAST, house2);
        westOfHouse.addExit(Action.EXIT_SOUTHWEST, house3);
        westOfHouse.addExit(Action.EXIT_WEST, house4);

        Room northOfHouse = new Room("North of House", GameStrings.DESC_NORTH_OF_HOUSE, Location.NORTH_OF_HOUSE);
        northOfHouse.addExit(Action.EXIT_NORTH, house5);
        northOfHouse.addExit(Action.EXIT_EAST, house6);
        northOfHouse.addExit(Action.EXIT_SOUTHEAST, house6);
        northOfHouse.addExit(Action.EXIT_WEST, house1);
        northOfHouse.addExit(Action.EXIT_SOUTHWEST, house1);


        Room behindHouse = new Room("Behind House", GameStrings.DESC_BEHIND_HOUSE, Location.BEHIND_HOUSE);


        Room southOfHouse = new Room("South of House", GameStrings.DESC_SOUTH_OF_HOUSE, Location.SOUTH_OF_HOUSE);



        Room kitchen = new Room("Kitchen", GameStrings.DESC_KITCHEN, Location.KITCHEN);

        Room attic = new Room("Attic", GameStrings.DESC_ATTIC, Location.ATTIC);

        Room livingRoom = new Room("Living Room", GameStrings.DESC_LIVING_ROOM, Location.LIVING_ROOM);

        Room forestPath = new Room("Forest Path", GameStrings.DESC_FOREST_PATH, Location.FOREST_PATH);

        Room upTree = new Room("Up a Tree", GameStrings.DESC_UP_TREE, Location.UP_TREE);

        Room forestWest = new Room("Forest", GameStrings.DESC_FOREST_WEST, Location.FOREST_WEST);

        Room forestEast = new Room("Forest", GameStrings.DESC_FOREST_EAST, Location.FOREST_EAST);

        Room forestNortheast = new Room("Forest", GameStrings.DESC_FOREST_NORTHEAST, Location.FOREST_NORTHEAST);

        Room forestSouth = new Room("Forest", GameStrings.DESC_FOREST_SOUTH, Location.FOREST_SOUTH);

        Room clearingNorth = new Room("Clearing", GameStrings.DESC_CLEARING_NORTH, Location.CLEARING_NORTH);

        Room clearingEast = new Room("Clearing", GameStrings.DESC_CLEARING_EAST, Location.CLEARING_EAST);

        Room canyonView = new Room("Canyon View", GameStrings.DESC_CANYON_VIEW, Location.CANYON_VIEW);

        Room rockyLedge = new Room("Rocky Ledge", GameStrings.DESC_ROCKY_LEDGE, Location.ROCKY_LEDGE);

        Room canyonBottom = new Room("Canyon Bottom", GameStrings.DESC_CANYON_BOTTOM, Location.CANYON_BOTTOM);

        Room endOfRainbow = new Room("End of Rainbow", GameStrings.DESC_END_OF_RAINBOW, Location.END_OF_RAINBOW);

        Room stoneBarrow = new Room("Stone Barrow", GameStrings.DESC_STONE_BARROW, Location.STONE_BARROW);

        Room insideStoneBarrow = new Room("Inside Stone Barrow", GameStrings.DESC_INSIDE_STONE_BARROW, Location.INSIDE_STONE_BARROW);


        state.worldMap.put(westOfHouse.roomID, westOfHouse);
        state.worldMap.put(northOfHouse.roomID, northOfHouse);
        state.worldMap.put(behindHouse.roomID, behindHouse);
        state.worldMap.put(southOfHouse.roomID, southOfHouse);
        state.worldMap.put(kitchen.roomID, kitchen);
        state.worldMap.put(attic.roomID, attic);
        state.worldMap.put(livingRoom.roomID, livingRoom);

        state.worldMap.put(forestPath.roomID, forestPath);
        state.worldMap.put(forestWest.roomID, forestWest);
        state.worldMap.put(forestEast.roomID, forestEast);
        state.worldMap.put(forestNortheast.roomID, forestNortheast);
        state.worldMap.put(forestSouth.roomID, forestSouth);

        state.worldMap.put(clearingNorth.roomID, clearingNorth);
        state.worldMap.put(clearingEast.roomID, clearingEast);
        state.worldMap.put(upTree.roomID, upTree);

        state.worldMap.put(canyonView.roomID, canyonView);
        state.worldMap.put(rockyLedge.roomID, rockyLedge);
        state.worldMap.put(canyonBottom.roomID, canyonBottom);
        state.worldMap.put(endOfRainbow.roomID, endOfRainbow);

        state.worldMap.put(stoneBarrow.roomID, stoneBarrow);
        state.worldMap.put(insideStoneBarrow.roomID, insideStoneBarrow);


		// 

		state.itemList.put(nullItem.name, nullItem);
		state.featureList.put(nullFeature.name, nullFeature);
	
		state.actorList.put(nullActor.name, nullActor);

		

		createActions();


        ActivateMethod dummyMethod = (GameState gs, Action act) -> {};


		

		

	


		

	

		// Game objects complete. Start setting up the game



	

		


		// Put the player in the starting location
		state.setPlayerLocation(initialLocation);
		state.worldMap.get(initialLocation).firstVisit = false;

		// Beginning text of the game.
		outputLine();
		output(GameStrings.DESC_WEST_OF_HOUSE);
		
	}






	private static void parsePlayerInput(GameState state, String playerText)
	{

		/* Takes whatever the player entered and sets three strings in the gamestate:
		   first (action), second (object 1), third (object 2).
		   Also sets the number of action arguments (non-empty strings).

		*/

		state.resetInput();

		

		String[] words = playerText.split(" ");

		for (int i = 0; i < words.length; ++i)
		{
			if (!isGameWord(words[i]))
			{
				output("I don't know what " + words[i] + " means.");
				return;
			}
		}

		// Make sure we're deleting the words, not portions of other words...
		playerText = playerText.replaceAll(" the ", " ");
		playerText = playerText.replaceAll(" to ", " ");
		playerText = playerText.replaceAll(" with ", " ");

		// get rid of extra spaces
		while (playerText.contains("  "))
		{
			playerText = playerText.replaceAll("  ", " ");		
		}

		/* Check for an action. If the action is found, set it and trim it
		   from the beginning of the string. If not, check if the first word
		   is recognized by the game.

		*/

		String arg1 = "";
		String arg2 = "";
		String arg3 = "";

		for (String token : commandOne.keySet())
		{
			
			if (startsWith(token, playerText))
			{
				arg1 = token;
				state.type = ActionType.REFLEXIVE;
			}
		}

		for (String token : commandTwo.keySet())
		{
			if (startsWith(token, playerText))
			{
				arg1 = token;
				state.type = ActionType.DIRECT;
			}
		}

		for (String token : commandThree.keySet())
		{
			if (startsWith(token, playerText))
			{
				arg1 = token;
				state.type = ActionType.INDIRECT;
			}
		}

				
		if (arg1.isEmpty())
		{
			arg1 = words[0];
		}

		state.first = arg1;

		playerText = playerText.substring(arg1.length()).trim();
		if (playerText.isEmpty()) return;


		// Set the second argument

		for (String token : state.featureList.keySet())
		{
			if (startsWith(token, playerText))
				arg2 = token;
		}

		for (String token : state.itemList.keySet())
		{
			if (startsWith(token, playerText))
				arg2 = token;
		}

		for (String token : state.actorList.keySet())
		{
			if (startsWith(token, playerText))
				arg2 = token;
		}

		words = playerText.split(" ");
		if (arg2.isEmpty())
		{
			arg2 = words[0];
		}

		state.second = arg2;
		playerText = playerText.substring(arg2.length()).trim();
		if (playerText.isEmpty()) return;


		// Set the third argument
		
		for (String token : state.featureList.keySet())
		{
			if (startsWith(token, playerText))
				arg3 = token;
		}

		for (String token : state.itemList.keySet())
		{
			if (startsWith(token, playerText))
				arg3 = token;
		}

		for (String token : state.actorList.keySet())
		{
			if (startsWith(token, playerText))
				arg3 = token;
		}

		words = playerText.split(" ");
		if (arg3.isEmpty())
		{
			arg3 = words[0];
		}

		state.third = arg3;
		playerText = playerText.substring(arg3.length()).trim();
		if (!playerText.isEmpty())
		{
			output("I don't know what \"" + playerText + "\" means.");
		}



	}

	private static boolean validateAction(GameState state)
	{
		/* Verifies that the action arguments are recognized by the game.

		   Gets more information from the player if the action is incomplete.
		*/
		boolean result = true;


		String first = state.first;
		String second = state.second;
		String third = state.third;

		Action act = Action.NULL_ACTION;
		if (commandOne.containsKey(first)) { act = commandOne.get(first); }
		else if (commandTwo.containsKey(first)) { act = commandTwo.get(first); }
		else if (commandThree.containsKey(first)) { act = commandThree.get(first); }
		else
		{ 
			return false;
		}

		state.playerAction = act;


		// Incomplete actions
		if (commandTwo.containsKey(first) && second.isEmpty())
		{
			output("What do you want to " + first + "?");
			String input = getPlayerText();
			if (isGameWord(input))
			{
				second = input;
			}
			else
			{
				parsePlayerInput(state, input);
				return validateAction(state);
			}
		}


		if (commandThree.containsKey(first) && third.isEmpty())
		{

			if (second.isEmpty())
			{
				output("What do you want to " + first + "?");
				String input = getPlayerText();
				if (isGameWord(input))
				{
					second = input;
				}
				else
				{
					parsePlayerInput(state, input);
					return validateAction(state);
				}
			}

			output("What do you want to " + first + " the " + second + " with?");
			{
				String input2 = getPlayerText();
				if (isGameWord(input2))
				{
					third = input2;
				}
				else
				{
					parsePlayerInput(state, input2);
					return validateAction(state);
				}
			}
		}

		switch(state.type)
		{
			case BLANK:
			{

			} break;
			case REFLEXIVE:
			{

			} break;


			case DIRECT:
			case INDIRECT:
			{

				if (state.featureList.containsKey(second))
				// if (actionObjects.get(second).equals("feature"))
				{
					state.objectFeature = state.featureList.get(second);
				}

				else if (state.itemList.containsKey(second))
				//if (actionObjects.get(second).equals("item"))
				{
					state.objectItem = state.itemList.get(second);
				}

				else if (state.actorList.containsKey(second))
				//if (actionObjects.get(second).equals("actor"))
				{
					state.objectActor = state.actorList.get(second);
				}
				
				else 
				{
					return false;
				}

				if (!third.isEmpty())
				{
				
					if (state.itemList.containsKey(third))
					{
						Item it = state.itemList.get(third);
						if (it.getLocation() != Location.PLAYER_INVENTORY)
						{
							output("You're not carrying the " + it.name + ".");
							return false;
						}

						state.usedItem = it;
					}

					else
					{
						output("That isn't going to work.");
					}
					

				}
			
			} break;


			default:
			{
				// we should never be here
			} break;
		}

		return result;

	}


	private static void updateGame(GameState state)
	{
		


		Location curLoc = state.getPlayerLocation();
		Room curRoom = state.worldMap.get(curLoc);

		Action curAction = state.getPlayerAction();

		Feature objFeature = state.objectFeature;
		Item objItem = state.objectItem;
		Actor objActor = state.objectActor;

		Item indItem = state.usedItem;

		// For testing
		if (false)
		{
			output("Selection action is " + curAction);
			output("Selected feature is " + objFeature.name);
			output("Selected item is " + objItem.name);
			output("Selected actor is " + objActor.name);
			output("Indirect object is " + indItem.name);
		}
		



		switch (curAction)
		{

			// Features

			case ACTIVATE:
			case RING:
			case PLAY:
			case KICK:
			case READ:
			case TIE:
			case ATTACK:
			case HIGH_FIVE:
			case OPEN:
			{
				if (!objFeature.name.equals("null"))
				{
					if (objFeature.location == curLoc)
						objFeature.activate(state, curAction);
					else
						output("There's no " + objFeature.name + " here.");
				}

				if (!objActor.name.equals("null"))
				{
					if (objActor.location == curLoc)
						objActor.activate(state, curAction);
					else
						output("There's no " + objActor.name + " here.");
				}

				if (!objItem.name.equals("null"))
				{
					if (objItem.getLocation() == Location.PLAYER_INVENTORY)
						objItem.activate(state, curAction);
					else
						output("You're not carrying the " + objItem.name + ".");
				}

				

			} break;



			case LOOK:
			{
				curRoom.lookAround(state);

			} break;

			case TAKE:
			{
                if (objItem.name.equals("null"))
                {
                    output("That's not something you can take.");
                    return;
                }

                if (objItem.getLocation() == Location.PLAYER_INVENTORY)
				{
					output("You're already carrying the " + objItem.name + "!");
					return;
				}
				
				// Successful take
				if (objItem.getLocation() == curLoc)
				{
					objItem.setLocation(Location.PLAYER_INVENTORY);
					output("You picked up the " + objItem.name + ".");

					// Special cases where taking items affects the game state
					
				}

				else
				{
					output("There's no " + objItem.name + " here.");
				}	
			} break;

			case DROP:
			{
				if (objItem.getLocation() == Location.PLAYER_INVENTORY)
				{
					objItem.setLocation(curLoc);
					output("You dropped the " + objItem.name + ".");
				}
				else
				{
					output("You're not carrying that.");
				}
			} break;

			case INVENTORY:
			{
				output("You are carrying: \n");
				for (Item it : state.itemList.values())
				{
					if (it.getLocation() == Location.PLAYER_INVENTORY)
						output(it.name);
				}
			} break;


			case EXIT_NORTH:
			case EXIT_SOUTH:
			case EXIT_EAST:
			case EXIT_WEST:
            case EXIT_NORTHEAST:
            case EXIT_NORTHWEST:
            case EXIT_SOUTHEAST:
            case EXIT_SOUTHWEST:
			case EXIT_UP:
			case EXIT_DOWN:
			{
				boolean exited = curRoom.exit(state, curAction);

				if (exited)
				{
					curRoom = state.worldMap.get(state.getPlayerLocation());
					output(curRoom.name);
					outputLine();
					if (curRoom.firstVisit)
					{
						curRoom.firstVisit = false;
						curRoom.lookAround(state);
					}
				}

			} break;


			// Simple actions

			case WAIT: { output("Time passes..."); } break;
			case JUMP: { output("Wheeeeeeee!"); } break;
			case SHOUT: { output("Yaaaaarrrrggghhh!"); } break;
			case NULL_ACTION: {} break;
			case VERBOSE: { output("You said too many words."); } break;
			case PROFANITY: { output(GameStrings.PROFANITY_ONE); } break;			
			case QUIT: { /* if (verifyQuit()) */ gameover = true; } break;

			default: {} break;
		}

		// The player's action could end the game before anything else happens.
		if (gameover) return;


		for (Actor a : state.actorList.values())
		{
			if (a.isAlive()) { a.actorTurn(); }
		}

		state.addTurn();

	}


	// Utility methods used by the other methods in Game.java

	private static void createActions()
	{
		commandOne.put("north",       Action.EXIT_NORTH);
		commandOne.put("go north",    Action.EXIT_NORTH);
		commandOne.put("walk north",  Action.EXIT_NORTH);
		commandOne.put("exit north",  Action.EXIT_NORTH);
		commandOne.put("n",           Action.EXIT_NORTH);
		commandOne.put("go n",        Action.EXIT_NORTH);
		commandOne.put("walk n",      Action.EXIT_NORTH);
		commandOne.put("exit n",      Action.EXIT_NORTH);

		commandOne.put("south",       Action.EXIT_SOUTH);
		commandOne.put("go south",    Action.EXIT_SOUTH);
		commandOne.put("walk south",  Action.EXIT_SOUTH);
		commandOne.put("exit south",  Action.EXIT_SOUTH);
		commandOne.put("s",           Action.EXIT_SOUTH);
		commandOne.put("go s",        Action.EXIT_SOUTH);
		commandOne.put("walk s",      Action.EXIT_SOUTH);
		commandOne.put("exit s",      Action.EXIT_SOUTH);

		commandOne.put("east",        Action.EXIT_EAST);
		commandOne.put("e",           Action.EXIT_EAST);
		commandOne.put("go east",     Action.EXIT_EAST);
		commandOne.put("walk east",   Action.EXIT_EAST);
		commandOne.put("exit east",   Action.EXIT_EAST);
		commandOne.put("go e",        Action.EXIT_EAST);
		commandOne.put("walk e",      Action.EXIT_EAST);
		commandOne.put("exit e",      Action.EXIT_EAST);

		commandOne.put("west",        Action.EXIT_WEST);
		commandOne.put("go west",     Action.EXIT_WEST);
		commandOne.put("walk west",   Action.EXIT_WEST);
		commandOne.put("exit west",   Action.EXIT_WEST);
		commandOne.put("w",           Action.EXIT_WEST);
		commandOne.put("go w",        Action.EXIT_WEST);
		commandOne.put("walk w",      Action.EXIT_WEST);
		commandOne.put("exit w",      Action.EXIT_WEST);

        commandOne.put("northeast",        Action.EXIT_NORTHEAST);
        commandOne.put("go northeast",     Action.EXIT_NORTHEAST);
        commandOne.put("walk northeast",   Action.EXIT_NORTHEAST);
        commandOne.put("exit northeast",   Action.EXIT_NORTHEAST);
        commandOne.put("ne",               Action.EXIT_NORTHEAST);
        commandOne.put("go ne",            Action.EXIT_NORTHEAST);
        commandOne.put("walk ne",          Action.EXIT_NORTHEAST);
        commandOne.put("exit ne",          Action.EXIT_NORTHEAST);

        commandOne.put("northwest",        Action.EXIT_NORTHWEST);
        commandOne.put("go northwest",     Action.EXIT_NORTHWEST);
        commandOne.put("walk northwest",   Action.EXIT_NORTHWEST);
        commandOne.put("exit northwest",   Action.EXIT_NORTHWEST);
        commandOne.put("nw",               Action.EXIT_NORTHWEST);
        commandOne.put("go nw",            Action.EXIT_NORTHWEST);
        commandOne.put("walk nw",          Action.EXIT_NORTHWEST);
        commandOne.put("exit nw",          Action.EXIT_NORTHWEST);

        commandOne.put("southeast",        Action.EXIT_SOUTHEAST);
        commandOne.put("go southeast",     Action.EXIT_SOUTHEAST);
        commandOne.put("walk southeast",   Action.EXIT_SOUTHEAST);
        commandOne.put("exit southeast",   Action.EXIT_SOUTHEAST);
        commandOne.put("se",               Action.EXIT_SOUTHEAST);
        commandOne.put("go se",            Action.EXIT_SOUTHEAST);
        commandOne.put("walk se",          Action.EXIT_SOUTHEAST);
        commandOne.put("exit se",          Action.EXIT_SOUTHEAST);

        commandOne.put("southwest",        Action.EXIT_SOUTHWEST);
        commandOne.put("go southwest",     Action.EXIT_SOUTHWEST);
        commandOne.put("walk southwest",   Action.EXIT_SOUTHWEST);
        commandOne.put("exit southwest",   Action.EXIT_SOUTHWEST);
        commandOne.put("sw",               Action.EXIT_SOUTHWEST);
        commandOne.put("go sw",            Action.EXIT_SOUTHWEST);
        commandOne.put("walk sw",          Action.EXIT_SOUTHWEST);
        commandOne.put("exit sw",          Action.EXIT_SOUTHWEST);

		commandOne.put("up",	     Action.EXIT_UP);
		commandOne.put("go up",	     Action.EXIT_UP);
		commandOne.put("exit up",	 Action.EXIT_UP);
		commandOne.put("u",	         Action.EXIT_UP);
		commandOne.put("go u",	     Action.EXIT_UP);
		commandOne.put("exit u",	 Action.EXIT_UP);

		commandOne.put("down",       Action.EXIT_DOWN);
		commandOne.put("go down",    Action.EXIT_DOWN);
		commandOne.put("exit down",  Action.EXIT_DOWN);
		commandOne.put("d",          Action.EXIT_DOWN);
		commandOne.put("go d",       Action.EXIT_DOWN);
		commandOne.put("exit d",     Action.EXIT_DOWN);

		commandOne.put("quit",  Action.QUIT);
		commandOne.put("q",     Action.QUIT);
		commandOne.put("jump",  Action.JUMP);

		commandOne.put("look",  Action.LOOK);
		commandOne.put("look around",  Action.LOOK);
		commandOne.put("l",     Action.LOOK);

		commandOne.put("inventory", Action.INVENTORY);
		commandOne.put("i",         Action.INVENTORY);
		commandOne.put("fuck",  Action.PROFANITY);
		commandOne.put("shit",  Action.PROFANITY);
		commandOne.put("shout", Action.SHOUT);
		commandOne.put("yell",  Action.SHOUT);
		commandOne.put("scream",  Action.SHOUT);
		commandOne.put("wait", Action.WAIT);

		commandTwo.put("take", Action.TAKE);
		commandTwo.put("pick up", Action.TAKE);
		commandTwo.put("drop", Action.DROP);
		commandTwo.put("open", Action.OPEN);
		commandTwo.put("close", Action.CLOSE);
		commandTwo.put("lock", Action.LOCK);
		commandTwo.put("say", Action.SPEAK);
		commandTwo.put("ring", Action.RING);
		commandTwo.put("play", Action.PLAY);
		commandTwo.put("read", Action.READ);
		commandTwo.put("kick", Action.KICK);
		commandTwo.put("hit", Action.ATTACK);
		commandTwo.put("attack", Action.ATTACK);
		commandTwo.put("punch", Action.ATTACK);
		commandTwo.put("slap", Action.SLAP);
		commandTwo.put("highfive", Action.HIGH_FIVE);
		commandTwo.put("high five", Action.HIGH_FIVE);

		commandThree.put("open", Action.OPEN);
		commandThree.put("unlock", Action.UNLOCK);
		commandThree.put("lock", Action.LOCK);
		commandThree.put("tie", Action.TIE);
	}

	private static void fillDictionary()
	{
		for (int i = 0; i < GameStrings.GAME_WORDS.length; ++i)
		{
			dictionary.add(GameStrings.GAME_WORDS[i]);
		}
	}

	public static void prompt() { System.out.print(">> "); }
	public static void outputLine() { System.out.println(); }
	public static void output() { System.out.println(); }
	public static void output(String s) { System.out.println(s); }

	private static String getPlayerText()
	{
		Scanner scn = new Scanner(System.in);
		String result = "";
		prompt();

		while(result.isEmpty())
		{
			result = scn.nextLine();

			if (result.isEmpty())
			{
				output("What?");
				prompt();
			}
		}


		return result.trim().toLowerCase();
	}

	// Checks if "input" starts with "token"
	private static boolean startsWith(String tok, String inp)
	{
		String[] token = tok.split(" ");
		String[] input  = inp.split(" ");

		if (input.length < token.length)
			return false;

		for (int i = 0; i < token.length; ++i)
		{
			if (!token[i].equals(input[i]))
				return false;
		}

		return true;
	}

	private static boolean isGameWord(String str)
	{
		return (dictionary.contains(str));
	}

	public static boolean verifyQuit()
	{
		boolean result = false;
		Scanner scn = new Scanner(System.in);
		output("Are you sure you want to quit?");
		String input = scn.nextLine().toLowerCase();
		if (input.equals("y")) result = true;
		if (input.equals("yes")) result = true;

		return result;
	}



	private static void endGame(GameState state)
	{
		// Save the gamestate

		output("Game has ended.");
		output("Total turns: " + state.turns);

	}



}