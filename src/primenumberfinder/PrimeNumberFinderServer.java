/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;

public class PrimeNumberFinderServer {
    
    //Create variable for writing output
    
    static Writer writer;
    
    public static void main(String args[]){ 

        //Create variable to track for dropped packets
        
        long messages_index = getMostRecentNumber() + 2;
        
        //Create a datagram socket 

        DatagramSocket aSocket = null;

        try {
            
            //Assign the socket to port 6789

            aSocket = new DatagramSocket(6789);

            //Continually check for message requests

            while (true){

                //Create a byte[] buffer

                byte[] buffer = new byte[1000];
                
                //Get message from a client

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);

                aSocket.receive(request);
                
                //Convert the message from the client to a String, eliminating all whitespace
                
                String clientMessage = new String(request.getData()).trim();
                
                //If the message given by the client is a prime number, add it to our file

                try {
                    
                    Long.parseLong(clientMessage, 10);

                    writeToFile(clientMessage);

                } catch (NumberFormatException e) {

                    //Message was not a number, continue

                }

                //Reply to the client with the number for it to check
                
                byte[] message = String.valueOf(messages_index).getBytes();
                
                DatagramPacket reply = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());

                aSocket.send(reply);

                //Increment messages index
                
                if (messages_index > 2) {
                    
                    messages_index++;
                    
                }
                
                messages_index++;

            }

        } catch (SocketException e){

            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {
        
            System.out.println("IO: " + e.getMessage());
    
        } finally {
            
            if (aSocket != null) aSocket.close();
        
        }

    }
    
    private static void openFile() {
        
        //Get the file object
        
        File outputFile = new File("Prime Numbers.txt");
        
        try {
        
            //If the file does not exist, create it

            if (!outputFile.exists()) {

                outputFile.createNewFile();

            }

            //Open the file

            writer = new BufferedWriter(new FileWriter(outputFile, true));

        } catch (IOException e) {
                
                System.out.println("Error creating output file");
                
        }
        
    }
    
    private static void writeToFile(String string) {
        
        //Open the file for us to write to
        
        openFile();
        
        try {
            
            //Append the string to the file

            writer.write(string + "\r\n");
            
            //Close the file
            
            writer.close();
            
        } catch (IOException e) {
            
            System.out.println("Error writing to file");
            
        }

    }
    
    private static long getMostRecentNumber() {
        
        openFile();
        
        BufferedReader input = null;
        
        String lastLine = "-1";
        
        try {
        
            File outputFile = new File("Prime Numbers.txt");

            input = new BufferedReader(new FileReader(outputFile));

            String currentLine;

            while ((currentLine = input.readLine()) != null) { 

                lastLine = currentLine;
                
            }

        } catch (IOException e) {

            e.printStackTrace();
            
        } finally {

            try {
                
                if (input != null) {
                    
                    input.close();
                    
                }
                
            } catch (IOException ex) {
                
                ex.printStackTrace();
                
            }
            
        }
        
        return Long.parseLong(lastLine, 10);
        
    }

}