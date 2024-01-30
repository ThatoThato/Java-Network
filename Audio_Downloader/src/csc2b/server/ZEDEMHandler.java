package csc2b.server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ZEDEMHandler implements Runnable {
	//Declaring private variables
	private ArrayList<String> itemsList;
	
	 private PrintWriter writeText;
	 private BufferedReader readText;
	 private DataOutputStream writeBinary;
	 private DataInputStream readBinary;
	 private Socket serverSocket;
	 

	public ZEDEMHandler(Socket connection) 
	{
		//Initializing variables
		serverSocket = connection;
		
		 try 
		 {
			//Setting up the streams
			 
			writeText = new PrintWriter(serverSocket.getOutputStream());
			readText = new BufferedReader( new InputStreamReader(serverSocket.getInputStream()));
			writeBinary = new DataOutputStream((serverSocket.getOutputStream()));
			readBinary = new DataInputStream(serverSocket.getInputStream());
			
		} 
		 catch (IOException e) 
		 {
           e.printStackTrace();
		}
		 
	}
	
	@Override
	public void run() 
	{
	
		//
		boolean running = true;
		String command = "";
				
		while(running)
		{
		
		   command = readCommand(readText);
		 
		   //Checking if its a logIn command
		   logIn(command);
		   
		   //Checking for PLAYLIST command
		   playList(command);
		   
		   //Checking for ZEDEMGET command
		   uploadFile(command);
		   
		   //Checking if it is the ZEDEMBYE command
		  running = logOut(command);
		  
		}
	}
	
	/**
	 * Method for user log in
	 * @param command
	 *               the command sent from client
	 *               
	 */
	private void logIn(String command)
	{
		String response="";
		
		System.out.println("logIn method");
		System.out.println(command);
		//Checking if it is the log in command
		if(command.contains("BONJOUR"))
		{
		
			StringTokenizer tokens = new StringTokenizer(command);
			
			String commnd = tokens.nextToken();
			String name = tokens.nextToken();		
			String password = tokens.nextToken();
			
			//Checking if the user information is valid
			boolean valid = matchUser(name,password);
			if(valid)
			{
				//Sending response to the client
				response="JA user data matches.";
				
			}
			else
			{
				//Sending response to the client
				response="NEE user data does not match.";
			}
			
			sendResponse(response);
		}
		
	}
	
	
	/**
	 * Method for PLAYLIST 
	 * @param command
	 *               command sent by the client
	 *               
	 */
	private void playList(String command)
	{
		if(command.equals("PLAYLIST"))
		{
			String playlist ="";
			
			ArrayList<String> list =  getFileList();
			
			for(String song : list)
			{
				playlist += song+";";
			}
			
			sendResponse(playlist);
			
			sendResponse("JA play list has been sent.");
		}
	}
	
	
	/**
	 * Method for ZEDEMGET command
	 * @param command
	 *               command sent by the server
	 */
	private void uploadFile(String command)
	{
		if(command.contains("ZEDEMGET"))
		{
			StringTokenizer tokens = new StringTokenizer(command);
			
			String commnd = tokens.nextToken();
			String ID = tokens.nextToken();
			String fileName = idToFileName(ID);
			
			if(!fileName.equals(""))
			{
				//sending file name to client
				sendResponse(fileName);
				
				try 
				{
					//uploading the file
					File fileToUpload = new File("data/server",fileName);
					
					//sending file size
					writeText.println(fileToUpload.length());
					writeText.flush();
								
					FileInputStream fis = new FileInputStream(fileToUpload);
					
					byte[] buffer = new byte[1024];	
					int n=0;
					
					while((n = fis.read(buffer))>0)
					{
						
						writeBinary.write(buffer,0,n);
						writeBinary.flush();
					}
					
					fis.close();
					
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				
				sendResponse("JA File has been uploaded from server");
				
			}
			else
			{
				sendResponse("NEE File does not exist");
			}
		}
	}
	
	/**
	 * Method for logging out
	 * @param command
	 *               the ZEDEMBYE command sent by the client
	 *@return running
	 *               variable that       
	 */
	private boolean logOut(String command)
	{
		boolean running = true;
		
		 if(command.equals("ZEDEMBYE"))
		   {
			   running = false;
			   
			   sendResponse("Logged off");
			   
			   //Closing the streams
			   try 
			   {
				readText.close();
				readBinary.close();
				writeText.close();
				writeBinary.close();
				serverSocket.close();
			   } 
			   catch (IOException e)
			   {	
				e.printStackTrace();
			   }
			   
		   }
		 
		 return running;
	}
	
	
	private boolean matchUser(String userN, String passW)
	{
		boolean found = false;
		
		//Code to search users.txt file for match with userN and passW.
		File userFile = new File("data/server/users.txt"/*OMITTED - Enter file location*/);
		try
		{
		    Scanner scan = new Scanner(userFile);
		    while(scan.hasNextLine()&&!found)
		    {
		    	String line = scan.nextLine();
				String lineSec[] = line.split("\\s");
	    		
			//***OMITTED - Enter code here to compare user***
			    
			    String userName = "";
			    String userPass = "";
			   
			    userName = lineSec[0];
			    userPass = lineSec[1];
			    
			    //Checking if the data matches
			    if(userName.equals(userN) && userPass.equals(passW))
			    {
			    	found = true;
			    }
			
		    }
		    scan.close();
		}
		catch(IOException ex)
		{
		    ex.printStackTrace();
		}
		
		return found;
	}
	
	private ArrayList<String> getFileList()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/List.txt");
		try
		{
			Scanner scan = new Scanner(lstFile);

			//***OMITTED - Read each line of the file and add to the arraylist***
			while(scan.hasNext())
			{
				String line = scan.nextLine();
				
				result.add(line);
			}
			
			
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	
	private String idToFileName(String strID)
	{
		String result ="";
		
		//Code to find the file name that matches strID
		File lstFile = new File("data/server/List.txt");
    	try
    	{
    		Scanner scan = new Scanner(lstFile);

    		String line = "";
    		//***OMITTED - Read filename from file and search for filename based on ID***
    		while(scan.hasNextLine())
    		{
    			line = scan.nextLine();
    			
    			StringTokenizer tokens = new StringTokenizer(line);
    			
    			String ID = tokens.nextToken();
    			if(ID.equals(strID))
    			{
    				result = tokens.nextToken();
    			}
    		
    		}
    		
    		
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	
		return result;
	}
	
	
	/**
	 * Method for reading Command 
	 * @return command
	 *                 the command sent to the server by the client
	 *               
	 */
	private String readCommand(BufferedReader readText)
	{
		
		String command = "";
		try
		{
			command = readText.readLine();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

		return command;
		
	}
	
	/**
	 * Method for sending responses
	 * @param response
	 *                 response sent to the client    
	 */
	private void sendResponse(String response)
	{
		writeText.println(response);
		writeText.flush();
	}
}
