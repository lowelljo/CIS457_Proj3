//import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
import javax.xml.crypto.Data;

public class ftpserver extends Thread {
        private Socket connectionSocket;
        int port;
        int count = 1;

        public ftpserver(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }


        public void run() {
            if (count == 1)
                System.out.println("User connected" + connectionSocket.getInetAddress());
            count++;

            try {
                processRequest();

            } catch (Exception e) {
                System.out.println(e);
            }

        }


        private void processRequest() throws Exception {
            String fromClient;
            String clientCommand;
            String clientFileName;
            byte[] data;
            String frstln;

            while (true) {
                if (count == 1)
                    System.out.println("User connected" + connectionSocket.getInetAddress());
                count++;

                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                fromClient = inFromClient.readLine();

                System.out.println(fromClient);
                StringTokenizer tokens = new StringTokenizer(fromClient);

                frstln = tokens.nextToken();
                port = Integer.parseInt(frstln);
                clientCommand = tokens.nextToken();
                System.out.println(clientCommand);


                if (clientCommand.equals("LIST:")) {
                    String curDir = System.getProperty("user.dir");

                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataOutputStream dataOutToClient =
                            new DataOutputStream(dataSocket.getOutputStream());
                    File dir = new File(curDir);

                    String[] children = dir.list();
                    if (children == null) {
                        // Either dir does not exist or is not a directoryRET
                    } else {
                        for (int i = 0; i < children.length; i++) {
                            // Get filename of file or directory
                            String filename = children[i];

                            if (filename.endsWith(".txt"))
                                dataOutToClient.writeUTF(children[i]);
                            //System.out.println(filename);
                            if (i - 1 == children.length - 2) {
                                dataOutToClient.writeUTF("eof");
                                // System.out.println("eof");
                            }//if(i-1)


                        }//for

                        dataSocket.close();
                        //System.out.println("Data Socket closed");
                    }//else


                }//if list:


                if (clientCommand.equals("RETR:")) {
                    clientFileName = tokens.nextToken();

                    String curDir = System.getProperty("user.dir");

                    File file = new File(curDir + "/" + clientFileName);
		    file.createNewFile();

                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataInputStream fileIn = new DataInputStream(new FileInputStream(file));
                    DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                    byte[] buffer = new byte[1024];
                    while ((fileIn.read(buffer))!=-1) {
                        dataOutToClient.write(buffer, 0, buffer.length);
                    }

                    System.out.println("\nFile " + clientFileName + " sent");

                    dataSocket.close();
                }

                if (clientCommand.equals("STOR:")){
                    clientFileName = tokens.nextToken();
		    
                    String curDir = System.getProperty("user.dir");

		    File file = new File(curDir + "/" + clientFileName);

                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
		    byte[] buffer = new byte[1024];                    
		    DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

		    while ((inData.read(buffer))!=-1){
			dataOut.write(buffer, 0, buffer.length);
		    }
                    
		    dataOut.close();
                    System.out.println("\n File " + clientFileName + " saved");

                }
		
		if (clientCommand.equals("QUIT")){
			connectionSocket.close();
			System.out.println("\nConnection is closed.");	
			break;
			
		}
            }
        }
    }
	

