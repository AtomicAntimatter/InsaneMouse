package insanity;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Sound
{
	private static Music music;
	
	@SuppressWarnings("CallToThreadDumpStack")
	public static void start()
	{
		try 
		{
			music = new Music("Resources/00.wav");
			music.loop();
		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void stop()
	{
		if(music.playing())
		{
			music.stop();
		}
	}
}
