import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
    
    public boolean alive;
    public boolean cyclopsAggro;
    public int cyclopsCycle;
    public boolean cyclopsFirstTurn;
    public boolean cyclopsThirsty;
    public boolean disarmed;
    public boolean firstCombatTurn;
    public int hitPoints;
    public int riverTurns;
    public boolean staggered;
    public int swordGlowLevel;
    public boolean thiefAggro;
    public boolean thiefFirstTurn;
    public boolean thiefItemsHidden;
    public boolean unconscious;

    public static final int CYCLOPS_CYCLE_MAX = 8;
    public static final int MAX_ENEMY_HIT_POINTS = 10;
    public static final int THIEF_ENCOUNTER_PERCENT = 2;
    public static final int SONGBIRD_CHIRP_PERCENT = 15;


    public Actor(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.ACTOR;

        alive = true;
        cyclopsAggro = false;
        cyclopsCycle = 0;
        cyclopsFirstTurn = true;
        cyclopsThirsty = false;
        disarmed = false;
        firstCombatTurn = true;
        hitPoints = MAX_ENEMY_HIT_POINTS;
        riverTurns = 0;
        staggered = false;
        swordGlowLevel = 0;
        thiefAggro = false;
        thiefFirstTurn = true;
        thiefItemsHidden = false;
        unconscious = false;
        presenceString = "";

    }


    @Override
    public void attack(GameState state)
    {
        GameObject weapon = state.indirectObject;

        switch (name)
        {
            case "cyclops":
            case "thief":
            case "troll":
            {
                if (weapon.name.equals("dummy_feature"))
                {
                    Game.output("Trying to attack the " + name + " with your bare hands is suicidal.");
                    return;
                }

                if (!weapon.isWeapon || weapon.name.equals("sceptre"))
                {
                    Game.output("Attacking the " + name + " with " + weapon.articleName + " is suicide.");
                    return;
                }

                 // Should this just be when delivering a killing blow? No...
                if (weapon.name.equals("rusty knife"))
                {
                    Game.output(ObjectStrings.RUSTY_KNIFE_CURSE);
                    state.playerDies();
                    return;
                }

                if (state.playerStaggered)
                {
                    Game.output(GameStrings.COMBAT_STAGGERED);
                    state.playerStaggered = false;
                    return;
                }

                if (unconscious)
                {
                    Game.output(GameStrings.COMBAT_FINISH_UNCONSCIOUS);
                    alive = false;
                }

                else if (disarmed)
                {
                    Game.output(GameStrings.COMBAT_FINISH_DISARMED);
                    alive = false;
                }

                else
                {
                    if (name.equals("troll")) trollCombat(state);
                    if (name.equals("thief")) thiefCombat(state);
                    if (name.equals("cyclops")) cyclopsCombat(state);
                }


            } break;

            default:
            {
                super.attack(state);
            } break;
        }

        if (hitPoints <= 0) alive = false;

        if (!alive)
        {
            if (name.equals("thief")) thiefDies(state);
            if (name.equals("troll")) trollDies(state);
        }

    }


    @Override
    public void give(GameState state)
    {
        switch (name)
        {
            case "cyclops":
            {
                switch (state.indirectObject.name)
                {
                    case "lunch":
                    {
                        Game.output(ObjectStrings.CYCLOPS_LUNCH_1);
                        state.indirectObject.location = Location.NULL_LOCATION;
                        cyclopsThirsty = true;

                    } break;

                    case "glass bottle":
                    {
                        if (state.bottleFilled && cyclopsThirsty)
                        {
                            Game.output(ObjectStrings.CYCLOPS_DRINK_2);
                            unconscious = true;
                            cyclopsThirsty = false;
                            state.bottleFilled = false;
                            state.indirectObject.location = state.playerLocation;
                        }

                        else if (!cyclopsThirsty)
                            Game.output(ObjectStrings.CYCLOPS_DRINK_1);

                        else
                            Game.output(ObjectStrings.CYCLOPS_GIVE_REJECT_1);

                    } break;

                    default:
                    {
                        Game.output(ObjectStrings.CYCLOPS_GIVE_REJECT_2);
                    } break;
                }
            } break;

            case "thief":
            {
                Item it = (Item)(state.indirectObject);
                it.location = Location.THIEF_INVENTORY;

                if (it.trophyCaseValue > 0)
                {
                    Game.output(ObjectStrings.THIEF_GIVE_TREASURE);
                    staggered = true;
                }
                else
                    Game.output(ObjectStrings.THIEF_GIVE_ITEM);


            } break;
            case "troll":
            {
                trollGive(state);
            } break;

            default:
            {
                super.give(state);
            } break;
        }

    }


    @Override
    public void kick(GameState state)
    {
        switch (name)
        {
            case "vampire bat":
            {
                Game.output(ObjectStrings.BAT_CEILING);
            } break;

            default:
            {
                super.kick(state);
            } break;
        }

    }

    
    public void cyclopsCombat(GameState state)
    {
        if (unconscious)
        {
            Game.output(ObjectStrings.CYCLOPS_WAKE);
            unconscious = false;
            Room c = state.worldMap.get(Location.CYCLOPS_ROOM);
            Passage p = c.exits.get(Action.UP);
            p.close();
            return;
        }

        cyclopsAggro = true;
        Game.output(ObjectStrings.CYCLOPS_SHRUG);


    }


    public void cyclopsTurn(GameState state)
    {
        if (!alive) return;

        if (state.playerLocation == Location.CELLAR &&
            state.playerPreviousLocation == Location.LIVING_ROOM)
        {
            state.trapDoorOpen = false;
            Room rm = state.worldMap.get(Location.CELLAR);
            Passage p = rm.exits.get(Action.UP);
            p.close();
        }

        if (state.playerLocation != Location.CYCLOPS_ROOM)
        {
            cyclopsFirstTurn = true;
            presenceString = "";
            return;
        }

        if (cyclopsFirstTurn)
        {
            Game.output(ObjectStrings.CYCLOPS_1);
            presenceString = ObjectStrings.CYCLOPS_2;
            cyclopsFirstTurn = false;
            cyclopsCycle = 0;
        }

        if (cyclopsThirsty)
        {
            Game.output(ObjectStrings.CYCLOPS_LUNCH_2);
            ++cyclopsCycle;
            if (cyclopsCycle == CYCLOPS_CYCLE_MAX - 1)
            {
                Game.output(ObjectStrings.CYCLOPS_WAIT_7);
                state.playerDies();
            }

        }

        else if (unconscious)
        {
            presenceString = ObjectStrings.CYCLOPS_SLEEP_1;

            Room c = state.worldMap.get(Location.CYCLOPS_ROOM);
            Passage p = c.exits.get(Action.UP);
            p.open();

            Random rand = new Random();
            int option = rand.nextInt(5);
            if (option == 0 || option == 1) Game.output(ObjectStrings.CYCLOPS_SLEEP_1);
            if (option == 2 || option == 3) Game.output(ObjectStrings.CYCLOPS_SLEEP_2);
            if (option == 4)
            {
                Game.output(ObjectStrings.CYCLOPS_WAKE);
                unconscious = false;
                p.close();
            }
        }

        else if (cyclopsAggro)
        {
            // Game.output("The cyclops smashes your stupid face.");

            Random rand = new Random();
            int dieRoll = rand.nextInt(100);

            if (0 <= dieRoll && dieRoll < 10)
            {
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.CYCLOPS_FIGHT_MISS_1);
                if (option == 1) Game.output(ObjectStrings.CYCLOPS_FIGHT_MISS_2);
            }

            else if (10 <= dieRoll && dieRoll < 20)
            {
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.CYCLOPS_FIGHT_LIGHT_1);
                if (option == 1) Game.output(ObjectStrings.CYCLOPS_FIGHT_LIGHT_2);
                state.playerHitPoints -= 3;
            }

            else if (20 <= dieRoll && dieRoll < 45)
            {
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.CYCLOPS_FIGHT_SEVERE_1);
                if (option == 1) Game.output(ObjectStrings.CYCLOPS_FIGHT_SEVERE_2);
                state.playerHitPoints -= 9;
                state.playerStaggered = true;
            }

            else if (45 <= dieRoll && dieRoll < 65)
            {
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.CYCLOPS_FIGHT_STAGGER_1);
                if (option == 1) Game.output(ObjectStrings.CYCLOPS_FIGHT_STAGGER_2);
                state.playerHitPoints -= 7;
                state.playerStaggered = true;
            }

            else if (65 <= dieRoll && dieRoll < 75)
            {
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.CYCLOPS_FIGHT_DISARM_1);
                if (option == 1)
                {
                    Game.output(ObjectStrings.CYCLOPS_FIGHT_DISARM_2);
                    state.playerHitPoints -= 2;
                }

                state.indirectObject.location = state.playerLocation;
            }

            else if (75 <= dieRoll && dieRoll < 85)
            {
                Game.output(ObjectStrings.CYCLOPS_FIGHT_KNOCKOUT);
                Game.output(ObjectStrings.CYCLOPS_FIGHT_HESITATE);
                Game.output(ObjectStrings.CYCLOPS_FIGHT_FINISH);
                state.playerDies();
            }

            else if (85 <= dieRoll && dieRoll < 100)
            {
                Game.output(ObjectStrings.CYCLOPS_FIGHT_FATAL);
                state.playerDies();
            }
        }

        else
        {
            String[] saltAndPepper = { "", ObjectStrings.CYCLOPS_WAIT_1, ObjectStrings.CYCLOPS_WAIT_2,
                ObjectStrings.CYCLOPS_WAIT_3, ObjectStrings.CYCLOPS_WAIT_4, ObjectStrings.CYCLOPS_WAIT_5, 
                ObjectStrings.CYCLOPS_WAIT_6, ObjectStrings.CYCLOPS_WAIT_7 };

            if (cyclopsCycle > 0)
                Game.lineOutput(saltAndPepper[cyclopsCycle]);

            if (cyclopsCycle == 7)
                state.playerDies();

            ++cyclopsCycle;
            cyclopsCycle %= CYCLOPS_CYCLE_MAX;
        }

    }


    public void damFlowTurn(GameState state)
    {
        Room resNorth = state.worldMap.get(Location.RESERVOIR_NORTH);
        Room resSouth = state.worldMap.get(Location.RESERVOIR_SOUTH);
        Room res = state.worldMap.get(Location.RESERVOIR);
        Room stream = state.worldMap.get(Location.STREAM);

        Passage res_N = res.exits.get(Action.NORTH);
        Passage res_S = res.exits.get(Action.SOUTH);
        Passage res_STR = res.exits.get(Action.WEST);

        Room resEmpty = state.worldMap.get(Location.RESERVOIR_EMPTY);
        Passage res_empty_N = resEmpty.exits.get(Action.NORTH);
        Passage res_empty_S = resEmpty.exits.get(Action.SOUTH);
        Passage res_empty_STR = resEmpty.exits.get(Action.WEST);

        // Water is falling
        if (state.damGatesOpen && state.damWaterHigh && state.damWaterStage > 0)
        {
            --state.damWaterStage;
            state.waterFalling = true;
            state.waterRising = false;
            resNorth.description = MapStrings.DESC_RESERVOIR_NORTH_FALLING;
            resSouth.description = MapStrings.DESC_RESERVOIR_SOUTH_FALLING;

            // Game.output("Dam water stage is " + state.damWaterStage);

            // Water finishes falling
            if (state.damWaterStage == 0)
            {
                if (state.playerLocation == Location.RESERVOIR_SOUTH ||
                    state.playerLocation == Location.RESERVOIR_NORTH)
                {
                    Game.lineOutput(MapStrings.RESERVOIR_EMPTIES);
                }

                if (state.playerLocation == Location.RESERVOIR)
                {
                    Game.lineOutput(MapStrings.RESERVOIR_EMPTIES_BOAT);
                    Game.outputLine();
                    state.relocatePlayer(Location.RESERVOIR_EMPTY);
                }

                if (state.playerLocation == Location.DEEP_CANYON)
                    Game.lineOutput("The roar of rushing water is quieter now.");
            }

        }

        // Water is rising
        if (!state.damGatesOpen && state.damWaterLow && state.damWaterStage < GameState.RESERVOIR_DRAIN_TURNS)
        {
            ++state.damWaterStage;
            state.waterRising = true;
            state.waterFalling = false;
            resNorth.description = MapStrings.DESC_RESERVOIR_NORTH_RISING;
            resSouth.description = MapStrings.DESC_RESERVOIR_SOUTH_RISING;
            resEmpty.description = MapStrings.RESERVOIR_RISING;
            GameObject boat = state.objectList.get("magic boat");
            // Game.output("Dam water stage is " + state.damWaterStage);

            if (state.playerLocation == Location.RESERVOIR_EMPTY)
            {
                if (state.damWaterStage == 3 || state.damWaterStage == 6)
                    Game.lineOutput(MapStrings.RESERVOIR_RISING);

                if (state.damWaterStage == 4 && state.playerInBoat)
                    Game.lineOutput(MapStrings.RESERVOIR_RISING_BOAT);
            }

            // Water finishes rising and goes over the dam
            if (state.damWaterStage == GameState.RESERVOIR_DRAIN_TURNS)
            {
                if (state.playerLocation == Location.RESERVOIR_SOUTH ||
                    state.playerLocation == Location.RESERVOIR_NORTH)
                {
                    Game.lineOutput(MapStrings.RESERVOIR_FILLS);
                }

                if (state.playerLocation == Location.RESERVOIR_EMPTY)
                {
                    if (state.playerInBoat)
                    {
                        Game.lineOutput(MapStrings.RESERVOIR_FILLS_BOAT);
                        state.playerInBoat = false;
                        boat.location = Location.RESERVOIR_SOUTH;
                    }
                    else
                        Game.lineOutput(MapStrings.RESERVOIR_FILLS_SWIM);

                    state.playerDies();

                }

                if (state.playerLocation == Location.LOUD_ROOM)
                {
                    Random rand = new Random();
                    int choice = rand.nextInt(3);

                    Game.lineOutput(MapStrings.LOUD_ROOM_RUSH);
                    Game.outputLine();

                    if (choice == 0) state.relocatePlayer(Location.DAMP_CAVE);
                    if (choice == 1) state.relocatePlayer(Location.ROUND_ROOM);
                    if (choice == 2) state.relocatePlayer(Location.DEEP_CANYON);
                }

                if (state.playerLocation == Location.DEEP_CANYON)
                    Game.lineOutput("A sound, like that of flowing water, starts to come from below.");

                if (boat.location == Location.RESERVOIR_EMPTY && !state.playerInBoat)
                    boat.location = Location.DAM;
            }
        }

        // Reservoir is empty
        if (state.damWaterStage == 0)
        {
            state.damWaterHigh = false;
            state.damWaterLow = true;
            state.waterFalling = false;
            state.waterRising = false;
            resNorth.description = MapStrings.DESC_RESERVOIR_NORTH_EMPTY;
            resSouth.description = MapStrings.DESC_RESERVOIR_SOUTH_EMPTY;
            resEmpty.description = MapStrings.DESC_RESERVOIR_EMPTY;

            stream.exits.remove(Action.EAST);
            stream.addExit(Action.EAST, res_empty_STR);

            resNorth.exits.remove(Action.LAUNCH);
            resNorth.addExit(Action.SOUTH, res_empty_N);

            resSouth.exits.remove(Action.LAUNCH);
            resSouth.addExit(Action.NORTH, res_empty_S);
        }

        // Reservoir is full
        if (state.damWaterStage == GameState.RESERVOIR_DRAIN_TURNS)
        {
            state.damWaterLow = false;
            state.damWaterHigh = true;
            state.waterFalling = false;
            state.waterRising = false;
            resNorth.description = MapStrings.DESC_RESERVOIR_NORTH;
            resSouth.description = MapStrings.DESC_RESERVOIR_SOUTH;
            res.description = MapStrings.DESC_RESERVOIR;

            stream.exits.remove(Action.EAST);
            stream.addExit(Action.EAST, res_STR);

            resNorth.exits.remove(Action.SOUTH);
            resNorth.addExit(Action.LAUNCH, res_N);

            resSouth.exits.remove(Action.NORTH);
            resSouth.addExit(Action.LAUNCH, res_S);
        }
    }


    public void floodTurn(GameState state)
    {
        if (!state.blueButtonPushed) return;

        int maxFloodStage = 15;

        if (state.floodStage < maxFloodStage)
        {
            Random rand = new Random();
            int step = rand.nextInt(2) + 1;
            state.floodStage += step;
        }

        if (state.floodStage >= maxFloodStage)
        {
            Room rm = state.worldMap.get(Location.DAM_LOBBY);
            Passage psg = rm.exits.get(Action.NORTH);
            psg.close();
            psg.closedFail = "The room is full of water and cannot be entered.";

        }

        if (state.playerLocation != Location.MAINTENANCE_ROOM) return;

        int check = state.floodStage / 2;
        String floodString = "The water level here is now up to your ";

        // ankles, shin, knees, hips, waist, chest, neck
        switch(check)
        {
            case 1: { Game.output(floodString += "ankles."); } break;
            case 2: { Game.output(floodString += "shin."); } break;
            case 3: { Game.output(floodString += "knees."); } break;
            case 4: { Game.output(floodString += "hips."); } break;
            case 5: { Game.output(floodString += "waist."); } break;
            case 6: { Game.output(floodString += "chest."); } break;
            case 7: { Game.output(floodString += "neck."); } break;

            default: {} break;
        }

        if (state.floodStage >= maxFloodStage)
        {
            Game.output("I'm afraid you have done drowned yourself.");
            state.playerDies();
        }

    }


    public void gustOfWindTurn(GameState state)
    {
        Random rand = new Random();
        int chance = rand.nextInt(2);

        if (chance == 0) return;

        if (state.playerLocation == Location.CAVE_SOUTH)
        {
            Item candles = (Item)(state.objectList.get("pair of candles"));
            Item match = (Item)(state.objectList.get("matchbook"));

            if (candles.activated && candles.location == Location.PLAYER_INVENTORY
                && match.activated && match.location == Location.PLAYER_INVENTORY)
            {
                Game.output("A gust of wind blows out your candles AND your match!");
                candles.activated = false;
                match.activated = false;
            }

            else if (candles.activated && candles.location == Location.PLAYER_INVENTORY)
            {
                Game.output("A gust of wind blows out your candles!");
                candles.activated = false;
            }

            else if (match.activated && match.location == Location.PLAYER_INVENTORY)
            {
                Game.output("A gust of wind blows out your match!");
                match.activated = false;
            }

            state.darknessCheck();

        }

        if (state.playerLocation == Location.DRAFTY_ROOM)
        {
            Item candles = (Item)(state.objectList.get("pair of candles"));
            if (candles.activated && candles.location == Location.PLAYER_INVENTORY)
            {
                Game.output("A gust of wind blows out your candles!");
                candles.activated = false;
            }
        }
    }


    public void riverCurrentTurn(GameState state)
    {
        Room room = state.worldMap.get(state.playerLocation);

        if (room.bodyOfWater)
        {
            ++riverTurns;
        }

        if (riverTurns == 2)
        {
            switch (state.playerLocation)
            {
                case FRIGID_RIVER_1:
                {
                    Game.lineOutput("The flow of the river carries you downstream.");
                    Game.outputLine();
                    state.relocatePlayer(Location.FRIGID_RIVER_2);
                } break;

                case FRIGID_RIVER_2:
                {
                    Game.lineOutput("The flow of the river carries you downstream.");
                    Game.outputLine();
                    state.relocatePlayer(Location.FRIGID_RIVER_3);
                } break;

                case FRIGID_RIVER_3:
                {
                    Game.lineOutput("The flow of the river carries you downstream.");
                    Game.outputLine();
                    state.relocatePlayer(Location.FRIGID_RIVER_4);
                } break;

                case FRIGID_RIVER_4:
                {
                    Game.lineOutput("The flow of the river carries you downstream.");
                    Game.outputLine();
                    state.relocatePlayer(Location.FRIGID_RIVER_5);
                } break;

                case FRIGID_RIVER_5:
                {
                    Game.lineOutput(GameStrings.WATERFALL_DEATH_BOAT);
                    Game.outputLine();
                    state.playerInBoat = false;
                    state.playerDies();
                    Item boat = (Item)state.objectList.get("magic boat");
                    boat.location = Location.SHORE;
                } break;

                default: {} break;
            }

            riverTurns = 0;
        }

    }


    public void songbirdTurn(GameState state)
    {
        // The songbird shouldn't chirp on the same turn it drops the bauble.
        if (state.baubleFell)
        {
            state.baubleFell = false;
            return;
        }

        if (altLocations.contains(state.playerLocation))
        {
            Random rand = new Random();
            if (rand.nextInt(100) < SONGBIRD_CHIRP_PERCENT)
                Game.output(ObjectStrings.SONGBIRD);
        }
        
    }


    public void spiritsTurn(GameState state)
    {
        if (state.spiritsBanished) return;

        if (state.playerLocation != Location.ENTRANCE_TO_HADES)
        {
            state.spiritCeremonyCount = 0;
            return;
        }

        if (state.spiritCeremonyCount > 0)
        {
            --state.spiritCeremonyCount;
            if (state.spiritCeremonyCount == 0)
            {
                Game.output(ObjectStrings.SPIRITS_REVERT);
                state.spiritsBellRung = false;
                state.spiritsCandlesLit = false;
            }
        }

        Item candles = (Item)(state.objectList.get("pair of candles"));
        if (candles.location == Location.PLAYER_INVENTORY &&
            candles.activated &&
            state.spiritsBellRung &&
            !state.spiritsCandlesLit)
        {
            state.spiritsCandlesLit = true;
            Game.output(ObjectStrings.CANDLES_LIT_SPIRITS);
        }
    }


    public void swordGlowTurn(GameState state)
    {
        Item sword = (Item)(state.objectList.get("elvish sword"));

        if (sword.location != Location.PLAYER_INVENTORY)
            return;

        int newGlowLevel = 0;

        Actor bat = (Actor)state.objectList.get("vampire bat");
        Actor cyclops = (Actor)state.objectList.get("cyclops");
        Actor spirits = (Actor)state.objectList.get("spirits");
        Actor thief = (Actor)state.objectList.get("thief");
        Actor troll = (Actor)state.objectList.get("troll");

        Actor[] enemies = { bat, cyclops, spirits, thief, troll };

        for (int i = 0; i < enemies.length; ++i)
        {
            if (!enemies[i].alive) continue;
            if (enemies[i].location == Location.NULL_LOCATION) continue;

            // Game.output("Checking sword glow for (Actor): " + enemies[i].name);
            // Game.output("Checking sword glow for (Location): " + enemies[i].location);

            if (state.playerLocation == enemies[i].location)
                newGlowLevel = 2;

            else 
            {
                Room enemyRoom = state.worldMap.get(enemies[i].location);
                for (Passage psg : enemyRoom.exits.values())
                {
                    if (psg.open)
                    {
                        if (psg.locationA == enemies[i].location && psg.locationB == state.playerLocation)
                            newGlowLevel = 1;
                        if (psg.locationB == enemies[i].location && psg.locationA == state.playerLocation)
                            newGlowLevel = 1;

                    }
                }
            }

            if (!enemies[i].alive)
                newGlowLevel = 0;
        }

        boolean check = (newGlowLevel != swordGlowLevel);

        switch (newGlowLevel)
        {
            case 0:
            {
                if (check)
                    Game.output("Your sword is no longer glowing.");
                sword.examineString = "There's nothing special about the elvish sword.";

            } break;

            case 1:
            {
                if (check)
                    Game.output("Your sword is glowing with a faint blue glow.");
                sword.examineString = "Your sword is glowing with a faint blue glow.";

            } break;

            case 2:
            {
                if (check)
                    Game.output("Your sword has begun to glow very brightly.");
                sword.examineString = "Your sword is glowing very brightly.";

            } break;

            default: {} break;    
        }
        
        swordGlowLevel = newGlowLevel;

    }


    public void thiefAttacks(GameState state)
    {
        // Game.output("The thief attacks you, sucka!");
        Random rand = new Random();

        String[] misses = { ObjectStrings.THIEF_FIGHT_MISS_1, ObjectStrings.THIEF_FIGHT_MISS_2, ObjectStrings.THIEF_FIGHT_MISS_3,
                ObjectStrings.THIEF_FIGHT_MISS_4 };
        String[] lightBlows = { ObjectStrings.THIEF_FIGHT_LIGHT_1, ObjectStrings.THIEF_FIGHT_LIGHT_2, ObjectStrings.THIEF_FIGHT_LIGHT_3,
                ObjectStrings.THIEF_FIGHT_LIGHT_4 };
        String[] severeBlows = { ObjectStrings.THIEF_FIGHT_SEVERE_1, ObjectStrings.THIEF_FIGHT_SEVERE_2, ObjectStrings.THIEF_FIGHT_SEVERE_3,
            ObjectStrings.THIEF_FIGHT_SEVERE_4 };
        String[] staggerBlows = { ObjectStrings.THIEF_FIGHT_STAGGER_1, ObjectStrings.THIEF_FIGHT_STAGGER_2, ObjectStrings.THIEF_FIGHT_STAGGER_3 };
        String[] disarmingBlows = { ObjectStrings.THIEF_FIGHT_DISARM_1, ObjectStrings.THIEF_FIGHT_DISARM_2, ObjectStrings.THIEF_FIGHT_DISARM_3 };
        String[] knockoutBlows = { ObjectStrings.THIEF_FIGHT_KNOCKOUT_1, ObjectStrings.THIEF_FIGHT_KNOCKOUT_2 };
        String[] fatalBlows = { ObjectStrings.THIEF_FIGHT_FATAL_1, ObjectStrings.THIEF_FIGHT_FATAL_2, ObjectStrings.THIEF_FIGHT_FATAL_3 };
        String[] hesitations = { ObjectStrings.THIEF_FIGHT_HESITATE_1, ObjectStrings.THIEF_FIGHT_HESITATE_2, ObjectStrings.THIEF_FIGHT_HESITATE_3};
        String[] finishes = { ObjectStrings.THIEF_FIGHT_FINISH_1, ObjectStrings.THIEF_FIGHT_FINISH_2 };

        int missCutoff = 15;
        int lightCutoff = 35;
        int severeCutoff = 50;
        int staggerCutoff = 60;
        int disarmCutoff = 75;
        int knockoutCutoff = 90;

        if (disarmed && !unconscious)
        {
            Game.lineOutput(ObjectStrings.THIEF_RECOVER_STILETTO);
            Item stil = (Item)(state.objectList.get("stiletto"));
            stil.location = Location.THIEF_INVENTORY;
            disarmed = false;
            return;
        }

        if (staggered)
        {
            staggered = false;
            return;
        }

        if (unconscious)
        {
            // 50% chance to recover
            int check = rand.nextInt(2);

            if (check == 0)
            {
                Game.lineOutput(ObjectStrings.THIEF_WAKES);
                unconscious = false;
                presenceString = ObjectStrings.THIEF_PRESENT_2;
            }

            return;
        }

        int dieRoll = rand.nextInt(100);

        if (0 <= dieRoll && dieRoll < missCutoff)
        {
            int phrase = rand.nextInt(misses.length);
                Game.lineOutput(misses[phrase]);
        }

        else if (missCutoff <= dieRoll && dieRoll < lightCutoff)
        {
            int phrase = rand.nextInt(lightBlows.length);
            Game.lineOutput(lightBlows[phrase]);
            state.playerHitPoints -= 1;
        }

        else if (lightCutoff <= dieRoll && dieRoll < severeCutoff)
        {
            int phrase = rand.nextInt(severeBlows.length);
            Game.lineOutput(severeBlows[phrase]);
            state.playerHitPoints -= 5;
        }

        else if (severeCutoff <= dieRoll && dieRoll < staggerCutoff)
        {
            int phrase = rand.nextInt(staggerBlows.length);
            Game.lineOutput(staggerBlows[phrase]);
            state.playerStaggered = true;
        }

        else if (staggerCutoff <= dieRoll && dieRoll < disarmCutoff)
        {
            // If the player hasn't attacked with a weapon, stagger instead.

            if (!state.indirectObject.isWeapon)
            {
                int phrase = rand.nextInt(staggerBlows.length);
                Game.lineOutput(staggerBlows[phrase]);
                state.playerStaggered = true;
                return;
            }

            int phrase = rand.nextInt(disarmingBlows.length);
            Game.lineOutput(disarmingBlows[phrase]);
            state.indirectObject.location = state.playerLocation;
            for (GameObject g : state.objectList.values())
            {
                if (g.isWeapon)
                {
                    Game.lineOutput("Fortunately, you still have " + g.articleName + ".");
                    break;
                }
            }
        }

        else if (disarmCutoff <= dieRoll && dieRoll < knockoutCutoff)
        {
            int phrase = rand.nextInt(knockoutBlows.length);
            Game.lineOutput(knockoutBlows[phrase]);

            phrase = rand.nextInt(hesitations.length);
            Game.lineOutput(hesitations[phrase]);

            phrase = rand.nextInt(finishes.length);
            Game.lineOutput(finishes[phrase]);

            state.playerDies();
        }

        else if (knockoutCutoff <= dieRoll && dieRoll < 100)
        {
            int phrase = rand.nextInt(fatalBlows.length);
            Game.lineOutput(fatalBlows[phrase]);
            Game.lineOutput(GameStrings.COMBAT_HP_ZERO);
            state.playerDies();
        }

        if (state.playerHitPoints <= 0)
        {
            Game.lineOutput(GameStrings.COMBAT_HP_ZERO);
            state.playerDies();

        }

    }


    public void thiefCombat(GameState state)
    {
        firstCombatTurn = false;
        thiefAggro = true;

        String[] misses = { GameStrings.COMBAT_MISS_1, GameStrings.COMBAT_MISS_2, GameStrings.COMBAT_MISS_3,
            GameStrings.COMBAT_PARRY_1, GameStrings.COMBAT_PARRY_2, GameStrings.COMBAT_PARRY_3 };
        String[] lightBlows = { GameStrings.COMBAT_LIGHT_1, GameStrings.COMBAT_LIGHT_2, GameStrings.COMBAT_LIGHT_3,
            GameStrings.COMBAT_LIGHT_4 };
        String[] severeBlows = { GameStrings.COMBAT_SEVERE_1, GameStrings.COMBAT_SEVERE_2, GameStrings.COMBAT_SEVERE_3,
            GameStrings.COMBAT_SEVERE_4 };
        String[] staggerBlows = { GameStrings.COMBAT_STAGGER_1, GameStrings.COMBAT_STAGGER_2, GameStrings.COMBAT_STAGGER_3,
            GameStrings.COMBAT_STAGGER_4 };
        String[] disarmingBlows = { GameStrings.COMBAT_DISARM_1, GameStrings.COMBAT_DISARM_2 };
        String[] knockoutBlows = { GameStrings.COMBAT_KNOCKOUT_1, GameStrings.COMBAT_KNOCKOUT_2, GameStrings.COMBAT_KNOCKOUT_3,
            GameStrings.COMBAT_KNOCKOUT_4 };
        String[] fatalBlows = { GameStrings.COMBAT_FATAL_1, GameStrings.COMBAT_FATAL_2, GameStrings.COMBAT_FATAL_3 };

        // values for the sword - not very useful against the thief
        int missCutoff = 40;
        int lightCutoff = 80;
        int severeCutoff = 85;
        int staggerCutoff = 90;
        int disarmCutoff = 93;
        int knockoutCutoff = 97;

        // Fighting the thief with the knife is a lot more effective.
        if (state.indirectObject.name.equals("nasty knife"))
        {
            // remove the decapitation string. These arrays have strange rules...
            String[] fatals = { GameStrings.COMBAT_FATAL_2, GameStrings.COMBAT_FATAL_3 };
            fatalBlows = fatals;

            missCutoff = 20;
            lightCutoff = 50;
            severeCutoff = 60;
            staggerCutoff = 70;
            disarmCutoff = 80;
            knockoutCutoff = 90;

        }

        // Fighting the thief with the axe is almost completely ineffective.
        if (state.indirectObject.name.equals("bloody axe"))
        {
            missCutoff = 60;
            lightCutoff = 90;
            severeCutoff = 92;
            staggerCutoff = 94;
            disarmCutoff = 96;
            knockoutCutoff = 99;
        }

        Random rand = new Random();
        int dieRoll = rand.nextInt(100);

        if (0 <= dieRoll && dieRoll < missCutoff)
        {
            int phrase = rand.nextInt(misses.length);
            Game.output(misses[phrase]);
        }

        else if (missCutoff <= dieRoll && dieRoll < lightCutoff)
        {
            hitPoints -= 1;

            if (hitPoints <= 0)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.output(fatalBlows[phrase]);
            }

            else
            {
                int phrase = rand.nextInt(lightBlows.length);
                Game.output(lightBlows[phrase]);
            }
        }

        else if (lightCutoff <= dieRoll && dieRoll < severeCutoff)
        {
            hitPoints -= 5;

            if (hitPoints <= 0)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.output(fatalBlows[phrase]);
            }

            else
            {
                int phrase = rand.nextInt(severeBlows.length);
                Game.output(severeBlows[phrase]);
            }         
        }

        else if (severeCutoff <= dieRoll && dieRoll < staggerCutoff)
        {
            int phrase = rand.nextInt(staggerBlows.length);
            Game.output(staggerBlows[phrase]);
            staggered = true;
        }

        else if (staggerCutoff <= dieRoll && dieRoll < disarmCutoff)
        {
            int phrase = rand.nextInt(disarmingBlows.length);
            Game.output(disarmingBlows[phrase]);
            GameObject stil = state.objectList.get("stiletto");
            stil.location = state.playerLocation;
            disarmed = true;
        }

        else if (disarmCutoff <= dieRoll && dieRoll < knockoutCutoff)
        {
            int phrase = rand.nextInt(knockoutBlows.length);
            Game.output(knockoutBlows[phrase]);
            presenceString = ObjectStrings.THIEF_PRESENT_UNCONSCIOUS;
            unconscious = true;
        }

        else if (knockoutCutoff <= dieRoll && dieRoll < 100)
        {
            int phrase = rand.nextInt(fatalBlows.length);
            Game.output(fatalBlows[phrase]);
            alive = false;
        }

    }

 
    public void thiefDies(GameState state)
    {
        for (Item it : inventory)
        {
            it.location = location;
        }
        alive = false;
        location = Location.NULL_LOCATION;

        if (state.playerLocation == Location.TREASURE_ROOM)
            Game.output(ObjectStrings.THIEF_MAGIC_2);

        for (GameObject g : state.objectList.values())
        {
            if (g.location == Location.TREASURE_ROOM_INVISIBLE)
            {
                g.location = Location.TREASURE_ROOM;
                if (state.playerLocation == Location.TREASURE_ROOM)
                    Game.output("The " + g.name + " is now safe to take.");
            }
        }

    }
    
    
    public void thiefLootsRoom(GameState state)
    {
        for (GameObject g : state.objectList.values())
        {
            if (g.isItem() && g.location == this.location)
            {
                Item it = (Item)(g);
                if (it.trophyCaseValue > 0)
                    it.location = Location.THIEF_INVENTORY;
            }
        }

    }


    public void thiefMoves(GameState state)
    {
        Random rand = new Random();
        int thiefPossibleLocations = GameSetup.thiefLocations.length;
        int nextThiefLocation = rand.nextInt(thiefPossibleLocations);
        location = GameSetup.thiefLocations[nextThiefLocation];
        thiefFirstTurn = true;

    }


    public void thiefRobsPlayer(GameState state)
    {
        for (GameObject g : state.objectList.values())
        {
            if (g.isItem() && g.location == Location.PLAYER_INVENTORY)
            {
                Item it = (Item)(g);
                if (it.trophyCaseValue > 0)
                    it.location = Location.THIEF_INVENTORY;
            }
        }

    }


    public void thiefTurn(GameState state)
    {
        if (!alive) return;

        Random rand = new Random();
        boolean playerHasTreasure = false;
        boolean roomHasTreasure = false;

        for (GameObject g : state.objectList.values())
        {
            if (g.isItem())
            {
                Item it = (Item)(g);
                if (it.trophyCaseValue > 0 && it.location == this.location)
                    roomHasTreasure = true;

                if (it.trophyCaseValue > 0 && it.location == Location.PLAYER_INVENTORY)
                    playerHasTreasure = true;
            }
        }

        // Has the player found my secret hideout?
        if (state.playerLocation == Location.TREASURE_ROOM)
        {
            // Did the player just get here?
            if (thiefFirstTurn)
            {
                Game.output(ObjectStrings.THIEF_HIDEOUT);
                location = Location.TREASURE_ROOM;
                thiefFirstTurn = false;
            }

            // Have I already hidden the treasures?
            if (!thiefItemsHidden)
            {
                Game.output(ObjectStrings.THIEF_MAGIC_1);

                for (GameObject g : state.objectList.values())
                {
                    if (g.isItem())
                    {
                        Item it = (Item)(g);
                        if (it.location == Location.TREASURE_ROOM && it.trophyCaseValue > 0)
                            it.location = Location.TREASURE_ROOM_INVISIBLE;
                    }

                }

                thiefItemsHidden = true;
            }

            // Attack without pity!
            thiefAttacks(state);

            // If the player is still here, check sword glow.
            if (state.playerLocation == Location.TREASURE_ROOM)
                swordGlowTurn(state);

            return;
        }

        // Am I in the same room as the player?
        if (location == state.playerLocation)
        {
            // Is the player attacking me?
            if (thiefAggro)
            {
                // Retreat, dropping my bag
                if (hitPoints == 1)
                {
                    Game.output(ObjectStrings.THIEF_FIGHT_RETREAT_2);
                    for (Item it : inventory)
                        it.location = this.location;
                    thiefMoves(state);
                }

                // Retreat, holding my bag
                else if (2 <= hitPoints && hitPoints <= 4)
                {
                    Game.output(ObjectStrings.THIEF_FIGHT_RETREAT_1);
                    thiefMoves(state);
                }

                // Attack the player...
                else
                {
                    thiefAttacks(state);
                }

                return;
            }

            // Did the player just get here?
            if (thiefFirstTurn)
            {
                // I am here.
                // int option = rand.nextInt(2);
                // if (option == 0) Game.output(ObjectStrings.THIEF_PRESENT_1);
                // if (option == 1) Game.output(ObjectStrings.THIEF_PRESENT_2);
                thiefFirstTurn = false;
                return;
            }

            // The player has been here at least one turn and we're not fighting.
            {
                int option = rand.nextInt(5);

                // Wait...
                if (option == 0) return;

                // Rob the player and leave...
                else if (option > 2 && playerHasTreasure)
                {
                    Game.output(ObjectStrings.THIEF_LEAVES_ROBS);
                    thiefRobsPlayer(state);
                    thiefMoves(state);
                }

                // Loot the room and leave...
                else if (option <= 2 && roomHasTreasure)
                {
                    Game.output(ObjectStrings.THIEF_LEAVES_LOOTS);
                    thiefLootsRoom(state);
                    thiefMoves(state);
                }

                // Leave without taking anything.
                else
                {
                    if (option > 2) Game.output(ObjectStrings.THIEF_LEAVES_1);
                    if (option <= 2) Game.output(ObjectStrings.THIEF_LEAVES_2);
                    thiefMoves(state);
                }

                return;
            }
        }

        // I'm not in the same room as the player. Let's move!
        if (location != state.playerLocation)
        {
            thiefMoves(state);

            // Check if the player is in a possible thief location
            boolean playerInThiefArea = false;
            for (int i = 0; i < GameSetup.thiefLocations.length; ++i)
            {
                if (GameSetup.thiefLocations[i] == state.playerLocation)
                    playerInThiefArea = true;
            }

            // Move the thief to the player if we roll an encounter.
            int encounterCheck = rand.nextInt(100);
            if (encounterCheck < THIEF_ENCOUNTER_PERCENT && playerInThiefArea)
            {
                this.location = state.playerLocation;
                int option = rand.nextInt(3);
                if (option == 0)
                {
                    Game.output(ObjectStrings.THIEF_ARRIVES_GRIN);
                    
                }

                if (option == 1)
                {
                    Game.output(ObjectStrings.THIEF_COMES_AND_ROBS);
                    while (location == state.playerLocation)
                    {
                        thiefRobsPlayer(state);
                        thiefLootsRoom(state);
                        thiefMoves(state);
                    }
                }

                if (option == 2)
                {
                    Game.output(ObjectStrings.THIEF_COMES_AND_GOES);
                    while (location == state.playerLocation)
                    {
                        thiefMoves(state);
                    }
                }

            }
        }

        // Update egg
        Item egg = (Item)(state.objectList.get("jewel-encrusted egg"));
        Item goodCanary = (Item)(state.objectList.get("golden clockwork canary"));

        if (egg.location == Location.THIEF_INVENTORY &&
            !thiefAggro && state.thiefEggTurns < GameState.THIEF_OPENS_EGG)
        {
            ++state.thiefEggTurns;

            if (state.thiefEggTurns == GameState.THIEF_OPENS_EGG)
            {
                goodCanary.location = Location.INSIDE_EGG;
                egg.open = true;
                state.thiefOpenedEgg = true;
            }
        }
        
    }


    public void trollCombat(GameState state)
    {
        String[] misses = { GameStrings.COMBAT_MISS_1, GameStrings.COMBAT_MISS_2, GameStrings.COMBAT_MISS_3,
            GameStrings.COMBAT_PARRY_1, GameStrings.COMBAT_PARRY_2, GameStrings.COMBAT_PARRY_3 };
        String[] lightBlows = { GameStrings.COMBAT_LIGHT_1, GameStrings.COMBAT_LIGHT_2, GameStrings.COMBAT_LIGHT_3,
            GameStrings.COMBAT_LIGHT_4 };
        String[] severeBlows = { GameStrings.COMBAT_SEVERE_1, GameStrings.COMBAT_SEVERE_2, GameStrings.COMBAT_SEVERE_3,
            GameStrings.COMBAT_SEVERE_4 };
        String[] staggerBlows = { GameStrings.COMBAT_STAGGER_1, GameStrings.COMBAT_STAGGER_2, GameStrings.COMBAT_STAGGER_3,
            GameStrings.COMBAT_STAGGER_4 };
        String[] disarmingBlows = { GameStrings.COMBAT_DISARM_1, GameStrings.COMBAT_DISARM_2 };
        String[] knockoutBlows = { GameStrings.COMBAT_KNOCKOUT_1, GameStrings.COMBAT_KNOCKOUT_2, GameStrings.COMBAT_KNOCKOUT_3,
            GameStrings.COMBAT_KNOCKOUT_4 };
        String[] fatalBlows = { GameStrings.COMBAT_FATAL_1, GameStrings.COMBAT_FATAL_2, GameStrings.COMBAT_FATAL_3 };

        // values for the sword
        int missCutoff = 10;
        int lightCutoff = 25;
        int severeCutoff = 40;
        int staggerCutoff = 55;
        int disarmCutoff = 70;
        int knockoutCutoff = 85;

        // Fighting the troll with the knife is a little harder.
        if (state.indirectObject.name.equals("nasty knife"))
        {
            // remove the decapitation string. These arrays have strange rules...
            String[] fatals = { GameStrings.COMBAT_FATAL_2, GameStrings.COMBAT_FATAL_3 };
            fatalBlows = fatals;

            missCutoff = 20;
            lightCutoff = 50;
            severeCutoff = 60;
            staggerCutoff = 70;
            disarmCutoff = 80;
            knockoutCutoff = 90;

        }

        Random rand = new Random();
        int dieRoll = rand.nextInt(100);

        if (0 <= dieRoll && dieRoll < missCutoff)
        {
            int phrase = rand.nextInt(misses.length);
            Game.output(misses[phrase]);
        }

        else if (missCutoff <= dieRoll && dieRoll < lightCutoff)
        {
            hitPoints -= 1;

            if (hitPoints <= 0)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.output(fatalBlows[phrase]);
            }

            else
            {
                int phrase = rand.nextInt(lightBlows.length);
                Game.output(lightBlows[phrase]);
            }
        }

        else if (lightCutoff <= dieRoll && dieRoll < severeCutoff)
        {
            hitPoints -= 5;

            if (hitPoints <= 0)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.output(fatalBlows[phrase]);
            }

            else
            {
                int phrase = rand.nextInt(severeBlows.length);
                Game.output(severeBlows[phrase]);
            }         
        }

        else if (severeCutoff <= dieRoll && dieRoll < staggerCutoff)
        {
            int phrase = rand.nextInt(staggerBlows.length);
            Game.output(staggerBlows[phrase]);
            staggered = true;
        }

        else if (staggerCutoff <= dieRoll && dieRoll < disarmCutoff)
        {
            int phrase = rand.nextInt(disarmingBlows.length);
            Game.output(disarmingBlows[phrase]);
            GameObject axe = state.objectList.get("bloody axe");
            axe.location = state.playerLocation;
            disarmed = true;
        }

        else if (disarmCutoff <= dieRoll && dieRoll < knockoutCutoff)
        {
            int phrase = rand.nextInt(knockoutBlows.length);
            Game.output(knockoutBlows[phrase]);
            unconscious = true;
            GameObject axe = state.objectList.get("bloody axe");
            axe.location = state.playerLocation;
            disarmed = true;
        }

        else if (knockoutCutoff <= dieRoll && dieRoll < 100)
        {
            int phrase = rand.nextInt(fatalBlows.length);
            Game.output(fatalBlows[phrase]);
            alive = false;
        }

    }

    
    public void trollDies(GameState state)
    {
        alive = false;

        for (Item it : inventory)
        {
            it.location = location;
        }

        location = Location.NULL_LOCATION;
        Room trollrm = state.worldMap.get(Location.TROLL_ROOM);
        Passage p1 = trollrm.exits.get(Action.WEST);
        Passage p2 = trollrm.exits.get(Action.EAST);
        p1.open();
        p2.open();

        if (state.playerLocation == Location.TROLL_ROOM && state.directObject.name.equals("troll"))
            Game.lineOutput(GameStrings.COMBAT_ENEMY_DIES);

    }

    
    public void trollGive(GameState state)
    {
        String item = state.indirectObject.name;
        switch (item)
        {
            case "axe":
            {
                Game.output(ObjectStrings.TROLL_GIVE_AXE);
                state.indirectObject.location = Location.TROLL_INVENTORY;
            } break;

            default:
            {
                
                Game.output("The troll, who is remarkably coordinated, catches the " + item + ".");
                state.indirectObject.location = Location.TROLL_INVENTORY;
            } break;
        }

    }


    public void trollTurn(GameState state)
    {
        if (!alive) return;

        GameObject axe = state.objectList.get("bloody axe");
        if (axe.location != Location.TROLL_INVENTORY)
            disarmed = true;


        if (disarmed) presenceString = ObjectStrings.TROLL_PRESENCE_DISARMED;
        else if (unconscious) presenceString = ObjectStrings.TROLL_PRESENCE_UNCONSCIOUS;
        else presenceString = ObjectStrings.TROLL_PRESENCE;

        Room trollrm = state.worldMap.get(Location.TROLL_ROOM);
        Passage p1 = trollrm.exits.get(Action.WEST);
        Passage p2 = trollrm.exits.get(Action.EAST);

        p1.close();
        p2.close();
        p1.closedFail = ObjectStrings.TROLL_FEND;
        p2.closedFail = ObjectStrings.TROLL_FEND;

        if (location == state.playerLocation)
        {
            String[] misses = { ObjectStrings.TROLL_FIGHT_MISS_1, ObjectStrings.TROLL_FIGHT_MISS_2, ObjectStrings.TROLL_FIGHT_MISS_3,
                ObjectStrings.TROLL_FIGHT_MISS_4, ObjectStrings.TROLL_FIGHT_MISS_5};
            String[] lightBlows = { ObjectStrings.TROLL_FIGHT_LIGHT_1, ObjectStrings.TROLL_FIGHT_LIGHT_2, ObjectStrings.TROLL_FIGHT_LIGHT_3,
                ObjectStrings.TROLL_FIGHT_LIGHT_4, ObjectStrings.TROLL_FIGHT_LIGHT_5 };
            String[] severeBlows = { ObjectStrings.TROLL_FIGHT_SEVERE_1, ObjectStrings.TROLL_FIGHT_SEVERE_2, ObjectStrings.TROLL_FIGHT_SEVERE_3 };
            String[] staggerBlows = { ObjectStrings.TROLL_FIGHT_STAGGER_1, ObjectStrings.TROLL_FIGHT_STAGGER_2, ObjectStrings.TROLL_FIGHT_STAGGER_3 };
            String[] disarmingBlows = { ObjectStrings.TROLL_FIGHT_DISARM_1, ObjectStrings.TROLL_FIGHT_DISARM_2, ObjectStrings.TROLL_FIGHT_DISARM_3 };
            String[] fatalBlows = { ObjectStrings.TROLL_FIGHT_FATAL_1, ObjectStrings.TROLL_FIGHT_FATAL_2, ObjectStrings.TROLL_FIGHT_FATAL_3 };

            /** COMBAT WITH THE TROLL **

            In order to attack you, the troll must be alive,
            conscious, not staggered, and armed with his axe.
            He can't fight with any other weapons.

            When the troll attacks with the axe, the possible results are:

             - Miss: 25%
             - Light blow: 20%
             - Severe blow: 15% 
             - Stagger: 15%
             - Disarm: 15%
             - Knockout: 5%
             - Death: 5%

            If he's staggered, he has a 1 in 2 chance to recover.
            If he's unconscious, he has a 1 in 3 chance to wake up.
            If he's disarmed and can't recover his weapon, he'll pathetically babble and plead for his life.
            If you leave the axe on the ground, he has a 2 in 3 chance to pick it up.

            */
            // Game.output("The troll attacks you.");

            int missCutoff = 25;
            int lightCutoff = 45;
            int severeCutoff = 60;
            int staggerCutoff = 75;
            int disarmCutoff = 90;
            int knockoutCutoff = 95;

            Random rand = new Random();

            if (disarmed && !unconscious)
            {
                if (axe.location == Location.TROLL_ROOM)
                {
                    int check = rand.nextInt(3);

                    if (check > 0)
                    {
                        Game.lineOutput(ObjectStrings.TROLL_RECOVER_AXE);
                        axe.location = Location.TROLL_INVENTORY;
                        disarmed = false;
                        presenceString = ObjectStrings.TROLL_PRESENCE;
                    }
                    return;
                }
                else
                {
                    Game.lineOutput(ObjectStrings.TROLL_DISARMED);
                    return;
                }
            }

            if (staggered)
            {
                int check = rand.nextInt(2);

                if (check == 1)
                {
                    Game.lineOutput(ObjectStrings.TROLL_RECOVERS_STAGGER);
                    staggered = false;
                }
                
                return;
            }

            if (unconscious)
            {
                int check = rand.nextInt(3);

                if (check == 0)
                {
                    Game.lineOutput(ObjectStrings.TROLL_RECOVERS_STAGGER);
                    unconscious = false;
                    if (axe.location == Location.TROLL_ROOM)
                    {
                        axe.location = Location.TROLL_INVENTORY;
                        disarmed = false;
                    }
                    presenceString = ObjectStrings.TROLL_PRESENCE;
                }

                return;
            }

            int dieRoll = rand.nextInt(100);

            // The player won't die without a chance to attack.
            if (firstCombatTurn)
            {
                firstCombatTurn = false;
                if (dieRoll >= 90) dieRoll = rand.nextInt(90);
            }

            if (0 <= dieRoll && dieRoll < missCutoff)
            {
                int phrase = rand.nextInt(misses.length);
                    Game.lineOutput(misses[phrase]);
            }

            else if (missCutoff <= dieRoll && dieRoll < lightCutoff)
            {
                int phrase = rand.nextInt(lightBlows.length);
                    Game.lineOutput(lightBlows[phrase]);
                state.playerHitPoints -= 1;
            }

            else if (lightCutoff <= dieRoll && dieRoll < severeCutoff)
            {
                int phrase = rand.nextInt(severeBlows.length);
                    Game.lineOutput(severeBlows[phrase]);
                state.playerHitPoints -= 5;
            }

            else if (severeCutoff <= dieRoll && dieRoll < staggerCutoff)
            {
                int phrase = rand.nextInt(staggerBlows.length);
                    Game.lineOutput(staggerBlows[phrase]);
                state.playerStaggered = true;
            }

            else if (staggerCutoff <= dieRoll && dieRoll < disarmCutoff)
            {
                // If the player hasn't attacked with a weapon, stagger instead.

                if (!state.indirectObject.isWeapon)
                {
                    int phrase = rand.nextInt(staggerBlows.length);
                    Game.lineOutput(staggerBlows[phrase]);
                    state.playerStaggered = true;
                    return;
                }

                int phrase = rand.nextInt(disarmingBlows.length);
                Game.lineOutput(disarmingBlows[phrase]);
                state.indirectObject.location = state.playerLocation;
                for (GameObject g : state.objectList.values())
                {
                    if (g.isWeapon)
                    {
                        Game.lineOutput("Fortunately, you still have " + g.articleName + ".");
                        break;
                    }
                }
            }

            else if (disarmCutoff <= dieRoll && dieRoll < knockoutCutoff)
            {
                Game.lineOutput(ObjectStrings.TROLL_FIGHT_KNOCKOUT);

                int phrase = rand.nextInt(2);

                if (phrase == 0)
                    Game.lineOutput(ObjectStrings.TROLL_FIGHT_HESITATE_1);
                else
                    Game.lineOutput(ObjectStrings.TROLL_FIGHT_HESITATE_2);

                Game.lineOutput(ObjectStrings.TROLL_FIGHT_FINISH);
                state.playerDies();

            }

            else if (knockoutCutoff <= dieRoll && dieRoll < 100)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.lineOutput(fatalBlows[phrase]);
                Game.lineOutput(GameStrings.COMBAT_HP_ZERO);
                state.playerDies();

            }

            if (state.playerHitPoints <= 0)
            {
                Game.lineOutput(GameStrings.COMBAT_HP_ZERO);
                state.playerDies();

            }

        }

    }


    public void vampireBatTurn(GameState state)
    {

        Item garlic = (Item)(state.objectList.get("clove of garlic"));
        if (garlic.location == Location.PLAYER_INVENTORY || garlic.location == Location.BAT_ROOM)
        {
            presenceString = ObjectStrings.BAT_GARLIC;
        }

        else if (state.playerDead)
        {
            presenceString = "A large vampire bat is cowering on the ceiling, making whimpered squeaking noises.";
        }

        else
        {
            presenceString = ObjectStrings.BAT_ATTACKS;

            if (state.playerLocation == Location.BAT_ROOM)
            {
                // Let's give the player a 1 in 10 chance of making it back to the squeaky room.
                Random rand = new Random();

                int chance = rand.nextInt(10);

                if (chance == 0)
                {
                    state.relocatePlayer(Location.SQUEAKY_ROOM);
                }

                else
                {
                    int dieRoll = rand.nextInt(GameSetup.coalMine.length);
                    state.relocatePlayer(GameSetup.coalMine[dieRoll]);
                }
            }
            
        }
    }



    @Override
    public boolean isAlive() { return alive; }
    public String toString() { return name; }


}
