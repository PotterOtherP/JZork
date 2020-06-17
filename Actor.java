import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
	
	public boolean alive;
    public boolean staggered;
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

                // Should this just be when delivering a killing blow?
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
            Game.output("The troll attacks you.");
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

    public void vampireBatTurn(GameState state)
    {

    }

	@Override
	public boolean isAlive() { return alive; }

	public String toString() { return name; }


}