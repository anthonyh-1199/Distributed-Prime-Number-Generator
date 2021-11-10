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
                
                //Create buffer and output variable

                byte[] buffer = new byte[1000];
                
                String output;
                
                //Get the reply from the server of the number we need to check

                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

                aSocket.receive(reply);
                
                //Convert the reply to a long
                
                long number = Long.parseLong(new String(reply.getData()).replaceAll("[\\D]", ""));
                
                //If the number is prime, return it
                
                if (isPrime(number)) {
                    
                    output = String.valueOf(number) + "true";
                    
                } else {
                    
                    output = String.valueOf(number) + "false";
                    
                }

                //System.out.println(output);
                
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

        if (n == 2 || n == 3) {
            
            return true;
            
        }

        if (n <= 1 || n % 2 == 0 || n % 3 == 0) {
            
            return false;
            
        }

        for (int i = 5; i * i <= n; i += 6) {
            
            if (n % i == 0 || n % (i + 2) == 0) {
                
                return false;
            
            }
            
        }

        return true;

    }
 
}
