import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class client {
	
//	public volatile static boolean ready_to_receive = false;
	public volatile static boolean file_msg_sent = false;
	
	public volatile static String file_msg;

	public void clientk() {}
	
	public static void main(String args[])
	{
		Socket requestSocket1;
		Socket requestSocket2;

		try{
			requestSocket1 = new Socket("localhost", 8000);
			requestSocket2 = new Socket("localhost", 1000);

			System.out.println("Connected to localhost in port 8000");
			
			new msg_write(requestSocket1).start();
			new msg_listen(requestSocket1).start();
			new file_write(requestSocket2).start();
			new file_listen(requestSocket2).start();

		}
		catch (UnknownHostException e) {
            e.printStackTrace();
        }
		catch (ConnectException e) {
			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
//			try{
				//in.close();
				//out.close();
//				requestSocket1.close();
//				requestSocket2.close();
//
//			}
//			catch(IOException ioException){
//				ioException.printStackTrace();
//			}
		}
		
		
	}


	private static class file_write extends Thread{
		private Socket requestSocket;           //socket connect to the server
		private ObjectOutputStream out;         //stream write to the socket
		private ObjectInputStream in;          //stream read from the socket
		private String message;                //message send to the server
		private String MESSAGE;                //capitalized message read from the server

		public file_write(Socket requestSocket) {
    		this.requestSocket = requestSocket;

		}
		
		public void run(){
			try{

//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				while(true)
				{
					while(true){
						if(file_msg_sent==true)
							break;
					}
					message = file_msg;
//					System.out.println(" following message retreived from file_msg : "+message);
					
					file_msg=null;
					file_msg_sent=false;
					out = new ObjectOutputStream(requestSocket.getOutputStream());
					out.flush();
				
//					message = bufferedReader.readLine();
					

					String [] break1 = message.split(" ",2);
					
					if(break1[0].equals("file")){
						String [] break2 = break1[1].split(" ",2);
						
						File file = new File(break2[0]);
						byte[] file_content = Files.readAllBytes(file.toPath());
				
//						System.out.println("sending file...");
						out.writeObject(file_content);
					}

				}
			}
			catch (ConnectException e) {
	    			System.err.println("Connection refused. You need to initiate a server first.");
			} 
//			catch ( ClassNotFoundException e ) {
//	            		System.err.println("Class not found");
//	        } 
			catch(UnknownHostException unknownHost){
				System.err.println("You are trying to connect to an unknown host!");
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//Close connections
				try{
					//in.close();
					out.close();
					requestSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
		}

	}
	
	
	private static class msg_write extends Thread{
		private Socket requestSocket;           //socket connect to the server
		private ObjectOutputStream out;         //stream write to the socket
		private ObjectInputStream in;          //stream read from the socket
		private String message;                //message send to the server
		private String MESSAGE;                //capitalized message read from the server

		public msg_write(Socket requestSocket) {
    		this.requestSocket = requestSocket;

		}
		
		public void run(){
			try{
				
				//initialize inputStream and outputStream


				//get Input from standard input
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				
				System.out.println("Enter username : ");
				message = bufferedReader.readLine();
				out = new ObjectOutputStream(requestSocket.getOutputStream());
				out.flush();
				
				sendMessage("username "+message);			
				
				System.out.println("Your username is set to : "+message);
				
				
				
				while(true)
				{
					//System.out.print("Hello, please input a sentence: ");
					//read a sentence from the standard input
					
					
					message = bufferedReader.readLine();
					//Send the sentence to the server
//					System.out.println(message);
					out = new ObjectOutputStream(requestSocket.getOutputStream());
					out.flush();
					
					//System.out.println("io streams initialized");
					String [] break1 = message.split(" ",2);
					
//					String [] breakx = break1[1].split(" ",2);
					
					if(break1[0].equals("file")){
						String [] break2 = break1[1].split(" ",2);
						
						boolean check = new File(break2[0]).exists();
						
						if(check){
						
						file_msg = message;
						file_msg_sent=true;
											
						sendMessage(break1[0]+" "+break2[1]);
						}
						else{
							System.out.println("File does not exist!");
							sendMessage("File does not exist!");
						}
						
//						System.out.println("Following msg sent to server : "+break1[0]+" "+break2[1]);
						
					}
						
					else if(break1[0].equals("message"))
						sendMessage(break1[1]);
					
					else if(break1[0].equals("active"))
						sendMessage("active");
					else{
						sendMessage("Cleint entered message in wrong format");
						//System.out.println("Wrong format! Command should start with file or message.");
						
					}

				}
			}
			catch (ConnectException e) {
	    			System.err.println("Connection refused. You need to initiate a server first.");
			} 
//			catch ( ClassNotFoundException e ) {
//	            		System.err.println("Class not found");
//	        } 
			catch(UnknownHostException unknownHost){
				System.err.println("You are trying to connect to an unknown host!");
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//Close connections
				try{
					//in.close();
					out.close();
					requestSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
		}
		
		//send a message to the output stream
		void sendMessage(String msg)
		{
			try{
				//stream write the message
//				System.out.println("message sent...");
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	
	private static class file_listen extends Thread{
		
		private Socket requestSocket;           //socket connect to the server
		private ObjectOutputStream out;         //stream write to the socket
		private ObjectInputStream in;          //stream read from the socket
		private String message;                //message send to the server
		private String MESSAGE;                //capitalized message read from the server

		public file_listen(Socket requestSocket) {
    		this.requestSocket = requestSocket;

		}
		
		public void run(){
			
			try{
				

				while(true)
				{
					in= new ObjectInputStream(requestSocket.getInputStream());
//					System.out.println("in"+in);						
					File file = new File("received_file.zip");
						
					byte[] content = (byte[]) in.readObject();
			
					Files.write(file.toPath(), content);
					
				}
					
			}
			catch (ConnectException e) {
	    			System.err.println("Connection refused. You need to initiate a server first.");
			} 
			catch ( ClassNotFoundException e ) {
	            		System.err.println("Class not found");
	        	} 
			catch(UnknownHostException unknownHost){
				System.err.println("You are trying to connect to an unknown host!");
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//Close connections
				try{
					in.close();
					//out.close();
					requestSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
			
		}
	}
	
	
	private static class msg_listen extends Thread{
		
		private Socket requestSocket;           //socket connect to the server
		private ObjectOutputStream out;         //stream write to the socket
		private ObjectInputStream in;          //stream read from the socket
		private String message;                //message send to the server
		private String MESSAGE;                //capitalized message read from the server

		public msg_listen(Socket requestSocket) {
    		this.requestSocket = requestSocket;

		}
		
		public void run(){
			
			try{
				

				while(true)
				{
					in= new ObjectInputStream(requestSocket.getInputStream());
				
					MESSAGE = (String)in.readObject();
					

					System.out.println(MESSAGE);
				}
			}
			catch (ConnectException e) {
	    			System.err.println("Connection refused. You need to initiate a server first.");
			} 
			catch ( ClassNotFoundException e ) {
	            		System.err.println("Class not found");
	        	} 
			catch(UnknownHostException unknownHost){
				System.err.println("You are trying to connect to an unknown host!");
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//Close connections
				try{
					in.close();
					//out.close();
					requestSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
			
		}
	}
	


}

