import java.util.HashMap;

public class InputParser {

    private GameState state;
    private String input;
    private String[] inputWords;
    private String parseActionString;
    private String parseDirectString;
    private String parseIndirectString;

    // ATTACK, BREAK, BURN, CUT, DIG, FILL, INFLATE, LOCK,
    // TURN, UNLOCK,

    // GIVE, PUT, TIE, THROW

    private HashMap<Action, String> prepos;
    
    public InputParser (GameState st)
    {
        state = st;

        input = state.completePlayerInput;
        inputWords = input.split(" ");
        parseActionString = "";
        parseDirectString = "";
        parseIndirectString = "";

        prepos = new HashMap<Action, String>();

        prepos.put(Action.ATTACK, "with");
        prepos.put(Action.BREAK, "with");
        prepos.put(Action.BURN, "with");
        prepos.put(Action.CUT, "with");
        prepos.put(Action.DIG, "with");
        prepos.put(Action.FILL, "with");
        prepos.put(Action.GIVE, "to");
        prepos.put(Action.INFLATE, "with");
        prepos.put(Action.LOCK, "with");
        prepos.put(Action.PUT, "in");
        prepos.put(Action.THROW, "at");
        prepos.put(Action.TIE, "to");
        prepos.put(Action.TURN, "with");
        prepos.put(Action.UNLOCK, "with");

    }


    public void inputTest()
    {
        Game.outputLine();
        Game.output("Complete player input: " + state.completePlayerInput);
        // Game.output("Input words: ");
        // for (int i = 0; i < inputWords.length; ++i)
        //     Game.output(inputWords[i] + " ");
        Game.output("String in parseActionInput() is \"" + parseActionString + "\"");
        Game.output("String in parseDirectObject() is \"" + parseDirectString + "\"");
        Game.output("String in parseIndirectObject() is \"" + parseIndirectString + "\"");
        Game.output("Player action is " + state.playerAction);
        Game.output("Action type is " + state.playerActionType);
        Game.output("Direct object is " + state.directObject);
        Game.output("Indirect object is " + state.indirectObject);

    }


    public boolean isGameWord(String str)
    {
        return (state.dictionary.contains(str));
    }


    public boolean parseDirectObject()
    {
        boolean check = false;

        for (int i = 0; i < state.currentObjectNames.length; ++i)
        {
            String token = state.currentObjectNames[i];

            if (startsWith(token, input))
            {
                check = true;
                state.directObject = state.currentObjects.get(token);
                input = input.substring(token.length()).trim();
                break;
            }
        }

        if (!check)
        {
            for (String token : state.gameNouns)
            {
                if (startsWith(token, input))
                {
                    Game.output("You can't see any " + token + " here!");
                    return check;
                }
            }

            Game.output("You used the phrase \"" + state.firstInputPhrase
                + "\" in a way I don't understand.");
            if (Game.debug) Game.output("Method: parseDirectObject()");
        }


        return check;

    }


    public boolean parseIndirectObject()
    {
        boolean check = false;

        for (int i = 0; i < state.currentObjectNames.length; ++i)
        {
            String token = state.currentObjectNames[i];

            if (startsWith(token, input))
            {
                check = true;
                state.indirectObject = state.currentObjects.get(token);
                input = input.substring(token.length()).trim();
                break;
            }
        }

        // If the user enters a valid action, but an invalid object.
        if (!check)
        {
            for (String token : state.gameNouns)
            {
                if (startsWith(token, input))
                {
                    Game.output("You can't see any " + token + " here!");
                    return check;
                }
            }

            Game.output("You used the phrase \"" + state.firstInputPhrase
                + "\" in a way I don't understand.");
            if (Game.debug) Game.output ("Method: parseIndirectObject()");
        }

        return check;

    }


    public boolean parseInputAction()
    {
        parseActionString = input;

        // Compare the beginning of the input to the set of action phrases.
        boolean check = false;

        for (int i = 0; i < state.actionNames.length; ++i)
        {
            String token = state.actionNames[i];

            if (startsWith(token, input))
            {
                check = true;
                state.playerAction = state.actions.get(token);
                state.playerActionType = state.actionTypes.get(state.playerAction);
                state.firstInputPhrase = token;
                input = input.substring(token.length()).trim();
                break;
            }
        }

        return check;

    }


    /**
     * Returns true if the player inputs a phrase which is fully recognized by the game.
     * If not, player is re-prompted and no turn is taken.
     */
    public boolean parsePlayerInput()
    {
        // Godmode check
        if (Game.godmode && processGodmode())
            return false;

        // The loud room needs to be dealt with here
        if (state.playerLocation == Location.LOUD_ROOM && !state.loudRoomSolved && !state.damWaterLow)
        {
            if (input.equals("echo"))
            {
                Game.output(MapStrings.LOUD_ROOM_CHANGE);
                state.loudRoomSolved = true;
                return false;
            }

            String[] words = input.trim().split(" ");
            String lastWord = words[words.length - 1];

            parseInputAction();

            switch (state.playerAction)
            {
                case EAST:
                case WEST:
                case UP:
                case QUIT:
                {
                    return true;
                }

                default:
                {
                    Game.output(lastWord + " " + lastWord + "...");
                } break;

            }

            return false;
        }

        // Get previous input if player typed "again"
        if (input.equals("again") || input.equals("g"))
        {
            input = state.playerPreviousInput;
            if (input.isEmpty())
            {
                Game.output("Again what?");
                return false;
            }
            return parsePlayerInput();
        }

        if (input.equals("godmode"))
        {
            if (!Game.godmode)
            {
                Game.godmode = true;
                Game.output("God mode enabled.");
            }

            else
            {
                Game.godmode = false;
                Game.output("God mode disabled.");
            }
            
            return false;
        }

        if (input.equals("debug"))
        {
            if (!Game.debug)
            {
                Game.debug = true;
                Game.output("Debug mode enabled.");                
            }

            else
            {
                Game.debug = false;
                Game.output("Debug mode disabled.");
            }

            return false;
        }

        // Inside jokes, special cases, profanity, etc
        if (specialInputCheck())
            return false;

        // Remove articles and prepositions
        input = " " + input + " ";
        input = input.replaceAll(" back ", " ");
        input = input.replaceAll(" from ", " ");
        input = input.replaceAll(" of ", " ");
        input = input.replaceAll(" the ", " ");
        input = input.replaceAll(" to ", " ");
        input = input.replaceAll(" with ", " ");
        while(input.contains("  "))
            input = input.replaceAll("  ", " ");
        input = input.trim();
        
        // All words must be known by the game

        if (!startsWith("say", input) && !startsWith("speak", input))
        {
            for (int i = 0; i < inputWords.length; ++i)
            {
                if (!isGameWord(inputWords[i]))
                {
                    Game.output("I don't know what \"" + inputWords[i] + "\" means.");
                    return false;
                }
            }
        }

        // Get player action
        if (!parseInputAction())
        {
            Game.output("Sentence did not start with an action!");  
            return false;
        }

        input = input.replaceAll(" at ", " ");
        input = input.replaceAll(" in ", " ");
        input = input.replaceAll(" on ", " ");
        input = input.replaceAll(" out ", " ");

        switch (state.playerActionType)
        {
            case REFLEXIVE:
            case EXIT:
            {
                // If the player entered more text, it's an error.
                if (!input.isEmpty())
                {
                    Game.output("You used the phrase \"" + state.firstInputPhrase
                        + "\" in a way I don't understand.");
                    if (Game.debug) Game.output("Method: parsePlayerInput() Reflexive/Exit actions");
                    return false;
                }
            } break;

            case DIRECT:
            {
                // If the player entered a direct action without an object,
                // they will be prompted to provide the object.

                /* The player can launch the boat with just the word "launch".
                 * Otherwise, the game treat it as a direct object action.
                 */
                if (state.playerAction == Action.LAUNCH)
                {
                    if (input.isEmpty() && state.playerInBoat)
                    {
                        state.directObject = state.objectList.get("magic boat");
                        return true;
                    }
                }

                if (input.isEmpty())
                {
                    Game.output("What do you want to " + state.firstInputPhrase + "?");
                    input = Game.getPlayerText();
                }

                
                parseDirectString = input;

                if (!parseDirectObject())
                    return false;

                if (!input.isEmpty())
                {
                    Game.output("That sentence isn't one I recognize.");
                    return false;
                }
            } break;

            case INDIRECT:
            case SWITCH:
            {

                // If empty, prompt for direct object.
                if (input.isEmpty())
                {
                    Game.output("What do you want to " + state.firstInputPhrase + "?");
                    input = Game.getPlayerText();
                }

                parseDirectString = input;
                if (!parseDirectObject())
                    return false;

                // If empty, prompt for indirect object.
                // Might need to individualize this.
                if (input.isEmpty())
                {
                    if (state.playerActionType == ActionType.SWITCH)
                        return true;
                    
                    try
                    {
                        String prep = prepos.get(state.playerAction);
                        Game.output("What do you want to " + state.firstInputPhrase + " the " 
                            + parseDirectString + " " + prep + "?");
                    }
                    catch (NullPointerException e)
                    {
                        Game.output("Enter an indirect object: ");
                    }

                    input = Game.getPlayerText();
                }

                parseIndirectString = input;
                if (!parseIndirectObject())
                    return false;

                if (!input.isEmpty())
                {
                    Game.output("That sentence isn't one I recognize.");
                    return false;
                }

            } break;

            case INDIRECT_INVERSE:
            {
                // If empty, prompt for indirect object.
                if (input.isEmpty())
                {
                    Game.output("What do you want to " + state.firstInputPhrase + "?");
                    input = Game.getPlayerText();
                }


                parseIndirectString = input;
                if (!parseIndirectObject())
                    return false;


                // If empty, prompt for direct object.
                // Might need to individualize this.

                if (input.isEmpty())
                {
                    if (state.playerActionType == ActionType.SWITCH)
                        return true;
                    
                    try
                    {
                        String prep = prepos.get(state.playerAction);
                        Game.output("What do you want to " + state.firstInputPhrase + " the " 
                            + parseIndirectString + " " + prep + "?");
                    }
                    catch (NullPointerException e)
                    {
                        Game.output("Enter an indirect object: ");
                    }

                    input = Game.getPlayerText();
                }

                parseDirectString = input;
                if (!parseDirectObject())
                    return false;

                if (!input.isEmpty())
                {
                    Game.output("That sentence isn't one I recognize.");
                    return false;
                }


            } break;

            case SPEAK:
            {
                state.speakPhrase = input;
            } break;

            default: {} break;

        }

        return true;

    }


    public boolean processGodmode()
    {
        String teleport = "teleport";
        String accio = "accio";
        if (startsWith(teleport, input))
        {
            input = input.substring(teleport.length()).trim();

            boolean teleportCheck = false;

            for (Room rm : state.worldMap.values())
            {
                String roomName = rm.name.toLowerCase();
                if (roomName.equals(input))
                {
                    state.relocatePlayer(rm.roomID);
                    teleportCheck = true;
                    return true;
                }
            }

            if (!teleportCheck)
                Game.output("Room not found.");
            
            return true;
        }

        if (startsWith(accio, input))
        {
            if (state.playerDead)
            {
                Game.output("You cannot possess anything while dead.");
                return true;
            }

            input = input.substring(accio.length()).trim();

            boolean accioCheck = false;

            for (GameObject g : state.objectList.values())
            {
                if (g.isItem())
                {
                    if (g.name.equals(input) || g.altNames.contains(input))
                    {
                        Game.output("You now have the " + input + ".");
                        g.location = Location.PLAYER_INVENTORY;
                        g.movedFromStart = true;
                        accioCheck = true;

                        if (g.name.equals("rope") && state.ropeRailTied)
                        {
                            state.ropeRailTied = false;
                            g.location = Location.PLAYER_INVENTORY;

                            Room rm1 = state.worldMap.get(Location.DOME_ROOM);
                            Room rm2 = state.worldMap.get(Location.TORCH_ROOM);
                            rm1.description = MapStrings.DESC_DOME_ROOM;
                            rm2.description = MapStrings.DESC_TORCH_ROOM;
                            rm2.removeFailMessage(Action.UP);
                            Passage psg = rm1.exits.get(Action.DOWN);
                            psg.close();
                        }
                        return true;
                    }
                }
            }

            if (!accioCheck)
                Game.output("Item not found.");

            return true;
        }

        if (input.equals("find thief"))
        {
            Actor thief = (Actor)state.objectList.get("thief");
            if (!thief.alive)
            {
                Game.output("The thief is dead.");
                return true;
            }
            String room = state.worldMap.get(thief.location).name.toLowerCase();
            Game.output("The thief is in the " + thief.location + ".");
            return true;
        }

        if (input.equals("instant win"))
        {
            Game.output("You now have the ancient map.");
            Item map = (Item)state.objectList.get("ancient map");
            map.location = Location.PLAYER_INVENTORY;

            Room rm = state.worldMap.get(Location.WEST_OF_HOUSE);
            Passage p = rm.exits.get(Action.SOUTHWEST);
            p.open();
            return true;
        }

        if (input.equals("reactivate lamp"))
        {
            Item lamp = (Item)(state.objectList.get("brass lantern"));
            lamp.lifespan = GameState.LANTERN_LIFESPAN;
            lamp.activated = true;
            Game.output("The lantern has been reactivated.");
            return true;
        }

        if (input.equals("reanimate troll"))
        {
            Actor troll = (Actor)state.objectList.get("troll");
            troll.alive = true;
            troll.unconscious = false;
            troll.location = Location.TROLL_ROOM;
            troll.hitPoints = Actor.MAX_ENEMY_HIT_POINTS;
            Game.output("The troll comes back to life.");
            return true;
        }

        if (input.equals("reanimate thief"))
        {
            Actor thief = (Actor)state.objectList.get("thief");
            Game.output("The thief comes back to life.");
            thief.alive = true;
            thief.location = Location.TREASURE_ROOM;
            return true;
        }

        if (input.equals("revive") && state.playerDead)
        {
            Game.output("You have come back to life.");
            state.playerDead = false;
            return true;
        }


        if (input.equals("terminate troll"))
        {
            Game.output("The troll is now dead.");
            Actor troll = (Actor)state.objectList.get("troll");
            troll.trollDies();
            return true;
        }

        if (input.equals("terminate thief"))
        {
            Game.output("The thief is now dead.");
            Actor thief = (Actor)state.objectList.get("thief");
            thief.thiefDies();
            return true;
        }

        if (input.equals("zombie") && !state.playerDead)
        {
            Game.output("You are now dead.");
            state.playerDead = true;
            return true;
        }

        return false;

    }


    public void reset()
    {
        input = state.completePlayerInput;
        inputWords = input.split(" ");
        parseActionString = "";
        parseDirectString = "";
        parseIndirectString = "";

    }


    public boolean specialInputCheck()
    {
        boolean result = false;

        // Instantly quit the game if a racial slur is used
        for (int i = 0; i < GameStrings.SLURS.length; ++i)
        {
            if (input.contains(GameStrings.SLURS[i]))
            {
                Game.gameover = true;
                return true;
            }
        }

        // Mild profanity will be tolerated
        for (int i = 0; i < GameStrings.PROFANITY.length; ++i)
        {
            if (input.contains(GameStrings.PROFANITY[i]))
            {
                Game.output("Such language in a high-class establishment like this!");
                return true;
            }
        }

        if (input.equals("author")) Game.output(GameStrings.AUTHOR_INFO);

        if (input.equals("bug"))
            Game.output("Bug? Maybe in the original program, but not in a flawless remake like this! (Cough, cough.)");

        if (input.equals("help"))
        {
            Game.output("Find the 19 lost treasures of Zork and return them to the trophy case.");
            Game.outputLine();
            Game.output("Try moving north, south, east, west, up, or down. Try to take objects and do things with them.");
            Game.outputLine();
            Game.output("Possible actions:");
            Game.outputLine();
            Game.output("activate  again  answer  attack  blow  board  break  brief  burn");
            Game.output("climb  close  count  cross  cut  defend  deflate  diagnose  dig");
            Game.output("down  drink  drop  east  eat  enter  examine  exit  extinguish");
            Game.output("fill  follow  give  go  in  inflate  inventory  jump  kick  knock  ");
            Game.output("launch  light  listen  lock  look  lower  move  north  northeast");
            Game.output("northwest  open  out  play  pour  pray  pull  push  put  quit  raise");
            Game.output("read  restore  restart  ring  save  say  search  shake  shout  slide");
            Game.output("smell  south  southeast  southwest  stay  strike  superbrief  swim");
            Game.output("take  talk  throw  tie  touch  turn  unlock  up  verbose  wait");
            Game.output("walk  wake  wave  wear  west  wind");
            return true;
        }

        // Old ZORK inside jokes
        if (input.equals("xyzzy") || input.equals("plugh"))
        {
            Game.output("A hollow voice says 'Fool.'");
            return true;
        }

        if (input.equals("hello sailor")   ||
            input.equals("hello, sailor!") ||
            input.equals("hello sailor!")  ||
            input.equals("hello, sailor") )
        {
            Game.output("Nothing happens here.");
            return true;
        }

        if (input.equals("zork"))
        {
            Game.output("At your service!");
            return true;
        }


        return result;

    }


    public boolean startsWith(String token, String input)
    {
        boolean check = true;

        String[] tokenWords = token.split(" ");
        String[] inputWords = input.split(" ");

        if (inputWords.length < tokenWords.length)
            check = false;

        else
        {
            for (int i = 0; i < tokenWords.length; ++i)
            {
                if (!tokenWords[i].equals(inputWords[i]))
                    check = false;
            }
        }

        return check;

    }


    public boolean validateAction()
    {
        GameObject dirObj = state.directObject;
        GameObject indObj = state.indirectObject;
        Action act = state.playerAction;

        if (act == Action.ENTER && dirObj.name.equals("magic boat") && dirObj.location == state.playerLocation)
            return true;

        switch(state.playerActionType)
        {
            case DIRECT:
            {
                if (dirObj.isItem() && dirObj.location != Location.PLAYER_INVENTORY)
                {
                    switch (act)
                    {
                        // Here is the list of actions that can be performed on items
                        // which are present but not in the player's inventory.
                        case ATTACK:
                        case BOARD:
                        case CLOSE:
                        case CUT:
                        case DEFLATE:
                        case INFLATE:
                        case KICK:
                        case LAUNCH:
                        case MOVE_OBJECT:
                        case OPEN:
                        case REPAIR:
                        case TAKE:
                        case UNTIE:
                        {

                        } break;
                        default:
                        {
                            Game.output("You're not carrying the " + dirObj.name + ".");
                            return false;
                        } break;
                    }
                }

            } break;

            case INDIRECT:
            case INDIRECT_INVERSE:
            {
                if (indObj.isItem() && indObj.location != Location.PLAYER_INVENTORY)
                {
                    Game.output("You're not carrying the " + indObj.name + ".");
                    return false;
                }
            } break;

            default: {} break;
        }
        
        return true;

    }
    
    
}
