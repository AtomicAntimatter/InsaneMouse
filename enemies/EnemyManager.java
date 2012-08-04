package enemies;

import enemies.EnemyTypes.Bomb;
import enemies.EnemyTypes.Boss;
import enemies.EnemyTypes.Circle;
import enemies.EnemyTypes.Monster;
import enemies.EnemyTypes.Rain;
import enemies.EnemyTypes.Random;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import player.Player;

public class EnemyManager
{
	private static final int REFRESH_TIME = 10000, ADD_TIME = 1000;
	private static long lastRefresh, lastAdd;
	private static long lastSwitch, switchDelay;
	private static enum SpawnType{CIRCLE,CEILING,FLOOR,LEFT,RIGHT,RANDOM};
	private static int level;
	
	private static Constructor[] ec;
	
	private static final float CIRCLESPEED = 0.000005f;
	private static final float MONSTERSPEED = 0.2f;
	private static final float RANDOMSPEED = 0.1f;
	private static final float RAINSPEED = 0.2f;
	private static final float BOMBSPEED = 0.5f;
	private static final int DISTANCE = 250000;
	
	public static int FPS = 0;
	public static final int FPS_MIN = 80;
	
	public static boolean bossActive;
	public static Boss boss;
		
	@SuppressWarnings("CallToThreadDumpStack")
	public static void setup()
	{
		reset();
		
		try
		{
			ec = new Constructor[]{
				Rain.class.getConstructor(int[].class,float.class),
				Circle.class.getConstructor(int[].class,float.class),
				Monster.class.getConstructor(int[].class,float.class),
				Random.class.getConstructor(int[].class,float.class),
				Bomb.class.getConstructor(int[].class,float.class)};
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void reset()
	{
		bossActive = false;
		level = 0; lastSwitch = 0; switchDelay = 0;
		lastRefresh = 0; lastAdd = 0;
	}
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void manage(final int _FPS)
	{
		FPS = _FPS; 
		
		if(FPS > FPS_MIN)
		{	
			if(lastSwitch + switchDelay < System.currentTimeMillis())
			{
				lastSwitch = System.currentTimeMillis();
				switch(level)
				{
					case 0:
						newEnemies(SpawnType.CEILING, 25, RAINSPEED, ec[0]);
						switchDelay = 4500;
						break;
					case 1:
						newEnemies(SpawnType.CIRCLE, 25, CIRCLESPEED,ec[1]);
						switchDelay = 700;
						break;
					case 2:
						newEnemies(SpawnType.CIRCLE, 30, CIRCLESPEED,ec[1]);
						switchDelay = 4500;
						break;
					case 3:
						newEnemies(SpawnType.CIRCLE, 35, CIRCLESPEED,ec[1]);
						switchDelay = 700;
						break;
					case 4:
						newEnemies(SpawnType.CEILING, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.FLOOR, 25, CIRCLESPEED,ec[1]);
						switchDelay = 4500;
						break;
					case 5:
						newEnemies(SpawnType.CEILING, 25, RAINSPEED, ec[0]);
						newEnemies(SpawnType.CIRCLE, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.CEILING, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.FLOOR, 25, CIRCLESPEED,ec[1]);
						switchDelay = 700;
						break;
					case 6:
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						newEnemies(SpawnType.CIRCLE, 25, CIRCLESPEED,ec[1]);
						switchDelay = 4500;
						break;
					case 7:
						newEnemies(SpawnType.RANDOM, 25, RANDOMSPEED,ec[3]);
						newEnemies(SpawnType.CIRCLE, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						switchDelay = 700;
						break;
					case 8:
						newEnemies(SpawnType.RANDOM, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.CIRCLE, 25, CIRCLESPEED,ec[1]);
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						switchDelay = 9000;
						break;
					case 9:
						newEnemies(SpawnType.RANDOM, 5, BOMBSPEED,ec[4]);
						switchDelay = 5300;
						break;
					case 10:
						newEnemies(SpawnType.RANDOM, 5, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						switchDelay = 5300;
						break;
					case 11:
						newEnemies(SpawnType.RANDOM, 2, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						newEnemies(SpawnType.CEILING, 10, RAINSPEED, ec[0]);
						switchDelay = 5300;
						break;
					case 12:
						newEnemies(SpawnType.RANDOM, 5, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 5, MONSTERSPEED,ec[2]);
						switchDelay = 5300;
						break;
					case 13:
						newEnemies(SpawnType.RANDOM, 2, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 10, MONSTERSPEED,ec[2]);
						newEnemies(SpawnType.RANDOM, 10, RANDOMSPEED, ec[3]);
						switchDelay = 5300;
						break;
					case 14:
						newEnemies(SpawnType.RANDOM, 2, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 25, MONSTERSPEED,ec[2]);
						switchDelay = 5300;
						break;
					case 15:
						newEnemies(SpawnType.RANDOM, 1, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 5, MONSTERSPEED,ec[2]);
						switchDelay = 5300;
						break;
					case 16:
						newEnemies(SpawnType.RANDOM, 1, BOMBSPEED,ec[4]);
						newEnemies(SpawnType.RANDOM, 5, MONSTERSPEED,ec[2]);
						switchDelay = 9000;
						break;
					case 17:
						spawnBoss(MONSTERSPEED);
						bossActive = true;
						switchDelay = Integer.MAX_VALUE;
						break;
				}
				level++;
			}
		}
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
							int x = (int)(p.getLoc()[0] + Math.sqrt(DISTANCE) * (float) Math.sin(degree * i));
							int y = (int)(p.getLoc()[1] + Math.sqrt(DISTANCE) * (float) Math.cos(degree * i));
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
