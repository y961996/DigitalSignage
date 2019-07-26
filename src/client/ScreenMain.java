package client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ScreenMain extends Application{

	@Override
	public void start(Stage arg0) throws Exception {
		new DigitalScreenWindow(DigitalScreenWindow.NAME + (DigitalScreenWindow.screen_count + 1), DigitalScreenWindow.ADDRESS, DigitalScreenWindow.PORT);
	}
	
	public static void main(String[] args) {
		launch();
	}

}
