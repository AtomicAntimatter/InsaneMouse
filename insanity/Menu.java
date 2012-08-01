package insanity;

import java.util.HashSet;
import java.util.Iterator;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import util.GLMenuItem;
import util.GLMenuItem.Type;

@SuppressWarnings("CallToThreadDumpStack")
public class Menu extends BasicGameState
{
	private HashSet<GLMenuItem> menuItems = new HashSet<GLMenuItem>();
	private Image[] menuIMG = new Image[5];
	private final int stateID;
	
	public Menu(final int stateID)
	{
		this.stateID = stateID;
	}
	
	public void addButton(int x, int y, int i, Type t)
	{
		glButton buttonPlay = new glButton(x, y, menuIMG[i], t);
		menuItems.add(buttonPlay);
	}
	
	@Override
	public int getID() 
	{
		return stateID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException 
	{
		menuIMG[0] = new Image("Resources/MenuIMG.png").getScaledCopy(container.getWidth(), container.getHeight());
		Image buttonIMG = new Image("Resources/ButtonIMG.png");
		menuIMG[1] = buttonIMG.getSubImage(0, 0, 240, 84);
		menuIMG[2] = buttonIMG.getSubImage(0, 82, 240, 84);
		menuIMG[3] = buttonIMG.getSubImage(0, 164, 240, 84);
		menuIMG[4] = buttonIMG.getSubImage(0, 246, 240, 84);
		
		int menuX = container.getWidth()/2 - buttonIMG.getWidth()/2;
		int menuY = container.getHeight()/2;
		
		addButton(menuX, menuY-90, 1, Type.PLAY);
		addButton(menuX, menuY, 2, Type.SETTINGS);
		addButton(menuX, menuY+90, 3, Type.HOWTO);
		addButton(menuX, menuY+180, 4, Type.QUIT);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException 
	{
		menuIMG[0].draw(0, 0);
		
		Iterator<GLMenuItem> i = menuItems.iterator();
		while(i.hasNext())
		{
			i.next().render();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException 
	{
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.exit();
		
		Input input = container.getInput();	
		int mx = input.getMouseX();
		int my = input.getMouseY();
		boolean click = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		
		Iterator<GLMenuItem> i = menuItems.iterator();
		while(i.hasNext())
		{
			GLMenuItem m = i.next();
			if(m.mouseClick(mx, my, click))
			{
				switch(m.getType()) 
				{
					case PLAY:
						game.enterState(Insanity.GAMEPLAYSTATE);
						break;
					case SETTINGS:
						break;
					case HOWTO:
						break;
					case QUIT:
						container.exit();
						break;
				}
			}
		}
	}
	
	private class glButton implements GLMenuItem
	{
		private final int x, y, x2, y2;
		private final Image i;
		private final Type b;
		
		public glButton(final int _x, final int _y, final Image _i, final Type _b)
		{
			x = _x; y = _y;
			i = _i;
			x2 = x+i.getWidth();
			y2 = y+i.getHeight();
			b = _b;
		}
		
		@Override
		public Type getType()
		{
			return b;
		}
		
		@Override
		public void render()
		{
			i.draw(x, y);
		}

		@Override
		public boolean mouseClick(final int mx, final int my, final boolean click) 
		{	
			if((mx < x2)&&(mx > x)&&(my > y)&&(my < y2))
			{			
				if(click)
				{
					return true;
				}
			}
			return false;
		}
	}
}
