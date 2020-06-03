import java.util.Random;
import java.util.HashMap;


/**
 * A location is any place a moveable object can exist.
 *
 * Overworld
 * GUE, south area
 * GUE, dam/river area
 * GUE, north area
 * GUE, coal mine
 * GUE, maze
 * 
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


    INSIDE_BIRDS_NEST,
    INSIDE_BUOY,
    INSIDE_COFFIN,
    INSIDE_MAILBOX,
    INSIDE_TROPHY_CASE,
    INSIDE_SACK,
    INSIDE_BOAT,
    INSIDE_BASKET,
    INSIDE_BOTTLE,
    INSIDE_TUBE,
    INSIDE_COAL_MACHINE,

    ON_KITCHEN_TABLE,
    ON_ATTIC_TABLE,
    ON_PEDESTAL,


    PLAYER_INVENTORY,
    THIEF_INVENTORY,
    TROLL_INVENTORY,
    CYCLOPS_INVENTORY,

    NULL_INVENTORY,
    NULL_LOCATION

    }


/**
 * Actions:
 * Directional
 * Reflexive
 * Direct
 * Indirect
 * Indirect Inverse
 * Godmode
 */
enum Action {


    NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST,
    UP, DOWN, IN, OUT, WALK, GO, SLIDE, SWIM, EXIT, CROSS,
   
    AUTHOR, BRIEF, DEFEND, DIAGNOSE, INVENTORY, JUMP, LOOK, PRAY,
    PROFANITY, QUIT, RESTART, RESTORE, SAVE, SAY, SCORE, SHOUT,
    STAY, SUPERBRIEF, VERBOSE, WAIT,
  
    ACTIVATE, ANSWER, BLOW, CLIMB, CLOSE, COUNT,
    DEFLATE, DRINK, DROP, EAT, ENTER, EXAMINE, EXTINGUISH,
    FOLLOW, KICK, KNOCK, LAUNCH, LIGHT, LISTEN,
    LOWER, MOVE_OBJECT, OPEN, PLAY, POUR, PULL, PUSH, RAISE,
    READ, RING, SEARCH, SHAKE, SMELL, STRIKE, TAKE, TALK_TO,
    TOUCH, WAKE, WAVE, WEAR, WIND,

    ATTACK, BREAK, BURN, CUT, DIG, FILL, INFLATE, LOCK,
    TURN, UNLOCK,

    GIVE, PUT, TIE, THROW, 

    ACCIO,
    TELEPORT,
    
    AGAIN,
    NULL_ACTION

    }

enum ActionType {

    NULL_TYPE,
    REFLEXIVE,
    DIRECT,
    INDIRECT,
    INDIRECT_INVERSE,
    EXIT
    }

enum ObjectType {

    NULL_TYPE,
    FEATURE,
    ITEM,
    ACTOR,
    CONTAINER,
    SURFACE
    }


class GameSetup {

    private GameState state;
    private boolean godmode;
    private boolean debug;

    public GameSetup(GameState state, boolean godmode, boolean debug)
    {
        this.state = state;
        this.godmode = godmode;
        this.debug = debug;

        createWorldMap();
        createGameObjects();
        createActions();
        fillDictionary();
    }

    @SuppressWarnings("unused")
	public void createWorldMap()
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


        


        // Rooms: Name, description, ID
        Room westOfHouse = new Room("West of House", MapStrings.DESC_WEST_OF_HOUSE, Location.WEST_OF_HOUSE);
        westOfHouse.addExit(Action.NORTH, house_west_north);
        westOfHouse.addExit(Action.NORTHEAST, house_west_north);
        westOfHouse.addExit(Action.SOUTH, house_west_south);
        westOfHouse.addExit(Action.SOUTHEAST, house_west_south);
        westOfHouse.addExit(Action.SOUTHWEST, house_west_barrow);
        westOfHouse.addExit(Action.WEST, house_west_forestW);
        westOfHouse.addFailMessage(Action.EAST, "The door is boarded and you can't remove the boards.");

        Room northOfHouse = new Room("North of House", MapStrings.DESC_NORTH_OF_HOUSE, Location.NORTH_OF_HOUSE);
        northOfHouse.addExit(Action.NORTH, house_north_forestpath);
        northOfHouse.addExit(Action.EAST, house_north_behind);
        northOfHouse.addExit(Action.SOUTHEAST, house_north_behind);
        northOfHouse.addExit(Action.SOUTHWEST, house_west_north);
        northOfHouse.addExit(Action.WEST, house_west_north);
        northOfHouse.addFailMessage(Action.SOUTH, "The windows are all boarded.");


        Room behindHouse = new Room("Behind House", MapStrings.DESC_BEHIND_HOUSE, Location.BEHIND_HOUSE);
        behindHouse.addExit(Action.NORTH, house_north_behind);
        behindHouse.addExit(Action.NORTHWEST, house_north_behind);
        behindHouse.addExit(Action.EAST, house_behind_clearingE);
        behindHouse.addExit(Action.SOUTH, house_behind_south);
        behindHouse.addExit(Action.SOUTHWEST, house_behind_south);
        behindHouse.addExit(Action.WEST, house_behind_kitchen);
        behindHouse.addExit(Action.IN, house_behind_kitchen);


        Room southOfHouse = new Room("South of House", MapStrings.DESC_SOUTH_OF_HOUSE, Location.SOUTH_OF_HOUSE);
        southOfHouse.addExit(Action.EAST, house_behind_south);
        southOfHouse.addExit(Action.NORTHEAST, house_behind_south);
        southOfHouse.addExit(Action.WEST, house_west_south);
        southOfHouse.addExit(Action.NORTHWEST, house_west_south);
        southOfHouse.addExit(Action.SOUTH, house_south_forestS);
        southOfHouse.addFailMessage(Action.NORTH, "The windows are all boarded.");


        Room kitchen = new Room("Kitchen", MapStrings.DESC_KITCHEN_WINDOW_CLOSED, Location.KITCHEN);
        kitchen.addExit(Action.EAST, house_behind_kitchen);
        kitchen.addExit(Action.OUT, house_behind_kitchen);
        kitchen.addExit(Action.WEST, kitchen_livingroom);
        kitchen.addExit(Action.UP, kitchen_attic);
        kitchen.addFailMessage(Action.DOWN, "Only Santa Claus climbs down chimneys.");

        Room attic = new Room("Attic", MapStrings.DESC_ATTIC, Location.ATTIC);
        attic.addExit(Action.DOWN, kitchen_attic);
        

        Room livingRoom = new Room("Living Room", MapStrings.DESC_LIVING_ROOM, Location.LIVING_ROOM);
        livingRoom.addExit(Action.EAST, kitchen_livingroom);
        livingRoom.addExit(Action.DOWN, cellar_livingroom);
        livingRoom.addExit(Action.WEST, strange_living_room);

        Room forestPath = new Room("Forest Path", MapStrings.DESC_FOREST_PATH, Location.FOREST_PATH);
        forestPath.addExit(Action.NORTH, forestpath_clearingN);
        forestPath.addExit(Action.EAST, forestpath_forestE);
        forestPath.addExit(Action.SOUTH, house_north_forestpath);
        forestPath.addExit(Action.WEST, forestpath_forestW);
        forestPath.addExit(Action.UP, forestpath_uptree);

        Room upTree = new Room("Up a Tree", MapStrings.DESC_UP_TREE, Location.UP_TREE);
        upTree.addExit(Action.DOWN, forestpath_uptree);
        upTree.addFailMessage(Action.UP, "You cannot climb any higher.");

        Room forestWest = new Room("Forest", MapStrings.DESC_FOREST_WEST, Location.FOREST_WEST);
        forestWest.addExit(Action.NORTH, clearingN_forestW);
        forestWest.addExit(Action.EAST, forestpath_forestW);
        forestWest.addExit(Action.SOUTH, forestS_forestW);
        forestWest.addFailMessage(Action.WEST, "You would need a machete to go further west.");
        forestWest.addFailMessage(Action.UP, "There is no tree here suitable for climbing.");

        Room forestEast = new Room("Forest", MapStrings.DESC_FOREST_EAST, Location.FOREST_EAST);
        forestEast.addExit(Action.EAST, forestE_forestNE);
        forestEast.addExit(Action.SOUTH, forestE_clearingE);
        forestEast.addExit(Action.WEST, forestpath_forestE);
        forestEast.addFailMessage(Action.NORTH, "The forest becomes impenetrable to the north.");
        forestEast.addFailMessage(Action.UP, "There is no tree here suitable for climbing.");

        Room forestNortheast = new Room("Forest", MapStrings.DESC_FOREST_NORTHEAST, Location.FOREST_NORTHEAST);
        forestNortheast.addExit(Action.NORTH, forestE_forestNE);
        forestNortheast.addExit(Action.SOUTH, forestE_forestNE);
        forestNortheast.addExit(Action.WEST, forestE_forestNE);
        forestNortheast.addFailMessage(Action.EAST, MapStrings.FOREST_NE_FAIL_1);
        forestNortheast.addFailMessage(Action.UP, MapStrings.FOREST_NE_FAIL_1);

        Room forestSouth = new Room("Forest", MapStrings.DESC_FOREST_SOUTH, Location.FOREST_SOUTH);
        forestSouth.addExit(Action.NORTH, clearingE_forestS);
        forestSouth.addExit(Action.WEST, forestS_forestW);
        forestSouth.addExit(Action.NORTHWEST, house_south_forestS);
        forestSouth.addFailMessage(Action.UP, "There is no tree here suitable for climbing.");
        forestSouth.addFailMessage(Action.EAST, "The rank undergrowth prevents eastward movement.");
        forestSouth.addFailMessage(Action.SOUTH, "Storm-tossed trees block your way.");


        Room clearingNorth = new Room("Clearing", MapStrings.DESC_CLEARING_NORTH, Location.CLEARING_NORTH);
        clearingNorth.addExit(Action.EAST, clearingN_forestE);
        clearingNorth.addExit(Action.SOUTH, forestpath_clearingN);
        clearingNorth.addExit(Action.WEST, clearingN_forestW);
        clearingNorth.addExit(Action.DOWN, grating_clearing);
        clearingNorth.addFailMessage(Action.UP, "There is no tree here suitable for climbing.");
        clearingNorth.addFailMessage(Action.NORTH, "The forest becomes impenetrable to the north.");

        Room clearingEast = new Room("Clearing", MapStrings.DESC_CLEARING_EAST, Location.CLEARING_EAST);
        clearingEast.addExit(Action.NORTH, forestE_clearingE);
        clearingEast.addExit(Action.EAST, clearingE_canyon);
        clearingEast.addExit(Action.SOUTH, clearingE_forestS);
        clearingEast.addExit(Action.WEST, house_behind_clearingE);
        clearingEast.addFailMessage(Action.UP, "There is no tree here suitable for climbing.");


        Room canyonView = new Room("Canyon View", MapStrings.DESC_CANYON_VIEW, Location.CANYON_VIEW);
        canyonView.addExit(Action.NORTHWEST, clearingE_canyon);
        canyonView.addExit(Action.WEST, forestS_canyon);
        canyonView.addExit(Action.DOWN, canyon_ledge);
        canyonView.addExit(Action.EAST, canyon_ledge);
        canyonView.addFailMessage(Action.SOUTH, "Storm-tossed trees block your way.");

        Room rockyLedge = new Room("Rocky Ledge", MapStrings.DESC_ROCKY_LEDGE, Location.ROCKY_LEDGE);
        rockyLedge.addExit(Action.UP, canyon_ledge);
        rockyLedge.addExit(Action.DOWN, ledge_bottom);

        Room canyonBottom = new Room("Canyon Bottom", MapStrings.DESC_CANYON_BOTTOM, Location.CANYON_BOTTOM);
        canyonBottom.addExit(Action.UP, ledge_bottom);
        canyonBottom.addExit(Action.NORTH, canyon_bottom_rainbow);

        Room endOfRainbow = new Room("End of Rainbow", MapStrings.DESC_END_OF_RAINBOW, Location.END_OF_RAINBOW);
        endOfRainbow.addExit(Action.SOUTHWEST, canyon_bottom_rainbow);
        endOfRainbow.addExit(Action.EAST, rainbow_end);

        Room stoneBarrow = new Room("Stone Barrow", MapStrings.DESC_STONE_BARROW, Location.STONE_BARROW);
        stoneBarrow.addExit(Action.NORTHEAST, house_west_barrow);
        stoneBarrow.addExit(Action.WEST, barrowInside);

        Room insideStoneBarrow = new Room("Inside Stone Barrow", MapStrings.DESC_INSIDE_STONE_BARROW, Location.INSIDE_STONE_BARROW);
        insideStoneBarrow.addExit(Action.EAST, barrowInside);

        Room cellar = new Room("Cellar", MapStrings.DESC_CELLAR, Location.CELLAR);
        cellar.addExit(Action.NORTH, cellar_troll);
        cellar.addExit(Action.SOUTH, cellar_eastchasm);
        // This exit will be closed by the cyclops until he has been chased off. (in its actor method).
        cellar.addExit(Action.UP, cellar_livingroom);
        cellar.addFailMessage(Action.WEST, "You try to ascend the ramp, but it is impossible, and you slide back down.");
        

        Room eastOfChasm = new Room("East of Chasm", MapStrings.DESC_EAST_OF_CHASM, Location.EAST_OF_CHASM);
        eastOfChasm.addExit(Action.NORTH, cellar_eastchasm);
        eastOfChasm.addExit(Action.DOWN, cellar_eastchasm);
        eastOfChasm.addExit(Action.EAST, eastchasm_gallery);
        eastOfChasm.addFailMessage(Action.DOWN, "The chasm probably leads straight to the infernal regions.");

        Room gallery = new Room("Gallery", MapStrings.DESC_GALLERY, Location.GALLERY);
        gallery.addExit(Action.WEST, eastchasm_gallery);
        gallery.addExit(Action.NORTH, gallery_studio);

        Room studio = new Room("Studio", MapStrings.DESC_STUDIO, Location.STUDIO);
        studio.addExit(Action.SOUTH, gallery_studio);
        studio.addExit(Action.UP, studio_kitchen);

        Room trollRoom = new Room("Troll Room", MapStrings.DESC_TROLL_ROOM, Location.TROLL_ROOM);
        trollRoom.addExit(Action.SOUTH, cellar_troll);
        trollRoom.addExit(Action.WEST, troll_maze);
        trollRoom.addExit(Action.EAST, troll_eastwest);

        Room eastWestPassage = new Room("East-West Passage", MapStrings.DESC_EAST_WEST_PASSAGE , Location.EAST_WEST_PASSAGE);
        eastWestPassage.addExit(Action.WEST, troll_eastwest);
        eastWestPassage.addExit(Action.NORTH, eastwest_chasm);
        eastWestPassage.addExit(Action.EAST, eastwest_round);

        Room roundRoom = new Room("Round Room", MapStrings.DESC_ROUND_ROOM, Location.ROUND_ROOM);
        roundRoom.addExit(Action.WEST, eastwest_round);
        roundRoom.addExit(Action.NORTH, round_northsouth);
        roundRoom.addExit(Action.EAST, round_loud);
        roundRoom.addExit(Action.SOUTH, round_narrow);
        roundRoom.addExit(Action.SOUTHEAST, round_engravings);

        Room narrowPassage = new Room("Narrow Passage", MapStrings.DESC_NARROW_PASSAGE, Location.NARROW_PASSAGE);
        narrowPassage.addExit(Action.NORTH, round_narrow);
        narrowPassage.addExit(Action.SOUTH, narrow_mirror);

        Room mirrorRoomSouth = new Room("Mirror Room", MapStrings.DESC_MIRROR_ROOM_SOUTH, Location.MIRROR_ROOM_SOUTH);
        mirrorRoomSouth.addExit(Action.NORTH, narrow_mirror);
        mirrorRoomSouth.addExit(Action.WEST, mirror_winding);
        mirrorRoomSouth.addExit(Action.EAST, mirrorsouth_cave);

        Room windingPassage = new Room("Winding Passage", MapStrings.DESC_WINDING_PASSAGE, Location.WINDING_PASSAGE);
        windingPassage.addExit(Action.NORTH, mirror_winding);
        windingPassage.addExit(Action.EAST, winding_cave);

        Room caveSouth = new Room("Cave", MapStrings.DESC_CAVE_SOUTH, Location.CAVE_SOUTH);
        caveSouth.addExit(Action.NORTH, mirrorsouth_cave);
        caveSouth.addExit(Action.WEST, winding_cave);
        caveSouth.addExit(Action.DOWN, cave_hades);
        caveSouth.addExit(Action.DOWN, cave_hades);

        Room entranceToHades = new Room("Entrance to Hades", MapStrings.DESC_ENTRANCE_TO_HADES, Location.ENTRANCE_TO_HADES);
        entranceToHades.addExit(Action.UP, cave_hades);
        entranceToHades.addExit(Action.SOUTH, hades_land_dead);

        Room landOfTheDead = new Room("Land of the Dead", MapStrings.DESC_LAND_OF_THE_DEAD, Location.LAND_OF_THE_DEAD);
        landOfTheDead.addExit(Action.NORTH, hades_land_dead);

        Room engravingsCave = new Room("Engravings Cave", MapStrings.DESC_ENGRAVINGS_CAVE, Location.ENGRAVINGS_CAVE);
        engravingsCave.addExit(Action.NORTHWEST, round_engravings);
        engravingsCave.addExit(Action.EAST, engravings_dome);

        Room domeRoom = new Room("Dome Room", MapStrings.DESC_DOME_ROOM, Location.DOME_ROOM);
        domeRoom.addExit(Action.WEST, engravings_dome);
        domeRoom.addExit(Action.DOWN, dome_torch);

        Room torchRoom = new Room("Torch Room", MapStrings.DESC_TORCH_ROOM, Location.TORCH_ROOM);
        torchRoom.addExit(Action.SOUTH, torch_temple);
        torchRoom.addExit(Action.DOWN, torch_temple);

        Room temple = new Room("Temple", MapStrings.DESC_TEMPLE, Location.TEMPLE);
        temple.addExit(Action.NORTH, torch_temple);
        temple.addExit(Action.UP, torch_temple);
        temple.addExit(Action.EAST, temple_egypt);
        temple.addExit(Action.SOUTH, temple_altar);

        Room egyptianRoom = new Room("Egyptian Room", MapStrings.DESC_EGYPTIAN_ROOM, Location.EGYPTIAN_ROOM);
        egyptianRoom.addExit(Action.WEST, temple_egypt);

        Room altar = new Room("Altar", MapStrings.DESC_ALTAR, Location.ALTAR);
        altar.addExit(Action.NORTH, temple_altar);
        altar.addExit(Action.DOWN, altar_cave);

        Room loudRoom = new Room("Loud Room", MapStrings.DESC_LOUD_ROOM, Location.LOUD_ROOM);
        loudRoom.addExit(Action.WEST, round_loud);
        loudRoom.addExit(Action.UP, loud_deep_canyon);
        loudRoom.addExit(Action.EAST, loud_damp);

        Room dampCave = new Room("Damp Cave", MapStrings.DESC_DAMP_CAVE, Location.DAMP_CAVE);
        dampCave.addExit(Action.WEST, loud_damp);
        dampCave.addExit(Action.EAST, damp_white_north);

        Room whiteCliffsBeachNorth = new Room("White Cliffs Beach North", MapStrings.DESC_WHITE_CLIFFS_BEACH_NORTH, Location.WHITE_CLIFFS_BEACH_NORTH);
        whiteCliffsBeachNorth.addExit(Action.WEST, damp_white_north);
        whiteCliffsBeachNorth.addExit(Action.SOUTH, white_cliffs_north_south);
        whiteCliffsBeachNorth.addExit(Action.EAST, white_north_river);

        Room whiteCliffsBeachSouth = new Room("White Cliffs Beach South", MapStrings.DESC_WHITE_CLIFFS_BEACH_SOUTH, Location.WHITE_CLIFFS_BEACH_SOUTH);
        whiteCliffsBeachSouth.addExit(Action.NORTH, white_cliffs_north_south);
        whiteCliffsBeachSouth.addExit(Action.EAST, white_south_river);

        Room frigidRiver1 = new Room("Frigid River", MapStrings.DESC_FRIGID_RIVER_1, Location.FRIGID_RIVER_1);
        frigidRiver1.addExit(Action.WEST, dam_base_river);

        Room frigidRiver2 = new Room("Frigid River", MapStrings.DESC_FRIGID_RIVER_2, Location.FRIGID_RIVER_2);
        Room frigidRiver3 = new Room("Frigid River", MapStrings.DESC_FRIGID_RIVER_3, Location.FRIGID_RIVER_3);
        frigidRiver3.addExit(Action.WEST, white_north_river);

        Room frigidRiver4 = new Room("Frigid River", MapStrings.DESC_FRIGID_RIVER_4, Location.FRIGID_RIVER_4);
        frigidRiver4.addExit(Action.WEST, white_south_river);
        frigidRiver4.addExit(Action.EAST, river_sandy_beach);

        Room frigidRiver5 = new Room("Frigid River", MapStrings.DESC_FRIGID_RIVER_5, Location.FRIGID_RIVER_5);
        frigidRiver5.addExit(Action.EAST, river_shore);

        Room sandyCave = new Room("Sandy Cave", MapStrings.DESC_SANDY_CAVE, Location.SANDY_CAVE);
        sandyCave.addExit(Action.SOUTHWEST, sandy_beach_cave);

        Room sandyBeach = new Room("Sandy Beach", MapStrings.DESC_SANDY_BEACH, Location.SANDY_BEACH);
        sandyBeach.addExit(Action.NORTHEAST, sandy_beach_cave);
        sandyBeach.addExit(Action.SOUTH, sandy_beach_shore);
        sandyBeach.addExit(Action.WEST, river_sandy_beach);

        Room shore = new Room("Shore", MapStrings.DESC_SHORE, Location.SHORE);
        shore.addExit(Action.NORTH, sandy_beach_shore);
        shore.addExit(Action.WEST, river_shore);
        shore.addExit(Action.SOUTH, shore_falls);

        Room aragainFalls = new Room("Aragain Falls", MapStrings.DESC_ARAGAIN_FALLS, Location.ARAGAIN_FALLS);
        aragainFalls.addExit(Action.NORTH, shore_falls);
        aragainFalls.addExit(Action.WEST, falls_rainbow);

        Room onTheRainbow = new Room("On the Rainbow", MapStrings.DESC_ON_THE_RAINBOW, Location.ON_THE_RAINBOW);
        onTheRainbow.addExit(Action.EAST, falls_rainbow);
        onTheRainbow.addExit(Action.WEST, rainbow_end);

        Room dam = new Room("Dam", MapStrings.DESC_DAM, Location.DAM);
        dam.addExit(Action.WEST, dam_res_south);
        dam.addExit(Action.NORTH, dam_dam_lobby);
        dam.addExit(Action.SOUTH, dam_deep_canyon);
        dam.addExit(Action.EAST, dam_dam_base);
        dam.addExit(Action.DOWN, dam_dam_base);

        Room damBase = new Room("Dam Base", MapStrings.DESC_DAM_BASE, Location.DAM_BASE);
        damBase.addExit(Action.NORTH, dam_dam_base);
        damBase.addExit(Action.EAST, dam_base_river);
        
        Room damLobby = new Room("Dam Lobby", MapStrings.DESC_DAM_LOBBY, Location.DAM_LOBBY);
        damLobby.addExit(Action.NORTH, dam_lobby_maintenance);
        damLobby.addExit(Action.EAST, dam_lobby_maintenance);
        damLobby.addExit(Action.SOUTH, dam_dam_lobby);
        
        Room maintenanceRoom = new Room("Maintenance Room", MapStrings.DESC_MAINTENANCE_ROOM, Location.MAINTENANCE_ROOM);
        maintenanceRoom.addExit(Action.SOUTH, dam_lobby_maintenance);
        maintenanceRoom.addExit(Action.WEST, dam_lobby_maintenance);
        
        Room northSouthPassage = new Room("North-South Passage", MapStrings.DESC_NORTH_SOUTH_PASSAGE, Location.NORTH_SOUTH_PASSAGE);
        northSouthPassage.addExit(Action.NORTH, northsouth_chasm);
        northSouthPassage.addExit(Action.NORTHEAST, northsouth_deep_canyon);
        northSouthPassage.addExit(Action.SOUTH, round_northsouth);
        
        Room deepCanyon = new Room("Deep Canyon", MapStrings.DESC_DEEP_CANYON, Location.DEEP_CANYON);
        deepCanyon.addExit(Action.EAST, dam_deep_canyon);
        deepCanyon.addExit(Action.NORTHWEST, res_south_deep);
        deepCanyon.addExit(Action.SOUTHWEST, northsouth_deep_canyon);
        deepCanyon.addExit(Action.DOWN, loud_deep_canyon);
        
        Room chasm = new Room("Chasm", MapStrings.DESC_CHASM, Location.CHASM);
        chasm.addExit(Action.NORTHEAST, res_south_chasm);
        chasm.addExit(Action.SOUTHWEST, eastwest_chasm);
        chasm.addExit(Action.UP, eastwest_chasm);
        chasm.addExit(Action.SOUTH, northsouth_chasm);
        
        Room streamView = new Room("Stream View", MapStrings.DESC_STREAM_VIEW, Location.STREAM_VIEW);
        streamView.addExit(Action.EAST, res_south_stream_view);
        streamView.addExit(Action.NORTH, stream_view_stream);
        
        Room stream = new Room("Stream", MapStrings.DESC_STREAM, Location.STREAM);
        stream.addExit(Action.SOUTH, stream_view_stream);
        stream.addExit(Action.EAST, reservoir_stream);
        
        Room reservoirSouth = new Room("Reservoir South", MapStrings.DESC_RESERVOIR_SOUTH, Location.RESERVOIR_SOUTH);
        reservoirSouth.addExit(Action.NORTH, res_south_res);
        reservoirSouth.addExit(Action.WEST, res_south_stream_view);
        reservoirSouth.addExit(Action.SOUTHEAST, res_south_deep);
        reservoirSouth.addExit(Action.SOUTHWEST, res_south_chasm);
        reservoirSouth.addExit(Action.EAST, dam_res_south);
        
        Room reservoir = new Room("Reservoir", MapStrings.DESC_RESERVOIR, Location.RESERVOIR);
        reservoir.addExit(Action.NORTH, res_north_res);
        reservoir.addExit(Action.SOUTH, res_south_res);
        reservoir.addExit(Action.WEST, reservoir_stream);
        
        Room reservoirNorth = new Room("Reservoir North", MapStrings.DESC_RESERVOIR_NORTH, Location.RESERVOIR_NORTH);
        reservoirNorth.addExit(Action.NORTH, res_north_atlantis);
        reservoirNorth.addExit(Action.SOUTH, res_north_res);

        Room atlantisRoom = new Room("Atlantis Room", MapStrings.DESC_ATLANTIS_ROOM, Location.ATLANTIS_ROOM);
        atlantisRoom.addExit(Action.UP, atlantis_cave);
        atlantisRoom.addExit(Action.SOUTH, res_north_atlantis);
        
        Room caveNorth = new Room("Cave", MapStrings.DESC_CAVE_NORTH, Location.CAVE_NORTH);
        // Is this exit down or south??? Both.
        caveNorth.addExit(Action.SOUTH, atlantis_cave);
        caveNorth.addExit(Action.DOWN, atlantis_cave);
        caveNorth.addExit(Action.NORTH, cave_mirrornorth);
        caveNorth.addExit(Action.WEST, cave_twisting);
        
        Room twistingPassage = new Room("Twisting Passage", MapStrings.DESC_TWISTING_PASSAGE, Location.TWISTING_PASSAGE);
        twistingPassage.addExit(Action.EAST, cave_twisting);
        twistingPassage.addExit(Action.NORTH, twisting_mirror);
        
        Room mirrorRoomNorth = new Room("Mirror Room", MapStrings.DESC_MIRROR_ROOM_NORTH, Location.MIRROR_ROOM_NORTH);
        mirrorRoomNorth.addExit(Action.EAST, cave_mirrornorth);
        mirrorRoomNorth.addExit(Action.WEST, twisting_mirror);
        mirrorRoomNorth.addExit(Action.NORTH, mirror_cold);
        
        Room coldPassage = new Room("Cold Passage", MapStrings.DESC_COLD_PASSAGE, Location.COLD_PASSAGE);
        coldPassage.addExit(Action.SOUTH, mirror_cold);
        coldPassage.addExit(Action.WEST, cold_slide);
        
        Room slideRoom = new Room("Slide Room", MapStrings.DESC_SLIDE_ROOM, Location.SLIDE_ROOM);
        slideRoom.addExit(Action.EAST, cold_slide);
        slideRoom.addExit(Action.DOWN, slide_cellar);
        slideRoom.addExit(Action.NORTH, slide_mine_entrance);
        
        Room mineEntrance = new Room("Mine Entrance", MapStrings.DESC_MINE_ENTRANCE, Location.MINE_ENTRANCE);
        mineEntrance.addExit(Action.SOUTH, slide_mine_entrance);
        mineEntrance.addExit(Action.WEST, mine_entrance_squeaky);
        
        Room squeakyRoom = new Room("Squeaky Room", MapStrings.DESC_SQUEAKY_ROOM, Location.SQUEAKY_ROOM);
        squeakyRoom.addExit(Action.EAST, mine_entrance_squeaky);
        squeakyRoom.addExit(Action.NORTH, squeaky_bat);
        
        Room batRoom = new Room("Bat Room", MapStrings.DESC_BAT_ROOM, Location.BAT_ROOM);
        batRoom.addExit(Action.SOUTH, squeaky_bat);
        batRoom.addExit(Action.EAST, bat_shaft);
        
        Room shaftRoom = new Room("Shaft Room", MapStrings.DESC_SHAFT_ROOM, Location.SHAFT_ROOM);
        shaftRoom.addExit(Action.WEST, bat_shaft);
        shaftRoom.addExit(Action.NORTH, shaft_smelly);
        
        Room smellyRoom = new Room("Smelly Room", MapStrings.DESC_SMELLY_ROOM, Location.SMELLY_ROOM);
        smellyRoom.addExit(Action.SOUTH, shaft_smelly);
        smellyRoom.addExit(Action.DOWN, smelly_gas);
        
        Room gasRoom = new Room("Gas Room", MapStrings.DESC_GAS_ROOM, Location.GAS_ROOM);
        gasRoom.addExit(Action.UP, smelly_gas);
        gasRoom.addExit(Action.EAST, gas_coal_1);
        
        Room coalMine1 = new Room("Coal Mine", MapStrings.DESC_COAL_MINE_1, Location.COAL_MINE_1);
        coalMine1.addExit(Action.NORTH, gas_coal_1);
        coalMine1.addExit(Action.NORTHEAST, coal_1_coal_2);
        coalMine1.addExit(Action.EAST, coal_1_self);
        
        Room coalMine2 = new Room("Coal Mine", MapStrings.DESC_COAL_MINE_2, Location.COAL_MINE_2);
        coalMine2.addExit(Action.SOUTH, coal_1_coal_2);
        coalMine2.addExit(Action.NORTH, coal_2_self);
        coalMine2.addExit(Action.SOUTHEAST, coal_2_coal_3);
        
        Room coalMine3 = new Room("Coal Mine", MapStrings.DESC_COAL_MINE_3, Location.COAL_MINE_3);
        coalMine3.addExit(Action.EAST, coal_2_coal_3);
        coalMine3.addExit(Action.SOUTHWEST, coal_3_coal_4);
        coalMine3.addExit(Action.SOUTH, coal_3_self);
        
        Room coalMine4 = new Room("Coal Mine", MapStrings.DESC_COAL_MINE_4, Location.COAL_MINE_4);
        coalMine4.addExit(Action.NORTH, coal_3_coal_4);
        coalMine4.addExit(Action.DOWN, coal_4_ladder_top);
        coalMine4.addExit(Action.WEST, coal_4_self);
        
        Room ladderTop = new Room("Ladder Top", MapStrings.DESC_LADDER_TOP, Location.LADDER_TOP);
        ladderTop.addExit(Action.UP, coal_4_ladder_top);
        ladderTop.addExit(Action.DOWN, ladder_top_bottom);
        
        Room ladderBottom = new Room("Ladder Bottom", MapStrings.DESC_LADDER_BOTTOM, Location.LADDER_BOTTOM);
        ladderBottom.addExit(Action.UP, ladder_top_bottom);
        ladderBottom.addExit(Action.WEST, ladder_bottom_timber);
        ladderBottom.addExit(Action.SOUTH, ladder_bottom_dead_end);
        
        Room deadEndCoalMine = new Room("Dead End", MapStrings.DESC_DEAD_END_COAL_MINE, Location.DEAD_END_COAL_MINE);
        deadEndCoalMine.addExit(Action.NORTH, ladder_bottom_dead_end);
        
        Room timberRoom = new Room("Timber Room", MapStrings.DESC_TIMBER_ROOM, Location.TIMBER_ROOM);
        timberRoom.addExit(Action.EAST, ladder_bottom_timber);
        timberRoom.addExit(Action.WEST, timber_drafty);
        
        Room draftyRoom = new Room("Drafty Room", MapStrings.DESC_DRAFTY_ROOM, Location.DRAFTY_ROOM);
        draftyRoom.addExit(Action.EAST, timber_drafty);
        draftyRoom.addExit(Action.SOUTH, drafty_machine);
        
        Room machineRoom = new Room("Machine Room", MapStrings.DESC_MACHINE_ROOM, Location.MACHINE_ROOM);
        machineRoom.addExit(Action.NORTH, drafty_machine);
        
        Room gratingRoom = new Room("Grating Room", MapStrings.DESC_GRATING_ROOM, Location.GRATING_ROOM);
        gratingRoom.addExit(Action.UP, grating_clearing);
        gratingRoom.addExit(Action.SOUTHWEST, maze11_grating);
        
        Room cyclopsRoom = new Room("Cyclops Room", MapStrings.DESC_CYCLOPS_ROOM, Location.CYCLOPS_ROOM);
        cyclopsRoom.addExit(Action.NORTHWEST, maze15_cyclops);
        cyclopsRoom.addExit(Action.EAST, cyclops_strange);
        cyclopsRoom.addExit(Action.UP, cyclops_treasure);
        
        Room strangePassage = new Room("Strange Passage", MapStrings.DESC_STRANGE_PASSAGE, Location.STRANGE_PASSAGE);
        strangePassage.addExit(Action.WEST, cyclops_strange);
        strangePassage.addExit(Action.EAST, strange_living_room);
        
        Room treasureRoom = new Room("Treasure Room", MapStrings.DESC_TREASURE_ROOM, Location.TREASURE_ROOM);
        treasureRoom.addExit(Action.DOWN, cyclops_treasure);
        
        Room maze1 = new Room("Maze", MapStrings.DESC_MAZE_1, Location.MAZE_1);
        maze1.addExit(Action.EAST, troll_maze);
        maze1.addExit(Action.NORTH, maze1_self);
        maze1.addExit(Action.SOUTH, maze1_maze2);
        maze1.addExit(Action.WEST, maze1_maze4);
        
        Room maze2 = new Room("Maze", MapStrings.DESC_MAZE_2, Location.MAZE_2);
        maze2.addExit(Action.SOUTH, maze1_maze2);
        maze2.addExit(Action.EAST, maze2_maze3);
        maze2.addExit(Action.DOWN, maze2_maze4);
        
        Room maze3 = new Room("Maze", MapStrings.DESC_MAZE_3, Location.MAZE_3);
        maze3.addExit(Action.WEST, maze2_maze3);
        maze3.addExit(Action.NORTH, maze3_maze4);
        maze3.addExit(Action.UP, maze3_maze5);
        
        Room maze4 = new Room("Maze", MapStrings.DESC_MAZE_4, Location.MAZE_4);
        maze4.addExit(Action.WEST, maze3_maze4);
        maze4.addExit(Action.NORTH, maze1_maze4);
        maze4.addExit(Action.EAST, maze4_dead_end);
        
        Room maze5 = new Room("Maze", MapStrings.DESC_MAZE_5, Location.MAZE_5);
        maze5.addExit(Action.NORTH, maze3_maze5);
        maze5.addExit(Action.EAST, maze5_dead_end);
        maze5.addExit(Action.SOUTHWEST, maze5_maze6);
        
        Room maze6 = new Room("Maze", MapStrings.DESC_MAZE_6, Location.MAZE_6);
        maze6.addExit(Action.DOWN, maze5_maze6);
        maze6.addExit(Action.EAST, maze6_maze7);
        maze6.addExit(Action.WEST, maze6_self);
        maze6.addExit(Action.UP, maze6_maze9);
        
        Room maze7 = new Room("Maze", MapStrings.DESC_MAZE_7, Location.MAZE_7);
        maze7.addExit(Action.DOWN, maze7_dead_end);
        maze7.addExit(Action.WEST, maze6_maze7);
        maze7.addExit(Action.EAST, maze7_maze8);
        maze7.addExit(Action.SOUTH, maze7_maze15);
        maze7.addExit(Action.UP, maze7_maze14);
        
        Room maze8 = new Room("Maze", MapStrings.DESC_MAZE_8, Location.MAZE_8);
        maze8.addExit(Action.NORTHEAST, maze7_maze8);
        maze8.addExit(Action.SOUTHEAST, maze8_dead_end);
        maze8.addExit(Action.WEST, maze8_self);
        
        Room maze9 = new Room("Maze", MapStrings.DESC_MAZE_9, Location.MAZE_9);
        maze9.addExit(Action.NORTH, maze6_maze9);
        maze9.addExit(Action.DOWN, maze9_maze11);
        maze9.addExit(Action.EAST, maze9_maze10);
        maze9.addExit(Action.SOUTH, maze9_maze13);
        maze9.addExit(Action.WEST, maze9_maze12);
        maze9.addExit(Action.NORTHWEST, maze9_self);
        
        Room maze10 = new Room("Maze", MapStrings.DESC_MAZE_10, Location.MAZE_10);
        maze10.addExit(Action.EAST, maze9_maze10);
        maze10.addExit(Action.UP, maze10_maze11);
        maze10.addExit(Action.WEST, maze10_maze13);
        
        Room maze11 = new Room("Maze", MapStrings.DESC_MAZE_11, Location.MAZE_11);
        maze11.addExit(Action.DOWN, maze10_maze11);
        maze11.addExit(Action.SOUTHWEST, maze11_maze12);
        maze11.addExit(Action.NORTHWEST, maze11_maze13);
        maze11.addExit(Action.NORTHEAST, maze11_grating);
        
        Room maze12 = new Room("Maze", MapStrings.DESC_MAZE_12, Location.MAZE_12);
        maze12.addExit(Action.EAST, maze12_maze13);
        maze12.addExit(Action.UP, maze9_maze12);
        maze12.addExit(Action.NORTH, maze12_dead_end);
        maze12.addExit(Action.DOWN, maze12_maze5);
        maze12.addExit(Action.SOUTHWEST, maze11_maze12);
        
        Room maze13 = new Room("Maze", MapStrings.DESC_MAZE_13, Location.MAZE_13);
        maze13.addExit(Action.EAST, maze9_maze13);
        maze13.addExit(Action.DOWN, maze12_maze13);
        maze13.addExit(Action.WEST, maze11_maze13);
        maze13.addExit(Action.SOUTH, maze10_maze13);
        
        Room maze14 = new Room("Maze", MapStrings.DESC_MAZE_14, Location.MAZE_14);
        maze14.addExit(Action.NORTHWEST, maze14_self);
        maze14.addExit(Action.WEST, maze14_maze15);
        maze14.addExit(Action.NORTHEAST, maze7_maze14);
        maze14.addExit(Action.SOUTH, maze7_maze14);
        
        Room maze15 = new Room("Maze", MapStrings.DESC_MAZE_15, Location.MAZE_15);
        maze15.addExit(Action.WEST, maze14_maze15);
        maze15.addExit(Action.SOUTH, maze7_maze15);
        maze15.addExit(Action.SOUTHEAST, maze15_cyclops);
        
        Room mazeDeadEndNorth = new Room("Dead End", MapStrings.DESC_DEAD_END_MAZE_NORTH, Location.DEAD_END_MAZE_NORTH);
        mazeDeadEndNorth.addExit(Action.SOUTH, maze4_dead_end);
        
        Room mazeDeadEndCenter = new Room("Dead End", MapStrings.DESC_DEAD_END_MAZE_CENTER, Location.DEAD_END_MAZE_CENTER);
        mazeDeadEndCenter.addExit(Action.WEST, maze5_dead_end);

        Room mazeDeadEndSouthEast = new Room("Dead End", MapStrings.DESC_DEAD_END_MAZE_SOUTHEAST, Location.DEAD_END_MAZE_SOUTHEAST);
        mazeDeadEndSouthEast.addExit(Action.NORTH, maze8_dead_end); 

        Room mazeDeadEndSouthWest = new Room("Dead End", MapStrings.DESC_DEAD_END_MAZE_SOUTHWEST, Location.DEAD_END_MAZE_SOUTHWEST);
        mazeDeadEndSouthWest.addExit(Action.SOUTH, maze12_dead_end);

        // Dark rooms
        attic.setDark(); cellar.setDark(); eastOfChasm.setDark(); gallery.setDark(); studio.setDark(); eastWestPassage.setDark();
        roundRoom.setDark(); narrowPassage.setDark(); mirrorRoomSouth.setDark(); windingPassage.setDark(); caveSouth.setDark();
        entranceToHades.setDark(); landOfTheDead.setDark(); engravingsCave.setDark(); domeRoom.setDark(); torchRoom.setDark(); 
        temple.setDark(); egyptianRoom.setDark(); altar.setDark(); loudRoom.setDark(); dampCave.setDark(); northSouthPassage.setDark();
        chasm.setDark(); deepCanyon.setDark(); damLobby.setDark(); maintenanceRoom.setDark(); atlantisRoom.setDark(); caveNorth.setDark();
        twistingPassage.setDark(); mirrorRoomNorth.setDark(); coldPassage.setDark(); slideRoom.setDark(); mineEntrance.setDark();
        squeakyRoom.setDark(); batRoom.setDark(); shaftRoom.setDark(); gasRoom.setDark(); coalMine1.setDark(); coalMine2.setDark();
        coalMine3.setDark(); coalMine4.setDark(); ladderTop.setDark(); ladderBottom.setDark(); deadEndCoalMine.setDark(); timberRoom.setDark();
        draftyRoom.setDark(); machineRoom.setDark(); maze1.setDark(); maze2.setDark(); maze3.setDark(); maze4.setDark(); maze5.setDark();
        maze6.setDark(); maze7.setDark(); maze8.setDark(); maze8.setDark(); maze10.setDark(); maze11.setDark(); maze12.setDark(); maze13.setDark();
        maze14.setDark(); maze15.setDark(); mazeDeadEndCenter.setDark(); mazeDeadEndNorth.setDark(); mazeDeadEndSouthWest.setDark();
        mazeDeadEndSouthEast.setDark(); gratingRoom.setDark(); cyclopsRoom.setDark(); strangePassage.setDark(); treasureRoom.setDark();

        // Rooms with a dangerous height
        eastOfChasm.height = true; chasm.height = true; canyonView.height = true;

        // Gaseous rooms
        gasRoom.setGas();

        house_behind_kitchen.close();

        // Closed passages
        grating_clearing.close();
        house_behind_kitchen.close();
        cellar_livingroom.close();
        troll_maze.close();
        troll_eastwest.close();
        strange_living_room.close();
        house_west_barrow.close();
        rainbow_end.close();
        falls_rainbow.close();
        dome_torch.close();
        hades_land_dead.close();
        dam_base_river.close();
        white_north_river.close();
        white_south_river.close();
        river_sandy_beach.close();
        river_shore.close();
        res_south_res.close();
        res_north_res.close();
        stream_view_stream.close();
        cyclops_strange.close();
        cyclops_treasure.close();

        grating_clearing.closedFail = "The grating is closed!";
        house_behind_kitchen.closedFail = MapStrings.KITCHEN_WINDOW_CLOSED;
        strange_living_room.closedFail = "The door is nailed shut.";
        troll_eastwest.closedFail = "The troll fends you off with a menacing gesture.";
        troll_maze.closedFail = "The troll fends you off with a menacing gesture.";
        dome_torch.closedFail = "You cannot do gown without fracturing many bones.";
        hades_land_dead.closedFail = "Some invisible force prevents you from passing through the gate.";
        res_south_res.closedFail = "You would drown.";
        res_north_res.closedFail = "You would drown.";
        cyclops_strange.closedFail = "The east wall is solid rock.";
        cyclops_treasure.closedFail = "The cyclops doesn't look like he'll let you past.";
        maze2_maze4.message = "You won't be able to get back up to the tunnel you are going through "
            + "when it gets to the next room.";
        cellar_livingroom.message = "The trap door crashes shut, and you hear someone barring it.";

        // Narrow passages
        studio_kitchen.weightLimit = 5;
        altar_cave.weightLimit = 5;
        timber_drafty.weightLimit = 0;




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

    public void createGameObjects()
    {
        /* Items */

        // There are 19 treasure items with point values.
        // Name, location, point value, weight.

        Item bar = new Item("platinum bar", Location.LOUD_ROOM);
        bar.altNames.add("bar");
        bar.altNames.add("platinum");
        bar.presenceString = ObjectStrings.PLATINUM_BAR;

        Item bauble = new Item("brass bauble", Location.NULL_LOCATION);
        bauble.altNames.add("brass");
        bauble.altNames.add("bauble");
        
        Item chalice = new Item("silver chalice", Location.TREASURE_ROOM);
        chalice.altNames.add("silver");
        chalice.altNames.add("chalice");
        
        Item coffin = new Item("gold coffin", Location.EGYPTIAN_ROOM);
        coffin.altNames.add("coffin");
        coffin.presenceString = ObjectStrings.COFFIN;
        coffin.containerID = Location.INSIDE_COFFIN;
        
        Item coins = new Item("bag of coins", Location.MAZE_5);
        coins.altNames.add("bag");
        coins.altNames.add("coins");
        coins.presenceString = ObjectStrings.INIT_COINS;
        
        Item canary = new Item("golden canary", Location.NULL_LOCATION);
        canary.altNames.add("golden clockwork canary");
        canary.altNames.add("golden clockwork");
        canary.altNames.add("clockwork canary");
        canary.altNames.add("clockwork");
        canary.altNames.add("canary");
        
        Item diamond = new Item("huge diamond", Location.NULL_LOCATION);
        diamond.altNames.add("diamond");
        diamond.presenceString = ObjectStrings.DIAMOND;

        Item egg = new Item("jewel-encrusted egg", Location.UP_TREE);
        egg.altNames.add("egg");
        egg.initialPresenceString = ObjectStrings.INIT_EGG;

        Item emerald = new Item("large emerald", Location.NULL_LOCATION);
        emerald.altNames.add("emerald");
        
        Item jade = new Item("jade figurine", Location.BAT_ROOM);
        jade.altNames.add("jade");
        jade.altNames.add("figurine");
        jade.presenceString = ObjectStrings.JADE;

        Item painting = new Item("painting", Location.GALLERY);
        painting.initialPresenceString = ObjectStrings.INIT_PAINTING;
        painting.presenceString = ObjectStrings.PAINTING;
        
        Item pot = new Item("pot of gold", Location.NULL_LOCATION);
        pot.altNames.add("pot");
        pot.altNames.add("gold");
        pot.presenceString = ObjectStrings.POT;
        
        Item sapphire = new Item("sapphire-encrusted bracelet", Location.GAS_ROOM);
        sapphire.altNames.add("sapphire");
        sapphire.altNames.add("bracelet");
        sapphire.altNames.add("sapphire bracelet");
        
        Item scarab = new Item("beautiful jeweled scarab", Location.NULL_LOCATION);
        scarab.altNames.add("jeweled scarab");
        scarab.altNames.add("scarab");
        
        Item sceptre = new Item("sceptre", Location.INSIDE_COFFIN);
        sceptre.altNames.add("scepter");
        sceptre.initialPresenceString = ObjectStrings.INIT_SCEPTRE;
        sceptre.presenceString = ObjectStrings.SCEPTRE;
        
        Item skull = new Item("crystal skull", Location.LAND_OF_THE_DEAD);
        skull.altNames.add("skull");
        skull.altNames.add("crystal");
        skull.initialPresenceString = ObjectStrings.INIT_SKULL;
        
        Item torch = new Item("torch", Location.TORCH_ROOM);
        torch.altNames.add("ivory");
        torch.altNames.add("ivory torch");
        torch.initialPresenceString = ObjectStrings.INIT_TORCH;
        
        Item trident = new Item("crystal trident", Location.ATLANTIS_ROOM);
        trident.altNames.add("trident");
        trident.altNames.add("crystal");
        trident.initialPresenceString = ObjectStrings.INIT_TRIDENT;
        
        Item trunk = new Item("trunk of jewels", Location.NULL_LOCATION);
        trunk.altNames.add("trunk");
        trunk.altNames.add("jewels");

        

        // And another 40 items that can be taken.

        Item axe = new Item("bloody axe", Location.TROLL_INVENTORY);
        axe.altNames.add("axe");
        axe.altNames.add("ax");
        
        Item bell = new Item("brass bell", Location.TEMPLE);
        bell.altNames.add("bell");

        Item blackBook = new Item("black book", Location.ALTAR);
        blackBook.altNames.add("book");
        blackBook.initialPresenceString = ObjectStrings.INIT_BLACK_BOOK;

        Item boatLabel = new Item("tan label", Location.NULL_LOCATION);
        boatLabel.altNames.add("label");
        boatLabel.readString = GameStrings.BOAT_LABEL_TEXT;
        
        Item bottle = new Item("glass bottle", Location.ON_KITCHEN_TABLE);
        bottle.altNames.add("bottle");
        bottle.altNames.add("glass");
        bottle.initialPresenceString = ObjectStrings.INIT_BOTTLE;
        bottle.containerID = Location.INSIDE_BOTTLE;

        Item brokenCanary = new Item("broken clockwork canary", Location.NULL_LOCATION);
        brokenCanary.altNames.add("broken canary");
        brokenCanary.altNames.add("canary");
        brokenCanary.altNames.add("broken clockwork");
        brokenCanary.altNames.add("clockwork");
        brokenCanary.initialPresenceString = ObjectStrings.INIT_BROKEN_CANARY;

        Item brokenEgg = new Item("broken jewel-encrusted egg", Location.NULL_LOCATION);
        brokenEgg.presenceString = "There is a somewhat ruined egg here.";
        brokenEgg.altNames.add("broken egg");
        brokenEgg.altNames.add("egg");

        Item buoy = new Item("red buoy", Location.FRIGID_RIVER_4);
        buoy.altNames.add("buoy");
        buoy.containerID = Location.INSIDE_BUOY;

        Item candles = new Item("pair of candles", Location.ALTAR);
        candles.altNames.add("candles");
        candles.altNames.add("candle");
        candles.altNames.add("pair");
        candles.initialPresenceString = ObjectStrings.INIT_CANDLES;

        Item coal = new Item("small pile of coal", Location.DEAD_END_COAL_MINE);
        coal.altNames.add("coal");
        coal.altNames.add("pile");
        coal.altNames.add("coal pile");
        coal.altNames.add("pile of coal");
        coal.altNames.add("small pile");

        Item deflatedBoat = new Item("pile of plastic", Location.DAM_BASE);
        deflatedBoat.altNames.add("boat");
        deflatedBoat.altNames.add("raft");
        deflatedBoat.altNames.add("pile");
        deflatedBoat.altNames.add("plastic");
        deflatedBoat.presenceString = ObjectStrings.INIT_BOAT;
        
        Item garlic = new Item("clove of garlic", Location.INSIDE_SACK);
        garlic.altNames.add("clove");
        garlic.altNames.add("garlic");

        Item guideBook = new Item("guidebook", Location.DAM_LOBBY);
        guideBook.altNames.add("book");
        guideBook.initialPresenceString = ObjectStrings.INIT_GUIDEBOOK;

        Item gunk = new Item("viscous material", Location.NULL_LOCATION);
        gunk.altNames.add("gunk");
        gunk.altNames.add("material");
        
        Item inflatedBoat = new Item("magic boat", Location.NULL_LOCATION);
        inflatedBoat.altNames.add("boat");
        inflatedBoat.altNames.add("raft");
        inflatedBoat.containerID = Location.INSIDE_BOAT;
        
        Item knife = new Item("nasty knife", Location.ATTIC);
        knife.altNames.add("knife");
        knife.initialPresenceString = ObjectStrings.INIT_NASTY_KNIFE;
        
        Item lantern = new Item("brass lantern", Location.LIVING_ROOM);
        lantern.initialPresenceString = ObjectStrings.INIT_LANTERN;
        lantern.altNames.add("lamp");
        lantern.altNames.add("lantern");
        lantern.altNames.add("brass lamp");
        lantern.lifespan = Game.LANTERN_LIFESPAN;

        Item nest = new Item("bird's nest", Location.UP_TREE);
        nest.altNames.add("nest");
        nest.initialPresenceString = ObjectStrings.INIT_NEST;
        nest.containerID = Location.INSIDE_BIRDS_NEST;
        
        Item leafPile = new Item("pile of leaves", Location.CLEARING_NORTH);
        leafPile.altNames.add("pile");
        leafPile.altNames.add("leaves");
        leafPile.initialPresenceString = ObjectStrings.INIT_LEAF_PILE;

        Item leaflet = new Item("leaflet", Location.INSIDE_MAILBOX);
        leaflet.readString = GameStrings.LEAFLET_TEXT;

        Item lunch = new Item("lunch", Location.INSIDE_SACK);
        lunch.altNames.add("peppers");
        lunch.altNames.add("hot peppers");
        lunch.altNames.add("hot lunch");

        Item matchbook = new Item("matchbook", Location.DAM_LOBBY);
        matchbook.altNames.add("matches");
        matchbook.presenceString = ObjectStrings.INIT_MATCHBOOK;

        Item pump = new Item("hand-held air pump", Location.RESERVOIR_NORTH);
        pump.altNames.add("air pump");
        pump.altNames.add("pump");

        Item puncturedBoat = new Item("punctured boat", Location.NULL_LOCATION);
        puncturedBoat.altNames.add("boat");
        puncturedBoat.altNames.add("ruined boat");

        Item rope = new Item("rope", Location.ATTIC);
        rope.initialPresenceString = ObjectStrings.INIT_ROPE;
        
        Item rustyKnife = new Item("rusty knife", Location.MAZE_5);
        rustyKnife.initialPresenceString = ObjectStrings.INIT_RUSTY_KNIFE;

        Item sack = new Item("brown sack", Location.KITCHEN);
        sack.altNames.add("sack");
        sack.altNames.add("bag");
        sack.altNames.add("brown bag");
        sack.initialPresenceString = ObjectStrings.INIT_SACK;
        sack.containerID = Location.INSIDE_SACK;
        
        Item screwdriver = new Item("screwdriver", Location.MAINTENANCE_ROOM);
        screwdriver.altNames.add("driver");

        Item shovel = new Item("shovel", Location.SANDY_BEACH);

        Item skeletonKey = new Item("skeleton key", Location.MAZE_5);
        skeletonKey.altNames.add("key");

        Item stiletto = new Item("stiletto", Location.THIEF_INVENTORY);

        Item studioPaper = new Item("ZORK owner's manual", Location.STUDIO);
        studioPaper.altNames.add("paper");
        studioPaper.altNames.add("manual");
        studioPaper.readString = GameStrings.NATE_MANUAL_TEXT;
        studioPaper.initialPresenceString = ObjectStrings.INIT_ZORK_MANUAL;
        
        Item sword = new Item("elvish sword", Location.LIVING_ROOM);
        sword.initialPresenceString = ObjectStrings.INIT_SWORD;
        sword.altNames.add("sword");
        
        Item timber = new Item("broken timber", Location.TIMBER_ROOM);
        timber.altNames.add("timber");
   
        Item tube = new Item("tube", Location.MAINTENANCE_ROOM);
        tube.presenceString = ObjectStrings.TUBE;
        tube.examineString = ObjectStrings.DESC_TUBE;
        tube.containerID = Location.INSIDE_TUBE;
        
        Item uselessLantern = new Item("useless lantern", Location.MAZE_5);
        uselessLantern.altNames.add("lantern");
        uselessLantern.initialPresenceString = ObjectStrings.INIT_USELESS;

        Item wrench = new Item("wrench", Location.MAINTENANCE_ROOM);


        // Features, containers and surfaces
        Surface atticTable = new Surface("attic table", Location.ATTIC);
        atticTable.containerID = Location.ON_ATTIC_TABLE;
        
        Container basket = new Container("basket", Location.SHAFT_ROOM);
        basket.altNames.add("cage");
        basket.takeString = "The cage is securely fastened to the iron chain.";
        basket.containerID = Location.INSIDE_BASKET;
        
        Feature buttonBlue = new Feature("blue button", Location.MAINTENANCE_ROOM);
        buttonBlue.altNames.add("blue");

        Feature buttonBrown = new Feature("brown button", Location.MAINTENANCE_ROOM);
        buttonBrown.altNames.add("brown");
        
        Feature buttonRed = new Feature("red button", Location.MAINTENANCE_ROOM);
        buttonRed.altNames.add("red");
        
        Feature buttonYellow = new Feature("yellow button", Location.MAINTENANCE_ROOM);
        buttonYellow.altNames.add("yellow");
        
        Feature carpet = new Feature("oriental rug", Location.LIVING_ROOM);
        carpet.takeString = "The rug is extremely heavy and cannot be carried.";
        carpet.altNames.add("carpet");
        carpet.altNames.add("oriental carpet");
        carpet.altNames.add("rug");

        Feature chain = new Feature("chain", Location.SHAFT_ROOM);
        chain.lowerString = "Perhaps you should do that to the basket.";
        chain.raiseString = "Perhaps you should do that to the basket.";
        
        Feature coalMachine = new Feature("machine", Location.MACHINE_ROOM);
        coalMachine.containerID = Location.INSIDE_COAL_MACHINE;
        
        Feature damBolt = new Feature("bolt", Location.DAM);
        
        Feature forest = new Feature("forest", Location.FOREST_PATH);
        forest.altNames.add("woods");
        forest.altNames.add("trees");
        forest.altLocations.add(Location.FOREST_WEST);
        forest.altLocations.add(Location.FOREST_EAST);
        forest.altLocations.add(Location.FOREST_NORTHEAST);
        forest.altLocations.add(Location.FOREST_SOUTH);
        forest.altLocations.add(Location.CLEARING_NORTH);
        forest.altLocations.add(Location.CLEARING_EAST);
        forest.altLocations.add(Location.UP_TREE);
        forest.listenString = "The pines and the hemlocks seem to be murmuring.";
        
        Feature grating = new Feature("grating", Location.NULL_LOCATION);
        
        Feature house = new Feature("white house", Location.WEST_OF_HOUSE);
        house.altNames.add("house");
        house.altLocations.add(Location.NORTH_OF_HOUSE);
        house.altLocations.add(Location.BEHIND_HOUSE);
        house.altLocations.add(Location.SOUTH_OF_HOUSE);
        house.altLocations.add(Location.KITCHEN);
        house.altLocations.add(Location.LIVING_ROOM);
        house.altLocations.add(Location.ATTIC);

        Feature houseWindow = new Feature("kitchen window", Location.BEHIND_HOUSE);
        houseWindow.altNames.add("window");
        houseWindow.altLocations.add(Location.KITCHEN);

        Surface kitchenTable = new Surface("kitchen table", Location.KITCHEN);
        kitchenTable.containerID = Location.ON_KITCHEN_TABLE;
        
        Container mailbox = new Container("small mailbox", Location.WEST_OF_HOUSE);
        mailbox.altNames.add("mailbox");
        mailbox.altNames.add("box");
        mailbox.takeString = "It is securely anchored.";
        mailbox.moveString = "You can't move the small mailbox.";
        mailbox.inventory.add(leaflet);
        mailbox.containerID = Location.INSIDE_MAILBOX;
        
        Feature mirror = new Feature("mirror", Location.MIRROR_ROOM_SOUTH);
        mirror.altLocations.add(Location.MIRROR_ROOM_NORTH);
        
        Feature mountains = new Feature("mountains", Location.FOREST_NORTHEAST);
        mountains.altNames.add("mountain");
        mountains.climbString = "Don't you believe me? The mountains are impassable!";
        
        Surface pedestal = new Surface("pedestal", Location.TORCH_ROOM);
        pedestal.containerID = Location.ON_PEDESTAL;
        
        Feature skeleton = new Feature("skeleton", Location.MAZE_5);
        
        Feature toolChests = new Feature("tool chests", Location.MAINTENANCE_ROOM);
        toolChests.initialPresenceString = ObjectStrings.INIT_TOOL_CHESTS;
        toolChests.takeString = "The chests are so rusty and corroded that they crumble when you touch them.";
        toolChests.examineString = "The chests are all empty.";
        
        Feature trapDoor = new Feature("trap door", Location.NULL_LOCATION);
        trapDoor.altNames.add("trap");
        trapDoor.altNames.add("door");
        
        Container trophyCase = new Container("trophy case", Location.LIVING_ROOM);
        trophyCase.altNames.add("case");
        trophyCase.containerID = Location.INSIDE_TROPHY_CASE;

        
        // Actors
        Actor current = new Actor("current", Location.FRIGID_RIVER_1);
        current.altLocations.add(Location.FRIGID_RIVER_2);
        current.altLocations.add(Location.FRIGID_RIVER_3);
        current.altLocations.add(Location.FRIGID_RIVER_4);
        current.altLocations.add(Location.FRIGID_RIVER_5);

        Actor cyclops = new Actor("cyclops", Location.CYCLOPS_ROOM);
        
        Actor flood = new Actor("flood", Location.MAINTENANCE_ROOM);
        
        Actor gustOfWind = new Actor("gust of wind", Location.CAVE_SOUTH);
        
        Actor songbird = new Actor("songbird", Location.NULL_LOCATION);
        
        Actor spirits = new Actor("spirits", Location.ENTRANCE_TO_HADES);
        
        Actor thief = new Actor("thief", Location.TREASURE_ROOM);
        thief.containerID = Location.THIEF_INVENTORY;
        
        Actor troll = new Actor("troll", Location.TROLL_ROOM);
        troll.presenceString = "A nasty-looking troll, brandishing a bloody axe, blocks all passages out of the room.";
        troll.takeString = "The troll spits in your face, grunting \"Better luck next time\" in a rather barbarous accent.";
        troll.talkString = "The troll isn't much of a conversationalist.";
        troll.containerID = Location.TROLL_INVENTORY;
        
        Actor vampireBat = new Actor("vampire bat", Location.BAT_ROOM);




        // Actor methods

        ActorMethod dummyActorMethod = () -> {};

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
                    if (rand.nextInt(100) < Game.SONGBIRD_CHIRP_PERCENT)
                        Game.output(GameStrings.SONGBIRD);
                } break;

                default: {} break;
            }

        };

        ActorMethod cyclopsMethod = () -> {

            if (state.playerLocation == Location.CELLAR && state.playerPreviousLocation == Location.LIVING_ROOM)
            {
                Room rm = state.worldMap.get(Location.CELLAR);
                Passage p = rm.exits.get(Action.UP);
                p.close();
            }

        };

        ActorMethod windMethod = () -> {

            if (state.playerLocation == Location.CAVE_SOUTH)
            {
                Item it = (Item)(state.objectList.get("candles"));
                if (it.activated)
                {
                    Random rand = new Random();
                    if (rand.nextInt(100) < 50)
                    {
                        Game.output("A gust of wind blows out your candles!");
                        it.activated = false;
                    }

                }
            }
        };

        songbird.setActorMethod(songbirdMethod);
        songbird.presenceString = "";
        songbird.takeString = GameStrings.SONGBIRD_NEARBY;
        songbird.examineString = GameStrings.SONGBIRD_NEARBY;

        troll.setActorMethod(dummyActorMethod);
        thief.setActorMethod(dummyActorMethod);
        cyclops.setActorMethod(cyclopsMethod);
        vampireBat.setActorMethod(dummyActorMethod);
        spirits.setActorMethod(dummyActorMethod);
        gustOfWind.setActorMethod(windMethod);
        flood.setActorMethod(dummyActorMethod);
        current.setActorMethod(dummyActorMethod);




        

        // Add all objects to the gamestate list

        state.objectList.put(bar.name, bar);
        state.objectList.put(bauble.name, bauble);
        state.objectList.put(chalice.name, chalice);
        state.objectList.put(coffin.name, coffin);
        state.objectList.put(coins.name, coins);
        state.objectList.put(canary.name, canary);
        state.objectList.put(diamond.name, diamond);
        state.objectList.put(egg.name, egg);
        state.objectList.put(emerald.name, emerald);
        state.objectList.put(jade.name, jade);
        state.objectList.put(painting.name, painting);
        state.objectList.put(pot.name, pot);
        state.objectList.put(sapphire.name, sapphire);
        state.objectList.put(scarab.name, scarab);
        state.objectList.put(sceptre.name, sceptre);
        state.objectList.put(skull.name, skull);
        state.objectList.put(torch.name, torch);
        state.objectList.put(trident.name, trident);
        state.objectList.put(trunk.name, trunk);

        state.objectList.put(axe.name, axe);
        state.objectList.put(bell.name, bell);
        state.objectList.put(blackBook.name, blackBook);
        state.objectList.put(bottle.name, bottle);
        state.objectList.put(brokenCanary.name, brokenCanary);
        state.objectList.put(brokenEgg.name, brokenEgg);
        state.objectList.put(buoy.name, buoy);
        state.objectList.put(candles.name, candles);
        state.objectList.put(coal.name, coal);
        state.objectList.put(deflatedBoat.name, deflatedBoat);
        state.objectList.put(garlic.name, garlic);
        state.objectList.put(guideBook.name, guideBook);
        state.objectList.put(gunk.name, gunk);
        state.objectList.put(inflatedBoat.name, inflatedBoat);
        state.objectList.put(lantern.name, lantern);
        state.objectList.put(leaflet.name, leaflet);
        state.objectList.put(leafPile.name, leafPile);
        state.objectList.put(lunch.name, lunch);
        state.objectList.put(knife.name, knife);
        state.objectList.put(matchbook.name, matchbook);
        state.objectList.put(nest.name, nest);
        state.objectList.put(pump.name, pump);
        state.objectList.put(puncturedBoat.name, puncturedBoat);
        state.objectList.put(rope.name, rope);
        state.objectList.put(rustyKnife.name, rustyKnife);
        state.objectList.put(sack.name, sack);
        state.objectList.put(screwdriver.name, screwdriver);
        state.objectList.put(shovel.name, shovel);
        state.objectList.put(skeletonKey.name, skeletonKey);
        state.objectList.put(stiletto.name, stiletto);
        state.objectList.put(studioPaper.name, studioPaper);
        state.objectList.put(sword.name, sword);
        state.objectList.put(timber.name, timber);
        state.objectList.put(tube.name, tube);
        state.objectList.put(uselessLantern.name, uselessLantern);
        state.objectList.put(wrench.name, wrench);

        state.objectList.put(atticTable.name, atticTable);
        state.objectList.put(basket.name, basket);
        state.objectList.put(buttonBlue.name, buttonBlue);
        state.objectList.put(buttonYellow.name, buttonYellow);
        state.objectList.put(buttonBrown.name, buttonBrown);
        state.objectList.put(buttonRed.name, buttonRed);
        state.objectList.put(carpet.name, carpet);
        state.objectList.put(chain.name, chain);
        state.objectList.put(coalMachine.name, coalMachine);
        state.objectList.put(damBolt.name, damBolt);
        state.objectList.put(forest.name, forest);
        state.objectList.put(grating.name, grating);
        state.objectList.put(house.name, house);
        state.objectList.put(houseWindow.name, houseWindow);
        state.objectList.put(kitchenTable.name, kitchenTable);
        state.objectList.put(mailbox.name, mailbox);
        state.objectList.put(mirror.name, mirror);
        state.objectList.put(mountains.name, mountains);
        state.objectList.put(pedestal.name, pedestal);
        state.objectList.put(skeleton.name, skeleton);
        state.objectList.put(trapDoor.name, trapDoor);
        state.objectList.put(trophyCase.name, trophyCase);
        state.objectList.put(toolChests.name, toolChests);

        state.objectList.put(current.name, current);
        state.objectList.put(cyclops.name, cyclops);
        state.objectList.put(flood.name, flood);
        state.objectList.put(gustOfWind.name, gustOfWind);
        state.objectList.put(songbird.name, songbird);
        state.objectList.put(spirits.name, spirits);
        state.objectList.put(thief.name, thief);
        state.objectList.put(troll.name, troll);
        state.objectList.put(vampireBat.name, vampireBat);

        // Fill the inventories of the containers
        for (GameObject cont : state.objectList.values())
        {
            if (cont.isContainer())
            {
                Container c = (Container)(cont);

                for (GameObject it : state.objectList.values())
                {
                    if (it.location == c.containerID && it.isItem())
                        c.inventory.add((Item)(it));
                }
            }
        }




    }

	public void createActions()
	{
	    // Movement actions
		state.actions.put("north",       Action.NORTH);
		state.actions.put("go north",    Action.NORTH);
		state.actions.put("walk north",  Action.NORTH);
		state.actions.put("exit north",  Action.NORTH);
		state.actions.put("n",           Action.NORTH);
		state.actions.put("go n",        Action.NORTH);
		state.actions.put("walk n",      Action.NORTH);
		state.actions.put("exit n",      Action.NORTH);
	
		state.actions.put("south",       Action.SOUTH);
		state.actions.put("go south",    Action.SOUTH);
		state.actions.put("walk south",  Action.SOUTH);
		state.actions.put("exit south",  Action.SOUTH);
		state.actions.put("s",           Action.SOUTH);
		state.actions.put("go s",        Action.SOUTH);
		state.actions.put("walk s",      Action.SOUTH);
		state.actions.put("exit s",      Action.SOUTH);
	
		state.actions.put("east",        Action.EAST);
		state.actions.put("e",           Action.EAST);
		state.actions.put("go east",     Action.EAST);
		state.actions.put("walk east",   Action.EAST);
		state.actions.put("exit east",   Action.EAST);
		state.actions.put("go e",        Action.EAST);
		state.actions.put("walk e",      Action.EAST);
		state.actions.put("exit e",      Action.EAST);
	
		state.actions.put("west",        Action.WEST);
		state.actions.put("go west",     Action.WEST);
		state.actions.put("walk west",   Action.WEST);
		state.actions.put("exit west",   Action.WEST);
		state.actions.put("w",           Action.WEST);
		state.actions.put("go w",        Action.WEST);
		state.actions.put("walk w",      Action.WEST);
		state.actions.put("exit w",      Action.WEST);
	
	    state.actions.put("northeast",        Action.NORTHEAST);
	    state.actions.put("go northeast",     Action.NORTHEAST);
	    state.actions.put("walk northeast",   Action.NORTHEAST);
	    state.actions.put("exit northeast",   Action.NORTHEAST);
	    state.actions.put("ne",               Action.NORTHEAST);
	    state.actions.put("go ne",            Action.NORTHEAST);
	    state.actions.put("walk ne",          Action.NORTHEAST);
	    state.actions.put("exit ne",          Action.NORTHEAST);
	
	    state.actions.put("northwest",        Action.NORTHWEST);
	    state.actions.put("go northwest",     Action.NORTHWEST);
	    state.actions.put("walk northwest",   Action.NORTHWEST);
	    state.actions.put("exit northwest",   Action.NORTHWEST);
	    state.actions.put("nw",               Action.NORTHWEST);
	    state.actions.put("go nw",            Action.NORTHWEST);
	    state.actions.put("walk nw",          Action.NORTHWEST);
	    state.actions.put("exit nw",          Action.NORTHWEST);
	
	    state.actions.put("southeast",        Action.SOUTHEAST);
	    state.actions.put("go southeast",     Action.SOUTHEAST);
	    state.actions.put("walk southeast",   Action.SOUTHEAST);
	    state.actions.put("exit southeast",   Action.SOUTHEAST);
	    state.actions.put("se",               Action.SOUTHEAST);
	    state.actions.put("go se",            Action.SOUTHEAST);
	    state.actions.put("walk se",          Action.SOUTHEAST);
	    state.actions.put("exit se",          Action.SOUTHEAST);
	
	    state.actions.put("southwest",        Action.SOUTHWEST);
	    state.actions.put("go southwest",     Action.SOUTHWEST);
	    state.actions.put("walk southwest",   Action.SOUTHWEST);
	    state.actions.put("exit southwest",   Action.SOUTHWEST);
	    state.actions.put("sw",               Action.SOUTHWEST);
	    state.actions.put("go sw",            Action.SOUTHWEST);
	    state.actions.put("walk sw",          Action.SOUTHWEST);
	    state.actions.put("exit sw",          Action.SOUTHWEST);
	
		state.actions.put("up",	     Action.UP);
	    state.actions.put("go up",         Action.UP);
		state.actions.put("walk up",	     Action.UP);
		state.actions.put("exit up",	 Action.UP);
		state.actions.put("u",	         Action.UP);
	    state.actions.put("go u",      Action.UP);
		state.actions.put("walk u",	     Action.UP);
		state.actions.put("exit u",	 Action.UP);
	
		state.actions.put("down",       Action.DOWN);
	    state.actions.put("go down",    Action.DOWN);
		state.actions.put("walk down",    Action.DOWN);
		state.actions.put("exit down",  Action.DOWN);
		state.actions.put("d",          Action.DOWN);
	    state.actions.put("go d",       Action.DOWN);
		state.actions.put("walk d",       Action.DOWN);
		state.actions.put("exit d",     Action.DOWN);
	
	    state.actions.put("in", Action.IN);
	    state.actions.put("inside", Action.IN);
	    state.actions.put("go in", Action.IN);
        state.actions.put("go inside", Action.IN);
	    state.actions.put("out", Action.OUT);
        state.actions.put("go out", Action.OUT);
        state.actions.put("go outside", Action.OUT);
        state.actions.put("outside", Action.OUT);
        state.actions.put("exit", Action.OUT);
	    state.actions.put("slide", Action.SLIDE);
	    state.actions.put("swim", Action.SWIM);
	
	    // Reflexive actions: no interaction with game objects
		state.actions.put("quit",  Action.QUIT);
		state.actions.put("q",     Action.QUIT);
		state.actions.put("jump",  Action.JUMP);
		state.actions.put("look around",  Action.LOOK);
		state.actions.put("look",  Action.LOOK);
		state.actions.put("l",     Action.LOOK);
		state.actions.put("inventory", Action.INVENTORY);
		state.actions.put("i",         Action.INVENTORY);
		state.actions.put("fuck",  Action.PROFANITY);
		state.actions.put("shit",  Action.PROFANITY);
		state.actions.put("shout", Action.SHOUT);
		state.actions.put("yell",  Action.SHOUT);
		state.actions.put("scream",  Action.SHOUT);
		state.actions.put("wait", Action.WAIT);
	    state.actions.put("author", Action.AUTHOR);
	    state.actions.put("pray", Action.PRAY);
	
	
	    // Direct object interaction actions
	    
	    state.actions.put("answer", Action.ANSWER);
	    state.actions.put("blow", Action.BLOW);
	    state.actions.put("climb", Action.CLIMB);
	    state.actions.put("close", Action.CLOSE);
	    state.actions.put("count", Action.COUNT);
	    state.actions.put("cross", Action.CROSS);
	    state.actions.put("deflate", Action.DEFLATE);
	    state.actions.put("drink", Action.DRINK);
	    state.actions.put("drop", Action.DROP);
	    state.actions.put("eat", Action.EAT);
	    state.actions.put("enter", Action.ENTER);
	    state.actions.put("examine", Action.EXAMINE);
	    state.actions.put("look at", Action.EXAMINE);
	    state.actions.put("l at", Action.EXAMINE);
	    state.actions.put("extinguish", Action. EXTINGUISH);
	    state.actions.put("follow", Action.FOLLOW);
	    state.actions.put("kick", Action.KICK);
	    state.actions.put("knock", Action.KNOCK);
	    state.actions.put("light", Action.LIGHT);
	    state.actions.put("listen", Action.LISTEN);
	    state.actions.put("lower", Action.LOWER);
	    state.actions.put("move", Action.MOVE_OBJECT);
	    state.actions.put("open", Action.OPEN);
	    state.actions.put("pour", Action.POUR);
	    state.actions.put("pull", Action.PULL);
	    state.actions.put("push", Action.PUSH);
	    state.actions.put("raise", Action.RAISE);
	    state.actions.put("read", Action.READ);
	    state.actions.put("say", Action.TALK_TO);
	    state.actions.put("search", Action.SEARCH);
	    state.actions.put("shake", Action.SHAKE);
	    state.actions.put("smell", Action.SMELL);
	    state.actions.put("stay", Action.STAY);
	    state.actions.put("take", Action.TAKE);
	    state.actions.put("pick up", Action.TAKE);
	    state.actions.put("get", Action.TAKE);
	    state.actions.put("acquire", Action.TAKE);
	    state.actions.put("talk to", Action.TALK_TO);
	    state.actions.put("touch", Action.TOUCH);
	    state.actions.put("turn", Action.TURN);
	    state.actions.put("wake", Action.WAKE);
	    state.actions.put("wave", Action.WAVE);
	    state.actions.put("wear", Action.WEAR);
	    state.actions.put("wind", Action.WIND);
	
	
	
	    // Indirect actions
	    state.actions.put("attack", Action.ATTACK);
	    state.actions.put("break", Action.BREAK);
	    state.actions.put("burn", Action.BURN);
	    state.actions.put("cut", Action.CUT);
	    state.actions.put("dig", Action.DIG);
	    state.actions.put("fill", Action.FILL);
	    state.actions.put("inflate", Action.INFLATE);
	
	    state.actions.put("unlock", Action.UNLOCK);
	    state.actions.put("lock", Action.LOCK);
	    state.actions.put("strike", Action.STRIKE);
	
	    state.actions.put("give", Action.GIVE);
	    state.actions.put("place", Action.PUT);
	    state.actions.put("put", Action.PUT);
	    state.actions.put("throw", Action.THROW);
	    state.actions.put("tie", Action.TIE);

        // Godmode actions
        state.actions.put("accio", Action.ACCIO);
        state.actions.put("teleport", Action.TELEPORT);
	
	
	    // Assigning action types
	
	    state.actionTypes.put(Action.QUIT, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.LOOK, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.INVENTORY, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.SHOUT, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.WAIT, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.PROFANITY, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.JUMP, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.AUTHOR, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.PRAY, ActionType.REFLEXIVE);
	    state.actionTypes.put(Action.SWIM, ActionType.REFLEXIVE);
	
	    state.actionTypes.put(Action.NORTH, ActionType.EXIT);
	    state.actionTypes.put(Action.SOUTH, ActionType.EXIT);
	    state.actionTypes.put(Action.EAST, ActionType.EXIT);
	    state.actionTypes.put(Action.WEST, ActionType.EXIT);
	    state.actionTypes.put(Action.NORTHEAST, ActionType.EXIT);
	    state.actionTypes.put(Action.NORTHWEST, ActionType.EXIT);
	    state.actionTypes.put(Action.SOUTHEAST, ActionType.EXIT);
	    state.actionTypes.put(Action.SOUTHWEST, ActionType.EXIT);
	    state.actionTypes.put(Action.UP, ActionType.EXIT);
	    state.actionTypes.put(Action.DOWN, ActionType.EXIT);
	    state.actionTypes.put(Action.IN, ActionType.EXIT);
	    state.actionTypes.put(Action.OUT, ActionType.EXIT);
	    state.actionTypes.put(Action.EXIT, ActionType.EXIT);
	
	    state.actionTypes.put(Action.ANSWER, ActionType.DIRECT);
	    state.actionTypes.put(Action.BLOW, ActionType.DIRECT);
	    state.actionTypes.put(Action.CLIMB, ActionType.DIRECT);
	    state.actionTypes.put(Action.CLOSE, ActionType.DIRECT);
	    state.actionTypes.put(Action.COUNT, ActionType.DIRECT);
	    state.actionTypes.put(Action.CROSS, ActionType.DIRECT);
	    state.actionTypes.put(Action.DEFLATE, ActionType.DIRECT);
	    state.actionTypes.put(Action.DRINK, ActionType.DIRECT);
	    state.actionTypes.put(Action.DROP, ActionType.DIRECT);
	    state.actionTypes.put(Action.EAT, ActionType.DIRECT);
	    state.actionTypes.put(Action.ENTER, ActionType.DIRECT);
	    state.actionTypes.put(Action.EXAMINE, ActionType.DIRECT);
	    state.actionTypes.put(Action.EXTINGUISH, ActionType.DIRECT);
	    state.actionTypes.put(Action.FOLLOW, ActionType.DIRECT);
	    state.actionTypes.put(Action.KICK, ActionType.DIRECT);
	    state.actionTypes.put(Action.KNOCK, ActionType.DIRECT);
	    state.actionTypes.put(Action.LIGHT, ActionType.DIRECT);
	    state.actionTypes.put(Action.LISTEN, ActionType.DIRECT);
	    state.actionTypes.put(Action.LOWER, ActionType.DIRECT);
	    state.actionTypes.put(Action.MOVE_OBJECT, ActionType.DIRECT);
	    state.actionTypes.put(Action.OPEN, ActionType.DIRECT);
	    state.actionTypes.put(Action.POUR, ActionType.DIRECT);
	    state.actionTypes.put(Action.PULL, ActionType.DIRECT);
	    state.actionTypes.put(Action.PUSH, ActionType.DIRECT);
	    state.actionTypes.put(Action.RAISE, ActionType.DIRECT);
	    state.actionTypes.put(Action.READ, ActionType.DIRECT);
	    state.actionTypes.put(Action.TALK_TO, ActionType.DIRECT);
	    state.actionTypes.put(Action.SEARCH, ActionType.DIRECT);
	    state.actionTypes.put(Action.SHAKE, ActionType.DIRECT);
	    state.actionTypes.put(Action.SMELL, ActionType.DIRECT);
	    state.actionTypes.put(Action.STAY, ActionType.DIRECT);
	    state.actionTypes.put(Action.TAKE, ActionType.DIRECT);
	    state.actionTypes.put(Action.TALK_TO, ActionType.DIRECT);
	    state.actionTypes.put(Action.TOUCH, ActionType.DIRECT);
	    state.actionTypes.put(Action.TURN, ActionType.DIRECT);
	    state.actionTypes.put(Action.WAKE, ActionType.DIRECT);
	    state.actionTypes.put(Action.WAVE, ActionType.DIRECT);
	    state.actionTypes.put(Action.WEAR, ActionType.DIRECT);
	    state.actionTypes.put(Action.WIND, ActionType.DIRECT);
	
	    state.actionTypes.put(Action.ATTACK, ActionType.INDIRECT);
	    state.actionTypes.put(Action.BREAK, ActionType.INDIRECT);
	    state.actionTypes.put(Action.BURN, ActionType.INDIRECT);
	    state.actionTypes.put(Action.CUT, ActionType.INDIRECT);
	    state.actionTypes.put(Action.DIG, ActionType.INDIRECT);
	    state.actionTypes.put(Action.FILL, ActionType.INDIRECT);
	    state.actionTypes.put(Action.INFLATE, ActionType.INDIRECT);
	    state.actionTypes.put(Action.UNLOCK, ActionType.INDIRECT);
	    state.actionTypes.put(Action.LOCK, ActionType.INDIRECT);
	    state.actionTypes.put(Action.STRIKE, ActionType.INDIRECT);
	
	    state.actionTypes.put(Action.GIVE, ActionType.INDIRECT_INVERSE);
	    state.actionTypes.put(Action.PUT, ActionType.INDIRECT_INVERSE);
	    state.actionTypes.put(Action.THROW, ActionType.INDIRECT_INVERSE);
	    state.actionTypes.put(Action.TIE, ActionType.INDIRECT_INVERSE);
	    
	
	}

    public void fillDictionary()
    {
        for (int i = 0; i < GameStrings.GAME_WORDS.length; ++i)
        {
            state.dictionary.add(GameStrings.GAME_WORDS[i]);
        }

        for (String name : state.objectList.keySet())
        {
            String[] words = name.split(" ");
            for (int i = 0; i < words.length; ++i)
            {
                state.dictionary.add(words[i]);
                state.gameNouns.add(name);
                
            }

        }

        for (GameObject g : state.objectList.values())
        {
            for (String str : g.altNames)
            {
                state.dictionary.add(str);
                state.gameNouns.add(str);
            }
        }

        for (String str : state.actions.keySet())
        {
            String[] words = str.split(" ");
            for (int i = 0; i < words.length; ++i)
                state.dictionary.add(words[i]);
        }

        if (godmode)
        {
            for (int i = 0; i < GameStrings.GODMODE_WORDS.length; ++i)
            {
                state.dictionary.add(GameStrings.GODMODE_WORDS[i]);
            }
        }
    }

}