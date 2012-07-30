package insanity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Insanity extends StateBasedGame
{
	public static final int MAINMENUSTATE = 0;
	public static final int GAMEPLAYSTATE = 1;
	
	public Insanity()
	{
		super("Insanity");
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) throws Exception
    {	
		AppGameContainer app = new AppGameContainer(new Insanity());
		
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
		app.start();
    }

	@Override
	public void initStatesList(GameContainer container) throws SlickException 
	{
		addState(new Menu(MAINMENUSTATE));
		addState(new Game(GAMEPLAYSTATE));
	}
}