//import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
class FTPClient {

    public static void main(String argv[]) throws Exception
    {
        String sentence;
        String modifiedSentence;
        boolean isOpen = true;
        int number=1;
        boolean notEnd = true;
        int port1=3483;
        int port = 3485;
        String statusCode;
        boolean clientgo = true;

        System.out.println("Welcome to the simple FTP App   \n     Commands  \nconnect servername port# connects to a specified server \nLIST: lists files on server \nRETR: fileName.txt downloads that text file to your current directory \nSTOR: fileName.txt Stores the file on the server \nQUIT terminates the connection to the server");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        sentence = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(sentence);


        if(sentence.startsWith("connect")){
            String serverName = tokens.nextToken(); // pass the connect command
            serverName = tokens.nextToken();
            port1 = Integer.parseInt(tokens.nextToken());
            System.out.println("You are connected to " + serverName);
            Socket ControlSocket= new Socket(serverName, port1);
            while(isOpen && clientgo)
            {

                sentence = inFromUser.readLine();
                DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());
                DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));

                if(sentence.equals("LIST:"))
                {

                    port = port +2;
                    ServerSocket welcomeData = new ServerSocket(port);


                    System.out.println("\n \n \nThe files on this server are:");
                    outToServer.writeBytes (port + " " + sentence + " " + '\n');

                    Socket dataSocket = welcomeData.accept();
                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
                    while(notEnd)
                    {
                        modifiedSentence = inData.readUTF();
                        if(modifiedSentence.equals("eof"))
                            break;
                        System.out.println("	" + modifiedSentence);
                    }

                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\nWhat would you like to do next: \nRETR: file.txt ||  STOR: file.txt  || QUIT");

                }
                else if(sentence.startsWith("RETR: ")) {
		    StringTokenizer tokensretr = new StringTokenizer(sentence);
                    String fileName = tokensretr.nextToken();
                    fileName = tokensretr.nextToken();
                    String curDir = System.getProperty("user.dir");
                    port = port + 2;
                    ServerSocket welcomeData = new ServerSocket(port);
		    outToServer.writeBytes (port + " " + sentence + " " + '\n');


                    System.out.println("\n\nDownloading File....");

                    Socket dataSocket = welcomeData.accept();

                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
		    File file = new File(curDir + "/" + fileName);
		    file.createNewFile();
		    byte[] buffer = new byte[1024];
                    DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

		    while ((inData.read(buffer))!=-1){
		    	dataOut.write(buffer, 0, buffer.length);
		    }

                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\n\nFile Downloaded");
                    System.out.println("\nWhat would you like to do next: \nLIST: ||  STOR: file.txt  || QUIT");

                }
                else if (sentence.startsWith("STOR: ")){
		    StringTokenizer tokensstor = new StringTokenizer(sentence);
                    String fileName = tokensstor.nextToken();
                    fileName = tokensstor.nextToken();
                    port = port + 2;
                    ServerSocket welcomeData = new ServerSocket(port);
		    outToServer.writeBytes (port + " " + sentence + " " + '\n');

                    System.out.println("\n\nFile uploading to server... Please wait...");

                    Socket dataSocket = welcomeData.accept();
                    String curDir = System.getProperty("user.dir");

		    File file = new File(curDir + "/" + fileName);

                    DataInputStream inData = new DataInputStream(new FileInputStream(file));
		    DataOutputStream dataOutToServer = new DataOutputStream(dataSocket.getOutputStream());

                    byte[] buffer = new byte[1024];
                    while ((inData.read(buffer))!=-1){
                        dataOutToServer.write(buffer, 0, buffer.length);
                    }

                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\n\nFile Uploaded");
                    System.out.println("\nWhat would you like to do next: \nLIST: || RETR: file.txt || QUIT");

                }
                else {
                    if (sentence.equals("QUIT")) {
                        clientgo = false;
                    }
                    System.out.print("No server exists with that name or server not listening on that port try agian");
                }
            }
        }
    }
}
