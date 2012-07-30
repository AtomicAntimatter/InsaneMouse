package enemies;

import java.util.LinkedList;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import util.Interval;
import util.Interval2D;

public abstract class Enemy 
{
	protected float[] loc = new float[2];
	protected static final int[] BORDER = {0,0,Display.getWidth(),Display.getHeight()};
	protected boolean isDead;
	
	public Enemy(int[] _loc) 
	{
		loc[0] = _loc[0];
		loc[1] = _loc[1];
	}
	
	public int[] getLoc() 
	{
		return new int[]{(int)loc[0], (int)loc[1]};
	}

	public Color getColor() 
	{
            return Color.white;
	}
	
	public void logic(int delta)
	{
		if(insanity.Game.rejects.contains(this)){return;}
		
		if(!EnemyTypes.Random.class.isInstance(this))
		{
			Interval<Integer> intX = new Interval<Integer>((int)loc[0]-1, (int)loc[0]+1);
			Interval<Integer> intY = new Interval<Integer>((int)loc[1]-1, (int)loc[1]+1);
			Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
			LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, this.getClass()); 
			if(!l.isEmpty())
			{
				l.remove(this);
				insanity.Game.rejects.addAll(l);		
			}
		}
		
		subLogic(delta);
		
		if(!isDead)
		{
			insanity.Game.qb.insert((int)loc[0],(int)loc[1], this);
		}
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
		g.setColor(this.getColor());
		g.fillOval(loc[0]-5, loc[1]-5, 10, 10);
	}
}