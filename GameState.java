import java.util.HashMap;

class GameState {

	// gameplay information
	public int turns;


	// player attributes
	public Location playerLocation;
	public Location playerPreviousLocation;
	public int playerHitPoints;


	// player action
	public String first;
	public String second;
	public String third;
	public ActionType type;


	public Action playerAction;
	public Feature objectFeature;
	public Actor objectActor;
	public Item objectItem;
	public Item usedItem;
	public String speechText;

	public Feature dummyFeature;
	public Item dummyItem;
	public Actor dummyActor;

	// lists of game objects
	public HashMap<Location, Room> worldMap;
	public HashMap<String, Feature> featureList;
	public HashMap<String, Item> itemList;
	public HashMap<String, Actor> actorList;


	// constructor
	public GameState()
	{
		this.dummyFeature = new Feature();
		this.dummyItem = new Item();
		this.dummyActor = new Actor();
		this.resetInput();

		this.turns = 0;
		this.playerLocation = Location.NULL_LOCATION;
		this.playerPreviousLocation = Location.NULL_LOCATION;
		

		worldMap = new HashMap<Location, Room>();
		featureList = new HashMap<String, Feature>();
		itemList = new HashMap<String, Item>();
		actorList = new HashMap<String, Actor>();
	}



	public void setPlayerLocation(Location loc) { playerLocation = loc; }
	public Location getPlayerLocation() { return playerLocation; }

	public void setPreviousLocation(Location loc) { playerPreviousLocation = loc; }
	public Location getPreviousLocation() { return playerPreviousLocation; }

	public void setPlayerAction(Action act) { playerAction = act; }
	public Action getPlayerAction() { return playerAction; }



	public void resetInput()
	{
		this.first = "";
		this.second = "";
		this.third = "";

		this.playerAction = Action.NULL_ACTION;
		this.type = ActionType.BLANK;
		this.objectFeature = dummyFeature;
		this.objectItem = dummyItem;
		this.objectActor = dummyActor;
		this.usedItem = dummyItem;
	}




	// It should not be possible to alter the number of turns except by adding 1.
	public void addTurn() { ++turns; }
	public int getTurns() { return turns; }



}