/**
 * 
 */
package csc2b.leecher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author SN MAHLOBO
 *
 */
public class Leecher extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		LeecherPane root = new LeecherPane();
		Scene scene =new Scene(root, 400, 500);
		primaryStage.setTitle("Leecher");
		primaryStage.setScene(scene);
		primaryStage.show();
	
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
