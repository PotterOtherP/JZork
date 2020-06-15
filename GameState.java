import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GameState {

	// gameplay information
	public int turns;
    public int darknessTurns;
    public boolean darkness;
    public boolean lightActivated;
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

	public void fillCurrentObjectList()
    {
        currentObjects.clear();

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
    	Game.outputLine();
    	Game.output(GameStrings.PLAYER_DIES);

    	Random rnd = new Random();
    	for (GameObject g : objectList.values())
    	{
    		if (g.location == Location.PLAYER_INVENTORY)
    		{

    			int i = rnd.nextInt(overworld.length);
   				g.location = overworld[i];
   				if (g.name.equals("brass lantern"))
   					g.location = Location.LIVING_ROOM;
   			}
   		}

    	if (playerDeaths % MAX_PLAYER_DEATHS == 0)
    	{
    		playerDeaths = 0;
    		playerDiesForReal();
    	}

    	else
    	{
    		int p = rnd.nextInt(forest.length);

    		playerPreviousLocation = playerLocation;
    		playerLocation = forest[p];
    		Room path = worldMap.get(playerLocation);
    		path.lookAround(this);
    	}
    }

    public void playerDiesForReal()
    {
    	if (Game.godmode) return;

    	playerDead = true;

    	Game.outputLine();
    	Game.output("You really died this time.");
    	playerPreviousLocation = playerLocation;
    	playerLocation = Location.ENTRANCE_TO_HADES;
    	Room r = worldMap.get(Location.ENTRANCE_TO_HADES);
    	r.lookAround(this);
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
		if (playerDead)
		{
			updateDeath();
			return;
		}

		if (darkness)
		{
			updateDarkness();
			return;
		}

		switch (playerAction)
		{
			
		}


	}

	public void updateDarkness()
	{

	}

	public void updateDeath()
	{

	}








}