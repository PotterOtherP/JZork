import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GameState {

	// gameplay information
	public int turns;
    public int darknessTurns;
    public boolean darkness;
    public boolean lightActivated;
    public boolean playerStaggered;
	public boolean playerDead;
    public int playerDeaths;
    public int suicideCount;
    public Verbosity verbosity;

	// game events
	public boolean houseWindowOpened;
	public boolean carpetMoved;
	public boolean leafPileMoved;
	public boolean mirrorBroken;
	public boolean rainbowSolid;
	public boolean potOfGoldAppeared;

	// player attributes
	public Location playerLocation;
	public Location playerPreviousLocation;
	public int playerHitPoints;
	public int playerScore;
	public int playerCarryWeight;
	public int playerMaxCarryWeight;

	// player action
	public String completePlayerInput;
	public String playerPreviousInput;
	public String firstInputPhrase;
	public String secondInputPhrase;
	public String thirdInputPhrase;
	public ActionType playerActionType;
	public Action playerAction;
	public GameObject directObject;
	public GameObject indirectObject;
	public Feature dummyObject;

	// lists of game objects
	public HashMap<Location, Room> worldMap;
	public HashMap<String, GameObject> objectList;
	public HashMap<String, GameObject> currentObjects;
	public HashMap<String, Action> actions;
	public HashMap<Action, ActionType> actionTypes;
	public ArrayList<String> dictionary;
	public ArrayList<String> gameNouns;

	// Constants
	public static final int MAX_PLAYER_DEATHS = 3;
    public static final int MAX_DARKNESS_TURNS = 2;
    public static final int MAX_HIT_POINTS = 10;

	public GameState()
	{
		dummyObject = new Feature("dummy_feature", Location.NULL_LOCATION);

		turns = 0;
		darknessTurns = 0;
		playerLocation = Location.NULL_LOCATION;
		playerPreviousLocation = Location.NULL_LOCATION;
		lightActivated = false;
		playerDead = false;
		playerCarryWeight = 0;
		playerDeaths = 0;
		playerScore = 0;
        playerHitPoints = MAX_HIT_POINTS;
		playerMaxCarryWeight = Game.CARRY_WEIGHT_LIMIT;
		completePlayerInput = "";
		playerPreviousInput = "";

		houseWindowOpened = false;
		carpetMoved = false;
		leafPileMoved = false;
		potOfGoldAppeared = false;
		rainbowSolid = false;
		verbosity = Verbosity.BRIEF;

		
		resetInput();

		worldMap = new HashMap<Location, Room>();
		dictionary = new ArrayList<String>();
		gameNouns = new ArrayList<String>();
		objectList = new HashMap<String, GameObject>();
		currentObjects = new HashMap<String, GameObject>();
		actions = new HashMap<String, Action>();
		actionTypes = new HashMap<Action, ActionType>();
	}


	public void addTurn() { ++turns; }

    public void calculateScore()
    {
        
    }

    public void darknessCheck()
    {
        lightActivated = false;

        Item lightSource1 = (Item)(objectList.get("brass lantern"));
        Item lightSource2 = (Item)(objectList.get("torch"));
        Item lightSource3 = (Item)(objectList.get("pair of candles"));
        Item lightSource4 = (Item)(objectList.get("matchbook"));

        Item[] lightSources = { lightSource1, lightSource2, lightSource3, lightSource4 };

        for (Item source : lightSources)
        {
            if ((source.location == Location.PLAYER_INVENTORY || source.location == playerLocation) && source.activated)
                lightActivated = true;
        }

        Room currentRoom = worldMap.get(playerLocation);

        darkness = (currentRoom.isDark() && !lightActivated);

        if (!darkness) darknessTurns = 0;
    }

	public void fillCurrentObjectList()
    {
        currentObjects.clear();

        // Item self = (Item)objectList.get("you");
        // currentObjects.put(self.name, self);

        for (GameObject g : objectList.values())
        {
            if (g.location == playerLocation ||
                g.altLocations.contains(playerLocation) ||
                g.playerHasObject())
            {
                currentObjects.put(g.name, g);
                
                for (String str : g.altNames)
                    currentObjects.put(str, g);              
            }

            // Items in an open container that is present in the room
            if ( (g.location == playerLocation || g.playerHasObject())
                 && g.isContainer() && g.isOpen())
            {
                for (Item it : g.inventory)
                {
                    currentObjects.put(it.name, it);

                    for (String str : it.altNames)
                        currentObjects.put(str, it);
                }
            }

            // Items on a surface
            if (g.location == playerLocation && g.isSurface())
            {
                for (Item it : g.inventory)
                {
                    currentObjects.put(it.name, it);

                    for (String str : it.altNames)
                        currentObjects.put(str, it);
                }
            }

            
        }
    }

    public void playerDies()
    {
    	if (Game.godmode) return;

    	/*
    	 * Items get randomly distributed in the overworld for any kind of death.
    	 * The brass lantern always goes back in the living room. 
    	 */
		Location[] overworld = { Location.WEST_OF_HOUSE, Location.NORTH_OF_HOUSE, Location.BEHIND_HOUSE,
    	Location.SOUTH_OF_HOUSE, Location.FOREST_PATH, Location.FOREST_WEST, Location.FOREST_EAST,
    	Location.FOREST_NORTHEAST, Location.FOREST_SOUTH, Location.CLEARING_NORTH, Location.CLEARING_EAST,
    	Location.CANYON_VIEW, Location.ROCKY_LEDGE, Location.CANYON_BOTTOM };

    	Location[] forest = { Location.FOREST_WEST, Location.FOREST_EAST,
    	Location.FOREST_NORTHEAST, Location.FOREST_SOUTH };

    	++playerDeaths;

    	Random rnd = new Random();
    	for (GameObject g : objectList.values())
    	{
    		if (g.location == Location.PLAYER_INVENTORY)
    		{

    			int i = rnd.nextInt(overworld.length);
   				g.location = overworld[i];
   			}

            // The lantern should be returned to the living room even if the player dropped it
   			if (g.name.equals("brass lantern"))
   				g.location = g.startLocation;
   		}

    	if (playerDeaths % MAX_PLAYER_DEATHS == 0)
    	{
    		playerDeaths = 0;
    		playerDiesForReal();
    	}

    	else
    	{
	    	Game.output(GameStrings.PLAYER_DIES);
    		int p = rnd.nextInt(forest.length);

    		playerPreviousLocation = playerLocation;
    		playerLocation = forest[p];
            playerHitPoints = MAX_HIT_POINTS;
    		Room path = worldMap.get(playerLocation);
    		path.lookAround(this);
    	}
    }

    public void playerDiesForReal()
    {
    	if (Game.godmode) return;

    	playerDead = true;

    	Game.output(GameStrings.PLAYER_DIES_FOR_REAL);
    	playerPreviousLocation = playerLocation;
    	playerLocation = Location.ENTRANCE_TO_HADES;
    	Room r = worldMap.get(Location.ENTRANCE_TO_HADES);
        Game.outputLine();
        Game.output(r.name);
        Game.outputLine();
    	Game.output(GameStrings.DEAD_LOOK);
    	Game.output(r.description);
    }

	public void refreshInventories()
	{
		for (GameObject container : objectList.values())
		{
			if (container.inventoryID != Location.NULL_INVENTORY)
			{
				container.inventory.clear();

				for (GameObject item : objectList.values())
				{
					if (item.location == container.inventoryID)
					{
						Item it = (Item)(item);
						container.inventory.add(it);
					}
				}
			}
		}
	}

	public void resetInput()
	{
		if (!completePlayerInput.equals("again") && !completePlayerInput.equals("g"))
		{
			playerPreviousInput = completePlayerInput;
		}

		firstInputPhrase = "";
		secondInputPhrase = "";
		thirdInputPhrase = "";
		completePlayerInput = "";

		playerAction = Action.NULL_ACTION;
		playerActionType = ActionType.NULL_TYPE;
		directObject = dummyObject;
		indirectObject = dummyObject;
	}

	public void updateGame()
	{
		refreshInventories();
		Room currentRoom = worldMap.get(playerLocation);

		if (playerDead)
		{
			updateDeath();
			return;
		}

        darknessCheck();

		if (darkness)
		{
			updateDarkness();
			return;
		}


		switch (playerAction)
		{
			/* ACTION ON AN OBJECT */
			case ANSWER: { directObject.answer(this); } break;
            case BLOW: { directObject.blow(this); } break;
            case BREAK: { directObject.breakObject(this); } break;
            case CLIMB: {directObject.climb(this); } break;
            case CLOSE: {directObject.close(this); } break;
            case COUNT: { directObject.count(this); } break;
            case CROSS: { directObject.cross(this); } break;
            case DEFLATE: { directObject.deflate(this); } break;
            case DRINK: { directObject.drink(this); } break;
            case DROP: {directObject.drop(this); } break;
            case EAT: { directObject.eat(this); } break;
            case ENTER: { directObject.enter(this); } break;
            case EXAMINE: { directObject.examine(this); } break;
            case EXTINGUISH: { directObject.extinguish(this); } break;
            case FOLLOW: { directObject.follow(this); } break;
            case KICK: { directObject.kick(this); } break;
            case KNOCK: { directObject.knock(this); } break;
            case LIGHT: { directObject.light(this); } break;
            case LISTEN: { directObject.listen(this); } break;
            case LOCK: {directObject.lock(this); } break;
            case LOOK_IN: {directObject.lookIn(this); } break;
            case LOOK_OUT: {directObject.lookOut(this); } break;
            case LOOK_UNDER: {directObject.lookUnder(this); } break;
            case MOVE_OBJECT: { directObject.move(this); } break;
            case LOWER: { directObject.lower(this); } break;
            case OPEN: {directObject.open(this); } break;
            case POUR: { directObject.pour(this); } break;
            case PULL: { directObject.pull(this); } break;
            case PUT: { directObject.put(this); } break;
            case PUSH: { directObject.push(this); } break;
            case RAISE: { directObject.raise(this); } break;
            case READ: { directObject.read(this); } break;
            case SEARCH: { directObject.search(this); } break;
            case SHAKE: { directObject.shake(this); } break;
            case SMELL: { directObject.smell(this); } break;
            case TAKE: {directObject.take(this); } break;
            case TALK_TO: { directObject.talk(this); } break;
            case TOUCH: { directObject.touch(this); } break;
            case TURN: { directObject.turn(this); } break;
            case UNLOCK: {directObject.unlock(this); } break;
            case WAKE: { directObject.wake(this); } break;
            case WAVE: { directObject.wave(this); } break;
            case WEAR: { directObject.wear(this); } break;
            case WIND: { directObject.wind(this); } break;

            /* REFLEXIVE GAME ACTIONS */
            case INVENTORY:
            {
            	int count = 0;
				for (GameObject item : objectList.values())
				{
                    
					if (item.location == Location.PLAYER_INVENTORY)
                    {
                        ++count;
                        if (count == 1)
                            Game.output("You are carrying: \n");
						Game.output(item.capArticleName);
                    }

                    if (item.location == Location.PLAYER_INVENTORY && item.isContainer()
                    	&& (item.isOpen() || item.name.equals("glass bottle")) )
                    {
                        if (!item.inventory.isEmpty())
                        {
                            boolean check = false;

                            for (Item it : item.inventory)
                            {
                                if (!it.initialPresenceString.isEmpty() && !it.movedFromStart)
                                {
                                    Game.output(it.initialPresenceString);
                                    check = true;
                                }
                            }

                            if (!check)
                            {
                                Game.output("The " + item.name + " contains:");
                                for (Item it : item.inventory)
                                    Game.output(it.capArticleName);
                            }
                        }
                    }
				}
                if (count == 0)
                    Game.output("You are empty-handed.");
            } break;

            case JUMP:
            {
            	if (currentRoom.height)
            	{
            		Game.output(currentRoom.jumpString);
            		playerDies();
            	}

            	else
            		Game.output(GameStrings.getJumpSarcasm());
            } break;

            case LOOK:
            {
            	currentRoom.lookAround(this);
            } break;

            case SHOUT: { Game.output("Yaaaaarrrrggghhh!"); } break;

            case WAIT:
            {
            	Game.output("Time passes...");
            } break;

            /* EXIT ACTIONS */
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
				
				if (currentRoom.exit(this, playerAction))
				{
					Room nextRoom = worldMap.get(playerLocation);
					Game.output(nextRoom.name);

                    darknessCheck();

                    if (nextRoom.isDark() && !lightActivated)
                    {
                        Game.output(GameStrings.ENTER_DARKNESS);
                        // return;
                    }

                    switch(verbosity)
                    {
                        case SUPERBRIEF:
                        {
                            nextRoom.getRoomObjects(this);
            
                        } break;

                        case BRIEF:
                        {
                            if (nextRoom.firstVisit)
                            {
                                Game.outputLine();
                                nextRoom.getDescription(this);
                            }
                            
                            nextRoom.getRoomObjects(this);

                        } break;

                        case VERBOSE:
                        {
                            Game.outputLine();
                            nextRoom.getDescription(this);
                            nextRoom.getRoomObjects(this);
                        } break;

                        default: {} break;
                    }

					if (nextRoom.firstVisit)
						nextRoom.firstVisit = false;

				}

			} break; 
			
			/*
			case IN:
            case OUT:
            {

            } break;
            */

            /* UI/UTILITY ACTIONS */
            case BRIEF:
            {
            	Game.output("Brief verbosity on.");
            	verbosity = Verbosity.BRIEF;
            } break;

            case DIAGNOSE:
            {
                Game.output("You have " + playerHitPoints + "/" + MAX_HIT_POINTS + " hit points.");
            } break;

            case QUIT:
            {
            	Game.gameover = true;
            } break;

            case SCORE:
            {

            } break;

            case SUPERBRIEF:
            {
            	Game.output("Superbrief verbosity on.");
            	verbosity = Verbosity.SUPERBRIEF;
            } break;

            case VERBOSE:
            {
            	Game.output("Maximum verbosity on.");
            	verbosity = Verbosity.VERBOSE;
            } break;


            case NULL_ACTION: {} break;
            default: {} break;
		}

		// The player's action could end the game before anything else happens.
        if (Game.gameover) return;

        for (GameObject g : objectList.values())
        {   
            if (g.isItem())
            {
                Item it = (Item)(g);
                if (it.activated && it.lifespan > 0)
                {
                    it.tick();
                    if (it.lifespan <= 0)
                        it.activated = false;
                    
                }
            }
        }

        // The actors get to take their turns
        updateActors();

		addTurn();

        if (playerHitPoints <= 0)
            playerDies();


	}

    public void updateActors()
    {
        Actor cyclops = (Actor)(objectList.get("cyclops"));
        Actor flood = (Actor)(objectList.get("flood"));
        Actor gustOfWind = (Actor)(objectList.get("gust of wind"));
        Actor riverCurrent = (Actor)(objectList.get("current"));
        Actor songbird = (Actor)(objectList.get("songbird"));
        Actor spirits = (Actor)(objectList.get("spirits"));
        Actor thief = (Actor)(objectList.get("thief"));
        Actor troll = (Actor)(objectList.get("troll"));
        Actor vampireBat = (Actor)(objectList.get("vampire bat"));
        cyclops.cyclopsTurn(this);
        flood.floodTurn(this);
        gustOfWind.gustOfWindTurn(this);
        riverCurrent.riverCurrentTurn(this);
        songbird.songbirdTurn(this);
        spirits.spiritsTurn(this);
        thief.thiefTurn(this);
        troll.trollTurn(this);
        vampireBat.vampireBatTurn(this);
    }

	public void updateDarkness()
	{
        Room currentRoom = worldMap.get(playerLocation);

        if (darknessTurns > MAX_DARKNESS_TURNS)
        {
            Game.output(GameStrings.GRUE_DEATH_2);
            playerDies();
            return;
        }

        switch (playerAction)
        {
            case DROP:
            {
                directObject.drop(this);
                ++darknessTurns;
            } break;

            case INVENTORY:
            {
                int count = 0;
                for (GameObject item : objectList.values())
                {
                    
                    if (item.location == Location.PLAYER_INVENTORY)
                    {
                        ++count;
                        if (count == 1)
                            Game.output("You are carrying: \n");
                        Game.output(item.capArticleName);
                    }

                    if (item.location == Location.PLAYER_INVENTORY && item.isContainer()
                        && (item.isOpen() || item.name.equals("glass bottle")) )
                    {
                        if (!item.inventory.isEmpty())
                        {
                            boolean check = false;

                            for (Item it : item.inventory)
                            {
                                if (!it.initialPresenceString.isEmpty() && !it.movedFromStart)
                                {
                                    Game.output(it.initialPresenceString);
                                    check = true;
                                }
                            }

                            if (!check)
                            {
                                Game.output("The " + item.name + " contains:");
                                for (Item it : item.inventory)
                                    Game.output(it.capArticleName);
                            }
                        }
                    }
                }
                if (count == 0)
                    Game.output("You are empty-handed.");
            } break;

            case JUMP:
            {
                Game.output(GameStrings.getJumpSarcasm());
                ++darknessTurns;
            } break;

            case LIGHT:
            {
                directObject.light(this);
                ++darknessTurns;
            } break;

            case LISTEN:
            {
                Game.output(GameStrings.DARKNESS_LISTEN);
                ++darknessTurns;
            } break;

            case LOOK:
            {
                Game.output(GameStrings.DARKNESS);
                ++darknessTurns;
            } break;

            case SHOUT:
            {
                Game.output("Yaaaaarrrrggghhh!");
                ++darknessTurns;
            } break;

            case WAIT:
            {
                Game.output("Time passes...");
                ++darknessTurns;
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
                if (currentRoom.exit(this, playerAction))
                {
                    Room nextRoom = worldMap.get(playerLocation);
                    Game.output(nextRoom.name);

                    darknessCheck();

                    if (darkness)
                    {
                        Game.output(GameStrings.ENTER_DARKNESS);
                        // return;
                    }

                    switch(verbosity)
                    {
                        case SUPERBRIEF:
                        {
                            nextRoom.getRoomObjects(this);
            
                        } break;

                        case BRIEF:
                        {
                            if (nextRoom.firstVisit)
                            {
                                Game.outputLine();
                                nextRoom.getDescription(this);
                            }
                            
                            nextRoom.getRoomObjects(this);

                        } break;

                        case VERBOSE:
                        {
                            Game.outputLine();
                            nextRoom.getDescription(this);
                            nextRoom.getRoomObjects(this);
                        } break;

                        default: {} break;
                    }

                    if (nextRoom.firstVisit)
                        nextRoom.firstVisit = false;

                }


            } break;

            case BRIEF:
            {
                Game.output("Brief verbosity on.");
                verbosity = Verbosity.BRIEF;
            } break;

            case DIAGNOSE:
            {

            } break;

            case QUIT:
            {
                Game.gameover = true;
            } break;

            case SCORE:
            {

            } break;

            case SUPERBRIEF:
            {
                Game.output("Superbrief verbosity on.");
                verbosity = Verbosity.SUPERBRIEF;
            } break;

            case VERBOSE:
            {
                Game.output("Maximum verbosity on.");
                verbosity = Verbosity.VERBOSE;
            } break;


            case NULL_ACTION: {} break;
            default:
            {
                Game.output("It's too dark to see!");
                ++darknessTurns;
            } break;

        }

        updateActors();

	}

	public void updateDeath()
	{
        Room currentRoom = worldMap.get(playerLocation);

        switch (playerAction)
        {
            case INVENTORY:
            {
                Game.output(GameStrings.DEAD_INVENTORY);
            } break;

            case LOOK:
            {
                currentRoom.lookAround(this);

            } break;

            case PRAY:
            {
                if (playerLocation == Location.ALTAR)
                {
                    Game.output(GameStrings.DEAD_PRAY_ALTAR);
                    playerPreviousLocation = playerLocation;
                    playerLocation = Location.FOREST_WEST;
                    playerDead = false;
                    playerHitPoints = 1;
                }

                else
                    Game.output(GameStrings.DEAD_PRAY_FAIL);
            } break;

            case TAKE:
            {
                Game.output(GameStrings.DEAD_TAKE_OBJECT);
            } break;

            case TOUCH:
            {
                Game.output(GameStrings.DEAD_TOUCH);
            } break;

            case WAIT:
            {
                Game.output(GameStrings.DEAD_WAIT);
            } break;

            case BRIEF:
            {
                Game.output("Brief verbosity on.");
                verbosity = Verbosity.BRIEF;
            } break;

            case DIAGNOSE:
            {
                Game.output(GameStrings.DEAD_DIAGNOSE);
            } break;

            case QUIT:
            {
                Game.gameover = true;
            } break;

            case SCORE:
            {
                Game.output(GameStrings.DEAD_SCORE);
            } break;

            case SUPERBRIEF:
            {
                Game.output("Superbrief verbosity on.");
                verbosity = Verbosity.SUPERBRIEF;
            } break;

            case VERBOSE:
            {
                Game.output("Maximum verbosity on.");
                verbosity = Verbosity.VERBOSE;
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
                if (playerLocation == Location.TIMBER_ROOM && playerAction == Action.WEST)
                {
                    Game.output(GameStrings.DEAD_CANNOT_ENTER);
                    return;
                }

                if (playerLocation == Location.STUDIO && playerAction == Action.UP)
                {
                    Game.output(GameStrings.DEAD_CANNOT_ENTER);
                    return;
                }

                if (playerLocation == Location.SLIDE_ROOM && playerAction == Action.DOWN)
                {
                    Game.output(GameStrings.DEAD_CANNOT_ENTER);
                    return;
                }
                
                if (currentRoom.exit(this, playerAction))
                {
                    Room nextRoom = worldMap.get(playerLocation);
                    Game.output(nextRoom.name);

                    if (playerLocation == Location.DOME_ROOM)
                    {
                        Game.outputLine();
                        Game.output(GameStrings.DEAD_DOME_PASSAGE);
                        playerLocation = Location.TORCH_ROOM;
                        Room rm = worldMap.get(Location.TORCH_ROOM);
                        Game.outputLine();
                        rm.lookAround(this);
                        return;
                    }

                    switch(verbosity)
                    {
                        case SUPERBRIEF:
                        {
                            nextRoom.getRoomObjects(this);
            
                        } break;

                        case BRIEF:
                        {
                            if (nextRoom.firstVisit)
                            {
                                Game.outputLine();
                                nextRoom.getDescription(this);
                            }
                            
                            nextRoom.getRoomObjects(this);

                        } break;

                        case VERBOSE:
                        {
                            Game.outputLine();
                            nextRoom.getDescription(this);
                            nextRoom.getRoomObjects(this);
                        } break;

                        default: {} break;
                    }

                    if (nextRoom.firstVisit)
                        nextRoom.firstVisit = false;

                }

            } break; 


            case NULL_ACTION: {} break;
            default:
            {
                Game.output(GameStrings.DEAD_ACTION_FAIL);
            }
        }
	}








}