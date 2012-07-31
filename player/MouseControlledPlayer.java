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
	public void subLogic()
	{
		loc[0] = Mouse.getX();
		loc[1] = height - Mouse.getY();
		
		if(Mouse.isButtonDown(0))
		{
			s.detonateLocal(loc);
		}
	}
}
