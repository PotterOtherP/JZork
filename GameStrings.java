import java.util.Random;

class GameStrings {

    // This class does not get instantiated.
    private GameStrings() {}
    
    public static final String COMBAT_MISS_1 = "Your WEAPON misses the ENEMY by an inch.";
    public static final String COMBAT_MISS_2 = "A good slash, but it misses the ENEMY by a mile.";
    public static final String COMBAT_MISS_3 = "You charge, but the ENEMY jumps nimbly aside.";
    public static final String COMBAT_PARRY_1 = "Clang! Crash! The ENEMY parries.";
    public static final String COMBAT_PARRY_2 = "A quick stroke, but the ENEMY is on guard.";
    public static final String COMBAT_PARRY_3 = "A good stroke, but it's too slow; the ENEMY dodges.";
    public static final String COMBAT_KNOCKOUT_1 = "Your WEAPON crashes down, knocking the ENEMY into dreamland.";
    public static final String COMBAT_KNOCKOUT_2 = "The ENEMY is battered into unconsciousness.";
    public static final String COMBAT_KNOCKOUT_3 = "A furious exchange, and the ENEMY is knocked out!";
    public static final String COMBAT_KNOCKOUT_4 = "The haft of your WEAPON knocks out the ENEMY.";
    public static final String COMBAT_FATAL_1 = "It's curtains for the ENEMY as your WEAPON removes his head.";
    public static final String COMBAT_FATAL_2 = "The fatal blow strikes the ENEMY square in the heart: He dies.";
    public static final String COMBAT_FATAL_3 = "The ENEMY takes a fatal blow and slumps to the floor dead.";
    public static final String COMBAT_LIGHT_1 = "The ENEMY is struck on the arm; blood begins to trickle down.";
    public static final String COMBAT_LIGHT_2 = "The WEAPON pinks the ENEMY on the wrist, but it's not serious.";
    public static final String COMBAT_LIGHT_3 = "Your stroke lands, but it was only the flat of the blade.";
    public static final String COMBAT_LIGHT_4 = "The blow lands, making a shallow gash in the ENEMY's arm!";
    public static final String COMBAT_SEVERE_1 = "The ENEMY receives a deep gash in his side.";
    public static final String COMBAT_SEVERE_2 = "A savage blow on the thigh! The ENEMY is stunned but can still fight!";
    public static final String COMBAT_SEVERE_3 = "Slash! Your blow lands! That one hit an artery, it could be serious!";
    public static final String COMBAT_SEVERE_4 = "Slash! Your stroke connects! This could be serious!";
    public static final String COMBAT_STAGGER_1 = "The ENEMY is staggered, and drops to his knees.";
    public static final String COMBAT_STAGGER_2 = "The ENEMY is momentarily disoriented and can't fight back.";
    public static final String COMBAT_STAGGER_3 = "The force of your blow knocks the ENEMY back, stunned.";
    public static final String COMBAT_STAGGER_4 = "The ENEMY is confused, and can't fight back.";
    public static final String COMBAT_DISARM_1 = "The quickness of your thrust knocks the ENEMY's weapon to the floor, leaving him unarmed.";
    public static final String COMBAT_DISARM_2 = "The ENEMY is disarmed by a subtle feint past his guard.";
    public static final String COMBAT_FINISH_DISARMED = "The unarmed ENEMY cannot defend himself: He dies.";
    public static final String COMBAT_FINISH_UNCONSCIOUS = "The unconscious ENEMY cannot defend himself: He dies.";

    public static final String COMBAT_ENEMY_DIES = "Almost as soon as the ENEMY breathes his last breath, a cloud of sinister black fog "
        + "envelops him, and when the fog lifts, the carcass has disappeared.";
    public static final String COMBAT_HP_ZERO = "It appears that last blow was too much for you. I'm afraid you are dead.";
    public static final String COMBAT_STAGGERED = "You are still recovering from that last blow, so your attack is ineffective.";


    public static final String GAME_BEGIN = "ZORK I: The Great Underground Empire"
        + "\nCopyright (c) 1981, 1982, 1983 Infocom, Inc. All rights reserved."
        + "\n ZORK is a registered trademark of Infocom, Inc."
        + "\n Revision 88 / Serial number 840726";

    public static final String ALL_TREASURES_IN_CASE = "An almost inaudible voice whispers in your ear, \"Look to your treasures for the "
        + "final secret.";

    public static final String AUTHOR_INFO = "This sad attempt to recreate Zork was written by Nate Tryon "
        + "in the year of are Lord 2020.\n\nhttps://github.com/PotterOtherP/JZork";
    public static final String BLACK_BOOK_TEXT = "Commandment #12592\n\n"
        + "Oh ye who go about saying unto each: \"Hello sailor\":\n"
        + "Dost thou know the magnitude of thy sin before the gods?\n"
        + "Yea, verily, thou shalt be ground between two stones.\n"
        + "Shall the angry gods cast thy body into the whirlpool?\n"
        + "Surely, thy eye shall be put out with a sharp stick!\n"
        + "Even unto the ends of the earth shalt thou wander and\n"
        + "Unto the land of the dead shalt thou be sent at last.\n"
        + "Surely thou shalt repent of thy cunning.";
    public static final String BOAT_LABEL_TEXT = "         !!!!  FROBOZZ MAGIC BOAT COMPANY  !!!!\n\n"
        + "Hello, Sailor!\n\n"
        + "Instructions for use:\n\n"
        + "   To get into a body of water, say \"Launch\".\n"
        + "   To get to shore, say \"Land\" or the direction in which you want to maneuver the boat.\n\n"
        + "Warranty:\n\n"
        + "  This boat is guaranteed against all defects for a period of 76 milliseconds from date of "
        + "purchase or until first used, whichever comes first.\n\n"
        + "Warning:\n"
        + "   This boat is made of thin plastic.\n"
        + "   Good Luck!";
    public static final String CANT_GO = "You can't go that way.";
    public static final String DAM_GUIDEBOOK_TEXT = "       Flood Control Dam #3\n\n"
        + "FCD#3 was constructed in the year 783 of the Great Underground Empire to harness the mighty Frigid River. This work was supported by "
        + "a grant of 37 million zorkmids from your omnipotent local tyrant Lord Dimwit Flathead the Excessive. This impressive structure is "
        + "composed of 370,000 cubic feet of concrete, is 256 feet tall at the center, and 193 feet wide at the top. The lake created behind the "
        + "dam has a volume of 1.7 billion cubic feet, an area of 12 million square feet, and a shore line of 36 thousand feet.\n\n"
        + "We will now point out soem of the more interesting features of FCD#3 as we conduct you on a guided tour of the facilities:\n"
        + "        1) You start your tour here in the Dam Lobby. You will notice on your right that...\n";
    public static final String DARKNESS = "It is pitch black. You are likely to be eaten by a grue.";
    public static final String DARKNESS_LISTEN = "There are sinister gurgling noises in the darkness all around you!";
    public static final String DEAD_ACTION_FAIL = "You can't even do that.";
    public static final String DEAD_CANNOT_ENTER = "You cannot enter in your condition.";
    public static final String DEAD_DIAGNOSE = "You are dead.";
    public static final String DEAD_DOME_PASSAGE = "As you enter the dome you feel a strong pull as if from a wind drawing "
        + "you over the railing and down.";
    public static final String DEAD_INVENTORY = "You have no possessions.";
    public static final String DEAD_LOOK = "The room looks strage and unearthly and objects appear indistinct."
        + "\nAlthough there is no light, the room seems dimly illuminated.";
    public static final String DEAD_PRAY_ALTAR = "From the distance the sound of a lone trumpet is heard. The room becomes "
        + "very bright and you feel disembodied. In a moment, the brightness fades and you find yourself rising as if from "
        + "a long sleep, deep in the woods. In the distance you can faintly hear a songbird and the sounds of the forest.";
    public static final String DEAD_PRAY_FAIL = "Your prayers are not heard.";
    public static final String DEAD_SCORE = "You're dead! How can you think of your score?";
    public static final String DEAD_TAKE_OBJECT = "Your hand passes through its object.";
    public static final String DEAD_TOUCH = "Even such an action is beyond your capabilities.";
    public static final String DEAD_WAIT = "Might as well. You've got an eternity.";
    public static final String ENGRAVINGS_TEXT = "The engravings were incised in the living rock of the cave wall by an unknown hand. They depict, "
        + "in symbolic form, the beliefs of the ancient Zorkers. Skillfully interwoven with the bas reliefs are excerpts illustrating the major "
        + "religious tenets of that time. Unfortunately, a later age seems to have considered them blasphemous and just as skillfully excised them.";
    public static final String ENTER_DARKNESS = "You have moved into a dark place.";
    public static final String GAS_EXPLOSION = "Oh dear. It appears that the smell coming from this room was coal gas. I would have "
        + "thought twice about carrying flaming objects in here.\n\n      ** BOOOOOOOOOOOOM **";
    public static final String GRUE_DEATH_1 = "Oh no! You have walked into the slavering fangs of a lurking grue!";
    public static final String GRUE_DEATH_2 = "Oh no! A lurking grue slithered into the room and devoured you!";
    public static final String HOLLOW_VOICE = "A hollow voice says \"Fool.\"";
    public static final String LEAFLET_TEXT = "WELCOME TO ZORK!\n\nZORK is a game of adventure, danger, and low cunning. In it you will explore "
        + "some of the most amazing territory ever seen by mortals. No computer should be without one!";
    public static final String MOVE_RUG = "With a great effort, the rug is moved to one side of the room, revealing the dusty cover "
        + "of a closed trap door.";
    public static final String NATE_MANUAL_TEXT = "\nCongratulations!\n\nYou are the privileged owner of a shoddy facsimile of ZORK I: "
        + "The Great Underground Empire, a legendary self-contained and self-maintaining universe created in the late 1970's by some "
        + "computer geniuses at MIT. If used and maintained in accordance with normal operating practices for small universes, this pale "
        + "imitation of ZORK I will provide many months of troubled and bug-ridden operation, including bizarre logical errors and "
        + "countless thrown exceptions.";   
    public static final String OVERBURDENED = "You can't carry any more.";
    public static final String PASSAGE_OVERBURDENED = "You are carrying too much.";
    public static final String PLAYER_DIES = "\n   ****  You have died  ****\n\n"
        + "Now, let's take a look here... Well, you probably deserve another chance. I can't quite fix you up "
        + "completely, but you can't have everything.";
    public static final String PLAYER_DIES_FOR_REAL = "\n   ****  You have died  ****\n\n"
        + "As you take your last breath, you feel relieved of your burdens. "
        + "The feeling passes as you find yourself before the gates of Hell, where the spirits jeer at you and deny you "
        + "entry. Your senses are disturbed. The objects in the dungeon appear indistinct, bleached of color, even unreal.";
    public static final String PLAYER_DIES_SUICIDE = "You clearly are a suicidal maniac. We don't allow psychotics in the cave, "
        + "since they may harm other adventurers. Your remains will be installed in the Land of the Living Dead, where your "
        + "fellow adventurers may gloat over them.";
    public static final String PLAYER_DIES_WHILE_DEAD = "It takes a talented person to be killed while already dead. YOU are "
        + "such a talent. Unfortunately, it takes a talented person to deal with it. I am not such a talent. Sorry.";
    public static final String PROFANITY_ONE = "Such language in a high-class establishment like this!";
    public static final String PROFANITY_TWO = "Do you have to use so many cuss words?";
    public static final String PROFANITY_THREE = "There's no need for that kind of language.";
    public static final String RUG_ALREADY_MOVED = "Having moved the carpet previously, you find it impossible to move it again.";
    public static final String SAILOR = "Nothing happens here.";
    public static final String TOO_DARK = "It's too dark to see!";
    public static final String TRAP_DOOR_OPENS = "The door reluctantly opens to reveal a rickety staircase descending into darkness.";
    public static final String WINDOW_CLOSES = "The window closes (more easily than it opened).";
    public static final String WINDOW_OPENS = "With great effort, you open the window far enough to allow entry.";
    public static final String ZORK_MANUAL_TEXT = "Congratulations!\n\nYou are the privileged owner of ZORK I: The Great Underground Empire, a "
        + "self-contained and self-maintaining universe. If used and maintained in accordance with normal operating practices for small "
        + "universes, ZORK will provide many months of trouble-free operation.";


    public static final String[] SARCASM = { "What a concept!", "You can't be serious.", "A valiant attempt.", "An interesting idea..." };

    public static final String[] JUMP_SARCASM = { "Very good. Now you can go to the second grade.", "Wheeeeeeeeee!!!!!",
        "Do you expect me to applaud?", "Are you enjoying yourself?" };

    public static final String[] HARD_SARCASM = { "Look around.", "Too late for that.", "Have your eyes checked." };

    public static String getSarcasticResponse()
    {
        Random rand = new Random();

        int i = rand.nextInt(SARCASM.length);

        return SARCASM[i];
    }

    public static String getJumpSarcasm()
    {
        Random rand = new Random();

        int i = rand.nextInt(JUMP_SARCASM.length);

        return JUMP_SARCASM[i];
    }

    public static String getHardSarcasm()
    {
        Random rand = new Random();

        int i = rand.nextInt(HARD_SARCASM.length);

        return HARD_SARCASM[i];
    }





    // All the words recognized by the game.
    public static final String[] GAME_WORDS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
    "u", "v", "w", "x", "y", "z", "ne", "nw", "se", "sw",
    "again", "an", "around", "at", "attach", "attack", "author",
    "bar", "bell", "bird", "bottle", "box", "bug",
    "carpet", "case", "close",
    "door", "down", "drop",
    "east", "egg", "examine", "exit",
    "five", "fuck",
    "giant", "go",
    "hand", "high", "highfive", "hit", "house",
    "in", "inn", "inside", "inventory",
    "jump", "juniper",
    "key", "kick", "kitchen", "knife",
    "lantern", "leaflet", "light", "lock", "look",
    "mailbox", "move",
    "nest", "next", "north", "northeast", "northwest", "note",
    "odysseus", "off", "on", "open", "out",
    "passage", "piano", "pick", "pile", "place", "put", "play", "please", "pull", "punch",
    "quit",
    "read", "ring", "room", "rope",
    "sack", "say", "scream", "shit", "shout", "slap", "songbird", "south", "southeast", "southwest", "store", "sword",
    "take", "the", "tie", "to", "trap", "trophy", "turn",
    "ulysses", "unlock", "up",
    "wait", "walk", "west", "window", "with", "wizard",
    "yell"
    };

    public static final String[] GODMODE_WORDS = {

    "accio",
    "teleport",
    "zombie"

    };

    public static final String[] PROFANITY = {

    " fuck ", " shit "

    };

    public static final String[] SLURS = {

    };



}
