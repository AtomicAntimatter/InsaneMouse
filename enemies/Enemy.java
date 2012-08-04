package enemies;

import java.util.LinkedList;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import util.Interval;
import util.Interval2D;

public abstract class Enemy 
{
	protected float[] loc = new float[2];
	protected static final int[] BORDER = {0,0,Display.getWidth(),Display.getHeight()};
	protected boolean isDead;
	protected boolean bossMinion;
	public int size = 10;
	private final long creationTime = System.currentTimeMillis();
	private final long breatheTime = 2000;
	protected Color c = Color.white;
	protected Vector2f v = new Vector2f(0,0);
	
	public Enemy(int[] _loc) 
	{
		loc[0] = _loc[0];
		loc[1] = _loc[1];
	}
	
	public int[] getLoc() 
	{
		return new int[]{(int)loc[0], (int)loc[1]};
	}
	
	public void logic(int delta)
	{
		if(insanity.Game.rejects.contains(this)){return;}
		
		if(creationTime+breatheTime<System.currentTimeMillis())
		{
			Interval<Integer> intX = new Interval<Integer>((int)loc[0]-1, (int)loc[0]+1);
			Interval<Integer> intY = new Interval<Integer>((int)loc[1]-1, (int)loc[1]+1);
			Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
			LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, this.getClass());
			LinkedList<Enemy> lm = insanity.Game.qa.query2D(rect, EnemyTypes.Boss.class);
			l.removeAll(lm);
			if(!l.isEmpty())
			{
				if(l.contains(this))
				{
					l.remove(this);
				}	
				insanity.Game.rejects.addAll(l);
			}

			if(EnemyTypes.Boss.class.isInstance(this))
			{
				l.clear();
				l = insanity.Game.qa.query2D(rect, Enemy.class);
				if(!l.isEmpty())
				{
					if(l.contains(this))
					{
						l.remove(this);
					}
					
					for(int i = 0; i < l.size(); i++)
					{
						if(!l.get(i).bossMinion)
						{
							this.size = Math.min(10+this.size, 100);
							EnemyManager.boss.health = Math.min(EnemyManager.boss.health+1, EnemyManager.boss.MAXHEALTH);
						}
					}
			
					insanity.Game.rejects.addAll(l);
				}
			}
		}
		if(!EnemyManager.bossActive||bossMinion
			||creationTime+breatheTime>System.currentTimeMillis())
		{
			subLogic(delta);
		}
		else
		{
			minionLogic(delta);
		}

		if(!isDead)
		{
			insanity.Game.qb.insert((int)loc[0],(int)loc[1], this);
		}
	}
	
	public void minionLogic(int delta)
	{	
		Vector2f a = new Vector2f(EnemyManager.boss.loc[0] - loc[0], EnemyManager.boss.loc[1] - loc[1]);
		v.add(a);
		v.scale(1/v.length());

		loc[0] += v.x*delta;
		loc[1] += v.y*delta;
	}
	
	protected abstract void subLogic(int delta);
	
	protected int distanceFrom(int[] mloc)
	{
		int p1 = (int)loc[0]-mloc[0];
		int p2 = (int)loc[1]-mloc[1];
		return (int)Math.pow(p2, 2) + (int)Math.pow(p1, 2);
	}
	
	public void render(Graphics g) 
	{
		g.setColor(c);
		g.fillOval(loc[0]-size/2, loc[1]-size/2, size, size);
	}
}