import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;

/**
 * This program is my attempt to recreate Zork I as well as possible.
 *
 * @author Nathan Tryon January 2020 - 
 */
public final class Game {

    /* TODO (After break)
     *
     * Item/container/inventory issue
     * Update object class definitions
     * Object creation
     * Carry weight
     * UpdateGame needs to check for light at the end. (multiple light sources)
     * Implement all objects, one by one
     * All text responses - sarcasm, random selection, etc
     * Player death - less harsh than original? Unlimited lives, auto-restore
     * Score and winning
     * Ambiguity
     * Acting on multiple objects, "all except", etc
     * Easter eggs and trivia
     * Save files
     * Transcripts
     * Web/Javascript version
     * AI program... ???
     *
     */

    // Global variables
	public static boolean gameover = true;
	public static boolean godmode = false;
    public static boolean debug = false;


    // Lists and hashmaps
    public static HashMap<String, Action> actions = new HashMap<String, Action>();
	public static HashMap<Action, ActionType> actionTypes = new HashMap<Action, ActionType>();
    public static HashMap<String, ObjectType> currentObjects = new HashMap<String, ObjectType>();
	public static ArrayList<String> dictionary = new ArrayList<String>();



    // Constants
    public static final int LINE_LENGTH = 50;
	public static final Location STARTING_LOCATION = Location.WEST_OF_HOUSE;
    public static final int LANTERN_LIFESPAN = 100;
    public static final int CARRY_WEIGHT_LIMIT = 20;
    public static final int SONGBIRD_CHIRP_PERCENT = 31;



	public static void main(String[] args)
	{

		GameState gameState = new GameState();

        if (args.length > 0 && args[0].equals("debug"))
        {
            debug = true;
            output("Testing");
        }

        if (args.length > 0 && args[0].equals("godmode"))
        {
            godmode = true;
        }


		String playerText = "";
	
		initGame(gameState);

		gameover = false;

		while (!gameover)
		{	
			playerText = getPlayerText();

			if (parsePlayerInput(gameState, playerText))
            {
                if (validateAction(gameState))
                    updateGame(gameState);
            }

            if (debug)
            {
                output("Parse player text results: ");
                output("First phrase is: " + gameState.first);
                output("Second phrase is: " + gameState.second);
                output("Third phrase is: " + gameState.third);
            }

            outputLine();
		}


		endGame(gameState);
		
	}




	public static void initGame(GameState state)
	{	
		// Populate the action lists and the dictionary
		createActions();
        
        GameSetup.createWorldMap(state);
        GameSetup.createGameObjects(state);

		fillDictionary(state);
        
	

		// Object creation complete. Start setting up the game

		// Put the player in the starting location
		state.setPlayerLocation(STARTING_LOCATION);
		state.worldMap.get(STARTING_LOCATION).firstVisit = false;

		// Beginning text of the game.
        outputLine();
        output(state.worldMap.get(STARTING_LOCATION).name);
		output(MapStrings.DESC_WEST_OF_HOUSE);
        outputLine();
		
	}

    


	public static boolean parsePlayerInput(GameState state, String playerText)
	{
        /* ACTION OBJECT OBJECT.

        Not assigning direct or indirect objects in this method, or validating anything else.
        Just checking for 1 to 3 game-recognized phrases. Look for one, remove it, if there is more in the string,
        check for another phrase.

        */
        outputLine();
		state.resetInput();
        state.phrase = playerText;

		
        // Method fails if any word the player typed is not known by the game.
		String[] words = playerText.split(" ");

        // Before regular parsing, handle the godmode cases.

        if (godmode)
        {
            switch (words[0])
            {
                case "teleport":
                {
                    String dest = "";
                    try { dest = playerText.substring(words[0].length() + 1); }
                    catch (StringIndexOutOfBoundsException e) { dest = "noRoom"; }
                    boolean teleported = false;
                    for (Room r : state.worldMap.values())
                    {
                        String name = r.name.toLowerCase();
                        if (dest.equals(name))
                        {
                            outputLine();
                            output(r.name);
                            state.playerLocation = r.roomID;
                            teleported  = true;
                            r.lookAround(state);
                            break;
                        }
                    }

                    if (!teleported) output("Room not found.");
                    return false;
                }

                case "accio":
                {
                    String name = "";

                    try { name = playerText.substring(words[0].length() + 1); }
                    catch (StringIndexOutOfBoundsException e) { name = "noItem"; }
                    Item it = (Item)(state.objectList.get(name));

                    try
                    {
                        it.location = Location.PLAYER_INVENTORY;
                        for (GameObject g : state.objectList.values())
                        {
                            if (g.inventory != null)
                                if (g.inventory.contains(it))
                                    g.inventory.remove(it);
                        }
                        output("You now have the " + name + ".");
                    }
                    catch (NullPointerException e)
                    {
                        output("That item not found.");
                    }

                    return false;
                }

                default:
                {

                } break;
            }          
        }

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
            return false;
		}

		
        // Remove the first phrase and check if there is more text.
        // The next token should be an object.
		playerText = playerText.substring(state.first.length()).trim();
		if (playerText.isEmpty()) return true;

        for (String token : state.objectList.keySet())
        {
            if (startsWith(token, playerText))
            {
                state.second = token;
            }
        }

        // If the user entered something known by the game but is not a valid object.
        if (state.second.isEmpty())
        {
            output("Second phrase was not an object.");
            return false;
        }

        // Remove the second phrase and check if there is more text.
        // The next token should also be an ojbect.
        playerText = playerText.substring(state.second.length()).trim();
        if (playerText.isEmpty()) return true;

        for (String token : state.objectList.keySet())
        {
            if (startsWith(token, playerText))
            {
                state.third = token;
            }
        }

        // If the user entered something known by the game but is not a valid object.
        if (state.third.isEmpty())
        {
            output("Third phrase was not an object.");
            return false;
        }


		return true;

	}

	public static boolean validateAction(GameState state)
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

        /* debug */
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
        state.actionType = actionTypes.get(state.playerAction);


        if (state.playerAction == Action.QUIT)
        {
            return true;
        }

        // FIRST SWITCH
        // If the player entered an incomplete phrase, we probably want to
        // prompt them to complete it before doing any further validation.

        switch(state.actionType)
        {
            // Handling the case where the player types stuff after a one-word action.
            // Special cases can go here too.
            case REFLEXIVE:
            {
                if (!second.isEmpty())
                {
                    output("I don't understand what \"" + state.phrase + "\" means.");
                    return false;
                }
            } break;

            case DIRECT:
            {
                // If player entered just "action" with no object
                if (second.isEmpty())
                {
                    output("What do you want to " + first + "?");
                    second = getPlayerText();

                    // Player is starting over with a new phrase.
                    if (second.split(" ").length > 1)
                    {
                        if (parsePlayerInput(state, second))
                            return validateAction(state);
                    }

                    if (!state.objectList.containsKey(second))
                    {
                        output("You used the word \"" + second + "\" in a way I don't understand.");
                        return false;
                    }
                }
            } break;

            case INDIRECT:
            {

            } break;

            default:
            {

            } break;

        }

        // SECOND SWITCH
        // At this point we either have a complete phrase or the method has returned.
		switch(state.actionType)
		{

			case REFLEXIVE:
			{

			} break;


			case DIRECT:
            {
                
                if (currentObjects.containsKey(second))
                {
                    state.directObject = state.objectList.get(second);
                }

                else
                {
                    output("There's no " + second + " here!");
                    return false;
                }

                if (state.directObject.isItem() && !state.directObject.playerHasObject() && state.playerAction != Action.TAKE)
                {
                    output("You're not carrying the " + state.directObject.name + ".");
                    return false;
                }
            } break;

            

			case INDIRECT:
			{

				if (currentObjects.containsKey(second))
                {
                    state.directObject = state.objectList.get(second);
                }

                else
                {
                    output("There's no " + second + " here!");
                    return false;
                }

                if (currentObjects.containsKey(third))
                {
                    state.indirectObject = state.objectList.get(third);
                }

                else
                {
                    output("There's no " + third + " here!");
                    return false;
                }

                if (state.directObject.isItem() && !state.directObject.playerHasObject())
                {
                    output("You're not carrying the " + state.directObject.name + ".");
                    return false;
                }

                if (state.indirectObject.isItem() && !state.indirectObject.playerHasObject())
                {
                    output("You're not carrying the " + state.indirectObject.name + ".");
                    return false;
                }
				
			
			} break;


			default:
			{
				// we should never be here
			} break;
		}


		return result;

	}




	public static void updateGame(GameState state)
	{
		
		Location currentLocation = state.playerLocation;
		Room currentRoom = state.worldMap.get(currentLocation);

		Action currentAction = state.playerAction;

        GameObject obj = state.directObject;

		GameObject indObj = state.indirectObject;

        /* debug */
		if (debug)
		{
            output("Current action is " + currentAction);
			output("Action type is " + state.actionType);
			output("Direct object is " + obj.name);
			output("Indirect object is " + indObj.name);
		}
		
        /* If the room is dark, you can't do anything except:
         * Drop items
         * Turn on a light (in your inventory)
         * Move in a direction (resulting in death if not back to a light room)
         * Reflexive actions
         *
         * Three consecutive turns in darkness without moving will result in death.
         * This is a change from the original game, which allows you to do some things
         * that don't really make sense.
         */

        state.lightActivated = false;

        Item lightsrc = (Item)(state.objectList.get("lantern"));
        if (lightsrc.location == Location.PLAYER_INVENTORY && lightsrc.activated)
            state.lightActivated = true;
        
        lightsrc = (Item)(state.objectList.get("torch"));
        if (lightsrc.location == Location.PLAYER_INVENTORY && lightsrc.activated)
            state.lightActivated = true;
        
        lightsrc = (Item)(state.objectList.get("candles"));
        if (lightsrc.location == Location.PLAYER_INVENTORY && lightsrc.activated)
            state.lightActivated = true;


        boolean dark = (currentRoom.isDark() && !state.lightActivated);

        if (!dark) state.darknessTurns = 0;

       


		switch (currentAction)
		{

            case ANSWER: { obj.answer(state); } break;
            case BLOW: { obj.blow(state); } break;
            case CLIMB: {obj.climb(state); } break;
            case COUNT: { obj.count(state); } break;
            case CROSS: { obj.cross(state); } break;
            case DEFLATE: { obj.deflate(state); } break;
            case DRINK: { obj.drink(state); } break;
            case EAT: { obj.eat(state); } break;
            case ENTER: { obj.enter(state); } break;
            case EXAMINE: { obj.examine(state); } break;
            case EXTINGUISH: { obj.extinguish(state); } break;
            case FOLLOW: { obj.follow(state); } break;
            case KICK: { obj.kick(state); } break;
            case KNOCK: { obj.knock(state); } break;
            case LIGHT: { obj.light(state); } break;
            case LISTEN: { obj.listen(state); } break;
            case LOWER: { obj.lower(state); } break;
            case POUR: { obj.pour(state); } break;
            case PULL: { obj.pull(state); } break;
            case PUSH: { obj.push(state); } break;
            case RAISE: { obj.raise(state); } break;
            case READ: { obj.read(state); } break;
            case SEARCH: { obj.search(state); } break;
            case SHAKE: { obj.shake(state); } break;
            case SMELL: { obj.smell(state); } break;
            case TALK_TO: { obj.talk(state); } break;
            case TOUCH: { obj.touch(state); } break;
            case TURN: { obj.turn(state); } break;
            case WAKE: { obj.wake(state); } break;
            case WAVE: { obj.wave(state); } break;
            case WEAR: { obj.wear(state); } break;
            case WIND: { obj.wind(state); } break;







            case MOVE_OBJECT:
            {
                obj.move(state);
            } break;
            
   


            


            // Specific actions involving an object.

    

            case OPEN:
            {
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }
                obj.open(state);
            } break;

            case CLOSE:
            {
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }
                obj.close(state);
            } break;


            case UNLOCK:
            case LOCK:
            {
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }

            } break;



			case TAKE:
			{
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }

                // If the player already has the item
                if (obj.playerHasObject())
                {
                    output("You're already carrying the " + obj.name + "!");
                    return;
                }

                // If the item is in an open container, remove it from the container.

                boolean taken = false;
                for (GameObject cont : state.objectList.values())
                {
                    if (cont.isOpen() && cont.inventory.contains(obj))
                    {
                        cont.remove(state, (Item)obj);
                        taken = true;
                    }
                }

                // Otherwise, it is in the player's location and we just take it.
                if (!taken)
                    obj.take(state);
                	
			} break;

			case DROP:
			{
				obj.drop(state);
			} break;

            case PLACE:
            {
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }

                if (obj.isItem())
                    indObj.place(state, (Item)obj);

            } break;


            // Simpler actions

            case LOOK:
            {
                output(currentRoom.name);
                currentRoom.lookAround(state);

            } break;

			case INVENTORY:
			{
                int count = 0;
				for (GameObject item : state.objectList.values())
				{
                    
					if (item.playerHasObject())
                    {
                        ++count;
                        if (count == 1)
                            output("You are carrying: \n");
						output(item.capArticleName);
                    }

				}


                if (count == 0)
                    output("You are empty-handed.");

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
            case IN:
            case OUT:
			{
				
				if (currentRoom.exit(state, currentAction))
				{

					Room nextRoom = state.worldMap.get(state.playerLocation);

					output(nextRoom.name);

                    if (nextRoom.isDark() && !state.lightActivated)
                            output(GameStrings.ENTER_DARKNESS);

					if (nextRoom.firstVisit)
					{
						nextRoom.firstVisit = false;              
						nextRoom.lookAround(state);
					}
				}

			} break;


			// Simple actions

			case WAIT:
            {
                if (dark)
                {   
                    if (state.darknessTurns >= 2)
                    {
                        output(GameStrings.GRUE_DEATH_2);
                        state.playerAlive = false;
                        break;
                    }
                    ++state.darknessTurns;
                }
                output("Time passes...");
            } break;

			case JUMP:
            {
                if (currentRoom.height)
                {
                    output("Not a good place to try jumping.");
                    state.playerAlive = false;
                    break;
                }

                if (dark)
                {   
                    if (state.darknessTurns >= 2)
                    {
                        output(GameStrings.GRUE_DEATH_2);
                        state.playerAlive = false;
                        break;
                    }
                    ++state.darknessTurns;
                }

                output(GameStrings.getJumpSarcasm());
            } break;

			case SHOUT:
            { 
                if (dark)
                {   
                    if (state.darknessTurns >= 2)
                    {
                        output(GameStrings.GRUE_DEATH_2);
                        state.playerAlive = false;
                        break;
                    }
                    ++state.darknessTurns;
                }
                output("Yaaaaarrrrggghhh!");
            } break;

			case NULL_ACTION: {} break;
			case VERBOSE: { output("You said too many words."); } break;
			case PROFANITY: { output(GameStrings.PROFANITY_ONE); } break;			
			case QUIT: { /* if (verifyQuit()) */ gameover = true; } break;
            case AUTHOR: { output(GameStrings.AUTHOR_INFO); } break;


			default: {} break;
		}

		// The player's action could end the game before anything else happens.

        if (!state.playerAlive) gameover = true;

		if (gameover) return;

        // The actors get to take their turns
        for (GameObject ob : state.objectList.values())
        {
            if (ob.isActor() && ob.isAlive())
            {
                ob.actorTurn();
            }

            if (ob.isItem())
            {
                Item it = (Item)(ob);
                if(it.activated && it.lifespan > 0)
                {
                    it.tick();
                    if (it.lifespan <= 0)
                        it.activated = false;
                }
            }
        }

		state.addTurn();

	}


	// Utility methods used by the other methods in Game.java

	public static void createActions()
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
        actions.put("go up",         Action.UP);
		actions.put("walk up",	     Action.UP);
		actions.put("exit up",	 Action.UP);
		actions.put("u",	         Action.UP);
        actions.put("go u",      Action.UP);
		actions.put("walk u",	     Action.UP);
		actions.put("exit u",	 Action.UP);

		actions.put("down",       Action.DOWN);
        actions.put("go down",    Action.DOWN);
		actions.put("walk down",    Action.DOWN);
		actions.put("exit down",  Action.DOWN);
		actions.put("d",          Action.DOWN);
        actions.put("go d",       Action.DOWN);
		actions.put("walk d",       Action.DOWN);
		actions.put("exit d",     Action.DOWN);

        actions.put("in", Action.IN);
        actions.put("inside", Action.IN);
        actions.put("go in", Action.IN);
        actions.put("out", Action.OUT);
        actions.put("slide", Action.SLIDE);

        // Reflexive actions: no interaction with game objects
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
        actions.put("author", Action.AUTHOR);
        actions.put("pray", Action.PRAY);


        // Direct object interaction actions
        
        actions.put("answer", Action.ANSWER);
        actions.put("blow", Action.BLOW);
        actions.put("climb", Action.CLIMB);
        actions.put("close", Action.CLOSE);
        actions.put("count", Action.COUNT);
        actions.put("cross", Action.CROSS);
        actions.put("deflate", Action.DEFLATE);
        actions.put("drink", Action.DRINK);
        actions.put("drop", Action.DROP);
        actions.put("eat", Action.EAT);
        actions.put("enter", Action.ENTER);
        actions.put("examine", Action.EXAMINE);
        actions.put("look at", Action.EXAMINE);
        actions.put("l at", Action.EXAMINE);
        actions.put("extinguish", Action. EXTINGUISH);
        actions.put("follow", Action.FOLLOW);
        actions.put("kick", Action.KICK);
        actions.put("knock", Action.KNOCK);
        actions.put("light", Action.LIGHT);
        actions.put("listen", Action.LISTEN);
        actions.put("lower", Action.LOWER);
        actions.put("move", Action.MOVE_OBJECT);
        actions.put("open", Action.OPEN);
        actions.put("pour", Action.POUR);
        actions.put("pull", Action.PULL);
        actions.put("push", Action.PUSH);
        actions.put("raise", Action.RAISE);
        actions.put("read", Action.READ);
        actions.put("say", Action.SPEAK);
        actions.put("search", Action.SEARCH);
        actions.put("shake", Action.SHAKE);
        actions.put("smell", Action.SMELL);
        actions.put("stay", Action.STAY);
        actions.put("swim", Action.SWIM);
        actions.put("take", Action.TAKE);
        actions.put("pick up", Action.TAKE);
        actions.put("get", Action.TAKE);
        actions.put("acquire", Action.TAKE);
        actions.put("talk to", Action.TAKE);
        actions.put("touch", Action.TOUCH);
        actions.put("turn", Action.TURN);
        actions.put("wake", Action.WAKE);
        actions.put("walk", Action.WALK);
        actions.put("wave", Action.WAVE);
        actions.put("wear", Action.WEAR);
        actions.put("wind", Action.WIND);



        // Indirect actions
        actions.put("attack", Action.ATTACK);
        actions.put("break", Action.BREAK);
        actions.put("burn", Action.BURN);
        actions.put("cut", Action.CUT);
        actions.put("dig", Action.DIG);
        actions.put("fill", Action.FILL);
        actions.put("inflate", Action.INFLATE);

        actions.put("unlock", Action.UNLOCK);
        actions.put("lock", Action.LOCK);
        actions.put("strike", Action.STRIKE);

        actions.put("give", Action.GIVE);
        actions.put("place", Action.PLACE);
        actions.put("put", Action.PLACE);
        actions.put("put", Action.PUT);
        actions.put("throw", Action.THROW);
        actions.put("tie", Action.TIE);


        // Assigning action types

        actionTypes.put(Action.QUIT, ActionType.REFLEXIVE);
        actionTypes.put(Action.LOOK, ActionType.REFLEXIVE);
        actionTypes.put(Action.INVENTORY, ActionType.REFLEXIVE);
        actionTypes.put(Action.SHOUT, ActionType.REFLEXIVE);
        actionTypes.put(Action.WAIT, ActionType.REFLEXIVE);
        actionTypes.put(Action.PROFANITY, ActionType.REFLEXIVE);
        actionTypes.put(Action.JUMP, ActionType.REFLEXIVE);
        actionTypes.put(Action.AUTHOR, ActionType.REFLEXIVE);
        actionTypes.put(Action.PRAY, ActionType.REFLEXIVE);

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
        actionTypes.put(Action.IN, ActionType.EXIT);
        actionTypes.put(Action.OUT, ActionType.EXIT);

        actionTypes.put(Action.ANSWER, ActionType.DIRECT);
        actionTypes.put(Action.BLOW, ActionType.DIRECT);
        actionTypes.put(Action.CLIMB, ActionType.DIRECT);
        actionTypes.put(Action.CLOSE, ActionType.DIRECT);
        actionTypes.put(Action.COUNT, ActionType.DIRECT);
        actionTypes.put(Action.CROSS, ActionType.DIRECT);
        actionTypes.put(Action.DEFLATE, ActionType.DIRECT);
        actionTypes.put(Action.DRINK, ActionType.DIRECT);
        actionTypes.put(Action.DROP, ActionType.DIRECT);
        actionTypes.put(Action.EAT, ActionType.DIRECT);
        actionTypes.put(Action.ENTER, ActionType.DIRECT);
        actionTypes.put(Action.EXAMINE, ActionType.DIRECT);
        actionTypes.put(Action.EXAMINE, ActionType.DIRECT);
        actionTypes.put(Action.EXAMINE, ActionType.DIRECT);
        actionTypes.put(Action.EXIT, ActionType.DIRECT);
        actionTypes.put(Action.EXTINGUISH, ActionType.DIRECT);
        actionTypes.put(Action.FOLLOW, ActionType.DIRECT);
        actionTypes.put(Action.KICK, ActionType.DIRECT);
        actionTypes.put(Action.KNOCK, ActionType.DIRECT);
        actionTypes.put(Action.LIGHT, ActionType.DIRECT);
        actionTypes.put(Action.LISTEN, ActionType.DIRECT);
        actionTypes.put(Action.LOWER, ActionType.DIRECT);
        actionTypes.put(Action.OPEN, ActionType.DIRECT);
        actionTypes.put(Action.POUR, ActionType.DIRECT);
        actionTypes.put(Action.PULL, ActionType.DIRECT);
        actionTypes.put(Action.PUSH, ActionType.DIRECT);
        actionTypes.put(Action.RAISE, ActionType.DIRECT);
        actionTypes.put(Action.READ, ActionType.DIRECT);
        actionTypes.put(Action.SPEAK, ActionType.DIRECT);
        actionTypes.put(Action.SEARCH, ActionType.DIRECT);
        actionTypes.put(Action.SHAKE, ActionType.DIRECT);
        actionTypes.put(Action.SMELL, ActionType.DIRECT);
        actionTypes.put(Action.STAY, ActionType.DIRECT);
        actionTypes.put(Action.SWIM, ActionType.DIRECT);
        actionTypes.put(Action.TAKE, ActionType.DIRECT);
        actionTypes.put(Action.TALK_TO, ActionType.DIRECT);
        actionTypes.put(Action.TOUCH, ActionType.DIRECT);
        actionTypes.put(Action.TURN, ActionType.DIRECT);
        actionTypes.put(Action.WAKE, ActionType.DIRECT);
        actionTypes.put(Action.WALK, ActionType.DIRECT);
        actionTypes.put(Action.WAVE, ActionType.DIRECT);
        actionTypes.put(Action.WEAR, ActionType.DIRECT);
        actionTypes.put(Action.WIND, ActionType.DIRECT);

        actionTypes.put(Action.ATTACK, ActionType.INDIRECT);
        actionTypes.put(Action.BREAK, ActionType.INDIRECT);
        actionTypes.put(Action.BURN, ActionType.INDIRECT);
        actionTypes.put(Action.CUT, ActionType.INDIRECT);
        actionTypes.put(Action.DIG, ActionType.INDIRECT);
        actionTypes.put(Action.FILL, ActionType.INDIRECT);
        actionTypes.put(Action.INFLATE, ActionType.INDIRECT);
        actionTypes.put(Action.MOVE_OBJECT, ActionType.INDIRECT);
        actionTypes.put(Action.UNLOCK, ActionType.INDIRECT);
        actionTypes.put(Action.LOCK, ActionType.INDIRECT);
        actionTypes.put(Action.STRIKE, ActionType.INDIRECT);

        actionTypes.put(Action.GIVE, ActionType.INDIRECT_INVERSE);
        actionTypes.put(Action.PLACE, ActionType.INDIRECT_INVERSE);
        actionTypes.put(Action.PLACE, ActionType.INDIRECT_INVERSE);
        actionTypes.put(Action.PUT, ActionType.INDIRECT_INVERSE);
        actionTypes.put(Action.THROW, ActionType.INDIRECT_INVERSE);
        actionTypes.put(Action.TIE, ActionType.INDIRECT_INVERSE);
        

	}

	public static void fillDictionary(GameState state)
	{
		for (int i = 0; i < GameStrings.GAME_WORDS.length; ++i)
		{
			dictionary.add(GameStrings.GAME_WORDS[i]);
		}

        for (String name : state.objectList.keySet())
        {
            String[] words = name.split(" ");
            for (int i = 0; i < words.length; ++i)
                dictionary.add(words[i]);

        }

        for (String str : actions.keySet())
        {
            String[] words = str.split(" ");
            for (int i = 0; i < words.length; ++i)
                dictionary.add(words[i]);
        }
	}

    public static void fillCurrentObjectList(GameState state)
    {
        currentObjects.clear();

        for (GameObject g : state.objectList.values())
        {
            if (g.location == state.playerLocation ||
                g.location == Location.PLAYER_INVENTORY)
                currentObjects.put(g.name, g.type);

            // Items in an open container that is present in the room
            if (g.location == state.playerLocation && g.isContainer() && g.isOpen())
            {
                for (Item it : g.inventory)
                    currentObjects.put(it.name, it.type);
            }

            // Features that can exist in multiple locations (e.g. the house)
            if (g.isFeature())
            {
                if (g.altLocations.contains(state.playerLocation))
                    currentObjects.put(g.name, g.type);
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
            if (count > LINE_LENGTH)
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
				output("\nWhat?\n");
				prompt();
			}
		}


		return result.trim().toLowerCase();
	}

	// Checks if "input" starts with "token"
	public static boolean startsWith(String tok, String inp)
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

	public static boolean isGameWord(String str)
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



	public static void endGame(GameState state)
	{
		// Save the gamestate

		output("Game has ended.");
		output("Total turns: " + state.turns);

	}



}