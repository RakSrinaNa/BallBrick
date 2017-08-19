package fr.mrcraftcod.ballbrick.jfx;

import fr.mrcraftcod.utils.javafx.ApplicationBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.function.Consumer;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/05/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-05-31
 */
public class MainApplication extends ApplicationBase
{
	public static final double WIDTH = 500;
	public static final double HEIGHT = 800;
	public static final boolean DEBUG = false;
	public static final Color BACKGROUND = Color.GRAY;
	public static Timeline drawTimeline;
	private static Canvas canvas;
	private static GameController gameController;
	public static Timeline gameTimeline;
	
	@Override
	public String getFrameTitle()
	{
		return "BallBrick";
	}
	
	@Override
	public Scene buildScene(Stage stage)
	{
		return new Scene(createContent(stage), WIDTH, HEIGHT);
	}
	
	@Override
	public Consumer<Stage> getStageHandler()
	{
		return stage -> {
			stage.setResizable(false);
			buildTimelines(stage);
			drawTimeline.play();
			gameTimeline.play();
			gameTimeline.pause();
			stage.setOnCloseRequest(evt -> {
				drawTimeline.stop();
				gameTimeline.stop();
			});
		};
	}
	
	private void buildTimelines(Stage stage)
	{
		drawTimeline = new Timeline();
		drawTimeline.setCycleCount(Animation.INDEFINITE);
		KeyFrame painter = new KeyFrame(Duration.seconds(1.0 / 60), event -> {
			canvas.getGraphicsContext2D().closePath();
			canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			canvas.getGraphicsContext2D().setFill(BACKGROUND);
			canvas.getGraphicsContext2D().fillRect(0, 0, WIDTH, HEIGHT);
			canvas.getGraphicsContext2D().setFill(Color.BLACK);
			canvas.getGraphicsContext2D().setTextAlign(TextAlignment.LEFT);
			canvas.getGraphicsContext2D().fillText("Speed:" + gameTimeline.getRate() + "x", 5, 15, WIDTH);
			gameController.draw(canvas.getGraphicsContext2D());
		});
		drawTimeline.getKeyFrames().add(painter);
		
		gameTimeline = new Timeline();
		gameTimeline.setCycleCount(Animation.INDEFINITE);
		KeyFrame game = new KeyFrame(Duration.seconds(1.0 / 120), gameController = new GameController());
		gameTimeline.getKeyFrames().add(game);
		
		stage.getScene().setOnKeyPressed(evt -> {
			if(evt.getCode() == KeyCode.UP)
				gameTimeline.setRate(round(gameTimeline.getRate() + 0.2, 1, 2));
			else if(evt.getCode() == KeyCode.DOWN)
				gameTimeline.setRate(round(gameTimeline.getRate() - 0.2, 1, 2));
			else if(evt.getCode() == KeyCode.R)
				gameTimeline.setRate(1);
		});
		
		canvas.setOnMouseClicked(evt -> {
			if(gameTimeline.statusProperty().get() == Animation.Status.PAUSED)
				gameController.onNewRound(evt);
		});
	}
	
	private double round(double val, int digits, int step)
	{
		if(val <= 0)
			return  0.01;
		double temp = Math.round(val * Math.pow(10, digits));
		temp = step * temp / step;
		return temp / Math.pow(10, digits);
	}
	
	@Override
	public Consumer<Stage> getOnStageDisplayed() throws Exception
	{
		return null;
	}
	
	@Override
	public Parent createContent(Stage stage)
	{
		Group root = new Group();
		canvas = new Canvas();
		canvas.widthProperty().bind(stage.widthProperty());
		canvas.heightProperty().bind(stage.heightProperty());
		root.getChildren().add(canvas);
		return root;
	}
}
