import java.util.ArrayList;
import java.util.HashMap;

class GameState {

	// gameplay information
	public int turns;
    public int darknessTurns;
    public boolean lightActivated;

	// game events
	public boolean houseWindowOpened;
	public boolean carpetMoved;

	// player attributes
	public Location playerLocation;
	public Location playerPreviousLocation;
	public int playerHitPoints;
	public int playerScore;
	public int playerCarryWeight;
	public int playerMaxCarryWeight;
	public boolean playerAlive;

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




	// constructor
	public GameState()
	{
		dummyObject = new Feature("dummy_feature", Location.NULL_LOCATION);

		turns = 0;
		playerLocation = Location.NULL_LOCATION;
		playerPreviousLocation = Location.NULL_LOCATION;
		lightActivated = false;
		playerAlive = true;
		playerCarryWeight = 0;
		playerScore = 0;
		playerMaxCarryWeight = Game.CARRY_WEIGHT_LIMIT;
		completePlayerInput = "";
		playerPreviousInput = "";

		
		resetInput();

		worldMap = new HashMap<Location, Room>();
		dictionary = new ArrayList<String>();
		gameNouns = new ArrayList<String>();
		objectList = new HashMap<String, GameObject>();
		currentObjects = new HashMap<String, GameObject>();
		actions = new HashMap<String, Action>();
		actionTypes = new HashMap<Action, ActionType>();
	}



	public void setPlayerLocation(Location loc) { playerLocation = loc; }
	public Location getPlayerLocation() { return playerLocation; }

	public void setPreviousLocation(Location loc) { playerPreviousLocation = loc; }
	public Location getPreviousLocation() { return playerPreviousLocation; }

	public void setPlayerAction(Action act) { playerAction = act; }
	public Action getPlayerAction() { return playerAction; }



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


	public void refreshInventories()
	{
		for (GameObject container : objectList.values())
		{
			if (container.inventoryID != Location.NULL_INVENTORY)
			{
				container.inventory.clear();

				for (GameObject item : objectList.values())
				{
					if (item.isItem() && item.location == container.inventoryID)
					{
						Item it = (Item)(item);
						container.inventory.add(it);
					}
				}
			}
		}
	}




	// It should not be possible to alter the number of turns except by adding 1.
	public void addTurn() { ++turns; }
	public int getTurns() { return turns; }


	/**
	 * Game Score
	 * 
	 * Kitchen 10
	 * Cellar 25
	 * East-West Passage 5
	 * 
	 * 
	 * 272 points from treasures.
	 *
	 * 10 Egg 5 in case 5
	 * 15 Platinum bar 10 in case 5
	 * 15 Trident 4 in case 11
	 * 20 Torch 14 in case 6
	 * 10 Sceptre 4 in case 6
	 * 25 Coffin 10 in case 15
	 * 20 Pot of gold 10 in case 10
	 * 10 Scarab 5 in case 5
	 * 20 Skull 10 in case 10
	 * 10 Jade figurine 5 in case 5
	 * 10 Sapphire bracelet 5 in case 5
	 * 20 Diamond 10 in case 10
	 * 15 Emerald (opening buoy) 5 in case 10
	 * 20 Trunk of jewels 15 in case 5
	 * 15 Bag of coins 10 in case 5
	 * 15 Silver chalice 10 in case 5
	 * 10 Canary 6 in case 4
	 * 2  Bauble 1 in case 1
	 * 10 Painting 4 in case 6
	 * Broken canary 0 in case 1
	 */





}