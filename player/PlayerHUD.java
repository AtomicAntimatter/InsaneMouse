package player;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.*;
import player.SenbonSakura.SBSK_Timer;

public class PlayerHUD
{
	public static List<PlayerInfo> pInfo = new LinkedList();
	private static Image hudIMG;
	private static int[] dim;
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void setup(GameContainer c)
	{
		dim = new int[]{c.getWidth(),c.getHeight()};
		try
		{
			hudIMG = new Image("Resources/HUDIMG.png").getScaledCopy(dim[0], 100);
		}catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
		
	public static void render(Graphics g)
	{
		hudIMG.draw(0, 0);
		
		g.setColor(Color.white);
		g.drawString(String.valueOf(insanity.Game.enemies.size()), dim[0]-100, 20);
		
		for(int i = 0; i < pInfo.size(); i++)
		{
			PlayerInfo pi = pInfo.get(i);
			
			g.setColor(Color.white);
			int spot = (int)(i*dim[0]*0.75/pInfo.size())+100;
			g.drawString(pi.num + ": " + pi.name, spot+15, 10);
			
			g.drawString(String.valueOf(pi.life), spot+120, 10);
			
			g.setColor(Color.cyan);
			g.fillRect(spot+15, 30, 150*pi.reservoir/SBSK_Timer.MAX_RESERVOIR, 5);
			
			if(pi.dead)
			{
				g.setColor(Color.red);
			}
			else
			{
				g.setColor(Color.green);
			}
			
			g.fillOval(spot, 15, 10, 10);
		}
	}
}
