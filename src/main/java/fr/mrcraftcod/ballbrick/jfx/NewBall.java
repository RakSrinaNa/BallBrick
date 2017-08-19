package fr.mrcraftcod.ballbrick.jfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 02/06/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-06-02
 */
public class NewBall extends Circle implements Sprite
{
	private boolean taken;
	
	public NewBall(double x, double y, double r)
	{
		super(x, y, r);
		taken = false;
	}
	
	public void setTaken()
	{
		taken = true;
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		gc.setFill(Color.GREEN);
		gc.fillOval(getCenterX() - getRadius(), getCenterY() - getRadius(), getRadius() * 2, getRadius() * 2);
		gc.setFill(MainApplication.BACKGROUND);
		gc.fillOval(getCenterX() - 0.75 * getRadius(), getCenterY() - 0.75 * getRadius(), 0.75 * getRadius() * 2, 0.75 * getRadius() * 2);
	}
	
	public Ball getBall()
	{
		Ball ball = new Ball(getCenterX(), getCenterY());
		ball.setVelocityX(0);
		ball.setVelocityY(5);
		ball.setPhantom(true);
		return ball;
	}
	
	public boolean isTaken()
	{
		return taken;
	}
}
