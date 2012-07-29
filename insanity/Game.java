package insanity;

import enemies.Enemy;
import enemies.EnemyManager;
import java.util.HashSet;
import java.util.Iterator;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import player.MouseControlledPlayer;
import player.Player;

public class Game extends BasicGameState
{
	private final int stateID;
	public static HashSet<Player> players = new HashSet();
	public static HashSet<Enemy> enemies = new HashSet();
	private EnemyManager em = new EnemyManager();
	public static QuadTree<Integer, Enemy> qa = new QuadTree<Integer, Enemy>();
	public static QuadTree<Integer, Enemy> qb = new QuadTree<Integer, Enemy>();
    public static HashSet<Enemy> rejects = new HashSet<Enemy>();
	
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
		playerSetup();	
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		Iterator<Player> i = players.iterator();
		while(i.hasNext())
		{
			Player p = i.next();
			p.logic();
		}
		
		em.start();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException 
	{
		g.setColor(Color.white);
		g.drawString(String.valueOf(enemies.size()), 500, 20);
		
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
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException 
	{
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.exit();
		
		em.manage(container.getFPS());
		qa = qb;
		qb = new QuadTree<Integer, Enemy>();
		enemies.removeAll(rejects);
		rejects.clear();
		
		Iterator<Player> i = players.iterator();
		while(i.hasNext())
		{
			Player p = i.next();
			p.logic();
		}
		
		Iterator<Enemy> j = enemies.iterator();
		while(j.hasNext())
		{
			Enemy e = j.next();
			e.logic(players, delta);
		}
	}
	
	public void playerSetup()
	{
		MouseControlledPlayer p1 = new MouseControlledPlayer();		
        players.add(p1);
	}
}
