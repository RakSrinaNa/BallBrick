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
public class Box extends Rectangle implements Sprite
{
	private final Rectangle left;
	private final Rectangle right;
	private final Rectangle top;
	private final Rectangle bottom;
	private int value;

	public Box(double x, double y, double w, double h, int v)
	{
		super(x, MainApplication.HEIGHT - y, w, h);
		this.value = v;
		double hitPadding = 5;
		left = new Rectangle(getX(), getY() + hitPadding, 1, getHeight() - 2 * hitPadding);
		right = new Rectangle(getX() + getWidth(), getY() + hitPadding, 1, getHeight() - 2 * hitPadding);
		top = new Rectangle(getX() + hitPadding, getY(), getWidth() - 2 * hitPadding, 1);
		bottom = new Rectangle(getX() + hitPadding, getY() + getHeight(), getWidth() - 2 * hitPadding, 1);
	}

	@Override
	public void draw(GraphicsContext gc)
	{
		gc.setFill(Color.RED);
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
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
		gc.fillText("" + value, getX() + getWidth() / 2, getY() + getHeight() / 2, getWidth() / 2);
	}

	public boolean bounceBall(Ball ball)
	{

		boolean hit = false;
		if(ball.intersects(top.getLayoutBounds()) || ball.intersects(bottom.getLayoutBounds()))
		{
			hit = true;
			ball.setInvertY();
		}
		if(ball.intersects(left.getLayoutBounds()) || ball.intersects(right.getLayoutBounds()))
		{
			hit = true;
			ball.setInvertX();
		}
		if(hit)
			value--;
		return value <= 0;
	}
}
