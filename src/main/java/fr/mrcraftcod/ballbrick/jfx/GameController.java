package fr.mrcraftcod.ballbrick.jfx;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class GameController implements EventHandler<ActionEvent>
{
	private final ArrayList<Sprite> sprites = new ArrayList<>();
	private final ObservableList<Box> boxes = FXCollections.observableArrayList();
	private final Ball ball;
	private static final int rows = 5;
	private static final int cols = 6;
	private static final int padding = 2;
	private static final int bottomSpace = 250;


	public GameController()
	{
		boxes.addListener((ListChangeListener<Box>) c -> {
			while(c.next())
			{
				sprites.addAll(c.getAddedSubList());
				sprites.removeAll(c.getRemoved());
			}
		});

		double aw = MainApplication.WIDTH - (cols + 1) * padding;
		double ah = (MainApplication.HEIGHT - bottomSpace) - (rows + 1) * padding;
		double w = MainApplication.WIDTH / cols;
		double h = (MainApplication.HEIGHT - bottomSpace) / rows;
		for(int i = 0; i < rows * cols; i++)
		{
			int r = i / cols;
			int c = i % cols;
			Box box = new Box(c * w + (c + 1) * padding, MainApplication.HEIGHT - r * h - (r + 1) * padding, aw / cols, ah / rows, new Random().nextInt(10));
			boxes.add(box);
		}

		sprites.add(ball = new Ball(MainApplication.WIDTH / 2, 2 * Ball.RADIUS));
		ball.setVelocityX(3);
		ball.setVelocityY(-2);
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
		Iterator<Box> boxIterator = boxes.iterator();
		while(boxIterator.hasNext())
			if(boxIterator.next().bounceBall(ball))
				boxIterator.remove();
		ball.update();
		if(ball.getCenterY() >= MainApplication.HEIGHT - 1.5 * ball.getRadius())
		{
			ball.setCenterY(MainApplication.HEIGHT - 2.5 * ball.getRadius());
			MainApplication.gameTimeline.pause();
		}
	}

	public List<Sprite> getSprites()
	{
		return sprites;
	}

	public Ball getBall()
	{
		return ball;
	}
}
