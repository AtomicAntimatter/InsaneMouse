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
		playerSetup();
		EnemyManager.setup();
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
		
		EnemyManager.manage(container.getFPS());
		
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
		MouseControlledPlayer p1 = new MouseControlledPlayer();		
		p1.logic();
        players.add(p1);
	}
}
