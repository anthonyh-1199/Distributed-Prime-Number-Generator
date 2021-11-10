/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class PrimeNumberFinderServer {
    
    //Create variable for writing output
    
    static Writer writer;
    
    public static void main(String args[]){ 

        //Create variable to track for dropped packets
        
        long number_index = getMostRecentNumber() + 2;
        
        //Create a buffer to track the amount of checks on the current number
        
        int number_buffer = 0;

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

                //Check the contents of the message

                try {

                    //First, we extract the number from our message to see if it's relevant

                    long client_number = Long.parseLong(clientMessage.replaceAll("[^0-9]", ""));

                    if (client_number == number_index) {

                        //Next we parse a boolean value from our message

                        boolean client_result = Boolean.parseBoolean(clientMessage.replaceAll("\\d",""));

                        //Then we update the buffer according to the result

                        if (client_result) {

                            number_buffer++;

                        } else {

                            number_buffer--;

                        }

                    }

                } catch (NumberFormatException e) {

                    //Message was not formatted correctly, discarded

                    System.out.println("Warning: invalid message received");

                }
                
                //Check if our buffer is complete and ready to move to the next value

                if (Math.abs(number_buffer) > 3) {

                    //Check if number was determined to be prime

                    if (number_buffer > 0) {

                        //Write the number to the list of prime numbers

                        writeToFile(clientMessage.replaceAll("[^0-9]", ""));

                    }

                    //Clear out the buffer

                    number_buffer = 0;

                    //Increment our number index

                    if (number_index > 2) {

                        number_index++;

                    }

                    number_index++;

                }

                //Reply to the client with the number for it to check
                
                byte[] message = String.valueOf(number_index).getBytes();
                
                DatagramPacket reply = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());

                aSocket.send(reply);

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

            System.out.println("Error reading file");
            
        } finally {

            try {
                
                if (input != null) {
                    
                    input.close();
                    
                }
                
            } catch (IOException e) {
                
                System.out.println("Error closing input");
                
            }
            
        }
        
        return Long.parseLong(lastLine, 10);
        
    }

}