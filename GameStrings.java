class GameStrings {
	
	public static final String GAME_BEGIN = "ZORK I: The Great Underground Empire"
		+ "\nCopyright (c) 1981, 1982, 1983 Infocom, Inc. All rights reserved."
		+ "\n ZORK is a registered trademark of Infocom, Inc."
		+ "\n Revision 88 / Serial number 840726";	
	
	public static final String DESC_WEST_OF_HOUSE = "You are standing in an open field west of a white house, with a boarded front door."
		+ "\nThere is a small mailbox here.";
	public static final String DESC_NORTH_OF_HOUSE = "You are facing the north side of a white house. There is no door here, "
		+ "and all the windows are boarded up. To the north a narrow path winds through the trees.";
	public static final String DESC_BEHIND_HOUSE = "You are behind the white house. A path leads into the forest to the east. "
		+ "In one corner of the house there is a small window which is slightly ajar.";
	public static final String DESC_SOUTH_OF_HOUSE = "You are facing the south side of a white house. There is no door here, "
		+ "and all the windows are boarded.";
	public static final String DESC_KITCHEN = "";
	public static final String DESC_ATTIC = "";
	public static final String DESC_LIVING_ROOM = "";

	public static final String DESC_FOREST_PATH = "";
	public static final String DESC_UP_TREE = "";
	public static final String DESC_FOREST_WEST = "";
	public static final String DESC_FOREST_EAST = "";
	public static final String DESC_FOREST_NORTHEAST = "";
	public static final String DESC_FOREST_SOUTH = "";
	public static final String DESC_CLEARING_NORTH = "";
	public static final String DESC_CLEARING_EAST = "";

	public static final String DESC_CANYON_VIEW = "";
	public static final String DESC_ROCKY_LEDGE = "";
	public static final String DESC_CANYON_BOTTOM = "";
	public static final String DESC_END_OF_RAINBOW = "";

	public static final String DESC_STONE_BARROW = "";
	public static final String DESC_INSIDE_STONE_BARROW = "";



	public static final String CANT_GO = "You can't go that way.";



	public static final String PROFANITY_ONE = "There's no need for that kind of language.";
	public static final String PROFANITY_TWO = "Do you have to use so many cuss words?";


	public static final String GAME_WON = "You won the game!";

	// All the words recognized by the game.
	public static final String[] GAME_WORDS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
	"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
	"u", "v", "w", "x", "y", "z", "ne", "nw", "se", "sw",
	"an", "around", "at", "attach", "attack",
	"bell", "close", "door", "down", "drop", "east", "egg", "exit", "five",
	"fuck", "go", "hand", "high", "highfive", "hit", "inventory", "jump", "juniper",
	"key", "kick", "lock", "look", "north", "northeast", "northwest", "note", "open", "passage", "piano", "pick",
	"play", "pull", "punch", "quit", "read", "ring", "room", "rope", "say", "scream",
	"shit", "shout", "slap", "south", "southeast", "southwest", "take", "the", "tie", "to", "unlock", "up", "wait",
	"walk", "west", "with", "wizard", "yell"};

	// This class does not get instantiated.
	private GameStrings() {}

}