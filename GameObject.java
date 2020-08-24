import java.util.ArrayList;
import java.util.Random;



public abstract class GameObject {
    
    public final String name;
    public String articleName;
    public String capArticleName;
    public ObjectType type;
    public boolean isWeapon;
    public boolean intangible;
    protected GameState state;

    public Location location;
    public Location inventoryID;
    public String initialPresenceString;
    public String presenceString;
    public boolean movedFromStart;

    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;
    public ArrayList<String> altNames;

    public String answerString;  
    public String attackString;
    public String blowString;
    public String boardString;
    public String breakString;
    public String brushString;
    public String burnString;
    public String climbString;
    public String closeString;
    public String countString;
    public String crossString;
    public String cutString;
    public String deflateString;
    public String digString;
    public String digStringInd;
    public String drinkString;
    public String eatString;
    public String enterString;
    public String enterItemString;
    public String examineString;
    public String extinguishString;
    public String fillString;
    public String followString;
    public String giveString;
    public String helloString;
    public String inflateString;
    public String kickString;
    public String knockString;
    public String launchString;
    public String lightString;
    public String listenString;
    public String lockString;
    public String lookInString;
    public String lookOutString;
    public String lookUnderString;
    public String lowerString;
    public String moveString;
    public String moveItemString;
    public String openString;
    public String pourString;
    public String pullString;
    public String pushString;
    public String putString;
    public String raiseString;
    public String readString;
    public String removeString;
    public String repairString;
    public String ringString;
    public String searchString;
    public String shakeString;
    public String smellString;    
    public String takeString;
    public String talkString;
    public String throwString;
    public String tieString;
    public String touchString;
    public String turnString;
    public String unlockString;
    public String untieString;
    public String wakeString;
    public String waveString;
    public String wearString;
    public String windString;

    public String noEffect1 = " doesn't seem to work.";
    public String noEffect2 = " has no effect.";
    public String noEffect3 = " isn't notably helpful.";

    public String[] noEffect = { noEffect1, noEffect2, noEffect3 };
    

    public GameObject(String nm, Location loc)
    {
        name = nm;
        location = loc;

        articleName = (vowelStart() ? "an " : "a ") + name;
        capArticleName = (vowelStart() ? "An " : "A ") + name;

        isWeapon = false;
        movedFromStart = false;
        intangible = false;

        setStrings();

        altLocations = new ArrayList<Location>();
        altNames = new ArrayList<String>();
        inventory = new ArrayList<Item>();

        altLocations.add(location);
        inventoryID = Location.NULL_INVENTORY;

        state = Game.gameState;
    
    }


    public void setStrings()
    {
        initialPresenceString = "";
        presenceString = "There is " + articleName + " here.";

        answerString = "It is hardly likely that the " + name + " is interested.";
        blowString = "You can't blow that out.";
        boardString = "You have a theory on how to board " + articleName + ", perhaps?";
        brushString = "If you wish, but heaven only knows why.";
        climbString = "You can't do that!";
        closeString = "You must tell me how to do that to " + articleName + ".";
        countString = "You have lost your mind.";
        crossString = "You can't cross that!";
        deflateString = "Come on, now!";
        drinkString = "I don't think that the " + name + " would agree with you.";
        eatString = "I don't think that the " + name + " would agree with you.";
        enterString = "You hit your head against the " + name + " as you attempt this feat.";
        examineString = "There's nothing special about the " + name + ".";
        extinguishString = "You can't turn that off.";
        followString = "You're nuts!";
        helloString = "It's a well known fact that only schizophrenics say \"Hello\" to " + articleName + ".";
        inflateString = "How can you inflate that?";
        knockString = "Why knock on " + articleName + "?";
        launchString = "How exactly do you imagine trying a launch " + articleName + "?";
        lightString = "You can't turn that on.";
        listenString = "The " + name + " makes no sound.";
        lookInString = "You can't look inside " + articleName + ".";
        lookOutString = "You can't look out of " + articleName + ".";
        lookUnderString = "There is nothing but dust there.";
        moveString = "Moving the " + name + " reveals nothing.";
        openString = "You must tell me how to do that to " + articleName + ".";
        pourString = "How were you planning to pour something which is not a liquid?";
        pullString = "";    // game treats this as "move"
        readString = "You can't read that!";
        removeString = "You can't read that!";
        repairString = "This has no effect.";
        ringString = "How, exactly, can you ring that?";
        searchString = "You find nothing unusual.";
        shakeString = "Shaken.";
        smellString = "It smells like " + articleName + ".";    
        takeString = "";
        talkString = "You can't talk to the " + name + "!";
        touchString = "";
        untieString = "The " + name + " cannot be tied, so it cannot be untied!";
        wakeString = "The " + name + " isn't sleeping.";
        wearString = "You can't wear the " + name + ".";
        windString = "You cannot wind up " + articleName + ".";

        // These strings are used for items in the player's inventory.
        enterItemString = "That would involve quite a contortion!";
        moveItemString = "You aren't an accomplished enough juggler.";

        // These strings have one of three endings randomly added.
        kickString = "Kicking the " + name;
        lowerString = "Playing in this way with the " + name;
        raiseString = "Playing in this way with the " + name;
        pushString = "Pushing the " + name;
        touchString = "Fiddling with the " + name;
        waveString = "Waving the " + name;

        // indirect action objects
        attackString = "I've known strange people, but fighting " + articleName + "?";
        breakString = "";
        burnString = "";
        cutString = "Strange concept, cutting the " + name + "...";
        digString = "";
        digStringInd = "Digging with " + articleName + " is silly.";
        fillString = "You may know how to do that, but I don't.";
        giveString = "";
        lockString = "";
        putString = "There's no good surface on the " + name + ".";
        throwString = "";
        tieString = "";
        turnString = "";
        unlockString = "It doesn't seem to work.";


        // Modifications

        switch (name)
        {
            case "small mailbox":
            case "house":
            {
                readString = "How does one read " + articleName + "?";
            } break;

            default:
            {

            } break;
        }

    }


    // Common methods to all objects, which will be overridden in the child classes. 
    public void answer() { Game.output(answerString); }
    public void attack() { Game.output(attackString); }
    public void blow() { Game.output(blowString); }
    public void board() { Game.output(boardString); }
    public void brush() { Game.output(brushString); }
    public void burn() { Game.output(burnString); }
    public void breakObject()
    {
        if (state.indirectObject.isWeapon)
        {
            Game.output("Nice try.");
        }

        else
        {
            Game.output("Trying to destroy " + articleName + " with "
            + state.indirectObject.articleName + " is futile.");
        }
    }
    public void climb() { Game.output(climbString); }
    public void close() { Game.output("You can't close that."); }
    public void count() { Game.output(countString); }
    public void cross() { Game.output(crossString); }    
    public void cut()
    {
        String word = "";
        String weapon = state.indirectObject.name;
        if (weapon.equals("elvish sword")) word = "swordsmanship";
        else if (weapon.equals("bloody axe")) word = "axesmanship";
        else if (weapon.equals("stiletto")) word = "stilettosmanship";
        else word = "knifesmanship";

        switch(state.indirectObject.name)
        {
            case "elvish sword":
            case "nasty knife":
            case "rusty knife":
            case "stiletto":
            case "bloody axe":
            {

                switch (name)
                {
                    case "ancient map":
                    case "guidebook":
                    case "leaflet":
                    case "matchbook":
                    case "rope":
                    case "tan label":
                    case "ZORK owner's manual":
                    {
                        Game.output("Your skillful " + word + " slices the " + name
                            + " into innumerable slivers which blow away.");

                        location = Location.NULL_LOCATION;
                    } break;

                    case "black book":
                    {
                        Game.output(ObjectStrings.BLACK_BOOK_CUT);
                        state.playerDies();
                    } break;

                    case "brown sack":
                    {
                        if (!inventory.isEmpty())
                        {
                            Game.output("The sack is cut open and its contents spill onto the floor.");
                            for (GameObject g : inventory)
                                g.location = state.playerLocation;
                        }

                        else
                        {
                            Game.output("The sack has been cut open and now rendered useless.");
                        }

                        location = Location.NULL_LOCATION;

                    } break;

                    case "magic boat":
                    {

                    } break;

                    case "painting":
                    {

                    } break;

                    default:
                    {
                        Game.output("Strange concept, cutting the " + name + "...");
                    }
                }

            } break;

            default:
            {
                Game.output("The \"cutting edge\" of " + state.indirectObject.articleName + " is hardly adequate.");
            } break;
        }
    }
    public void deflate() { Game.output(deflateString); }
    public void dig()
    {
        Game.output("Digging with " + state.indirectObject.articleName + " is silly.");
    }
    public void drink() { Game.output(drinkString); }
    public void drop() { Game.output("You can't drop that."); }
    public void eat() { Game.output(eatString); }
    public void enter()
    {
        if (state.directObject.name.equals("magic boat"))
            board();
        else
        {
            if (isItem())
                Game.output(enterItemString);
            else 
                Game.output(enterString);
        }
    }
    public void examine() { Game.output(examineString); }
    public void extinguish() { Game.output(extinguishString); }   
    public void fill() { Game.output(fillString); }
    public void follow() { Game.output(followString); }  
    public void give()
    {
        Game.output("You can't give " + state.indirectObject.articleName + " to "
            + state.directObject.articleName + "!");
    }
    public void greet() { Game.output(helloString); }  
    public void inflate() { Game.output(inflateString); }
    public void kick() { Game.output(kickString + randPhrase()); }
    public void knock() { Game.output(knockString); }
    public void launch() { Game.output(launchString); }
    public void light() { Game.output(lightString); }
    public void listen() { Game.output(listenString); }
    public void lock() { Game.output("You can't lock that."); }
    public void lookIn() { Game.output(lookInString); }
    public void lookOut() { Game.output(lookOutString); }
    public void lookUnder() { Game.output(lookUnderString); }
    public void lower() { Game.output(lowerString + randPhrase()); }
    public void move() { Game.output(moveString); }
    public void moveItem() { Game.output(moveItemString); }
    public void open() { Game.output("You can't open that."); }
    public void pour() { Game.output(pourString); }
    public void pull() { Game.output(pullString); }
    public void push() { Game.output(pushString + randPhrase()); }    
    public void put() { Game.output(putString); }
    public void raise() { Game.output(raiseString + randPhrase()); }
    public void read() { Game.output(readString); }
    public void remove() { Game.output(removeString); }
    public void repair() { Game.output(repairString); }
    public void ring() { Game.output(ringString); }
    public void search() { Game.output(searchString); }
    public void shake() { Game.output(shakeString); }
    public void smell() { Game.output(smellString); }   
    public void take()
    {
        if (takeString.isEmpty())
            Game.output(GameStrings.getSarcasticResponse());
        else
            Game.output(takeString);
    }
    public void talk() { Game.output(talkString); }   
    public void throwObject() { Game.output(throwString); }
    public void tie() { Game.output("You can't tie the " + state.indirectObject.name + " to that."); }
    public void touch() { Game.output(touchString + randPhrase()); }
    public void turn() { Game.output(turnString); }
    public void untie() { Game.output(untieString); }
    public void wake() { Game.output(wakeString); }
    public void wave() { Game.output(waveString + randPhrase()); }
    public void wear() { Game.output(wearString); }
    public void wind() { Game.output(windString); }
    public void unlock() { Game.output(unlockString); }


    public void getDescription()
    {
        if (initialPresenceString.isEmpty() || movedFromStart)
            Game.output(presenceString);
        else
            Game.output(initialPresenceString);

    }
    public boolean isActor() { return type == ObjectType.ACTOR; }
    public boolean isAlive() { return false; }
    public boolean isContainer() { return false; }
    public boolean isItem() { return type == ObjectType.ITEM; }
    public boolean isFeature() { return type == ObjectType.FEATURE; }
    public boolean isOpen() { return false; }
    public boolean isSurface() { return type == ObjectType.SURFACE; }
    public boolean playerHasObject() { return location == Location.PLAYER_INVENTORY; }
    public String randPhrase()
    {
        Random rand = new Random();

        int i = rand.nextInt(noEffect.length);

        return noEffect[i];


    }
    public void tick() {}
    public String toString() { return name; }
    public boolean vowelStart()
    {
        // Exceptions can go here
        if (false)
        {
            return true;
        }

        boolean result = false;

        String str = name.toLowerCase();
        char c  = str.charAt(0);

        switch(c)
        {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            {
                result = true;
            } break;

            default:
            {
                result = false;
            } break;
        }

        return result;

    }


    

}