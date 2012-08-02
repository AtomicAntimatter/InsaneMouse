package player;

import enemies.Enemy;
import enemies.EnemyManager;
import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import player.SenbonSakura.SBSK_Timer;
import util.Interval;
import util.Interval2D;

public abstract class Player 
{
    private static final int IMMUNITY_LIFETIME = 2000;
    private int lives = 10;
    private boolean isDead, isImmune;
    private long lastImmunity = 0;
    protected int[] loc = new int[2];
	protected SenbonSakura s = new SenbonSakura();
	private PlayerInfo pi;
	
	public Player(final String name, final int playerNum)
	{
		pi = new PlayerInfo(name, playerNum);
		PlayerHUD.pInfo.add(pi);
	}
	
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
	
	public void logic(Input m)
	{
		if(isDead){return;}
		
		if(!isImmune())
		{
			Interval<Integer> intX = new Interval<Integer>((int)loc[0]-5, (int)loc[0]+5);
			Interval<Integer> intY = new Interval<Integer>((int)loc[1]-5, (int)loc[1]+5);
			Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
			LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, Enemy.class); 
			if(!l.isEmpty())
			{
				if(l.contains(EnemyManager.boss))
				{
					l.remove(EnemyManager.boss);
				}
				
				lives--;	
				isImmune = true;
				lastImmunity = System.currentTimeMillis();	
				insanity.Game.rejects.addAll(l);

				if(lives == 0)
				{
					isDead = true;
				}						
			}
		}
		subLogic(m);
		s.logic();
		pi.reservoir = s.st.getReservoir();
		pi.life = lives;
	}
	
    public abstract void subLogic(Input m);

    public void render(Graphics g)
    {
		if(isDead)
		{
			g.setColor(Color.red);
			g.drawLine(loc[0] - 15, loc[1] - 15, loc[0] + 15, loc[1] + 15);
			g.drawLine(loc[0] + 15, loc[1] - 15, loc[0] - 15, loc[1] + 15);
			return;
		}

        g.setColor(Color.white);
        g.fillOval(loc[0] - 5, loc[1] - 5, 10, 10);
		
		if(isImmune)
		{
			g.setColor(Color.red);
			g.drawOval(loc[0] - 15, loc[1] - 15, 30, 30);
		}
		g.setColor(Color.cyan);
		g.drawArc(loc[0]-10, loc[1]-10, 20, 20, 0, 360*pi.reservoir/SBSK_Timer.MAX_RESERVOIR);
		s.render(g);
    }
}
