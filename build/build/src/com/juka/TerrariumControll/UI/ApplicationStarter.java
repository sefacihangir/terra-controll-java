package com.juka.TerrariumControll.UI;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationStarter extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("TerrariumControll.fxml"));

	    stage.setTitle("FXML Welcome");
	    stage.setScene(new Scene(root, 300, 275));
	    stage.show();
	}

	public static void main(String[] args) {
		launch(args);
		
	}
}
