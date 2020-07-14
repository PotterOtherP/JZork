import java.util.ArrayList;

class Feature extends GameObject {

    private GameState state;


    public Feature(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.FEATURE;
        presenceString = "";
        state = super.state;
    
    }


    @Override
    public void breakObject()
    {
        switch (name)
        {
            case "broken mirror":
            {
                Game.output(breakString);
            } break;

            case "mirror":
            {
                if (state.indirectObject.isWeapon)
                {
                    Game.output(breakString);
                    location = Location.NULL_LOCATION;
                    altLocations.clear();

                    Feature brokeMirror = (Feature)(state.objectList.get("broken mirror"));
                    brokeMirror.location = Location.MIRROR_ROOM_SOUTH;
                    brokeMirror.altLocations.add(Location.MIRROR_ROOM_NORTH);
                    state.mirrorBroken = true;
                }
            } break;

            default:
            {
                super.breakObject();
            }
        }

    }


    @Override
    public void climb()
    {
        switch (name)
        {
            case "forest":
            {
                if (state.playerLocation == Location.FOREST_PATH)
                {
                    state.relocatePlayer(Location.UP_TREE);
                }

                else if (state.playerLocation == Location.UP_TREE)
                {
                    Game.output("You cannot climb any higher.");
                }

                else
                {
                    Game.output("There is no tree here suitable for climbing.");
                }
            } break;

            default:
            {
                super.climb();
            } break;
        }

    }


    @Override
    public void close()
    {
        switch (name)
        {

            case "grating":
            {
                if (!state.gratingOpened)
                {
                    Game.output(GameStrings.getHardSarcasm());
                }

                else
                {
                    state.gratingOpened = false;
                    Room r = state.worldMap.get(Location.GRATING_ROOM);
                    Passage p = r.exits.get(Action.UP);
                    p.close();
                    Game.output("Done.");
                    examineString = "The grating is closed.";
                }
            } break;

            case "kitchen window":
            {
                Room r = state.worldMap.get(Location.BEHIND_HOUSE);
                Passage p = r.exits.get(Action.WEST);
                if (state.houseWindowOpened)
                {
                    Game.output(GameStrings.WINDOW_CLOSES);
                    examineString = ObjectStrings.WINDOW_EXAMINE_CLOSED;
                    state.houseWindowOpened = false;
                    p.close();
                }
                else
                    Game.output("The window is already closed.");
            } break;

            case "trap door":
            {
                Room r = state.worldMap.get(Location.LIVING_ROOM);
                Passage p = r.exits.get(Action.DOWN);
                if (state.trapDoorOpen)
                {
                    state.trapDoorOpen = false;
                    Game.output("Done.");
                    p.close();
                }
                else
                {
                    Game.output(GameStrings.getHardSarcasm());
                }
            } break;

            default:
            {
                Game.output(closeString);
            } break;
        }

    }


    @Override
    public void dig()
    {
        switch (name)
        {
            case "sand":
            {
                if (state.indirectObject.name.equals("shovel"))
                {
                    ++state.sandStage;

                    if (state.sandStage == 1)
                    {
                        Game.output("You seem to be digging a hole here.");
                    }

                    else if (state.sandStage == 2)
                    {
                        Game.output("The hole is getting deeper, but that's about it.");
                    }

                    else if (state.sandStage == 3)
                    {
                        Game.output("You are surrounded by a wall of sand on all sides.");
                    }

                    else if (state.sandStage == 4)
                    {
                        if (!state.scarabFound)
                        {
                            Item scarab = (Item)state.objectList.get("beautiful jeweled scarab");
                            scarab.location = Location.SANDY_CAVE;
                            Game.output("You can see a scarab here in the sand.");
                            state.scarabFound = true;
                        }

                        else
                            Game.output("There's no reason to be digging here!");
                    }

                    else if (state.sandStage == 5)
                    {
                        Game.output("The hole collapses, smothering you.");
                        state.playerDies();
                        state.sandStage = 0;
                        Item scarab = (Item)state.objectList.get("beautiful jeweled scarab");
                        if (scarab.location == Location.SANDY_CAVE)
                        {
                            scarab.location = Location.NULL_LOCATION;
                            state.scarabFound = false;
                        }

                    }
                }

                else
                    Game.output("Digging with " + state.indirectObject.articleName + " is silly.");
            } break;

            default:
            {
                super.dig();
            } break;
        }
    }


    @Override
    public void drink()
    {
        switch (name)
        {
            case "quantity of water":
            {
                Item bottle = (Item)(state.objectList.get("glass bottle"));
                if (bottle.location == Location.PLAYER_INVENTORY && bottle.isOpen())
                {
                    Game.output(ObjectStrings.WATER_DRINK);
                    state.bottleFilled = false;
                }

                else if (bottle.location == Location.PLAYER_INVENTORY && !bottle.isOpen())
                {
                    Game.output("The bottle is closed.");
                }

                else if (bottle.location != Location.PLAYER_INVENTORY)
                {
                    Game.output("It's in the bottle. Perhaps you should take that first.");
                }
            } break;

            default:
            {
                super.drink();
            } break;
        }
    }


    @Override
    public void kick()
    {
        switch (name)
        {
            case "gate":
            {
                Game.output(ObjectStrings.DEAD_GATE);
            } break;

            default:
            {
                super.kick();
            } break;
        }
    }


    @Override
    public void lock()
    {
        switch (name)
        {
            case "grating":
            {
                if (state.indirectObject.name.equals("skeleton key"))
                {
                    if (state.playerLocation == Location.GRATING_ROOM)
                    {
                        Game.output("The grate is locked.");
                        state.gratingUnlocked = false;
                    }

                    else if (state.playerLocation == Location.CLEARING_NORTH)
                    {
                        Game.output("You can't reach the lock from here.");
                    }
                }

                else
                {
                    Game.output("Can you lock a grating with " + state.indirectObject.articleName + "?");
                }
            } break;

            default:
            {
                super.lock();
            } break;
        }
    }


    @Override
    public void lookIn()
    {
        switch (name)
        {
            case "kitchen window":
            {
                if (state.playerLocation == Location.BEHIND_HOUSE)
                    Game.output(ObjectStrings.WINDOW_LOOK_IN);
                else
                    Game.output("You are inside.");
            } break;

            default: { super.lookIn(); } break;
        }

    }


    @Override
    public void lookOut()
    {
        switch (name)
        {
            case "kitchen window":
            {
                if (state.playerLocation == Location.KITCHEN)
                    Game.output(ObjectStrings.WINDOW_LOOK_OUT);
                else
                    Game.output("You are outside.");
            } break;

            default: { super.lookOut(); } break;
        }

    }


    @Override
    public void lower()
    {
        switch (name)
        {
            case "gate":
            {
                Game.output(ObjectStrings.DEAD_GATE);
            } break;

            default:
            {
                super.lower();
            } break;
        }
    }


    @Override
    public void move()
    {
        switch (name)
        {
            case "oriental rug":
            {
                if (!state.carpetMoved)
                {
                    state.carpetMoved = true;
                    boardString = ObjectStrings.CARPET_SIT_2;
                    lookUnderString = "There is nothing but dust there.";
                    GameObject trap = state.objectList.get("trap door");
                    trap.location = Location.LIVING_ROOM;
                    trap.altLocations.add(Location.CELLAR);
                    Game.output(GameStrings.MOVE_RUG);
                    Room rm = state.worldMap.get(Location.LIVING_ROOM);
                    Passage p = rm.exits.get(Action.DOWN);
                    p.closedFail = "The trap door is closed.";
                }

                else
                {
                    Game.output(GameStrings.RUG_ALREADY_MOVED);
                }
            } break;

            default:
            {
                super.move();
            } break;
        }

    }


    @Override
    public void open()
    {
        switch (name)
        {
            case "grating":
            {
                if (state.gratingOpened)
                {
                    Game.output(GameStrings.getHardSarcasm());
                }

                else
                {
                    if (state.gratingUnlocked)
                    {
                        Room r = state.worldMap.get(Location.GRATING_ROOM);
                        Passage p = r.exits.get(Action.UP);
                        p.open();
                        state.gratingOpened = true;
                        if (state.playerLocation == Location.GRATING_ROOM)
                        {
                            if (!state.leafPileMoved)
                            {
                                state.leafPileMoved = true;
                                altLocations.add(Location.CLEARING_NORTH);
                                Room rm = state.worldMap.get(Location.CLEARING_NORTH);
                                Room rm2 = state.worldMap.get(Location.GRATING_ROOM);
                                Passage psg = rm2.exits.get(Action.UP);
                                rm.addExit(Action.DOWN, psg);
                                rm2.darkness = false;

                                Game.output("A pile of leaves falls onto your head and to the ground.");
                                Item leaves = (Item)state.objectList.get("pile of leaves");
                                leaves.location = Location.GRATING_ROOM;
                            }
                            Game.output("The grating opens to reveal trees above you.");
                        }
                        else if (state.playerLocation == Location.CLEARING_NORTH)
                            Game.output("The grating opens to reveal darkness below.");
                        examineString = "The grating is open, but I can't tell what's beyond it.";
                    }

                    else
                    {
                        Game.output("The grating is locked.");
                    }
                }

            } break;

            case "kitchen window":
            {
                Room r = state.worldMap.get(Location.BEHIND_HOUSE);
                Passage p = r.exits.get(Action.WEST);
                if (!state.houseWindowOpened)
                {
                    Game.output(GameStrings.WINDOW_OPENS);
                    examineString = ObjectStrings.WINDOW_EXAMINE_OPEN;
                    state.houseWindowOpened = true;
                    p.open();
                }
                else
                    Game.output(GameStrings.getHardSarcasm());
            } break;

            case "trap door":
            {
                if (state.playerLocation == Location.CELLAR)
                {
                    Game.output("The door is locked from above.");
                }

                else if (state.playerLocation == Location.LIVING_ROOM)
                {
                    Room r = state.worldMap.get(Location.LIVING_ROOM);
                    Passage p = r.exits.get(Action.DOWN);
                    if (!state.trapDoorOpen)
                    {
                        state.trapDoorOpen = true;
                        Game.output(GameStrings.TRAP_DOOR_OPENS);
                        p.open();
                    }
                    else
                    {
                        Game.output(GameStrings.getHardSarcasm());
                    }
                }

            } break;


            default:
            {
                super.open();
            } break;
        }

    }


    @Override
    public void pour()
    {
        switch (name)
        {
            case "quantity of water":
            {
                Item bottle = (Item)state.objectList.get("glass bottle");

                if (bottle.location != Location.PLAYER_INVENTORY)
                    Game.output("It's in the bottle. Perhaps you should take that first.");

                else if (!bottle.isOpen())
                    Game.output("The bottle is closed.");

                else
                {
                    switch (state.indirectObject.name)
                    {
                        case "red hot brass bell":
                        {
                            Game.output("The water cools the bell and is evaporated.");
                            state.indirectObject.location = Location.NULL_LOCATION;
                            Item bell = (Item)(state.objectList.get("brass bell"));
                            bell.location = state.playerLocation;
                            state.bottleFilled = false;

                        } break;

                        default:
                        {
                            Game.output("The water spills onto the " + state.indirectObject.name + " and dissipates.");
                            state.bottleFilled = false;
                        } break;
                    } 
                }
                

            } break;

            default:
            {
                super.pour();
            } break;
        }
    }


    @Override
    public void push()
    {
        switch (name)
        {
            case "blue button":
            {
                if (!state.blueButtonPushed)
                {
                    Game.output(ObjectStrings.BLUE_BUTTON);
                    state.blueButtonPushed = true;
                }

                else
                    Game.output(ObjectStrings.BLUE_BUTTON_JAMMED);

            } break;

            case "brown button":
            {
                Game.output("Click.");
                state.yellowButtonPushed = false;
                Room dam = state.worldMap.get(Location.DAM);
                dam.firstVisit = true;

            } break;

            case "red button":
            {
                Room rm = state.worldMap.get(Location.MAINTENANCE_ROOM);

                if (!state.redButtonPushed)
                {
                    Game.output("The lights within the room come on.");
                    rm.setLight();
                    state.darknessCheck();
                    state.redButtonPushed = true;
                }

                else
                {
                    Game.output("The lights within the room shut off.");
                    rm.setDark();
                    state.darknessCheck();
                    state.redButtonPushed = false;

                }

            } break;

            case "yellow button":
            {
                Game.output("Click.");
                state.yellowButtonPushed = true;
                Room dam = state.worldMap.get(Location.DAM);
                dam.firstVisit = true;

            } break;


            default:
            {
                super.push();
            } break;
        }

    }


    @Override
    public void put()
    {
        switch(name)
        {
            case "grating":
            {
                if (state.playerLocation == Location.CLEARING_NORTH)
                {
                    Item it = (Item)state.indirectObject;
                    if (it.weight < 10)
                    {
                        Game.output("The " + state.indirectObject.name + " goes through the grating into the darkness below.");
                        state.indirectObject.location = Location.GRATING_ROOM;
                    }
                    else
                        Game.output("It won't fit through the grating.");
                }

                else if (state.playerLocation == Location.GRATING_ROOM)
                {
                    Game.output("You can't get anything through the grating from here.");
                }

            } break;

            default:
            {
                super.put();
            } break;
        }
    }


    @Override
    public void raise()
    {
        switch (name)
        {
            case "gate":
            {
                Game.output(ObjectStrings.DEAD_GATE);
            } break;

            default:
            {
                super.raise();
            } break;
        }
    }


    @Override
    public void take()
    {
        switch (name)
        {
            case "quantity of water":
            {
                Item bottle = (Item)state.objectList.get("glass bottle");

                if (bottle.location != Location.PLAYER_INVENTORY)
                    Game.output("It's in the bottle. Perhaps you should take that first.");

                else if (!bottle.isOpen())
                    Game.output("The bottle is closed.");

                else
                    Game.output("The water slips through your fingers.");
                
            } break;

            default:
            {
                super.take();
            } break;
        }
    }


    @Override
    public void tie()
    {
        switch (name)
        {
            case "wooden railing":
            {
                if (state.indirectObject.name.equals("rope"))
                {
                    if (!state.ropeRailTied)
                    {
                        state.ropeRailTied = true;
                        Game.output("The rope drops over the side and comes within ten feet of the floor.");
                        GameObject rope = state.objectList.get("rope");
                        rope.location = Location.ON_RAILING;

                        Room rm1 = state.worldMap.get(Location.DOME_ROOM);
                        Room rm2 = state.worldMap.get(Location.TORCH_ROOM);
                        rm1.description = MapStrings.DESC_DOME_ROOM_ROPE;
                        rm2.description = MapStrings.DESC_TORCH_ROOM_ROPE;
                        rm2.addFailMessage(Action.UP, "You cannot reach the rope.");
                        Passage psg = rm1.exits.get(Action.DOWN);
                        psg.open();
                    }

                    else
                    {
                        Game.output("The rope is already tied to it.");
                    }
                }

                else
                {
                    Game.output("You can't tie the " + state.indirectObject.name + " to that.");
                }
            } break;

            default:
            {
                super.tie();
            } break;
        }

    }


    @Override
    public void touch()
    {
        switch (name)
        {
            case "gate":
            {
                Game.output(ObjectStrings.DEAD_GATE);
            } break;

            case "mirror":
            {
                Game.output(touchString);
                if (state.playerLocation == Location.MIRROR_ROOM_SOUTH)
                {
                    state.playerPreviousLocation = Location.MIRROR_ROOM_SOUTH;
                    state.playerLocation = Location.MIRROR_ROOM_NORTH;
                }
                else
                {
                    state.playerPreviousLocation = Location.MIRROR_ROOM_NORTH;
                    state.playerLocation = Location.MIRROR_ROOM_SOUTH;
                }

                for (GameObject g : state.objectList.values())
                {
                    if (g.isItem() && g.location == state.playerPreviousLocation)
                        g.location = state.playerLocation;
                }
            } break;

            default:
            {
                super.touch();
            }
        }

    }

    @Override
    public void turn()
    {
        switch (name)
        {
            case "bolt":
            {
                if (state.indirectObject.name.equals("wrench"))
                {
                    if (!state.yellowButtonPushed)
                    {
                        Game.output("The bolt won't turn with your best effort.");
                    }

                    else
                    {
                        if (state.damGatesOpen)
                        {
                            state.damGatesOpen = false;
                            Game.output("The sluice gates close and water starts to collect behind the dam.");
                        }

                        else
                        {
                            state.damGatesOpen = true;
                            Game.output("The sluice gates open and water pours through the dam.");
                        }
                    }
                }

                else
                {
                    Game.output("The bolt won't turn using the " + state.indirectObject.name);
                }
            } break;

            case "switch":
            {
                switch (state.indirectObject.name)
                {
                    case "screwdriver":
                    {
                        Container machine = (Container)state.objectList.get("machine");
                        if (!machine.isOpen() && !machine.inventory.isEmpty())
                        {
                            Game.output(ObjectStrings.MACHINE_SUCCESS);

                            Item subject = machine.inventory.get(0);
                            machine.inventory.clear();
                            if (subject.name.equals("small pile of coal"))
                            {
                                subject.location = Location.NULL_LOCATION;
                                Item diamond = (Item)state.objectList.get("huge diamond");
                                diamond.location = Location.INSIDE_COAL_MACHINE;
                            }

                            else
                            {
                                subject.location = Location.NULL_LOCATION;
                                Item slag = (Item)state.objectList.get("small piece of vitreous slag");
                                slag.location = Location.INSIDE_COAL_MACHINE;
                            }

                            state.refreshInventories();
                        }

                        else
                            Game.output("The machine doesn't seem to want to do anything.");
                    } break;

                    case "dummy_object":
                    {
                        Game.output("You can't turn it with your bare hands...");
                    } break;

                    default:
                    {
                        Game.output("It seems that " + state.indirectObject.articleName + " won't do.");
                    } break;
                }
            } break;

            default:
            {
                super.turn();
            } break;
        }

    }


    @Override
    public void unlock()
    {
        switch (name)
        {
            case "grating":
            {
                if (state.indirectObject.name.equals("skeleton key"))
                {
                    if (state.playerLocation == Location.GRATING_ROOM)
                    {
                        Game.output("The grate is unlocked.");
                        state.gratingUnlocked = true;
                    }

                    else if (state.playerLocation == Location.CLEARING_NORTH)
                    {
                        Game.output("You can't reach the lock from here.");
                    }
                }

                else
                {
                    Game.output("Can you unlock a grating with " + state.indirectObject.articleName + "?");
                }
            } break;

            default:
            {
                super.unlock();
            } break;
        }
    }
    

    public String toString() { return name; }

}

