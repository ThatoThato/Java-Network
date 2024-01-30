/**
 * 
 */
package csc2b.Seeder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
 * @author TG MASIKE
 *
 */
public class SeederPane extends BorderPane {

	private DatagramSocket seederSoc;
	private DatagramPacket rcvPacket;
	private DatagramPacket sendPacket;
	
	private int filecount = 0;
	private ArrayList<String> list = new ArrayList<>();
	private boolean isRunning = false;
	
	private Button btnAddFile;
	private TextArea txtFileList;
	
	
	/**
	  *Constructor
	  *@param portNumber
	  *                 to connect on when sending data
	  */
	public SeederPane(int portNumber)
	{
		System.out.println("Initializing...");
		createUI();
		try
		{
			seederSoc = new DatagramSocket(portNumber);
			
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		btnAddFile.setOnAction((event)->
		{
			filecount++;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./data/Seeder"));
			
			//Openning window for file chooser
			File handle = fileChooser.showOpenDialog(null);
			
			if(handle != null)
			{
				String fileName = handle.getName();
				System.out.println(fileName);
				list.add(fileName);
				
				txtFileList.appendText(filecount + " " +fileName + "\n");
			}
		});
		
		runSeeder();
	}

	
	public void runSeeder()
	{
		isRunning = true;
		while(isRunning)
		{
			try {
				//receive packet data
				byte[] buffer = new byte[2048];
				rcvPacket = new DatagramPacket(buffer, buffer.length);
				seederSoc.receive(rcvPacket);
			
				String data = new String(rcvPacket.getData());
				
				StringTokenizer tokens = new StringTokenizer(data);
				String cmd = tokens.nextToken();
				try {
				
				if(cmd.equals("Connect"))
				{
					String msg = "Connected";
					sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, rcvPacket.getAddress(), rcvPacket.getPort());
				}else if(cmd.equals("GETLIST"))
				{
					String res = "";
					//sending the list
					for(String str: list)
					{
						res += str + "@";
					}
					
					sendPacket = new DatagramPacket(res.getBytes(), res.getBytes().length, rcvPacket.getAddress(), rcvPacket.getPort());
					
				}else if(cmd.equals("GETFILE"))
				{
						//sending the file
						int id = Integer.parseInt(tokens.nextToken());
						
						String filename = list.get(id - 1);
						
						File fileToSend = new File("./data/Seeder/" + filename);
						
						FileInputStream fis = new FileInputStream(fileToSend);
						
						byte[] fileBuffer = fis.readAllBytes();
						sendPacket = new DatagramPacket(buffer, fileBuffer.length , rcvPacket.getAddress(), rcvPacket.getPort());
						
						fis.close();
				}
				
				seederSoc.send(sendPacket);
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

/**
*Method for setting up the GUI
*
*/
	public void createUI()
	{
		System.out.println("Making ui");
		txtFileList = new TextArea();
		btnAddFile = new Button("Add File");
		
		
		GridPane root = new GridPane();
		root.setPadding(new Insets(5, 5, 5, 5));
		root.setVgap(5);
		root.setHgap(5);
		
		root.add(btnAddFile, 0, 0, 14, 1);
		root.add(txtFileList, 0, 2, 40, 40);
		
		getChildren().add(root);
	}
}
