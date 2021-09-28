/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;

public class PrimeNumberFinderServer {
    
    public static void main(String args[]){ 
        
        //Create variable to track for dropped packets
        
        int messages_index = 1;
        
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

                //System.out.println(new String(request.getData()));

                //Reply to the client with the number for it to check
                
                byte [] message = String.valueOf(messages_index).getBytes();
                
                DatagramPacket reply = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());

                aSocket.send(reply);

                //Increment messages index
                
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
    
}