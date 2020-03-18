import java.util.ArrayList;

interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public Location location;

    public ObjectType type;
    public String articleName;
    public String capArticleName;
    public boolean visible;
    public boolean movedFromStart;
    public String initialPresenceString;
    public String presenceString;

    // responses to player actions, mostly taking the place of the lambda methods.

    public String answerString;
    public String attackString;
    public String blowString;
    public String boardString;
    public String breakString;
    public String burnString;
    public String climbString;
    public String closeString;
    public String countString;
    public String crossString;
    public String cutString;
    public String deflateString;
    public String digString;
    public String drinkString;
    public String eatString;
    public String enterString;
    public String examineString;
    public String extinguishString;
    public String fillString;
    public String followString;
    public String giveString;
    public String inflateString;
    public String kickString;
    public String knockString;
    public String lightString;
    public String listenString;
    public String lockString;
    public String lowerString;
    public String moveString;
    public String openString;
    public String pourString;
    public String pullString;
    public String pushString;
    public String putString;
    public String raiseString;
    public String readString;
    public String searchString;
    public String shakeString;
    public String smellString;    
    public String takeString;
    public String throwString;
    public String tieString;
    public String touchString;
    public String turnString;
    public String unlockString;
    public String wakeString;
    public String waveString;
    public String wearString;
    public String windString;
    
    public ActivateMethod method;
    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;
    public ArrayList<String> altNames;


    // Constructors
    public GameObject(String nm, Location loc)
    {
        name = nm;
        location = loc;

        articleName = (vowelStart() ? "an " : "a ") + name;
        capArticleName = (vowelStart() ? "An " : "A ") + name;
        visible = true;

        setStrings();
        

        method = (GameState state, Action act) -> {};
    }

    public void setMethod(ActivateMethod am)
    {
        method = am;
    }

    public void setStrings()
    {
        initialPresenceString = "";
        presenceString = "There is " + articleName + " here.";

        answerString = "It is hardly likely that the " + name + " is interested.";
        attackString = "I've known strange people, but fighting " + articleName + "?";
        blowString = "";
        boardString = "You have a theory on how to board " + articleName + ", perhaps?";
        breakString = "";
        burnString = "";
        climbString = "You can't do that!";
        closeString = "You must tell me how to do that to " + articleName + ".";
        countString = "You have lost your mind.";
        crossString = "You can't cross that!";
        cutString = "Strange concept, cutting the " + name + "...";
        deflateString = "Come on, now!";
        digString = "";
        drinkString = "I don't think that the " + name + " would agree with you.";
        eatString = "I don't think that the " + name + " would agree with you.";
        enterString = "You hit your head against the " + name + " as you attempt this feat.";
        enterItemString = "That would involve quite a contortion!";
        examineString = "There's nothing special about the " + name + ".";
        extinguishString = "";
        fillString = "";
        followString = "";
        giveString = "";
        inflateString = "";
        kickString = "";
        knockString = "";
        lightString = "";
        listenString = "";
        lockString = "";
        lowerString = "";
        moveString = "";
        openString = "";
        pourString = "";
        pullString = "";
        pushString = "";
        putString = "";
        raiseString = "";
        readString = "You can't read that!";
        searchString = "";
        shakeString = "";
        smellString = "";    
        takeString = "That's not something you can take, really.";
        throwString = "";
        tieString = "";
        touchString = "";
        turnString = "";
        unlockString = "";
        wakeString = "";
        waveString = "";
        wearString = "";
        windString = "";

    }

    public Location getLocation() { return location; }

    public void activate(GameState state, Action act)
    {
        method.run(state, act);
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
    public boolean isContainer() { return type == ObjectType.CONTAINER; }
    public boolean isItem() { return type == ObjectType.ITEM; }
    public boolean isFeature() { return type == ObjectType.FEATURE; }
    public boolean isSurface() { return type == ObjectType.SURFACE; }
    public boolean playerHasObject() { return location == Location.PLAYER_INVENTORY; }

    // Common methods to all objects, which will be overridden in the child classes.

    public void open(GameState state) { Game.output("You can't open that."); }
    public void close(GameState state) { Game.output("You can't close that."); }
    public void unlock(GameState state) { Game.output("You can't unlock that."); }
    public void lock(GameState state) { Game.output("You can't lock that."); }

    public void breakObject(GameState state)
    {
        Game.output("Trying to destroy " + articleName + " with "
        + state.indirectObject.articleName + " is futile.");
    }

    public void dig(GameState state)
    {
        Game.output("Digging with " + state.indirectObject.articleName + " is silly.");
    }

    public void take(GameState state) { Game.output(takeString); }
    public void drop(GameState state) { Game.output("You can't drop that."); }
    public void place(GameState state, Item it) { Game.output("You can't place that."); }
    public void remove(GameState state, Item it) { Game.output("You can't do that."); }
    public void read(GameState state) { Game.output(readString); }
    public void board(GameState state) { Game.output(boardString); }
    public void tick() {}

    public void examine(GameState state) { Game.output(examineString); }

    public boolean isOpen() { return false; }
    public boolean isAlive() { return false; }
    public boolean isVisible() { return visible; }


    

}