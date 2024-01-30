package csc2b.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import csc2b.server.Server;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		ZEDEMClientPane root  = new ZEDEMClientPane();
		
		primaryStage.setTitle("Client");	
		primaryStage.setScene(new Scene(root));
		
		primaryStage.setHeight(450);
		primaryStage.setWidth(600);
		primaryStage.show();
	}

}
