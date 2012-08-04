package insanity;

import enemies.Enemy;
import enemies.EnemyManager;
import java.util.HashSet;
import java.util.Iterator;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import player.MouseControlledPlayer;
import player.Player;
import player.PlayerHUD;
import util.QuadTree;

public class Game extends BasicGameState
{
	private final int stateID;
	public static HashSet<Player> players = new HashSet<Player>();
	public static HashSet<Enemy> enemies = new HashSet<Enemy>();	
    public static HashSet<Enemy> rejects = new HashSet<Enemy>();
	public static HashSet<Enemy> newcomer = new HashSet<Enemy>(); 
	public static QuadTree<Integer, Enemy> qa = new QuadTree<Integer, Enemy>();
	public static QuadTree<Integer, Enemy> qb = new QuadTree<Integer, Enemy>();
	
	public Game(final int stateID)
	{
		this.stateID = stateID;
	}
	
	@Override
	public int getID() 
	{
		return stateID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException 
	{
		EnemyManager.setup();
		PlayerHUD.setup(container);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException 
	{
		container.setMouseGrabbed(true);
		playerSetup();
		Sound.start();
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException 
	{
		container.setMouseGrabbed(false);
		reset();
		EnemyManager.reset();
		PlayerHUD.reset();
		Sound.stop();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException 
	{			
		Iterator<Player> i = players.iterator();
		while(i.hasNext())
		{
			Player p = i.next();
			p.render(g);
		}
		
		Iterator<Enemy> j = enemies.iterator();
		while(j.hasNext())
		{
			Enemy e = j.next();
			e.render(g);
		}
		
		PlayerHUD.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException 
	{
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)) game.enterState(Insanity.MAINMENUSTATE);
		
		EnemyManager.manage(container.getFPS());
		
		Iterator<Player> i = players.iterator();
		while(i.hasNext())
		{
			Player p = i.next();
			p.logic(container.getInput());
		}
		
		Iterator<Enemy> j = enemies.iterator();
		while(j.hasNext())
		{
			Enemy e = j.next();
			e.logic(delta);
		}
		
		qa = qb;
		qb = new QuadTree<Integer, Enemy>();
		enemies.removeAll(rejects);
		enemies.addAll(newcomer);
		rejects.clear();
		newcomer.clear();
	}
	
	public void reset()
	{
		players.clear();
		enemies.clear();
		rejects.clear();
		newcomer.clear();
		qa = new QuadTree<Integer, Enemy>();
		qb = new QuadTree<Integer, Enemy>();
	}
	
	public void playerSetup()
	{
		MouseControlledPlayer p1 = new MouseControlledPlayer("Atomic", 0);		
        players.add(p1);
	}
}
