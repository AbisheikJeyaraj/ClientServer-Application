package com.abisheik.server.encryptDecrypt;
/**
 Name    : EncryptDecryptServer.java
 Authors : Selvaraj, Sangeetha (1448045)
           Jayaraja Perumal, Abisheik (1464440)
	       Nagarajan, Venkat Nivas(1448042)
 */
 
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;


public class EncryptDecryptServer extends Thread {

    private ServerSocket serverSocket;
    private static final int ENY_DEY_SERV_TCP_PORT = 6000;

    // Constructor for enabling server socket
    public EncryptDecryptServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    /**
     * Main function to execute the thread for Encrypt/Decrypt server
     * a) Thread starts in the corresponding port
     * @param args args value from console
     */
    public static void main(String [] args) {
        // Connected to the encrpt/decrypt TCP server port
        try {
            Thread t = new EncryptDecryptServer(ENY_DEY_SERV_TCP_PORT);
            t.start(); // Starting the thread
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runnable method for processing the client request
     * a) Server will accept socket connection from client when true.
     * b) DataInputStream and DataInputStream are declared and initialized.
     * c) Client data will be received from DataInputStream and processed.
     * d) Based on the process, encrypt or decrypt will be selected.
     * e) Encrppted/Decrypted data in pushed to client through DataOutputStream.
     * f) Close the current connection with client and wait for other to get connected.
     */
    public void run() {
        String process = "";
        String message = "";
        String clientData = "";
        List<String> valueList = null;
        while(true) {
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to new server " + server.getRemoteSocketAddress());

                // DataInputStream to get data input from client
                DataInputStream in = new DataInputStream(server.getInputStream());
                // DataOutputStream to push the processed data output to client
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                // Data will received as the format (process, StringToencrypt)
                String receivedData = in.readUTF();
                // Split the value with comma to get the process and StringToencrypt
                valueList = Arrays.asList(receivedData.split(",",2));
                // process and message are get from the list
                process = valueList.get(0).trim();
                message = valueList.get(1).trim();

                // If process is encrypt, encrypt function is called with message as parameter
                // clientData is returned from the functions
                if ( process.equalsIgnoreCase("encrypt") ) {
                    clientData = "Encrypted string :: " + encrypt(message);
                }
                // If process is decrypt, decrypt function is called with message as parameter
                else if ( process.equalsIgnoreCase("decrypt") ) {
                    clientData = "Decrypted string :: " + decrypt(message);
                }

                // clientData is write to output stream and send to the client machine
                out.writeUTF("\n" + clientData + "\n");

                // Once the process is completed server connection get closed
                server.close();
            }
            catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }
            catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Convert the string to encrypted value
     * a) Each character in the string is get and passed to CharToASCII() to get the ascii values.
     * b) Add + 2 to the ascii value and send as the parameter to ASCIIToChar() which will convert ascii value to character
     * c) Character array is converted to string and return.
     * @param encryptString
     * @return String value
     */
    public static String encrypt(String encryptString){
        System.out.println("Processing Encrypt Request...");
        String encryptedString = "";
        int stringLength = encryptString.length();
        System.out.println(" string to ency:: " + encryptString + "lenght" + stringLength );
        char encryptedCharArray[] = new char[stringLength];
        for (int i = 0;i<stringLength;i++) {
            int asciiNumber = CharToASCII( encryptString.charAt(i) );
            asciiNumber = asciiNumber + 2;
            encryptedCharArray[i] = (char)  ASCIIToChar ( asciiNumber );
        }
        encryptedString = new String (encryptedCharArray);
        System.out.println("Encrypted string :: " + encryptedString);
        return encryptedString;
    }

    /**
     * Convert the string to encrypted value
     * a) Each character in the string is get and passed to CharToASCII() to get the ascii values.
     * b) Subract - 2 to the ascii value and send as the parameter to ASCIIToChar() which will convert ascii value to character
     * c) Character array is converted to string and return.
     * @param decryptString
     * @return String value
     */
    public static String decrypt(String decryptString){
        System.out.println("Processing Decrypt Request...");
        String decryptedString = "";
        int stringLength = decryptString.length();
        char decryptedCharArray[] = new char[stringLength];
        for (int i = 0;i<stringLength;i++) {
            int asciiNumber = CharToASCII( decryptString.charAt(i) );
            asciiNumber = asciiNumber - 2;
            decryptedCharArray[i] = (char)  ASCIIToChar ( asciiNumber );
        }
        decryptedString = new String (decryptedCharArray);
        System.out.println("Decrypted string :: " + decryptedString);
        return decryptedString;
    }

    /**
     * Convert the characters to ASCII value
     * @param character character
     * @return ASCII value
     */
    public static int CharToASCII(final char character){
        //System.out.println("ascii number" + (int)character );
        return (int)character;
    }

    /**
     * Convert the ASCII value to character
     * @param ascii ascii value
     * @return character value
     */
    public static char ASCIIToChar(final int ascii){
        //System.out.println("encrypted" + (char)ascii);
        return (char)ascii;
    }




}
