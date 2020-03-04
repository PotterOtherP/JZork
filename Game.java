import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;

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

    CELLAR, EAST_OF_CHASM, GALLERY, STUDIO, TROLL_ROOM, EAST_WEST_PASSAGE,
    ROUND_ROOM, NARROW_PASSAGE, MIRROR_ROOM_SOUTH, WINDING_PASSAGE, CAVE_SOUTH,
    ENTRANCE_TO_HADES, LAND_OF_THE_DEAD, ALTAR, TEMPLE, EGYPTIAN_ROOM,
    TORCH_ROOM, DOME_ROOM, ENGRAVINGS_CAVE,

    LOUD_ROOM, DAMP_CAVE,
    WHITE_CLIFFS_BEACH_NORTH, WHITE_CLIFFS_BEACH_SOUTH, FRIGID_RIVER_1,
    FRIGID_RIVER_2, FRIGID_RIVER_3, FRIGID_RIVER_4, FRIGID_RIVER_5,
    SANDY_BEACH, SANDY_CAVE, SHORE, ARAGAIN_FALLS, ON_THE_RAINBOW,
    DAM_BASE, DAM, DAM_LOBBY, MAINTENANCE_ROOM,

    NORTH_SOUTH_PASSAGE, CHASM, DEEP_CANYON, RESERVOIR_SOUTH, STREAM_VIEW,
    STREAM, RESERVOIR, RESERVOIR_NORTH, ATLANTIS_ROOM, CAVE_NORTH,
    TWISTING_PASSAGE, MIRROR_ROOM_NORTH, COLD_PASSAGE, SLIDE_ROOM,
    MINE_ENTRANCE, SQUEAKY_ROOM, BAT_ROOM,

    SHAFT_ROOM, SMELLY_ROOM, GAS_ROOM, COAL_MINE_1, COAL_MINE_2,
    COAL_MINE_3, COAL_MINE_4, LADDER_TOP, LADDER_BOTTOM, DEAD_END_COAL_MINE,
    TIMBER_ROOM, DRAFTY_ROOM, MACHINE_ROOM,

    GRATING_ROOM, CYCLOPS_ROOM, STRANGE_PASSAGE, TREASURE_ROOM,

    MAZE_1, MAZE_2, MAZE_3, MAZE_4, MAZE_5, MAZE_6, MAZE_7, MAZE_8,
    MAZE_9, MAZE_10, MAZE_11, MAZE_12, MAZE_13, MAZE_14, MAZE_15,
    DEAD_END_MAZE_NORTH, DEAD_END_MAZE_SOUTHEAST, DEAD_END_MAZE_CENTER,
    DEAD_END_MAZE_SOUTHWEST, 


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

	SHOUT, INVENTORY, WAIT,

    NORTH, SOUTH, EAST,	WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST,
	UP,	DOWN, IN, OUT,

    ANSWER, ATTACK, BLOW, BREAK, BURN, CLIMB, CLOSE, COUNT, CROSS, CUT,
    DEFLATE, DIG, DRINK, DROP, EAT, ENTER, EXAMINE, EXIT, EXTINGUISH,
    FILL, FOLLOW, GIVE, INFLATE, JUMP, KICK, KNOCK, LIGHT, LISTEN,
    LOCK, LOOK, LOWER, MOVE, OPEN, POUR, PRAY, PULL, PUSH, PUT, RAISE,
    READ, SAY, SEARCH, SHAKE, SLIDE, SMELL, STAY, STRIKE, SWIM, TAKE,
    TELL, THROW, TIE, TOUCH, TURN, UNLOCK, WAKE, WALK, WAVE, WEAR, WIND,


	STORE, PLACE,

	SPEAK, MOVE_OBJECT,	ACTIVATE, RING,	PLAY,
	UNLIGHT,

	DEFEND,

	BRIEF, SUPERBRIEF, VERBOSE, PROFANITY, AGAIN,
    DIAGNOSE, SCORE, SAVE, RESTART, RESTORE, QUIT,
    GODMODE_TOGGLE, AUTHOR,

    TELEPORT,
	NULL_ACTION
	}

enum ActionType {

	NULL_TYPE,
	REFLEXIVE,
	DIRECT,
	INDIRECT,
    EXIT,
    PLACE_REMOVE,
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
 * This program is my attempt to recreate Zork I as well as possible.
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
     * Fix action lookup
     *
     * Overworld objects:
     *
     * Kitchen window - passage from Behind House to Kitchen
     * Songbird
     * Attic - Darkness
     * Lantern - turn on, lifetime
     * Object weights and point values
     * Trophy case - points
     * Kitchen table
     * Sack, lunch, water
     * Egg, clockwork canary
     * Leaf pile
     * Carpet and trap door
     *
     */

    // Global variables
	private static boolean gameover = true;
	private static boolean godmode = false;
    private static boolean TESTING = false;


    // Lists and hashmaps
    private static HashMap<String, Action> actions = new HashMap<String, Action>();
	private static HashMap<Action, ActionType> actionTypes = new HashMap<Action, ActionType>();
    private static HashMap<String, ObjectType> currentObjects = new HashMap<String, ObjectType>();
	private static ArrayList<String> dictionary = new ArrayList<String>();



    // Constants
    private static final int LINE_LENGTH = 50;
	private static final Location STARTING_LOCATION = Location.WEST_OF_HOUSE;
    private static final int LANTERN_LIFESPAN = 100;


	public static void main(String[] args)
	{

		GameState gameState = new GameState();

        if (args.length > 0 && args[0].equals("test"))
        {
            TESTING = true;
            output("Testing");
        }


		String playerText = "";
	
        createWorldMap(gameState);
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

            outputLine();
		}


		endGame(gameState);
		
	}




	private static void initGame(GameState state)
	{	
		// Populate the action lists and the dictionary
		createActions();
		fillDictionary();

		/* Features - Overworld
         * 
         * Mailbox (West of House)
         * Window (Kitchen, Behind House)
         * Carpet (Living Room)
         * Trophy Case (Living Room)
         * Trap Door (Living Room)
         * Pile of Leaves(Clearing North)
         * Grating (Null Location / Clearing North)
         *
         */
        
        Container mailbox = new Container("mailbox", Location.WEST_OF_HOUSE, 10, Location.INSIDE_MAILBOX);
        Container trophyCase = new Container("trophy case", Location.LIVING_ROOM, 1000, Location.INSIDE_TROPHY_CASE);
        Container brownSack = new Container("sack", Location.KITCHEN, 50, Location.INSIDE_SACK);

        state.objectList.put(mailbox.name, mailbox);
        state.objectList.put(trophyCase.name, trophyCase);
        state.objectList.put(brownSack.name, brownSack);

        state.objectList.put(mailbox.name, mailbox);

        Feature houseWindow = new Feature("window", Location.BEHIND_HOUSE);
        houseWindow.altLocations.add(Location.KITCHEN);

        Feature carpet = new Feature("carpet", Location.LIVING_ROOM);
        Feature trapDoor = new Feature("trap door", Location.LIVING_ROOM);
        Feature leafPile = new Feature("pile", Location.CLEARING_NORTH);
        Feature house = new Feature("house", Location.WEST_OF_HOUSE);
        Feature grating = new Feature("grating", Location.NULL_LOCATION);
        house.altLocations.add(Location.NORTH_OF_HOUSE);
        house.altLocations.add(Location.BEHIND_HOUSE);
        house.altLocations.add(Location.SOUTH_OF_HOUSE);

        state.objectList.put(house.name, house);
        state.objectList.put(houseWindow.name, houseWindow);
        //state.objectList.put(kitchenWindow.name, kitchenWindow);
        state.objectList.put(carpet.name, carpet);
        state.objectList.put(trapDoor.name, trapDoor);
        state.objectList.put(leafPile.name, leafPile);


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
        Item leaflet = new Item("leaflet", Location.INSIDE_MAILBOX, 0, 0);
        Item rope = new Item("rope", Location.ATTIC, 0, 0);
        Item rustyKnife = new Item("knife", Location.ATTIC, 0, 0);
        Item glassBottle = new Item("bottle", Location.KITCHEN, 0, 0);
        Item elvishSword = new Item("sword", Location.LIVING_ROOM, 0, 0);
        Item jewelEgg = new Item("egg", Location.UP_TREE, 0, 0);
        Item birdsNest = new Item("nest", Location.UP_TREE, 0, 0);
        Item lantern = new Item("lantern", Location.LIVING_ROOM, 0, 0);
        lantern.lifespan = LANTERN_LIFESPAN;

        state.objectList.put(leaflet.name, leaflet);
        state.objectList.put(rope.name, rope);
        state.objectList.put(rustyKnife.name, rustyKnife);
        state.objectList.put(glassBottle.name, glassBottle);
        state.objectList.put(elvishSword.name, elvishSword);
        state.objectList.put(jewelEgg.name, jewelEgg);
        state.objectList.put(birdsNest.name, birdsNest);
        state.objectList.put(lantern.name, lantern);

        // Fill the inventories of the containers
        for (GameObject cont : state.objectList.values())
        {
            if (cont.isContainer())
            {
                Container c = (Container)(cont);

                for (GameObject it : state.objectList.values())
                {
                    if (it.location == c.containerID)
                        c.inventory.add((Item)it);
                }
            }
        }


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

        Actor songbird = new Actor("songbird", Location.NULL_LOCATION);
        ActorMethod songbirdMethod = () -> {

            switch (state.playerLocation)
            {
                case FOREST_PATH:
                case FOREST_SOUTH:
                case FOREST_EAST:
                case FOREST_NORTHEAST:
                case FOREST_WEST:
                case CLEARING_NORTH:
                case CLEARING_EAST:
                case UP_TREE:
                {
                    songbird.location = state.playerLocation;
                    Random rand = new Random();
                    if (rand.nextInt(100) < 37)
                        output(GameStrings.SONGBIRD);
                } break;

                default: {} break;
            }




        };

        songbird.setActorMethod(songbirdMethod);
        songbird.presence = "";
        songbird.takeFail = GameStrings.SONGBIRD_NEARBY;
        songbird.examineString = GameStrings.SONGBIRD_NEARBY;

        state.objectList.put(giant.name, giant);
        state.objectList.put(songbird.name, songbird);
		



        


        ActivateMethod dummyMethod = (GameState gs, Action act) -> {};

        


        ActivateMethod leafletMethod = (GameState gs, Action act) -> {

            switch(act)
            {
                case READ:
                {
                    output(GameStrings.LEAFLET_TEXT);
                } break;

                default:
                {
                    output("You can't do that to the leaflet.");
                } break;
            }


        };
		
        leaflet.setMethod(leafletMethod);

        ActivateMethod lanternMethod = (GameState gs, Action act) -> {

            Item self = (Item)(gs.objectList.get("lantern"));
            switch (act)
            {
                case LIGHT:
                {
                    self.activated = true;
                    gs.lightActivated = true;
                    output("You turn on the lantern.");
                    self.examineString = "The lantern is on.";
                } break;

                case UNLIGHT:
                {
                    self.activated = false;
                    gs.lightActivated = false;
                    output("You turn off the lantern.");
                } break;

                default:
                {
                    output("You can't do that to the lantern.");

                } break;
            }
        };

        lantern.setMethod(lanternMethod);


        ActivateMethod leafPileMethod = (GameState gs, Action act) -> {

            switch (act)
            {
                case MOVE_OBJECT:
                {
                    if (!gs.leafPileMoved)
                    {
                        gs.leafPileMoved = true;
                        grating.location = Location.CLEARING_NORTH;


                    }
                    else
                    {
                        output("Having moved the leaf pile once, you find it impossible to move again.");
                    }

                } break;

                default:
                {
                    output("You can't do that to the leaf pile.");
                } break;
            }


        };

        leafPile.setMethod(leafPileMethod);
	

		// Object creation complete. Start setting up the game

		// Put the player in the starting location
		state.setPlayerLocation(STARTING_LOCATION);
		state.worldMap.get(STARTING_LOCATION).firstVisit = false;

		// Beginning text of the game.
        outputLine();
        output(state.worldMap.get(STARTING_LOCATION).name);
		output(GameStrings.DESC_WEST_OF_HOUSE);
        outputLine();
		
	}


    private static void createWorldMap(GameState state)
    {
        // Overworld passages
        Passage house_west_north = new Passage(Location.WEST_OF_HOUSE, Location.NORTH_OF_HOUSE);
        Passage house_west_south = new Passage(Location.WEST_OF_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house_west_barrow = new Passage(Location.WEST_OF_HOUSE, Location.STONE_BARROW);
        Passage house_west_forestW = new Passage(Location.WEST_OF_HOUSE, Location.FOREST_WEST);
        Passage house_north_forestpath = new Passage(Location.NORTH_OF_HOUSE, Location.FOREST_PATH);
        Passage house_north_behind = new Passage(Location.NORTH_OF_HOUSE, Location.BEHIND_HOUSE);
        Passage house_behind_clearingE = new Passage(Location.BEHIND_HOUSE, Location.CLEARING_EAST);
        Passage house_behind_south = new Passage(Location.BEHIND_HOUSE, Location.SOUTH_OF_HOUSE);
        Passage house_behind_kitchen = new Passage(Location.BEHIND_HOUSE, Location.KITCHEN);
        Passage house_south_forestS = new Passage(Location.SOUTH_OF_HOUSE, Location.FOREST_SOUTH);
        Passage kitchen_attic = new Passage(Location.KITCHEN, Location.ATTIC);
        Passage kitchen_livingroom = new Passage(Location.KITCHEN, Location.LIVING_ROOM);
        Passage forestpath_clearingN = new Passage(Location.FOREST_PATH, Location.CLEARING_NORTH);
        Passage forestpath_forestE = new Passage(Location.FOREST_PATH, Location.FOREST_EAST);
        Passage forestpath_forestW = new Passage(Location.FOREST_PATH, Location.FOREST_WEST);
        Passage forestpath_uptree = new Passage(Location.FOREST_PATH, Location.UP_TREE);
        Passage clearingN_forestE = new Passage(Location.CLEARING_NORTH, Location.FOREST_EAST);
        Passage clearingN_forestW = new Passage(Location.CLEARING_NORTH, Location.FOREST_WEST);
        Passage forestE_clearingE = new Passage(Location.FOREST_EAST, Location.CLEARING_EAST);
        Passage forestE_forestNE = new Passage(Location.FOREST_EAST, Location.FOREST_NORTHEAST);
        Passage clearingE_forestS = new Passage(Location.CLEARING_EAST, Location.FOREST_SOUTH);
        Passage clearingE_canyon = new Passage(Location.CLEARING_EAST, Location.CANYON_VIEW);
        Passage forestS_canyon = new Passage(Location.FOREST_SOUTH, Location.CANYON_VIEW);
        Passage forestS_forestW = new Passage(Location.FOREST_SOUTH, Location.FOREST_WEST);
        Passage canyon_ledge = new Passage(Location.CANYON_VIEW, Location.ROCKY_LEDGE);
        Passage ledge_bottom = new Passage(Location.ROCKY_LEDGE, Location.CANYON_BOTTOM);
        Passage canyon_bottom_rainbow = new Passage(Location.CANYON_BOTTOM, Location.END_OF_RAINBOW);
        Passage barrowInside = new Passage(Location.STONE_BARROW, Location.INSIDE_STONE_BARROW);

        // GUE southern passages
        Passage cellar_livingroom = new Passage(Location.CELLAR, Location.LIVING_ROOM);
        Passage cellar_troll = new Passage(Location.CELLAR, Location.TROLL_ROOM);
        Passage cellar_eastchasm = new Passage(Location.CELLAR, Location.EAST_OF_CHASM);
        Passage eastchasm_gallery = new Passage(Location.EAST_OF_CHASM, Location.GALLERY);
        Passage gallery_studio = new Passage(Location.GALLERY, Location.STUDIO);
        Passage studio_kitchen = new Passage(Location.STUDIO, Location.KITCHEN);
        Passage troll_eastwest = new Passage(Location.TROLL_ROOM, Location.EAST_WEST_PASSAGE);
        Passage eastwest_chasm  = new Passage(Location.EAST_WEST_PASSAGE, Location.CHASM);
        Passage eastwest_round = new Passage(Location.EAST_WEST_PASSAGE, Location.ROUND_ROOM);
        Passage round_northsouth = new Passage(Location.ROUND_ROOM, Location.NORTH_SOUTH_PASSAGE);
        Passage round_narrow = new Passage(Location.ROUND_ROOM, Location.NARROW_PASSAGE);
        Passage round_loud = new Passage(Location.ROUND_ROOM, Location.LOUD_ROOM);
        Passage round_engravings = new Passage(Location.ROUND_ROOM, Location.ENGRAVINGS_CAVE);
        Passage narrow_mirror = new Passage(Location.NARROW_PASSAGE, Location.MIRROR_ROOM_SOUTH);
        Passage mirror_winding = new Passage(Location.MIRROR_ROOM_SOUTH, Location.WINDING_PASSAGE);
        Passage mirrorsouth_cave = new Passage(Location.MIRROR_ROOM_SOUTH, Location.CAVE_SOUTH);
        Passage winding_cave = new Passage(Location.WINDING_PASSAGE, Location.CAVE_SOUTH);
        Passage cave_hades = new Passage(Location.CAVE_SOUTH, Location.ENTRANCE_TO_HADES);
        Passage hades_land_dead = new Passage(Location.ENTRANCE_TO_HADES, Location.LAND_OF_THE_DEAD);
        Passage engravings_dome = new Passage(Location.ENGRAVINGS_CAVE, Location.DOME_ROOM);
        Passage dome_torch = new Passage(Location.DOME_ROOM, Location.TORCH_ROOM);
        Passage torch_temple = new Passage(Location.TORCH_ROOM, Location.TEMPLE);
        Passage temple_egypt = new Passage(Location.TEMPLE, Location.EGYPTIAN_ROOM);
        Passage temple_altar = new Passage(Location.TEMPLE, Location.ALTAR);
        Passage altar_cave = new Passage(Location.ALTAR, Location.CAVE_SOUTH);
        Passage cyclops_strange = new Passage(Location.CYCLOPS_ROOM, Location.STRANGE_PASSAGE);
        Passage cyclops_treasure = new Passage(Location.CYCLOPS_ROOM, Location.TREASURE_ROOM);
        Passage strange_living_room = new Passage(Location.STRANGE_PASSAGE, Location.LIVING_ROOM);
        Passage grating_clearing = new Passage(Location.GRATING_ROOM, Location.CLEARING_NORTH);

        // GUE dam area passages
        Passage loud_damp = new Passage(Location.LOUD_ROOM, Location.DAMP_CAVE);
        Passage loud_deep_canyon = new Passage(Location.LOUD_ROOM, Location.DEEP_CANYON);
        Passage damp_white_north = new Passage(Location.DAMP_CAVE, Location.WHITE_CLIFFS_BEACH_NORTH);
        Passage white_cliffs_north_south = new Passage(Location.WHITE_CLIFFS_BEACH_NORTH, Location.WHITE_CLIFFS_BEACH_SOUTH);
        Passage white_north_river = new Passage(Location.WHITE_CLIFFS_BEACH_NORTH, Location.FRIGID_RIVER_3);
        Passage white_south_river = new Passage(Location.WHITE_CLIFFS_BEACH_SOUTH, Location.FRIGID_RIVER_4);
        Passage river_one_two = new Passage(Location.FRIGID_RIVER_1, Location.FRIGID_RIVER_2);
        Passage river_two_three = new Passage(Location.FRIGID_RIVER_2, Location.FRIGID_RIVER_3);
        Passage river_three_four = new Passage(Location.FRIGID_RIVER_3, Location.FRIGID_RIVER_4);
        Passage river_four_five = new Passage(Location.FRIGID_RIVER_4, Location.FRIGID_RIVER_5);
        Passage river_sandy_beach = new Passage(Location.FRIGID_RIVER_4, Location.SANDY_BEACH);
        Passage river_shore = new Passage(Location.FRIGID_RIVER_5, Location.SHORE);
        Passage sandy_beach_cave = new Passage(Location.SANDY_BEACH, Location.SANDY_CAVE);
        Passage sandy_beach_shore = new Passage(Location.SANDY_BEACH, Location.SHORE);
        Passage shore_falls = new Passage(Location.SHORE, Location.ARAGAIN_FALLS);
        Passage falls_rainbow = new Passage(Location.ARAGAIN_FALLS, Location.ON_THE_RAINBOW);
        Passage rainbow_end = new Passage(Location.ON_THE_RAINBOW, Location.END_OF_RAINBOW);
        Passage dam_base_river = new Passage(Location.DAM_BASE, Location.FRIGID_RIVER_1);
        Passage dam_dam_base = new Passage(Location.DAM, Location.DAM_BASE);
        Passage dam_dam_lobby = new Passage(Location.DAM, Location.DAM_LOBBY);
        Passage dam_lobby_maintenance = new Passage(Location.DAM_LOBBY, Location.MAINTENANCE_ROOM);
        Passage dam_deep_canyon = new Passage(Location.DAM, Location.DEEP_CANYON);
        Passage dam_res_south = new Passage(Location.DAM, Location.RESERVOIR_SOUTH);
        Passage northsouth_deep_canyon = new Passage(Location.NORTH_SOUTH_PASSAGE, Location.DEEP_CANYON);
        Passage northsouth_chasm = new Passage(Location.NORTH_SOUTH_PASSAGE, Location.CHASM);
        Passage res_south_chasm = new Passage(Location.RESERVOIR_SOUTH, Location.CHASM);
        Passage res_south_stream_view = new Passage(Location.RESERVOIR_SOUTH, Location.STREAM_VIEW);
        Passage res_south_res = new Passage(Location.RESERVOIR_SOUTH, Location.RESERVOIR);
        Passage res_south_deep = new Passage(Location.RESERVOIR_SOUTH, Location.DEEP_CANYON);
        Passage stream_view_stream = new Passage(Location.STREAM_VIEW, Location.STREAM);

        // GUE northern passages
        Passage reservoir_stream = new Passage(Location.RESERVOIR, Location.STREAM);
        Passage res_north_res = new Passage(Location.RESERVOIR_NORTH, Location.RESERVOIR);
        Passage res_north_atlantis = new Passage(Location.RESERVOIR_NORTH, Location.ATLANTIS_ROOM);
        Passage atlantis_cave = new Passage(Location.ATLANTIS_ROOM, Location.CAVE_NORTH);
        Passage cave_twisting = new Passage(Location.CAVE_NORTH, Location.TWISTING_PASSAGE);
        Passage cave_mirrornorth = new Passage(Location.CAVE_NORTH, Location.MIRROR_ROOM_NORTH);
        Passage twisting_mirror = new Passage(Location.TWISTING_PASSAGE, Location.MIRROR_ROOM_NORTH);
        Passage mirror_cold = new Passage(Location.MIRROR_ROOM_NORTH, Location.COLD_PASSAGE);
        Passage cold_slide = new Passage(Location.COLD_PASSAGE, Location.SLIDE_ROOM);
        Passage slide_cellar = new Passage(Location.SLIDE_ROOM, Location.CELLAR);
        Passage slide_mine_entrance = new Passage(Location.SLIDE_ROOM, Location.MINE_ENTRANCE);
        Passage mine_entrance_squeaky = new Passage(Location.MINE_ENTRANCE, Location.SQUEAKY_ROOM);
        Passage squeaky_bat = new Passage(Location.SQUEAKY_ROOM, Location.BAT_ROOM);
        Passage bat_shaft = new Passage(Location.BAT_ROOM, Location.SHAFT_ROOM);

        // Coal mine passages

        Passage shaft_smelly = new Passage(Location.SHAFT_ROOM, Location.SMELLY_ROOM);
        Passage smelly_gas = new Passage(Location.SMELLY_ROOM, Location.GAS_ROOM);
        Passage gas_coal_1 = new Passage(Location.GAS_ROOM, Location.COAL_MINE_1);
        Passage coal_1_self = new Passage(Location.COAL_MINE_1, Location.COAL_MINE_1);
        Passage coal_1_coal_2 = new Passage(Location.COAL_MINE_1, Location.COAL_MINE_2);
        Passage coal_2_self = new Passage(Location.COAL_MINE_2, Location.COAL_MINE_2);
        Passage coal_2_coal_3 = new Passage(Location.COAL_MINE_2, Location.COAL_MINE_3);
        Passage coal_3_self = new Passage(Location.COAL_MINE_3, Location.COAL_MINE_3);
        Passage coal_3_coal_4 = new Passage(Location.COAL_MINE_3, Location.COAL_MINE_4);
        Passage coal_4_self = new Passage(Location.COAL_MINE_4, Location.COAL_MINE_4);
        Passage coal_4_ladder_top = new Passage(Location.COAL_MINE_4, Location.LADDER_TOP);
        Passage ladder_top_bottom = new Passage(Location.LADDER_TOP, Location.LADDER_BOTTOM);
        Passage ladder_bottom_dead_end = new Passage(Location.LADDER_BOTTOM, Location.DEAD_END_COAL_MINE);
        Passage ladder_bottom_timber = new Passage(Location.LADDER_BOTTOM, Location.TIMBER_ROOM);
        Passage timber_drafty = new Passage(Location.TIMBER_ROOM, Location.DRAFTY_ROOM);
        Passage drafty_machine = new Passage(Location.DRAFTY_ROOM, Location.MACHINE_ROOM);

        // Maze passages
        Passage troll_maze = new Passage(Location.TROLL_ROOM, Location.MAZE_1);
        Passage maze1_maze2 = new Passage(Location.MAZE_1, Location.MAZE_2);
        Passage maze1_maze4 = new Passage(Location.MAZE_1, Location.MAZE_4);
        Passage maze1_self = new Passage(Location.MAZE_1, Location.MAZE_1);
        
        Passage maze2_maze3 = new Passage(Location.MAZE_2, Location.MAZE_3);
        Passage maze2_maze4 = new Passage(Location.MAZE_2, Location.MAZE_4);

        Passage maze3_maze4 = new Passage(Location.MAZE_3, Location.MAZE_4);
        Passage maze3_maze5 = new Passage(Location.MAZE_3, Location.MAZE_5);

        Passage maze4_dead_end = new Passage(Location.MAZE_4, Location.DEAD_END_MAZE_NORTH);

        Passage maze5_maze6 = new Passage(Location.MAZE_5, Location.MAZE_6);
        Passage maze5_dead_end = new Passage(Location.MAZE_5, Location.DEAD_END_MAZE_CENTER);

        Passage maze6_maze7 = new Passage(Location.MAZE_6, Location.MAZE_7);
        Passage maze6_maze9 = new Passage(Location.MAZE_6, Location.MAZE_9);
        Passage maze6_self = new Passage(Location.MAZE_6, Location.MAZE_6);

        Passage maze7_dead_end = new Passage(Location.MAZE_7, Location.DEAD_END_MAZE_NORTH);
        Passage maze7_maze8 = new Passage(Location.MAZE_7, Location.MAZE_8);
        Passage maze7_maze14 = new Passage(Location.MAZE_7, Location.MAZE_14);
        Passage maze7_maze15 = new Passage(Location.MAZE_7, Location.MAZE_15);

        Passage maze8_dead_end = new Passage(Location.MAZE_8, Location.DEAD_END_MAZE_SOUTHEAST);
        Passage maze8_self = new Passage(Location.MAZE_8, Location.MAZE_8);

        Passage maze9_maze10 = new Passage(Location.MAZE_9, Location.MAZE_10);
        Passage maze9_maze11 = new Passage(Location.MAZE_9, Location.MAZE_11);
        Passage maze9_maze12 = new Passage(Location.MAZE_9, Location.MAZE_12);
        Passage maze9_maze13 = new Passage(Location.MAZE_9, Location.MAZE_13);
        Passage maze9_self = new Passage(Location.MAZE_9, Location.MAZE_9);

        Passage maze10_maze11 = new Passage(Location.MAZE_10, Location.MAZE_11);
        Passage maze10_maze13 = new Passage(Location.MAZE_10, Location.MAZE_13);

        Passage maze11_maze12 = new Passage(Location.MAZE_11, Location.MAZE_12);
        Passage maze11_maze13 = new Passage(Location.MAZE_11, Location.MAZE_13);
        Passage maze11_grating = new Passage(Location.MAZE_11, Location.GRATING_ROOM);

        Passage maze12_maze13 = new Passage(Location.MAZE_12, Location.MAZE_13);
        Passage maze12_maze5 = new Passage(Location.MAZE_12, Location.MAZE_5);
        Passage maze12_dead_end = new Passage(Location.MAZE_12, Location.DEAD_END_MAZE_SOUTHEAST);

        Passage maze14_maze15 = new Passage(Location.MAZE_14, Location.MAZE_15);
        Passage maze14_self = new Passage(Location.MAZE_14, Location.MAZE_14);

        Passage maze15_cyclops = new Passage(Location.MAZE_15, Location.CYCLOPS_ROOM);


        // Special passage modifications
        house_behind_kitchen.close();
        house_behind_kitchen.closedFail = GameStrings.KITCHEN_WINDOW_CLOSED;


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
        

        Room livingRoom = new Room("Living Room", GameStrings.DESC_LIVING_ROOM_TRAPDOOR_CLOSED, Location.LIVING_ROOM);
        livingRoom.addExit(Action.EAST, kitchen_livingroom);
        livingRoom.addExit(Action.DOWN, cellar_livingroom);
        livingRoom.addExit(Action.WEST, strange_living_room);

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
        clearingNorth.addExit(Action.DOWN, grating_clearing);

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
        endOfRainbow.addExit(Action.EAST, rainbow_end);

        Room stoneBarrow = new Room("Stone Barrow", GameStrings.DESC_STONE_BARROW, Location.STONE_BARROW);
        stoneBarrow.addExit(Action.NORTHEAST, house_west_barrow);
        stoneBarrow.addExit(Action.WEST, barrowInside);

        Room insideStoneBarrow = new Room("Inside Stone Barrow", GameStrings.DESC_INSIDE_STONE_BARROW, Location.INSIDE_STONE_BARROW);
        insideStoneBarrow.addExit(Action.EAST, barrowInside);

        Room cellar = new Room("Cellar", GameStrings.DESC_CELLAR, Location.CELLAR);
        cellar.addExit(Action.NORTH, cellar_troll);
        cellar.addExit(Action.SOUTH, cellar_eastchasm);

        Room eastOfChasm = new Room("East of Chasm", GameStrings.DESC_EAST_OF_CHASM, Location.EAST_OF_CHASM);
        eastOfChasm.addExit(Action.NORTH, cellar_eastchasm);
        eastOfChasm.addExit(Action.EAST, eastchasm_gallery);

        Room gallery = new Room("Gallery", GameStrings.DESC_GALLERY, Location.GALLERY);
        gallery.addExit(Action.WEST, eastchasm_gallery);
        gallery.addExit(Action.NORTH, gallery_studio);

        Room studio = new Room("Studio", GameStrings.DESC_STUDIO, Location.STUDIO);
        studio.addExit(Action.SOUTH, gallery_studio);
        studio.addExit(Action.UP, studio_kitchen);

        Room trollRoom = new Room("Troll Room", GameStrings.DESC_TROLL_ROOM, Location.TROLL_ROOM);
        trollRoom.addExit(Action.SOUTH, cellar_troll);
        trollRoom.addExit(Action.WEST, troll_maze);
        trollRoom.addExit(Action.EAST, troll_eastwest);

        Room eastWestPassage = new Room("East-West Passage", GameStrings.DESC_EAST_WEST_PASSAGE , Location.EAST_WEST_PASSAGE);
        eastWestPassage.addExit(Action.WEST, troll_eastwest);
        eastWestPassage.addExit(Action.NORTH, eastwest_chasm);
        eastWestPassage.addExit(Action.EAST, eastwest_round);

        Room roundRoom = new Room("Round Room", GameStrings.DESC_ROUND_ROOM, Location.ROUND_ROOM);
        roundRoom.addExit(Action.WEST, eastwest_round);
        roundRoom.addExit(Action.NORTH, round_northsouth);
        roundRoom.addExit(Action.EAST, round_loud);
        roundRoom.addExit(Action.SOUTH, round_narrow);
        roundRoom.addExit(Action.SOUTHEAST, round_engravings);

        Room narrowPassage = new Room("Narrow Passage", GameStrings.DESC_NARROW_PASSAGE, Location.NARROW_PASSAGE);
        narrowPassage.addExit(Action.NORTH, round_narrow);
        narrowPassage.addExit(Action.SOUTH, narrow_mirror);

        Room mirrorRoomSouth = new Room("Mirror Room", GameStrings.DESC_MIRROR_ROOM_SOUTH, Location.MIRROR_ROOM_SOUTH);
        mirrorRoomSouth.addExit(Action.NORTH, narrow_mirror);
        mirrorRoomSouth.addExit(Action.WEST, mirror_winding);
        mirrorRoomSouth.addExit(Action.EAST, mirrorsouth_cave);

        Room windingPassage = new Room("Winding Passage", GameStrings.DESC_WINDING_PASSAGE, Location.WINDING_PASSAGE);
        windingPassage.addExit(Action.NORTH, mirror_winding);
        windingPassage.addExit(Action.EAST, winding_cave);

        Room caveSouth = new Room("Cave", GameStrings.DESC_CAVE_SOUTH, Location.CAVE_SOUTH);
        caveSouth.addExit(Action.NORTH, mirrorsouth_cave);
        caveSouth.addExit(Action.WEST, winding_cave);
        caveSouth.addExit(Action.DOWN, cave_hades);
        caveSouth.addExit(Action.DOWN, cave_hades);

        Room entranceToHades = new Room("Entrance to Hades", GameStrings.DESC_ENTRANCE_TO_HADES, Location.ENTRANCE_TO_HADES);
        entranceToHades.addExit(Action.UP, cave_hades);
        entranceToHades.addExit(Action.SOUTH, hades_land_dead);

        Room landOfTheDead = new Room("Land of the Dead", GameStrings.DESC_LAND_OF_THE_DEAD, Location.LAND_OF_THE_DEAD);
        landOfTheDead.addExit(Action.NORTH, hades_land_dead);

        Room engravingsCave = new Room("Engravings Cave", GameStrings.DESC_ENGRAVINGS_CAVE, Location.ENGRAVINGS_CAVE);
        engravingsCave.addExit(Action.NORTHWEST, round_engravings);
        engravingsCave.addExit(Action.EAST, engravings_dome);

        Room domeRoom = new Room("Dome Room", GameStrings.DESC_DOME_ROOM, Location.DOME_ROOM);
        domeRoom.addExit(Action.WEST, engravings_dome);
        domeRoom.addExit(Action.DOWN, dome_torch);

        Room torchRoom = new Room("Torch Room", GameStrings.DESC_TORCH_ROOM, Location.TORCH_ROOM);
        torchRoom.addExit(Action.UP, dome_torch);
        torchRoom.addExit(Action.SOUTH, torch_temple);

        Room temple = new Room("Temple", GameStrings.DESC_TEMPLE, Location.TEMPLE);
        temple.addExit(Action.NORTH, torch_temple);
        temple.addExit(Action.EAST, temple_egypt);
        temple.addExit(Action.SOUTH, temple_altar);

        Room egyptianRoom = new Room("Egyptian Room", GameStrings.DESC_EGYPTIAN_ROOM, Location.EGYPTIAN_ROOM);
        egyptianRoom.addExit(Action.WEST, temple_egypt);

        Room altar = new Room("Altar", GameStrings.DESC_ALTAR, Location.ALTAR);
        altar.addExit(Action.NORTH, temple_altar);
        altar.addExit(Action.DOWN, altar_cave);

        Room loudRoom = new Room("Loud Room", GameStrings.DESC_LOUD_ROOM, Location.LOUD_ROOM);
        loudRoom.addExit(Action.WEST, round_loud);
        loudRoom.addExit(Action.UP, loud_deep_canyon);
        loudRoom.addExit(Action.EAST, loud_damp);

        Room dampCave = new Room("Damp Cave", GameStrings.DESC_DAMP_CAVE, Location.DAMP_CAVE);
        dampCave.addExit(Action.WEST, loud_damp);
        dampCave.addExit(Action.EAST, damp_white_north);

        Room whiteCliffsBeachNorth = new Room("White Cliffs Beach North", GameStrings.DESC_WHITE_CLIFFS_BEACH_NORTH, Location.WHITE_CLIFFS_BEACH_NORTH);
        whiteCliffsBeachNorth.addExit(Action.WEST, damp_white_north);
        whiteCliffsBeachNorth.addExit(Action.SOUTH, white_cliffs_north_south);
        whiteCliffsBeachNorth.addExit(Action.EAST, white_north_river);

        Room whiteCliffsBeachSouth = new Room("White Cliffs Beach South", GameStrings.DESC_WHITE_CLIFFS_BEACH_SOUTH, Location.WHITE_CLIFFS_BEACH_SOUTH);
        whiteCliffsBeachSouth.addExit(Action.NORTH, white_cliffs_north_south);
        whiteCliffsBeachSouth.addExit(Action.EAST, white_south_river);

        Room frigidRiver1 = new Room("Frigid River", GameStrings.DESC_FRIGID_RIVER_1, Location.FRIGID_RIVER_1);
        frigidRiver1.addExit(Action.WEST, dam_base_river);

        Room frigidRiver2 = new Room("Frigid River", GameStrings.DESC_FRIGID_RIVER_2, Location.FRIGID_RIVER_2);
        Room frigidRiver3 = new Room("Frigid River", GameStrings.DESC_FRIGID_RIVER_3, Location.FRIGID_RIVER_3);
        frigidRiver3.addExit(Action.WEST, white_north_river);

        Room frigidRiver4 = new Room("Frigid River", GameStrings.DESC_FRIGID_RIVER_4, Location.FRIGID_RIVER_4);
        frigidRiver4.addExit(Action.WEST, white_south_river);
        frigidRiver4.addExit(Action.EAST, river_sandy_beach);

        Room frigidRiver5 = new Room("Frigid River", GameStrings.DESC_FRIGID_RIVER_5, Location.FRIGID_RIVER_5);
        frigidRiver5.addExit(Action.EAST, river_shore);

        Room sandyCave = new Room("Sandy Cave", GameStrings.DESC_SANDY_CAVE, Location.SANDY_CAVE);
        sandyCave.addExit(Action.SOUTHWEST, sandy_beach_cave);

        Room sandyBeach = new Room("Sandy Beach", GameStrings.DESC_SANDY_BEACH, Location.SANDY_BEACH);
        sandyBeach.addExit(Action.NORTHEAST, sandy_beach_cave);
        sandyBeach.addExit(Action.SOUTH, sandy_beach_shore);
        sandyBeach.addExit(Action.WEST, river_sandy_beach);

        Room shore = new Room("Shore", GameStrings.DESC_SHORE, Location.SHORE);
        shore.addExit(Action.NORTH, sandy_beach_shore);
        shore.addExit(Action.WEST, river_shore);
        shore.addExit(Action.SOUTH, shore_falls);

        Room aragainFalls = new Room("Aragain Falls", GameStrings.DESC_ARAGAIN_FALLS, Location.ARAGAIN_FALLS);
        aragainFalls.addExit(Action.NORTH, shore_falls);
        aragainFalls.addExit(Action.WEST, falls_rainbow);

        Room onTheRainbow = new Room("On the Rainbow", GameStrings.DESC_ON_THE_RAINBOW, Location.ON_THE_RAINBOW);
        onTheRainbow.addExit(Action.EAST, falls_rainbow);
        onTheRainbow.addExit(Action.WEST, rainbow_end);

        Room dam = new Room("Dam", GameStrings.DESC_DAM, Location.DAM);
        dam.addExit(Action.WEST, dam_res_south);
        dam.addExit(Action.NORTH, dam_dam_lobby);
        dam.addExit(Action.SOUTH, dam_deep_canyon);
        dam.addExit(Action.EAST, dam_dam_base);

        Room damBase = new Room("Dam Base", GameStrings.DESC_DAM_BASE, Location.DAM_BASE);
        damBase.addExit(Action.NORTH, dam_dam_base);
        damBase.addExit(Action.EAST, dam_base_river);
        
        Room damLobby = new Room("Dam Lobby", GameStrings.DESC_DAM_LOBBY, Location.DAM_LOBBY);
        damLobby.addExit(Action.NORTH, dam_lobby_maintenance);
        damLobby.addExit(Action.EAST, dam_lobby_maintenance);
        damLobby.addExit(Action.SOUTH, dam_dam_lobby);
        
        Room maintenanceRoom = new Room("Maintenance Room", GameStrings.DESC_MAINTENANCE_ROOM, Location.MAINTENANCE_ROOM);
        maintenanceRoom.addExit(Action.SOUTH, dam_lobby_maintenance);
        maintenanceRoom.addExit(Action.WEST, dam_lobby_maintenance);
        
        Room northSouthPassage = new Room("North-South Passage", GameStrings.DESC_NORTH_SOUTH_PASSAGE, Location.NORTH_SOUTH_PASSAGE);
        northSouthPassage.addExit(Action.NORTH, northsouth_chasm);
        northSouthPassage.addExit(Action.NORTHEAST, northsouth_deep_canyon);
        northSouthPassage.addExit(Action.SOUTH, round_northsouth);
        
        Room deepCanyon = new Room("Deep Canyon", GameStrings.DESC_DEEP_CANYON, Location.DEEP_CANYON);
        deepCanyon.addExit(Action.EAST, dam_deep_canyon);
        deepCanyon.addExit(Action.NORTHWEST, res_south_deep);
        deepCanyon.addExit(Action.SOUTHWEST, northsouth_deep_canyon);
        deepCanyon.addExit(Action.DOWN, loud_deep_canyon);
        
        Room chasm = new Room("Chasm", GameStrings.DESC_CHASM, Location.CHASM);
        chasm.addExit(Action.NORTHEAST, res_south_chasm);
        chasm.addExit(Action.SOUTHWEST, eastwest_chasm);
        chasm.addExit(Action.SOUTH, northsouth_chasm);
        
        Room streamView = new Room("Stream View", GameStrings.DESC_STREAM_VIEW, Location.STREAM_VIEW);
        streamView.addExit(Action.EAST, res_south_stream_view);
        streamView.addExit(Action.NORTH, stream_view_stream);
        
        Room stream = new Room("Stream", GameStrings.DESC_STREAM, Location.STREAM);
        stream.addExit(Action.SOUTH, stream_view_stream);
        stream.addExit(Action.EAST, reservoir_stream);
        
        Room reservoirSouth = new Room("Reservoir South", GameStrings.DESC_RESERVOIR_SOUTH, Location.RESERVOIR_SOUTH);
        reservoirSouth.addExit(Action.NORTH, res_south_res);
        reservoirSouth.addExit(Action.WEST, res_south_stream_view);
        reservoirSouth.addExit(Action.EAST, res_south_chasm);
        reservoirSouth.addExit(Action.SOUTHWEST, res_south_deep);
        reservoirSouth.addExit(Action.SOUTHEAST, dam_res_south);
        
        Room reservoir = new Room("Reservoir", GameStrings.DESC_RESERVOIR, Location.RESERVOIR);
        reservoir.addExit(Action.NORTH, res_north_res);
        reservoir.addExit(Action.SOUTH, res_south_res);
        reservoir.addExit(Action.WEST, reservoir_stream);
        
        Room reservoirNorth = new Room("Reservoir North", GameStrings.DESC_RESERVOIR_NORTH, Location.RESERVOIR_NORTH);
        reservoirNorth.addExit(Action.NORTH, res_north_atlantis);
        reservoirNorth.addExit(Action.SOUTH, res_north_res);

        Room atlantisRoom = new Room("Atlantis Room", GameStrings.DESC_ATLANTIS_ROOM, Location.ATLANTIS_ROOM);
        atlantisRoom.addExit(Action.UP, atlantis_cave);
        atlantisRoom.addExit(Action.SOUTH, res_north_atlantis);
        
        Room caveNorth = new Room("Cave", GameStrings.DESC_CAVE_NORTH, Location.CAVE_NORTH);
        // Is this exit down or south???
        caveNorth.addExit(Action.DOWN, atlantis_cave);
        caveNorth.addExit(Action.NORTH, cave_mirrornorth);
        caveNorth.addExit(Action.WEST, cave_twisting);
        
        Room twistingPassage = new Room("Twisting Passage", GameStrings.DESC_TWISTING_PASSAGE, Location.TWISTING_PASSAGE);
        twistingPassage.addExit(Action.EAST, cave_twisting);
        twistingPassage.addExit(Action.NORTH, twisting_mirror);
        
        Room mirrorRoomNorth = new Room("Mirror Room", GameStrings.DESC_MIRROR_ROOM_NORTH, Location.MIRROR_ROOM_NORTH);
        mirrorRoomNorth.addExit(Action.EAST, cave_mirrornorth);
        mirrorRoomNorth.addExit(Action.WEST, twisting_mirror);
        mirrorRoomNorth.addExit(Action.NORTH, mirror_cold);
        
        Room coldPassage = new Room("Cold Passage", GameStrings.DESC_COLD_PASSAGE, Location.COLD_PASSAGE);
        coldPassage.addExit(Action.SOUTH, mirror_cold);
        coldPassage.addExit(Action.WEST, cold_slide);
        
        Room slideRoom = new Room("Slide Room", GameStrings.DESC_SLIDE_ROOM, Location.SLIDE_ROOM);
        slideRoom.addExit(Action.EAST, cold_slide);
        slideRoom.addExit(Action.DOWN, slide_cellar);
        slideRoom.addExit(Action.NORTH, slide_mine_entrance);
        
        Room mineEntrance = new Room("Mine Entrance", GameStrings.DESC_MINE_ENTRANCE, Location.MINE_ENTRANCE);
        mineEntrance.addExit(Action.SOUTH, slide_mine_entrance);
        mineEntrance.addExit(Action.WEST, mine_entrance_squeaky);
        
        Room squeakyRoom = new Room("Squeaky Room", GameStrings.DESC_SQUEAKY_ROOM, Location.SQUEAKY_ROOM);
        squeakyRoom.addExit(Action.EAST, mine_entrance_squeaky);
        squeakyRoom.addExit(Action.NORTH, squeaky_bat);
        
        Room batRoom = new Room("Bat Room", GameStrings.DESC_BAT_ROOM, Location.BAT_ROOM);
        batRoom.addExit(Action.SOUTH, squeaky_bat);
        batRoom.addExit(Action.EAST, bat_shaft);
        
        Room shaftRoom = new Room("Shaft Room", GameStrings.DESC_SHAFT_ROOM, Location.SHAFT_ROOM);
        shaftRoom.addExit(Action.WEST, bat_shaft);
        shaftRoom.addExit(Action.NORTH, shaft_smelly);
        
        Room smellyRoom = new Room("Smelly Room", GameStrings.DESC_SMELLY_ROOM, Location.SMELLY_ROOM);
        smellyRoom.addExit(Action.SOUTH, shaft_smelly);
        smellyRoom.addExit(Action.DOWN, smelly_gas);
        
        Room gasRoom = new Room("Gas Room", GameStrings.DESC_GAS_ROOM, Location.GAS_ROOM);
        gasRoom.addExit(Action.UP, smelly_gas);
        gasRoom.addExit(Action.EAST, gas_coal_1);
        
        Room coalMine1 = new Room("Coal Mine", GameStrings.DESC_COAL_MINE_1, Location.COAL_MINE_1);
        coalMine1.addExit(Action.NORTH, gas_coal_1);
        coalMine1.addExit(Action.NORTHEAST, coal_1_coal_2);
        coalMine1.addExit(Action.EAST, coal_1_self);
        
        Room coalMine2 = new Room("Coal Mine", GameStrings.DESC_COAL_MINE_2, Location.COAL_MINE_2);
        coalMine2.addExit(Action.SOUTH, coal_1_coal_2);
        coalMine2.addExit(Action.NORTH, coal_2_self);
        coalMine2.addExit(Action.SOUTHEAST, coal_2_coal_3);
        
        Room coalMine3 = new Room("Coal Mine", GameStrings.DESC_COAL_MINE_3, Location.COAL_MINE_3);
        coalMine3.addExit(Action.EAST, coal_2_coal_3);
        coalMine3.addExit(Action.SOUTHWEST, coal_3_coal_4);
        coalMine3.addExit(Action.SOUTH, coal_3_self);
        
        Room coalMine4 = new Room("Coal Mine", GameStrings.DESC_COAL_MINE_4, Location.COAL_MINE_4);
        coalMine4.addExit(Action.NORTH, coal_3_coal_4);
        coalMine4.addExit(Action.DOWN, coal_4_ladder_top);
        coalMine4.addExit(Action.WEST, coal_4_self);
        
        Room ladderTop = new Room("Ladder Top", GameStrings.DESC_LADDER_TOP, Location.LADDER_TOP);
        ladderTop.addExit(Action.UP, coal_4_ladder_top);
        ladderTop.addExit(Action.DOWN, ladder_top_bottom);
        
        Room ladderBottom = new Room("Ladder Bottom", GameStrings.DESC_LADDER_BOTTOM, Location.LADDER_BOTTOM);
        ladderBottom.addExit(Action.UP, ladder_top_bottom);
        ladderBottom.addExit(Action.WEST, ladder_bottom_timber);
        ladderBottom.addExit(Action.SOUTH, ladder_bottom_dead_end);
        
        Room deadEndCoalMine = new Room("Dead End", GameStrings.DESC_DEAD_END_COAL_MINE, Location.DEAD_END_COAL_MINE);
        deadEndCoalMine.addExit(Action.NORTH, ladder_bottom_dead_end);
        
        Room timberRoom = new Room("Timber Room", GameStrings.DESC_TIMBER_ROOM, Location.TIMBER_ROOM);
        timberRoom.addExit(Action.EAST, ladder_bottom_timber);
        timberRoom.addExit(Action.WEST, timber_drafty);
        
        Room draftyRoom = new Room("Drafty Room", GameStrings.DESC_DRAFTY_ROOM, Location.DRAFTY_ROOM);
        draftyRoom.addExit(Action.EAST, timber_drafty);
        draftyRoom.addExit(Action.SOUTH, drafty_machine);
        
        Room machineRoom = new Room("Machine Room", GameStrings.DESC_MACHINE_ROOM, Location.MACHINE_ROOM);
        machineRoom.addExit(Action.NORTH, drafty_machine);
        
        Room gratingRoom = new Room("Grating Room", GameStrings.DESC_GRATING_ROOM, Location.GRATING_ROOM);
        gratingRoom.addExit(Action.UP, grating_clearing);
        gratingRoom.addExit(Action.SOUTHWEST, maze11_grating);
        
        Room cyclopsRoom = new Room("Cyclops Room", GameStrings.DESC_CYCLOPS_ROOM, Location.CYCLOPS_ROOM);
        cyclopsRoom.addExit(Action.NORTHWEST, maze15_cyclops);
        cyclopsRoom.addExit(Action.EAST, cyclops_strange);
        cyclopsRoom.addExit(Action.UP, cyclops_treasure);
        
        Room strangePassage = new Room("Strange Passage", GameStrings.DESC_STRANGE_PASSAGE, Location.STRANGE_PASSAGE);
        strangePassage.addExit(Action.WEST, cyclops_strange);
        strangePassage.addExit(Action.EAST, strange_living_room);
        
        Room treasureRoom = new Room("Treasure Room", GameStrings.DESC_TREASURE_ROOM, Location.TREASURE_ROOM);
        treasureRoom.addExit(Action.DOWN, cyclops_treasure);
        
        Room maze1 = new Room("Maze", GameStrings.DESC_MAZE_1, Location.MAZE_1);
        maze1.addExit(Action.EAST, troll_maze);
        maze1.addExit(Action.NORTH, maze1_self);
        maze1.addExit(Action.SOUTH, maze1_maze2);
        maze1.addExit(Action.WEST, maze1_maze4);
        
        Room maze2 = new Room("Maze", GameStrings.DESC_MAZE_2, Location.MAZE_2);
        maze2.addExit(Action.SOUTH, maze1_maze2);
        maze2.addExit(Action.EAST, maze2_maze3);
        maze2.addExit(Action.DOWN, maze2_maze4);
        
        Room maze3 = new Room("Maze", GameStrings.DESC_MAZE_3, Location.MAZE_3);
        maze3.addExit(Action.WEST, maze2_maze3);
        maze3.addExit(Action.NORTH, maze3_maze4);
        maze3.addExit(Action.UP, maze3_maze5);
        
        Room maze4 = new Room("Maze", GameStrings.DESC_MAZE_4, Location.MAZE_4);
        maze4.addExit(Action.WEST, maze3_maze4);
        maze4.addExit(Action.NORTH, maze1_maze4);
        maze4.addExit(Action.EAST, maze4_dead_end);
        
        Room maze5 = new Room("Maze", GameStrings.DESC_MAZE_5, Location.MAZE_5);
        maze5.addExit(Action.NORTH, maze3_maze5);
        maze5.addExit(Action.EAST, maze5_dead_end);
        maze5.addExit(Action.SOUTHWEST, maze5_maze6);
        
        Room maze6 = new Room("Maze", GameStrings.DESC_MAZE_6, Location.MAZE_6);
        maze6.addExit(Action.DOWN, maze5_maze6);
        maze6.addExit(Action.EAST, maze6_maze7);
        maze6.addExit(Action.WEST, maze6_self);
        maze6.addExit(Action.UP, maze6_maze9);
        
        Room maze7 = new Room("Maze", GameStrings.DESC_MAZE_7, Location.MAZE_7);
        maze7.addExit(Action.DOWN, maze7_dead_end);
        maze7.addExit(Action.WEST, maze6_maze7);
        maze7.addExit(Action.EAST, maze7_maze8);
        maze7.addExit(Action.SOUTH, maze7_maze15);
        maze7.addExit(Action.UP, maze7_maze14);
        
        Room maze8 = new Room("Maze", GameStrings.DESC_MAZE_8, Location.MAZE_8);
        maze8.addExit(Action.NORTHEAST, maze7_maze8);
        maze8.addExit(Action.SOUTHEAST, maze8_dead_end);
        maze8.addExit(Action.WEST, maze8_self);
        
        Room maze9 = new Room("Maze", GameStrings.DESC_MAZE_9, Location.MAZE_9);
        maze9.addExit(Action.NORTH, maze6_maze9);
        maze9.addExit(Action.DOWN, maze9_maze11);
        maze9.addExit(Action.EAST, maze9_maze10);
        maze9.addExit(Action.SOUTH, maze9_maze13);
        maze9.addExit(Action.WEST, maze9_maze12);
        maze9.addExit(Action.NORTHWEST, maze9_self);
        
        Room maze10 = new Room("Maze", GameStrings.DESC_MAZE_10, Location.MAZE_10);
        maze10.addExit(Action.EAST, maze9_maze10);
        maze10.addExit(Action.UP, maze10_maze11);
        maze10.addExit(Action.WEST, maze10_maze13);
        
        Room maze11 = new Room("Maze", GameStrings.DESC_MAZE_11, Location.MAZE_11);
        maze11.addExit(Action.DOWN, maze10_maze11);
        maze11.addExit(Action.SOUTHWEST, maze11_maze12);
        maze11.addExit(Action.NORTHWEST, maze11_maze13);
        maze11.addExit(Action.NORTHEAST, maze11_grating);
        
        Room maze12 = new Room("Maze", GameStrings.DESC_MAZE_12, Location.MAZE_12);
        maze12.addExit(Action.EAST, maze12_maze13);
        maze12.addExit(Action.UP, maze9_maze12);
        maze12.addExit(Action.NORTH, maze12_dead_end);
        maze12.addExit(Action.DOWN, maze12_maze5);
        maze12.addExit(Action.SOUTHWEST, maze11_maze12);
        
        Room maze13 = new Room("Maze", GameStrings.DESC_MAZE_13, Location.MAZE_13);
        maze13.addExit(Action.EAST, maze9_maze13);
        maze13.addExit(Action.DOWN, maze12_maze13);
        maze13.addExit(Action.WEST, maze11_maze13);
        maze13.addExit(Action.SOUTH, maze10_maze13);
        
        Room maze14 = new Room("Maze", GameStrings.DESC_MAZE_14, Location.MAZE_14);
        maze14.addExit(Action.NORTHWEST, maze14_self);
        maze14.addExit(Action.WEST, maze14_maze15);
        maze14.addExit(Action.NORTHEAST, maze7_maze14);
        maze14.addExit(Action.SOUTH, maze7_maze14);
        
        Room maze15 = new Room("Maze", GameStrings.DESC_MAZE_15, Location.MAZE_15);
        maze15.addExit(Action.WEST, maze14_maze15);
        maze15.addExit(Action.SOUTH, maze7_maze15);
        maze15.addExit(Action.SOUTHEAST, maze15_cyclops);
        
        Room mazeDeadEndNorth = new Room("Dead End", GameStrings.DESC_DEAD_END_MAZE_NORTH, Location.DEAD_END_MAZE_NORTH);
        mazeDeadEndNorth.addExit(Action.SOUTH, maze4_dead_end);
        
        Room mazeDeadEndCenter = new Room("Dead End", GameStrings.DESC_DEAD_END_MAZE_CENTER, Location.DEAD_END_MAZE_CENTER);
        mazeDeadEndCenter.addExit(Action.WEST, maze5_dead_end);

        Room mazeDeadEndSouthEast = new Room("Dead End", GameStrings.DESC_DEAD_END_MAZE_SOUTHEAST, Location.DEAD_END_MAZE_SOUTHEAST);
        mazeDeadEndSouthEast.addExit(Action.NORTH, maze8_dead_end); 

        Room mazeDeadEndSouthWest = new Room("Dead End", GameStrings.DESC_DEAD_END_MAZE_SOUTHWEST, Location.DEAD_END_MAZE_SOUTHWEST);
        mazeDeadEndSouthWest.addExit(Action.SOUTH, maze12_dead_end);

        // Dark rooms
        attic.setDark();

        // Gaseous rooms
        // Closed passages

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

        state.worldMap.put(cellar.roomID, cellar);
        state.worldMap.put(eastOfChasm.roomID, eastOfChasm);
        state.worldMap.put(gallery.roomID, gallery);
        state.worldMap.put(studio.roomID, studio);
        state.worldMap.put(trollRoom.roomID, trollRoom);
        state.worldMap.put(eastWestPassage.roomID, eastWestPassage);
        state.worldMap.put(roundRoom.roomID, roundRoom);
        state.worldMap.put(narrowPassage.roomID, narrowPassage);
        state.worldMap.put(mirrorRoomSouth.roomID, mirrorRoomSouth);
        state.worldMap.put(windingPassage.roomID, windingPassage);
        state.worldMap.put(caveSouth.roomID, caveSouth);
        state.worldMap.put(entranceToHades.roomID, entranceToHades);
        state.worldMap.put(landOfTheDead.roomID, landOfTheDead);
        state.worldMap.put(engravingsCave.roomID, engravingsCave);
        state.worldMap.put(domeRoom.roomID, domeRoom);
        state.worldMap.put(torchRoom.roomID, torchRoom);
        state.worldMap.put(temple.roomID, temple);
        state.worldMap.put(egyptianRoom.roomID, egyptianRoom);
        state.worldMap.put(altar.roomID, altar);
        state.worldMap.put(loudRoom.roomID, loudRoom);
        state.worldMap.put(dampCave.roomID, dampCave);
        state.worldMap.put(whiteCliffsBeachNorth.roomID, whiteCliffsBeachNorth);
        state.worldMap.put(whiteCliffsBeachSouth.roomID, whiteCliffsBeachSouth);
        state.worldMap.put(frigidRiver1.roomID, frigidRiver1);
        state.worldMap.put(frigidRiver2.roomID, frigidRiver2);
        state.worldMap.put(frigidRiver3.roomID, frigidRiver3);
        state.worldMap.put(frigidRiver4.roomID, frigidRiver4);
        state.worldMap.put(frigidRiver5.roomID, frigidRiver5);
        state.worldMap.put(sandyCave.roomID, sandyCave);
        state.worldMap.put(sandyBeach.roomID, sandyBeach);
        state.worldMap.put(shore.roomID, shore);
        state.worldMap.put(aragainFalls.roomID, aragainFalls);
        state.worldMap.put(onTheRainbow.roomID, onTheRainbow);
        state.worldMap.put(dam.roomID, dam);
        state.worldMap.put(damBase.roomID, damBase);
        state.worldMap.put(damLobby.roomID, damLobby);
        state.worldMap.put(maintenanceRoom.roomID, maintenanceRoom);
        state.worldMap.put(northSouthPassage.roomID, northSouthPassage);
        state.worldMap.put(chasm.roomID, chasm);
        state.worldMap.put(deepCanyon.roomID, deepCanyon);
        state.worldMap.put(reservoirSouth.roomID, reservoirSouth);
        state.worldMap.put(reservoir.roomID, reservoir);
        state.worldMap.put(reservoirNorth.roomID, reservoirNorth);
        state.worldMap.put(streamView.roomID, streamView);
        state.worldMap.put(stream.roomID, stream);
        state.worldMap.put(atlantisRoom.roomID, atlantisRoom);
        state.worldMap.put(caveNorth.roomID, caveNorth);
        state.worldMap.put(twistingPassage.roomID, twistingPassage);
        state.worldMap.put(mirrorRoomNorth.roomID, mirrorRoomNorth);
        state.worldMap.put(coldPassage.roomID, coldPassage);
        state.worldMap.put(slideRoom.roomID, slideRoom);
        state.worldMap.put(mineEntrance.roomID, mineEntrance);
        state.worldMap.put(squeakyRoom.roomID, squeakyRoom);
        state.worldMap.put(batRoom.roomID, batRoom);
        state.worldMap.put(shaftRoom.roomID, shaftRoom);
        state.worldMap.put(smellyRoom.roomID, smellyRoom);
        state.worldMap.put(gasRoom.roomID, gasRoom);
        state.worldMap.put(coalMine1.roomID, coalMine1);
        state.worldMap.put(coalMine2.roomID, coalMine2);
        state.worldMap.put(coalMine3.roomID, coalMine3);
        state.worldMap.put(coalMine4.roomID, coalMine4);
        state.worldMap.put(ladderTop.roomID, ladderTop);
        state.worldMap.put(ladderBottom.roomID, ladderBottom);
        state.worldMap.put(deadEndCoalMine.roomID, deadEndCoalMine);
        state.worldMap.put(timberRoom.roomID, timberRoom);
        state.worldMap.put(draftyRoom.roomID, draftyRoom);
        state.worldMap.put(machineRoom.roomID, machineRoom);
        state.worldMap.put(maze1.roomID, maze1);
        state.worldMap.put(maze2.roomID, maze2);
        state.worldMap.put(maze3.roomID, maze3);
        state.worldMap.put(maze4.roomID, maze4);
        state.worldMap.put(maze5.roomID, maze5);
        state.worldMap.put(maze6.roomID, maze6);
        state.worldMap.put(maze7.roomID, maze7);
        state.worldMap.put(maze8.roomID, maze8);
        state.worldMap.put(maze9.roomID, maze9);
        state.worldMap.put(maze10.roomID, maze10);
        state.worldMap.put(maze11.roomID, maze11);
        state.worldMap.put(maze12.roomID, maze12);
        state.worldMap.put(maze13.roomID, maze13);
        state.worldMap.put(maze14.roomID, maze14);
        state.worldMap.put(maze15.roomID, maze15);
        state.worldMap.put(mazeDeadEndNorth.roomID, mazeDeadEndNorth);
        state.worldMap.put(mazeDeadEndCenter.roomID, mazeDeadEndCenter);
        state.worldMap.put(mazeDeadEndSouthWest.roomID, mazeDeadEndSouthWest);
        state.worldMap.put(mazeDeadEndSouthEast.roomID, mazeDeadEndSouthEast);
        state.worldMap.put(gratingRoom.roomID, gratingRoom);
        state.worldMap.put(cyclopsRoom.roomID, cyclopsRoom);
        state.worldMap.put(treasureRoom.roomID, treasureRoom);
        state.worldMap.put(strangePassage.roomID, strangePassage);



        // end world map creation
    }

    private static void createGameObjects(GameState state)
    {

    }


	private static boolean parsePlayerInput(GameState state, String playerText)
	{
        /* ACTION OBJECT OBJECT.

        Not assigning direct or indirect objects in this method, or validating anything else.
        Just checking for 1 to 3 game-recognized phrases. Look for one, remove it, if there is more in the string,
        check for another phrase.

        */
		state.resetInput();
        state.phrase = playerText;

		
        // Method fails if any word the player typed is not known by the game.
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
        playerText = playerText.replaceAll(" in ", " ");
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

        /* TESTING */
        if (TESTING)
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
            case OPEN_CLOSE:
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
            case OPEN_CLOSE:
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
            case PLACE_REMOVE:
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




	private static void updateGame(GameState state)
	{
		
		Location currentLocation = state.playerLocation;
		Room currentRoom = state.worldMap.get(currentLocation);

		Action currentAction = state.playerAction;

        GameObject obj = state.directObject;

		GameObject indObj = state.indirectObject;

        /* TESTING */
		if (TESTING)
		{
			output("Selected action is " + currentAction);
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

        boolean dark = (currentRoom.isDark() && !state.lightActivated);

        if (!dark) state.darknessTurns = 0;

       


		switch (currentAction)
		{



            // These actions will activate the object's lambda method.

            case LIGHT:
            {
                obj.activate(state, Action.LIGHT);
                currentRoom.lookAround(state);
            } break;

            case UNLIGHT:
            {
                obj.activate(state, Action.UNLIGHT);
                currentRoom.lookAround(state);
            } break;
            
			case ACTIVATE:
			case RING:
			case PLAY:
			case KICK:
			case READ:
			case TIE:
			case ATTACK:
            case MOVE_OBJECT:
			{
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }

                obj.activate(state, currentAction);

			} break;

            


            // Specific actions involving an object.

            case EXAMINE:
            {
                if (dark)
                {
                    output(GameStrings.TOO_DARK);
                    break;
                }
                obj.examine(state);
            } break;

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
						output(item.name);
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
                output("Wheeeeeeee!");
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

            case TELEPORT:
            {
                Scanner console = new Scanner(System.in);

                output("Where do you want to go? ");
                String dest = console.nextLine();

                Room room = null;

                for (Room r : state.worldMap.values())
                {
                    if (dest.equals(r.name));
                        room = r;
                }

                if (room != null)
                {
                    state.playerLocation = room.roomID;
                    room.lookAround(state);
                }

                else
                {
                    output("Room not found.");
                }

            } break;

			default: {} break;
		}

		// The player's action could end the game before anything else happens.

        if (!state.playerAlive) gameover = true;

		if (gameover) return;

        // The actors get to take their turns
        for (GameObject objAct : state.objectList.values())
        {
            if (objAct.isActor() && objAct.isAlive())
            {
                objAct.actorTurn();
            }
        }

		state.addTurn();

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

        // Simple actions: no interaction with game objects
		actions.put("quit",  Action.QUIT);
		actions.put("q",     Action.QUIT);
		actions.put("jump",  Action.JUMP);
		actions.put("look around",  Action.LOOK);
		actions.put("look",  Action.LOOK);
		actions.put("l",     Action.LOOK);
        actions.put("examine", Action.EXAMINE);
        actions.put("look at", Action.EXAMINE);
        actions.put("l at", Action.EXAMINE);
		actions.put("inventory", Action.INVENTORY);
		actions.put("i",         Action.INVENTORY);
		actions.put("fuck",  Action.PROFANITY);
		actions.put("shit",  Action.PROFANITY);
		actions.put("shout", Action.SHOUT);
		actions.put("yell",  Action.SHOUT);
		actions.put("scream",  Action.SHOUT);
		actions.put("wait", Action.WAIT);
        actions.put("author", Action.AUTHOR);
        actions.put("teleport", Action.TELEPORT);


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
        actions.put("put", Action.PLACE);
        actions.put("place", Action.PLACE);

        // Combat actions
        actions.put("kick", Action.KICK);
        actions.put("hit", Action.ATTACK);
        actions.put("attack", Action.ATTACK);
        actions.put("punch", Action.ATTACK);


        // Special actions
        actions.put("move", Action.MOVE_OBJECT);
		actions.put("say", Action.SPEAK);
        actions.put("play", Action.PLAY);
		actions.put("ring", Action.RING);
        actions.put("light", Action.LIGHT);
        actions.put("turn on", Action.LIGHT);
        actions.put("turn off", Action.UNLIGHT);
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
        actionTypes.put(Action.TELEPORT, ActionType.REFLEXIVE);

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

        actionTypes.put(Action.TAKE, ActionType.DIRECT);
        actionTypes.put(Action.MOVE_OBJECT, ActionType.DIRECT);
        actionTypes.put(Action.DROP, ActionType.DIRECT);
        actionTypes.put(Action.STORE, ActionType.DIRECT);
        actionTypes.put(Action.LIGHT, ActionType.DIRECT);
        actionTypes.put(Action.UNLIGHT, ActionType.DIRECT);
        actionTypes.put(Action.PLACE, ActionType.PLACE_REMOVE);

        actionTypes.put(Action.OPEN, ActionType.OPEN_CLOSE);
        actionTypes.put(Action.CLOSE, ActionType.OPEN_CLOSE);

        actionTypes.put(Action.EXAMINE, ActionType.DIRECT);
        actionTypes.put(Action.READ, ActionType.DIRECT);
        actionTypes.put(Action.KICK, ActionType.DIRECT);
        actionTypes.put(Action.PLAY, ActionType.DIRECT);
        actionTypes.put(Action.RING, ActionType.DIRECT);
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

    private static void fillCurrentObjectList(GameState state)
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
				output("\nWhat?\n");
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