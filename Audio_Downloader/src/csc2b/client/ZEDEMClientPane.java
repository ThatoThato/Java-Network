package csc2b.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class ZEDEMClientPane extends GridPane //You may change the JavaFX pane layout
{
	//Declaring private attributes
	     //streams attributes
	private BufferedReader readText;
	private PrintWriter writeText;
	private DataOutputStream writeBinary;
	private DataInputStream readBinary;
	
	     //network attributes
	private Socket clientSocket;
	
	    //GUI attributes
	private Button btnConnect;
	private TextField txtName;
	private TextField txtPassword;
	private Button btnBONJOUR;
	Label lblError;
	
	
	private Button btnPLAYLIST;
	private TextField txtID;
	private Button btnZEDEMGET;
	private Button btnZEDEMBYE;
	private TextArea txtListArea;
	private TextArea txtResponse;
	
	
	/**
	 * Constructor
	 * 
	 */
	public ZEDEMClientPane()
	{
		//Setting up the GUI
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.CENTER);
	
		setupLoginGUI();
		
		
		//Implementing the connect button
		btnConnect.setOnAction((event)->
		{
			//Setting up the dataStreams	
			try
			{
				clientSocket = new Socket("localhost",2021);
				System.out.println("Client connect on port: "+ clientSocket.getPort());
				
				readText = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				writeText = new PrintWriter(clientSocket.getOutputStream());
				readBinary = new DataInputStream(clientSocket.getInputStream());
				writeBinary = new DataOutputStream(clientSocket.getOutputStream());
				
			} 
			catch (UnknownHostException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		});
					
		//Implementing the button to log in
		btnBONJOUR.setOnAction((event)->
		{
			String command="";
			String response="";
			
			String name = txtName.getText();
			String password = txtPassword.getText();
			
			command = "BONJOUR "+name+" "+password;
			
			//sending command
			sendCommand(command);
			
			//Reading the server's response
			response = readResponse();
			
			//Checking the response type
			if(response.contains("JA"))
			{
				//Showing the GUI for logged in user.
				setupGUIAfterLogIn();
				
				txtResponse.setText(response);
				
				
			}
			else if(response.contains("NEE"))
			{
			    lblError = new Label(response);
				add(lblError,0,5);
				
			}
			
		});
		
		//Implementing btnPLAYLIST
		btnPLAYLIST.setOnAction((event)->
		{
			sendCommand("PLAYLIST");
			
			String response = readResponse();
			
			String comment = readResponse();
			
			//writing iut the response
			txtResponse.setText(comment);
			
			String list[] = response.split(";");	
			
			for(int i=0; i<list.length; i++)
			{
				txtListArea.appendText(list[i] + "\n");
			}
		});
		
		
		//Implementing btnZEDEMGET
		btnZEDEMGET.setOnAction((event)->
		{
			//getting the ID of the mp4 file
			String ID = txtID.getText();
			
			String command = "ZEDEMGET "+ID;
			
			//Sending command
			sendCommand(command);
			
			downloadFile();
			
		});
	
		//implementing btnZEDEMBYE
		btnZEDEMBYE.setOnAction((event)->
		{
			sendCommand("ZEDEMBYE");
			
			String response = readResponse();
			
			logOut();
		});
		
		
	}
	
	
	/**
	 * Method for Downloading file from server
	 * @return response
	 *                 the response sent by the server
	 *               
	 */
	private void downloadFile()
	{
		//Reading file name
		String fileName  = readResponse();
		
		//Reading file size
		Integer fileSize = Integer.parseInt(readResponse());

		//Creating a new file in client data folder
		File fileToDownload = new File("data/client/"+fileName);
		
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(fileToDownload);
			
			byte[] buffer = new byte[2048];
			int n = 0;
			int totalBytes = 0;
			
			while(totalBytes != fileSize)
			{
				n = readBinary.read(buffer,0,buffer.length);
				fos.write(buffer,0,n);
				
				totalBytes +=n;
			}
			
			String response = readResponse();
			txtListArea.setText(response);
			txtListArea.appendText("file has been downloaded to client.");
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
		finally 
		{
			if(fos !=null)
			{
				try
				{
					fos.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	/**
	 * Method for logging out
	 * 	
	 */
	private void logOut()
	{
		btnPLAYLIST.setVisible(false);
		txtListArea.setVisible(false);
		txtID.setVisible(false);
		btnZEDEMGET.setVisible(false);
		txtResponse.setVisible(false);
		btnZEDEMBYE.setVisible(false);
		
		
		btnConnect.setVisible(true);
		txtName.setVisible(true);
		txtPassword.setVisible(true);
		btnBONJOUR.setVisible(true);
		lblError.setVisible(true);
		
		String response = readResponse();
		lblError.setText(response);
	}
	
	/**
	 * Method for setting up the GUI for log in
	 * 
	 */
	private void setupLoginGUI()
	{
		
		txtName = new TextField("Name");
		txtPassword = new TextField("Password");
		btnBONJOUR = new Button("Log in");
		lblError = new Label("");
		
		
		btnConnect = new Button("Connect to server.");
		btnPLAYLIST = new Button("PLAYLIST");
		
		txtID = new TextField("mp3 ID");
		txtID.setMaxWidth(100);
		btnZEDEMGET = new Button("ZEDEMGET");
		btnZEDEMBYE = new Button("Log out");
		
		txtListArea = new TextArea();
		txtListArea.setMaxHeight(100);
		
		txtResponse= new TextArea();	
		txtResponse.setMaxHeight(50);
		
		
		add(btnConnect,0,0);
		add(txtName,0,1);
		add(txtPassword,0,2);
		add(btnBONJOUR,0,3);
		
	}
	
	/**
	 * Method for setting up the GUI after log in
	 * 
	 */
	private void setupGUIAfterLogIn()
	{
		
		btnConnect.setVisible(false);
		txtName.setVisible(false);
		txtPassword.setVisible(false);
		btnBONJOUR.setVisible(false);
		lblError.setVisible(false);
		
		btnPLAYLIST.setVisible(true);
		txtListArea.setVisible(true);
		txtID.setVisible(true);
		btnZEDEMGET.setVisible(true);
		txtResponse.setVisible(true);
		btnZEDEMBYE.setVisible(true);
		
		add(btnPLAYLIST,0,1);
		add(txtListArea,0,2);
		add(txtID,0,3);
		add(btnZEDEMGET,1,3);
		add(txtResponse,0,4);
		add(btnZEDEMBYE,1,6);
		
		
	}
	
	/**
	 * Method for sending command to a server
	 * @param command
	 *                 the command to be sent to the server
	 *                 
	 */
	private void sendCommand(String command)
	{
		writeText.println(command);
		writeText.flush();
		
		System.out.println("command sent :"+ command);
	}
	
	/**
	 * Method for receiving response from server
	 * @return response
	 *                  the response sent by the server
	 *                  
	 */
	private String readResponse()
	{
		String response="";
		
		try 
		{
			response = readText.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//Checking the response type
		if(response.contains("JA"))
		{
			txtResponse.setText(response);	
		}
		else if(response.contains("NEE"))
		{
		    txtResponse.setText(response);
			add(lblError,0,5);
			
		}
		return response;
	}
	

}
