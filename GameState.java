import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameState {

    // gameplay information
    public int turns;
    public int darknessTurns;
    public boolean darkness;
    public boolean lightActivated;
    public Verbosity verbosity;

    // game events
    public boolean baubleFell;
    public boolean bottleFilled;
    public boolean houseWindowOpened;
    public boolean carpetMoved;
    public boolean cyclopsGone;
    public boolean damGatesOpen;
    public boolean damWaterHigh;
    public boolean damWaterLow;
    public boolean waterFalling;
    public boolean waterRising;
    public int damWaterStage;
    public boolean gameWon;
    public boolean gratingOpened;
    public boolean gratingUnlocked;
    public boolean leafPileMoved;
    public boolean loudRoomSolved;
    public int matchCount;
    public boolean mirrorBroken;
    public boolean playerInBoat;
    public boolean potOfGoldAppeared;
    public boolean rainbowSolid;
    public boolean ropeRailTied;
    public int sandStage;
    public boolean scarabFound;
    public boolean shaftBasketLowered;
    public boolean shaftBasketUsed;
    public int spiritCeremonyCount;
    public boolean spiritsBellRung;
    public boolean spiritsCandlesLit;
    public boolean spiritsBanished;
    public int thiefEggTurns;
    public boolean thiefOpenedEgg;
    public boolean trapDoorOpen;
    public boolean winMessageDisplayed;

    public boolean blueButtonPushed;
    public boolean redButtonPushed;
    public boolean yellowButtonPushed;
    public int floodStage;

    // player attributes
    public int playerCarryWeight;
    public boolean playerDead;
    public int playerDeaths;
    public int playerHitPoints;
    public Location playerLocation;
    public Location playerPreviousLocation;
    public int playerScore;
    public String playerScoreRank;
    public boolean playerStaggered;

    // player action
    public String completePlayerInput;
    public String playerPreviousInput;
    public String firstInputPhrase;
    public String secondInputPhrase;
    public String thirdInputPhrase;
    public String speakPhrase;
    public ActionType playerActionType;
    public Action playerAction;
    public GameObject directObject;
    public GameObject indirectObject;
    public GameObject dummyObject;

    // lists of game objects
    public HashMap<String, Action> actions;
    public HashMap<Action, ActionType> actionTypes;
    public String[] actionNames;
    public HashMap<String, GameObject> currentObjects;
    public String[] currentObjectNames;
    public ArrayList<String> dictionary;
    public ArrayList<String> gameNouns;    // used in direct object validation
    public HashMap<String, GameObject> objectList;
    public HashMap<Location, Room> worldMap;

    // Constants
    public static final int CARRY_WEIGHT_LIMIT = 20;
    public static final int DEATH_POINTS = 10;
    public static final int LANTERN_LIFESPAN = 500;
    public static final int MAX_PLAYER_DEATHS = 3;
    public static final int MAX_DARKNESS_TURNS = 2;
    public static final int MAX_HIT_POINTS = 10;
    public static final int MATCH_LIFESPAN = 2;
    public static final int MATCHES_IN_BOOK = 20;
    public static final int RESERVOIR_DRAIN_TURNS = 8;
    public static final int SHAFT_BASKET_POINTS = 13;
    public static final int SPIRIT_CEREMONY_LENGTH = 5;
    public static final int THIEF_OPENS_EGG = 5;
    public static final int WINNING_SCORE = 350;

    public GameState()
    {
        dummyObject = new Feature("dummy_object", Location.NULL_LOCATION);

        turns = 0;
        darknessTurns = 0;
        darkness = false;
        lightActivated = false;
        verbosity = Verbosity.BRIEF;

        playerCarryWeight = 0;
        playerDead = false;
        playerDeaths = 0;
        playerHitPoints = MAX_HIT_POINTS;
        playerLocation = Location.NULL_LOCATION;
        playerPreviousLocation = Location.NULL_LOCATION;
        playerScore = 0;
        playerScoreRank = "";
        playerStaggered = false;

        completePlayerInput = "";
        playerPreviousInput = "";

        bottleFilled = false;
        carpetMoved = false;
        damGatesOpen = false;
        damWaterHigh = true;
        damWaterLow = false;
        damWaterStage = RESERVOIR_DRAIN_TURNS;
        gameWon = false;
        houseWindowOpened = false;
        leafPileMoved = false;
        loudRoomSolved = false;
        matchCount = MATCHES_IN_BOOK;
        mirrorBroken = false;
        potOfGoldAppeared = false;
        ropeRailTied = false;
        rainbowSolid = false;
        sandStage = 0;
        scarabFound = false;
        shaftBasketLowered = false;
        shaftBasketUsed = false;
        spiritCeremonyCount = 0;
        spiritsBellRung = false;
        spiritsCandlesLit = false;
        spiritsBanished = false;
        trapDoorOpen = false;
        winMessageDisplayed = false;

        blueButtonPushed = false;
        redButtonPushed = false;
        yellowButtonPushed = false;
        floodStage = 0;
        
        resetInput();

        actions = new HashMap<String, Action>();
        actionTypes = new HashMap<Action, ActionType>();
        actionNames = null;
        currentObjects = new HashMap<String, GameObject>();
        currentObjectNames = null;
        dictionary = new ArrayList<String>();
        gameNouns = new ArrayList<String>();
        objectList = new HashMap<String, GameObject>();
        worldMap = new HashMap<Location, Room>();

        objectList.put(dummyObject.name, dummyObject);

    }


    public boolean boatCheck()
    {
        
        boolean result = true;

        switch (playerAction)
        {
            case ATTACK:
            case CLIMB:
            case DEFLATE:
            case TIE:
            case KICK:
            case DIG:
            {
                result = false;
            } break;

            case TAKE:
            {
                if (directObject.name.equals("magic boat"))
                {
                    Game.output("You can't take the boat while you're inside it!");
                    result = false;
                }
            } break;

            default: {} break;
        }

        if ( (playerActionType == ActionType.DIRECT || playerActionType == ActionType.INDIRECT) 
             && directObject.location == playerLocation)
            result = true;

        if (worldMap.get(playerLocation).bodyOfWater)
            result = true;

        return result;
    }


    public void bottleCheck(GameObject g)
    {
        if (g.name.equals("glass bottle") && bottleFilled)
        {
            Feature water = (Feature)objectList.get("quantity of water");
            currentObjects.put("water", water);
            currentObjects.put("quantity of water", water);
        }    
    }


    public void calculateScore()
    {
        playerScore = 0;

        for (GameObject g : objectList.values())
        {
            if (g.isItem())
            {
                Item it = (Item)(g);

                if (it.location == Location.INSIDE_TROPHY_CASE)
                    playerScore += it.trophyCaseValue;

                if (it.acquired)
                    playerScore += it.acquireValue;
            }
        }

        for (Room r : worldMap.values())
        {
            if (r.firstVisit == false)
                playerScore += r.discoverValue;
        }

        if (shaftBasketUsed)
            playerScore += SHAFT_BASKET_POINTS;

        playerScore -= playerDeaths * DEATH_POINTS;

        if (playerScore >= 350) playerScoreRank = "Master Adventurer";
        else if (playerScore >= 330) playerScoreRank = "Wizard";
        else if (playerScore >= 300) playerScoreRank = "Master";
        else if (playerScore >= 200) playerScoreRank = "Adventurer";
        else if (playerScore >= 100) playerScoreRank = "Junior Adventurer";
        else if (playerScore >= 50)  playerScoreRank = "Novice Adventurer";
        else if (playerScore >= 25) playerScoreRank = "Amateur Adventurer";
        else playerScoreRank = "Beginner";

        if (playerScore >= WINNING_SCORE)
        {
            gameWon = true;
            if (!winMessageDisplayed)
            {
                Game.output(GameStrings.ALL_TREASURES_IN_CASE);
                winMessageDisplayed = true;

                Item map = (Item)objectList.get("ancient map");
                map.location = Location.INSIDE_TROPHY_CASE;

                Room rm = worldMap.get(Location.WEST_OF_HOUSE);
                Passage p = rm.exits.get(Action.SOUTHWEST);
                p.open();
            }
        }

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

            // If the light source is in an open container in the same room
            for (GameObject g : objectList.values())
            {
                if (g.isContainer() && g.isOpen() && source.location == g.inventoryID)
                    lightActivated = true;
            }
        
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

            // Object in the player's location or inventory
            if (g.location == playerLocation ||
                g.altLocations.contains(playerLocation) ||
                g.playerHasObject())
            {
                currentObjects.put(g.name, g);

                for (String str : g.altNames)
                    currentObjects.put(str, g);

                bottleCheck(g);             
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

                    bottleCheck(it);
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

                    if (it.isContainer() && it.isOpen())
                    {
                        for (Item nIt : it.inventory)
                        {
                            currentObjects.put(nIt.name, nIt);

                            for (String nStr : nIt.altNames)
                                currentObjects.put(nStr, nIt);
                        }
                    }

                    bottleCheck(it);
                }
            }

            if (g.intangible)
                currentObjects.remove(g.name);
        
        }


        // Individual cases

        // The rope tied to the railing
        if (ropeRailTied && (playerLocation == Location.DOME_ROOM || playerLocation == Location.TORCH_ROOM) )
            currentObjects.put("rope", objectList.get("rope"));

        // Items in the shaft basket
        if ( (playerLocation == Location.SHAFT_ROOM && !shaftBasketLowered) ||
             (playerLocation == Location.DRAFTY_ROOM && shaftBasketLowered) )
        {
            Container basket = (Container)objectList.get("basket");
            for (Item it : basket.inventory)
            {
                currentObjects.put(it.name, it);

                for (String str : it.altNames)
                    currentObjects.put(str, it);

                bottleCheck(it);
            }
        }

        else
        {
            Container basket = (Container)objectList.get("basket");
            for (Item it : basket.inventory)
            {
                currentObjects.remove(it.name);

                for (String str : it.altNames)
                    currentObjects.remove(str);
            }
        }


        // Create the list of current object names, which can be sorted
        Object[] keys = currentObjects.keySet().toArray();
        currentObjectNames = new String[keys.length];

        for (int i = 0; i < currentObjectNames.length; ++i)
        {
            currentObjectNames[i] = (String)(keys[i]);
        }

        // Bubble sort by length
        for (int x = 0; x < currentObjectNames.length - 1; ++x)
        {
            for (int y = x + 1; y < currentObjectNames.length; ++y)
            {
                if (currentObjectNames[x].length() < currentObjectNames[y].length())
                {
                    String temp = currentObjectNames[x];
                    currentObjectNames[x] = currentObjectNames[y];
                    currentObjectNames[y] = temp;
                }
            }
        }

    }

    public void getNextRoomDescription(Location nextLoc)
    {

        Room nextRoom = worldMap.get(nextLoc);
        switch(verbosity)
        {
            case SUPERBRIEF:
            {
                nextRoom.getRoomObjects();

            } break;

            case BRIEF:
            {
                if (nextRoom.firstVisit)
                {
                    Game.outputLine();
                    nextRoom.getDescription();
                }
                
                nextRoom.getRoomObjects();

            } break;

            case VERBOSE:
            {
                Game.outputLine();
                nextRoom.getDescription();
                nextRoom.getRoomObjects();
            } break;

            default: {} break;
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
                   g.location = Location.LIVING_ROOM;
           }

        Room altar = worldMap.get(Location.ALTAR);

        if (playerDeaths % MAX_PLAYER_DEATHS == 0 && !altar.firstVisit)
        {
            playerDiesForReal();
        }

        else
        {
            Game.output(GameStrings.PLAYER_DIES);
            Game.outputLine();
            int p = rnd.nextInt(forest.length);

            playerPreviousLocation = playerLocation;
            playerLocation = forest[p];
            playerHitPoints = MAX_HIT_POINTS;
            Room path = worldMap.get(playerLocation);
            path.lookAround();
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

        playerCarryWeight = 0;

        for (GameObject g : objectList.values())
        {
            if (g.isItem() && g.location == Location.PLAYER_INVENTORY)
            {
                Item it = (Item)(g);
                playerCarryWeight += it.weight;
            }
        }

        Item coffin = (Item)(objectList.get("gold coffin"));
        Room altar = worldMap.get(Location.ALTAR);
        Passage p = altar.exits.get(Action.DOWN);
        if (coffin.location == Location.PLAYER_INVENTORY)
        {
            p.weightFail = "You haven't a prayer of getting the coffin down there.";
        }
        else
        {
            p.weightFail = "You can't get down there with what you're carrying.";
        }

    }


    public void relocatePlayer(Location loc)
    {
        playerPreviousLocation = playerLocation;
        playerLocation = loc;
        Room rm = worldMap.get(loc);
        darknessCheck();
        rm.lookAround();
        rm.firstVisit = false;

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
        speakPhrase = "";
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

        Item boat = (Item)objectList.get("magic boat");

        if (playerInBoat && !boatCheck())
        {
            Game.output("You need to get out of the boat first.");
            return;
        }

        if (directObject.name.equals("basket"))
        {
            if (playerAction == Action.RAISE || playerAction == Action.LOWER) {}

            else if ( (playerLocation == Location.SHAFT_ROOM && shaftBasketLowered) ||
                 (playerLocation == Location.DRAFTY_ROOM && !shaftBasketLowered) )
            {
                Game.output("The basket is at the other end of the chain.");
                updateActors();
                updateItems();
                calculateScore();

                ++turns;

                return;
            }
            
        }

        switch (playerAction)
        {
            /* ACTION ON AN OBJECT */
            case ANSWER: { directObject.answer(); } break;
            case ATTACK: { directObject.attack(); } break;
            case BLOW: { directObject.blow(); } break;
            case BOARD: { directObject.board(); } break;
            case BREAK: { directObject.breakObject(); } break;
            case BRUSH: { directObject.brush(); } break;
            case CLIMB: {directObject.climb(); } break;
            case CLOSE: {directObject.close(); } break;
            case COUNT: { directObject.count(); } break;
            case CROSS: { directObject.cross(); } break;
            case DEFLATE: { directObject.deflate(); } break;
            case DIG: { directObject.dig(); } break;
            case DRINK: { directObject.drink(); } break;
            case DROP: {directObject.drop(); } break;
            case EAT: { directObject.eat(); } break;
            case ENTER: { directObject.enter(); } break;
            case EXAMINE: { directObject.examine(); } break;
            case EXTINGUISH: { directObject.extinguish(); } break;
            case FOLLOW: { directObject.follow(); } break;
            case GIVE: { directObject.give(); } break;
            case GREET: { directObject.greet(); } break;
            case INFLATE: { directObject.inflate(); } break;
            case KICK: { directObject.kick(); } break;
            case KNOCK: { directObject.knock(); } break;
            case LAUNCH: { directObject.launch(); } break;
            case LIGHT: { directObject.light(); } break;
            case LISTEN: { directObject.listen(); } break;
            case LOCK: {directObject.lock(); } break;
            case LOOK_IN: {directObject.lookIn(); } break;
            case LOOK_OUT: {directObject.lookOut(); } break;
            case LOOK_UNDER: {directObject.lookUnder(); } break;
            case MOVE_OBJECT: { directObject.move(); } break;
            case LOWER: { directObject.lower(); } break;
            case OPEN: {directObject.open(); } break;
            case POUR: { directObject.pour(); } break;
            case PULL: { directObject.pull(); } break;
            case PUT: { directObject.put(); } break;
            case PUSH: { directObject.push(); } break;
            case RAISE: { directObject.raise(); } break;
            case READ: { directObject.read(); } break;
            case REMOVE: { directObject.remove(); } break;
            case REPAIR: { directObject.repair(); } break;
            case RING: { directObject.ring(); } break;
            case SEARCH: { directObject.search(); } break;
            case SHAKE: { directObject.shake(); } break;
            case SMELL: { directObject.smell(); } break;
            case TAKE: {directObject.take(); } break;
            case TALK_TO: { directObject.talk(); } break;
            case TIE: {directObject.tie(); } break;
            case TOUCH: { directObject.touch(); } break;
            case TURN: { directObject.turn(); } break;
            case UNLOCK: {directObject.unlock(); } break;
            case UNTIE: {directObject.untie(); } break;
            case WAKE: { directObject.wake(); } break;
            case WAVE: { directObject.wave(); } break;
            case WEAR: { directObject.wear(); } break;
            case WIND: { directObject.wind(); } break;

            /* REFLEXIVE GAME ACTIONS */
            case DEBOARD:
            {

                if (playerInBoat)
                {
                    if (currentRoom.bodyOfWater)
                        Game.output("You realize that getting out here would be fatal.");
                    else
                    {
                        Game.output("You are on your own feet again.");
                        playerInBoat = false;
                            
                    }
                }

                else if (boat.location == Location.PLAYER_INVENTORY || boat.location == playerLocation)
                {
                    Game.output("You're already not in the boat.");
                }

                else
                    Game.output("There is nothing to get out of.");

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

                    if (item.location == Location.PLAYER_INVENTORY && item.name.equals("glass bottle") && bottleFilled)
                        Game.output("The glass bottle contains:\n  A quantity of water");

                    if (item.location == Location.PLAYER_INVENTORY && item.isContainer()
                        && item.isOpen())
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
                                    Game.output("  " + it.capArticleName);
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
                currentRoom.lookAround();
            } break;

            case PRAY:
            {
                if (playerLocation == Location.ALTAR)
                {
                    relocatePlayer(Location.FOREST_WEST);
                }

                else
                {
                    Game.output("If you pray enough, your prayers may be answered.");
                }
            } break;

            case SAY:
            {
                Actor clops = (Actor)objectList.get("cyclops");

                if (speakPhrase.equals("ulysses") || speakPhrase.equals("odysseus"))
                {
                    if (playerLocation == Location.CYCLOPS_ROOM &&
                        clops.location == Location.CYCLOPS_ROOM)
                    {
                        Game.output(ObjectStrings.CYCLOPS_FLEES);
                        clops.alive = false;
                        cyclopsGone = true;
                        clops.location = Location.NULL_LOCATION;
                        Room strange = worldMap.get(Location.STRANGE_PASSAGE);
                        Room clopsRoom = worldMap.get(Location.CYCLOPS_ROOM);
                        Room cell = worldMap.get(Location.CELLAR);
                        Passage p1 = strange.exits.get(Action.EAST);
                        Passage p2 = strange.exits.get(Action.WEST);
                        Passage p3 = clopsRoom.exits.get(Action.UP);
                        Passage p4 = cell.exits.get(Action.UP);
                        p1.open();
                        p2.open();
                        p3.open();
                        p4.message = "";
                    }

                    else
                        Game.output("Wasn't he a sailor?");
                }

                else
                    Game.output("\"" + speakPhrase + "\" yourself.");

            } break;

            case SHOUT: { Game.output("Yaaaaarrrrggghhh!"); } break;

            case SWIM:
            {
                Game.output("You need to wait an hour after eating first.");
            } break;

            case WAIT:
            {
                if (playerHitPoints < MAX_HIT_POINTS) ++playerHitPoints;
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
            case LAND:
            {
                
                if (currentRoom.exit(playerAction))
                {
                    Room nextRoom = worldMap.get(playerLocation);

                    if (playerInBoat)
                        Game.output(nextRoom.name + ", in the magic boat");
                    else
                        Game.output(nextRoom.name);

                    darknessCheck();

                    if (nextRoom.isDark() && !lightActivated)
                    {
                        Game.lineOutput(GameStrings.ENTER_DARKNESS);
                    }

                    if (nextRoom.roomID == Location.LOUD_ROOM && waterFalling)
                    {
                        Random rand = new Random();
                        int choice = rand.nextInt(3);

                        Game.outputLine();
                        nextRoom.getRoomObjects();
                        Game.output(MapStrings.DESC_LOUD_ROOM_WATER);
                        Game.outputLine();

                        if (choice == 0) relocatePlayer(Location.DAMP_CAVE);
                        if (choice == 1) relocatePlayer(Location.ROUND_ROOM);
                        if (choice == 2) relocatePlayer(Location.DEEP_CANYON);

                        updateActors();
                        ++turns;
                        return;                       
                    }

                    getNextRoomDescription(playerLocation);
                    

                    if (nextRoom.firstVisit)
                        nextRoom.firstVisit = false;

                    if (nextRoom.roomID == Location.GAS_ROOM)
                    {
                        boolean flameCheck = false;

                        Item torch = (Item)objectList.get("torch");
                        Item candles = (Item)objectList.get("pair of candles");
                        Item match = (Item)objectList.get("matchbook");

                        if (torch.location == Location.PLAYER_INVENTORY && torch.activated)
                            flameCheck = true;
                        if (candles.location == Location.PLAYER_INVENTORY && candles.activated)
                            flameCheck = true;
                        if (match.location == Location.PLAYER_INVENTORY && match.activated)
                            flameCheck = true;

                        if (flameCheck)
                        {
                            Game.outputLine();
                            Game.output(GameStrings.GAS_EXPLOSION);
                            playerDies();
                        }
                    }


                }

            } break; 
            

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

            case RESTART:
            {
                Game.restart();            
                return;
            }

            case SCORE:
            {
                calculateScore();
                Game.output("Your score is " + playerScore + ".");
                Game.output("This gives you the rank of " + playerScoreRank + ".");

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

        updateActors();
        updateItems();
        calculateScore();

        ++turns;
       
    }


    public void updateActors()
    {
        Actor cyclops = (Actor)(objectList.get("cyclops"));
        Actor flood = (Actor)(objectList.get("flood"));
        Actor flow = (Actor)(objectList.get("flow"));
        Actor gustOfWind = (Actor)(objectList.get("gust of wind"));
        Actor riverCurrent = (Actor)(objectList.get("current"));
        Actor songbird = (Actor)(objectList.get("song bird"));
        Actor spirits = (Actor)(objectList.get("spirits"));
        Actor swordGlow = (Actor)(objectList.get("glow"));
        Actor thief = (Actor)(objectList.get("thief"));
        Actor troll = (Actor)(objectList.get("troll"));
        Actor vampireBat = (Actor)(objectList.get("vampire bat"));
        cyclops.cyclopsTurn();
        flood.floodTurn();
        flow.damFlowTurn();
        gustOfWind.gustOfWindTurn();
        riverCurrent.riverCurrentTurn();
        songbird.songbirdTurn();
        spirits.spiritsTurn();
        swordGlow.swordGlowTurn();
        thief.thiefTurn();
        troll.trollTurn();
        vampireBat.vampireBatTurn();

        if (playerHitPoints <= 0)
            playerDies();

    }


    public void updateItems()
    {
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


        GameObject boat = objectList.get("magic boat");

        if (playerInBoat)
        {
            boat.location = playerLocation;
            boat.presenceString = "";
            worldMap.get(Location.DAM_BASE).addFailMessage(Action.EAST, "Refer to the boat label for instructions.");
            worldMap.get(Location.WHITE_CLIFFS_BEACH_NORTH).addFailMessage(Action.EAST, "Refer to the boat label for instructions.");
            worldMap.get(Location.WHITE_CLIFFS_BEACH_SOUTH).addFailMessage(Action.EAST, "Refer to the boat label for instructions.");
            worldMap.get(Location.SANDY_BEACH).addFailMessage(Action.WEST, "Refer to the boat label for instructions.");
            worldMap.get(Location.SHORE).addFailMessage(Action.WEST, "Refer to the boat label for instructions.");
            worldMap.get(Location.RESERVOIR_SOUTH).addFailMessage(Action.NORTH, "Refer to the boat label for instructions.");
            worldMap.get(Location.RESERVOIR_NORTH).addFailMessage(Action.SOUTH, "Refer to the boat label for instructions.");
            worldMap.get(Location.STREAM_VIEW).addFailMessage(Action.NORTH, "Refer to the boat label for instructions.");
        }

        if (!playerInBoat)
        {
            boat.presenceString = "There is a magic boat here.";
            worldMap.get(Location.DAM_BASE).removeFailMessage(Action.EAST);
            worldMap.get(Location.WHITE_CLIFFS_BEACH_NORTH).removeFailMessage(Action.EAST);
            worldMap.get(Location.WHITE_CLIFFS_BEACH_SOUTH).removeFailMessage(Action.EAST);
            worldMap.get(Location.SANDY_BEACH).removeFailMessage(Action.WEST);
            worldMap.get(Location.SHORE).removeFailMessage(Action.WEST);
            worldMap.get(Location.RESERVOIR_SOUTH).removeFailMessage(Action.NORTH);
            worldMap.get(Location.RESERVOIR_NORTH).removeFailMessage(Action.SOUTH);
            worldMap.get(Location.STREAM_VIEW).removeFailMessage(Action.NORTH);
        }
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
                directObject.drop();
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
                directObject.light();
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
                if (currentRoom.exit(playerAction))
                {
                    Room nextRoom = worldMap.get(playerLocation);
                    Game.output(nextRoom.name);

                    darknessCheck();

                    if (darkness)
                    {
                        Game.output(GameStrings.ENTER_DARKNESS);
                        // return;
                    }

                    getNextRoomDescription(playerLocation);

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
        ++turns;

    }


    public void updateDeath()
    {
        Room currentRoom = worldMap.get(playerLocation);

        switch (playerAction)
        {
            case ATTACK:
            {
                Game.output("All such attacks are vain in your condition.");
            } break;

            case GREET:
            {
                Game.output("The dead may not greet the living.");
            } break;

            case INVENTORY:
            {
                Game.output(GameStrings.DEAD_INVENTORY);
            } break;

            case LIGHT:
            {
                Game.output("You need no light to guide you.");
            } break;

            case LOOK:
            {
                currentRoom.lookAround();

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
                
                if (currentRoom.exit(playerAction))
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
                        rm.lookAround();
                        return;
                    }

                    getNextRoomDescription(playerLocation);

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

        ++turns;

    }


}
