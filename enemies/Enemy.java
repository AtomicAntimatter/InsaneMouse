package enemies;

import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import player.Player;

public abstract class Enemy 
{
	protected float[] loc = new float[2];
	
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
	
	public abstract void logic(HashSet<Player> pl, int delta);
	
	protected int distanceFrom(int[] mloc)
	{
		int p1 = (int)(loc[0]+5)-mloc[0];
		int p2 = (int)(loc[1]+5)-mloc[1];
		return (int)Math.pow(p2, 2) + (int)Math.pow(p1, 2);
	}
	
	public boolean collidesWith(int[] mloc) 
	{
		return distanceFrom(mloc) < 25;
	}
	
	public void render(Graphics g) 
	{
		g.setColor(this.getColor());
		g.fillOval(loc[0], loc[1], 10, 10);
	}
	
	public boolean isMortal() 
	{
		return false;
	}
}