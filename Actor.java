import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
	
	public boolean alive;
    public boolean staggered;
    public boolean unconscious;
    public int hitPoints;
    public int strength;



    public static final int SONGBIRD_CHIRP_PERCENT = 15;
	public static final int ENEMY_HIT_POINTS = 10;


	public Actor(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ACTOR;

		alive = true;
        staggered = false;
        unconscious = false;
        hitPoints = ENEMY_HIT_POINTS;
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

                Random rand = new Random();
                int dieRoll = rand.nextInt(100);


            } break;

            case "vampire bat":
            {

            } break;



            default:
            {
                super.attack(state);
            } break;
        }
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
    

    public void trollDies(GameState state)
    {
        alive = false;
        Game.output("The troll dies.");

        Room trollrm = state.worldMap.get(Location.TROLL_ROOM);
        Passage p1 = trollrm.exits.get(Action.WEST);
        Passage p2 = trollrm.exits.get(Action.EAST);

        p1.open();
        p2.open();
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

        Room trollrm = state.worldMap.get(Location.TROLL_ROOM);
        Passage p1 = trollrm.exits.get(Action.WEST);
        Passage p2 = trollrm.exits.get(Action.EAST);

        p1.close();
        p2.close();
        p1.closedFail = GameStrings.TROLL_FEND;
        p2.closedFail = GameStrings.TROLL_FEND;

        if (location == state.playerLocation)
        {
            /** COMBAT WITH THE TROLL **

            In order to attack you, the troll must be alive,
            conscious, not staggered, and armed with his axe.
            He can't fight with any other weapons.

            When the troll attacks with the axe, the possible results are:

             - Miss
             - Glancing or light blow
             - Severe blow
             - Staggering blow
             - Knockout blow
             - Fatal blow

            If he's unconscious, he'll wake up after X turns.
            If he's staggered, he'll take one turn to recover.
            If he's disarmed, he will pathetically babbly and plead for his life.
            If you give him the axe or leave it on the ground, he'll continue fighting.

            */
            // Game.output("The troll attacks you.");

            Random rand = new Random();
            int dieRoll = rand.nextInt(100);

            if (0 <= dieRoll && dieRoll < 20)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_MISS_1);
            }

            else if (20 <= dieRoll && dieRoll < 40)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_LIGHT_1);
                state.playerHitPoints -= 1;
            }

            else if (40 <= dieRoll && dieRoll < 60)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_SEVERE_1);
                state.playerHitPoints -= 5;
            }

            else if (60 <= dieRoll && dieRoll < 80)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_STAGGER_1);
                state.playerStaggered = true;
            }

            else if (80 <= dieRoll && dieRoll < 90)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_KNOCKOUT);

            }

            else if (90 <= dieRoll && dieRoll < 100)
            {
                Game.output(ObjectStrings.TROLL_FIGHT_FATAL_1);
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