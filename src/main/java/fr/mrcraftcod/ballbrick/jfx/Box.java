package fr.mrcraftcod.ballbrick.jfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class Box extends Obstacle
{
	private Rectangle hitbox;
	private Rectangle left;
	private Rectangle right;
	private Rectangle top;
	private Rectangle bottom;
	

	public Box(int value, double x, double y, double width, double height)
	{
		super(value);
		hitbox = new Rectangle(x, y, width, height);
		maxValue = Math.max(value, maxValue);
		updateHitbox();
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		gc.setFill(Color.GREEN.interpolate(Color.RED, (value - 1) / (maxValue - 1)));
		gc.fillRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		if(MainApplication.DEBUG)
		{
			gc.setFill(Color.BLUE);
			gc.fillRect(left.getX(), left.getY(), left.getWidth(), left.getHeight());
			gc.fillRect(right.getX(), right.getY(), right.getWidth(), right.getHeight());
			gc.fillRect(top.getX(), top.getY(), top.getWidth(), top.getHeight());
			gc.fillRect(bottom.getX(), bottom.getY(), bottom.getWidth(), bottom.getHeight());
		}
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("" + value, hitbox.getX() + hitbox.getWidth() / 2, hitbox.getY() + hitbox.getHeight() / 2, hitbox.getWidth() / 2);
	}
	
	@Override
	public void setY(double y)
	{
		hitbox.setY(y);
		updateHitbox();
	}
	
	public boolean bounceBall(Ball ball)
	{
		boolean hit = false;
		if(ball.intersects(top.getLayoutBounds()))
		{
			hit = true;
			ball.setYDirection(Ball.YDirection.UP);
		}
		else if(ball.intersects(bottom.getLayoutBounds()))
		{
			hit = true;
			ball.setYDirection(Ball.YDirection.DOWN);
		}
		if(ball.intersects(left.getLayoutBounds()))
		{
			hit = true;
			ball.setXDirection(Ball.XDirection.LEFT);
		}
		else if(ball.intersects(right.getLayoutBounds()))
		{
			hit = true;
			ball.setXDirection(Ball.XDirection.RIGHT);
		}
		if(hit)
			value--;
		return hit;
	}

	public void updateHitbox()
	{
		double hitPadding = 5;
		left = new Rectangle(hitbox.getX(), hitbox.getY() + hitPadding, 1, hitbox.getHeight() - 2 * hitPadding);
		right = new Rectangle(hitbox.getX() + hitbox.getWidth(), hitbox.getY() + hitPadding, 1, hitbox.getHeight() - 2 * hitPadding);
		top = new Rectangle(hitbox.getX() + hitPadding, hitbox.getY(), hitbox.getWidth() - 2 * hitPadding, 1);
		bottom = new Rectangle(hitbox.getX() + hitPadding, hitbox.getY() + hitbox.getHeight(), hitbox.getWidth() - 2 * hitPadding, 1);
	}
}
