import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
	
	public boolean alive;
    public boolean disarmed;
    public boolean firstCombatTurn;
    public int hitPoints;
    public boolean staggered;
    public boolean thiefAggro;
    public boolean thiefFirstTurn;
    public boolean thiefItemsHidden;
    public boolean unconscious;

	public static final int MAX_ENEMY_HIT_POINTS = 10;
    public static final int THIEF_ENCOUNTER_PERCENT = 5;
    public static final int SONGBIRD_CHIRP_PERCENT = 15;


	public Actor(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ACTOR;

		alive = true;
        disarmed = false;
        firstCombatTurn = true;
        hitPoints = MAX_ENEMY_HIT_POINTS;
        staggered = false;
        thiefAggro = false;
        thiefFirstTurn = false;
        thiefItemsHidden = false;
        unconscious = false;

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
        
    }


    public void cyclopsTurn(GameState state)
    {
        if (state.playerLocation == Location.CELLAR && state.playerPreviousLocation == Location.LIVING_ROOM)
        {
            Room rm = state.worldMap.get(Location.CELLAR);
            Passage p = rm.exits.get(Action.UP);
            p.close();
        }

    }


    public void floodTurn(GameState state)
    {
        if (!alive) return;
    }


    public void gustOfWindTurn(GameState state)
    {
        
    }


    public void riverCurrentTurn(GameState state)
    {

    }


    public void songbirdTurn(GameState state)
    {
        if (altLocations.contains(state.playerLocation))
        {
            Random rand = new Random();
            if (rand.nextInt(100) < SONGBIRD_CHIRP_PERCENT)
                Game.output(ObjectStrings.SONGBIRD);
        }
        
    }


    public void spiritsTurn(GameState state)
    {
        if (!alive) return;
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
                    if (g.location == Location.TREASURE_ROOM)
                        g.location = Location.TREASURE_ROOM_INVISIBLE;
                }

                thiefItemsHidden = true;
            }

            // Attack without pity!
            thiefAttacks(state);

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
                int option = rand.nextInt(2);
                if (option == 0) Game.output(ObjectStrings.THIEF_PRESENT_1);
                if (option == 1) Game.output(ObjectStrings.THIEF_PRESENT_2);
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