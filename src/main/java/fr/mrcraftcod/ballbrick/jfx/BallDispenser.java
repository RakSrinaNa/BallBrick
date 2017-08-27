package fr.mrcraftcod.ballbrick.jfx;

import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 02/06/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-06-02
 */
public class BallDispenser implements Sprite
{
	private final ArrayList<Ball> balls = new ArrayList<>();
	private boolean status = false;
	private Ball first = null;
	private Ball lastBall = null;
	private Queue<Ball> toLaunch = new LinkedList<>();
	private Queue<Ball> toAdd = new LinkedList<>();
	
	public BallDispenser(Ball initBall)
	{
		balls.add(initBall);
		toLaunch.add(initBall);
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		balls.forEach(ball -> ball.draw(gc));
		if(toLaunch.peek() != null)
		{
			Ball next = toLaunch.peek();
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("x" + toLaunch.size(), next.getCenterX(), next.getCenterY() - 1.25 * next.getRadius(), MainApplication.WIDTH);
		}
	}
	
	public void addBall()
	{
		toAdd.add(new Ball(balls.get(0).getCenterX(), balls.get(0).getCenterY(), balls.get(0).getRadius()));
	}
	
	public boolean update(ObservableList<Row> rows)
	{
		if(status)
		{
			startNextBall();
			balls.forEach(ball -> {
				if(!ball.isStopped())
				{
					ball.setCenterX(ball.getCenterX() + ball.getVelocityX());
					ball.setCenterY(ball.getCenterY() + ball.getVelocityY());
					if(ball.getCenterX() + ball.getRadius() >= MainApplication.WIDTH || ball.getCenterX() - ball.getRadius() <= 0)
						ball.setInvertX();
					if(ball.getCenterY() + ball.getRadius() >= MainApplication.HEIGHT || ball.getCenterY() - ball.getRadius() <= 0)
						ball.setInvertY();
					rows.forEach(row -> {
						row.update(ball);
						ball.update();
						if(ball.getCenterY() >= MainApplication.HEIGHT - 1.5 * ball.getRadius())
						{
							if(first != null)
								ball.setCenterX(first.getCenterX());
							else
								first = ball;
							ball.setCenterY(MainApplication.HEIGHT - 1.5001 * ball.getRadius());
							ball.stop();
						}
					});
				}
			});
			if(status && balls.stream().allMatch(Ball::isStopped))
			{
				status = false;
				Ball b;
				while((b = toAdd.poll()) != null)
				{
					b.setCenterX(first.getCenterX());
					b.setCenterY(first.getCenterY());
					balls.add(b);
				}
				first = null;
				lastBall = null;
				toLaunch.addAll(balls);
			}
		}
		return status;
	}
	
	public boolean start(MouseEvent evt)
	{
		double padding = Math.PI / 64;
		boolean[] start = {true};
		balls.forEach(ball -> {
			double angle = Math.atan2(evt.getY() - ball.getCenterY(), evt.getX() - ball.getCenterX());
			if(Math.PI + angle >= padding && angle <= -padding)
			{
				ball.setVelocityX(Math.cos(angle) * 5);
				ball.setVelocityY(Math.sin(angle) * 5);
			}
			else
				start[0] = false;
		});
		if(start[0])
		{
			startNextBall();
			status = true;
		}
		return start[0];
	}
	
	private void startNextBall()
	{
		Ball launch = toLaunch.peek();
		if(launch != null)
		{
			if(lastBall == null || distance(lastBall, launch) >= 3.5 * lastBall.getRadius())
			{
				toLaunch.poll();
				lastBall = launch;
				launch.start();
			}
		}
	}
	
	private double distance(Ball b1, Ball b2)
	{
		return Math.sqrt(Math.pow(b1.getCenterX() - b2.getCenterX(), 2) + Math.pow(b1.getCenterY() - b2.getCenterY(), 2));
	}
	
	public int getCount()
	{
		return balls.size();
	}
}
