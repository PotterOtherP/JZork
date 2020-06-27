import java.util.Random;

public class Item extends GameObject{

    // Items can be picked up and moved to other locations, including the player's inventory.

    public int acquirePointValue;
    public boolean activated;
    public int capacity;
    public int lifespan;
    public boolean locked;
    public boolean open;
    public int trophyCaseValue;
    public int weight;

    public Item(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.ITEM;

        acquirePointValue = 0;
        activated = false;
        capacity = 0;
        lifespan = 0;
        locked = false;
        open = false;
        trophyCaseValue = 0;
        weight = 0;
    }


    @Override
    public void close(GameState state)
    {
        if (!isContainer())
        {
            Game.output(closeString);
            return;
        } 

        if (open)
        {
            open = false; 
            Game.output("Closed.");
        }
        else
        {
            Game.output("It is already closed.");
        }

    }

    @Override
    public void drink(GameState state)
    {
        if (name.equals("quantity of water"))
        {
            Item bottle = (Item)(state.objectList.get("glass bottle"));

            if (!bottle.open)
            {
                Game.output("The bottle is closed.");
                return;
            }

            Game.output("Thank you very much. I was rather thirsty (from all this talking, probably.)");
            location = Location.NULL_LOCATION;
            return;
        }

        else
            super.drink(state);

    }

    @Override
    public void drop(GameState state)
    {
        Location loc = state.playerLocation;

        if (loc == Location.UP_TREE)
        {
            state.playerCarryWeight -= weight;

            if (name.equals("jewel-encrusted egg"))
            {
                Game.output("The egg falls to the ground and springs open, seriously damaged.");
                breakEgg(state);
                Item badEgg = (Item)(state.objectList.get("broken jewel-encrusted egg"));
                badEgg.location = Location.FOREST_PATH;
            }

            else if (name.equals("bird's nest"))
            {
                Item goodEgg = (Item)(state.objectList.get("jewel-encrusted egg"));
                if (goodEgg.location == Location.INSIDE_BIRDS_NEST)
                {
                    Game.output("The nest falls to the ground, and the egg spills out of it, seriously damaged.");
                    breakEgg(state);
                    Item badEgg = (Item)(state.objectList.get("broken jewel-encrusted egg"));
                    badEgg.location = Location.FOREST_PATH;
                }

                else
                    Game.output("The bird's nest falls to the ground.");

                location = Location.FOREST_PATH;
            }

            else
            {
                Game.output("The " + name + " falls to the ground.");
                location = Location.FOREST_PATH;
            }
        }


        else
        {
            state.playerCarryWeight -= weight;
            location = state.playerLocation;
            Game.output("Dropped.");
        }
        
    }

    @Override
    public void eat(GameState state)
    {
        switch (name)
        {
            case "clove of garlic":
            {
                this.location = Location.NULL_LOCATION;
                Game.output(ObjectStrings.GARLIC_EAT);

                if (state.playerLocation == Location.BAT_ROOM)
                {
                    Game.output("The bat, no longer deterred, swoops down at you!"
                    + "\n\nFweep!\nFweep!\nFweep!\n\n\n"
                    + "The bat grabs you by the scruff of your neck and lifts you away....\n\n");

                    Random rand = new Random();
                    int dieRoll = rand.nextInt(GameSetup.coalMine.length);
                    state.relocatePlayer(GameSetup.coalMine[dieRoll]);
                }
                
            } break;

            case "lunch":
            {
                this.location = Location.NULL_LOCATION;
                Game.output(ObjectStrings.LUNCH_EAT);
            } break;

            default:
            {
                super.eat(state);
            } break;
        }

    }


    @Override
    public void examine(GameState state)
    {
        if (!isContainer())
        {
            Game.output(examineString);
            return;
        }

        if (open)
        {
            if (inventory.size() == 0)
                Game.output("The " + name + " is empty.");
            else
            {
                Game.output("The " + name + " contains:");

                for (int i = 0; i < inventory.size(); ++i)
                {
                    Item it = inventory.get(i);
                    Game.output("  " + it.capArticleName);
                }
            }
        }

        else
        {
            Game.output("The " + name + " is closed.");
        }

    }


    @Override
    public void extinguish(GameState state)
    {
        switch (name)
        {
            case "lantern":
            {
                if (activated)
                {
                    activated = false;
                    Game.output("The brass lantern is now off.");

                    Room rm = state.worldMap.get(state.playerLocation);
                    if (rm.isDark())
                    {
                        Game.output("It is now pitch black.");
                    }
                }

                else
                {
                    Game.output("It is already off.");
                }
            } break;

            default:
            {
                Game.output(extinguishString);
            } break;
        }

    }


    @Override
    public void light(GameState state)
    {
        switch (name)
        {
            case "brass lantern":
            {
                if (!activated && lifespan > 0)
                {
                    activated = true;
                    state.lightActivated = true;
                    Game.output("The brass lantern is now on.");
                    Room rm = state.worldMap.get(state.playerLocation);
                    if (rm.isDark())
                    {
                        Game.outputLine();
                        rm.lookAround(state);
                    }
                    
                    examineString = "The lamp is on.";
                }

                else if (!activated && lifespan <= 0)
                {
                    Game.output("A burned-out lamp won't light.");
                }

                else
                {
                    Game.output("It is already on.");
                }

            } break;

            case "matchbook":
            {
                if (!activated && state.matchCount > 0)
                {
                    Game.output("One of the matches begins to burn.");
                    activated = true;
                    --state.matchCount;
                    lifespan = GameState.MATCH_LIFESPAN;
                }

                else if (state.matchCount <= 0)
                {
                    Game.output("There are no matches left in the matchbook.");
                }

                else if (activated)
                {
                    Game.output("You already have a lit match.");
                }

            } break;

            default:
            {
                Game.output(lightString);
            } break;
        }

    }


    @Override
    public void move(GameState state)
    {
        switch (name)
        {
            case "pile of leaves":
            {
                Game.output("Done.");
                if (!state.leafPileMoved)
                {
                    revealGrating(state);
                }
                else
                {
                    Game.output("Moving the pile of leaves reveals nothing.");
                }
            } break;

            default:
            {
                super.move(state);
            } break;
        }

    }


    @Override
    public void open(GameState state)
    {
        if (!isContainer())
        {
            Game.output(openString);
            return;
        }

        if (open)
        {
            Game.output("It is already open.");
        }

        else
        {
            open = true;
            if (inventory.isEmpty())
                Game.output("Opened.");
            else
            {
                String str = "Opening the " + name + " reveals ";

                for (int i = 0; i < inventory.size(); ++i)
                {
                    Item it = inventory.get(i);

                    if (inventory.size() > 1 && i == inventory.size() - 1) str  += " and ";
                    str += it.articleName;
                    if (inventory.size() > 2 && i < inventory.size() - 1)
                        str += ",";
                }
                
                str += ".";

                Game.output(str);
            }
        }

    }


    @Override
    public void put(GameState state)
    {

        if (!isContainer())
        {
            Game.output(putString);
            return;
        }

        if (open)
        {
            Item it = (Item)(state.indirectObject);
            inventory.add(it);
            it.location = inventoryID;
            Game.output("Done.");
        }
        else
        {
            Game.output("The " + name + " isn't open.");
        }

    }


    @Override
    public void remove(GameState state)
    {
        Item it = (Item)(state.indirectObject);
        if (!isContainer())
        {
            Game.output("You can't remove that from the " + name);
            return;
        }

        if (open)
        {
            if (inventory.contains(it))
            {
                inventory.remove(it);
                it.location = Location.PLAYER_INVENTORY;
                Game.output("Taken.");
            }

            else
            {
                Game.output("There's no " + it.name + " in the " + name);
            }
        }

        else
        {
            Game.output("The " + name + " is closed.");
        }
        
    }


    @Override
    public void take(GameState state)
    {
        if (location == Location.PLAYER_INVENTORY)
        {
            Game.output("You're already carrying the " + name + "!");
            return;
        }

        if (name.equals("pile of leaves") && !state.leafPileMoved)
        {
            revealGrating(state);       
        }

        if (name.equals("quantity of water"))
        {
            Game.output("The water slips through your fingers.");
            location = Location.NULL_LOCATION;
            return;
        }

        if (name.equals("rope") && state.ropeRailTied)
        {
            this.untie(state);
            return;
        }

        if ((state.playerCarryWeight + weight) >= GameState.CARRY_WEIGHT_LIMIT)
        {
            Game.output(GameStrings.OVERBURDENED);
            return;
        }

        state.playerCarryWeight += weight;
        location = Location.PLAYER_INVENTORY;
        Game.output("Taken.");

        movedFromStart = true;

    }


    @Override
    public void untie(GameState state)
    {
        switch (name)
        {
            case "rope":
            {
                if (state.ropeRailTied)
                {
                    if (state.playerLocation == Location.TORCH_ROOM)
                        Game.output("You cannot reach the rope.");
                    else if (state.playerLocation == Location.DOME_ROOM)
                    {
                        state.ropeRailTied = false;
                        Game.output("The rope is now untied.");
                        GameObject rope = state.objectList.get("rope");
                        rope.location = Location.PLAYER_INVENTORY;

                        Room rm1 = state.worldMap.get(Location.DOME_ROOM);
                        Room rm2 = state.worldMap.get(Location.TORCH_ROOM);
                        rm1.description = MapStrings.DESC_DOME_ROOM;
                        rm2.description = MapStrings.DESC_TORCH_ROOM;
                        rm2.removeFailMessage(Action.UP);
                        Passage psg = rm1.exits.get(Action.DOWN);
                        psg.close();
                    }
                }

                else
                {
                    Game.output("It is not tied to anything.");
                }
            } break;

            default:
            {
                super.untie(state);
            }
        }

    }


    @Override
    public void wave(GameState state)
    {
        if (name.equals("sceptre"))
        {
            switch (state.playerLocation)
            {
                case END_OF_RAINBOW:
                case ARAGAIN_FALLS:
                case ON_THE_RAINBOW:
                {
                    Room onRainbow = state.worldMap.get(Location.ON_THE_RAINBOW);
                    Passage p1 = onRainbow.exits.get(Action.EAST);
                    Passage p2 = onRainbow.exits.get(Action.WEST);
                    Item pot = (Item)state.objectList.get("pot of gold");

                    if (!state.rainbowSolid)
                    {
                        state.rainbowSolid = true;
                        Game.output(ObjectStrings.SCEPTRE_RAINBOW);
                        p1.open();
                        p2.open();

                        if (state.playerLocation == Location.END_OF_RAINBOW &&
                            !state.potOfGoldAppeared)
                        {
                            state.potOfGoldAppeared = true;
                            Game.output("A shimmering pot of gold appears at the end of the rainbow.");
                            pot.location = Location.END_OF_RAINBOW;
                        }
                    }

                    else
                    {
                        state.rainbowSolid = false;
                        p1.close();
                        p2.close();

                        if (state.playerLocation == Location.ON_THE_RAINBOW)
                        {
                            Game.output(ObjectStrings.SCEPTRE_RAINBOW_2);
                            state.playerDies();
                        }

                        else
                            Game.output(ObjectStrings.SCEPTRE_RAINBOW_1);

                    }
                } break;

                default:
                {
                    Game.output(waveString);
                } break;
            }

        }

        else
            super.wave(state);

    }


    private void breakEgg(GameState state)
    {
        Item goodEgg = (Item)(state.objectList.get("jewel-encrusted egg"));
        Item badEgg = (Item)(state.objectList.get("broken jewel-encrusted egg"));
        Item badCanary = (Item)(state.objectList.get("broken clockwork canary"));

        goodEgg.location = Location.NULL_LOCATION;
        badCanary.location = Location.INSIDE_BROKEN_EGG;
        badEgg.location = state.playerLocation;
        badEgg.open = true;

        Game.output(ObjectStrings.INIT_BROKEN_CANARY);

    }


    public String getItemDescription()
    {
        if (movedFromStart || initialPresenceString.isEmpty())
        {
            return presenceString;
        }

        else
        {
            return initialPresenceString;
        }

    }


    private void revealGrating(GameState state)
    {
        state.leafPileMoved = true;
        Feature grate = (Feature)state.objectList.get("grating");
        grate.altLocations.add(Location.CLEARING_NORTH);
        Game.output("In disturbing the pile of leaves, a grating is revealed.");

        Room rm = state.worldMap.get(Location.CLEARING_NORTH);
        rm.description = MapStrings.DESC_CLEARING_NORTH_GRATING;
        Passage psg = new Passage(Location.GRATING_ROOM, Location.CLEARING_NORTH);
        psg.close();
        psg.closedFail = "The grating is closed!";
        rm.addExit(Action.DOWN, psg);

    }


    @Override
    public boolean isAlive() { return lifespan > 0; }
    @Override
    public boolean isContainer() { return inventoryID != Location.NULL_INVENTORY; }
    @Override
    public boolean isOpen() { return open; }
    @Override
    public void tick() { --lifespan; }
    public String toString() { return name; }
    
}
