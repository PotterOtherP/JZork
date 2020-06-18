public class InputParseTest {
	
	public static void main(String[] args)
	{
		String[] testPhrases = {

			
			"attack house sword"

		};

		GameState gs = new GameState();

		new GameSetup(gs, true, true);

		Game.gameState = gs;
		gs.directObject = gs.objectList.get("troll");
		gs.indirectObject = gs.objectList.get("nasty knife");

		String test = "The OBJECT swings his axe and sends your ITEM flying.";

		Game.output(test);




	}
}