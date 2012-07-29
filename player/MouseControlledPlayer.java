package player;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseControlledPlayer extends Player
{	
	private static final int height = Display.getHeight();
	
	public MouseControlledPlayer()
	{
		loc[0] = Mouse.getX();
		loc[1] = height - Mouse.getY();
	}
	
	@Override
	public void logic()
	{
		if(!isDead)
		{
			loc[0] = Mouse.getX();
			loc[1] = height - Mouse.getY();
		}
	}
}
