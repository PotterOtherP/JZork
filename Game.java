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
    INSIDE_MAILBOX,
	PLAYER_INVENTORY,
    INSIDE_TROPHY_CASE,
    INSIDE_SACK,
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
	QUIT,
	VERBOSE,
	PROFANITY,
	WAIT,
	DEFEND,
	HIGH_FIVE,

	TAKE,
	DROP,
    STORE,
    PLACE,
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

	NULL_TYPE,
	REFLEXIVE,
	DIRECT,
	INDIRECT,
    EXIT,
    TAKE_DROP,
    OPEN_CLOSE
}

enum ObjectType {

    NULL_TYPE,
    FEATURE,
    ITEM,
    ACTOR,
    CONTAINER
}



/**
 * This program is my attempt to replicate Zork I as closely as possible.
 *
 * @author Nathan Tryon January 2020 - 
 */
public final class Game {

    /* TODO
     *
     * Ambiguous words - Alternate names for objects
     * Create dictionary from existing lists
     * Object presence strings
     * New object type: Container
     * List of currently actionable objects?
     * Universal action methods in class definitions (take, put, open, read, etc)
     *
     */


	private static boolean gameover = true;
	private static boolean godmode = false;



    private static HashMap<String, Action> actions = new HashMap<String, Action>();
	private static HashMap<Action, ActionType> actionTypes = new HashMap<Action, ActionType>();
    private static HashMap<String, ObjectType> currentObjects = new HashMap<String, ObjectType>();

	private static ArrayList<String> dictionary = new ArrayList<String>();
    private static ArrayList<String> objectStrings = new ArrayList<String>();

	private static Location initialLocation = Location.WEST_OF_HOUSE;

    private static final int LINE_LENGTH = 50;
    private static final boolean DEBUG = true;


	public static void main(String[] args)
	{

		GameState gameState = new GameState();


		String playerText = "";

		
		initGame(gameState);


		gameover = false;

		while (!gameover)
		{	
			playerText = getPlayerText();

			if (parsePlayerInput(gameState, playerText))
            {
                validateAction(gameState);
                updateGame(gameState);
            }


		}


		endGame(gameState);
		
	}


	private static void initGame(GameState state)
	{	
		// Populate the action lists and the dictionary
		createActions();
		fillDictionary();

		// Create all the objects, then add them to the lists, then add their methods



        // Passages: 

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


		/* Features - Overworld
         * 
         * Mailbox (West of House)
         * Window (Kitchen, Behind House)
         * Carpet (Living Room)
         * Trophy Case (Living Room)
         * Trap Door (Living Room)
         * Pile of Leaves(Clearing North)
         *
         */
        
        Container mailbox = new Container("mailbox", Location.WEST_OF_HOUSE, 10, Location.INSIDE_MAILBOX);
        Container trophyCase = new Container("trophy case", Location.LIVING_ROOM, 1000, Location.INSIDE_TROPHY_CASE);
        Container brownSack = new Container("sack", Location.KITCHEN, 50, Location.INSIDE_SACK);

        state.containerList.put(mailbox.name, mailbox);
        state.containerList.put(trophyCase.name, trophyCase);
        state.containerList.put(brownSack.name, brownSack);

        // These are the same window!
        Feature houseWindow = new Feature("house window", Location.BEHIND_HOUSE);
        Feature kitchenWindow = new Feature("kitchen window", Location.KITCHEN);
        Feature carpet = new Feature("carpet", Location.LIVING_ROOM);
        Feature trapDoor = new Feature("trap door", Location.LIVING_ROOM);
        Feature leafPile = new Feature("pile", Location.CLEARING_NORTH);

        state.featureList.put(houseWindow.name, houseWindow);
        state.featureList.put(kitchenWindow.name, kitchenWindow);
        state.featureList.put(carpet.name, carpet);
        state.featureList.put(trapDoor.name, trapDoor);
        state.featureList.put(leafPile.name, leafPile);


        /* Items - Overworld
         * 
         * Rope (Attic)
         * Rusty Knife (Attic)
         * Glass Bottle (Kitchen)
         * Brown Sack (Kitchen)
         * Brass Lantern (Living Room)
         * Elvish Sword (Living Room)
         * Jewel-Encrusted Egg (Up a Tree)
         * Small Bird's Nest (Up a Tree)
         * Leaflet (West of House)
         *
         * Item: name, location, point value, weight
         */
        Item leaflet = new Item("leaflet", Location.WEST_OF_HOUSE, 0, 0);
        Item rope = new Item("rope", Location.ATTIC, 0, 0);
        Item rustyKnife = new Item("knife", Location.ATTIC, 0, 0);
        Item glassBottle = new Item("bottle", Location.KITCHEN, 0, 0);
        Item elvishSword = new Item("sword", Location.LIVING_ROOM, 0, 0);
        Item jewelEgg = new Item("egg", Location.UP_TREE, 0, 0);
        Item birdsNest = new Item("nest", Location.UP_TREE, 0, 0);

        state.itemList.put(leaflet.name, leaflet);
        state.itemList.put(rope.name, rope);
        state.itemList.put(rustyKnife.name, rustyKnife);
        state.itemList.put(glassBottle.name, glassBottle);
        state.itemList.put(elvishSword.name, elvishSword);
        state.itemList.put(jewelEgg.name, jewelEgg);
        state.itemList.put(birdsNest.name, birdsNest);


        /* Actors - Underworld
         *
         * Troll
         * Thief
         * Cyclops
         *
         */
        
        // Testing an actor in the overworld.
        Actor giant = new Actor("giant", Location.FOREST_SOUTH);
        ActorMethod giantMethod = () -> {};
        giant.setActorMethod(giantMethod);

        state.actorList.put(giant.name, giant);
		



        


        ActivateMethod dummyMethod = (GameState gs, Action act) -> {};

        


        ActivateMethod leafletMethod = (GameState gs, Action act) -> {

            switch(act)
            {
                case READ:
                {
                    output(GameStrings.LEAFLET_TEXT);
                }
            }


        };
		
        leaflet.setMethod(leafletMethod);
	


		

	   fillObjectStringList(state);


		// Object creation complete. Start setting up the game

		// Put the player in the starting location
		state.setPlayerLocation(initialLocation);
		state.worldMap.get(initialLocation).firstVisit = false;

		// Beginning text of the game.
        outputLine();
        output(state.worldMap.get(initialLocation).name);
		output(GameStrings.DESC_WEST_OF_HOUSE);
        outputLine();
		
	}






	private static boolean parsePlayerInput(GameState state, String playerText)
	{
        /* ACTION OBJECT OBJECT.

        Not assigning direct or indirect objects in this method, or validating anything else.
        Just checking for 1 to 3 game-recognized phrases. Look for one, remove it, if there is more in the string,
        check for another phrase.

        */
		state.resetInput();

		

		String[] words = playerText.split(" ");

		for (int i = 0; i < words.length; ++i)
		{
			if (!isGameWord(words[i]))
			{
				output("I don't know what \"" + words[i] + "\" means.");
				return false;
			}
		}

		// Make sure we're deleting the words, not portions of other words...
        playerText = " " + playerText + " ";
		playerText = playerText.replaceAll(" the ", " ");
		playerText = playerText.replaceAll(" to ", " ");
		playerText = playerText.replaceAll(" with ", " ");
        playerText = playerText.trim();

		// get rid of extra spaces
		while (playerText.contains("  "))
		{
			playerText = playerText.replaceAll("  ", " ");		
		}



        // See if the player text starts with an action.
		for (String token : actions.keySet())
		{
			
			if (startsWith(token, playerText))
			{
                state.first = token;
                
			}
		}


        // If not, exit		
		if (state.first.isEmpty())
		{
			output("Sentence did not start with an action.");
            return true;
		}

		
		playerText = playerText.substring(state.first.length()).trim();
		if (playerText.isEmpty()) return true;

        for (String token : objectStrings)
        {
            if (startsWith(token, playerText))
            {
                state.second = token;
            }
        }

        if (state.second.isEmpty())
        {
            output("Second phrase not recognized.");
            return true;
        }

        playerText = playerText.substring(state.second.length()).trim();
        if (playerText.isEmpty()) return true;

        for (String token : objectStrings)
        {
            if (startsWith(token, playerText))
            {
                state.third = token;
            }
        }

        if (state.third.isEmpty())
        {
            output("Third phrase not recognized.");
            return true;
        }


		return true;

	}

	private static boolean validateAction(GameState state)
	{
		/* 
            Create a list of actionable objects at the beginning of each turn.
            Use the objects in the player's location, inventory, and any containers in the same location.

            Recognize the selected action from the first phrase.

            Based on action type, recognize the direct and indirect objects if warranted.
            Find the objects in the actionable object list.

		*/

		boolean result = true;

        // Need to address ambiguous words here - the same key can't occur twice in a hashmap.
        fillCurrentObjectList(state);

        if (false)
        {
            output("Parse player text results: ");
            output("First phrase is: " + state.first);
            output("Second phrase is: " + state.second);
            output("Third phrase is: " + state.third);
        }


		String first = state.first;
		String second = state.second;
		String third = state.third;

        state.playerAction = actions.get(first);
        state.type = actionTypes.get(state.playerAction);

        
		

        if (state.playerAction == Action.QUIT)
        {
            return true;
        }

		switch(state.type)
		{

			case REFLEXIVE:
			{

			} break;


			case DIRECT:
            case OPEN_CLOSE:
            case TAKE_DROP:
            {
                if (currentObjects.containsKey(second))
                {
                    ObjectType objType = currentObjects.get(second);

                    switch(objType)
                    {
                        case FEATURE:
                            state.objectFeature = state.featureList.get(second);
                            break;
                        case ITEM:
                            state.objectItem = state.itemList.get(second);
                            break;
                        case ACTOR:
                            state.objectActor = state.actorList.get(second);
                            break;
                        case CONTAINER:
                            state.objectContainer = state.containerList.get(second);
                            break;
                        default:
                            break;
                    }
                }
            } break;

			case INDIRECT:
			{

				if (currentObjects.containsKey(second))
                {
                    ObjectType objType = currentObjects.get(second);

                    switch(objType)
                    {
                        case FEATURE:
                            state.objectFeature = state.featureList.get(second);
                            break;
                        case ITEM:
                            state.objectItem = state.itemList.get(second);
                            break;
                        case ACTOR:
                            state.objectActor = state.actorList.get(second);
                            break;
                        case CONTAINER:
                            state.objectContainer = state.containerList.get(second);
                            break;
                        default:
                            break;
                    }
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

						state.indirectObject = it;
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

		Item indObj = state.indirectObject;

		// For testing
		if (false)
		{
			output("Selected action is " + curAction);
			output("Selected feature is " + objFeature.name);
			output("Selected item is " + objItem.name);
			output("Selected actor is " + objActor.name);
            output("Selected container is " + state.objectContainer.name);
			output("Indirect object is " + indObj.name);
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
            case CLOSE:
			{
				if (!objFeature.name.equals("dummy_feature"))
				{
					if (objFeature.location == curLoc)
						objFeature.activate(state, curAction);
					else
						output("There's no " + objFeature.name + " here.");
				}

				if (!objActor.name.equals("dummy_actor"))
				{
					if (objActor.location == curLoc)
						objActor.activate(state, curAction);
					else
						output("There's no " + objActor.name + " here.");
				}

				if (!objItem.name.equals("dummy_item"))
				{
					if (objItem.getLocation() == Location.PLAYER_INVENTORY)
						objItem.activate(state, curAction);
					else
						output("You're not carrying the " + objItem.name + ".");
				}
                if (!state.objectContainer.name.equals("dummy_container"))
                {
                    if (state.objectContainer.location == curLoc)
                        state.objectContainer.activate(state, curAction);
                    else
                        output("There's no " + state.objectContainer.name + " here.");
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
        // Movement actions
		actions.put("north",       Action.NORTH);
		actions.put("go north",    Action.NORTH);
		actions.put("walk north",  Action.NORTH);
		actions.put("exit north",  Action.NORTH);
		actions.put("n",           Action.NORTH);
		actions.put("go n",        Action.NORTH);
		actions.put("walk n",      Action.NORTH);
		actions.put("exit n",      Action.NORTH);

		actions.put("south",       Action.SOUTH);
		actions.put("go south",    Action.SOUTH);
		actions.put("walk south",  Action.SOUTH);
		actions.put("exit south",  Action.SOUTH);
		actions.put("s",           Action.SOUTH);
		actions.put("go s",        Action.SOUTH);
		actions.put("walk s",      Action.SOUTH);
		actions.put("exit s",      Action.SOUTH);

		actions.put("east",        Action.EAST);
		actions.put("e",           Action.EAST);
		actions.put("go east",     Action.EAST);
		actions.put("walk east",   Action.EAST);
		actions.put("exit east",   Action.EAST);
		actions.put("go e",        Action.EAST);
		actions.put("walk e",      Action.EAST);
		actions.put("exit e",      Action.EAST);

		actions.put("west",        Action.WEST);
		actions.put("go west",     Action.WEST);
		actions.put("walk west",   Action.WEST);
		actions.put("exit west",   Action.WEST);
		actions.put("w",           Action.WEST);
		actions.put("go w",        Action.WEST);
		actions.put("walk w",      Action.WEST);
		actions.put("exit w",      Action.WEST);

        actions.put("northeast",        Action.NORTHEAST);
        actions.put("go northeast",     Action.NORTHEAST);
        actions.put("walk northeast",   Action.NORTHEAST);
        actions.put("exit northeast",   Action.NORTHEAST);
        actions.put("ne",               Action.NORTHEAST);
        actions.put("go ne",            Action.NORTHEAST);
        actions.put("walk ne",          Action.NORTHEAST);
        actions.put("exit ne",          Action.NORTHEAST);

        actions.put("northwest",        Action.NORTHWEST);
        actions.put("go northwest",     Action.NORTHWEST);
        actions.put("walk northwest",   Action.NORTHWEST);
        actions.put("exit northwest",   Action.NORTHWEST);
        actions.put("nw",               Action.NORTHWEST);
        actions.put("go nw",            Action.NORTHWEST);
        actions.put("walk nw",          Action.NORTHWEST);
        actions.put("exit nw",          Action.NORTHWEST);

        actions.put("southeast",        Action.SOUTHEAST);
        actions.put("go southeast",     Action.SOUTHEAST);
        actions.put("walk southeast",   Action.SOUTHEAST);
        actions.put("exit southeast",   Action.SOUTHEAST);
        actions.put("se",               Action.SOUTHEAST);
        actions.put("go se",            Action.SOUTHEAST);
        actions.put("walk se",          Action.SOUTHEAST);
        actions.put("exit se",          Action.SOUTHEAST);

        actions.put("southwest",        Action.SOUTHWEST);
        actions.put("go southwest",     Action.SOUTHWEST);
        actions.put("walk southwest",   Action.SOUTHWEST);
        actions.put("exit southwest",   Action.SOUTHWEST);
        actions.put("sw",               Action.SOUTHWEST);
        actions.put("go sw",            Action.SOUTHWEST);
        actions.put("walk sw",          Action.SOUTHWEST);
        actions.put("exit sw",          Action.SOUTHWEST);

		actions.put("up",	     Action.UP);
		actions.put("go up",	     Action.UP);
		actions.put("exit up",	 Action.UP);
		actions.put("u",	         Action.UP);
		actions.put("go u",	     Action.UP);
		actions.put("exit u",	 Action.UP);

		actions.put("down",       Action.DOWN);
		actions.put("go down",    Action.DOWN);
		actions.put("exit down",  Action.DOWN);
		actions.put("d",          Action.DOWN);
		actions.put("go d",       Action.DOWN);
		actions.put("exit d",     Action.DOWN);

        // Simple actions: no interaction with game objects
		actions.put("quit",  Action.QUIT);
		actions.put("q",     Action.QUIT);
		actions.put("jump",  Action.JUMP);
		actions.put("look around",  Action.LOOK);
		actions.put("look",  Action.LOOK);
		actions.put("l",     Action.LOOK);
		actions.put("inventory", Action.INVENTORY);
		actions.put("i",         Action.INVENTORY);
		actions.put("fuck",  Action.PROFANITY);
		actions.put("shit",  Action.PROFANITY);
		actions.put("shout", Action.SHOUT);
		actions.put("yell",  Action.SHOUT);
		actions.put("scream",  Action.SHOUT);
		actions.put("wait", Action.WAIT);
		actions.put("say", Action.SPEAK);


        // General object interaction actions
        actions.put("take", Action.TAKE);
        actions.put("pick up", Action.TAKE);
        actions.put("get", Action.TAKE);
        actions.put("acquire", Action.TAKE);
        actions.put("drop", Action.DROP);
        actions.put("open", Action.OPEN);
        actions.put("close", Action.CLOSE);
        actions.put("lock", Action.LOCK);
        actions.put("read", Action.READ);
        actions.put("unlock", Action.UNLOCK);
        actions.put("lock", Action.LOCK);

        // Combat actions
        actions.put("kick", Action.KICK);
        actions.put("hit", Action.ATTACK);
        actions.put("attack", Action.ATTACK);
        actions.put("punch", Action.ATTACK);
        actions.put("slap", Action.SLAP);


        // Special actions
        actions.put("play", Action.PLAY);
		actions.put("ring", Action.RING);
        actions.put("highfive", Action.HIGH_FIVE);
        actions.put("high five", Action.HIGH_FIVE);
		actions.put("tie", Action.TIE);


        // Assigning action types

        actionTypes.put(Action.QUIT, ActionType.REFLEXIVE);
        actionTypes.put(Action.LOOK, ActionType.REFLEXIVE);
        actionTypes.put(Action.INVENTORY, ActionType.REFLEXIVE);
        actionTypes.put(Action.SHOUT, ActionType.REFLEXIVE);
        actionTypes.put(Action.WAIT, ActionType.REFLEXIVE);
        actionTypes.put(Action.PROFANITY, ActionType.REFLEXIVE);
        actionTypes.put(Action.JUMP, ActionType.REFLEXIVE);

        actionTypes.put(Action.NORTH, ActionType.EXIT);
        actionTypes.put(Action.SOUTH, ActionType.EXIT);
        actionTypes.put(Action.EAST, ActionType.EXIT);
        actionTypes.put(Action.WEST, ActionType.EXIT);
        actionTypes.put(Action.NORTHEAST, ActionType.EXIT);
        actionTypes.put(Action.NORTHWEST, ActionType.EXIT);
        actionTypes.put(Action.SOUTHEAST, ActionType.EXIT);
        actionTypes.put(Action.SOUTHWEST, ActionType.EXIT);
        actionTypes.put(Action.UP, ActionType.EXIT);
        actionTypes.put(Action.DOWN, ActionType.EXIT);

        actionTypes.put(Action.TAKE, ActionType.TAKE_DROP);
        actionTypes.put(Action.DROP, ActionType.TAKE_DROP);
        actionTypes.put(Action.STORE, ActionType.TAKE_DROP);
        actionTypes.put(Action.PLACE, ActionType.TAKE_DROP);

        actionTypes.put(Action.OPEN, ActionType.OPEN_CLOSE);
        actionTypes.put(Action.CLOSE, ActionType.OPEN_CLOSE);

        actionTypes.put(Action.READ, ActionType.DIRECT);
        actionTypes.put(Action.SLAP, ActionType.DIRECT);
        actionTypes.put(Action.KICK, ActionType.DIRECT);
        actionTypes.put(Action.PLAY, ActionType.DIRECT);
        actionTypes.put(Action.RING, ActionType.DIRECT);
        actionTypes.put(Action.HIGH_FIVE, ActionType.DIRECT);
        actionTypes.put(Action.TIE, ActionType.DIRECT);

        actionTypes.put(Action.UNLOCK, ActionType.INDIRECT);
        actionTypes.put(Action.LOCK, ActionType.INDIRECT);
        actionTypes.put(Action.ATTACK, ActionType.INDIRECT);

	}

	private static void fillDictionary()
	{
		for (int i = 0; i < GameStrings.GAME_WORDS.length; ++i)
		{
			dictionary.add(GameStrings.GAME_WORDS[i]);
		}
	}

    private static void fillObjectStringList(GameState state)
    {
        for (Feature f : state.featureList.values())
        {
            objectStrings.add(f.name);
        }

        for (Item i : state.itemList.values())
        {
            objectStrings.add(i.name);
        }
        for (Actor a : state.actorList.values())
        {
            objectStrings.add(a.name);
        }

        for (Container c : state.containerList.values())
        {
            objectStrings.add(c.name);
        }
    }

    private static void fillCurrentObjectList(GameState state)
    {
        for (Feature f : state.featureList.values())
        {
            if (f.location == state.playerLocation)
                currentObjects.put(f.name, f.type);
        }

        for (Item it : state.itemList.values())
        {
            if (it.getLocation() == state.playerLocation ||
                it.getLocation() == Location.PLAYER_INVENTORY)
                currentObjects.put(it.name, it.type);
        }

        for (Actor a : state.actorList.values())
        {
            if (a.location == state.playerLocation)
                currentObjects.put(a.name, a.type);
        }

        for (Container c : state.containerList.values())
        {
            if (c.location == state.playerLocation)
            {
                currentObjects.put(c.name, c.type);

                if (c.isOpen())
                {
                    for (Item it : c.inventory)
                        currentObjects.put(it.name, it.type);
                }
            }
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