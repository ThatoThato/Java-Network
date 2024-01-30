package csc2b.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
	
	private ServerSocket ss;
	private Socket s;
	private int portNumber;
	
	private SERVERHandler serverhandler;
	
	public Server(int portNumber){
		this.portNumber = portNumber;
		
		try{
			ss = new ServerSocket(portNumber);
			system.out.println("waiting for connection");
			
			boolean running = true;
			
			while(running){
				s = ss.accept();
				
				system.out.println("connection received");
				
				serverhandler = new ServerHandler(s);
				Thread thread = new Thread(serverhandler);
				thread.start();
				
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		public static void main(String[] args){
			Server server = new Server(2021);
		}
	}
	
	
	
}