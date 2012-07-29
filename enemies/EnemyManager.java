package enemies;

import java.util.Iterator;
import org.lwjgl.opengl.Display;
import player.Player;

public class EnemyManager
{
	private final int REFRESH_TIME = 10000, ADD_TIME = 500, FPS_MIN = 80;
	private long lastRefresh = 0, lastAdd = 0;
	private enum SpawnType{CIRCLE,CEILING,FLOOR,LEFT,RIGHT};
	
	public void start()
	{
		insanity.Game.enemies.clear();
		//newCircles(SpawnType.CIRCLE, 25, 0.000002f, 500);
	}
	
	public void manage(final int FPS)
	{
		if(FPS > FPS_MIN)
		{
			/*if(System.currentTimeMillis() - lastRefresh > REFRESH_TIME)
			{
				insanity.Game.enemies.clear();
				//newCircles(SpawnType.CIRCLE, 25, 0.000002f, 500);
				lastRefresh = System.currentTimeMillis();
			}*/
			if(System.currentTimeMillis() - lastAdd > ADD_TIME)
			{
				newCircles(SpawnType.CEILING, 10, 0.000002f, 0);
				newCircles(SpawnType.FLOOR, 10, 0.000002f, 0);
				lastAdd = System.currentTimeMillis();
			}
		}
	}
	
	private void newCircles(SpawnType s, int circleNum, float circleSpeed, int distance)
	{	
		if(circleNum <= 1) return;

		switch(s)
		{
			case CIRCLE:
				Iterator<Player> it = insanity.Game.players.iterator();
				while(it.hasNext())
				{
					Player p = it.next();
					for(int i = 0; i < circleNum/insanity.Game.players.size(); i++) 
					{
						double degree = 2*Math.PI/circleNum;
						int x = (int)(p.getLoc()[0] + distance * (float) Math.sin(degree * i));
						int y = (int)(p.getLoc()[1] + distance * (float) Math.cos(degree * i));
						int[] mloc = {x,y};
						Enemy e = new EnemyTypes.Circle(mloc, circleSpeed);
						insanity.Game.qa.insert(mloc[0], mloc[1], e);
						insanity.Game.enemies.add(e);
					}
				}
				break;
			case CEILING:
				for(int i = 0; i < circleNum; i++) 
				{
					int x = i*Display.getWidth()/(circleNum-1);
					int[] mloc = {x, 0};
					Enemy e = new EnemyTypes.Circle(mloc, circleSpeed);
					insanity.Game.qa.insert(mloc[0], mloc[1], e);
					insanity.Game.enemies.add(e);
				}
				break;
			case FLOOR:
				for(int i = 0; i < circleNum; i++) 
				{
					int x = i*Display.getWidth()/(circleNum-1);
					int y = Display.getHeight();
					int[] mloc = {x, y};
					Enemy e = new EnemyTypes.Circle(mloc, circleSpeed);
					insanity.Game.qa.insert(mloc[0], mloc[1], e);
					insanity.Game.enemies.add(e);
				}
				break;
		}
	}
}
