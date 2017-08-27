package fr.mrcraftcod.ballbrick.jfx;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 01/06/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-06-01
 */
public class Row implements Sprite
{
	private final ArrayList<Box> boxes = new ArrayList<>();
	private NewBall newBall;
	private final double padding;
	private final double cellWidth;
	private final double cellHeight;
	private final GameController parent;
	private int row;
	
	public Row(GameController parent, int row, int cols, double padding, double cellWidth, double cellHeight)
	{
		this.parent = parent;
		this.row = row;
		this.padding = padding;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		init(cols);
	}
	
	public void init(int count)
	{
		int rnd = 1 + ThreadLocalRandom.current().nextInt(count - 2);
		LinkedList<Integer> indices = new LinkedList<>();
		for(int i = 0; i < count; i++)
			indices.add(i);
		
		double y = (row + 1) * padding + row * cellHeight;
		for(int i = 0; i < rnd; i++)
		{
			Collections.shuffle(indices);
			int index = indices.poll();
			double x = (index + 1) * padding + index * cellWidth;
			boxes.add(new Box(1 + ThreadLocalRandom.current().nextInt(2 * parent.getBallCount()), x, y, cellWidth, cellHeight));
		}
		Collections.shuffle(indices);
		int index = indices.poll();
		double x1 = (index + 1) * padding + index * cellWidth;
		double x2 = (index + 2) * padding + (index + 1) * cellWidth;
		double y2 = (row + 2) * padding + (row + 1) * cellHeight;
		newBall = new NewBall((x2 + x1) / 2, (y + y2) / 2, 1.75 * Ball.RADIUS);
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		boxes.forEach(box -> box.draw(gc));
		if(!newBall.isTaken())
			newBall.draw(gc);
	}
	
	public void update(Ball ball)
	{
		if(!newBall.isTaken() && ball.intersects(newBall.getLayoutBounds()))
		{
			parent.addBall();
			newBall.setTaken();
		}
		Iterator<Box> boxIterator = boxes.iterator();
		while(boxIterator.hasNext())
		{
			Box box = boxIterator.next();
			if(box.bounceBall(ball))
			{
				parent.addScore(1);
				if(box.getValue() <= 0)
					boxIterator.remove();
			}
		}
	}
	
	public void moveDown()
	{
		row++;
		double y = (row + 1) * padding + row * cellHeight;
		boxes.forEach(box -> {
			box.setY(y);
			box.updateHitbox();
		});
		double y2 = (row + 2) * padding + (row + 1) * cellHeight;
		newBall.setCenterY((y + y2) / 2);
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getBoxesCount()
	{
		return boxes.size();
	}
}
