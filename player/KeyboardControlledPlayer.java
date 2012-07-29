package player;
/*
import org.lwjgl.input.Keyboard;
import util.EnemyDeletable;

public class KeyboardControlledPlayer extends Player
{
    private final int up, left, down, right, sbskey, sbsRkey;
    private final float speed;
    
    public KeyboardControlledPlayer(int _x, int _y, int _lives, boolean _active,
                                    EnemyDeletable _parent, int _infoOffset,
                                    int _up, int _left, int _down, int _right,
                                    int _speed, int _sbskey, int _sbsRkey, int _playerNumber, int _width) 
	{
		
        super(_x, _y, _lives, _active, _parent, _infoOffset, _playerNumber, _width);
        up = _up; down = _down; left = _left; right = _right;
        speed = (float)_speed/100;
        sbskey = _sbskey; sbsRkey = _sbsRkey;
    }
	
	@Override
	public void move()
	{	
		if (Keyboard.isKeyDown(up))
		{
            x += speed;
        }
        if(Keyboard.isKeyDown(left) )
		{
            x -= speed;
        }
        if (Keyboard.isKeyDown(down)) 
		{
            y += speed;
        }
        if (Keyboard.isKeyDown(right)) 
		{
            y -= speed;
        }
        if (Keyboard.isKeyDown(sbskey)) 
		{
            senbonSakura(false);
        }
        if (Keyboard.isKeyDown(sbsRkey)) 
		{
            senbonSakura(true);
        }
	}
}
*/