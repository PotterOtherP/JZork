import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
	
	public boolean alive;
    public boolean staggered;
    public boolean disarmed;
    public boolean unconscious;
    public boolean firstCombatTurn;
    public int hitPoints;
    public int strength;



    public static final int SONGBIRD_CHIRP_PERCENT = 15;
	public static final int MAX_ENEMY_HIT_POINTS = 10;


	public Actor(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ACTOR;

		alive = true;
        staggered = false;
        unconscious = false;
        disarmed = false;
        firstCombatTurn = true;
        hitPoints = MAX_ENEMY_HIT_POINTS;
        strength = 0;

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

            case "vampire bat":
            {

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

    public void cyclopsCombat(GameState state)
    {
        
    }

    @Override
    public void give(GameState state)
    {
        switch (name)
        {
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

    public void thiefCombat(GameState state)
    {
        Random rand = new Random();
        int dieRoll = rand.nextInt(100);

        if (0 <= dieRoll && dieRoll < 10)
        {
            Game.output(GameStrings.COMBAT_MISS_1);
        }

        else if (10 <= dieRoll && dieRoll < 25)
        {
            Game.output(GameStrings.COMBAT_LIGHT_1);
            hitPoints -= 1;
        }

        else if (25 <= dieRoll && dieRoll < 40)
        {
            Game.output(GameStrings.COMBAT_SEVERE_1);
            hitPoints -= 5;
        }

        else if (40 <= dieRoll && dieRoll < 55)
        {
            Game.output(GameStrings.COMBAT_STAGGER_1);
            staggered = true;
        }

        else if (55 <= dieRoll && dieRoll < 70)
        {
            Game.output(GameStrings.COMBAT_DISARM_1);
        }

        else if (70 <= dieRoll && dieRoll < 85)
        {
            Game.output(GameStrings.COMBAT_KNOCKOUT_1);
            unconscious = true;
        }

        else if (85 <= dieRoll && dieRoll < 100)
        {
            Game.output(GameStrings.COMBAT_FATAL_1);
            alive = false;
        }
    }

    public void thiefDies(GameState state)
    {

    }
    
    public void trollCombat(GameState state)
    {
        Random rand = new Random();
        int dieRoll = rand.nextInt(100);

        if (0 <= dieRoll && dieRoll < 10)
        {
            Game.output(GameStrings.COMBAT_MISS_1);
        }

        else if (10 <= dieRoll && dieRoll < 25)
        {
            Game.output(GameStrings.COMBAT_LIGHT_1);
            hitPoints -= 1;
        }

        else if (25 <= dieRoll && dieRoll < 40)
        {
            Game.output(GameStrings.COMBAT_SEVERE_1);
            hitPoints -= 5;
        }

        else if (40 <= dieRoll && dieRoll < 55)
        {
            Game.output(GameStrings.COMBAT_STAGGER_1);
            staggered = true;
        }

        else if (55 <= dieRoll && dieRoll < 70)
        {
            Game.output(GameStrings.COMBAT_DISARM_1);
            GameObject axe = state.objectList.get("bloody axe");
            axe.location = state.playerLocation;
            disarmed = true;
        }

        else if (70 <= dieRoll && dieRoll < 85)
        {
            Game.output(GameStrings.COMBAT_KNOCKOUT_1);
            unconscious = true;
        }

        else if (85 <= dieRoll && dieRoll < 100)
        {
            Game.output(GameStrings.COMBAT_FATAL_1);
            alive = false;
        }
    }

    public void trollDies(GameState state)
    {
        alive = false;

        for (GameObject g : state.objectList.values())
        {
            if (g.location == Location.TROLL_INVENTORY)
                g.location = location;
        }

        location = Location.NULL_LOCATION;
        Room trollrm = state.worldMap.get(Location.TROLL_ROOM);
        Passage p1 = trollrm.exits.get(Action.WEST);
        Passage p2 = trollrm.exits.get(Action.EAST);
        p1.open();
        p2.open();

        Game.output(GameStrings.COMBAT_ENEMY_DIES);
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
				Game.output(GameStrings.SONGBIRD);
		}

		
	}

	public void spiritsTurn(GameState state)
    {
        if (!alive) return;
    }

    public void thiefTurn(GameState state)
    {
        if (!alive) return;
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
        p1.closedFail = GameStrings.TROLL_FEND;
        p2.closedFail = GameStrings.TROLL_FEND;

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
            Random rand = new Random();

            if (disarmed)
            {
                if (axe.location == Location.TROLL_ROOM)
                {
                    int check = rand.nextInt(3);

                    if (check > 0)
                    {
                        Game.output(ObjectStrings.TROLL_RECOVER_AXE);
                        axe.location = Location.TROLL_INVENTORY;
                        disarmed = false;
                    }
                    return;
                }
                else
                {
                    Game.output(ObjectStrings.TROLL_DISARMED);
                    return;
                }
            }

            if (staggered)
            {
                int check = rand.nextInt(2);

                if (check == 1)
                {
                    Game.output(ObjectStrings.TROLL_RECOVERS_STAGGER);
                    staggered = false;
                }
                
                return;
            }

            if (unconscious)
            {
                int check = rand.nextInt(3);

                if (check > 0)
                {
                    Game.output(ObjectStrings.TROLL_RECOVERS_STAGGER);
                    unconscious = false;
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

            if (0 <= dieRoll && dieRoll < 25)
            {
                int phrase = rand.nextInt(misses.length);
                    Game.output(misses[phrase]);
            }

            else if (25 <= dieRoll && dieRoll < 45)
            {
                int phrase = rand.nextInt(lightBlows.length);
                    Game.output(lightBlows[phrase]);
                state.playerHitPoints -= 1;
            }

            else if (45 <= dieRoll && dieRoll < 60)
            {
                int phrase = rand.nextInt(severeBlows.length);
                    Game.output(severeBlows[phrase]);
                state.playerHitPoints -= 5;
            }

            else if (60 <= dieRoll && dieRoll < 75)
            {
                int phrase = rand.nextInt(staggerBlows.length);
                    Game.output(staggerBlows[phrase]);
                state.playerStaggered = true;
            }

            else if (75 <= dieRoll && dieRoll < 90)
            {
                // If the player hasn't attacked with a weapon, stagger instead.

                if (!state.indirectObject.isWeapon)
                {
                    int phrase = rand.nextInt(staggerBlows.length);
                    Game.output(staggerBlows[phrase]);
                    state.playerStaggered = true;
                    return;
                }

                int phrase = rand.nextInt(disarmingBlows.length);
                Game.output(disarmingBlows[phrase]);
                state.indirectObject.location = state.playerLocation;
                for (GameObject g : state.objectList.values())
                {
                    if (g.isWeapon)
                    {
                        Game.output("Fortunately, you still have " + g.articleName + ".");
                        break;
                    }
                }
            }

            else if (90 <= dieRoll && dieRoll < 95)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_KNOCKOUT);

                int phrase = rand.nextInt(2);

                if (phrase == 0)
                    Game.output(ObjectStrings.TROLL_FIGHT_HESITATE_1);
                else
                    Game.output(ObjectStrings.TROLL_FIGHT_HESITATE_2);

                Game.output(ObjectStrings.TROLL_FIGHT_FINISH);
                state.playerDies();

            }

            else if (95 <= dieRoll && dieRoll < 100)
            {
                int phrase = rand.nextInt(fatalBlows.length);
                Game.output(fatalBlows[phrase]);
                Game.output(GameStrings.COMBAT_HP_ZERO);
                state.playerDies();

            }

            if (state.playerHitPoints <= 0)
            {
                Game.output(GameStrings.COMBAT_HP_ZERO);
                state.playerDies();

            }

        }
    }


    public void vampireBatTurn(GameState state)
    {

    }

	@Override
	public boolean isAlive() { return alive; }

	public String toString() { return name; }


}