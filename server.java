import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class server {
	
	//public static Socket[] hashMap=new Socket[10];
	//public static Socket[] hashMap_file=new Socket[10];
	// 1 Create Arraylist to store the sockets for Messaging and File transfer respectively
	public static ArrayList<Socket> hashMap = new ArrayList<>();
	public static ArrayList<Socket> hashMap_file = new ArrayList<>();
	public static ArrayList<String> userName = new ArrayList<>();
			
	
	public volatile static String[] file_msg = new String[10];

	public volatile static boolean[] ready_to_send = new boolean[10];

	private static final int sPort_msg = 8000;   //The server will be listening on this port number
	private static final int sPort_file = 1000;
	
	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
        	ServerSocket listener1 = new ServerSocket(sPort_msg);
        	ServerSocket listener2 = new ServerSocket(sPort_file);

		int clientNum = 0;
        	try {
            		while(true) {
                		new Handler_msg(listener1.accept(),clientNum).start();
                		System.out.println("Client "  + clientNum + " messenger is connected!");
                		new Handler_file(listener2.accept(),clientNum).start();
                		System.out.println("Client "  + clientNum + " file sender is connected!");
				
				clientNum++;
            			}
        	} finally {
            		listener1.close();
            		listener2.close();
        	} 
 
    	}

	
	private static class Handler_file extends Thread {
    	private String message;    //message received from the client
    	private String MESSAGE;    //uppercase message send to the client
    	private Socket connection;
    	private ObjectInputStream in;	//stream read from the socket
    	private ObjectOutputStream out;    //stream write to the socket
    	private int no;		//The index number of the client
    	
    	public Handler_file(Socket connection, int no) {
        		this.connection = connection;
    		this.no = no;
    	
    		hashMap_file.add(no,connection);
    	}
    	

        public void run() {
	 		try{
				//initialize Input and Output streams
				
				try{
					while(true)
					{
						in = new ObjectInputStream(connection.getInputStream());
						
						byte[] content = (byte[]) in.readObject();
//		        		System.out.println("file size : "+content.length);
		        		
		        		while(true){
		        			if(ready_to_send[no]==true)
		        				break;
		        		}
		        		
						message = file_msg[no];
						
						file_msg[no]=null;
						
//		        		System.out.println("whom to send : "+message);

						
						String [] break1 = message.split(" ",2);
						
						if(break1[0].equals("file")){
							
							String [] break2 = break1[1].split(" ",2);
							if(break2[0].equals("broadcast")){
								broadcast_file(content,no);
							}
							else if(break2[0].equals("unicast")){
								int client_no= Integer.parseInt(break2[1]);
								unicast_file(content,client_no,no);
							}
						}
						
					}
					

					}

					catch(ClassNotFoundException classnot){
						System.err.println("Data received in unknown format");
					}
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
			finally{
				//Close connections
				try{
//					in.close();
//					out.close();
					connection.close();
					hashMap_file.set(no,null);
					userName.set(no,null);
					
				}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + no);
				}
			}
        }
        
        public void broadcast_file(byte[] content,int no){
    		System.out.println("broadcasting file.... ");

        	for(int i=0; i<hashMap_file.size(); i++){
				
				if(i==no)
					continue;
				
				if(hashMap_file.get(i)==null){
					System.out.println("Client "+i+" does not exist!");
					continue;
				}
					
    			

				try{
					ObjectOutputStream out1 = new ObjectOutputStream(hashMap_file.get(i).getOutputStream());
					out1.flush();
					
 
        			byte[] file_content = content;
					
					try{
	        			out1.writeObject(file_content);
	        		    System.out.println("file sent to client " + i);

	    			}
					catch(IOException ioException){
						System.out.println("Disconnect with Client " + no);
					}

					
				}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + no);
				}
//				catch(ClassNotFoundException classnot){
//					System.err.println("Data received in unknown format");
//				}
//				catch (InterruptedException e) {
//			         Thread.currentThread().interrupt();  // set interrupt flag
//			         System.out.println("Failed to compute sum");
//			    }
				
				
        	}
		
        	
        }
        
        public void unicast_file(byte[] content,int target_no,int source_no){
        	System.out.println("unicasting file to "+target_no+".... ");
        	int no=target_no;
			
			if(hashMap_file.get(target_no)==null)
				return;
			
			try{
				ObjectOutputStream out1 = new ObjectOutputStream(hashMap_file.get(target_no).getOutputStream());
				out1.flush();
				
				byte[] file_content = content;
    			
				
				try{out1.writeObject(file_content);
        			
        		    System.out.println("file sent to client " + target_no);

    			}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + no);
				}
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}    			

//			catch(ClassNotFoundException classnot){
//				System.err.println("Data received in unknown format");
//			}
//			catch (InterruptedException e) {
//		         Thread.currentThread().interrupt();  // set interrupt flag
//		         System.out.println("Failed to compute sum");
//		    }
		
        	
        }
        
      


}
	

    	private static class Handler_msg extends Thread {
        	private String message;    //message received from the client
        	private String MESSAGE;    //uppercase message send to the client
        	private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
        	private int no;		//The index number of the client
        	
        	public Handler_msg(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
	    	
	    		hashMap.add(no,connection);
	    		userName.add(String.valueOf(no));
        	}
        	

	        public void run() {
		 		try{
					//initialize Input and Output streams
					
					try{
						
						
						while(true)
						{
							in = new ObjectInputStream(connection.getInputStream());

							
							message = (String)in.readObject();
							
							//show the message to the user
							System.out.println("Received message from client " + no +":" + message);
							
							String [] break1 = message.split(" ",2);
							if(break1[0].equals("active")){
								print_active_users(no);
								
							}
							else if(break1[0].equals("username")){
								userName.add(no,break1[1]);
								//String un = userName.get(no);
								System.out.println("Username of client"+ no +": " +userName.get(no));
							}
							
							else if(break1[0].equals("broadcast")){
								broadcast_message(break1[1],no);
							}
							else if(break1[0].equals("unicast")){
								String [] break2 = break1[1].split(" ",2);
								int client_no= Integer.parseInt(break2[0]);
								unicast_message(break2[1],client_no,no);
								
							}
							else if(break1[0].equals("blockcast")){
								String [] break2 = break1[1].split(" ",2);
								int block_no= Integer.parseInt(break2[0]);
								blockcast_message(break2[1],no,block_no);
								
							}
							else if(break1[0].equals("file")){
								
								file_msg[no]=message;
								ready_to_send[no]=true;
								
								String [] break2 = break1[1].split(" ",2);
								if(break2[0].equals("broadcast")){
									broadcast_message("Sent a file.",no);
								
								}
								else if(break2[0].equals("unicast")){
									int client_no= Integer.parseInt(break2[1]);
									unicast_message("Sent a file.",client_no,no);
									
								}
								else if(break2[0].equals("blockcast")){
									int block_no= Integer.parseInt(break2[1]);
									blockcast_message("Sent a file.",no,block_no);
								}
								
							}
							else{
								error_message("message or file in wrong format!",no);
							}

						}
					}
					catch(ClassNotFoundException classnot){
							System.err.println("Data received in unknown format");
						}
				}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + userName.get(no));
				}
				finally{
					//Close connections
					try{
						in.close();
						//out.close();
						connection.close();
						hashMap.set(no,null);
						userName.set(no,null);
					}
					catch(IOException ioException){
						System.out.println("Disconnect with Client " + no);
					}
				}
	        }
	        
	        public void print_active_users(int no){
	        	
    			
	        	
				for(int i=0; i<hashMap.size(); i++){
				
					if(hashMap.get(i)==null)
						continue;
					
					String msg = "Client "+i+" : "+userName.get(i);
					
					try{
	        			ObjectOutputStream out1 = new ObjectOutputStream(hashMap.get(no).getOutputStream());
	        			out1.flush();
	        			
						try{
							out1.writeObject(msg);
							out1.flush();
//							System.out.println("Send message: " + msg + " to client " + userName.get(cno));
						}
						catch(IOException ioException){
							ioException.printStackTrace();
						}

	    			}
					catch(IOException ioException){
						System.out.println("Disconnect with Client " + userName.get(no));
					}


					
				}
	        	
	        }
	        
    
	        public void error_message(String message,int target_no){

	        	int i=target_no;
    			
    			if(hashMap.get(i)==null)
    				return;

    			try{
        			//System.out.println("trying to send to client :"+i);
        			ObjectOutputStream out1 = new ObjectOutputStream(hashMap.get(i).getOutputStream());
        			out1.flush();
        			
        			//receive the message sent from the client
        		
        			//Capitalize all letters in the message

        			MESSAGE = message;
        			//send MESSAGE back to the client
        			sendMessage(MESSAGE,out1,i);
    			}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + userName.get(no));
				}
    			
	    	}
	        
//	        public void send_ready_for_file_msg
	        
	        public void blockcast_message(String message,int no, int block_no){

				for(int i=0; i<hashMap.size(); i++){
					
					if(i==no || i==block_no)
						continue;
					
					if(hashMap.get(i)==null)
						continue;
	    			try{
	        			//System.out.println("trying to send to client :"+i);
	        			ObjectOutputStream out1 = new ObjectOutputStream(hashMap.get(i).getOutputStream());
	        			out1.flush();
	        			
	        			//receive the message sent from the client
	        		
	        			//Capitalize all letters in the message
	        			MESSAGE=("Client "+no+" "+userName.get(no)+" says : ");
	        			MESSAGE+= message;
	        			//send MESSAGE back to the client
	        			sendMessage(MESSAGE,out1,i);
	    			}
					catch(IOException ioException){
						System.out.println("Disconnect with Client " + userName.get(no));
					}
					
					
				}
	    	}
	        
	        public void unicast_message(String message,int target_no, int source_no){

	        	int i=target_no;
    			
    			if(hashMap.get(i)==null)
    				return;

    			try{
        			//System.out.println("trying to send to client :"+i);
        			ObjectOutputStream out1 = new ObjectOutputStream(hashMap.get(i).getOutputStream());
        			out1.flush();
        			
        			//receive the message sent from the client
        		
        			//Capitalize all letters in the message
        			MESSAGE=("Client "+source_no+" "+userName.get(source_no)+" says : ");
        			MESSAGE+= message;
        			//send MESSAGE back to the client
        			sendMessage(MESSAGE,out1,i);
    			}
				catch(IOException ioException){
					System.out.println("Disconnect with Client " + userName.get(no));
				}
    			
	    	}
	        
			public void broadcast_message(String message,int no){
				for(int i=0; i<hashMap.size(); i++){
					
					if(i==no)
						continue;
					
					if(hashMap.get(i)==null)
						continue;
	    			try{
	        			//System.out.println("trying to send to client :"+i);
	        			ObjectOutputStream out1 = new ObjectOutputStream(hashMap.get(i).getOutputStream());
	        			out1.flush();
	        			
	        			//receive the message sent from the client
	        		
	        			//Capitalize all letters in the message
	        			
	        			MESSAGE=("Client "+no+" "+userName.get(no)+" says : ");
	        			MESSAGE+= message;
	        			//send MESSAGE back to the client
	        			sendMessage(MESSAGE,out1,i);
	    			}
					catch(IOException ioException){
						System.out.println("Disconnect with Client " + userName.get(no));
					}
					
					
				}
			}

	//send a message to the output stream
	public void sendMessage(String msg, ObjectOutputStream out1, int cno)
	{
		try{
			out1.writeObject(msg);
			out1.flush();
//			System.out.println("Send message: " + msg + " to client " + userName.get(cno));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

    }

}
