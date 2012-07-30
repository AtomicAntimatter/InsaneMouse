package util;

public interface GLMenuItem 
{
	public enum Type {PLAY,SETTINGS,HOWTO,QUIT,NULL};
	public void render();
	public boolean mouseClick(int mx, int my, boolean click);
	public Type getType();
}
