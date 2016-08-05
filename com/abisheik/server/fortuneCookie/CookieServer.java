package com.abisheik.server.fortuneCookie; /**
 Name    : CookieServer.java
 Authors : Nagarajan, Venkat Nivas (1448042)
		   Jayaraja Perumal, Abisheik (1464440)
	       Selvaraj, Sangeetha (1448045)
		   
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CookieServer extends Thread {

    private ServerSocket serverSocket;
    private static final int COOKIE_SERV_TCP_PORT = 8000;
    private static final List<String> fortuneCookieList = new ArrayList<String>();

    // Constructor for enabling server socket
    public CookieServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    /**
     * Main function to execute the thread for Cookie server.
     * a) Fortune text is read from Fortune.txt and store in the fortuneCookieList through BufferedReader
     * b) Thread starts in the corresponding port
     * @param args args value from console
     */
    public static void main(String [] args) {
        BufferedReader bufferedReader = null;
        try {
            String readLine;
            bufferedReader = new BufferedReader(new FileReader("src/Fortune.txt"));
            while ((readLine = bufferedReader.readLine()) != null) {
                fortuneCookieList.add(readLine);
            }
            bufferedReader.close();
            Thread t = new CookieServer(COOKIE_SERV_TCP_PORT);
            t.start();
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
     * d) getFortuneCookies() method is called with client data which is the nos required.
     * e) Cookies data in pushed to client through DataOutputStream.
     * f) Close the current connection with client and wait for other to get connected.
     */
    public void run() {
        byte[] inputData;
        while(true) {
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to new server " + server.getRemoteSocketAddress());

                // DataInputStream to get data input from client
                DataInputStream in = new DataInputStream(server.getInputStream());
                // DataOutputStream to push the processed data output to client
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                // Read the input data from stream and saved to inputData.
                inputData = new byte[1024];
                in.read(inputData);

                // getFortuneCookies() is called with nos of required cookies and the function will return the required cookies
                String clientData = getFortuneCookies( inputData );

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
     * Function will fetch fortune text from the fortuneCookieList based on random number
     * a) Based on cookiesRequested value random number is generated and fetched from list.
     * b) All the values append to the string and return to the run function.
     * c) Character array is converted to string and return.
     * @param byteData
     * @return String cookieBuffer
     */
    public static String getFortuneCookies(byte[] byteData){
        System.out.println("Processing the Client Request...");
        String cookiesRequested = new String(byteData).trim();
        int num = Integer.parseInt(cookiesRequested);
        System.out.println("cookies req..." + num);
        int numToLook = 0;
        String cookie = "";
        String cookieBuffer = "";
        cookieBuffer = cookieBuffer + "Your Cookies are \n";
        // For loop to fetch random cookie from list and append to string
        for (int j = 1;j<=num; j++){
            numToLook = randomNumber();
            cookie = fortuneCookieList.get(numToLook);
            cookieBuffer = cookieBuffer + j + ") " + cookie + "\n";
        }
        return cookieBuffer;
    }

    /**
     * Generate random number based on the size.
     * @return int value
     */
    public static int randomNumber() {
        Random rand = new Random();
        int size = fortuneCookieList.size();
        return rand.nextInt(size);
    }


}