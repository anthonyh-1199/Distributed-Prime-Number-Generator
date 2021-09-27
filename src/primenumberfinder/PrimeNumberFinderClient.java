/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;

public class PrimeNumberFinderClient {
    
    //Initialize variables

    static Socket socket = null;

    static DataInputStream in = null;

    static DataOutputStream out = null;

    public static void main (String args[]) {

        //Alert the server that the client is online
        
        connectToServer();
        
        //Continuously receive and check numbers from the server
        
        while (true) {

            checkNumber();

        }
        
    }

    private static void checkNumber() {
        
        try {

            //Receive number from the server

            long number = Long.parseLong(in.readUTF().replaceAll("[\\D]", ""));

            //Check if the number is prime
            
            String output = String.valueOf(number) + isPrime(number);

            //Send our answer back to the server

            out.writeUTF(output);

        }  catch (IOException e) {

            System.out.println("IO:" + e.getMessage());

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
    
    private static void connectToServer() {
        
        try {
            
            //Connect to the server
            
            int serverPort = 7896;

            socket = new Socket("localhost", serverPort);
            
            //Assign input and output stream variables
            
            in = new DataInputStream(socket.getInputStream());
            
            out = new DataOutputStream(socket.getOutputStream());
            
            //Send an alert message to the server
            
            out.writeUTF("Alert: new client online");
            
        } catch (UnknownHostException e){

            System.out.println("Sock:" + e.getMessage()); 

        } catch (EOFException e){

            System.out.println("EOF:" + e.getMessage());

        } catch (IOException e){

            System.out.println("IO:" + e.getMessage());

        } finally {

            if(socket != null) try {

                socket.close();

            } catch (IOException e){

                /*close failed*/

            }

        }
        
    }

}
