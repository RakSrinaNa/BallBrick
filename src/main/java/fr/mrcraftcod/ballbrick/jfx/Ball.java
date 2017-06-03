package fr.mrcraftcod.ballbrick.jfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class Ball extends Circle implements Sprite
{
	public static final int RADIUS = 10;
	private double vx;
	private double vy;
	private boolean invertY;
	private boolean invertX;
	private boolean stop;
	
	public Ball(double x, double y)
	{
		this(x, y, RADIUS);
	}

	public Ball(double x, double y, double radius)
	{
		super(x, y, radius);
		this.vx = 0;
		this.vy = 0;
		this.stop = true;
	}

	@Override
	public void draw(GraphicsContext gc)
	{
		if(MainApplication.DEBUG)
		{
			gc.setFill(Color.PINK);
			gc.fillRect(getCenterX() - getRadius(), getCenterY() - getRadius(), 2 * getRadius(), 2 * getRadius());
		}
		gc.setFill(Color.ORANGE);
		gc.fillOval(getCenterX() - getRadius(), getCenterY() - getRadius(),  2 * getRadius(), 2 * getRadius());
		if(MainApplication.DEBUG)
		{
			gc.setFill(Color.GREEN);
			gc.fillOval(getCenterX() - 5, getCenterY() - 5, 10, 10);
		}
	}
	
	public void stop()
	{
		this.stop = true;
	}
	
	public void start()
	{
		stop = false;
	}
	
	public double getVelocityX()
	{
		return vx;
	}

	public double getVelocityY()
	{
		return vy;
	}
	
	public boolean isStopped()
	{
		return stop;
	}
	
	public void setVelocityY(double velocityY)
	{
		this.vy = velocityY;
	}

	public void setVelocityX(double velocityX)
	{
		this.vx = velocityX;
	}

	public void update()
	{
		if(isInvertX())
			setVelocityX(-getVelocityX());
		if(isInvertY())
			setVelocityY(-getVelocityY());
		invertX = false;
		invertY = false;
	}

	public void setInvertY()
	{
		this.invertY = true;
	}

	public void setInvertX()
	{
		this.invertX = true;
	}

	public boolean isInvertX()
	{
		return invertX;
	}

	public boolean isInvertY()
	{
		return invertY;
	}

	@Override
	public String toString()
	{
		return "Center=[" + getCenterX() + ";" + getCenterY() + "], Radius=" + getRadius() + ", V=[" + getVelocityX() + ";" + getVelocityY() + "]";
	}
}
