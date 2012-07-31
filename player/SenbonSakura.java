package player;

import enemies.Enemy;
import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import util.Interval;
import util.Interval2D;

public class SenbonSakura
{
    private static final int 
            SBSK_R = 150,
			SBSK_S = SBSK_R*SBSK_R,
            SBSK_T = 5000;
	
	private int[] loc = {0,0};
    private long senbonSakuraT = 0;
	private int senbonSakuraC = 0;
	private boolean detonate, detonateLocal;
    
    public void detonateLocal(int[] _loc)
    {     
        if(senbonSakuraT + SBSK_T < System.currentTimeMillis()) 
        {
			loc = _loc;
            senbonSakuraC = 40;
            senbonSakuraT = System.currentTimeMillis();
            detonateLocal = true;
			detonate = true;
        }
    }
	
	private int distanceFrom(int[] mloc)
	{
		int p1 = loc[0]-mloc[0];
		int p2 = loc[1]-mloc[1];
		return (int)Math.pow(p2, 2) + (int)Math.pow(p1, 2);
	}
    
    public void logic()
    {
		if(detonate)
		{
			Interval<Integer> intX = new Interval<Integer>((int)loc[0]-SBSK_R, (int)loc[0]+SBSK_R);
			Interval<Integer> intY = new Interval<Integer>((int)loc[1]-SBSK_R, (int)loc[1]+SBSK_R);
			Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
			LinkedList<Enemy> l = insanity.Game.qa.query2D(rect, Enemy.class); 
			if(!l.isEmpty())
			{
				for(int i = 0; i < l.size(); i++)
				{
					Enemy e = l.get(i);
					if(distanceFrom(e.getLoc()) < SBSK_S)
					{	
						insanity.Game.rejects.add(e);
					}
				}		
			}
			detonate = false;
		}
    }
    
    public void render(Graphics g) 
    { 
        if(detonateLocal)
        {
			g.setColor(Color.pink);
            if(senbonSakuraC-- > 0) 
            {
                g.fillOval(loc[0]-SBSK_R, loc[1]-SBSK_R, 2*SBSK_R, 2*SBSK_R);
            }
            else
            {
                detonateLocal = false;
            }
        }
    }
}