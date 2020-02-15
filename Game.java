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
	NORTH,
	SOUTH,
	EAST,
	WEST,
    NORTHEAST,
    NORTHWEST,
    SOUTHEAST,
    SOUTHWEST,
	UP,
	DOWN,
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

    private static final int LINE_LENGTH = 50;


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
        Passage house_west_north = new Passage(Location.WEST_OF_HOUSE, Location.NORTH_OF_HOUSE);
        Passage house_west_south = new Passage(Location.WEST_OF_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house_west_barrow = new Passage(Location.WEST_OF_HOUSE, Location.STONE_BARROW);
        Passage house_west_forestW = new Passage(Location.WEST_OF_HOUSE, Location.FOREST_WEST);

        // North of House
        Passage house_north_forestpath = new Passage(Location.NORTH_OF_HOUSE, Location.FOREST_PATH);
        Passage house_north_behind = new Passage(Location.NORTH_OF_HOUSE, Location.BEHIND_HOUSE);

        // Behind house
        Passage house_behind_clearingE = new Passage(Location.BEHIND_HOUSE, Location.CLEARING_EAST);
        Passage house_behind_south = new Passage(Location.BEHIND_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house_behind_kitchen = new Passage(Location.BEHIND_HOUSE, Location.KITCHEN);

        // South of House
        Passage house_south_forestS = new Passage(Location.SOUTH_OF_HOUSE, Location.FOREST_SOUTH);

        // Kitchen
        Passage kitchen_attic = new Passage(Location.KITCHEN, Location.ATTIC);
        Passage kitchen_livingroom = new Passage(Location.KITCHEN, Location.LIVING_ROOM);

        // Forest Path
        Passage forestpath_clearingN = new Passage(Location.FOREST_PATH, Location.CLEARING_NORTH);
        Passage forestpath_forestE = new Passage(Location.FOREST_PATH, Location.FOREST_EAST);
        Passage forestpath_forestW = new Passage(Location.FOREST_PATH, Location.FOREST_WEST);
        Passage forestpath_uptree = new Passage(Location.FOREST_PATH, Location.UP_TREE);

        // Clearing North
        Passage clearingN_forestE = new Passage(Location.CLEARING_NORTH, Location.FOREST_EAST);
        Passage clearingN_forestW = new Passage(Location.CLEARING_NORTH, Location.FOREST_WEST);

        // Forest East
        Passage forestE_clearingE = new Passage(Location.FOREST_EAST, Location.CLEARING_EAST);
        Passage forestE_forestNE = new Passage(Location.FOREST_EAST, Location.FOREST_NORTHEAST);

        // Clearing East
        Passage clearingE_forestS = new Passage(Location.CLEARING_EAST, Location.FOREST_SOUTH);
        Passage clearingE_canyon = new Passage(Location.CLEARING_EAST, Location.CANYON_VIEW);

        // Forest South
        Passage forestS_canyon = new Passage(Location.FOREST_SOUTH, Location.CANYON_VIEW);
        Passage forestS_forestW = new Passage(Location.FOREST_SOUTH, Location.FOREST_WEST);

        // Canyon View
        Passage canyon_ledge = new Passage(Location.CANYON_VIEW, Location.ROCKY_LEDGE);

        // Rocky Ledge
        Passage ledge_bottom = new Passage(Location.ROCKY_LEDGE, Location.CANYON_BOTTOM);

        // Canyon Bottom
        Passage canyon_bottom_rainbow = new Passage(Location.CANYON_BOTTOM, Location.END_OF_RAINBOW);

        // Stone Barrow
        Passage barrowInside = new Passage(Location.STONE_BARROW, Location.INSIDE_STONE_BARROW);





        // Rooms: Name, description, ID
        Room westOfHouse = new Room("West of House", GameStrings.DESC_WEST_OF_HOUSE, Location.WEST_OF_HOUSE);
        westOfHouse.addExit(Action.NORTH, house_west_north);
        westOfHouse.addExit(Action.NORTHEAST, house_west_north);
        westOfHouse.addExit(Action.SOUTH, house_west_south);
        westOfHouse.addExit(Action.SOUTHEAST, house_west_south);
        westOfHouse.addExit(Action.SOUTHWEST, house_west_barrow);
        westOfHouse.addExit(Action.WEST, house_west_forestW);

        Room northOfHouse = new Room("North of House", GameStrings.DESC_NORTH_OF_HOUSE, Location.NORTH_OF_HOUSE);
        northOfHouse.addExit(Action.NORTH, house_north_forestpath);
        northOfHouse.addExit(Action.EAST, house_north_behind);
        northOfHouse.addExit(Action.SOUTHEAST, house_north_behind);
        northOfHouse.addExit(Action.SOUTHWEST, house_west_north);
        northOfHouse.addExit(Action.WEST, house_west_north);


        Room behindHouse = new Room("Behind House", GameStrings.DESC_BEHIND_HOUSE, Location.BEHIND_HOUSE);
        behindHouse.addExit(Action.NORTH, house_north_behind);
        behindHouse.addExit(Action.NORTHWEST, house_north_behind);
        behindHouse.addExit(Action.EAST, house_behind_clearingE);
        behindHouse.addExit(Action.SOUTH, house_behind_south);
        behindHouse.addExit(Action.SOUTHWEST, house_behind_south);
        behindHouse.addExit(Action.WEST, house_behind_kitchen);


        Room southOfHouse = new Room("South of House", GameStrings.DESC_SOUTH_OF_HOUSE, Location.SOUTH_OF_HOUSE);
        southOfHouse.addExit(Action.EAST, house_behind_south);
        southOfHouse.addExit(Action.NORTHEAST, house_behind_south);
        southOfHouse.addExit(Action.WEST, house_west_south);
        southOfHouse.addExit(Action.NORTHWEST, house_west_south);
        southOfHouse.addExit(Action.SOUTH, house_south_forestS);


        Room kitchen = new Room("Kitchen", GameStrings.DESC_KITCHEN_WINDOW_CLOSED, Location.KITCHEN);
        kitchen.addExit(Action.EAST, house_behind_kitchen);
        kitchen.addExit(Action.WEST, kitchen_livingroom);
        kitchen.addExit(Action.UP, kitchen_attic);

        Room attic = new Room("Attic", GameStrings.DESC_ATTIC, Location.ATTIC);
        attic.addExit(Action.DOWN, kitchen_attic);
        attic.setDark();

        Room livingRoom = new Room("Living Room", GameStrings.DESC_LIVING_ROOM_TRAPDOOR_CLOSED, Location.LIVING_ROOM);
        livingRoom.addExit(Action.EAST, kitchen_livingroom);

        Room forestPath = new Room("Forest Path", GameStrings.DESC_FOREST_PATH, Location.FOREST_PATH);
        forestPath.addExit(Action.NORTH, forestpath_clearingN);
        forestPath.addExit(Action.EAST, forestpath_forestE);
        forestPath.addExit(Action.SOUTH, house_north_forestpath);
        forestPath.addExit(Action.WEST, forestpath_forestW);
        forestPath.addExit(Action.UP, forestpath_uptree);

        Room upTree = new Room("Up a Tree", GameStrings.DESC_UP_TREE, Location.UP_TREE);
        upTree.addExit(Action.DOWN, forestpath_uptree);

        Room forestWest = new Room("Forest", GameStrings.DESC_FOREST_WEST, Location.FOREST_WEST);
        forestWest.addExit(Action.NORTH, clearingN_forestW);
        forestWest.addExit(Action.EAST, forestpath_forestW);
        forestWest.addExit(Action.SOUTH, forestS_forestW);

        Room forestEast = new Room("Forest", GameStrings.DESC_FOREST_EAST, Location.FOREST_EAST);
        forestEast.addExit(Action.NORTHWEST, clearingN_forestE);
        forestEast.addExit(Action.EAST, forestE_forestNE);
        forestEast.addExit(Action.SOUTH, forestE_clearingE);
        forestEast.addExit(Action.WEST, forestpath_forestE);

        Room forestNortheast = new Room("Forest", GameStrings.DESC_FOREST_NORTHEAST, Location.FOREST_NORTHEAST);
        forestNortheast.addExit(Action.NORTH, forestE_forestNE);
        forestNortheast.addExit(Action.SOUTH, forestE_forestNE);
        forestNortheast.addExit(Action.WEST, forestE_forestNE);
        forestNortheast.addFailMessage(Action.EAST, GameStrings.FOREST_NE_FAIL_1);

        Room forestSouth = new Room("Forest", GameStrings.DESC_FOREST_SOUTH, Location.FOREST_SOUTH);
        forestSouth.addExit(Action.NORTH, clearingE_forestS);
        forestSouth.addExit(Action.EAST, forestS_canyon);
        forestSouth.addExit(Action.WEST, forestS_forestW);
        forestSouth.addExit(Action.NORTHWEST, house_south_forestS);


        Room clearingNorth = new Room("Clearing", GameStrings.DESC_CLEARING_NORTH, Location.CLEARING_NORTH);
        clearingNorth.addExit(Action.EAST, clearingN_forestE);
        clearingNorth.addExit(Action.SOUTH, forestpath_clearingN);
        clearingNorth.addExit(Action.WEST, clearingN_forestW);

        Room clearingEast = new Room("Clearing", GameStrings.DESC_CLEARING_EAST, Location.CLEARING_EAST);
        clearingEast.addExit(Action.NORTH, forestE_clearingE);
        clearingEast.addExit(Action.EAST, forestE_clearingE);
        clearingEast.addExit(Action.SOUTH, clearingE_forestS);
        clearingEast.addExit(Action.WEST, forestE_clearingE);


        Room canyonView = new Room("Canyon View", GameStrings.DESC_CANYON_VIEW, Location.CANYON_VIEW);
        canyonView.addExit(Action.NORTHWEST, clearingE_canyon);
        canyonView.addExit(Action.WEST, forestS_canyon);
        canyonView.addExit(Action.DOWN, canyon_ledge);

        Room rockyLedge = new Room("Rocky Ledge", GameStrings.DESC_ROCKY_LEDGE, Location.ROCKY_LEDGE);
        rockyLedge.addExit(Action.UP, canyon_ledge);
        rockyLedge.addExit(Action.DOWN, ledge_bottom);

        Room canyonBottom = new Room("Canyon Bottom", GameStrings.DESC_CANYON_BOTTOM, Location.CANYON_BOTTOM);
        canyonBottom.addExit(Action.UP, ledge_bottom);
        canyonBottom.addExit(Action.NORTH, canyon_bottom_rainbow);

        Room endOfRainbow = new Room("End of Rainbow", GameStrings.DESC_END_OF_RAINBOW, Location.END_OF_RAINBOW);
        endOfRainbow.addExit(Action.SOUTHWEST, canyon_bottom_rainbow);

        Room stoneBarrow = new Room("Stone Barrow", GameStrings.DESC_STONE_BARROW, Location.STONE_BARROW);
        stoneBarrow.addExit(Action.NORTHEAST, house_west_barrow);
        stoneBarrow.addExit(Action.WEST, barrowInside);

        Room insideStoneBarrow = new Room("Inside Stone Barrow", GameStrings.DESC_INSIDE_STONE_BARROW, Location.INSIDE_STONE_BARROW);
        insideStoneBarrow.addExit(Action.EAST, barrowInside);


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

		



        ActivateMethod dummyMethod = (GameState gs, Action act) -> {};


		

		

	


		

	

		// Game objects complete. Start setting up the game



	

		


		// Put the player in the starting location
		state.setPlayerLocation(initialLocation);
		state.worldMap.get(initialLocation).firstVisit = false;

		// Beginning text of the game.
        outputLine();
        output(state.worldMap.get(initialLocation).name);
		output(GameStrings.DESC_WEST_OF_HOUSE);
        outputLine();
		
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
                output(curRoom.name);
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


			case NORTH:
			case SOUTH:
			case EAST:
			case WEST:
            case NORTHEAST:
            case NORTHWEST:
            case SOUTHEAST:
            case SOUTHWEST:
			case UP:
			case DOWN:
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
        outputLine();

	}


	// Utility methods used by the other methods in Game.java

	private static void createActions()
	{
		commandOne.put("north",       Action.NORTH);
		commandOne.put("go north",    Action.NORTH);
		commandOne.put("walk north",  Action.NORTH);
		commandOne.put("exit north",  Action.NORTH);
		commandOne.put("n",           Action.NORTH);
		commandOne.put("go n",        Action.NORTH);
		commandOne.put("walk n",      Action.NORTH);
		commandOne.put("exit n",      Action.NORTH);

		commandOne.put("south",       Action.SOUTH);
		commandOne.put("go south",    Action.SOUTH);
		commandOne.put("walk south",  Action.SOUTH);
		commandOne.put("exit south",  Action.SOUTH);
		commandOne.put("s",           Action.SOUTH);
		commandOne.put("go s",        Action.SOUTH);
		commandOne.put("walk s",      Action.SOUTH);
		commandOne.put("exit s",      Action.SOUTH);

		commandOne.put("east",        Action.EAST);
		commandOne.put("e",           Action.EAST);
		commandOne.put("go east",     Action.EAST);
		commandOne.put("walk east",   Action.EAST);
		commandOne.put("exit east",   Action.EAST);
		commandOne.put("go e",        Action.EAST);
		commandOne.put("walk e",      Action.EAST);
		commandOne.put("exit e",      Action.EAST);

		commandOne.put("west",        Action.WEST);
		commandOne.put("go west",     Action.WEST);
		commandOne.put("walk west",   Action.WEST);
		commandOne.put("exit west",   Action.WEST);
		commandOne.put("w",           Action.WEST);
		commandOne.put("go w",        Action.WEST);
		commandOne.put("walk w",      Action.WEST);
		commandOne.put("exit w",      Action.WEST);

        commandOne.put("northeast",        Action.NORTHEAST);
        commandOne.put("go northeast",     Action.NORTHEAST);
        commandOne.put("walk northeast",   Action.NORTHEAST);
        commandOne.put("exit northeast",   Action.NORTHEAST);
        commandOne.put("ne",               Action.NORTHEAST);
        commandOne.put("go ne",            Action.NORTHEAST);
        commandOne.put("walk ne",          Action.NORTHEAST);
        commandOne.put("exit ne",          Action.NORTHEAST);

        commandOne.put("northwest",        Action.NORTHWEST);
        commandOne.put("go northwest",     Action.NORTHWEST);
        commandOne.put("walk northwest",   Action.NORTHWEST);
        commandOne.put("exit northwest",   Action.NORTHWEST);
        commandOne.put("nw",               Action.NORTHWEST);
        commandOne.put("go nw",            Action.NORTHWEST);
        commandOne.put("walk nw",          Action.NORTHWEST);
        commandOne.put("exit nw",          Action.NORTHWEST);

        commandOne.put("southeast",        Action.SOUTHEAST);
        commandOne.put("go southeast",     Action.SOUTHEAST);
        commandOne.put("walk southeast",   Action.SOUTHEAST);
        commandOne.put("exit southeast",   Action.SOUTHEAST);
        commandOne.put("se",               Action.SOUTHEAST);
        commandOne.put("go se",            Action.SOUTHEAST);
        commandOne.put("walk se",          Action.SOUTHEAST);
        commandOne.put("exit se",          Action.SOUTHEAST);

        commandOne.put("southwest",        Action.SOUTHWEST);
        commandOne.put("go southwest",     Action.SOUTHWEST);
        commandOne.put("walk southwest",   Action.SOUTHWEST);
        commandOne.put("exit southwest",   Action.SOUTHWEST);
        commandOne.put("sw",               Action.SOUTHWEST);
        commandOne.put("go sw",            Action.SOUTHWEST);
        commandOne.put("walk sw",          Action.SOUTHWEST);
        commandOne.put("exit sw",          Action.SOUTHWEST);

		commandOne.put("up",	     Action.UP);
		commandOne.put("go up",	     Action.UP);
		commandOne.put("exit up",	 Action.UP);
		commandOne.put("u",	         Action.UP);
		commandOne.put("go u",	     Action.UP);
		commandOne.put("exit u",	 Action.UP);

		commandOne.put("down",       Action.DOWN);
		commandOne.put("go down",    Action.DOWN);
		commandOne.put("exit down",  Action.DOWN);
		commandOne.put("d",          Action.DOWN);
		commandOne.put("go d",       Action.DOWN);
		commandOne.put("exit d",     Action.DOWN);

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
        commandTwo.put("get", Action.TAKE);
        commandTwo.put("acquire", Action.TAKE);
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
	public static void output(String s)
    {
        String[] words = s.split(" ");
        int count = 0;

        for (int i = 0; i < words.length; ++i)
        {
            if (count > 50)
            {
                if (words[i].charAt(0) != '\n')
                    System.out.print("\n");
                count = 0;
            }

            System.out.print(words[i] + " ");
            count += words[i].length();
        }

        System.out.print("\n");
    }

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