package com.abisheik.client;
/**
 Name    : Client.java
 Authors : Jayaraja Perumal, Abisheik (1464440)
	       Selvaraj, Sangeetha (1448045)
		   Nagarajan, Venkat Nivas(1448042)
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


public class Client {
	
	private static int SERV_TCP_PORT = 0;
	private static final int ENY_DEY_SERV_TCP_PORT = 6000;
	private static final int COOKIE_SERV_TCP_PORT = 8000;
	private static Socket clientSocket = null;
	
	/**
     * Main function to execute the Client request
     * a) displayServiceOptions() is called to display the service offered.
     * b) Three service will be printed
     * 		* Fortune Cookie Server 
     *  	* Encrypt/Decrypt Server 
     *  	* Quit
     * c) Based on the selected option, each service is offered.
     * d) For Fortune Cookie Server, User need to enter the server address to get connected.
     * e) No of cookies required need to be entered.
     * f) communicateToServer() is called with server address and request.
     * g) Option to continue will be displayed. If continue, step from e will performed. If no, it will go to step a.
     * h) For Encrypt/Decrypt Server, User need to enter the server address to get connected.
     * i) Any service will be select from a) Encrypt b) Decrypt c) Quit
     * j) With client input communicateToServer() is called with server address and request.
     * k) Once quit, it will go to step a.
     * l) Quit from main service will end the connection with server. 
     * @param args args value from console
	 * @throws IOException 
     */
	@SuppressWarnings("resource")
	public static void main(String [] args) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException{
	   
		// All the variable are declared
	    String serviceRequested = "";
	    String ipAddress = "";
		String noOfCookies = "";
		String options = "";
		String encryptString = "";
		String decryptString = "";
	    
		try {
			// Scanner to read the input from console
			Scanner in = new Scanner(System.in);
			// Loop will execute continuously until break
			while(true) {
				// Function to display the service offered
				displayServiceOptions();
				// Required service will be read from console
				serviceRequested =  in.nextLine();
				// If Cookie sever is selected it will enter this if lock
				if ( serviceRequested.equalsIgnoreCase("a") )  {
					System.out.println( " Fortune Cookie Server");
					System.out.println( " ~~~~~~~~~~~~~~~~~~~~~");
					try {
						// Asking for Ip address
						System.out.println("\n Enter Server IP Address:");
						// value will be read from console
						ipAddress = in.nextLine();
						SERV_TCP_PORT = COOKIE_SERV_TCP_PORT;
						
						if ( validateServerAddress(ipAddress) ){
							
							// Loop will execute continuously until break
							while(true) {
								
								// Ask for required cookies
								System.out.println(" How many fortune cookies are required?");
								noOfCookies = in.nextLine();
								 // If more than 50 selected
							    if ( Integer.parseInt( noOfCookies ) > 50 ) {
							    	System.out.println("At most 50 cookies served at a time. Please select less than or equal to 50");
							    	continue;
							    }
							    // Else break the process
							    else {
							    	// This function is called to communicate to the server
									communicateToServer( ipAddress, noOfCookies );
							    }
								// Ask for resume process
							    //System.out.println("Do you want to continue with fortune cookie service Y/N?");
							    //response = in.nextLine();
							    // If Y continue the process
							    //if ( response.equalsIgnoreCase("Y") ) {
							    	//continue;
							    //}
							    // Else break the process
							    //else {
							    	//break;
							    //}
							    break;
							}
						}
						else {
							System.out.println( " Exception :: Server Host address is not correct. Enter correct server address." );
						}
					}
					catch (NumberFormatException ne) {
						System.out.println( "Exception :: Please enter valid required cookies." );
						continue;
					}
					catch (Exception e) {
						System.out.println( "Exception :: in system." + e.getMessage());
					}
					finally {
						if (clientSocket != null && clientSocket.isConnected()) {
							// Client connection will be closed at last
					        clientSocket.close();
						}
					}
				}
				// If Encrypt/Decrypt sever is selected it will enter this if block
				else if ( serviceRequested.equalsIgnoreCase("b") ) {
					System.out.println( " Encrypt/Decrypt Server");
					System.out.println( " ~~~~~~~~~~~~~~~~~~~~~~");
					try {
						// Asking for Ip address
						System.out.println("\n Enter Server IP Address :");
						// value will be read from console
						ipAddress = in.nextLine();	
						SERV_TCP_PORT = ENY_DEY_SERV_TCP_PORT;
						
						if ( validateServerAddress(ipAddress) ){
						
							// Loop will execute continuously until break
							while(true) {
								
								// Ask for the following options
								System.out.println(" \n Please select any one from following options");	
								System.out.println(" a) Encyrpt  \n b) Decrypt \n c) Quit \n");
								// value will be read from console
								options = in.nextLine();
								// If encrypt is select if block executes										
								if ( options.equalsIgnoreCase("a") ) {
									System.out.println("\n Please enter string to encrypt :");
									encryptString = "encrypt, " + in.nextLine();
									// This function is called to communicate to the server
									communicateToServer( ipAddress, encryptString );
								}
								// If decrypt is select if block executes
								else if ( options.equalsIgnoreCase("b") ) {
									System.out.println("\n Please enter string to decrypt : ");	
									decryptString = "decrypt, " + in.nextLine();
									communicateToServer( ipAddress, decryptString );
								}
								// This to quit
								else if ( options.equalsIgnoreCase("c") ) {
									break;
								}
								 // Else to continue the process
								else {
									continue;
								}					
							}
						}
						else {
							System.out.println( " Exception :: Server Host address is not correct. Enter correct server address." );
						}
					}
					catch (Exception e) {
						System.out.println( " Exception :: in system." + e.getMessage());
					}
					finally {
						if (clientSocket != null && clientSocket.isConnected()) {
							// Client connection will be closed at last
					        clientSocket.close();
						}
					}
				}
				// If block to exit the client application
				else if ( serviceRequested.equalsIgnoreCase("c") ) {
					System.out.println(" Quiting Application ");
					System.exit(0);
				}
				// Else block to display select a valid option
				else {
					System.out.println(" Please select a valid service option");
				}
			}
		} 
		catch (Exception e) {
			System.out.println( " Exception :: in system." + e.getMessage());
		}
		finally {
			if (clientSocket != null && clientSocket.isConnected()) {
				// Client connection will be closed at last
		        clientSocket.close();
			}
		}
	}
	
	 /**
     * Convert the characters to ASCII value
     * @param serverName parameter
     */
	public static void communicateToServer( String serverName, String parameter ) {
		
		try{
			// Connecting to server
			//System.out.println("Connecting to " + ipAddress + " on port " + COOKIE_SERV_TCP_PORT);
			clientSocket = new Socket(serverName, SERV_TCP_PORT);
			//System.out.println("Just connected to "  + clientSocket.getRemoteSocketAddress());
						
	        // Client data will pushed to DataOutputStream to server
	        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
	        out.writeUTF( parameter );
	        // Received data from server will pushed to DataInputStream
	        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
	        System.out.println(in.readUTF());
	        
	        // Client connection will be closed at last
	        clientSocket.close();
		}
		catch (UnknownHostException ue) {
			System.out.println( " Exception :: Server Host address is not correct. Enter correct server address." );
		}
		catch (ConnectException ce) {
			System.out.println( " Exception :: Server is not running in the given address. Check server and run the client again." );
		}
		catch(IOException e) {
			System.out.println( " Exception :: Please enter correct sever address and try again." );
		}
		
	}
	
	 /**
     * Check for server address and port verification
     * @param serverName parameter
     */
	public static boolean validateServerAddress( String serverName ) {
		 Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
	     Matcher mtch = ptn.matcher(serverName);
	     return serverName.equalsIgnoreCase("localhost") ? true : mtch.find();
	}
	
	 /**
     * This function is used to display the service offered
     */
	public static void displayServiceOptions() {

		System.out.println("\n");
		
		System.out.println("#####################################");
		System.out.println("#### Client - Server Application ####");
		System.out.println("#####################################");
		
		System.out.println("\n Which server do you want to connect ?");	
		System.out.println(" a) Fortune Cookie Server \n b) Encrypt/Decrypt Server \n c) Quit \n");
		
	}
	

}
