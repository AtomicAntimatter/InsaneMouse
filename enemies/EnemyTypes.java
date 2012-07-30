package enemies;

import java.util.Iterator;
import org.newdawn.slick.Color;
import player.Player;

public final class EnemyTypes 
{
    public static class Circle extends Enemy 
    {
        protected float speed;

		@Override
        public Color getColor() 
        {
            return Color.red;
        }

        public Circle(int[] mloc, float _speed) 
        {
            super(mloc);
            speed = _speed;
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
        protected float speed = 8;

		@Override
        public Color getColor() 
        {
            return Color.magenta.darker();
        }

        public Monster(int[] mloc, float _speed) 
        {
            super(mloc);
            speed = _speed;
        }

		@Override
        public void subLogic(int delta) 
        {
            float dx = Float.MAX_VALUE, d; 
			Player pm = null;
            
			Iterator i = insanity.Game.players.iterator();
            while(i.hasNext()) 
			{
                Player p = (Player)i.next();
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
            float deltaX = speed * (float) Math.abs(Math.cos(angle)) * directionX;
            float deltaY = speed * (float) Math.abs(Math.sin(angle)) * directionY;

            loc[0] += deltaX*delta;
            loc[1] += deltaY*delta;
        }
    }

    public static class Random extends Enemy 
    {
        private float vx, vy;
        protected int bounces = 0;

		@Override
        public Color getColor() 
        {
            return Color.green;
        }

        public Random(int[] mloc, float _speed) 
        {
            super(mloc);
            double ang = Math.random() * Math.PI;
            int qx = (Math.random() > .5)? 1: -1;
            int qy = (Math.random() > .5)? 1: -1;
            vx = _speed * qx * (float) Math.abs(Math.cos(ang));
            vy = _speed * qy * (float) Math.abs(Math.sin(ang));
        }

		@Override
        public void subLogic(int delta)
        {     
            if(loc[0] > BORDER[2])
            {                
                vx = Math.abs(vx)*-1;
                bounces++;
            }
            if(loc[0] < BORDER[0])
            {
            	vx = Math.abs(vx);
                bounces++;
            }
            if(loc[1] > BORDER[3])
            {                
                vy = Math.abs(vy)*-1;
                bounces++;
            }
            if(loc[1] < BORDER[1])
            {
            	vy = Math.abs(vy);
                bounces++;
            }
            
            loc[0] += vx*delta;
            loc[1] += vy*delta;
        }
    }

    public static class Rain extends Enemy 
    {
        private float vx, vy;
		private final int OFFSET;

		@Override
        public Color getColor() 
        {
            return Color.yellow;
        }
        
        public Rain(int[] mloc, float _speed) 
        {
            super(mloc);
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
        
		@Override
        public Color getColor() 
		{
            return Color.blue;
        }
        
        public Bomb(int[] mloc, float _speed) 
		{
            super(mloc, _speed);
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
					Shrapnel s = new Shrapnel(mloc, speed);
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
        }
        
		@Override
        public Color getColor() 
		{
            return Color.cyan;
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
}