package fr.mrcraftcod.ballbrick.jfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.function.Function;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class Ball extends Circle implements Sprite
{
	public enum YDirection
	{
		UP(v -> -Math.abs(v)),
		DOWN(Math::abs),
		NONE(v -> v);
		
		private final Function<Double, Double> process;
		
		YDirection(Function<Double, Double> process)
		{
			this.process = process;
		}
		
		public double process(double velocity)
		{
			return process.apply(velocity);
		}
	}
	
	public enum XDirection
	{
		LEFT(v -> -Math.abs(v)),
		RIGHT(Math::abs),
		NONE(v -> v);
		
		private final Function<Double, Double> process;
		
		XDirection(Function<Double, Double> process)
		{
			this.process = process;
		}
		
		public double process(double velocity)
		{
			return process.apply(velocity);
		}
	}
	
	public static final int RADIUS = 10;
	private double vx;
	private double vy;
	private YDirection yDirection = YDirection.NONE;
	private XDirection xDirection = XDirection.NONE;
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
			gc.fillOval(getCenterX() - getRadius() / 3, getCenterY() - getRadius() / 3, getRadius() / 1.5, getRadius() / 1.5);
			gc.setFill(Color.RED);
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
		setVelocityX(xDirection.process(getVelocityX()));
		setVelocityY(yDirection.process(getVelocityY()));
		xDirection = XDirection.NONE;
		yDirection = YDirection.NONE;
	}

	public void setYDirection(YDirection direction)
	{
		this.yDirection = direction;
	}

	public void setXDirection(XDirection direction)
	{
		this.xDirection = direction;
	}

	@Override
	public String toString()
	{
		return "Center=[" + getCenterX() + ";" + getCenterY() + "], Radius=" + getRadius() + ", V=[" + getVelocityX() + ";" + getVelocityY() + "]";
	}
}
