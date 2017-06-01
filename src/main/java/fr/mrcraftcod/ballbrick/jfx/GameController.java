package fr.mrcraftcod.ballbrick.jfx;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class GameController implements EventHandler<ActionEvent>, Sprite
{
	private final ArrayList<Sprite> sprites = new ArrayList<>();
	private final ObservableList<Ball> balls = FXCollections.observableArrayList();
	private final ObservableList<Row> rows = FXCollections.observableArrayList();
	private final Ball ball;
	private static final int ROWS = 7;
	private static final int COLS = 6;
	private static final int PADDING = 2;
	private static final int BOTTOM_SPACE = 100;
	private int hitCount = 0;
	private final double cellWidth;
	private final double cellHeight;
	
	public GameController()
	{
		rows.addListener((ListChangeListener<Row>) c -> {
			while(c.next())
			{
				sprites.addAll(c.getAddedSubList());
				sprites.removeAll(c.getRemoved());
			}
		});
		balls.addListener((ListChangeListener<Ball>) c -> {
			while(c.next())
			{
				sprites.addAll(c.getAddedSubList());
				sprites.removeAll(c.getRemoved());
			}
		});
		
		cellWidth = MainApplication.WIDTH / COLS;
		cellHeight = (MainApplication.HEIGHT - BOTTOM_SPACE) / ROWS;
		
		balls.add(ball = new Ball(MainApplication.WIDTH / 2, MainApplication.HEIGHT - 1.5001 * Ball.RADIUS));
		moveRows();
	}
	
	@Override
	public void handle(ActionEvent event)
	{
		ball.setCenterX(ball.getCenterX() + ball.getVelocityX());
		ball.setCenterY(ball.getCenterY() + ball.getVelocityY());
		if(ball.getCenterX() + ball.getRadius() >= MainApplication.WIDTH || ball.getCenterX() - ball.getRadius() <= 0)
			ball.setInvertX();
		if(ball.getCenterY() + ball.getRadius() >= MainApplication.HEIGHT || ball.getCenterY() - ball.getRadius() <= 0)
			ball.setInvertY();
		rows.forEach(row -> row.update(ball));
		ball.update();
		if(ball.getCenterY() >= MainApplication.HEIGHT - 1.5 * ball.getRadius())
		{
			ball.setCenterY(MainApplication.HEIGHT - 1.5001 * ball.getRadius());
			MainApplication.gameTimeline.pause();
			moveRows();
		}
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("Score: " + hitCount, MainApplication.WIDTH / 2, 20, MainApplication.WIDTH);
		rows.forEach(row -> row.draw(gc));
		balls.forEach(ball -> ball.draw(gc));
	}
	
	private void moveRows()
	{
		rows.forEach(Row::moveDown);
		Iterator<Row> rowIterator = rows.iterator();
		while(rowIterator.hasNext())
			if(rowIterator.next().getRow() >= ROWS)
				rowIterator.remove();
		rows.add(new Row(this, 1, COLS, PADDING, cellWidth, cellHeight));
	}
	
	public void onNewRound(MouseEvent evt)
	{
		double padding = Math.PI / 64;
		double angle = Math.atan2(evt.getY() - ball.getCenterY(), evt.getX() - ball.getCenterX());
		if(Math.PI + angle >= padding && angle <= - padding)
		{
			ball.setVelocityX(Math.cos(angle) * 5);
			ball.setVelocityY(Math.sin(angle) * 5);
			MainApplication.gameTimeline.play();
		}
	}
	
	public void addScore(int size)
	{
		hitCount += size;
	}
}
