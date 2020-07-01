import java.util.ArrayList;

class Feature extends GameObject {


    public Feature(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.FEATURE;
    
    }


    @Override
    public void breakObject(GameState state)
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
                super.breakObject(state);
            }
        }

    }


    @Override
    public void close(GameState state)
    {
        switch (name)
        {
            case "kitchen window":
            {
                Room r = state.worldMap.get(Location.BEHIND_HOUSE);
                Passage p = r.exits.get(Action.WEST);
                if (p.isOpen())
                {
                    Game.output(GameStrings.WINDOW_CLOSES);
                    examineString = ObjectStrings.WINDOW_EXAMINE_CLOSED;
                    p.close();
                }
                else
                    Game.output("The window is already closed.");
            } break;

            case "trap door":
            {
                Room r = state.worldMap.get(Location.LIVING_ROOM);
                Passage p = r.exits.get(Action.DOWN);
                if (p.isOpen())
                {
                    Game.output("Done.");
                    r.description = MapStrings.DESC_LIVING_ROOM_TRAPDOOR_CLOSED;
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
    public void lookIn(GameState state)
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

            default: { super.lookIn(state); } break;
        }

    }


    @Override
    public void lookOut(GameState state)
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

            default: { super.lookOut(state); } break;
        }

    }

    
    @Override
    public void move(GameState state)
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
                    Game.output(GameStrings.MOVE_RUG);
                    Room rm = state.worldMap.get(Location.LIVING_ROOM);
                    rm.description = MapStrings.DESC_LIVING_ROOM_TRAPDOOR_CLOSED;
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
                Game.output(moveString);
            } break;
        }

    }


    @Override
    public void open(GameState state)
    {
        switch (name)
        {
            case "kitchen window":
            {
                Room r = state.worldMap.get(Location.BEHIND_HOUSE);
                Passage p = r.exits.get(Action.WEST);
                if (!p.isOpen())
                {
                    Game.output(GameStrings.WINDOW_OPENS);
                    examineString = ObjectStrings.WINDOW_EXAMINE_OPEN;
                    p.open();
                }
                else
                    Game.output(GameStrings.getHardSarcasm());
            } break;

            case "trap door":
            {
                Room r = state.worldMap.get(Location.LIVING_ROOM);
                Passage p = r.exits.get(Action.DOWN);
                if (!p.isOpen())
                {
                    Game.output(GameStrings.TRAP_DOOR_OPENS);
                    r.description = MapStrings.DESC_LIVING_ROOM_TRAPDOOR_OPEN;
                    p.open();
                }
                else
                {
                    Game.output(GameStrings.getHardSarcasm());
                }
            } break;


            default:
            {
                Game.output(openString);
            } break;
        }

    }


    @Override
    public void push(GameState state)
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

            } break;


            default:
            {
                super.push(state);
            } break;
        }

    }


    @Override
    public void tie(GameState state)
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
                super.tie(state);
            } break;
        }

    }


    @Override
    public void touch(GameState state)
    {
        switch (name)
        {
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
                super.touch(state);
            }
        }

    }
    

    public String toString() { return name; }

}

