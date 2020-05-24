public class InputParser {

	private GameState state;
	private String input;
	private String[] inputWords;
	private String parseActionString;
	private String parseDirectString;
	private String parseIndirectString;
	
	public InputParser (GameState st)
	{
		state = st;
		input = state.completePlayerInput;
		inputWords = input.split(" ");
		parseActionString = "";
		parseDirectString = "";
		parseIndirectString = "";
	}

	/**
	 * Returns true if the player inputs a phrase which is fully recognized by the game.
	 * If not, player is re-prompted and no turn is taken.
	 */
	public boolean parsePlayerInput()
	{
		
		for (int i = 0; i < inputWords.length; ++i)
		{
			if (!isGameWord(inputWords[i]))
			{
				Game.output("I don't know what \"" + inputWords[i] + "\" means.");
				return false;
			}
		}

		if (!parseInputAction())
			return false;

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
					return false;
				}
			} break;

			case DIRECT:
			{
				// If the player entered a direct action without an object,
				// they will be prompted to provide the object.

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
			{

				parseDirectString = input;
				if (!parseDirectObject())
					return false;

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

			} break;

			default: {} break;
		}



		return true;
	}

	public boolean parseInputAction()
	{
		parseActionString = input;

		// Compare the beginning of the input to the set of action phrases.
		boolean check = false;
		for (String token : state.actions.keySet())
		{
			
			if (startsWith(token, input))
			{
				check = true;
                state.firstInputPhrase = token;
                state.playerAction = state.actions.get(token);
        		state.playerActionType = state.actionTypes.get(state.playerAction);
        		input = input.substring(token.length()).trim();
        		break;
			}
		}

		// If no action was found
		if (!check)
			Game.output("Sentence did not start with an action!");

		return check;
	}


	public boolean parseDirectObject()
	{
		boolean check = false;

		for (String token : state.objectList.keySet())
		{
			if (startsWith(token, input))
			{
				check = true;
				state.secondInputPhrase = token;
				state.directObject = state.objectList.get(token);
				input = input.substring(token.length()).trim();
				break;
			}
		}

		// If the user enters a valid action, but an invalid object.
		if (!check)
		{
			Game.output("You used the phrase \"" + input
				+ "\" in a way I don't understand.");
		}

		return check;
	}


	public boolean parseIndirectObject()
	{
		boolean check = false;

		for (String token : state.objectList.keySet())
		{
			if (startsWith(token, input))
			{
				check = true;
				state.indirectObject = state.objectList.get(token);
				input = input.substring(token.length()).trim();
				break;
			}
		}

		// If the user enters a valid action, but an invalid object.
		if (!check)
		{
			Game.output("You used the phrase \"" + input
				+ "\" in a way I don't understand.");
		}

		return check;


	}
	
	
	public boolean validateAction()
	{
		
		return true;
	}
	
	public void inputTest()
	{
		Game.output("Complete player input: " + state.completePlayerInput);
		// Game.output("Input words: ");
		// for (int i = 0; i < inputWords.length; ++i)
		// 	Game.output(inputWords[i] + " ");
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
	
}
