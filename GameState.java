import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GameState {

    // gameplay information
    public int turns;
    public int darknessTurns;
    public boolean darkness;
    public boolean lightActivated;
    public Verbosity verbosity;

    // game events
    public boolean houseWindowOpened;
    public boolean carpetMoved;
    public boolean damGatesOpen;
    public boolean damWaterHigh;
    public boolean damWaterLow;
    public boolean waterFalling;
    public boolean waterRising;
    public int damWaterStage;
    public boolean gameWon;
    public boolean leafPileMoved;
    public boolean loudRoomSolved;
    public int matchCount;
    public boolean mirrorBroken;
    public boolean potOfGoldAppeared;
    public boolean rainbowSolid;
    public boolean ropeRailTied;
    public boolean shaftBasketUsed;
    public int spiritCeremonyCount;
    public boolean spiritsBellRung;
    public boolean spiritsCandlesLit;
    public boolean spiritsBanished;
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
    public ActionType playerActionType;
    public Action playerAction;
    public GameObject directObject;
    public GameObject indirectObject;
    public Feature dummyObject;

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
    public static final int WINNING_SCORE = 350;

    public GameState()
    {
        dummyObject = new Feature("dummy_feature", Location.NULL_LOCATION);

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
        shaftBasketUsed = false;
        spiritCeremonyCount = 0;
        spiritsBellRung = false;
        spiritsCandlesLit = false;
        spiritsBanished = false;
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

            if (g.intangible)
                currentObjects.remove(g.name);
        }

        // Individual cases
        if (ropeRailTied && (playerLocation == Location.DOME_ROOM || playerLocation == Location.TORCH_ROOM) )
            currentObjects.put("rope", objectList.get("rope"));



        // Create the list of current object names, which can be sorted
        Object[] keys = currentObjects.keySet().toArray();
        currentObjectNames = new String[keys.length];

        for (int i = 0; i < currentObjectNames.length; ++i)
        {
            currentObjectNames[i] = (String)(keys[i]);
        }

        // Bubble sort
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
        rm.lookAround(this);
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
            case ATTACK: { directObject.attack(this); } break;
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
            case GIVE: { directObject.give(this); } break;
            case GREET: { directObject.greet(this); } break;
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
            case REMOVE: { directObject.remove(this); } break;
            case RING: { directObject.ring(this); } break;
            case SEARCH: { directObject.search(this); } break;
            case SHAKE: { directObject.shake(this); } break;
            case SMELL: { directObject.smell(this); } break;
            case TAKE: {directObject.take(this); } break;
            case TALK_TO: { directObject.talk(this); } break;
            case TIE: {directObject.tie(this); } break;
            case TOUCH: { directObject.touch(this); } break;
            case TURN: { directObject.turn(this); } break;
            case UNLOCK: {directObject.unlock(this); } break;
            case UNTIE: {directObject.untie(this); } break;
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

            case SHOUT: { Game.output("Yaaaaarrrrggghhh!"); } break;

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
            {
                
                if (currentRoom.exit(this, playerAction))
                {
                    Room nextRoom = worldMap.get(playerLocation);
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
                        nextRoom.getRoomObjects(this);
                        Game.output(MapStrings.DESC_LOUD_ROOM_WATER);
                        Game.outputLine();

                        if (choice == 0) relocatePlayer(Location.DAMP_CAVE);
                        if (choice == 1) relocatePlayer(Location.ROUND_ROOM);
                        if (choice == 2) relocatePlayer(Location.DEEP_CANYON);

                        updateActors();
                        ++turns;
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

        if (playerHitPoints <= 0)
            playerDies();

        ++turns;

        calculateScore();

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


    public void updateActors()
    {
        Actor cyclops = (Actor)(objectList.get("cyclops"));
        Actor flood = (Actor)(objectList.get("flood"));
        Actor flow = (Actor)(objectList.get("flow"));
        Actor gustOfWind = (Actor)(objectList.get("gust of wind"));
        Actor riverCurrent = (Actor)(objectList.get("current"));
        Actor songbird = (Actor)(objectList.get("song bird"));
        Actor spirits = (Actor)(objectList.get("spirits"));
        Actor thief = (Actor)(objectList.get("thief"));
        Actor troll = (Actor)(objectList.get("troll"));
        Actor vampireBat = (Actor)(objectList.get("vampire bat"));
        cyclops.cyclopsTurn(this);
        flood.floodTurn(this);
        flow.damFlowTurn(this);
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

        ++turns;

    }


}
