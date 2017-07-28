package fr.mrcraftcod.ballbrick.jfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.Iterator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class GameController implements EventHandler<ActionEvent>, Sprite
{
	private BallDispenser balls;
	private final ObservableList<Row> rows = FXCollections.observableArrayList();
	private static final int ROWS = 7;
	private static final int COLS = 6;
	private static final int PADDING = 2;
	private static final int BOTTOM_SPACE = 100;
	private int hitCount = 0;
	private final double cellWidth;
	private final double cellHeight;
	
	public GameController()
	{
		cellWidth = MainApplication.WIDTH / COLS;
		cellHeight = (MainApplication.HEIGHT - BOTTOM_SPACE) / ROWS;
		
		balls = new BallDispenser(new Ball(MainApplication.WIDTH / 2, MainApplication.HEIGHT - 1.5001 * Ball.RADIUS));
		
		moveRows();
	}
	
	@Override
	public void handle(ActionEvent event)
	{
		if(!balls.update(rows))
		{
			moveRows();
			MainApplication.gameTimeline.pause();
		}
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("Score: " + hitCount, MainApplication.WIDTH / 2, 15, MainApplication.WIDTH);
		rows.forEach(row -> row.draw(gc));
		balls.draw(gc);
	}
	
	private void moveRows()
	{
		boolean gameOver = false;
		rows.forEach(Row::moveDown);
		Iterator<Row> rowIterator = rows.iterator();
		while(rowIterator.hasNext())
		{
			Row row = rowIterator.next();
			if(row.getRow() >= ROWS)
			{
				if(row.getBoxesCount() > 0)
					gameOver = true;
				rowIterator.remove();
			}
		}
		if(gameOver)
			gameOver();
		rows.add(new Row(this, 1, COLS, PADDING, cellWidth, cellHeight));
	}
	
	private void gameOver()
	{
		hitCount = 0;
		rows.clear();
		balls = new BallDispenser(new Ball(MainApplication.WIDTH / 2, MainApplication.HEIGHT - 1.5001 * Ball.RADIUS));
	}
	
	public void onNewRound(MouseEvent evt)
	{
		if(balls.start(evt))
			MainApplication.gameTimeline.play();
	}
	
	public void addScore(int size)
	{
		hitCount += size;
	}
	
	public void addBall()
	{
		balls.addBall();
	}
	
	public void addBall(Ball ball)
	{
		balls.addBall(ball);
	}
	
	public int getBallCount()
	{
		return balls.getCount();
	}
}
