package videoPlayer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VideoPlayer extends Application{
	
	// Path of a video from your local storage
	public static final String MEDIA_FILE_PATH = "";
	
	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		
		Media media = new Media(MEDIA_FILE_PATH);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		MediaView mediaView = new MediaView(mediaPlayer);
		
		root.getChildren().add(mediaView);
		
		Scene scene = new Scene(root, 400, 400, Color.BLACK);
		stage.setScene(scene);
		stage.show();
		
		mediaPlayer.play();
		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				int width = mediaPlayer.getMedia().getWidth();
				int height = mediaPlayer.getMedia().getHeight();
				
				stage.setMinWidth(width);
				stage.setMinHeight(height);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * 
	 * private void showVideoPlayer(String path) {
		if(jfxVideoPanel == null) {
			jfxVideoPanel = new JFXPanel();
			
			media = new Media("file:///" + path.replace('\\', '/'));
			mediaPlayer = new MediaPlayer(media);
			
			Platform.runLater(() -> {
				Group root = new Group();
				mediaView = new MediaView(mediaPlayer);
				
				root.getChildren().add(mediaView);
				
				Scene scene = new Scene(root, 540, 209);
				jfxVideoPanel.setSize(1080, 640);
				jfxVideoPanel.setScene(scene);
			});
			
			window.getVideoPanel().add(jfxVideoPanel);
			window.getVideoPanel().setVisible(true);
			window.getFrame().setComponentZOrder(jfxVideoPanel, 0);
			
			mediaPlayer.play();
			mediaPlayer.setOnReady(new Runnable() {
				@Override
				public void run() {
					int width = mediaPlayer.getMedia().getWidth();
					int height = mediaPlayer.getMedia().getHeight();
				}
			});
		}else {
			Platform.runLater(() -> {
				media = new Media("file:///" + path.replace('\\', '/'));
				mediaPlayer = new MediaPlayer(media);
				mediaView = new MediaView(mediaPlayer);
			});
			
			window.getVideoPanel().setVisible(true);
			window.getFrame().setComponentZOrder(jfxVideoPanel, 0);
			window.getFrame().setComponentZOrder(this, 1);
			window.getFrame().setComponentZOrder(jfxWebPanel, 2);
			
			mediaPlayer.play();
		}
	}
	 * 
	 */
}
