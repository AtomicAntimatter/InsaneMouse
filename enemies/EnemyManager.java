package enemies;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import player.Player;

public class EnemyManager
{
	private static final int REFRESH_TIME = 10000, ADD_TIME = 500, FPS_MIN = 80;
	private static long lastRefresh = 0, lastAdd = 0;
	private static enum SpawnType{CIRCLE,CEILING,FLOOR,LEFT,RIGHT,RANDOM};
	
	private static final float CIRCLESPEED = 0.000002f;
	private static final float MONSTERSPEED = 0.1f;
	private static final float RANDOMSPEED = 0.1f;
	private static final float RAINSPEED = 0.1f;
	private static final float BOMBSPEED = 0.5f;
	private static final int DISTANCE = 500;
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void setup()
	{
		try
		{
			newEnemies(SpawnType.CEILING, 25, RAINSPEED, 500, 
				EnemyTypes.Rain.class.getConstructor(int[].class,float.class));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void manage(final int FPS)
	{
		/*
		if(FPS > FPS_MIN)
		{
			try
			{
				if(System.currentTimeMillis() - lastRefresh > REFRESH_TIME)
				{
					insanity.Game.enemies.clear();
					lastRefresh = System.currentTimeMillis();
				}
				
				if(System.currentTimeMillis() - lastAdd > ADD_TIME)
				{					
					newEnemies(SpawnType.RANDOM, 1, CIRCLESPEED, 500, 
						EnemyTypes.Circle.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED, 500, 
						EnemyTypes.Monster.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, RANDOMSPEED, 500, 
						EnemyTypes.Random.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, 0.1f, 500, 
						EnemyTypes.Rain.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 1, 0.5f, 500, 
						EnemyTypes.Bomb.class.getConstructor(int[].class,float.class));
					
					lastAdd = System.currentTimeMillis();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		*/
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
	private static void newEnemies(SpawnType s, int enemyNum, float enemySpeed, int distance, Constructor c)
	{	
		try
		{
			switch(s)
			{
				case CIRCLE:
					Iterator<Player> it = insanity.Game.players.iterator();
					while(it.hasNext())
					{
						Player p = it.next();
						for(int i = 0; i < enemyNum/insanity.Game.players.size(); i++) 
						{
							double degree = 2*Math.PI/enemyNum;
							int x = (int)(p.getLoc()[0] + distance * (float) Math.sin(degree * i));
							int y = (int)(p.getLoc()[1] + distance * (float) Math.cos(degree * i));
							int[] mloc = {x,y};
							Enemy e = (Enemy)c.newInstance(mloc, enemySpeed);
							insanity.Game.qa.insert(mloc[0], mloc[1], e);
							insanity.Game.enemies.add(e);
						}
					}
					break;
				case CEILING:
					if(enemyNum <= 1) return;
					for(int i = 0; i < enemyNum; i++) 
					{
						int x = i*Display.getWidth()/(enemyNum-1);
						int[] mloc = {x, 0};
						Enemy e = (Enemy)c.newInstance(mloc, enemySpeed);
						insanity.Game.qa.insert(mloc[0], mloc[1], e);
						insanity.Game.enemies.add(e);
					}
					break;
				case FLOOR:
					for(int i = 0; i < enemyNum; i++) 
					{
						if(enemyNum <= 1) return;
						int x = i*Display.getWidth()/(enemyNum-1);
						int y = Display.getHeight();
						int[] mloc = {x, y};
						Enemy e = (Enemy)c.newInstance(mloc, enemySpeed);
						insanity.Game.qa.insert(mloc[0], mloc[1], e);
						insanity.Game.enemies.add(e);
					}
					break;
				case LEFT:
					break;
				case RIGHT:
					break;
				case RANDOM:
					Iterator<Player> ite = insanity.Game.players.iterator();
					while(ite.hasNext())
					{
						Player p = ite.next();
						for(int i = 0; i < enemyNum/insanity.Game.players.size(); i++)
						{
							int[] mloc = new int[2];
							do
							{
								mloc[0] = (int)(Math.random()*Display.getWidth());
								mloc[1] = (int)(Math.random()*Display.getHeight());
							}while(p.distanceFrom(mloc) < distance);
							
							Enemy e = (Enemy)c.newInstance(mloc, enemySpeed);
							insanity.Game.qa.insert(mloc[0], mloc[1], e);
							insanity.Game.enemies.add(e);
						}
					}
					break;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
