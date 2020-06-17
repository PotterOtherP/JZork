import java.util.ArrayList;
import java.util.Random;



abstract class GameObject {
    
    public final String name;
    public Location location;
    public Location startLocation;
    public Location inventoryID;

    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;
    public ArrayList<String> altNames;

    public ObjectType type;
    public String articleName;
    public String capArticleName;
    public boolean movedFromStart;
    public boolean isWeapon;
    public String initialPresenceString;
    public String presenceString;

    public String answerString;  
    public String blowString;
    public String boardString;
    public String climbString;
    public String closeString;
    public String countString;
    public String crossString;
    public String deflateString;
    public String drinkString;
    public String eatString;
    public String enterString;
    public String enterItemString;
    public String examineString;
    public String extinguishString;
    public String followString;
    public String kickString;
    public String knockString;
    public String lightString;
    public String listenString;
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
    public String raiseString;
    public String readString;
    public String searchString;
    public String shakeString;
    public String smellString;    
    public String takeString;
    public String talkString;
    public String throwString;
    public String touchString;
    public String wakeString;
    public String waveString;
    public String wearString;
    public String windString;

    public String noEffect1 = " doesn't seem to work.";
    public String noEffect2 = " has no effect.";
    public String noEffect3 = " isn't notably helpful.";

    public String[] noEffect = { noEffect1, noEffect2, noEffect3 };
    
    // Indirect actions
    public String attackString;
    public String breakString;
    public String burnString;
    public String cutString;
    public String cutStringInd;
    public String digString;
    public String digStringInd;
    public String fillString;
    public String giveString;
    public String inflateString;
    public String lockString;
    public String putString;
    public String tieString;
    public String turnString;
    public String unlockString;

    


    // Constructors
    public GameObject(String nm, Location loc)
    {
        name = nm;
        location = loc;
        startLocation = loc;

        articleName = (vowelStart() ? "an " : "a ") + name;
        capArticleName = (vowelStart() ? "An " : "A ") + name;

        isWeapon = false;
        movedFromStart = false;

        setStrings();

        altLocations = new ArrayList<Location>();
        altLocations.add(location);
        altNames = new ArrayList<String>();
        inventoryID = Location.NULL_INVENTORY;
        inventory = new ArrayList<Item>();
    
    }


    public void setStrings()
    {
        initialPresenceString = "";
        presenceString = "There is " + articleName + " here.";

        answerString = "It is hardly likely that the " + name + " is interested.";
        blowString = "You can't blow that out.";
        boardString = "You have a theory on how to board " + articleName + ", perhaps?";
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
        knockString = "Why knock on " + articleName + "?";
        lightString = "You can't turn that on.";
        listenString = "The " + name + " makes no sound.";
        lookInString = "You can't look inside " + articleName + ".";
        lookOutString = "You can't look out of " + articleName + ".";
        lookUnderString = "There is nothing but dust there.";
        moveString = "Moving the " + name + " reveals nothing.";
        openString = "You must tell me how to do that to " + articleName + ".";
        pourString = "";    // game treats this as "drop"
        pullString = "";    // game treats this as "move"
        readString = "You can't read that!";
        searchString = "You find nothing unusual.";
        shakeString = "Shaken.";
        smellString = "It smells like " + articleName + ".";    
        takeString = "";
        talkString = "You can't talk to the " + name + "!";
        touchString = "";
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
        cutStringInd = "The \"cutting edge\" of " + articleName + " is hardly adequate.";
        digString = "";
        digStringInd = "Digging with " + articleName + " is silly.";
        fillString = "You may know how to do that, but I don't.";
        giveString = "";
        inflateString = "";
        lockString = "";
        putString = "There's no good surface on the " + name + ".";
        throwString = "";
        tieString = "";
        turnString = "";
        unlockString = "";


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

    public Location getLocation() { return location; }

    public void getDescription(GameState state)
    {
        if (initialPresenceString.isEmpty() || movedFromStart)
            Game.output(presenceString);
        else
            Game.output(initialPresenceString);
    }

    public void actorTurn() {}

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

    public boolean isActor() { return type == ObjectType.ACTOR; }
    public boolean isContainer() { return false; }
    public boolean isItem() { return type == ObjectType.ITEM; }
    public boolean isFeature() { return type == ObjectType.FEATURE; }
    public boolean isSurface() { return type == ObjectType.SURFACE; }
    public boolean playerHasObject() { return location == Location.PLAYER_INVENTORY; }

    // Common methods to all objects, which will be overridden in the child classes.

    
    // Direct actions
    public void answer(GameState state) { Game.output(answerString); }
    public void blow(GameState state) { Game.output(blowString); }
    public void board(GameState state) { Game.output(boardString); }
    public void climb(GameState state) { Game.output(climbString); }
    public void count(GameState state) { Game.output(countString); }
    public void cross(GameState state) { Game.output(crossString); }    
    public void deflate(GameState state) { Game.output(deflateString); }
    public void drink(GameState state) { Game.output(drinkString); }
    public void eat(GameState state) { Game.output(eatString); }
    public void enter(GameState state)
    {
        if (isItem())
            Game.output(enterItemString);
        else 
            Game.output(enterString);
    }
    public void examine(GameState state) { Game.output(examineString); }
    public void extinguish(GameState state) { Game.output(extinguishString); }   
    public void follow(GameState state) { Game.output(followString); }  
    public void kick(GameState state) { Game.output(kickString + randPhrase()); }
    public void knock(GameState state) { Game.output(knockString); }
    public void light(GameState state) { Game.output(lightString); }
    public void listen(GameState state) { Game.output(listenString); }
    public void lookIn(GameState state) { Game.output(lookInString); }
    public void lookOut(GameState state) { Game.output(lookOutString); }
    public void lookUnder(GameState state) { Game.output(lookUnderString); }
    public void lower(GameState state) { Game.output(lowerString + randPhrase()); }
    public void move(GameState state) { Game.output(moveString); }
    public void moveItem(GameState state) { Game.output(moveItemString); }
    public void open(GameState state) { Game.output("You can't open that."); }
    public void pour(GameState state) { Game.output(pourString); }
    public void pull(GameState state) { Game.output(pullString); }
    public void push(GameState state) { Game.output(pushString + randPhrase()); }    
    public void raise(GameState state) { Game.output(raiseString + randPhrase()); }
    public void read(GameState state) { Game.output(readString); }
    public void search(GameState state) { Game.output(searchString); }
    public void shake(GameState state) { Game.output(shakeString); }
    public void smell(GameState state) { Game.output(smellString); }   
    public void take(GameState state)
    {
        if (takeString.isEmpty())
            Game.output(GameStrings.getSarcasticResponse());
        else
            Game.output(takeString);
    }
    public void talk(GameState state) { Game.output(talkString); }   
    public void touch(GameState state) { Game.output(touchString + randPhrase()); }
    public void wake(GameState state) { Game.output(wakeString); }
    public void wave(GameState state) { Game.output(waveString + randPhrase()); }
    public void wear(GameState state) { Game.output(wearString); }
    public void wind(GameState state) { Game.output(windString); }


    // Indirect actions
    public void attack(GameState state) { Game.output(attackString); }
    public void breakObject(GameState state)
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
    public void burn(GameState state) { Game.output(burnString); }
    public void close(GameState state) { Game.output("You can't close that."); }
    public void cut(GameState state) { Game.output(cutString); }
    public void dig(GameState state)
    {
        Game.output("Digging with " + state.indirectObject.articleName + " is silly.");
    }
    public void fill(GameState state) { Game.output(fillString); }
    public void give(GameState state) { Game.output(giveString); }
    public void inflate(GameState state) { Game.output(inflateString); }
    public void lock(GameState state) { Game.output("You can't lock that."); }
    public void place(GameState state, Item it) {}

    public void put(GameState state) { Game.output(putString); }

    public void remove(GameState state) {}
    public void throwObject(GameState state) { Game.output(throwString); }
    public void tie(GameState state) { Game.output(tieString); }
    public void turn(GameState state) { Game.output(turnString); }
    public void unlock(GameState state) { Game.output("You can't unlock that."); }
    public void drop(GameState state) { Game.output("You can't drop that."); }

    public String randPhrase()
    {
        Random rand = new Random();

        int i = rand.nextInt(noEffect.length);

        return noEffect[i];


    }




    public boolean isOpen() { return false; }
    public boolean isAlive() { return false; }
    public void tick() {}
    public String toString() { return name; }


    

}