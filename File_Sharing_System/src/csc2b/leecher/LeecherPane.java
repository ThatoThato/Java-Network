package csc2b.leecher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * @author SN MAHLOBO
 *
 */
public class LeecherPane extends BorderPane {

	DatagramSocket soc = null;
	
	private InetAddress IPAddress;
	private int portNumber;
	private Button btnConnect;
	private Button btnGetList;
	private Button btnGetFile;
	private Label lblPort;
	private Label lblID;
	private TextField txtPort;
	private TextField txtID;
	private TextArea txtList;
	private TextArea txtStatus;
	
	
	/**
	  *Constructor
	  */
	public LeecherPane()
	{
		createUI();
		try {
			
			soc = new DatagramSocket();
			
		} catch (SocketException e2) 
		{
			e2.printStackTrace();
		}
		
		btnConnect.setOnAction(e->  
		{
			try {
				//Getting host address
				IPAddress = InetAddress.getByName("localhost");
				
				//getting port number
				portNumber = 2022;

				//initializing socket
				
				String msg = "Connect";
		
				
				sendMsg(msg);
				
				byte[] receiveData = new byte[1024];
				DatagramPacket rcvPacket =	new DatagramPacket(receiveData, receiveData.length);
				soc.receive(rcvPacket);
				
				
				String rcvMsg = new String(rcvPacket.getData());
				txtStatus.appendText(rcvMsg);
			
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		btnGetList.setOnAction(e->
		{ 
			String msg = "GETLIST";			
			sendMsg(msg);
			
			
			getList();
		});
		
		btnGetFile.setOnAction(e->
		{ 
			String msg = "GETFILE"; 
			sendMsg(msg);
			
		});
	}
	
	/**
	  *Method for setting up GUI
	  *
	  */
	public void createUI()
	{
		lblID = new Label("ID:");
		txtID = new TextField();
		txtList = new TextArea();
		txtStatus = new TextArea();
		btnGetFile = new Button("Get File");
		btnGetList = new Button("Get List");
		btnConnect = new Button("Connect");
		
		GridPane root = new GridPane();
		root.setPadding(new Insets(5, 5, 5, 5));
		root.setVgap(5);
		root.setHgap(5);
	
		root.add(btnConnect, 0, 0, 13, 1);
		root.add(btnGetList, 20, 0, 14, 1);
		root.add(txtList, 0, 2, 40, 40);
		root.add(lblID,0, 43, 5, 1);
		root.add(txtID, 5, 43, 20, 1);
		root.add(btnGetFile, 25, 43, 14, 1);
		root.add(txtStatus, 40, 0, 31, 40);
		getChildren().add(root);
	}
	
	/**
	  *Method for sending message
	  *@param message
	  */
	public void sendMsg(String message)
	{
		try {
			byte[] buffer = message.getBytes(); 
			DatagramPacket sndPacket = new DatagramPacket(buffer, buffer.length, IPAddress, portNumber);
			soc.send(sndPacket);
			
			System.out.println("Messsage sent to Seeder");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	  *Method for getting list
	  *
	  */
	public void getList()
	{
		DatagramPacket rcvPacket =	null;
		
		try {
			byte[] receiveData = new byte[2048];
			rcvPacket = new DatagramPacket(receiveData, receiveData.length);
			
			soc.receive(rcvPacket);
			
			String list = new String(rcvPacket.getData());
			String[] listArr = list.split("@");
			
			for(int i = 0; i < listArr.length; i++)
			{
				txtList.appendText(listArr[i]);
			}
			txtStatus.appendText("List sent");
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void getFile()
	{
		DatagramPacket rcvPacket =	null;
		
		try {
			byte[] receiveData = new byte[2048];
			rcvPacket = new DatagramPacket(receiveData, receiveData.length);
			
			soc.receive(rcvPacket);
			
			FileOutputStream fos = new FileOutputStream("./data/Leecher/");
			
			fos.write(receiveData);
			fos.flush();
			
			
			fos.close();
		} catch (IOException ex) 
		{	
			ex.printStackTrace();
		}
	}
}
