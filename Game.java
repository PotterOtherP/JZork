import java.util.Scanner;

/**
 * This program is my attempt to recreate Zork I as well as possible.
 *
 * @author Nathan Tryon January 2020 - 
 */
public final class Game {

    // Global variables
	public static boolean gameover = true;
	public static boolean godmode = true;
    public static boolean debug = false;


    // Constants
    public static final int LINE_LENGTH = 50;
	public static final Location STARTING_LOCATION = Location.WEST_OF_HOUSE;
    public static final int LANTERN_LIFESPAN = 100;
    public static final int CARRY_WEIGHT_LIMIT = 20;
    public static final int SONGBIRD_CHIRP_PERCENT = 31;



	public static void main(String[] args)
	{

        if (args.length > 0 && args[0].equals("debug"))
        {
            debug = true;
            output("Testing");
        }

        if (args.length > 0 && args[0].equals("godmode"))
        {
            godmode = true;
        }


		GameState gameState = new GameState();
        InputParser parser = new InputParser(gameState);

		String playerText = "";
	
		initGame(gameState);

		gameover = false;

		while (!gameover)
		{	
            outputLine();
            gameState.resetInput();
			gameState.completePlayerInput = getPlayerText();

			if (parsePlayerInput(gameState, playerText))
            {
                if (validateAction(gameState))
                    updateGame(gameState);
            }

            if (debug) parser.inputTest();
            
            outputLine();
		}

		endGame(gameState);
		
	}




	public static void initGame(GameState state)
	{	
		new GameSetup(state, godmode, debug);

		// Put the player in the starting location
		state.setPlayerLocation(Location.WEST_OF_HOUSE);
		state.worldMap.get(Location.WEST_OF_HOUSE).firstVisit = false;

		// Beginning text of the game.
        outputLine();
        output(state.worldMap.get(Location.WEST_OF_HOUSE).name);
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
		for (String token : state.actions.keySet())
		{
			
			if (startsWith(token, playerText))
			{
                state.firstInputPhrase = token;
                
			}
		}


        // If not, exit		
		if (state.firstInputPhrase.isEmpty())
		{
			output("Sentence did not start with an action.");
            return false;
		}

		
        // Remove the first phrase and check if there is more text.
        // The next token should be an object.
		playerText = playerText.substring(state.firstInputPhrase.length()).trim();
		if (playerText.isEmpty()) return true;

        for (String token : state.objectList.keySet())
        {
            if (startsWith(token, playerText))
            {
                state.secondInputPhrase = token;
            }
        }

        // If the user entered something known by the game but is not a valid object.
        if (state.secondInputPhrase.isEmpty())
        {
            output("Second phrase was not an object.");
            return false;
        }

        // Remove the second phrase and check if there is more text.
        // The next token should also be an ojbect.
        playerText = playerText.substring(state.secondInputPhrase.length()).trim();
        if (playerText.isEmpty()) return true;

        for (String token : state.objectList.keySet())
        {
            if (startsWith(token, playerText))
            {
                state.thirdInputPhrase = token;
            }
        }

        // If the user entered something known by the game but is not a valid object.
        if (state.thirdInputPhrase.isEmpty())
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




		String first = state.firstInputPhrase;
		String second = state.secondInputPhrase;
		String third = state.thirdInputPhrase;

        state.playerAction = state.actions.get(first);
        state.playerActionType = state.actionTypes.get(state.playerAction);


        if (state.playerAction == Action.QUIT)
        {
            return true;
        }

        // FIRST SWITCH
        // If the player entered an incomplete phrase, we probably want to
        // prompt them to complete it before doing any further validation.

        switch(state.playerActionType)
        {
            // Handling the case where the player types stuff after a one-word action.
            // Special cases can go here too.
            case REFLEXIVE:
            {
                if (!second.isEmpty())
                {
                    output("I don't understand what \"" + state.completePlayerInput + "\" means.");
                    return false;
                }
            } break;

            case DIRECT:
            {
                // If player entered just "action" with no object
                if (second.isEmpty())
                {
                    output("What do you want to " + first + "?");
                    
                    Scanner scan = new Scanner(System.in);
                    second = getPlayerText();
                    scan.close();

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
		switch(state.playerActionType)
		{

			case REFLEXIVE:
			{

			} break;


			case DIRECT:
            {
                
                if (state.currentObjects.containsKey(second))
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

				if (state.currentObjects.containsKey(second))
                {
                    state.directObject = state.objectList.get(second);
                }

                else
                {
                    output("There's no " + second + " here!");
                    return false;
                }

                if (state.currentObjects.containsKey(third))
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
			output("Action type is " + state.playerActionType);
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

	public static void fillCurrentObjectList(GameState state)
    {
        state.currentObjects.clear();

        for (GameObject g : state.objectList.values())
        {
            if (g.location == state.playerLocation ||
                g.location == Location.PLAYER_INVENTORY)
                state.currentObjects.put(g.name, g.type);

            // Items in an open container that is present in the room
            if (g.location == state.playerLocation && g.isContainer() && g.isOpen())
            {
                for (Item it : g.inventory)
                    state.currentObjects.put(it.name, it.type);
            }

            // Features that can exist in multiple locations (e.g. the house)
            if (g.isFeature())
            {
                if (g.altLocations.contains(state.playerLocation))
                    state.currentObjects.put(g.name, g.type);
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
				output("\nI beg your pardon?\n");
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
		return true;
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
		// Save the gamestate

		output("Game has ended.");
		output("Total turns: " + state.turns);

	}



}