package enemies;

import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import player.Player;

public final class EnemyTypes 
{
    public static class Circle extends Enemy 
    {
        protected float speed;

        public Circle(int[] mloc, float _speed) 
        {
            super(mloc);
            speed = _speed;
			c= Color.red;
        }

		@Override
        public void subLogic(int delta) 
        {		
            float dx = Float.MAX_VALUE, d; 
			
			Player pm = null;
			
            Iterator<Player> i = insanity.Game.players.iterator();
            while(i.hasNext()) 
			{
                Player p = i.next();
                d = distanceFrom(p.getLoc());
                if(d < dx) 
				{
                    dx = d;
                    pm = p;
                }
            }
            
            int directionX = 1;
            int directionY = 1;
            float p1 = pm.getLoc()[1] - loc[1];
            float p2 = pm.getLoc()[0] - loc[0];

            if(p1 < 0) 
            {
                directionY = -1;
            }
            
            if(p2 < 0) 
            {
                directionX = -1;
            }

            float angle = (float) Math.atan(p1 / p2);
            float deltaD = dx*speed*delta;
            float deltaX = deltaD * (float) Math.abs(Math.cos(angle)) * directionX;
            float deltaY = deltaD * (float) Math.abs(Math.sin(angle)) * directionY;

            loc[0] += deltaX;
            loc[1] += deltaY;
        }
    }
	
    public static class Monster extends Enemy 
    {
        protected final float SPEED;
		private float turnspeed = 1;
		private int stopDist = 0;
		
        public Monster(int[] mloc, float _speed) 
        {
            super(mloc);
			SPEED = _speed;
			c = Color.magenta.darker();
			double ang = Math.random()*360;
            v = new Vector2f(SPEED, SPEED);
			v.setTheta(ang);
        }

		@Override
        public void subLogic(int delta) 
        {
            float dx = Float.MAX_VALUE, d; 
			Player pm = null;
            
			Iterator<Player> i = insanity.Game.players.iterator();
            while(i.hasNext()) 
			{
                Player p = i.next();
                d = distanceFrom(p.getLoc());
                if(d < dx) 
				{
                    dx = d;
                    pm = p;
                }
            }
			
			if(dx > stopDist)
			{
				Vector2f a = new Vector2f(pm.getLoc()[0] - loc[0], pm.getLoc()[1] - loc[1]);
				a.scale(turnspeed);
				v.add(a);
				v.scale(SPEED/v.length());
			
				loc[0] += v.x*delta;
				loc[1] += v.y*delta;
			}
        }
    }

    public static class Random extends Enemy 
    {
        protected int bounces = 0;

        public Random(int[] mloc, float _speed) 
        {
            super(mloc);
			c = Color.green;
            double ang = Math.random()*360;
			v = new Vector2f(_speed, _speed);
			v.setTheta(ang);
        }

		@Override
        public void subLogic(int delta)
        {     
            if((loc[0] > BORDER[2])||(loc[0] < BORDER[0]))
            {                
                v.add(new Vector2f(-2*v.x,0));
                bounces++;
            }
            if((loc[1] > BORDER[3])||(loc[1] < BORDER[1]))
            {                
                v.add(new Vector2f(0,-2*v.y));
                bounces++;
            }
            
            loc[0] += v.x*delta;
            loc[1] += v.y*delta;
        }
    }

    public static class Rain extends Enemy 
    {
        private float vx, vy;
		private final int OFFSET;
        
        public Rain(int[] mloc, float _speed) 
        {
            super(mloc);
			c = Color.yellow;
            vx = -_speed;
            vy = _speed;
			
			if(mloc[0] < BORDER[3])
			{
				OFFSET = BORDER[2];
			}
			else
			{
				OFFSET = 0;
			}
        }

		@Override
        public void subLogic(int delta) 
        {
            loc[0] += vx*delta;
            loc[1] += vy*delta;
            if(loc[1] > BORDER[3])
            {
                loc[0] = loc[0]+loc[1]-OFFSET;
                loc[1] = 0;
            }
			if(loc[0] < BORDER[0])
			{
				loc[0] = BORDER[2];
			}
        }
    }
    
    public static class Bomb extends Monster 
	{
        private static final int PIECES = 50;
		private static final int MAX_DISTANCE = 8000;
        
        public Bomb(int[] mloc, float _speed) 
		{
            super(mloc, _speed);
			c = Color.blue;
        }
        
		@Override
        public void subLogic(int delta) 
		{
			super.subLogic(delta);

			float dx = Float.MAX_VALUE, d;
			Iterator<Player> i = insanity.Game.players.iterator();
			while (i.hasNext()) 
			{
				Player p = (Player) i.next();
				d = distanceFrom(p.getLoc());
				if(d < dx) 
				{
					dx = d;
				}
			}

			if (dx < MAX_DISTANCE) 
			{
				int[] mloc = {(int)loc[0],(int)loc[1]};
				for (int j = 0; j < PIECES; j++) 
				{
					Shrapnel s = new Shrapnel(mloc, 0.5f);
					insanity.Game.newcomer.add(s);
					insanity.Game.qb.insert(mloc[0], mloc[1], s);
				}
				isDead = true;
				insanity.Game.rejects.add(this);
			}
        }
    }
    
    public static class Shrapnel extends Random 
	{    
        public Shrapnel(int[] mloc, float _speed) 
		{
            super(mloc, _speed);
			c = Color.cyan;
        }   
		
		@Override
		public void subLogic(int delta)
		{
			super.subLogic(delta);
			if(bounces > 2)
			{
				isDead = true;
				insanity.Game.rejects.add(this);
			}
		}
    }
	
	public static class Boss extends Random
	{
		private final int SPEWRATE = 15000;
		private final int SHOOTRATE = 10000;
		private long lastSpew = 0;
		private long lastShoot = 0;
		private final float MINIONSPEED = 0.5f;
		private final float SHOOTSPEED = 0.5f;
		public int health = 100;
		
		@SuppressWarnings("LeakingThisInConstructor")
		public Boss(int[] mloc, float _speed)
		{
			super(mloc, _speed);
			bossMinion = true;
			EnemyManager.bossList.add(this);
			c = Color.cyan.darker(0.6f);
			size = 20;
		}
		
		@Override
		public void subLogic(int delta)
		{
			int speed = (int)(10*delta/size);
			super.subLogic(speed);
			
			if(EnemyManager.FPS > EnemyManager.FPS_MIN)
			{
				long spewDelay = (long)(SPEWRATE/size);
				long shootDelay = (long)(SHOOTRATE/size);
				if(lastSpew + spewDelay < System.currentTimeMillis())
				{
					lastSpew = System.currentTimeMillis();	
					int[] mloc = {(int)loc[0],(int)loc[1]};
					int minionAmount = size*3;
					for(int i = 0; i < minionAmount; i++)
					{
						Monster m = new Monster(mloc, MINIONSPEED);
						m.turnspeed = 0.00001f;
						m.stopDist = 10000+size;
						m.size = 5;
						m.bossMinion = true;
						insanity.Game.newcomer.add(m);
						insanity.Game.qb.insert(mloc[0], mloc[1], m);
					}
				}
				if(lastShoot + shootDelay < System.currentTimeMillis())
				{
					lastShoot = System.currentTimeMillis();	
					int[] mloc = {(int)loc[0],(int)loc[1]};
					Shrapnel r = new Shrapnel(mloc, SHOOTSPEED);
					r.c = Color.green.darker();
					r.bossMinion = true;
					
					float dx = Float.MAX_VALUE, d; 
					Player pm = null;
					
					Iterator<Player> i = insanity.Game.players.iterator();
					while(i.hasNext()) 
					{
						Player p = i.next();
						d = distanceFrom(p.getLoc());
						if(d < dx) 
						{
							dx = d;
							pm = p;
						}
					}

					r.v = new Vector2f(pm.getLoc()[0] - loc[0], pm.getLoc()[1] - loc[1]);
					r.v.scale(SHOOTSPEED/r.v.length());
					
					insanity.Game.newcomer.add(r);
					insanity.Game.qb.insert(mloc[0], mloc[1], r);
				}
			}
		}
		
		@Override
		public void render(Graphics g) 
		{
			g.setColor(c);
			g.fillOval(loc[0]-size/2, loc[1]-size/2, size, size);
			
			for(int i = 0; i < 5; i++)
			{
				if(i*20 < health)
				{
					g.setColor(Color.red.brighter());
					g.fillOval(loc[0]-size/2+i*size/5,loc[1]-size/1.5f,3,3);
				}
			}
		}
	}
}