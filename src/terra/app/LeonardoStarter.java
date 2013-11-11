package terra.app;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LeonardoStarter extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/terra/app/template/LeonardoTemplate.fxml"));

	    stage.setTitle("Leonardo 1.0");
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add("/terra/app/template/style.css");
	    stage.setScene(scene);
	    stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
