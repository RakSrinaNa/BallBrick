package fr.mrcraftcod.ballbrick.jfx;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 05/07/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-07-05
 */
public abstract class Obstacle implements Sprite
{
	
	protected static double maxValue = 0;
	protected int value;
	
	protected Obstacle(int value)
	{
		this.value = value;
	}
	
	public abstract void setY(double y);
	
	public abstract boolean bounceBall(Ball ball);
	
	public int getValue()
	{
		return value;
	}
}
