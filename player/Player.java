package player;

import enemies.Enemy;
import enemies.EnemyTypes;
import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import util.Interval;
import util.Interval2D;

public abstract class Player 
{
    private static final int IMMUNITY_LIFETIME = 2000;
    private int lives = 3;
    private boolean isDead, isImmune;
    private long lastImmunity = 0;
    protected int[] loc = new int[2];

    public int[] getLoc() 
    {
        return loc;
    }

    private boolean isImmune()
    {
        if((lastImmunity+IMMUNITY_LIFETIME) < System.currentTimeMillis())
        {
			isImmune = false;       
        }
        return isImmune;
    }
	
	public int distanceFrom(int[] mloc)
	{
		int p1 = loc[0]-mloc[0];
		int p2 = loc[1]-mloc[1];
		return (int)Math.pow(p2, 2) + (int)Math.pow(p1, 2);
	}
	
	public void logic()
	{
		if(isDead){return;}
		
		if(!isImmune())
		{
			Interval<Integer> intX = new Interval<Integer>((int)loc[0]-25, (int)loc[0]+25);
			Interval<Integer> intY = new Interval<Integer>((int)loc[1]-25, (int)loc[1]+25);
			Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
			LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, EnemyTypes.Monster.class); 
			if(!l.isEmpty())
			{
				for(int i = 0; i < l.size(); i++)
				{
					Enemy e = l.get(i);
					if(distanceFrom(e.getLoc()) < 25)
					{
						lives--;	
						isImmune = true;
						lastImmunity = System.currentTimeMillis();	
						insanity.Game.rejects.add(e);
						
						if(lives == 0)
						{
							isDead = true;
						}
						break;
					}
				}	
			}
		}
		subLogic();
	}
	
    public abstract void subLogic();

    public void render(Graphics g) 
    {
		if(isDead)
		{
			g.drawLine(loc[0] - 15, loc[1] - 15, loc[0] + 15, loc[1] + 15);
			g.drawLine(loc[0] + 15, loc[1] - 15, loc[0] - 15, loc[1] + 15);
			return;
		}
		
		g.drawString(String.valueOf(lives), 700, 20);
        g.setColor(Color.white);
        g.fillOval(loc[0] - 5, loc[1] - 5, 10, 10);
		
		if(isImmune)
		{
			g.setColor(Color.red);
			g.drawOval(loc[0] - 15, loc[1] - 15, 30, 30);
		}
    }
}
