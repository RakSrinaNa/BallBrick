package fr.mrcraftcod.ballbrick.jfx;

import fr.mrcraftcod.utils.javafx.ApplicationBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
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
	public static Timeline timeline;
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
			timeline = buildTimeline();
			timeline.play();
			gameTimeline.play();
			gameTimeline.pause();
			stage.setOnCloseRequest(evt -> timeline.stop());
		};
	}

	private Timeline buildTimeline()
	{
		Timeline drawTimeline = new Timeline();
		drawTimeline.setCycleCount(Animation.INDEFINITE);
		KeyFrame painter = new KeyFrame(Duration.seconds(1.0 / 60), event -> {
			canvas.getGraphicsContext2D().closePath();
			canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			canvas.getGraphicsContext2D().setFill(BACKGROUND);
			canvas.getGraphicsContext2D().fillRect(0, 0, WIDTH, HEIGHT);
			gameController.draw(canvas.getGraphicsContext2D());
		});
		drawTimeline.getKeyFrames().add(painter);

		gameTimeline = new Timeline();
		gameTimeline.setCycleCount(Animation.INDEFINITE);
		KeyFrame game = new KeyFrame(Duration.seconds(1.0 / 120), gameController = new GameController());
		gameTimeline.getKeyFrames().add(game);

		canvas.setOnMouseClicked(evt -> {
			if(gameTimeline.statusProperty().get() == Animation.Status.PAUSED)
			{
				gameController.onNewRound(evt);
			}
		});

		return drawTimeline;
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
