package csc2b.Seeder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author TG MASIKE
 *
 */

public class Seeder extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		SeederPane root = new SeederPane(2022);
		Scene scene = new Scene(root, 500, 3550);
		primaryStage.setTitle("Seeder");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
