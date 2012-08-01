package player;

public class PlayerInfo
{
	public final String name;
	public final int num;
	public int score;
	public boolean dead;
	public int life;
	public int[] deathLocation;
	public long reservoir;

	public PlayerInfo(final String _name, final int playerNum) {
		name = _name;
		num = playerNum;
	}

}
