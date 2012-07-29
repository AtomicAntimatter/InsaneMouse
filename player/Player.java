package player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class Player 
{
    private static final int IMMUNITY_LIFETIME = 2000;
    protected int lives = 3;
    protected boolean isDead, hasImmunity;
    protected long lastImmunity;
    protected int[] loc = new int[2];

    public int[] getLoc() 
    {
        return loc;
    }

    public int getLives() 
    {
        return lives;
    }
       
    public void setImmunity(boolean _hasImmunity)
    {
        hasImmunity = _hasImmunity;
        lastImmunity = System.currentTimeMillis();
    }
    
    public boolean getImmunity()
    {
        if((lastImmunity+IMMUNITY_LIFETIME) < System.currentTimeMillis())
        {
            hasImmunity = false;       
        }
        return hasImmunity;
    }

    public void decLives() 
    {
        lives--;
		if(lives == 0)
		{
			isDead = true;
		}
    }

    public boolean isDead() 
    {
        return isDead;
    }
	
    public abstract void logic();

    public void render(Graphics g) 
    {
        g.setColor(Color.white);
        g.fillOval(loc[0] - 5, loc[1] - 5, 10, 10);
    }
}
