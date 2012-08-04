package player;

import enemies.Enemy;
import enemies.EnemyManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import util.Interval;
import util.Interval2D;

public class SenbonSakura
{
    private static final int 
            SBSK_R = 150,
            SBSK_LT = 3000,
			SBSK_RT = 2000;

	public SBSK_Timer st = new SBSK_Timer();
	private HashSet<SBSK_Bomb> bSet = new HashSet<SBSK_Bomb>();
		
	public class SBSK_Timer
	{
		public static final long MAX_RESERVOIR = 10000;
		private long currentReservoir = MAX_RESERVOIR;
		private long lastTransaction = 0;
		
		public boolean transact(long amount)
		{
			long t = System.currentTimeMillis() - lastTransaction + currentReservoir;
			currentReservoir = Math.min(t, MAX_RESERVOIR);
			
			if(currentReservoir - amount < 0)
			{
				return false;
			}
			
			lastTransaction = System.currentTimeMillis();
			currentReservoir -= amount;
			return true;
		}
		
		public long getReservoir()
		{
			long t = System.currentTimeMillis() - lastTransaction + currentReservoir;
			currentReservoir = Math.min(t, MAX_RESERVOIR);
			lastTransaction = System.currentTimeMillis();
			return currentReservoir;
		}
	}
	
	public class SBSK_Bomb
	{
		private final boolean remote;
		private final int[] loc;
		private final long startTime, delayTime;
		private final long drawTime = 100;
		private boolean removed, detonated;
		
		public SBSK_Bomb(int[] _loc, boolean _remote)
		{
			remote = _remote;
			loc = _loc;
			startTime = System.currentTimeMillis();
			
			if(remote)
			{
				delayTime = 2000;
			}
			else
			{
				delayTime = 0;
			}
		}
		
		public boolean isExplodeTime()
		{
			if(startTime + delayTime < System.currentTimeMillis())
			{	
				return true;
			}
			return false;
		}
		
		public boolean isDrawTime()
		{
			if(System.currentTimeMillis() < startTime + delayTime + drawTime)
			{
				return true;
			}
			return false;
		}
	}
	
	public void addBomb(int[] loc, boolean remote)
    {     
        if(st.transact(remote?SBSK_RT:SBSK_LT))
        {
			SBSK_Bomb b = new SBSK_Bomb(loc.clone(), remote);	
			bSet.add(b);
        }
    }
	
    public void logic()
    {
		HashSet<SBSK_Bomb> deadBombs = new HashSet<SBSK_Bomb>();
		
		Iterator<SBSK_Bomb> i = bSet.iterator();
		while(i.hasNext())
		{
			SBSK_Bomb b = i.next();
			
			if(b.detonated)
			{
				deadBombs.add(b);
				continue;
			}
			
			if(!b.removed&&b.isExplodeTime())
			{
				Interval<Integer> intX = new Interval<Integer>(b.loc[0]-SBSK_R, b.loc[0]+SBSK_R);
				Interval<Integer> intY = new Interval<Integer>(b.loc[1]-SBSK_R, b.loc[1]+SBSK_R);
				Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
				LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, Enemy.class); 
				if(!l.isEmpty())
				{
					if(l.contains(EnemyManager.boss))
					{
						EnemyManager.boss.health -= 20;
						EnemyManager.boss.size = Math.max(20, EnemyManager.boss.size-10);
						l.remove(EnemyManager.boss);
						
						if(EnemyManager.boss.health <= 0)
						{
							EnemyManager.bossActive = false;		
							insanity.Game.rejects.add(EnemyManager.boss);
						}
					}		
					insanity.Game.rejects.addAll(l);
				}
				b.removed = true;
			}
		}
		bSet.removeAll(deadBombs);
    }
    
    public void render(Graphics g) 
    { 
		g.setColor(Color.pink);
		
		Iterator<SBSK_Bomb> i = bSet.iterator();
		while(i.hasNext())
		{
			SBSK_Bomb b = i.next();
			
			if(b.removed&&b.isDrawTime())
			{
				g.fillOval(b.loc[0]-SBSK_R, b.loc[1]-SBSK_R, 2*SBSK_R, 2*SBSK_R);
			}
			else if(b.isDrawTime())
			{
				g.fillOval(b.loc[0]-5, b.loc[1]-5, 10, 10);
			}
			else
			{
				b.detonated = true;
			}
		}
    }
}