package player;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Input;

public class MouseControlledPlayer extends Player
{	
	private static final int height = Display.getHeight();
	
	public MouseControlledPlayer(String name, int playerNum)
	{
		super(name, playerNum);
		loc[0] = Mouse.getX();
		loc[1] = height - Mouse.getY();
	}
	
	@Override
	public void subLogic(Input m)
	{
		loc[0] = Mouse.getX();
		loc[1] = height - Mouse.getY();
		
		if(m.isMousePressed(0))
		{
			s.addBomb(loc, false);
		}
		if(m.isMousePressed(1))
		{
			s.addBomb(loc, true);
		}
	}
}
