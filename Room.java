import java.util.HashMap;

class Room {
    
    public final String name;
    public final Location roomID;
    public String description;
    public int discoverValue;
    public String jumpString;
    public boolean darkness;
    public boolean firstVisit;
    public boolean height;

    public HashMap<Action, Passage> exits;
    public HashMap<Action, String> failMessages;

    public Room(String nm, String desc, Location loc)
    {
        name = nm;
        roomID = loc;
        description = desc;
        discoverValue = 0;
        jumpString = "";
        darkness = false;
        firstVisit = true;
        height = false;

        exits = new HashMap<Action, Passage>();
        failMessages = new HashMap<Action, String>();
    }


    public boolean exit(GameState state, Action act)
    {
        Passage psg = null;
        boolean result = false;
        Location dest = Location.NULL_LOCATION;


        // Identify which direction the player is trying to go.
        if (exits.containsKey(act))
        {
            psg = exits.get(act);
        }

        else
        {
            // Darkness check
            if (state.darkness && !state.playerDead)
            {
                Game.output(GameStrings.GRUE_DEATH_1);
                state.playerDies();
                return false;
            }

            if (failMessages.containsKey(act))
                Game.output(failMessages.get(act));
            else
                Game.output(GameStrings.CANT_GO);

            return false;
        }

        // Check here for baggage limit passages.
        if (state.playerCarryWeight > psg.weightLimit)
        {
            Game.output(psg.weightFail);
            return false;
        }


        // Figure out which side of the Passage the player is on.
        if (psg.locationA == roomID) { dest = psg.locationB; }
        else { dest = psg.locationA; }

        // Darkness check. If the room is in darkness, and the destination
        // is not the previous room, player dies by grue.
        if (isDark() && !state.lightActivated && (dest != state.playerPreviousLocation) && !state.playerDead)
        {
            Game.output(GameStrings.GRUE_DEATH_1);
            state.playerDies();
            return false;

        }

        // If the Passage is open... success
        if (psg.isOpen())
        {
            if (!psg.message.isEmpty())
            {
                Game.output(psg.message);
                Game.outputLine();
            }

            state.playerPreviousLocation = state.playerLocation;
            state.playerLocation = dest;
            result = true;
        }

        else
        {    
            Game.output(psg.closedFail);
        }
        

        return result;

    }


    public void getDescription(GameState state)
    {
        if (state.playerDead)
        {
            if (darkness)
            {
                Game.output(GameStrings.DEAD_LOOK);
                Game.outputLine();
            }
            Game.output(description);
            return;
        }

        if (darkness && !state.lightActivated)
        {
            Game.output(GameStrings.DARKNESS);
            return;
        }

        String result = description;

        switch (roomID)
        {
            case ARAGAIN_FALLS:
            {
                if (state.rainbowSolid)
                    result += "\nA solid rainbow spans the falls.";
            } break;

            case BEHIND_HOUSE:
            {
                if (state.houseWindowOpened)
                    result = MapStrings.DESC_BEHIND_HOUSE_WINDOW_OPEN;
            } break;

            case DAM:
            {
                if (state.damGatesOpen && state.damWaterHigh)
                    result += MapStrings.DAM_GATES_OPEN_HIGH;

                if (state.damGatesOpen && !state.damWaterHigh)
                    result += MapStrings.DAM_GATES_OPEN_LOW;

                if (!state.damGatesOpen && state.damWaterHigh)
                    result += MapStrings.DAM_GATES_CLOSED_HIGH;

                if (!state.damGatesOpen && !state.damWaterHigh)
                    result += MapStrings.DAM_GATES_CLOSED_LOW;

                if (state.yellowButtonPushed)
                    result += MapStrings.DAM_BUBBLE_ON;

                else
                    result += MapStrings.DAM_BUBBLE_OFF;
                
            } break;

            case DEEP_CANYON:
            {
                if (state.waterFalling)
                    result = MapStrings.DESC_DEEP_CANYON_RUSH;

                else if (state.loudRoomSolved || state.damWaterLow)
                    result = MapStrings.DESC_DEEP_CANYON_QUIET;

                else
                    result = MapStrings.DESC_DEEP_CANYON_WATER;

            } break;

            case KITCHEN:
            {
                if (state.houseWindowOpened)
                    result = MapStrings.DESC_KITCHEN_WINDOW_OPEN;
            } break;

            case LIVING_ROOM:
            {

            } break;

            case LOUD_ROOM:
            {
                if (state.waterFalling)
                    result = MapStrings.DESC_LOUD_ROOM_WATER;

                else if (state.loudRoomSolved || state.damWaterLow)
                    result = MapStrings.DESC_LOUD_ROOM_QUIET;

                else
                    result = MapStrings.DESC_LOUD_ROOM;

            } break;

            case MACHINE_ROOM:
            {
                Container machine = (Container)state.objectList.get("machine");
                if (machine.isOpen())
                    result += " On the front of the machine is a large lid, which is open.";
                else
                    result += " On the front of the machine is a large lid, which is closed.";
            } break;

            case MIRROR_ROOM_SOUTH:
            case MIRROR_ROOM_NORTH:
            {
                if (state.mirrorBroken)
                    result += "\nUnfortunately, the mirror has been destroyed by your recklessness.";
            } break;

            case SHAFT_ROOM:
            case DRAFTY_ROOM:
            {
                if (state.shaftBasketLowered)
                    result += "\nFrom the chain is suspended a basket.";
                else
                    result += "\nAt the end of the chain is a basket.";
            } break;


            default:
            {
                
            } break;
        }

        Game.output(result);

    }


    public void getRoomObjects(GameState state)
    {
        state.refreshInventories();

        if (darkness && !state.lightActivated && !state.playerDead)
        {
            return;
        }

        for (GameObject g : state.objectList.values())
        {
            if (g.location != roomID) continue;
            
            if ( (g.isActor() || g.isFeature()) && !g.presenceString.isEmpty())
            {
                Game.output(g.presenceString);
            }

            if (g.isItem())
            {
                Item it = (Item)(g);
                Game.output(it.getItemDescription());
            }

            // An annoying exception: the glass bottle, in which the water can be seen
            // even though it's closed.

            if (g.name.equals("glass bottle") && state.bottleFilled)
                Game.output("The glass bottle contains:\n  A quantity of water");

            if (g.isSurface())
            {

                if (!g.inventory.isEmpty())
                {
                    boolean check = true;

                    for (Item it : g.inventory)
                    {
                        if (it.initialPresenceString.isEmpty() || it.movedFromStart)
                        {
                            check = false;
                        }
                    }

                    if (check)
                    {
                        for (Item it : g.inventory)
                            Game.output(it.initialPresenceString);
                    }

                    else
                    {
                        Game.output("Sitting on the " + g.name + " is:");
                        for (Item it : g.inventory)
                        {
                            Game.output("  " + it.capArticleName);
                            if (it.name.equals("glass bottle") && state.bottleFilled)
                                Game.output("The glass bottle contains:\n  A quantity of water");
                        }
                    }

                }
            }

            if (g.isContainer() && g.isOpen())
            {
                // Shaft basket can be in one of two places
                if (g.name.equals("basket"))
                {
                    if (state.playerLocation == Location.SHAFT_ROOM && state.shaftBasketLowered)
                        return;
                    if (state.playerLocation == Location.DRAFTY_ROOM && !state.shaftBasketLowered)
                        return;
                }


                if (!g.inventory.isEmpty())
                {
                    boolean check = true;

                    for (Item it : g.inventory)
                    {
                        if (it.initialPresenceString.isEmpty() || it.movedFromStart)
                        {
                            check = false;
                        }
                    }

                    if (check)
                    {
                        for (Item it : g.inventory)
                            Game.output(it.initialPresenceString);
                    }

                    else
                    {
                        Game.output("The " + g.name + " contains:");
                        for (Item it : g.inventory)
                        {
                            Game.output("  " + it.capArticleName);
                            if (it.name.equals("glass bottle") && state.bottleFilled)
                                Game.output("The glass bottle contains:\n  A quantity of water");
                        }
                    }
                }
            }    
        }
        
    }


    public void lookAround(GameState state)
    {
        Game.output(name);
        Game.outputLine();

        getDescription(state);
        getRoomObjects(state);

    }


    public void addExit(Action act, Passage psg)
    {
        exits.put(act, psg);
    }

    public void addFailMessage(Action act, String msg)
    {
        failMessages.put(act, msg);
    }

    public void removeFailMessage(Action act)
    {
        failMessages.remove(act);
    }

    public void setDark() { darkness = true; }
    public void setLight() { darkness = false; }
    public boolean isDark() { return darkness; }


}
