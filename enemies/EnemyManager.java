package enemies;

import enemies.EnemyTypes.*;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import player.Player;

public class EnemyManager
{
	private static final int REFRESH_TIME = 10000, ADD_TIME = 1000;
	private static long lastRefresh = 0, lastAdd = 0;
	private static enum SpawnType{CIRCLE,CEILING,FLOOR,LEFT,RIGHT,RANDOM};
	
	private static final float CIRCLESPEED = 0.000002f;
	private static final float MONSTERSPEED = 0.2f;
	private static final float RANDOMSPEED = 0.1f;
	private static final float RAINSPEED = 0.1f;
	private static final float BOMBSPEED = 0.5f;
	private static final int DISTANCE = 500;
	
	public static int FPS = 0;
	public static final int FPS_MIN = 80;
	
	public static boolean bossActive;
	public static Boss boss;
		
	@SuppressWarnings("CallToThreadDumpStack")
	public static void setup()
	{
		spawnBoss(MONSTERSPEED);
		bossActive = true;
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void manage(final int _FPS)
	{
		FPS = _FPS; 
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
					newEnemies(SpawnType.RANDOM, 1, CIRCLESPEED,
						Circle.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED, 
						Monster.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, RANDOMSPEED, 
						Random.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 25, RAINSPEED, 
						Rain.class.getConstructor(int[].class,float.class));
					
					newEnemies(SpawnType.RANDOM, 1, BOMBSPEED, 
						Bomb.class.getConstructor(int[].class,float.class));
					
					lastAdd = System.currentTimeMillis();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}*/
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
	private static void newEnemies(SpawnType s, int enemyNum, float enemySpeed, Constructor c)
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
							int x = (int)(p.getLoc()[0] + DISTANCE * (float) Math.sin(degree * i));
							int y = (int)(p.getLoc()[1] + DISTANCE * (float) Math.cos(degree * i));
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
					for(int i = 0; i < enemyNum; i++) 
					{
						boolean satisfySpawn = false;
						int[] mloc = new int[2];
						while(!satisfySpawn)
						{
							satisfySpawn = true;
							mloc[0] = (int)(Math.random()*Display.getWidth());
							mloc[1] = (int)(Math.random()*Display.getHeight());
							Iterator<Player> ite = insanity.Game.players.iterator();
							while(ite.hasNext())
							{
								if(ite.next().distanceFrom(mloc) < DISTANCE)
								{
									satisfySpawn = false;
								}	
							}
						}

						Enemy e = (Enemy)c.newInstance(mloc, enemySpeed);
						insanity.Game.qa.insert(mloc[0], mloc[1], e);
						insanity.Game.enemies.add(e);
					}
					break;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void spawnBoss(float enemySpeed)
	{
		boolean satisfySpawn = false;
		int[] mloc = new int[2];
		while(!satisfySpawn)
		{
			satisfySpawn = true;
			mloc[0] = (int)(Math.random()*Display.getWidth());
			mloc[1] = (int)(Math.random()*Display.getHeight());
			Iterator<Player> ite = insanity.Game.players.iterator();
			while(ite.hasNext())
			{
				if(ite.next().distanceFrom(mloc) < DISTANCE)
				{
					satisfySpawn = false;
				}	
			}
		}

		boss = new Boss(mloc, enemySpeed);
		insanity.Game.qa.insert(mloc[0], mloc[1], boss);
		insanity.Game.enemies.add(boss);
	}
}
