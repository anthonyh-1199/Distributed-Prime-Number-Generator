/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;

public class PrimeNumberFinderClient {

    public static void main(String args[]){ 
        
        //Create socket object

        DatagramSocket aSocket = null;

        try {

            //Create a new socket

            aSocket = new DatagramSocket(); 

            //Connect to our server

            InetAddress aHost = InetAddress.getByName("localhost");

            int serverPort = 6789; 

            //Send an alert message to the server
            
            byte[] message = "Alert: new client online".getBytes();

            DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);

            aSocket.send(request); 

            //Retrieve our reply
            
            while (true) {
                
                //Create buffer

                byte[] buffer = new byte[1000];
                
                //Get the reply from the server of the number we need to check

                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

                aSocket.receive(reply);
                
                //Convert the reply to a long
                
                long number = Long.parseLong(new String(reply.getData()).replaceAll("[\\D]", ""));
                
                //Check the number

                String output = String.valueOf(number) + isPrime(number);
                
                System.out.println(output);
                
                //Return out results to the server

                message = output.getBytes();

                request = new DatagramPacket(message, message.length, aHost, serverPort);

                aSocket.send(request); 

            }

        } catch (SocketException e){

            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e){

            System.out.println("IO: " + e.getMessage());

        } finally { 

            if(aSocket != null) aSocket.close();

        } 

    }
    
    private static boolean isPrime(long n) {

        if (n <= 1) {
            
            return false;
            
        }

        for (int i = 2; i < n; i++) {
            
            if (n % i == 0) {
                
                return false;
                
            }
        
        }
  
        return true;
        
    }
 
}
