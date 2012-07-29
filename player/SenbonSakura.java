package player;
/*
import enemies.Enemy;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import util.EnemyDeletable;
import util.EnemyPredicate;

public class SenbonSakura
{
    private static final int 
            SENBONSAKURA_RADIUS = 250, 
            SENBONSAKURA_SQUARE = SENBONSAKURA_RADIUS*SENBONSAKURA_RADIUS,
            SENBONSAKURA_TIMEOUT = 5000,
            SENBONSAKURA_TIMER = 5000, 
            MAX_REMOTE_SENBON = 4,
			WIDTH = 100;
    private long senbonSakuraT = 0;
    protected EnemyDeletable parent;
    protected int playerNumber, infoOffset, senbonSakuraC, timeRemaining, saveTime, next = 0;
    protected float x, y;
    protected float remoteX, remoteY;
    protected boolean detonateLocal = false, detonateRemote = false;
    
    public SenbonSakura(int _infoOffset, int _playerNumber, EnemyDeletable _parent)
    {
        infoOffset = _infoOffset;
        playerNumber = _playerNumber;
        parent = _parent;
    }
    
    public void detonateLocal(float _x, float _y)
    {     
        if(timeRemaining > SENBONSAKURA_TIMEOUT) 
        {
            timeRemaining -= SENBONSAKURA_TIMEOUT;
            saveTime = timeRemaining;
            x = _x;
            y = _y;
            senbonSakuraC = 40;
            doRemoval(x, y);
            senbonSakuraT = System.currentTimeMillis();
            detonateLocal = true;
        }
    }
    
    public void detonateRemote(float _x, float _y)
    {        
        if(timeRemaining > 1250) 
        {
            timeRemaining -= 1250;  
            saveTime = timeRemaining;
            remoteX = _x;
            remoteY = _y;
            senbonSakuraT = System.currentTimeMillis();
            detonateRemote = true;
            senbonSakuraC = 40;
            next++;
            if(next == MAX_REMOTE_SENBON)
            {
                next = 0;
            }
        }         
    }
    
    private void doRemoval(final float _x, final float _y)
    {
        parent.deleteIf(new EnemyPredicate() 
        {
			@Override
                public boolean satisfiedBy(Enemy e) 
                {
                    float p1 = (_x + 5) - e.getX();
                    float p2 = (_y + 5) - e.getY();
                    return (p1*p1 + p2*p2) < SENBONSAKURA_SQUARE;
                }
        });
    }
    
    public void render(Graphics g) 
    {
        g.setColor(Color.PINK);
        
        if(detonateLocal)
        {
            if (senbonSakuraC-->0) 
            {
                g.fillOval((int)x-SENBONSAKURA_RADIUS/2, (int)y-SENBONSAKURA_RADIUS/2, SENBONSAKURA_RADIUS, SENBONSAKURA_RADIUS);
            }
            else
            {
                detonateLocal = false;
            }
        }
        if(detonateRemote)
        {          
            if((System.currentTimeMillis()-senbonSakuraT) > SENBONSAKURA_TIMER)
            {
                doRemoval(remoteX, remoteY);
                if (senbonSakuraC-->0) 
                {
                    g.fillOval((int)remoteX-SENBONSAKURA_RADIUS/2, (int)remoteY-SENBONSAKURA_RADIUS/2, SENBONSAKURA_RADIUS, SENBONSAKURA_RADIUS);
                }
                else
                {
                    detonateRemote = false;
                }
            }
            else
            {
                int timer = (int)((SENBONSAKURA_TIMER-(System.currentTimeMillis()-senbonSakuraT))/1000) + 1;
                g.fillOval((int)remoteX-5, (int)remoteY-5, 10, 10);
                g.setColor(Color.BLACK);
                Font old = g.getFont();
                g.setFont(new Font("sansserif", Font.BOLD, 10));
                g.drawString(String.valueOf(timer), (int)remoteX-3, (int)remoteY+4);
                g.setFont(old);
                g.setColor(Color.PINK);
            }
        }
        if(timeRemaining < SENBONSAKURA_TIMEOUT)
        {
            timeRemaining = (int)(System.currentTimeMillis()-senbonSakuraT) + saveTime;
        }
        
        int barWidth = (int)Math.min(SENBONSAKURA_TIMEOUT, timeRemaining);
        barWidth *=.05;
        if(playerNumber == 1)
        {
            g.fillRect(100, infoOffset, barWidth, 5);
        }
        else if(playerNumber == 2)
        {
            g.fillRect(WIDTH-barWidth-100, infoOffset, barWidth, 5);
        }
    }
}
*/